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
package uk.ac.standrews.cs.util.dataset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import uk.ac.standrews.cs.util.tools.FileManipulation;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Simple abstraction over a plain-text dataset. Data is represented as a list of rows, each of which is a list of strings, plus
 * a list of string column labels.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class DataSet {

    /**
     * The default CSV file format: <a href="https://commons.apache.org/proper/commons-csv/apidocs/org/apache/commons/csv/CSVFormat.html#RFC4180">RFC4180</a>.
     */
    public static final CSVFormat DEFAULT_CSV_FORMAT = CSVFormat.RFC4180;

    /**
     * The default delimiter.
     */
    public static final char DEFAULT_DELIMITER = ',';

      List<String> labels;
      List<List<String>> records;

    private CSVFormat output_format = DEFAULT_CSV_FORMAT;

    /**
     * Creates a new empty dataset with given column labels.
     *
     * @param labels the column labels
     */
    public DataSet(List<String> labels) {

        this(labels, new ArrayList<>());
    }

    /**
     * Creates a new dataset with column labels and data read from the given Reader, using the default delimiter: {@value #DEFAULT_DELIMITER}.
     *
     * @param reader the Reader to read column labels and data from
     */
    public DataSet(Reader reader) throws IOException {

        this(reader, DEFAULT_DELIMITER);
    }

    /**
     * Creates a new dataset with column labels and data read from the given Reader, using a specified delimiter.
     *
     * @param reader the Reader to read column labels and data from
     * @param delimiter the delimiter for labels and values
     */
    public DataSet(Reader reader, char delimiter) throws IOException {

        this(reader, DEFAULT_CSV_FORMAT.withDelimiter(delimiter));
    }

    /**
     * Creates a new dataset with column labels and data read from the given Reader, using a specified input format.
     *
     * @param reader the Reader to read column labels and data from
     * @param input_format the format
     */
    public DataSet(Reader reader, CSVFormat input_format) throws IOException {

        this();

        try (CSVParser parser = new CSVParser(reader, input_format.withHeader())) {

            labels.addAll(getColumnLabels(parser));

            for (CSVRecord record : parser) {

                final List<String> items = csvRecordToList(record);
                final int size = items.size();

                // Don't add row if the line was empty.
                if (size > 1 || (size == 1 && items.get(0).length() > 0)) {
                    records.add(items);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new dataset with column labels and data read from a file with the given path.
     *
     * @param path the path of the file to read column labels and data from
     */
    public DataSet(Path path) throws IOException {

        this(FileManipulation.getInputStreamReader(path));
    }

    /**
     * Creates a new dataset from a given dataset, with the same column labels and selected rows.
     *
     * @param selector a selector to determine which rows should be included
     */
    public DataSet select(Selector selector) {

        return new DataSet(labels, filterRecords(selector));
    }

    /**
     * Creates a new dataset from a given dataset, with selected columns.
     *
     * @param projector a projector to determine which columns should be included
     */
    public DataSet project(Projector projector) {

        return new DataSet(projector.getProjectedColumnLabels(), projectRecords(projector));
    }

    /**
     * Creates a new dataset from a given dataset, with each row transformed in a specified way.
     *
     * @param mapper a mapper to transform each row into a new row in the output dataset
     */
   public DataSet map(Mapper mapper) {

        return new DataSet(labels, mapRecords(mapper));
    }

    /**
     * Creates a new dataset from a given dataset, with additional generated columns.
     *
     * @param extender an extender to generate additional column labels and values
     */
    public DataSet extend(Extender extender) {

        return new DataSet(extender.getColumnLabels(), extendRecords(extender));
    }

    public DataSet(DataSet existing_records) {

        this(existing_records.getColumnLabels(), existing_records.getRecords());
    }

    protected void init(DataSet existing_records) {

        init(existing_records.getColumnLabels(), existing_records.getRecords());
    }

    protected DataSet() throws IOException {

        this(new ArrayList<>(), new ArrayList<>());
    }

    private DataSet(List<String> labels, List<List<String>> records) {

        init(labels, records);
    }

    protected void init(List<String> labels, List<List<String>> records) {

        this.labels = labels;
        this.records = records;
    }

    /**
     * Adds a new record to the dataset, specified as a list of strings.
     *
     * @param record a new record
     */
    public void addRow(List<String> record) {

        records.add(record);
    }

    /**
     * Adds a new record to the dataset, specified as a number of strings.
     *
     * @param values a new record
     */
    public void addRow(String... values) {

        addRow(Arrays.asList(values));
    }

    /**
     * Gets the records of the dataset.
     *
     * @return the records
     */
    public List<List<String>> getRecords() {

        return records;
    }

    /**
     * Gets the column labels of the dataset.
     *
     * @return the labels
     */
    public List<String> getColumnLabels() {

        return labels;
    }

    /**
     * Gets the value for a specified column label, from a given record.
     *
     * @param record the record
     * @param label the label of the required column
     * @return the value of the column for the record
     */
    public String getValue(List<String> record, String label) {

        return record.get(labels.indexOf(label));
    }

    /**
     * Sets the output format.
     *
     * @param output_format the output format
     */
    public void setOutputFormat(CSVFormat output_format) {

        this.output_format = output_format;
    }

    /**
     * Prints the dataset to the given output object.
     *
     * @param out the output object
     * @throws IOException if the dataset cannot be printed to the given output object
     */
    public void print(Appendable out) throws IOException {

        String[] header_array = labels.toArray(new String[labels.size()]);
        CSVPrinter printer = new CSVPrinter(out, output_format.withHeader(header_array));

        for (List<String> record : records) {

            printer.printRecord(record);
            printer.flush();
        }
    }

    private List<List<String>> filterRecords(Selector selector) {

        return records.stream().filter(record -> selector.select(record, this)).collect(Collectors.toList());
    }

    private List<List<String>> projectRecords(Projector projector) {

        return records.stream().map(record -> project(record, projector.getProjectedColumnLabels())).collect(Collectors.toList());
    }

    private List<List<String>> mapRecords(Mapper mapper) {

        Function<List<String>, List<String>> listListFunction = record -> mapper.map(record, this);
        return records.stream().map(listListFunction).collect(Collectors.toList());
    }

    private List<List<String>> extendRecords(Extender extender) {

        List<List<String>> result = new ArrayList<>();

        for (List<String> record : records) {

            List<String> new_record = extender.getAdditionalValues(record, this);
            new_record.addAll(record);
            result.add(new_record);
        }

        return result;
    }

    private List<String> getColumnLabels(CSVParser parser) {

        Map<String, Integer> headerMap = parser.getHeaderMap();
        List<String> labels = new ArrayList<>(headerMap.size());
        for (String label : headerMap.keySet()) {
            int pos = headerMap.get(label);
            labels.add(pos, label);
        }
        return labels;
    }

    private List<String> csvRecordToList(CSVRecord record) {

        List<String> list = new ArrayList<>();
        for (String value : record) {
            list.add(value);
        }
        return list;
    }

    private List<String> project(List<String> record, List<String> projected_columns) {

        List<String> values = new ArrayList<>();

        for (int i = 0; i < record.size(); i++) {
            if (projected_columns.contains(labels.get(i))) {
                values.add(record.get(i));
            }
        }

        return values;
    }
}
