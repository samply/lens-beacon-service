package de.samply.lens_beacon_service.beacon;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Used to capture timing information for Beacon queries aimed at a single entry type (Beacon
 * endpoint).
 */
@Slf4j
public class EntryTimings {
    // Name of the endpoint.
    private String entryType;
    private Integer queryTiming = (-1);
    private Integer queryTimingCount = 0;
    private List<Integer> allTimings = new ArrayList<Integer>();

    // Map from stratifier name to timing information.
    private Map<String, TotalSumPair> stratifierTimings = new HashMap<String, TotalSumPair>();

    // Method that returns a sorted list of stratifier timing keys
    public List<String> getStratifierNames() {
        List<String> stratifierTimingKeys = new ArrayList<String>(stratifierTimings.keySet());
        stratifierTimingKeys.sort(Comparator.naturalOrder());
        return stratifierTimingKeys;
    }

    public TotalSumPair getStratifierInfo(String stratifierName) {
        if (stratifierTimings.containsKey(stratifierName))
            return stratifierTimings.get(stratifierName);
        else {
            TotalSumPair stratifierInfo = new TotalSumPair();
            stratifierTimings.put(stratifierName, stratifierInfo);
            return stratifierInfo;
        }
    }

    public Integer getQueryTiming() {
        return queryTiming;
    }

    // method to add a timing to allTimings
    public void addTiming(Integer timing) {
        allTimings.add(timing);
    }

    public void addQueryTiming(Integer queryTiming) {
        this.queryTiming = queryTiming;
        queryTimingCount++;
        addTiming(queryTiming);
    }

    public void showTimings() {
        log.info("    ENTRY: " + entryType);
        if (queryTiming < 0) {
            log.info("        NO QUERY TIMING AVAILABLE FOR THIS ENTRY TYPE");
            return;
        }
        log.info("        MEAN QUERY TIMING: " + queryTiming/queryTimingCount + " ms");
        for (String stratifierName : getStratifierNames()) {
            int stratifierTimingCount = stratifierTimings.get(stratifierName).getTotal();
            int stratifierTotalTiming = stratifierTimings.get(stratifierName).getSum();
            if (stratifierTimingCount > 0)
                log.info("        MEAN STRATIFIER TIMING " + stratifierName + ": " +  stratifierTotalTiming/stratifierTimingCount + " ms" + "(" + stratifierTimingCount + " values)");
        }
        log.info("        ENTRY MEAN TIMING: " + calculateOverallMeanTiming() + " ms");
    }

    /**
     * Calculates the overall mean timing based on the query timing and stratifier timings.
     * If the query timing count is negative, returns -1.
     * Calculates the mean query timing by dividing the total query timing by the query timing count.
     * Calculates the mean stratifier timing by dividing the total stratifier timing by the stratifier timing count.
     * Returns the average of the mean query timing and mean stratifier timing.
     *
     * @return The overall mean timing. Returns -1 if the query timing count is negative.
     */
    public int calculateOverallMeanTiming() {
        if (queryTimingCount < 0)
            return (-1);

        // Average over all stratifiers
        int stratifierTiming = 0;
        int stratifierTimingCount = 0;
        for (String stratifierName : getStratifierNames()) {
            stratifierTiming += stratifierTimings.get(stratifierName).getSum()/stratifierTimings.get(stratifierName).getTotal();
            stratifierTimingCount++;
        }
        if (stratifierTimingCount == 0)
            return queryTiming/queryTimingCount;

        return (queryTiming + stratifierTiming)/(queryTimingCount + stratifierTimingCount);
    }

    public TotalSumPair calculateOverallTiming() {
        if (queryTimingCount < 0)
            return null;

        int overallTiming = queryTiming;
        int overallCount = queryTimingCount;

        for (String stratifierName : getStratifierNames()) {
            int stratifierTimingCount = stratifierTimings.get(stratifierName).getTotal();
            int stratifierTotalTiming = stratifierTimings.get(stratifierName).getSum();
            if (stratifierTimingCount > 0) {
                overallTiming += stratifierTotalTiming;
                overallCount += stratifierTimingCount;
            }
        }

        return new TotalSumPair(overallCount, overallTiming);
    }

//    public List<TotalSumPair> getOverallTimingsSumOverStratifiers() {
//        if (queryTimingCount < 0)
//            return null;
//
//        List<TotalSumPair> overallTimings = new ArrayList<TotalSumPair>();
//        overallTimings.add(new TotalSumPair(queryTimingCount, queryTiming));
//
//        for (String stratifierName : getStratifierNames()) {
//            int stratifierTimingCount = stratifierTimings.get(stratifierName).getTotal();
//            int stratifierTotalTiming = stratifierTimings.get(stratifierName).getSum();
//            if (stratifierTimingCount > 0)
//                overallTimings.add(new TotalSumPair(stratifierTimingCount, stratifierTotalTiming));
//        }
//
//        return overallTimings;
//    }

    public List<TotalSumPair> getOverallTimings() {
        if (queryTimingCount < 0)
            return null;

        List<TotalSumPair> overallTimings = allTimings.stream()
                .map(timing -> new TotalSumPair(1, timing))
                .collect(Collectors.toList());

        return overallTimings;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    /**
     * Represents a total/sum pair of integers.
     */
    public static class TotalSumPair {
        private int total;
        private int sum;

        public TotalSumPair() {
            this.total = 0;
            this.sum = (-1);
        }

        public TotalSumPair(int total, int sum) {
            this.total = total;
            this.sum = sum;
        }

        /**
         * Adds the specified value to the total and increments the total count by 1.
         * Initializes the sum if necessary.
         *
         * @param value The value to be added to the total.
         */
        public void add(int value) {
            total++;
            if (sum < 0)
                sum = 0;
            sum += value;
        }

        public void add(int count, int value) {
            total += count;
            if (sum < 0)
                sum = 0;
            sum += value;
        }

        public void add(TotalSumPair totalSumPair) {
            total += totalSumPair.getTotal();
            if (sum < 0)
                sum = 0;
            sum += totalSumPair.getSum();
        }

        /**
         * Returns the total number of values contributing to the sum.
         *
         * @return The total value.
         */
        public int getTotal() {
            return total;
        }

        /**
         * Returns the sum of all values.
         *
         * @return The sum.
         */
        public int getSum() {
            return sum;
        }

        public int getMean() {
            if (total == 0)
                return 0;
            return sum/total;
        }
    }
}
