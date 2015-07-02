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
package uk.ac.standrews.cs.util.tools;


import java.text.NumberFormat;
import java.time.Duration;

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

    public static String format(Duration duration) {

        long seconds = duration.getSeconds();

        int minutes = (int) (seconds / 60);
        int seconds_remaining = (int) (seconds - minutes * 60);

        return String.format("%s:%02d min", minutes, seconds_remaining);
    }
}
