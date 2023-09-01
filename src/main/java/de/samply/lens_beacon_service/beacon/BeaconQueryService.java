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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

//import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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
public abstract class BeaconQueryService {
    /**
     * Set up Beacon querying service.
     *
     * @param siteUrl URL of Beacon 2 site.
     * @param query   Query object that will be converted to JSON and sent to the site. Filters will be
     *                added to a clone of this object as necessary by the postQuery method.
     */
    public BeaconQueryService(String siteUrl, String proxyUrl, String proxyPort, BeaconQuery query) {
        init(siteUrl, proxyUrl, proxyPort, query);
    }

    protected abstract void init(String siteUrl, String proxyUrl, String proxyPort, BeaconQuery query);

    public abstract Integer runBeaconEntryTypeQueryAtSite(EntryType entryType, List<BeaconFilter> beaconFilters);

    public abstract Integer runFilterQueryAtSite(EntryType entryType,
                                        String filterName,
                                        String filterValue);
    }
