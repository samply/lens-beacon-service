package de.samply.lens_beacon_service.util.stats;

import de.samply.lens_beacon_service.beacon.EntryTimings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.MannWhitneyUTest;

import java.util.List;

@Slf4j
public class StatisticalComparison {
    public void compareDistributions(List<EntryTimings.TotalSumPair> dist1, List<EntryTimings.TotalSumPair> dist2) {
        double[] data1 = convertToDouble(dist1);
        double[] data2 = convertToDouble(dist2);

        // Generate histograms
        Bins bins1 = new Bins(data1, 100);
        Bins bins2 = new Bins(data2, 100);

        // Print histograms
        log.info("Histogram for data1:");
        bins1.printAsciiHistogram();
        log.info("Histogram for data2:");
        bins2.printAsciiHistogram();

        // Extract (possibly) cleaned data from histograms
        double[] cleanedData1 = bins1.getDataValues().stream().mapToDouble(Double::doubleValue).toArray();
        double[] cleanedData2 = bins2.getDataValues().stream().mapToDouble(Double::doubleValue).toArray();

        // Descriptive statistics
        DescriptiveStatistics stats1 = new DescriptiveStatistics(cleanedData1);
        DescriptiveStatistics stats2 = new DescriptiveStatistics(cleanedData2);

        log.info("Mean of data1: " + stats1.getMean() + ", Mean of data2: " + stats2.getMean());
        log.info("Median of data1: " + stats1.getPercentile(50) + ", Median of data2: " + stats2.getPercentile(50));
        log.info("Standard Deviation of data1: " + stats1.getStandardDeviation() + ", Standard Deviation of data2: " + stats2.getStandardDeviation());
        log.info("Variance of data1: " + stats1.getVariance() + ", Variance of data2: " + stats2.getVariance());
        log.info("Skewness (symmetry) of data1: " + stats1.getSkewness() + ", Skewness of data2: " + stats2.getSkewness());
        log.info("Kurtosis (tailedness) of data1: " + stats1.getKurtosis() + ", Kurtosis of data2: " + stats2.getKurtosis());

        // The Mann Whitney U test indicates whether one population tends to produce
        // higher values than the other population. Two populations are different if:
        // * U is large (the closer to the max the better)
        // * p-value is small
        MannWhitneyUTest mwTest = new MannWhitneyUTest();
        int uMax = cleanedData1.length * cleanedData2.length;
        double uStatistic = mwTest.mannWhitneyU(cleanedData1, cleanedData2);
        double mwPValue = mwTest.mannWhitneyUTest(cleanedData1, cleanedData2);
        log.info("Mann-Whitney U-test U: " + uStatistic + " (max: " + uMax + "), p-value: " + mwPValue + "(double min: " + Double.MIN_VALUE + ")");
    }

    private double[] convertToDouble(List<EntryTimings.TotalSumPair> dist) {
        double[] data = new double[dist.size()];
        for (int i = 0; i < dist.size(); i++)
            data[i] = dist.get(i).getMean();
        return data;
    }

    /**
     * Prints an ASCII histogram of the given data using the specified number of bins.
     * The histogram represents the distribution of the data by dividing it into bins.
     * Each bin represents a range of values and the height of the bar represents the count of data points within that range.
     *
     * @param data    The array of data to be histogrammed.
     * @param numBins The number of bins to divide the data into.
     */
    private Bins generateBins(double[] data, int numBins, double cutoffBinFraction) {
        // Generate initial histogram truncated to a given cutoff
        Bins binsWithCutoff = new Bins(data, numBins * 5, cutoffBinFraction);
        // Re-generate histogram with data remaining after truncation,
        // using fewer bins to make the histogram shape more obvious.
        Bins bins = new Bins(binsWithCutoff.getDataValues().stream().mapToDouble(Double::doubleValue).toArray(), numBins);

        return bins;
    }
}