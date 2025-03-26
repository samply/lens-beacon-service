package de.samply.lens_beacon_service.entrytype.genomicVariations;

import de.samply.lens_beacon_service.measurereport.GroupAdmin;

import java.util.Map;

/**
 * Generate the genomicVariations group for the measure report.
 */

public class GenomicVariationsGroupAdmin extends GroupAdmin {
    private final String STRATIFIER_VARIANT_NAME = "variant_name";


    /**
     * Generate group with all counts set to default initial values.
     *
     * @return The group object.
     */
    public void init() {
        super.init("variants"); // Measure report name used in GUI
        group.getStratifier().add(createStratifier(STRATIFIER_VARIANT_NAME));
    }

    /**
     * Set the counts for the various variant types known to Beam.
     *
     * @param counts
     */
    public void setVariantNameCounts(Map<String, Integer> counts) {
        setStratifierCounts(counts, STRATIFIER_VARIANT_NAME);
    }
}
