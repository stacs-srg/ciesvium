/*
 * Copyright 2015 Digitising Scotland project:
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

public class DataSet {

    public static final CSVFormat DEFAULT_CSV_FORMAT = CSVFormat.RFC4180;

    private final List<String> labels;
    private final List<List<String>> records;

    private CSVFormat output_format = DEFAULT_CSV_FORMAT;
    private static final char DEFAULT_DELIMITER = ',';

    private DataSet() {

        this(new ArrayList<>(), new ArrayList<>());
    }

    public DataSet(List<String> labels) {

        this(labels, new ArrayList<>());
    }

    private DataSet(List<String> labels, List<List<String>> records) {

        this.labels = labels;
        this.records = records;
    }

    public DataSet(Reader reader) {

        this(reader, DEFAULT_DELIMITER);
    }

    public DataSet(Reader reader, char delimiter) {

        this(reader, DEFAULT_CSV_FORMAT.withDelimiter(delimiter));
    }

    public DataSet(Reader reader, CSVFormat input_format) {

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DataSet(Path path) throws IOException {

        this(FileManipulation.getInputStreamReader(path));
    }

    public DataSet(DataSet existing_records, Selector selector) {

        this(existing_records.getColumnLabels(), existing_records.filterRecords(selector));
    }

    public DataSet(DataSet existing_records, Projector projector) {

        this(projector.getProjectedColumnLabels(), existing_records.projectRecords(projector));
    }

    public DataSet(DataSet existing_records, Mapper mapper) {

        this(existing_records.getColumnLabels(), existing_records.mapRecords(mapper));
    }

    public DataSet(DataSet existing_records, Extender extender) {

        this(extender.getColumnLabels(), existing_records.extendRecords(extender));
    }

    public void setOutputFormat(CSVFormat output_format) {

        this.output_format = output_format;
    }

    public void addRow(List<String> record) {

        records.add(record);
    }

    public void addRow(String... values) {

        addRow(Arrays.asList(values));
    }

    public List<List<String>> getRecords() {
        return records;
    }

    public List<String> getColumnLabels() {
        return labels;
    }

    public String getValue(List<String> record, String label) {

        return record.get(labels.indexOf(label));
    }

    public void print(Appendable out) throws IOException {

        String[] header_array = labels.toArray(new String[labels.size()]);
        CSVPrinter printer = new CSVPrinter(out, output_format.withHeader(header_array));

        for (List<String> record : records) {
            printer.printRecord(record);
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
