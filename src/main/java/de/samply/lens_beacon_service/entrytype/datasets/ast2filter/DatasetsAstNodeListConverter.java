package de.samply.lens_beacon_service.entrytype.datasets.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeListConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert a list of AstNode leaf elements into a list of Beacon filters for datasets.
 */

public class DatasetsAstNodeListConverter extends AstNodeListConverter {
    @Override
    public BeaconSearchParameters convertSingleAstNode(AstNode astNode) {
        BeaconSearchParameters beaconFilter = null;
        if (astNode.key != null)
            // Choose the relevant converter for this AstNode.
            switch (astNode.key) {
                case "data_use":
                    beaconFilter = new DatasetsDataUseAstNodeConverter().convert(astNode);
                    break;
            }

        return beaconFilter;
    }
}
