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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract superclass for simple statistical calculations over a rectangular numerical table.
 * A table is represented as a list of rows of numbers, with results calculated for each
 * column.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
abstract class StatisticValues {

    protected List<List<Double>> data;
    protected List<Double> results;

    StatisticValues(List<List<Double>> data) {

        this.data = data;
        results = calculateResults(data);
    }

    /**
     * Gets the calculated results.
     *
     * @return the results
     */
    public List<Double> getResults() {
        return results;
    }

    protected abstract double calculateColumnResult(List<Double> column);

    static List<List<Double>> parseStrings(List<List<String>> records) throws IOException {

        List<List<Double>> data = new ArrayList<>();

        for (List<String> record : records) {

            List<Double> row = new ArrayList<>();
            for (String value : record) {
                row.add(Double.parseDouble(value));
            }
            data.add(row);
        }
        return data;
    }

    private List<Double> calculateResults(final List<List<Double>> data) {

        List<Double> results = new ArrayList<>();

        int size = data.get(0).size();
        for (int column_number = 0; column_number < size; column_number++) {
            List<Double> column = getColumn(column_number);
            results.add(calculateColumnResult(column));
        }

        return results;
    }

    private List<Double> getColumn(int column_number) {

        List<Double> column = new ArrayList<>();
        for (List<Double> value : data) {
            column.add(value.get(column_number));
        }
        return column;
    }
}
