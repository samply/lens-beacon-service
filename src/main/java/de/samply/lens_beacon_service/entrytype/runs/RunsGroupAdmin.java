package de.samply.lens_beacon_service.entrytype.runs;

import de.samply.lens_beacon_service.measurereport.GroupAdmin;

/**
 * Generate the runs group for the measure report.
 */

public class RunsGroupAdmin extends GroupAdmin {
    /**
     * Generate group with all counts set to default initial values.
     *
     * @return The group object.
     */
    public void init() {
        super.init("runs"); // Measure report name used in GUI
        // Lens seems to like to have at least one stratifier, even if it is unused.
        group.getStratifier().add(createNullStratifier());
    }
}
