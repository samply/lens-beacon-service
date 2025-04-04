package de.samply.lens_beacon_service.site;

import de.samply.lens_beacon_service.beacon.model.GranularityUcBeaconQuery;
import de.samply.lens_beacon_service.entrytype.biosamples.BiosamplesEntryType;
import de.samply.lens_beacon_service.entrytype.genomicVariations.GenomicVariationsEntryType;
import de.samply.lens_beacon_service.entrytype.individuals.IndividualsEntryType;
import de.samply.lens_beacon_service.entrytype.cohorts.CohortsEntryType;
import de.samply.lens_beacon_service.entrytype.runs.RunsEntryType;
import de.samply.lens_beacon_service.entrytype.datasets.DatasetsEntryType;
import de.samply.lens_beacon_service.entrytype.analyses.AnalysesEntryType;

public class EgaCinecaSite extends Site {
    protected void init() {
        name = "EGA Cineca";
        url = "https://ega-archive.org/beacon-apis/cineca";
        query = new GranularityUcBeaconQuery();
        entryTypes.add(new IndividualsEntryType());
        entryTypes.add(new BiosamplesEntryType());
        entryTypes.add(new CohortsEntryType());
        entryTypes.add(new RunsEntryType());
        entryTypes.add(new DatasetsEntryType());
        entryTypes.add(new AnalysesEntryType());
        entryTypes.add(new GenomicVariationsEntryType());
    }
}
