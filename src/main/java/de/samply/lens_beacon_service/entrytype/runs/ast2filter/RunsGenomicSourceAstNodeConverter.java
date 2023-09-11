package de.samply.lens_beacon_service.entrytype.runs.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.entrytype.runs.RunsNameOntologyMaps;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert the Lens AST representation of genomic source into a corresponding Beacon filter.
 */

public class RunsGenomicSourceAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconFilter convert(AstNode astNode) {
        return(astAndOntologyMapToFilter(astNode, RunsNameOntologyMaps.genomicSourceGenepio));
    }
}
