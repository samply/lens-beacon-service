package de.samply.lens_beacon_service.entrytype.genomicVariations.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.entrytype.genomicVariations.GenomicVariationsNameOntologyMaps;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert the Lens AST representation of zygosity into a corresponding Beacon filter.
 */

public class ZygosityAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconFilter convert(AstNode astNode) {
        return(convert(astNode, GenomicVariationsNameOntologyMaps.zygosityNameGeno));
    }
}
