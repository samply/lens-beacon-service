package de.samply.lens_beacon_service.entrytype.biosamples.ast2filter;

import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.ast2filter.AstNodeListConverter;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert a list of AstNode leaf elements into a list of Beacon filters for cohorts.
 */

public class BiosamplesAstNodeListConverter extends AstNodeListConverter {
    @Override
    public BeaconSearchParameters convertSingleAstNode(AstNode astNode) {
        BeaconSearchParameters beaconFilter = null;
        if (astNode.key != null)
            // Choose the relevant converter for this AstNode.
            switch (astNode.key) {
                case "sample_kind":
                    beaconFilter = new SampleKindAstNodeConverter().convert(astNode);
                    break;
                case "biosample_status":
                    beaconFilter = new BiosampleStatusAstNodeConverter().convert(astNode);
                    break;
            }

        return beaconFilter;
    }
}
