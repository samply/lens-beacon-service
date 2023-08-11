package de.samply.lens_beacon_service.entrytype.analyses;

import de.samply.lens_beacon_service.measurereport.GroupAdmin;

/**
 * Generate the analyses group for the measure report.
 */

public class AnalysesGroupAdmin extends GroupAdmin {
    /**
     * Generate group with all counts set to default initial values.
     *
     * @return The group object.
     */
    public void init() {
        super.init("analyses"); // Measure report name used in GUI
        // Lens seems to like to have at least one stratifier, even if it is unused.
        group.getStratifier().add(createNullStratifier());
    }
}
