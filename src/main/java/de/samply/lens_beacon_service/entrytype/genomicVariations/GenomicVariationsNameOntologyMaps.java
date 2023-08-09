package de.samply.lens_beacon_service.entrytype.genomicVariations;

import java.util.HashMap;
import java.util.Map;

/**
 * Map Lens term names onto Ontology IDs understood by Beacon.
 */
public class GenomicVariationsNameOntologyMaps {
    // Maps the standard names for glossary terms onto ENSGLOSSARY codes
    public static Map<String, String> variantNameEnsglossary;
    static {
        variantNameEnsglossary = new HashMap<String, String>();

        variantNameEnsglossary.put("Variant", "ENSGLOSSARY:0000092");
        variantNameEnsglossary.put("Sequence variant", "ENSGLOSSARY:0000096");
        variantNameEnsglossary.put("Structural variant", "ENSGLOSSARY:0000097");
        variantNameEnsglossary.put("Allele", "ENSGLOSSARY:0000106");
        variantNameEnsglossary.put("Short tandem repeat variant", "ENSGLOSSARY:0000131");
        variantNameEnsglossary.put("Splice acceptor variant", "ENSGLOSSARY:0000141");
        variantNameEnsglossary.put("Splice donor variant", "ENSGLOSSARY:0000142");
        variantNameEnsglossary.put("Frameshift variant", "ENSGLOSSARY:0000144");
        variantNameEnsglossary.put("Missense variant", "ENSGLOSSARY:0000150");
        variantNameEnsglossary.put("Protein altering variant", "ENSGLOSSARY:0000151");
        variantNameEnsglossary.put("Splice region variant", "ENSGLOSSARY:0000152");
        variantNameEnsglossary.put("Incomplete terminal codon variant", "ENSGLOSSARY:0000153");
        variantNameEnsglossary.put("Intergenic variant", "ENSGLOSSARY:0000174");
        variantNameEnsglossary.put("Stop retained variant", "ENSGLOSSARY:0000154");
        variantNameEnsglossary.put("Synonymous variant", "ENSGLOSSARY:0000155");
        variantNameEnsglossary.put("Flagged variant", "ENSGLOSSARY:0000176");
    }

    // Maps the standard names for zygosity terms onto GENO codes
    public static Map<String, String> zygosityNameGeno;
    static {
        zygosityNameGeno = new HashMap<String, String>();

        zygosityNameGeno.put("Hemizygous", "GENO:GENO_0000134");
        zygosityNameGeno.put("Heterozygous", "GENO:GENO_0000135");
        zygosityNameGeno.put("Homozygous", "GENO:GENO_0000136");
        zygosityNameGeno.put("Hemizygous X-linked", "GENO:GENO_0000604");
        zygosityNameGeno.put("Hemizygous Y-linked", "GENO:GENO_0000605");
        zygosityNameGeno.put("Hemizygous insertion-linked", "GENO:GENO_0000606");
        zygosityNameGeno.put("Simple heterozygous", "GENO:GENO_0000458");
        zygosityNameGeno.put("Disomic zygosity", "GENO:GENO_0000391");
        zygosityNameGeno.put("Aneusomic zygosity", "GENO:GENO_0000392");
        zygosityNameGeno.put("Trisomic homozygous", "GENO:GENO_0000393");
        zygosityNameGeno.put("Trisomic heterozygous", "GENO:GENO_0000394");
        zygosityNameGeno.put("Compound heterozygous", "GENO:GENO_0000402");
    }
}
