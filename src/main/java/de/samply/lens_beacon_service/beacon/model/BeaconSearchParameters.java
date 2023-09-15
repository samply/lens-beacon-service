package de.samply.lens_beacon_service.beacon.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * Specify a set of search parameters.
 *
 * Used for both filters and request parameters.
 *
 */

@Slf4j
public class BeaconSearchParameters extends HashMap<String, Object> {
    public enum ParameterBlockType {
        FILTER,
        REQUEST_PARAMETER,
        URI_EXTENSION
    }

    private ParameterBlockType type;

    public BeaconSearchParameters merge(BeaconSearchParameters searchParameters) {
        // This really should not happen, but if it does, return null and
        // break things fast.
        if (getType() != searchParameters.getType())
            return null;

        if (searchParameters != null) {
            for (String key: searchParameters.keySet())
                put(key, searchParameters.get(key));
        }

        return this;
    }

    public ParameterBlockType getType() {
        return type;
    }

    /**
     * Constructor that allows a simple filter with only one term to be specified.
     *
     * @param type The type of the parameter block that this object will hold.
     */
    public BeaconSearchParameters(ParameterBlockType type) {
        this.type = type;
    }

    /**
     * Add a single  term to the filter.
     * @param name Name of filter term.
     * @param value Value of filter term.
     */
    public void addParameter(String name, Object value) {
        this.put(name, value);
    }

    public void addAnonymousStringParameter(String value) {
        this.put("Anonymous", value);
    }

    public String getAnonymousStringParameter() {
        return (String) get("Anonymous");
    }

    /**
     * Convert the filter object to a JSON string.
     *
     * @return JSON string.
     */
    public String toJson() {
        String jsonData = "{}";
        try {
            jsonData = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("An error occurred while processing JSON, check JSON syntax");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("An unknown error occurred while converting into JSON");
            e.printStackTrace();
        }
        return jsonData;
    }
}
