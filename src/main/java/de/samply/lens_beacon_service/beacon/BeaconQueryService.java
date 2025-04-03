package de.samply.lens_beacon_service.beacon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.samply.lens_beacon_service.util.Utils;
import de.samply.lens_beacon_service.beacon.model.*;
import de.samply.lens_beacon_service.entrytype.EntryType;
import de.samply.lens_beacon_service.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    public String siteUrl; // URL of Beacon 2 site.

    public String proxyUrl;
    private String proxyPort;
    private String proxyApiKey;
    private CloseableHttpClient httpClient;

    // Keep track of query timings. The key is the entry type (Beacon endpoint).
    // The value is an EntryTimings object, that is used to store the timing of
    // the primary query and the stratifiers.
    private Map<String,EntryTimings> timings;

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
        this.proxyApiKey = proxyApiKey;
        this.query = query;
        if (proxyUrl != null && proxyPort != null) {
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

        // Initialize query timings.
        timings  = new HashMap<String,EntryTimings>();
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
        List<BeaconSearchParameters> localFilters = new ArrayList<BeaconSearchParameters>(entryType.baseFilters); // Clone filters
        BeaconSearchParameters filter = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.FILTER);
        filter.addParameter(filterName, filterValue);
        localFilters.add(filter);
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
                                                 List<BeaconSearchParameters> beaconFilters) {
        if (entryType == null)
            log.warn("runQuery: entryType == null");

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
     * @return Deserialized response to query. Return null if something goes wrong.
     */
    private BeaconResponse query(EntryType entryType, List<BeaconSearchParameters> filters) {
        if (entryType == null) {
            log.warn("entryType == null");
            return null;
        }
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
     * Extracts filters from a list of BeaconSearchParameters.
     *
     * @param parametersList The list of BeaconSearchParameters to extract filters from.
     * @return A new list of BeaconSearchParameters containing only the filter parameters.
     */
    private List<BeaconSearchParameters> extractFilters(List<BeaconSearchParameters> parametersList) {
        List<BeaconSearchParameters> filters = new ArrayList<BeaconSearchParameters>();
        for (BeaconSearchParameters parameters: parametersList)
            if (parameters.getType() == BeaconSearchParameters.ParameterBlockType.FILTER)
                filters.add(parameters);

        return filters;
    }

    /**
     * Extracts request parameters from a list of BeaconSearchParameters.
     *
     * @param parametersList The list of BeaconSearchParameters to extract request parameters from.
     * @return A new BeaconSearchParameters object containing the merged request parameters.
     */
    private BeaconSearchParameters extractRequestParameters(List<BeaconSearchParameters> parametersList) {
        BeaconSearchParameters requestParameters = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.REQUEST_PARAMETER);
        for (BeaconSearchParameters parameters: parametersList)
            if (parameters.getType() == BeaconSearchParameters.ParameterBlockType.REQUEST_PARAMETER)
                requestParameters.merge(parameters);

        return requestParameters;
    }

    /**
     * Extracts the URI extension from a list of BeaconSearchParameters.
     *
     * @param parametersList The list of BeaconSearchParameters to extract the URI extension from.
     * @return The URI extension as a String, or null if not found.
     */
    private String extractUriExtension(List<BeaconSearchParameters> parametersList) {
        String uriExtension = null;
        for (BeaconSearchParameters parameters: parametersList)
            if (parameters.getType() == BeaconSearchParameters.ParameterBlockType.URI_EXTENSION) {
                uriExtension = parameters.getAnonymousStringParameter();
                break;
            }

        return uriExtension;
    }

    /**
     * Get a filtered count of objects of a given type know to Beacon.
     * <p>
     * Use a POST request.
     *
     * @param uri                   The URI of the objects be queried, e.g. "individuals" or "cohorts".
     * @param searchParameters      Search parameters that will be applied to the query.
     * @return JSON-format list of objects of a given type.
     */
    private BeaconResponse postQuery(String uri, List<BeaconSearchParameters> searchParameters) {
        // Extract filters, requestParameters and the URI extension from
        // the searchParameters.
        // Use them to construct the request body that will be sent to Beacon
        // and work out the final URL that will be employed.
        String jsonBeaconRequest = (new BeaconRequest(query.clone(extractFilters(searchParameters), extractRequestParameters(searchParameters)))).toString();
        String uriExtension = extractUriExtension(searchParameters);

        BeaconResponse beaconResponse = null;
        try {
            String url = buildUrl(siteUrl, uri, uriExtension);
            StringEntity entity = new StringEntity(jsonBeaconRequest, ContentType.APPLICATION_JSON);

            // Create POST request
            HttpPost httpPost = new HttpPost(url);

            // Set JSON request body
            httpPost.setEntity(entity);

            // Send the request
            CloseableHttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode < 200 || statusCode >= 300) {
                log.error("BeaconQueryService.postQuery: query FAILED, statusCode: " + statusCode);
                log.error("BeaconQueryService.postQuery: jsonBeaconRequest: " + jsonBeaconRequest);
                log.error("BeaconQueryService.postQuery: reasonPhrase: " + response.getStatusLine().getReasonPhrase());
                Header locationHeader = response.getFirstHeader("Location");
                if (locationHeader != null)
                    log.error("BeaconQueryService.postQuery: Location: " + locationHeader.getValue());
            }

            // Obtain the JSON response
            HttpEntity responseEntity = response.getEntity();
            String jsonResponse = EntityUtils.toString(responseEntity);

            // Parse JSON into Java object using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            beaconResponse = objectMapper.readValue(jsonResponse, BeaconResponse.class);

//            // Close the client and release resources
//            httpClient.close();
        } catch (Exception e) {
            log.error("postQuery: error running query");
            log.error("postQuery: searchParameters: " + JsonUtils.toJson(searchParameters));
            //log.error(Utils.traceFromException(e));
            String[] error = Utils.traceFromException(e).split("\n");
            if (error.length > 0)
                log.error("postQuery: error[0]: " + error[0]);
            if (error.length > 1)
                log.error("postQuery: error[1]: " + error[1]);
         }

        return beaconResponse;
    }

    private CloseableHttpResponse executeAtClient(String url, StringEntity entity, int count) throws IOException {
        // Create POST request
        HttpPost httpPost = new HttpPost(url);

        // Set JSON request body
        httpPost.setEntity(entity);

        // Send the request
        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode == 404 && count > 0) {
            try {
                //Thread.sleep(10);
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            return executeAtClient(url, entity, count - 1);
        } else
            return response;
    }

    private String buildUrl(String siteUrl, String uri, String uriExtension) {
        String url = buildUrl(siteUrl, uri);
        if (uriExtension != null) {
            url = buildUrl(url, uriExtension);
            if (uri.endsWith("/") && ! url.endsWith("/"))
                url = url + "/";
        }

        return url;
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

    /**
     * Retrieves the EntryTimings object for the given entry type. This object is used for storing
     * diverse information about timings, e.g. query timing, stratifier timings, etc.
     *
     * If the entry type is already present in the timings map, returns the corresponding EntryTimings object.
     * Otherwise, creates a new EntryTimings object, adds it to the timings map, and returns it.
     *
     * @param entryType The entry type for which to retrieve the EntryTimings object.
     * @return The EntryTimings object associated with the given entry type.
     */
    EntryTimings getEntryTimings(String entryType) {
        if (timings.containsKey(entryType))
            return timings.get(entryType);
        else {
            EntryTimings entryTimings = new EntryTimings();
            entryTimings.setEntryType(entryType);
            timings.put(entryType, entryTimings);
            return entryTimings;
        }
    }

    /**
     * Sets the query timing for the specified entry type. This is the timing associated with the
     * primary result of the query, i.e. the count (e.g. of patients).
     *
     * If the query timing is negative, logs a warning.
     *
     * @param entryType The entry type for which to set the query timing.
     * @param queryTiming The query timing value.
     */
    public void setQueryTiming(String entryType, Integer queryTiming) {
        if (queryTiming < 0)
            log.warn("queryTiming is negative for entryType: " + entryType);
        EntryTimings entryTimings = getEntryTimings(entryType);
        entryTimings.addQueryTiming(queryTiming);
    }

    /**
     * Adds the stratifier count and timing to the corresponding EntryTimings object
     * in the timings map.
     *
     * @param entryType The entry type for which to set the stratifier timing.
     * @param stratifierName The name of the stratifier.
     * @param stratifierCount The stratifier count value.
     * @param stratifierTiming The stratifier timing value.
     */
    public void setStratifierTimings(String entryType, String stratifierName, Integer stratifierCount, Integer stratifierTiming) {
        EntryTimings entryTimings = getEntryTimings(entryType);
        EntryTimings.TotalSumPair totalSumPair = entryTimings.getStratifierInfo(stratifierName);
        totalSumPair.add(stratifierCount, stratifierTiming);
    }

    public void setStratifierTiming(String entryType, Integer stratifierTiming) {
        EntryTimings entryTimings = getEntryTimings(entryType);
        //entryTimings.addTiming(stratifierTiming);
    }

    /**
     * Prints out the timings for each entry type in the timings map.
     *
     * @return cumulative timings for all entry types running on this service.
     */
    public List<EntryTimings.TotalSumPair> showTimings() {
        log.info("TIMINGS for " + siteUrl);
        EntryTimings.TotalSumPair cumulativeSumPair = new EntryTimings.TotalSumPair();
        List<EntryTimings.TotalSumPair> cumulativeSumPairs = new ArrayList<EntryTimings.TotalSumPair>();
        for (String entryType: timings.keySet()) {
            EntryTimings entryTimings = getEntryTimings(entryType);
            entryTimings.showTimings();
            EntryTimings.TotalSumPair sumPair = entryTimings.calculateOverallTiming();
            cumulativeSumPair.add(sumPair);
            cumulativeSumPairs.addAll(entryTimings.getOverallTimings());
        }
        log.info("    CUMULATIVE MEAN TIMING: " + cumulativeSumPair.getMean() + " ms");
        log.info("    CUMULATIVE VALUE COUNT: " + cumulativeSumPairs.size());

        return cumulativeSumPairs;
    }

    /**
     * Returns the map of entry types to EntryTimings objects.
     *
     * @return The map of entry types to EntryTimings objects.
     */
    public Map<String,EntryTimings> getTimings() {
        return timings;
    }
}
