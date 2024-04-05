package de.samply.lens_beacon_service.site;

import de.samply.lens_beacon_service.beacon.model.GranularityLcBeaconQuery;
import de.samply.lens_beacon_service.entrytype.analyses.AnalysesEntryType;
import de.samply.lens_beacon_service.entrytype.biosamples.BiosamplesEntryType;
import de.samply.lens_beacon_service.entrytype.cohorts.CohortsEntryType;
import de.samply.lens_beacon_service.entrytype.datasets.DatasetsEntryType;
import de.samply.lens_beacon_service.entrytype.genomicVariations.GenomicVariationsEntryType;
import de.samply.lens_beacon_service.entrytype.individuals.IndividualsEntryType;
import de.samply.lens_beacon_service.entrytype.runs.RunsEntryType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GdiStarterKitSite extends Site {
    protected void init() {
        name = "GDI Starter Kit";
        url = "http://beacon:5050/api";
        query = new GranularityLcBeaconQuery();
        entryTypes.add(new IndividualsEntryType());
        entryTypes.add(new BiosamplesEntryType());
        entryTypes.add(new CohortsEntryType());
        entryTypes.add(new RunsEntryType());
        entryTypes.add(new DatasetsEntryType());
        entryTypes.add(new AnalysesEntryType());
        entryTypes.add(new GenomicVariationsEntryType());
    }
}
