package de.samply.lens_beacon_service.entrytype.cohorts.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.entrytype.cohorts.CohortsNameOntologyMaps;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert the Lens AST representation of gender into a corresponding Beacon filter.
 */

public class CohortsSexAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconFilter convert(AstNode astNode) {
        return(astAndOntologyMapToFilter(astNode, CohortsNameOntologyMaps.genderNameNcit));
    }
}
