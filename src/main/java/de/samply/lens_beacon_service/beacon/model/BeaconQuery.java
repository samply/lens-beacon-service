package de.samply.lens_beacon_service.beacon.model;

import de.samply.lens_beacon_service.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Beacon query.
 *
 * Most of these parameters are hard-coded, but the filters need to be specified on the fly.
 */

@Slf4j
public class BeaconQuery implements Cloneable {
    /**
     * Create a clone of the object, with the supplied filters.
     *
     * @param filters
     * @param requestParameters
     * @return
     */
    public BeaconQuery clone(List<BeaconSearchParameters> filters, BeaconSearchParameters requestParameters) {
        try {
            BeaconQuery clonedBeaconQuery = (BeaconQuery) super.clone();
            clonedBeaconQuery.filters = filters;
            if (requestParameters != null)
                clonedBeaconQuery.requestParameters = requestParameters;

            return clonedBeaconQuery;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public List<BeaconSearchParameters> filters = new ArrayList<BeaconSearchParameters>(); // By default empty
    public BeaconSearchParameters requestParameters = new BeaconSearchParameters(BeaconSearchParameters.ParameterBlockType.REQUEST_PARAMETER); // By default empty
    public String includeResultsetResponses = "HIT";
    public boolean testMode = false;
}
