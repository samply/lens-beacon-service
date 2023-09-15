package de.samply.lens_beacon_service.entrytype.genomicVariations.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.lens.AstNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Convert the Lens AST representation of a genomic variations into a corresponding Beacon request parameter.
 */

@Slf4j
public class GenomicVariationRangeAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconSearchParameters convert(AstNode astNode) {
        List<Integer> startPositions = new ArrayList<Integer>();
        startPositions.add((Integer) (((LinkedHashMap) astNode.value).get("min")));
        List<Integer> endPositions = new ArrayList<Integer>();
        endPositions.add((Integer) (((LinkedHashMap) astNode.value).get("max")));
        BeaconSearchParameters parameters = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.REQUEST_PARAMETER);
        parameters.addParameter("start", startPositions);
        parameters.addParameter("end", endPositions);
        parameters.addParameter("variantType", "SNP");

        return(parameters);
    }
}
