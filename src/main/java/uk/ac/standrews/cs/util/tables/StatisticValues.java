package uk.ac.standrews.cs.util.tables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class StatisticValues {

    protected List<List<Double>> data;
    protected List<Double> results;

    public StatisticValues(List<List<Double>> data) {

        this.data = data;
        results = new ArrayList<>();

        int size = data.get(0).size();
        for (int column_number = 0; column_number < size; column_number++) {
            List<Double> column = getColumn(column_number);
            results.add(calculate(column));
        }
    }

    public List<Double> getResults() {
        return results;
    }

    private List<Double> getColumn(int column_number) {

        List<Double> column = new ArrayList<>();
        for (List<Double> value : data) {
            column.add(value.get(column_number));
        }
        return column;
    }

    protected abstract double calculate(List<Double> column);

    public static List<List<Double>> parseStrings(List<List<String>> records) throws IOException {

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
}
