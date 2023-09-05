package de.samply.lens_beacon_service.site;

import de.samply.lens_beacon_service.beacon.model.BeaconQuery;
import de.samply.lens_beacon_service.entrytype.EntryType;
import de.samply.lens_beacon_service.beacon.BeaconQueryService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Bundles together all the information known about a site hosting a Beacon API.
 *
 * This information includes a name that we can give the site, the URL of the
 * API, plus information about some of the endpoints (BeaconEndpoint).
 */
@Slf4j
public abstract class Site {
    public Site() {
        init();
        log.info("Site: name: " + name);
        log.info("Site: url: " + url);
        log.info("Site: proxyUrl: " + proxyUrl);
        log.info("Site: proxyPort: " + proxyPort);
        beaconQueryService = new BeaconQueryService(url, proxyUrl, proxyPort, proxyApiKey, query);
    }

    protected abstract void init();

    public String name; // Site name, e.g. "HD Cineca".

    public String url; // URL of site, e.g. "http://beacon:5050/api".
    public String proxyUrl = null;
    public String proxyPort = null;
    public String proxyApiKey = null;
    public BeaconQuery query; // Beacon query, minus the filters.
    public BeaconQueryService beaconQueryService; // Automatically derived from URL
    public List<EntryType> entryTypes = new ArrayList<EntryType>();
}
