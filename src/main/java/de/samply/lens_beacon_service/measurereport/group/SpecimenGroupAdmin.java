package de.samply.lens_beacon_service.measurereport.group;

import de.samply.lens_beacon_service.measurereport.MeasureReportUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.MeasureReport;

import java.util.List;

/**
 * Generate the specimen group for the measure report.
 */

public class SpecimenGroupAdmin extends GroupAdmin {
    /**
     * Generate group with all counts set to default initial values.
     *
     * @return The group object.
     */
    public MeasureReport.MeasureReportGroupComponent generate() {
        return generate("specimen", "sample_kind");
    }

    /**
     * Set the counts for the various specimen types known to Beam.
     *
     * @param bloodCount
     * @param bloodSerumCount
     * @param bloodPlasmaCount
     * @param lymphCount
     */
    public void setTypeCounts(int bloodCount, int bloodSerumCount, int bloodPlasmaCount, int lymphCount) {
        List<MeasureReport.MeasureReportGroupStratifierComponent> stratifierComponents = group.getStratifier();
        for (MeasureReport.MeasureReportGroupStratifierComponent stratifierComponent: stratifierComponents) {
            CodeableConcept code = stratifierComponent.getCode().get(0);
            String text = code.getText();
            if (text.equals("sample_kind")) {
                stratifierComponent.addStratum(MeasureReportUtils.createStratum("blood", bloodCount));
                stratifierComponent.addStratum(MeasureReportUtils.createStratum("lymph", lymphCount));
                stratifierComponent.addStratum(MeasureReportUtils.createStratum("blood-plasma", bloodPlasmaCount));
                stratifierComponent.addStratum(MeasureReportUtils.createStratum("blood-serum", bloodSerumCount));
                break;
            }
        }
    }
}