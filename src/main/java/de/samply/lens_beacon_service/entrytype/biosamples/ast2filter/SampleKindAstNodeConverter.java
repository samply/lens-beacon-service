package de.samply.lens_beacon_service.entrytype.biosamples.ast2filter;

import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.entrytype.biosamples.BiosamplesNameOntologyMaps;
import de.samply.lens_beacon_service.lens.AstNode;
import lombok.extern.slf4j.Slf4j;

/**
 * Convert the Lens AST representation of sample type into a corresponding Beacon filter.
 */

@Slf4j
public class SampleKindAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconSearchParameters convert(AstNode astNode) {
        return(astAndOntologyMapToFilter(astNode, BiosamplesNameOntologyMaps.biosmapleTypeUberon));
    }
}
