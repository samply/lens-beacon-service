package de.samply.lens_beacon_service.entrytype.genomicVariations.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.lens.AstNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert the Lens AST representation of a genomic variations into a corresponding Beacon request parameter.
 */

@Slf4j
public class GenomicVariationBaseChangeAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconSearchParameters convert(AstNode astNode) {
        String change = getStringOrFirstOfList(astNode);
        if (change == null) {
            log.warn("convert: change is null");
            return(null);
        }
        String referenceBase = change.split(">")[0];
        String alternateBase = change.split(">")[1];
        BeaconSearchParameters parameters = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.REQUEST_PARAMETER);
        parameters.addParameter("alternateBases", alternateBase);
        parameters.addParameter("referenceBases", referenceBase);
        parameters.addParameter("variantType", "SNP");

        return(parameters);
    }
}
