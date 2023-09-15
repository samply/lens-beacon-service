package de.samply.lens_beacon_service.entrytype.runs.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.entrytype.runs.RunsNameOntologyMaps;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert the Lens AST representation of platform model into a corresponding Beacon filter.
 */

public class RunsPlatformModelAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconSearchParameters convert(AstNode astNode) {
        return(astAndOntologyMapToFilter(astNode, RunsNameOntologyMaps.platformModelObi));
    }
}
