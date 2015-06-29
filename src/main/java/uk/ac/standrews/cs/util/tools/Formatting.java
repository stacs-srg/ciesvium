package uk.ac.standrews.cs.util.tools;


import java.text.NumberFormat;

public class Formatting {

    public static void printMetric(String label, double value) {

        System.out.println(label + ": " + format(value, 2));
    }

    public static void printMetric(String label, int value) {

        System.out.println(label + ": " + format(value));
    }

    public static String format(double value, int decimal_places) {

        return String.format("%." + decimal_places + "f", value);
    }

    public static String format(int value) {

        return NumberFormat.getNumberInstance().format(value);
    }
}
