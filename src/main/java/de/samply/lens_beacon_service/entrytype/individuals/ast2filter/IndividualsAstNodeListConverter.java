package de.samply.lens_beacon_service.entrytype.individuals.ast2filter;

import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.ast2filter.AstNodeListConverter;
import de.samply.lens_beacon_service.lens.AstNode;
import lombok.extern.slf4j.Slf4j;

/**
 * Convert a list of AstNode leaf elements into a list of Beacon filters for individuals.
 */

@Slf4j
public class IndividualsAstNodeListConverter extends AstNodeListConverter {
    @Override
    public BeaconFilter convertSingleAstNode(AstNode astNode) {
        BeaconFilter beaconFilter = null;
        if (astNode.key != null)
            // Choose the relevant converter for this AstNode.
            switch (astNode.key) {
                case "gender":
                    beaconFilter = new SexAstNodeConverter().convert(astNode);
                    break;
                case "ethnicity":
                    beaconFilter = new EthnicityAstNodeConverter().convert(astNode);
                    break;
                case "disease":
                    beaconFilter = new DiseaseAstNodeConverter().convert(astNode);
                    break;
                case "geographicOrigin":
                    beaconFilter = new GeographicOriginAstNodeConverter().convert(astNode);
                    break;
                case "procedureCode":
                    beaconFilter = new ProcedureCodeAstNodeConverter().convert(astNode);
                    break;
                case "bmi":
                    beaconFilter = new BmiAstNodeConverter().convert(astNode);
                    break;
                case "weight":
                    beaconFilter = new WeightAstNodeConverter().convert(astNode);
                    break;
                case "height":
                    beaconFilter = new HeightAstNodeConverter().convert(astNode);
                    break;
            }

        return beaconFilter;
    }
}
