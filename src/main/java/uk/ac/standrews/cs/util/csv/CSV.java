package uk.ac.standrews.cs.util.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSV {

    private List<String> labels;
    private List<List<String>> records;

    public CSV(InputStreamReader reader) throws IOException {

        CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180.withHeader());

        records = new ArrayList<>();
        labels = getLabels(parser);

        for (CSVRecord record : parser) {

            records.add(csvRecordToList(record));
        }
    }

    public CSV(CSV existing_records, Selector selector) throws IOException {

        records = existing_records.filterRecords(selector);
        labels = existing_records.getLabels();
    }

    public CSV(CSV existing_records, Projector projector) throws IOException {

        records = existing_records.projectRecords(projector);
        labels = projector.getProjectedColumnLabels();
    }

    public void print(Appendable out) throws IOException {

        String[] header_array = labels.toArray(new String[labels.size()]);
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.RFC4180.withHeader(header_array));

        for (List<String> record : records) {
            printer.printRecord(record);
        }
    }

    public List<List<String>> filterRecords(Selector selector) {

        List<List<String>> filtered_records = new ArrayList<>();

        for (List<String> record : records) {
            if (selector.select(record, this)) {
                filtered_records.add(record);
            }
        }

        return filtered_records;
    }

    public List<List<String>> projectRecords(Projector projector) {

        List<List<String>> projected_records = new ArrayList<>();

        for (List<String> record : records) {
            projected_records.add(project(record, projector.getProjectedColumnLabels()));
        }

        return projected_records;
    }

    public List<List<String>> getRecords() {
        return records;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getValue(List<String> record, String label) {

        return record.get(labels.indexOf(label));
    }

    private List<String> getLabels(CSVParser parser) {

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
