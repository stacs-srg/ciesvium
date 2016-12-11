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
package uk.ac.standrews.cs.util.tools;


import java.text.NumberFormat;
import java.time.Duration;

/**
 * Various simple formatting methods.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class Formatting {

    /**
     * Prints the given value if the current logging level is at or higher than the given threshold.
     *
     * @param info_level the logging threshold level for outputting the metric
     * @param label a label for the value
     * @param value the value
     */
    public static void printMetric(LoggingLevel info_level, String label, double value) {

        Logging.output(info_level, label + ": " + format(value, 2));
    }

    /**
     * Prints the given value if the current logging level is at or higher than the given threshold.
     *
     * @param info_level the logging threshold level for outputting the metric
     * @param label a label for the value
     * @param value the value
     */
    public static void printMetric(LoggingLevel info_level, String label, int value) {

        Logging.output(info_level, label + ": " + format(value));
    }

    /**
     * Returns the given value formatted with the given number of decimal places.
     *
     * @param value the value
     * @param decimal_places the number of decimal places
     * @return the formatted value
     */
    public static String format(double value, int decimal_places) {

        return String.format("%." + decimal_places + "f", value);
    }

    /**
     * Returns the given value formatted with the local number formatting rules.
     *
     * @param value the value
     * @return the formatted value
     */
    public static String format(int value) {

        return NumberFormat.getNumberInstance().format(value);
    }

    /**
     * Returns the given duration formatted as minutes and seconds.
     *
     * @param duration the value
     * @return the formatted value
     */
    public static String format(Duration duration) {

        long seconds = duration.getSeconds();

        int minutes = (int) (seconds / 60);
        int seconds_remaining = (int) (seconds - minutes * 60);

        return String.format("%s:%02d min", minutes, seconds_remaining);
    }
}
