package de.samply.lens_beacon_service.entrytype.cohorts;

import de.samply.lens_beacon_service.beacon.BeaconQueryService;
import de.samply.lens_beacon_service.entrytype.EntryType;
import de.samply.lens_beacon_service.query.Query;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CohortsQuery extends Query {
    /**
     * Run queries for stratifiers on the cohorts endpoint at a given Beacon site, using the supplied filters.
     *
     * @param entryType
     */
    public void runStratifierQueriesAtSite(BeaconQueryService beaconQueryService, EntryType entryType) {
        /* No stratifiers for this entry type */
    }
}
