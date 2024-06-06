package de.samply.lens_beacon_service.query;

import de.samply.lens_beacon_service.beacon.EntryTimings;
import de.samply.lens_beacon_service.beacon.MultiRunSiteTimings;
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
    public String runQuery(AstNode astNode, MultiRunSiteTimings multiRunSiteTimings) {
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

        showTimings(sites, multiRunSiteTimings);

        return jsonResults;
    }

    /**
     * Calculates and prints out the total timings for the given list of sites.
     * <p>
     * Iterates over each site, retrieves the timings for each beacon endpoint, and accumulates the total timings.
     * Additionally, calculates and logs the total stratifier timings for each beacon endpoint.
     *
     * @param sites                 The list of sites to calculate and print the timings for.
     * @param multiRunSiteTimings
     */
    private void showTimings(List<Site> sites, MultiRunSiteTimings multiRunSiteTimings) {
        EntryTimings totalEntryTimings = new EntryTimings();
        // Loop over sites
        for (Site site: sites) {
            log.info("");
            log.info("Site name: " + site.name);
            List<EntryTimings.TotalSumPair> siteTimings = site.beaconQueryService.showTimings();
            multiRunSiteTimings.add(site.name, siteTimings); // save for stats analysis
            Map<String, EntryTimings> timings = site.beaconQueryService.getTimings();
            // Loop over Beacon endpoints
            for (String entryType: timings.keySet()) {
                EntryTimings entryTimings = timings.get(entryType);

                // Did this end point return a valid timing?
                if (entryTimings.getQueryTiming() >= 0) {
                    totalEntryTimings.addQueryTiming(entryTimings.getQueryTiming());

                    // Loop over stratifiers
                    for (String stratifierName: entryTimings.getStratifierNames()) {
                        EntryTimings.TotalSumPair stratifierInfo = entryTimings.getStratifierInfo(stratifierName);
                        EntryTimings.TotalSumPair totalStratifierInfo = totalEntryTimings.getStratifierInfo(stratifierName);
                        totalStratifierInfo.add(stratifierInfo);
                    }
                }
            }
        }

        log.info("TOTALS:");
        log.info(":");
        totalEntryTimings.showTimings();
    }
}
