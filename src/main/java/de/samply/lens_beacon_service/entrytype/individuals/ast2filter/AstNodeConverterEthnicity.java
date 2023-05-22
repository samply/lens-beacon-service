package de.samply.lens_beacon_service.entrytype.individuals.ast2filter;

import de.samply.lens_beacon_service.beacon.model.BeaconFilter;
import de.samply.lens_beacon_service.convert.AstNodeConverter;
import de.samply.lens_beacon_service.entrytype.individuals.NameOntologyMaps;
import de.samply.lens_beacon_service.lens.AstNode;

/**
 * Convert the Lens AST representation of ethnicity into a corresponding Beacon filter.
 */

public class AstNodeConverterEthnicity extends AstNodeConverter {
    @Override
    public BeaconFilter convert(AstNode astNode) {
        return(convert(astNode, NameOntologyMaps.ethnicityNameNcit));
    }
}
