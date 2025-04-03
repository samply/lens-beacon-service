package de.samply.lens_beacon_service.entrytype.genomicVariations.ast2filter;

import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.lens.AstNode;
import de.samply.lens_beacon_service.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert the Lens AST representation of a genomic variations into a corresponding Beacon request parameter.
 */

@Slf4j
public class GenomicVariationChromosomeAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconSearchParameters convert(AstNode astNode) {
        String chromosome = getStringOrFirstOfList(astNode);
        if (chromosome == null) {
            log.warn("convert: chromosome is null");
            return(null);
        }
        BeaconSearchParameters parameters = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.REQUEST_PARAMETER);
        // TODO: at some point, it would be good to add the chromosome number or the relevant ontology term e.g. NC_000017.11
        parameters.addParameter("Chromosome", chromosome);
        parameters.addParameter("chromosome", chromosome);
        parameters.addParameter("variantType", "SNP");

        return(parameters);
    }
}
