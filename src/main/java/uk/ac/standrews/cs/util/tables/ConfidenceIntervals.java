/*
 * Copyright 2016 Digitising Scotland project:
 * <http://digitisingscotland.cs.st-andrews.ac.uk/>
 *
 * This file is part of the module ciesvium.
 *
 * ciesvium is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * ciesvium is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with ciesvium. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.standrews.cs.util.tables;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.List;

/**
 * Class to calculate confidence intervals for values within columns in a rectangular numerical table.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class ConfidenceIntervals extends StatisticValues {

    /**
     * The probability that the real mean lies within the confidence interval.
     */
    public static final double CONFIDENCE_LEVEL = 0.95;

    private static final double ONE_TAILED_CONFIDENCE_LEVEL = 1 - (1 - CONFIDENCE_LEVEL) / 2;

    /**
     * Creates a new calculation.
     *
     * @param data the numerical table
     */
    public ConfidenceIntervals(List<List<Double>> data) {

        super(data);
    }

    /**
     * Calculates the confidence interval for a list of values.
     *
     * @param values the values
     * @return the confidence interval of the values
     */
    public static double calculateConfidenceInterval(List<Double> values) {

        return standardError(values) * criticalValue(sampleSize(values));
    }

    protected double calculateColumnResult(List<Double> values) {

        return calculateConfidenceInterval(values);
    }

    private static double standardError(List<Double> values) {

        return standardDeviation(values) / Math.sqrt(sampleSize(values));
    }

    private static double standardDeviation(List<Double> values) {

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

    private static int sampleSize(List<Double> values) {

        return values.size();
    }
}
