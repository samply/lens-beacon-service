package de.samply.lens_beacon_service.site;

import de.samply.lens_beacon_service.GlobalVariables;
import de.samply.lens_beacon_service.beacon.model.GranularityLcBeaconQuery;
import de.samply.lens_beacon_service.entrytype.analyses.AnalysesEntryType;
import de.samply.lens_beacon_service.entrytype.biosamples.BiosamplesEntryType;
import de.samply.lens_beacon_service.entrytype.cohorts.CohortsEntryType;
import de.samply.lens_beacon_service.entrytype.datasets.DatasetsEntryType;
import de.samply.lens_beacon_service.entrytype.genomicVariations.GenomicVariationsEntryType;
import de.samply.lens_beacon_service.entrytype.individuals.IndividualsEntryType;
import de.samply.lens_beacon_service.entrytype.runs.RunsEntryType;
import lombok.extern.slf4j.Slf4j;

/**
 * Uses Beam-Connect to access a Beacon behind a firewall.
 *
 * Beam-Connect is a proxy, so we need to give the relevant URL and port.
 */
@Slf4j
public class GdiStarterKitBeamConnectSite extends Site {
    protected void init() {
        name = "GDI Starter Kit, firewalled";
        url = "http://beacon:5050/api";
        query = new GranularityLcBeaconQuery();
        proxyUrl = GlobalVariables.configuration.getProxyUrl(); // URL of Beam-Connect, see docker-compose.yml
        proxyPort = GlobalVariables.configuration.getProxyPort();
        proxyApiKey = GlobalVariables.configuration.getProxyApiKey();
        entryTypes.add(new IndividualsEntryType());
        //entryTypes.add(new BiosamplesEntryType());
        //entryTypes.add(new CohortsEntryType());
        //entryTypes.add(new RunsEntryType());
        //entryTypes.add(new DatasetsEntryType());
        //entryTypes.add(new AnalysesEntryType());
        //entryTypes.add(new GenomicVariationsEntryType());
    }
}
