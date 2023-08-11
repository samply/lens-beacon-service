package de.samply.lens_beacon_service.entrytype.datasets;

import de.samply.lens_beacon_service.entrytype.EntryType;
import de.samply.lens_beacon_service.entrytype.datasets.ast2filter.DatasetsAstNodeListConverter;

public class DatasetsEntryType extends EntryType {
    public DatasetsEntryType() {
        this("/datasets", "POST");
    }

    public DatasetsEntryType(String uri, String method) {
        super(uri, method);
        astNodeListConverter = new DatasetsAstNodeListConverter();
        query = new DatasetsQuery();
        groupAdmin = new DatasetsGroupAdmin();
    }
}
