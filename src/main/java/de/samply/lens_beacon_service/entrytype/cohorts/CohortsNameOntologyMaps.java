package de.samply.lens_beacon_service.entrytype.cohorts;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Lens term names onto Ontology IDs understood by Beacon.
 */
public class CohortsNameOntologyMaps {
    // Maps the standard names for genders onto NCIT codes
    public static Map<String, String> genderNameNcit;
    static {
        genderNameNcit = new HashMap<String, String>();

        genderNameNcit.put("female", "NCIT:C16576");
        genderNameNcit.put("male", "NCIT:C20197");
    }

    // Maps the standard names for geographic origin onto GAZ codes
    public static Map<String, String> geographicOriginGaz;
    static {
        geographicOriginGaz = new HashMap<String, String>();

        geographicOriginGaz.put("Wales", "GAZ:00002640");
        geographicOriginGaz.put("England", "GAZ:00002641");
        geographicOriginGaz.put("Scotland", "GAZ:00002639");
        geographicOriginGaz.put("Republic of Ireland", "GAZ:00004018");
        geographicOriginGaz.put("Northern Ireland", "GAZ:00002638");
    }
}
