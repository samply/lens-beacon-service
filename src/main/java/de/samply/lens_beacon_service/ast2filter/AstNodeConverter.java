package de.samply.lens_beacon_service.ast2filter;

import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.lens.AstNode;
import de.samply.lens_beacon_service.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        String value = getStringOrFirstOfList(astNode);
        if (value == null) {
            log.warn("astAndOntologyMapToFilter: value is null");
            return null;
        }
        String ontologyDefinition = nameOntologyMap.get(value);
        BeaconSearchParameters filter = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.FILTER);
        filter.addParameter("id", ontologyDefinition);
        return filter;
    }

    /**
     * Extracts a string value from the provided {@code AstNode}. If {@code astNode.value} is a {@link List},
     * the method retrieves and returns the first element of the list, casting it to a string. If {@code astNode.value}
     * is a {@link String}, the method directly returns the string value.
     * <p>
     * Logs warnings and returns {@code null} in the following cases:
     * <ul>
     *   <li>If {@code astNode} is {@code null}</li>
     *   <li>If {@code astNode.value} is {@code null}</li>
     *   <li>If {@code astNode.value} is neither a {@link List} nor a {@link String}</li>
     * </ul>
     *
     * @param astNode the {@link AstNode} containing the value to process. Can be {@code null}.
     * @return the extracted string value, the first element of the list (if applicable), or {@code null}
     *         if {@code astNode} or {@code astNode.value} is invalid.
     */
    protected String getStringOrFirstOfList(AstNode astNode) {
        if (astNode == null) {
            log.warn("getStringOrFirstOfList: astNode is null");
            return null;
        }
        if (astNode.value == null) {
            log.warn("getStringOrFirstOfList: astNode.value is null");
            return null;
        }
        try {
            if (astNode.value instanceof List)
                return (String) ((List) astNode.value).get(0);
            if (astNode.value instanceof String)
                return (String) astNode.value;
            log.warn("getStringOrFirstOfList: astNode.value ({}) is neither a List nor a String", astNode.value);
        } catch (Exception e) {
            log.warn("getStringOrFirstOfList: astNode.value ({}) cannot be cast to List/String, trace: {}", astNode.value, Utils.traceFromException(e));
        }
        return null;
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
