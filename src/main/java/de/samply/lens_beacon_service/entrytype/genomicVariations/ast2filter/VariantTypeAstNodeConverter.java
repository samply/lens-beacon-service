package de.samply.lens_beacon_service.entrytype.genomicVariations.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.entrytype.genomicVariations.GenomicVariationsNameOntologyMaps;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert the Lens AST representation of variant type into a corresponding Beacon filter.
 */

public class VariantTypeAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconSearchParameters convert(AstNode astNode) {
        return(astAndOntologyMapToFilter(astNode, GenomicVariationsNameOntologyMaps.variantNameEnsglossary));
    }
}
