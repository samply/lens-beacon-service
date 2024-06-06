package de.samply.lens_beacon_service.util.stats;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Bins {
    private double min;
    private double max;
    private double range;
    private double binWidth;
    private int maxBinSize;

    // Each bin contains a list of the data elements that contributed to it.
    // So, to get the count of elements in a bin, you need to use the
    // List size method.
    private List<List<Double>> bins = new ArrayList<>();

    /**
     * Creates a Bins (historgram) object with the given data array and number of bins.
     *
     * @param data    the array of data values to generate the histogram from
     * @param numBins the number of bins to use for the histogram
     */
    public Bins(double[] data, int numBins) {
        generate(data, numBins);
    }

    /**
     * Creates a Bins (histogram) object with the given data array, number of bins, and cutoff bin fraction.
     *
     * Llooks for the first bin after the maximum bin that has a count below the cutoff bin fraction.
     * The histogram is truncated to the cutoff point. This is a form of outlier removal.
     *
     * @param data              the array of data values to generate the histogram from
     * @param numBins           the number of bins to use for the histogram
     * @param cutoffBinFraction the fraction of the maximum bin count below which to truncate the histogram
     */
    public Bins(double[] data, int numBins, double cutoffBinFraction) {
        // Generate histogram
        generate(data, numBins);
        // Look for first bin after maximum that has a count below the cutoff
        int cutoffBinCount = findCutoffBinCount(cutoffBinFraction);
        // Truncate histogram to the cutoff point
        truncate(cutoffBinCount);
    }

    /**
     * Truncates the bins array to the specified cutoff bin count.
     *
     * @param cutoffBinCount the number of bins to retain after truncation
     */
    private void truncate(int cutoffBinCount) {
        List<List<Double>> newBins = new ArrayList<>();
        for (int i = 0; i < cutoffBinCount; i++) {
            List<Double> newBin = new ArrayList<>();
            newBin.addAll(bins.get(i));
            newBins.add(newBin);
        }
        bins = newBins;
    }

    /**
     * Finds the count of bins that should be retained based on a given cutoff bin fraction.
     * This is a form of outlier removal.
     *
     * The method iterates over the bins array from index 0 to the length of the array.
     * It checks if the current bin (bins[i]) is equal to the maximum bin size (maxBinSize).
     * If a bin with the maximum bin size is found, it calculates the cutoffBinSize based on the cutoffBinFraction and the maxBinSize.
     * It initializes a zeroCount variable to keep track of the number of consecutive zero bins.
     * It iterates over the remaining bins starting from the current bin (j = i + 1) to the end of the array.
     * For each bin, it checks if the bin size is greater than 0 and less than or equal to the cutoffBinSize.
     * If a bin meets the condition, it returns the index of the bin (j + 1) minus the zeroCount.
     * If a bin is not zero, it resets the zeroCount to 0.
     * If no bin meets the condition, it breaks out of the loop and returns the length of the bins array.
     *
     * @param cutoffBinFraction the fraction of the maximum bin size to use as the cutoff bin size
     * @return the index of the bin that meets the condition (j + 1 - zeroCount) or the length of the bins array if no bin meets the condition
     */
    private int findCutoffBinCount(double cutoffBinFraction) {
        for (int i = 0; i < bins.size(); i++) {
            if (bins.get(i).size() == maxBinSize) {
                int cutoffBinSize = (int) (maxBinSize * cutoffBinFraction);
                int zeroCount = 0;
                for (int j = i + 1; j < bins.size(); j++) {
                    if (bins.get(j).size() > 0 && bins.get(j).size() <= cutoffBinSize)
                        return j + 1 - zeroCount;
                    if (bins.get(j).size() == 0)
                        zeroCount++;
                    else
                        zeroCount = 0;
                }
                break;
            }
        }
        return bins.size();
    }

    /**
     * Generates the histogram based on the given data array and the specified number of bins.
     *
     * This method calculates the range and bin width based on the minimum and maximum values in the data array.
     * It initializes the bins using the `initializeBins` method.
     * Then, it iterates over each datum in the data array and determines the corresponding bin index.
     * The datum is added to the corresponding bin, and the maximum bin size is updated accordingly.
     *
     * @param data    the array of data values to generate the histogram from
     * @param numBins the number of bins to use for the histogram
     */
    private void generate(double[] data, int numBins) {
        // Calculate the range and bin width
        min = Arrays.stream(data).min().orElse(0);
        max = Arrays.stream(data).max().orElse(0);
        range = max - min;
        // round down to nearest integer
        binWidth = Math.floor(range / numBins);
        if (binWidth < 1.0)
            binWidth = 1.0;
        maxBinSize = 0;
        for (double datum : data) {
            int i = (int) ((datum - min) / binWidth);
            if (i == numBins)
                i--;
            while (i >= bins.size())
                bins.add(new ArrayList<>());
            bins.get(i).add(datum);
            maxBinSize = Math.max(maxBinSize, bins.get(i).size());
        }
    }

    /**
     * Returns a list of all data values in the bins.
     *
     * @return list of data values
     */
    public List<Double> getDataValues() {
        List<Double> dataValues = new ArrayList<>();
        for (List<Double> bin : bins) {
            dataValues.addAll(bin);
        }
        return dataValues;
    }

    /**
     * Prints an ASCII histogram of the data values in the bins.
     *
     * The histogram displays the count of values in each bin, with the bin range and the corresponding bar length.
     * The bar length represents the proportion of the maximum bin size in the current bin.
     * Empty bins are represented by a vertical bar.
     *
     * This method logs the total value count, the maximum value, and the maximum bin size using the log.info method.
     * It then iterates over the bins and prints the bin range and the corresponding bar length for each bin.
     */
    public void printAsciiHistogram() {
        log.info("Total value count: " + getDataValues().size());
        log.info("Maximum value: " + max);
        log.info("maxBinSize: " + maxBinSize);
        double previousBinStart = (-1.0);
        double previousBinEnd = (-1.0);
        for (int i = 0; i < bins.size(); i++) {
            double binStart = min + i * binWidth;
            double binEnd = binStart + binWidth;
            // Do not print duplicate bins. This situation can arise if there are more
            // bins than distinct values in the data.
            if ((int)binStart == (int)previousBinStart && (int)binEnd == (int)previousBinEnd && bins.get(i).size() == 0)
                continue;
            System.out.printf("%5.1f - %5.1f: ", binStart, binEnd);
            int barLength = (int) (200.0 * ((double) bins.get(i).size() / (double) maxBinSize));
            String bar = "";
            for (int j = 0; j < barLength; j++)
                bar += "#";
            // For non-empty bins with counts lower than the increment for printing a hash,
            // print a vertical bar instead of the hash
            if (bar.isEmpty() && bins.get(i).size() > 0)
                bar = "|";
            log.info((int)binStart + "-" + (int)binEnd + " " + bar);
            previousBinStart = binStart;
            previousBinEnd = binEnd;
        }
    }

    public int getBinCount() {
        return bins.size();
    }
}
