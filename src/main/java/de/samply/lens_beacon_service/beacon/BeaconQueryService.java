package de.samply.lens_beacon_service.beacon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.samply.lens_beacon_service.entrytype.EntryType;
import de.samply.lens_beacon_service.Utils;
import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.beacon.model.BeaconQuery;
import de.samply.lens_beacon_service.beacon.model.BeaconRequest;
import de.samply.lens_beacon_service.beacon.model.BeaconResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Send queries to the Beacon API. Both GET and POST requests are possible.
 *
 * The main things here are the URI and the filter.
 *
 * The URI specifies the endpoint we are talking to, e,g, "individuals/".
 *
 * The filter is the thing that one would generally understand as a query. It
 * generally comprises of a resource name (e.g. Omim), an ID, and possibly
 * a matching value. It is packed in JSON.
 *
 * Only the POST endpoints work with filters sent in the request body.
 */

@Slf4j
public class BeaconQueryService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(30);
    private BeaconQuery query; // BeaconQuery without filters.

    private WebClient webClient; // Talks directly to site.
    private String siteUrl; // URL of Beacon 2 site.

    /**
     * Set up Beacon querying service.
     *
     * @param siteUrl URL of Beacon 2 site.
     * @param query Query object that will be converted to JSON and sent to the site. Filters will be
     *              added to a clone of this object as necessary by the postQuery method.
     */
    public BeaconQueryService(String siteUrl, BeaconQuery query) {
        this.siteUrl = siteUrl;
        this.query = query;
        this.webClient = WebClient.builder().baseUrl(siteUrl).build();
    }

    /**
     * Takes the supplied filter set, and adds an extra filter based on filterName and filterValue.
     * Runs a query constrained by this expanded filter set.
     *
     * The original filter set (beaconEndpoint.baseFilters) is not modified, i.e. this method has no sideeffects.
     *
     * @param entryType
     * @param filterName
     * @param filterValue
     * @return
     */
    public Integer runFilterQueryAtSite(EntryType entryType,
                                        String filterName,
                                        String filterValue) {
        List<BeaconFilter> localFilters = new ArrayList<BeaconFilter>(entryType.baseFilters); // Clone filters
        localFilters.add(new BeaconFilter(filterName, filterValue));
        Integer count = runBeaconEntryTypeQueryAtSite(entryType, localFilters);

        return count;
    }

    /**
     * Runs a Beacon query at the site defined by beaconQueryService, using the endpoint defined
     * by beaconEndpoint.
     *
     * The supplied filters will be used to constrain the query.
     *
     * Returns a count of the number of matching hits. If no count can be found, return -1.
     * Possible reasons for this are that the site does not present the endpoint for the
     * entry type or an error has occurred.
     *
     * @param entryType
     * @param beaconFilters
     * @return
     */
    public Integer runBeaconEntryTypeQueryAtSite(EntryType entryType,
                                                 List<BeaconFilter> beaconFilters) {
        Integer count = -1;
        try {
            BeaconResponse response = query(entryType, beaconFilters);
            if (response != null) {
                count = response.getCount();
                if (entryType.beaconEndpoint.getEntryType().equals("cohorts")) {
                    log.info("");
                    log.info("");
                    log.info("");
                    log.info("");
                    log.info("\nPOST Full URL: " + siteUrl + entryType.beaconEndpoint.uri);
                    log.info("\nfilters: " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(beaconFilters));
                    log.info("response: " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
                    log.info("count: " + count);
                    log.info("");
                    log.info("");
                    log.info("");
                    log.info("");
                }
            }
        } catch (Exception e) {
            log.error("runQuery: problem with " + entryType.beaconEndpoint.getEntryType() + ", trace: " + Utils.traceFromException(e));
        }
        return count;
    }

    /**
     * Run a query against a Beacon endpoint. If the entry type specifies POST, then the supplied
     * filters will also be applied. Otherwise, a GET request will be assumed. In this latter case,
     * no filtering will be applied, because Beacon GET endpoints don't accept filters in their
     * bodies.
     *
     * @param entryType
     * @param filters
     * @return
     */
    private BeaconResponse query(EntryType entryType, List<BeaconFilter> filters) {
        if (entryType == null)
            return null;
        try {
            if (entryType.beaconEndpoint.method.equals("POST"))
                return postQuery(entryType.beaconEndpoint.uri, filters);
            else
                return getQuery(entryType.beaconEndpoint.uri);
        } catch (Exception e) {
            log.error(Utils.traceFromException(e));
            try {
                log.error("\nfilters: " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(filters));
            } catch (JsonProcessingException ex) {
                log.error(Utils.traceFromException(ex));
                return null;
            }
            return null;
        }
    }

    /**
     * Get all objects of a given type known to Beacon.
     *
     * Use a GET request.
     *
     * @param uri The URI of the objects be queried, e.g. "individuals" or "cohorts".
     * @return JSON-format list of objects of a given type.
     */
    private BeaconResponse getQuery(String uri) {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(BeaconResponse.class)
                .block(REQUEST_TIMEOUT);
    }

    /**
     * Get a filtered count of objects of a given type know to Beacon.
     *
     * Use a POST request.
     *
     * @param uri The URI of the objects be queried, e.g. "individuals" or "cohorts".
     * @param beaconFilters Filters that will be applied to the query.
     * @return JSON-format list of objects of a given type.
     */
    private BeaconResponse postQuery(String uri, List<BeaconFilter> beaconFilters) {
        String jsonBeaconRequest = (new BeaconRequest(query.clone(beaconFilters))).toString();
        return webClient
                .post()
                .uri(uri)
                .bodyValue(jsonBeaconRequest)
                .retrieve()
                .bodyToMono(BeaconResponse.class)
                .block(REQUEST_TIMEOUT);
    }
}