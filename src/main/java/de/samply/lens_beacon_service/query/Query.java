package de.samply.lens_beacon_service.query;

import de.samply.lens_beacon_service.GlobalVariables;
import de.samply.lens_beacon_service.beacon.BeaconQueryService;
import de.samply.lens_beacon_service.entrytype.EntryType;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.MeasureReport;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class Query {
    /**
     * Run queries for stratifiers on an endpoint at a given Beacon site, using the supplied filters.
     *
     * The measureReportAdmin will be used to store the results of the query.
     *
     * @param beaconQueryService
     * @param entryType
     */
    public abstract void runStratifierQueriesAtSite(BeaconQueryService beaconQueryService, EntryType entryType);

    /**
     * Run a query on an endpoint at a given Beacon site, using the supplied filters.
     *
     * @param entryType
     * @return The measure report group where the query results were stored.
     */
    public MeasureReport.MeasureReportGroupComponent runQueryAtSite(BeaconQueryService beaconQueryService, EntryType entryType) {
        int statsMaxQueryRepeats = Integer.parseInt(GlobalVariables.configuration.getStatsMaxQueryRepeats());
        if (statsMaxQueryRepeats < 1)
            log.warn("runQueryAtSite: statsMaxQueryRepeats less than 1, no queries will be run.");
        if (statsMaxQueryRepeats > 1)
            log.info("runQueryAtSite: running statistical comparisons, statsMaxQueryRepeats: " + statsMaxQueryRepeats);
        int count = 0;
        // For statistical analyses: run runCountQueryAtSite multiple times
        for (int i = 0; i < statsMaxQueryRepeats; i++)
            count = runCountQueryAtSite(beaconQueryService, entryType);

        // Only run stratifiers if main query was able to return a sensible value.
        if (count >= 0)
            runStratifierQueriesAtSite(beaconQueryService, entryType);

        return entryType.groupAdmin.group;
    }

    private int runCountQueryAtSite(BeaconQueryService beaconQueryService, EntryType entryType) {
        long startTime = System.currentTimeMillis(); // Capture query start time

        // Get and store the population count. This is always needed.
        Integer count = beaconQueryService.runBeaconEntryTypeQueryAtSite(entryType, entryType.baseFilters);

        long endTime = System.currentTimeMillis(); // Capture query end time
        long elapsedTime = endTime - startTime; // Calculate elapsed time
        if (elapsedTime < 0)
            log.warn("runQueryAtSite: elapsed time less than 0");
        if (count >= 0)
            beaconQueryService.setQueryTiming(entryType.beaconEndpoint.getEntryType(), (int) elapsedTime);
        entryType.groupAdmin.setCount(count);

        return count;
    }

    /**
     * Runs the query for a given stratifier.
     *
     * @param beaconQueryService
     * @param entryType
     * @param nameOntologyMap
     */
    protected Map<String, Integer> runStratifierQueryAtSite(BeaconQueryService beaconQueryService,
                                                            EntryType entryType,
                                                            Map<String, String> nameOntologyMap,
                                                            String stratifierName) {
        long startTime = System.currentTimeMillis(); // Capture start time

        Map<String, Integer> counts = new HashMap<String, Integer>();
        boolean positiveCountFlag = false;
        for (String name : nameOntologyMap.keySet()) {
            long innerStartTime = System.currentTimeMillis(); // Capture start time
            Integer count = beaconQueryService.runFilterQueryAtSite(entryType,"id", nameOntologyMap.get(name));
            long innerEndTime = System.currentTimeMillis(); // Capture end time
            long innerElapsedTime = innerEndTime - innerStartTime; // Calculate elapsed time
            beaconQueryService.setStratifierTiming(entryType.beaconEndpoint.getEntryType(), (int) innerElapsedTime);

            if (count >= 0)
                positiveCountFlag = true;
            counts.put(name, count);
        }

        long endTime = System.currentTimeMillis(); // Capture end time
        long elapsedTime = endTime - startTime; // Calculate elapsed time

        if (positiveCountFlag)
            beaconQueryService.setStratifierTimings(entryType.beaconEndpoint.getEntryType(), stratifierName, nameOntologyMap.keySet().size(), (int) elapsedTime);

        return counts;
    }
}
