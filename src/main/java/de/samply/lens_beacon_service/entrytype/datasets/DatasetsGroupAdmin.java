package de.samply.lens_beacon_service.entrytype.datasets;

import de.samply.lens_beacon_service.measurereport.GroupAdmin;

/**
 * Generate the datasets group for the measure report.
 */

public class DatasetsGroupAdmin extends GroupAdmin {
    /**
     * Generate group with all counts set to default initial values.
     *
     * @return The group object.
     */
    public void init() {
        super.init("datasets"); // Measure report name used in GUI
        // Lens seems to like to have at least one stratifier, even if it is unused.
        group.getStratifier().add(createNullStratifier());
    }
}
