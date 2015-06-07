package uk.ac.standrews.cs.util.tables;

import java.util.List;

public class Means extends StatisticValues {

    public Means(List<List<Double>> data) {

        super(data);
    }

    protected double calculate(List<Double> values) {

        double total = 0;
        for (Double value : values) {
            total += value;
        }
        return total / values.size();
    }
}
