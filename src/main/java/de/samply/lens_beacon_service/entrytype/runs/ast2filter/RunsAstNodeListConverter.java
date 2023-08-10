package de.samply.lens_beacon_service.entrytype.runs.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeListConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert a list of AstNode leaf elements into a list of Beacon filters for runs.
 */

public class RunsAstNodeListConverter extends AstNodeListConverter {
    @Override
    public BeaconFilter convertSingleAstNode(AstNode astNode) {
        BeaconFilter beaconFilter = null;
        if (astNode.key != null)
            // Choose the relevant converter for this AstNode.
            switch (astNode.key) {
                case "platform_model":
                    beaconFilter = new RunsPlatformModelAstNodeConverter().convert(astNode);
                    break;
                case "genomic_source":
                    beaconFilter = new RunsGenomicSourceAstNodeConverter().convert(astNode);
                    break;
            }

        return beaconFilter;
    }
}
