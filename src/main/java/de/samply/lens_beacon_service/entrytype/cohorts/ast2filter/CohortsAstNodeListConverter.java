package de.samply.lens_beacon_service.entrytype.cohorts.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeListConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert a list of AstNode leaf elements into a list of Beacon filters for cohorts.
 */

public class CohortsAstNodeListConverter extends AstNodeListConverter {
    @Override
    public BeaconSearchParameters convertSingleAstNode(AstNode astNode) {
        BeaconSearchParameters beaconFilter = null;
        if (astNode.key != null)
            // Choose the relevant converter for this AstNode.
            switch (astNode.key) {
                case "cohorts_gender":
                    beaconFilter = new CohortsSexAstNodeConverter().convert(astNode);
                    break;
                case "cohorts_geographicOrigin":
                    beaconFilter = new CohortsGeographicOriginAstNodeConverter().convert(astNode);
                    break;
            }

        return beaconFilter;
    }
}
