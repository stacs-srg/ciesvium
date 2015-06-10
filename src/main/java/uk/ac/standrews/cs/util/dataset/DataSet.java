package uk.ac.standrews.cs.util.dataset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSet {

    public static final CSVFormat DEFAULT_CSV_FORMAT = CSVFormat.RFC4180;

    private final List<String> labels;
    private final List<List<String>> records;

    private CSVFormat output_format = DEFAULT_CSV_FORMAT;

    private DataSet() {

        this(new ArrayList<String>(), new ArrayList<List<String>>());
    }

    public DataSet(List<String> labels) {

        this(labels, new ArrayList<List<String>>());
    }

    private DataSet(List<String> labels, List<List<String>> records) {

        this.labels = labels;
        this.records = records;
    }

    public DataSet(InputStreamReader reader) throws IOException {

        this(reader, DEFAULT_CSV_FORMAT);
    }

    public DataSet(InputStreamReader reader, CSVFormat input_format) throws IOException {

        this();

        try (CSVParser parser = new CSVParser(reader, input_format.withHeader())) {

            labels.addAll(getColumnLabels(parser));

            for (CSVRecord record : parser) {
                records.add(csvRecordToList(record));
            }
        }
    }

    public DataSet(DataSet existing_records, Selector selector) throws IOException {

        this(existing_records.getColumnLabels(), existing_records.filterRecords(selector));
    }

    public DataSet(DataSet existing_records, Projector projector) throws IOException {

        this(projector.getProjectedColumnLabels(), existing_records.projectRecords(projector));
    }

    public void setOutputFormat(CSVFormat output_format){

        this.output_format = output_format;
    }

    public void addRow(List<String> record) {

        records.add(record);
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

        List<List<String>> filtered_records = new ArrayList<>();

        for (List<String> record : records) {
            if (selector.select(record, this)) {
                filtered_records.add(record);
            }
        }

        return filtered_records;
    }

    private List<List<String>> projectRecords(Projector projector) {

        List<List<String>> projected_records = new ArrayList<>();

        for (List<String> record : records) {
            projected_records.add(project(record, projector.getProjectedColumnLabels()));
        }

        return projected_records;
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
