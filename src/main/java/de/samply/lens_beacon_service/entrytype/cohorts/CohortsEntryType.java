package de.samply.lens_beacon_service.entrytype.cohorts;

import de.samply.lens_beacon_service.entrytype.EntryType;
import de.samply.lens_beacon_service.entrytype.cohorts.ast2filter.CohortsAstNodeListConverter;

public class CohortsEntryType extends EntryType {
    public CohortsEntryType() {
        this("/cohorts", "POST");
    }

    public CohortsEntryType(String uri, String method) {
        super(uri, method);
        astNodeListConverter = new CohortsAstNodeListConverter();
        query = new CohortsQuery();
        groupAdmin = new CohortsGroupAdmin();
    }
}
