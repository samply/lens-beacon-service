package de.samply.lens_beacon_service.entrytype.analyses;

import de.samply.lens_beacon_service.entrytype.EntryType;

public class AnalysesEntryType extends EntryType {
    public AnalysesEntryType() {
        this("/analyses", "POST");
    }

    public AnalysesEntryType(String uri, String method) {
        super(uri, method);
        astNodeListConverter = null;
        query = new AnalysesQuery();
        groupAdmin = new AnalysesGroupAdmin();
    }
}
