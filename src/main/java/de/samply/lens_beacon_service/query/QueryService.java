package de.samply.lens_beacon_service.query;

import de.samply.lens_beacon_service.beacon.EntryTimings;
import de.samply.lens_beacon_service.site.Site;
import de.samply.lens_beacon_service.entrytype.EntryType;
import de.samply.lens_beacon_service.site.Sites;
import de.samply.lens_beacon_service.lens.AstNode;
import de.samply.lens_beacon_service.lens.SiteResult;
import de.samply.lens_beacon_service.measurereport.MeasureReportAdmin;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.MeasureReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is the bridge between the Lens and the Beacon worlds.
 *
 * It performs the conversion from Lens AST queries into Beacon filters,
 * runs the queries against Beacon and then packs the results into
 * FHIR-style measure reports, which are serialized and sent back to
 * Lens.
 */

@Slf4j
public class QueryService {
    /**
     * Takes an AST query from Lens and returns a set of results containing measure reports,
     * one per site.
     *
     * The query will be translated into a Beacon-friendly form and then run at each Beacon
     * site.
     *
     * @param astNode AST query.
     * @return Serialized results.
     */
    public String runQuery(AstNode astNode) {
        // Create a fresh list of known Beacon sites.
        List<Site> sites = Sites.getSites();

        // Add filters to sites.
        // Create an object for holding the result objects for all sites.
        // Insert placeholders for the measure reports.
        List<SiteResult> siteResults = new ArrayList<SiteResult>();
        for (Site site: sites) {
            for (EntryType entryType: site.entryTypes)
                entryType.convert(astNode);
            SiteResult siteResult = new SiteResult(site.name, site.url, "PLACEHOLDER" + site.name);
            siteResults.add(siteResult);
        }

        // Convert results object into a string.
        String jsonResults = siteResults.toString();

        // Run Beacon query at each site, serialize measure reports into JSON strings,
        // replace placeholders in results object with serialized measure reports.
        for (Site site: sites) {
            MeasureReportAdmin measureReportAdmin = new MeasureReportAdmin();

            for (EntryType entryType: site.entryTypes) {
                String entryTypeName = entryType.beaconEndpoint.getEntryType();
                MeasureReport.MeasureReportGroupComponent group = entryType.query.runQueryAtSite(site.beaconQueryService, entryType);
                measureReportAdmin.measureReport.addGroup(group);
            }

            String jsonMeasure = measureReportAdmin.toString();
            jsonResults = jsonResults.replaceAll("\"PLACEHOLDER" + site.name + "\"", "\n" + jsonMeasure.replaceAll("^", "        "));
        }

        showTimings(sites);

        return jsonResults;
    }

    private void showTimings(List<Site> sites) {
        EntryTimings totalEntryTimings = new EntryTimings();
        totalEntryTimings.queryTiming = 0;
        totalEntryTimings.queryTimingCount = 0;
        // Loop over sites
        for (Site site: sites) {
            site.beaconQueryService.showTimings();
            Map<String, EntryTimings> timings = site.beaconQueryService.getTimings();
            // Loop over Beacon endpoints
            for (String entryType: timings.keySet()) {
                EntryTimings entryTimings = timings.get(entryType);

                // Did this end point return a valid timing?
                if (entryTimings.queryTiming >= 0) {
                    totalEntryTimings.queryTiming += entryTimings.queryTiming;
                    totalEntryTimings.queryTimingCount++;

                    // Loop over stratifiers
                    for (String stratifierName: entryTimings.stratifierTimings.keySet()) {
                        List<Integer> stratifierInfo = entryTimings.getStratifierInfo(stratifierName);
                        List<Integer> totalStratifierInfo = totalEntryTimings.getStratifierInfo(stratifierName);
                        if (totalStratifierInfo.size() == 0) {
                            totalStratifierInfo.add(0);
                            totalStratifierInfo.add(0);
                            totalStratifierInfo.add(0);
                        }
                        totalStratifierInfo.set(0, stratifierInfo.get(0)); // value count, should always be the same
                        totalStratifierInfo.set(1, totalStratifierInfo.get(1) + stratifierInfo.get(1)); // timing
                        totalStratifierInfo.set(2, totalStratifierInfo.get(2) + 1); // stratifier count
                    }
                }
            }
        }

        log.info("TOTALS:");
        log.info(":");
        totalEntryTimings.showTimings();
    }
}
