package uk.ac.standrews.cs.util.tables;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.List;

public class ConfidenceIntervals extends StatisticValues {

    // The probability that the real mean lies within the confidence interval.
    private static final double CONFIDENCE_LEVEL = 0.95;
    private static final double ONE_TAILED_CONFIDENCE_LEVEL = 1 - (1 - CONFIDENCE_LEVEL) / 2;

    public ConfidenceIntervals(List<List<Double>> data) {

        super(data);
    }

    protected double calculate(List<Double> values) {

        return standardError(values) * criticalValue(sampleSize(values));
    }

    private double standardError(List<Double> values) {

        return standardDeviation(values) / Math.sqrt(sampleSize(values));
    }

    private double standardDeviation(List<Double> values) {

        double[] array = new double[values.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = values.get(i);
        }
        return new StandardDeviation().evaluate(array);
    }

    private static double criticalValue(int number_of_values) {

        int degrees_of_freedom = number_of_values - 1;
        return new TDistribution(degrees_of_freedom).inverseCumulativeProbability(ONE_TAILED_CONFIDENCE_LEVEL);
    }

    private int sampleSize(List<Double> values) {

        return values.size();
    }
}
