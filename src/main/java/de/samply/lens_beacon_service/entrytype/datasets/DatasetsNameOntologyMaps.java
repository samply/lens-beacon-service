package de.samply.lens_beacon_service.entrytype.datasets;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Lens term names onto Ontology IDs understood by Beacon.
 */
public class DatasetsNameOntologyMaps {
    // Maps the standard names for library source onto GENEPIO codes
    public static Map<String, String> dataUseDuo;
    static {
        dataUseDuo = new HashMap<String, String>();

        dataUseDuo.put("Publication required", "DUO:0000019");
        dataUseDuo.put("General research use", "DUO:0000042");
        dataUseDuo.put("User specific restriction", "DUO:0000026");
        dataUseDuo.put("Institution specific", "DUO:0000028");
    }
}
