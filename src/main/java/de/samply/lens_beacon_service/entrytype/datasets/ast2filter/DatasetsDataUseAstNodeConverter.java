package de.samply.lens_beacon_service.entrytype.datasets.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.entrytype.datasets.DatasetsNameOntologyMaps;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert the Lens AST representation of genomic source into a corresponding Beacon filter.
 */

public class DatasetsDataUseAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconSearchParameters convert(AstNode astNode) {
        return(astAndOntologyMapToFilter(astNode, DatasetsNameOntologyMaps.dataUseDuo));
    }
}
