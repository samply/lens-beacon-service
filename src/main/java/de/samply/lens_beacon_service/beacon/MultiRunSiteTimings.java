package de.samply.lens_beacon_service.beacon;

import de.samply.lens_beacon_service.util.stats.StatisticalComparison;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Comparator;

@Slf4j
public class MultiRunSiteTimings {
    Map<String, List<EntryTimings.TotalSumPair>> timings = new HashMap<String, List<EntryTimings.TotalSumPair>>();

    /**
     * Adds the specified site name and entry timings to the list of timings for a given site.
     * If the site name does not exist in the timings map, it is added with an empty list.
     * The entry timings are then added to the list of timings for that site.
     *
     * @param siteName The name of the site to add the entry timings to.
     * @param entryTimings The entry timings to add to the list of timings for the specified site.
     */
    public void add(String siteName, List<EntryTimings.TotalSumPair> entryTimings) {
        if (!timings.containsKey(siteName)) {
            timings.put(siteName, new ArrayList<EntryTimings.TotalSumPair>());
        }
        timings.get(siteName).addAll(entryTimings);
    }

    public void showTimings() {
        List<String> sortedKeys = new ArrayList<>(timings.keySet());
        sortedKeys.sort(Comparator.naturalOrder());
        int siteCounter = 0;
        List<EntryTimings.TotalSumPair> timings1 = null;
        List<EntryTimings.TotalSumPair> timings2 = null;
        for (String siteName: sortedKeys) {
            String meanTimingsString = "";
            if (siteCounter == 0)
                timings1 = timings.get(siteName);
            if (siteCounter == 1)
                timings2 = timings.get(siteName);
            for (EntryTimings.TotalSumPair totalSumPair: timings.get(siteName)) {
                if (meanTimingsString.length() > 0)
                    meanTimingsString += ",";
                meanTimingsString += totalSumPair.getMean();
            }
            log.info("TIMINGS for " + siteName + ": " + meanTimingsString);
            siteCounter++;
        }

        if (timings1 != null && timings2 != null) {
            StatisticalComparison statisticalComparison = new StatisticalComparison();
            statisticalComparison.compareDistributions(timings1, timings2);
        }
    }
}
