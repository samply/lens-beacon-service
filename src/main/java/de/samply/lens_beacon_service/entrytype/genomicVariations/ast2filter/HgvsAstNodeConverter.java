package de.samply.lens_beacon_service.entrytype.genomicVariations.ast2filter;

import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.ast2filter.AstNodeConverter;
import de.samply.lens_beacon_service.lens.AstNode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert the Lens AST representation of a genomic variations into a corresponding Beacon filter.
 */

@Slf4j
public class HgvsAstNodeConverter extends AstNodeConverter {
    @Override
    public BeaconSearchParameters convert(AstNode astNode) {
        // Extract the HGVS string from the AST node and parse it into chromosome,
        // position and base change. Also, check for formatting errors in the HGVS string.
        // It should look something like this: "22:g.12340000C>T".
        if (astNode == null) {
            log.warn("convert: astNode is null");
            return null;
        }
        if (astNode.value == null) {
            log.warn("convert: astNode.value is null");
            return null;
        }
        if (!(astNode.value instanceof String)) {
            log.warn("convert: astNode.value is not a String");
            log.warn("convert: astNode.value: " + astNode.value);
            return null;
        }
        String hgsv = (String) astNode.value;
        String[] hgsvParts = hgsv.split(":");
        if (hgsvParts.length != 2) {
            log.warn("convert: hgsvParts.length != 2");
            log.warn("convert: hgsvParts.length: " + hgsvParts.length);
            log.warn("convert: hgsv: " + hgsv);
            return null;
        }
        String chromosome = hgsvParts[0];
        String rest = hgsvParts[1];
        if (rest.split("\\.").length != 2) {
            log.warn("convert: rest.split(\".\").length != 2");
            log.warn("convert: rest: " + rest);
            return null;
        }
        rest = rest.split("\\.")[1];
        int restLen = rest.length();
        Integer position = Integer.parseInt(rest.substring(0, restLen - 3));
        List<Integer> startPositions = new ArrayList<Integer>();
        startPositions.add(position-1);
        List<Integer> endPositions = new ArrayList<Integer>();
        startPositions.add(position);
        String change = rest.substring(restLen - 3, restLen);
        String referenceBase = change.split(">")[0];
        String alternateBase = change.split(">")[1];
//        BeaconSearchParameters parameters = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.REQUEST_PARAMETER);
//        //parameters.addParameter("Chromosome", chromosome);
//        parameters.addParameter("alternateBases", referenceBase);
//        parameters.addParameter("referenceBases", alternateBase);
//        parameters.addParameter("start", startPositions);
//        parameters.addParameter("end", endPositions);
//        parameters.addParameter("variantType", "SNP");
        BeaconSearchParameters parameters = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.URI_EXTENSION);
        String uriExtension = "chr" + chromosome + "_" + position.toString() + "_" + referenceBase + "_" + alternateBase;
        parameters.addAnonymousStringParameter(uriExtension);

        log.info("HgvsAstNodeConverter.convert: uriExtension: " + uriExtension);

        return(parameters);
    }
}
