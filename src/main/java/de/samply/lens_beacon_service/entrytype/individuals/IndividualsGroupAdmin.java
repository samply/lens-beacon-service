package de.samply.lens_beacon_service.entrytype.individuals;

import de.samply.lens_beacon_service.measurereport.GroupAdmin;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Generate the individuals group for the measure report.
 */

@Slf4j
public class IndividualsGroupAdmin extends GroupAdmin {
    private final String STRATIFIER_GENDER = "Gender";
    private final String STRATIFIER_ETHNICITY = "Ethnicity";
    private final String STRATIFIER_TREATMENT = "Treatment";

    public void init() {
        super.init("individuals");
        group.getStratifier().add(createStratifier(STRATIFIER_GENDER));
        group.getStratifier().add(createStratifier(STRATIFIER_ETHNICITY));
        group.getStratifier().add(createStratifier(STRATIFIER_TREATMENT));
    }

    /**
     * Add counts to the gender stratifier.
     *
     * @param counts
     */
    public void setGenderCounts(Map<String, Integer> counts) {
        setStratifierCounts(counts, STRATIFIER_GENDER);
    }

    /**
     * Add counts to the ethnicity stratifier.
     *
     * @param counts
     */
    public void setEthnicityCounts(Map<String, Integer> counts) {
        setStratifierCounts(counts, STRATIFIER_ETHNICITY);
    }

//    /**
//     * Add counts to the treatment stratifier.
//     *
//     * @param counts
//     */
//    public void setTreatmentCounts(Map<String, Integer> counts) {
//        setStratifierCounts(counts, STRATIFIER_TREATMENT);
//    }
}
