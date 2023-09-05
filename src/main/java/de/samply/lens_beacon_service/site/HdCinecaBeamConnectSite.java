package de.samply.lens_beacon_service.site;

import de.samply.lens_beacon_service.GlobalVariables;
import lombok.extern.slf4j.Slf4j;

/**
 * Uses Beam-Connect to access a Beacon behind a firewall.
 *
 * Beam-Connect is a proxy, so we need to give the relevant URL and port.
 */
@Slf4j
public class HdCinecaBeamConnectSite extends HdCinecaSite {
    protected void init() {
        super.init();
        name = "HD Cineca firewalled";
//        proxyUrl = "http://lensbeacon-beam-connect"; // URL of Beam-Connect, see docker-compose.yml
//        proxyUrl = "http://lensbeacon-beam-connect/"; // URL of Beam-Connect, see docker-compose.yml
        proxyUrl = GlobalVariables.configuration.getProxyUrl(); // URL of Beam-Connect, see docker-compose.yml
        proxyPort = GlobalVariables.configuration.getProxyPort();
        proxyApiKey = GlobalVariables.configuration.getProxyApiKey();
        log.info("HdCinecaBeamConnectSite: name: " + name);
    }
}
