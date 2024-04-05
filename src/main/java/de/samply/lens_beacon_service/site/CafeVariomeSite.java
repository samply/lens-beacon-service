package de.samply.lens_beacon_service.site;

import de.samply.lens_beacon_service.beacon.model.GranularityUcBeaconQuery;
import de.samply.lens_beacon_service.entrytype.analyses.AnalysesEntryType;
import de.samply.lens_beacon_service.entrytype.cohorts.CohortsEntryType;
import de.samply.lens_beacon_service.entrytype.datasets.DatasetsEntryType;
import de.samply.lens_beacon_service.entrytype.genomicVariations.GenomicVariationsEntryType;
import de.samply.lens_beacon_service.entrytype.individuals.IndividualsEntryType;
import de.samply.lens_beacon_service.entrytype.runs.RunsEntryType;

public class CafeVariomeSite extends Site {
    protected void init() {
        name = "CafeVariome";
        url = "https://beaconv2.cafevariome.org/";
        query = new GranularityUcBeaconQuery();
        entryTypes.add(new IndividualsEntryType());
        entryTypes.add(new CohortsEntryType());
        entryTypes.add(new RunsEntryType());
        entryTypes.add(new DatasetsEntryType());
        entryTypes.add(new DatasetsEntryType());
        entryTypes.add(new AnalysesEntryType());
        entryTypes.add(new GenomicVariationsEntryType());
    }
}
