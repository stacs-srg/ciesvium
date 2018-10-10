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
package uk.ac.standrews.cs.utilities.dataset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import uk.ac.standrews.cs.utilities.FileManipulation;
import uk.ac.standrews.cs.utilities.dataset.derived.Extender;
import uk.ac.standrews.cs.utilities.dataset.derived.Mapper;
import uk.ac.standrews.cs.utilities.dataset.derived.Projector;
import uk.ac.standrews.cs.utilities.dataset.derived.Selector;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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
    @SuppressWarnings("WeakerAccess")
    public static final CSVFormat DEFAULT_CSV_FORMAT = CSVFormat.RFC4180;

    /**
     * The default delimiter.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String DEFAULT_DELIMITER = ",";

    private List<String> labels;
    private List<List<String>> records;

    private CSVFormat output_format = DEFAULT_CSV_FORMAT;

    /**
     * Creates a new empty dataset with given column labels.
     *
     * @param labels the column labels
     */
    public DataSet(final List<String> labels) {

        this(labels, new ArrayList<>());
    }

    /**
     * Creates a new dataset containing a copy of the given dataset.
     *
     * @param existing_records the dataset to copy
     */
    @SuppressWarnings("WeakerAccess")
    public DataSet(final DataSet existing_records) {

        init(existing_records);
    }

    /**
     * Creates a new dataset with column labels and data read from a file with the given path.
     *
     * @param path the path of the file to read column labels and data from
     * @throws IOException if the file cannot be read
     */
    @SuppressWarnings("WeakerAccess")
    public DataSet(final Path path) throws IOException {

        this(FileManipulation.getInputStreamReader(path));
    }

    /**
     * Creates a new dataset with column labels and data read from the given Reader, using the default delimiter: {@value #DEFAULT_DELIMITER}.
     *
     * @param reader the Reader to read column labels and data from
     */
    public DataSet(final Reader reader) {

        this(reader, DEFAULT_DELIMITER.charAt(0));
    }

    /**
     * Creates a new dataset with column labels and data read from the given Reader, using the default CSV input format and a specified delimiter.
     *
     * @param reader    the Reader to read column labels and data from
     * @param delimiter the delimiter for labels and values
     */
    public DataSet(final Reader reader, final char delimiter) {

        this(reader, DEFAULT_CSV_FORMAT.withDelimiter(delimiter));
    }

    /**
     * Creates a new dataset with column labels and data read from the given Reader, using a specified input format.
     *
     * @param reader       the Reader to read column labels and data from
     * @param input_format the format
     */
    @SuppressWarnings("WeakerAccess")
    public DataSet(final Reader reader, final CSVFormat input_format) {

        this();

        try (final CSVParser parser = new CSVParser(reader, input_format.withHeader())) {

            labels.addAll(getColumnLabels(parser));

            for (final CSVRecord record : parser) {

                final List<String> items = csvRecordToList(record);
                final int size = items.size();

                // Don't add row if the line was empty.
                if (size > 1 || (size == 1 && items.get(0).length() > 0)) {
                    records.add(items);
                }
            }

            reader.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new dataset from this dataset, with the same column labels and selected rows.
     *
     * @param selector a selector to determine which rows should be included
     * @return the new dataset
     */
    @SuppressWarnings("WeakerAccess")
    public DataSet select(final Selector selector) {

        return new DataSet(labels, filterRecords(selector));
    }

    /**
     * Creates a new dataset from this dataset, with specified columns.
     *
     * @param projector a projector to determine which columns should be included
     * @return the new dataset
     */
    @SuppressWarnings("WeakerAccess")
    public DataSet project(final Projector projector) {

        return new DataSet(projector.getProjectedColumnLabels(), projectRecords(projector));
    }

    /**
     * Creates a new dataset from this dataset, with each row transformed in a specified way.
     *
     * @param mapper a mapper to transform each row into a new row in the output dataset
     * @return the new dataset
     */
    @SuppressWarnings("unused")
    public DataSet map(final Mapper mapper) {

        return new DataSet(labels, mapRecords(mapper));
    }

    /**
     * Creates a new dataset from this dataset, with additional generated columns.
     *
     * @param extender an extender to generate additional column labels and values
     * @return the new dataset
     */
    @SuppressWarnings("unused")
    public DataSet extend(final Extender extender) {

        return new DataSet(extendLabels(extender), extendRecords(extender));
    }

    /**
     * Adds a new record to this dataset, specified as a list of strings.
     *
     * @param record a new record
     */
    public void addRow(final List<String> record) {

        records.add(record);
    }

    /**
     * Adds a new record to this dataset, specified as a number of strings.
     *
     * @param values a new record
     */
    public void addRow(final String... values) {

        addRow(Arrays.asList(values));
    }

    /**
     * Gets the records of this dataset.
     *
     * @return the records
     */
    public List<List<String>> getRecords() {

        return records;
    }

    /**
     * Gets the column labels of this dataset.
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
     * @param label  the label of the required column
     * @return the value of the column for the record
     * @throws RuntimeException if the specified label is not present
     */
    @SuppressWarnings("WeakerAccess")
    public String getValue(final List<String> record, final String label) {

        final int index = labels.indexOf(label);

        if (index == -1) {
            throw new RuntimeException("Unknown label: " + label);
        }
        return record.get(index);
    }

    /**
     * Sets the output format used by {@link #print(Appendable)}.
     *
     * @param output_format the output format
     */
    @SuppressWarnings("unused")
    public void setOutputFormat(final CSVFormat output_format) {

        this.output_format = output_format;
    }

    /**
     * Prints this dataset to the given output object.
     *
     * @param out the output object
     * @throws IOException if this dataset cannot be printed to the given output object
     */
    public void print(final Appendable out) throws IOException {

        final String[] header_array = labels.toArray(new String[0]);
        try (final CSVPrinter printer = new CSVPrinter(out, output_format.withHeader(header_array))) {

            for (final List<String> record : records) {

                printer.printRecord(record);
                printer.flush();
            }
        }
    }

    /**
     * Prints this dataset to the given file.
     *
     * @param path the path of the output file
     * @throws IOException if this dataset cannot be printed to the given file
     */
    @SuppressWarnings("unused")
    public void print(final Path path) throws IOException {

        try (final Writer writer = Files.newBufferedWriter(path)) {
            print(writer);
        }
    }

    @SuppressWarnings("NonFinalFieldReferenceInEquals")
    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DataSet other_dataset = (DataSet) o;

        return labels.equals(other_dataset.labels) && records.equals(other_dataset.records);
    }

    @SuppressWarnings("NonFinalFieldReferencedInHashCode")
    @Override
    public int hashCode() {

        int result = labels.hashCode();
        result = 31 * result + records.hashCode();
        return result;
    }

    protected DataSet() {

        this(new ArrayList<>(), new ArrayList<>());
    }

    private DataSet(final List<String> labels, final List<List<String>> records) {

        init(labels, records);
    }

    private void init(final List<String> labels, final List<List<String>> records) {

        this.labels = labels;
        this.records = records;
    }

    protected void init(final DataSet existing_records) {

        init(existing_records.getColumnLabels(), existing_records.getRecords());
    }

    private List<List<String>> filterRecords(final Selector selector) {

        return records.stream().filter(record -> selector.select(record, this)).collect(Collectors.toList());
    }

    private List<List<String>> projectRecords(final Projector projector) {

        return records.stream().map(record -> project(record, projector.getProjectedColumnLabels())).collect(Collectors.toList());
    }

    private List<List<String>> mapRecords(final Mapper mapper) {

        final Function<List<String>, List<String>> listListFunction = record -> mapper.map(record, this);
        return records.stream().map(listListFunction).collect(Collectors.toList());
    }

    private List<String> extendLabels(final Extender extender) {

        final List<String> extended_labels = new ArrayList<>(labels);
        extended_labels.addAll(extender.getColumnLabels());
        return extended_labels;
    }

    private List<List<String>> extendRecords(final Extender extender) {

        final List<List<String>> result = new ArrayList<>();

        for (final List<String> record : records) {

            final List<String> new_record = new ArrayList<>(record);
            new_record.addAll(extender.getAdditionalValues(record, this));
            result.add(new_record);
        }

        return result;
    }

    private static List<String> getColumnLabels(final CSVParser parser) {

        final Map<String, Integer> headerMap = parser.getHeaderMap();
        final List<String> labels = new ArrayList<>(headerMap.size());
        for (final String label : headerMap.keySet()) {
            final int pos = headerMap.get(label);
            labels.add(pos, label);
        }
        return labels;
    }

    private static List<String> csvRecordToList(final CSVRecord record) {

        final List<String> list = new ArrayList<>();
        for (final String value : record) {
            list.add(value);
        }
        return list;
    }

    private List<String> project(final List<String> record, final List<String> projected_columns) {

        if (containsDuplicates(projected_columns)) {
            // Can't throw checked exception because this is used in a stream map operation.
            throw new RuntimeException("duplicate column labels in projection");
        }

        return projected_columns.stream().map(projected_column_label -> getValue(record, projected_column_label)).collect(Collectors.toList());
    }

    private static boolean containsDuplicates(final List<String> strings) {

        return new HashSet<>(strings).size() < strings.size();
    }
}
