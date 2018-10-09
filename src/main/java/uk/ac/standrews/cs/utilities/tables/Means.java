/*
 * Copyright 2018 Systems Research Group, University of St Andrews:
 * <https://github.com/stacs-srg>
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
package uk.ac.standrews.cs.utilities.tables;

import java.util.List;

/**
 * Class to calculate means of columns in a rectangular numerical table.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class Means extends StatisticValues {

    /**
     * Creates a new calculation.
     *
     * @param data the numerical table
     */
    @SuppressWarnings({"UnusedDeclaration", "WeakerAccess"})
    public Means(final List<List<Double>> data) {

        super(data);
    }

    /**
     * Calculates the arithmetic mean of a list of values.
     *
     * @param values the values
     * @return the mean of the values
     */
    @SuppressWarnings("WeakerAccess")
    public static double calculateMean(final List<Double> values) {

        double total = 0;
        for (final Double value : values) {
            total += value;
        }

        return total / values.size();
    }

    protected double calculateColumnResult(final List<Double> values) {

        return calculateMean(values);
    }
}
