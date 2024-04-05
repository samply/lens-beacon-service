package de.samply.lens_beacon_service.query;

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
        long startTime = System.currentTimeMillis(); // Capture start time

        // Get and store the population count. This is always needed.
        Integer count = beaconQueryService.runBeaconEntryTypeQueryAtSite(entryType, entryType.baseFilters);

        long endTime = System.currentTimeMillis(); // Capture end time
        long elapsedTime = endTime - startTime; // Calculate elapsed time
        log.info("runQueryAtSite: QQQQQQQQQQQQZZZZZZZZZZZQQQQQQQQQQ entry type: " + entryType.beaconEndpoint.getEntryType());
        log.info("runQueryAtSite: QQQQQQQQQQQQZZZZZZZZZZZQQQQQQQQQQ query execution time (ms): " + elapsedTime);
        if (elapsedTime < 0)
            log.info("runQueryAtSite: QQQQQQQQQQQQZZZZZZZZZZZQQQQQQQQQQ elapsed time less than 0");
        if (count >= 0) {
            log.info("runQueryAtSite: QQQQQQQQQQQQZZZZZZZZZZZQQQQQQQQQQ INT query execution time (ms): " + (int) elapsedTime);
            beaconQueryService.setQueryTiming(entryType.beaconEndpoint.getEntryType(), (int) elapsedTime);
        }
        log.info("runQueryAtSite: QQQQQQQQQQQQZZZZZZZZZZZQQQQQQQQQQ siteUrl: " + beaconQueryService.siteUrl);
        log.info("runQueryAtSite: QQQQQQQQQQQQZZZZZZZZZZZQQQQQQQQQQ proxyUrl: " + beaconQueryService.proxyUrl);
        log.info("runQueryAtSite: QQQQQQQQQQQQZZZZZZZZZZZQQQQQQQQQQ count: " + count);
        entryType.groupAdmin.setCount(count);

        // Only run stratifiers if main query was able to return a sensible value.
        if (count >= 0)
            runStratifierQueriesAtSite(beaconQueryService, entryType);

        return entryType.groupAdmin.group;
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
            Integer count = beaconQueryService.runFilterQueryAtSite(entryType,"id", nameOntologyMap.get(name));
            if (count >= 0)
                positiveCountFlag = true;
            counts.put(name, count);
        }

        long endTime = System.currentTimeMillis(); // Capture end time
        long elapsedTime = endTime - startTime; // Calculate elapsed time

        if (positiveCountFlag)
            beaconQueryService.setStratifierTiming(entryType.beaconEndpoint.getEntryType(), stratifierName, nameOntologyMap.keySet().size(), (int) elapsedTime);

        return counts;
    }
}
