package de.samply.lens_beacon_service.site;

import de.samply.lens_beacon_service.beacon.model.GranularityLcBeaconQuery;
import de.samply.lens_beacon_service.entrytype.biosamples.BiosamplesEntryType;
import de.samply.lens_beacon_service.entrytype.genomicVariations.GenomicVariationsEntryType;
import de.samply.lens_beacon_service.entrytype.individuals.IndividualsEntryType;
import de.samply.lens_beacon_service.entrytype.cohorts.CohortsEntryType;
import de.samply.lens_beacon_service.entrytype.runs.RunsEntryType;
import de.samply.lens_beacon_service.entrytype.datasets.DatasetsEntryType;
import de.samply.lens_beacon_service.entrytype.analyses.AnalysesEntryType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HdCinecaSite extends Site {
    protected void init() {
        name = "HD Cineca";
        url = "http://beacon:5050/api";
        query = new GranularityLcBeaconQuery();
        entryTypes.add(new IndividualsEntryType("/individuals/", "POST")); // Error 380 w/o trailing slash
        entryTypes.add(new BiosamplesEntryType("/biosamples/", "POST")); // Error 380 w/o trailing slash
        entryTypes.add(new CohortsEntryType("/cohorts/", "POST")); // Error 380 w/o trailing slash
        entryTypes.add(new RunsEntryType("/runs/", "POST")); // Error 380 w/o trailing slash
        entryTypes.add(new DatasetsEntryType("/datasets/", "POST")); // Error 380 w/o trailing slash
        entryTypes.add(new AnalysesEntryType("/analyses/", "POST")); // Error 380 w/o trailing slash
        entryTypes.add(new GenomicVariationsEntryType("/g_variants/", "POST")); // Error 380 w/o trailing slash
        log.info("HdCinecaSite: name: " + name);
    }
}
