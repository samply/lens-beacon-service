package de.samply.lens_beacon_service.entrytype.runs;

import de.samply.lens_beacon_service.entrytype.EntryType;
import de.samply.lens_beacon_service.entrytype.runs.ast2filter.RunsAstNodeListConverter;

public class RunsEntryType extends EntryType {
    public RunsEntryType() {
        this("/runs", "POST");
    }

    public RunsEntryType(String uri, String method) {
        super(uri, method);
        astNodeListConverter = new RunsAstNodeListConverter();
        query = new RunsQuery();
        groupAdmin = new RunsGroupAdmin();
    }
}
