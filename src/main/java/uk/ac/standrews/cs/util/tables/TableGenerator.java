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

import uk.ac.standrews.cs.util.dataset.*;
import uk.ac.standrews.cs.util.tools.*;

import java.io.*;
import java.util.*;

public class TableGenerator {

    private final List<String> row_labels;
    private final List<DataSet> data_sets;
    private final PrintStream writer;
    private final String table_caption;
    private final String first_column_heading;
    private final List<Boolean> display_as_percentage;
    private final char output_separator;

    public TableGenerator(List<String> row_labels, List<DataSet> data_sets, PrintStream writer, String table_caption, String first_column_heading, List<Boolean> display_as_percentage, char output_separator) {

        this.row_labels = row_labels;
        this.data_sets = data_sets;
        this.writer = writer;
        this.table_caption = table_caption;
        this.first_column_heading = first_column_heading;
        this.display_as_percentage = display_as_percentage;
        this.output_separator = output_separator;
    }

    public void printTable() throws IOException {

        writer.println(table_caption);
        getProcessedData().print(writer);
    }

    public DataSet getProcessedData() throws IOException {

        // Get the column labels from the data set for the first row - all rows should have the same labels.
        // Construct a new array list for column labels since DataSet#getColumnLabels() returns an unmodifiable list.
        List<String> column_labels = new ArrayList<>(data_sets.get(0).getColumnLabels());
        column_labels.add(0, first_column_heading);

        DataSet processed_data = new DataSet(column_labels);
        processed_data.setOutputFormat(DataSet.DEFAULT_CSV_FORMAT.withDelimiter(output_separator));

        for (int row_number = 0; row_number < data_sets.size(); row_number++) {

            String row_label = row_labels.get(row_number);
            DataSet dataSet = data_sets.get(row_number);

            processed_data.addRow(getProcessedRow(row_label, dataSet));
        }

        return processed_data;
    }

    private List<String> getProcessedRow(String row_label, DataSet dataSet) throws IOException {

        List<List<Double>> numerical_values = StatisticValues.parseStrings(dataSet.getRecords());

        StatisticValues means = new Means(numerical_values);
        StatisticValues confidence_intervals = numerical_values.size() > 1 ? new ConfidenceIntervals(numerical_values) : null;

        List<String> values = new ArrayList<>();
        values.add(row_label);

        for (int column_number = 0; column_number < means.getResults().size(); column_number++) {
            values.add(getSummary(means, confidence_intervals, column_number));
        }

        return values;
    }

    private String getSummary(StatisticValues means, StatisticValues confidence_intervals, int column_number) {

        boolean percentage = display_as_percentage.get(column_number);
        String formatted_mean = format(means.getResults().get(column_number), percentage);
        String formatted_interval = confidence_intervals != null ? (" ± " + format(confidence_intervals.getResults().get(column_number), percentage)) : "";

        String summary = formatted_mean + formatted_interval;
        if (percentage) summary += "%";

        return summary;
    }

    private String format(Double value, boolean display_output_as_percentages) {

        int multiplier = display_output_as_percentages ? 100 : 1;

        return Formatting.format(value * multiplier, 1);
    }
}
