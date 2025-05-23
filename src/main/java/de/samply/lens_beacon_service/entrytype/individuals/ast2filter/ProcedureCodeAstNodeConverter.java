package de.samply.lens_beacon_service.entrytype.individuals.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.lens.AstNode;
import lombok.extern.slf4j.Slf4j;

/**
 * Convert the Lens AST representation of a procedure code into a corresponding Beacon filter.
 */

@Slf4j
public class ProcedureCodeAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconSearchParameters convert(AstNode astNode) {
        return(astAndOntologyTermToFilter(astNode, "OPCS4"));
    }
}
