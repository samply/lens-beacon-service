package de.samply.lens_beacon_service.site;

import de.samply.lens_beacon_service.beacon.model.GranularityUcBeaconQuery;
import de.samply.lens_beacon_service.entrytype.biosamples.BiosamplesEntryType;
import de.samply.lens_beacon_service.entrytype.genomicVariations.GenomicVariationsEntryType;
import de.samply.lens_beacon_service.entrytype.individuals.IndividualsEntryType;

public class MolgenisMutationsSite extends Site {
    protected void init() {
        name = "Molgenis mutations";
        url = "https://mutatiedatabases.molgeniscloud.org/api/beacon";
        query = new GranularityUcBeaconQuery();
        entryTypes.add(new IndividualsEntryType());
        entryTypes.add(new BiosamplesEntryType("/biosamples", "GET")); // Uses GET, deviates from Beacon 2 standard
        entryTypes.add(new GenomicVariationsEntryType("/g_variants", "GET")); // Uses GET, deviates from Beacon 2 standard
    }
}
