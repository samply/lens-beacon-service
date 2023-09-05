package de.samply.lens_beacon_service.beacon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.samply.lens_beacon_service.Utils;
import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.beacon.model.BeaconQuery;
import de.samply.lens_beacon_service.beacon.model.BeaconRequest;
import de.samply.lens_beacon_service.beacon.model.BeaconResponse;
import de.samply.lens_beacon_service.entrytype.EntryType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    private BeaconQuery query; // BeaconQuery without filters.

    private String siteUrl; // URL of Beacon 2 site.

    private String proxyUrl;
    private String proxyPort;
    private String proxyApiKey;
    private CloseableHttpClient httpClient;

    /**
     * Set up Beacon querying service.
     *
     * @param siteUrl   URL of Beacon 2 site.
     * @param proxyUrl
     * @param proxyPort
     * @param query     Query object that will be converted to JSON and sent to the site. Filters will be
     *                  added to a clone of this object as necessary by the postQuery method.
     */
    public BeaconQueryService(String siteUrl, String proxyUrl, String proxyPort, String proxyApiKey, BeaconQuery query) {
        this.siteUrl = siteUrl;
        this.proxyUrl = cleanProxyUrl(proxyUrl);
        this.proxyPort = proxyPort;
        this.query = query;
        log.info("BeaconQueryService: siteUrl: " + siteUrl);
        log.info("BeaconQueryService: proxyUrl: " + proxyUrl);
        log.info("BeaconQueryService: proxyPort: " + proxyPort);
        log.info("BeaconQueryService: proxyApiKey: " + proxyApiKey);
        if (proxyUrl != null && proxyPort != null) {
            log.info("BeaconQueryService: setting proxy and API key");
            // Configure proxy settings
            HttpHost proxy = new HttpHost(proxyUrl, Integer.parseInt(proxyPort));

            // Create HTTP client with proxy configuration
            httpClient = HttpClients.custom()
                    .setProxy(proxy)
                    .setDefaultHeaders(Collections.singletonList(
                            new BasicHeader("Proxy-Authorization", createProxyApiKeyString(proxyApiKey))))
                    .build();
        } else
            httpClient = HttpClients
                    .custom()
                    .build();
    }

    private String createProxyApiKeyString(String proxyApiKey) {
        return "ApiKey lensbeacon-connect.lensbeaconde.broker.bbmri.samply.de " + proxyApiKey;
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
            if (response != null)
                count = response.getCount();
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
            log.info("postQuery: uri: " + entryType.beaconEndpoint.uri);
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
        BeaconResponse beaconResponse = null;
        try {
            // Create GET request
            HttpGet httpGet = new HttpGet(buildUrl(siteUrl, uri));

            // Send the request
            CloseableHttpResponse response = httpClient.execute(httpGet);

            // Obtain the JSON response
            HttpEntity responseEntity = response.getEntity();
            String jsonResponse = EntityUtils.toString(responseEntity);

            // Parse JSON into Java object using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            beaconResponse = objectMapper.readValue(jsonResponse, BeaconResponse.class);

//        // Close the client and release resources
//        httpClient.close();
        } catch (Exception e) {
            log.info(Utils.traceFromException(e));
        }

        return beaconResponse;
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
        BeaconResponse beaconResponse = null;
        try {
            log.info("postQuery: connect to " + siteUrl);
            log.info("postQuery: proxyUrl: " + proxyUrl + ", proxyPort: " + proxyPort);
            // Create POST request
            HttpPost httpPost = new HttpPost(buildUrl(siteUrl, uri));

            // Set JSON request body
            StringEntity entity = new StringEntity(jsonBeaconRequest, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);

            // Send the request
            CloseableHttpResponse response = httpClient.execute(httpPost);

            // Obtain the JSON response
            HttpEntity responseEntity = response.getEntity();
            String jsonResponse = EntityUtils.toString(responseEntity);

            // Parse JSON into Java object using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            beaconResponse = objectMapper.readValue(jsonResponse, BeaconResponse.class);

//            // Close the client and release resources
//            httpClient.close();
        } catch (Exception e) {
            log.info(Utils.traceFromException(e));
         }

        return beaconResponse;
    }

    private String buildUrl(String siteUrl, String uri) {
        String strippedSiteUrl = siteUrl;
        if (siteUrl.endsWith("/"))
            strippedSiteUrl = siteUrl.substring(0, siteUrl.length() - 1);

        String strippedUri = uri;
        if (uri.startsWith("/"))
            strippedUri = uri.substring(1, uri.length());

        return strippedSiteUrl + "/" + strippedUri;
    }

    private String cleanProxyUrl(String proxyUrl) {
        if (proxyUrl == null)
            return null;

        String cleanProxyUrl = proxyUrl;

        if (proxyUrl.endsWith("/"))
            cleanProxyUrl = proxyUrl.substring(0, proxyUrl.length() - 1);

        if (cleanProxyUrl.startsWith("http://"))
            cleanProxyUrl = cleanProxyUrl.substring(7, cleanProxyUrl.length());

        if (cleanProxyUrl.startsWith("https://"))
            cleanProxyUrl = cleanProxyUrl.substring(8, cleanProxyUrl.length());

        return cleanProxyUrl;
    }
}
