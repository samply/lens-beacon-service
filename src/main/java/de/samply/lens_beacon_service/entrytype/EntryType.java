package de.samply.lens_beacon_service.entrytype;

import de.samply.lens_beacon_service.beacon.model.BeaconEndpoint;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.ast2filter.AstNodeListConverter;
import de.samply.lens_beacon_service.beacon.model.BeaconSearchParameters;
import de.samply.lens_beacon_service.lens.AstNode;
import de.samply.lens_beacon_service.measurereport.GroupAdmin;
import de.samply.lens_beacon_service.query.Query;
import de.samply.lens_beacon_service.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Brings together all of the operations and data associated with a single entry type (e.g.
 * cohorts) for a single site (e.g. EGA Cineca or Molgenis mutations).
 */
@Slf4j
public class EntryType {
    public EntryType() {
    }

    public EntryType(String uri, String method) {
        beaconEndpoint = new BeaconEndpoint(uri, method);
    }

    public BeaconEndpoint beaconEndpoint; // Information needed to query Beacon API for this entry type.
    public AstNodeListConverter astNodeListConverter; // Convert Lens AST to Beacon Filters.
    public List<BeaconSearchParameters> baseFilters; // Filters for a regular query
    public BeaconSearchParameters baseRequestParameters; // Request params for regular query
    public Query query; // Query Beacon, pack results in measure report.
    public GroupAdmin groupAdmin; // Add counts and stuff to measure report group.

    /**
     * Converts the supplied AST into a set of "base" filters that will be applied unaltered
     * to an endpoint, but will also serve as the starting point for building the more complex
     * filters used in the stratifiers.
     *
     * @param astNode The query in AST format.
     */
    public void convert(AstNode astNode) {
        baseFilters = new ArrayList<BeaconSearchParameters>();
        baseRequestParameters = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.REQUEST_PARAMETER);
        if (astNodeListConverter != null) {
            baseFilters = astNodeListConverter.convert(astNode);
            baseRequestParameters = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.REQUEST_PARAMETER); // TODO: pull this out of the nmode list as well
        }
        log.info("EntryType.convert: baseFilters:\n\n" + JsonUtils.toJson(baseFilters));
    }
}
