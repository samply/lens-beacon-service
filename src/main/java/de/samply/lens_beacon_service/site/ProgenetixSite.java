package de.samply.lens_beacon_service.site;

import de.samply.lens_beacon_service.beacon.model.GranularityUcBeaconQuery;
import de.samply.lens_beacon_service.entrytype.biosamples.BiosamplesEntryType;
import de.samply.lens_beacon_service.entrytype.genomicVariations.GenomicVariationsEntryType;
import de.samply.lens_beacon_service.entrytype.individuals.IndividualsEntryType;
import de.samply.lens_beacon_service.entrytype.cohorts.CohortsEntryType;
import de.samply.lens_beacon_service.entrytype.runs.RunsEntryType;
import de.samply.lens_beacon_service.entrytype.datasets.DatasetsEntryType;

public class ProgenetixSite extends Site {
    public ProgenetixSite() {
        name = "Progenetix";
        url = "https://progenetix.org/beacon";
        query = new GranularityUcBeaconQuery();
        entryTypes.add(new IndividualsEntryType());
        entryTypes.add(new CohortsEntryType());
        entryTypes.add(new RunsEntryType());
        entryTypes.add(new DatasetsEntryType());
        entryTypes.add(new BiosamplesEntryType());
        entryTypes.add(new GenomicVariationsEntryType());
        init();
    }
}
