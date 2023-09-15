package de.samply.lens_beacon_service.ast2filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.lens.AstNode;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generic template for converting a single Lens AST node into a Beacon 2 filter.
 */
@Slf4j
public abstract class AstNodeConverter {
    /**
     * Convert the supplied AST node to a Beacon filter. This should be a leaf node;
     * children will be ignored.
     *
     * @param astNode
     * @return
     */
    public abstract BeaconSearchParameters convert(AstNode astNode);

    protected BeaconSearchParameters astAndOntologyMapToFilter(AstNode astNode, Map<String, String> nameOntologyMap) {
        String ontologyDefinition = nameOntologyMap.get(((List) astNode.value).get(0));
        BeaconSearchParameters filter = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.FILTER);
        filter.addParameter("id", ontologyDefinition);
        return filter;
    }

    protected BeaconSearchParameters astAndOntologyTermToFilter(AstNode astNode, String ontologyName) {
        String value = (String) astNode.value;
        return ontologyAndValueToFilter(ontologyName, value);
    }

    protected BeaconSearchParameters astOperatorAndOntologyToFilter(AstNode astNode, String ontologyName, String ontologyTerm) {
        // Default type: "GREATER_THAN".
        String value = ((Integer) (((LinkedHashMap) astNode.value).get("min"))).toString();
        String type = (String) astNode.type;
        String operator = ">";

        // Silently ignoring the third type "BETWEEN"
        if (type.equals("LOWER_THAN")) {
            value = ((Integer) (((LinkedHashMap) astNode.value).get("max"))).toString();
            operator = "<";
        }
        BeaconSearchParameters beaconFilter = ontologyAndValueToFilter(ontologyName, ontologyTerm);
        beaconFilter.addParameter("operator", operator);
        beaconFilter.addParameter("value", value);

        return beaconFilter;
    }

    protected BeaconSearchParameters ontologyAndValueToFilter(String ontologyName, String ontologyTerm) {
        BeaconSearchParameters filter = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.FILTER);
        filter.addParameter("id", ontologyName + ":" + ontologyTerm);
        return filter;
    }

}
