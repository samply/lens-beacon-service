package de.samply.lens_beacon_service.entrytype.runs;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Lens term names onto Ontology IDs understood by Beacon.
 */
public class RunsNameOntologyMaps {
    // Maps the standard names for library source onto GENEPIO codes
    public static Map<String, String> genomicSourceGenepio;
    static {
        genomicSourceGenepio = new HashMap<String, String>();

        genomicSourceGenepio.put("Genomic Source", "GENEPIO:0001966");
        genomicSourceGenepio.put("Metagenomic Source", "GENEPIO:0001965");
    }

    // Maps the standard names for platform model onto OBI codes
    public static Map<String, String> platformModelObi;
    static {
        platformModelObi = new HashMap<String, String>();

        platformModelObi.put("DNA sequencer", "OBI:0400103");
        platformModelObi.put("Illumina HiSeq 2000", "OBI:0002048");
        platformModelObi.put("Oxford Nanopore MinION", "OBI:000275");
        platformModelObi.put("Large-insert clone DNA microarray", "EFO:0010938");
    }
}
