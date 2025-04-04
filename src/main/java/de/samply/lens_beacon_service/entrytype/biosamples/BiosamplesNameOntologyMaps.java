package de.samply.lens_beacon_service.entrytype.biosamples;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Lens term names onto Ontology IDs understood by Beacon.
 */
public class BiosamplesNameOntologyMaps {
    // Maps the standard names for sample types onto NCIT codes
    public static Map<String, String> biosmapleTypeUberon;
    static {
        biosmapleTypeUberon = new HashMap<String, String>();

        biosmapleTypeUberon.put("blood", "UBERON:0000178");
        biosmapleTypeUberon.put("blood-serum", "UBERON:0001977");
        biosmapleTypeUberon.put("blood-plasma", "UBERON:0001969");
        biosmapleTypeUberon.put("lymph", "UBERON:0002391");
    }

    // Maps the standard names for sample status onto EFO codes
    public static Map<String, String> biosmapleStatusEfo;
    static {
        biosmapleStatusEfo = new HashMap<String, String>();

        biosmapleStatusEfo.put("Reference sample", "EFO:0009654");
        biosmapleStatusEfo.put("Abnormal sample", "EFO:0009655");
    }
}
