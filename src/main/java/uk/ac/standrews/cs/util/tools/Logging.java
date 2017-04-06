/*
 * Copyright 2017 Systems Research Group, University of St Andrews:
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
package uk.ac.standrews.cs.util.tools;

/**
 * Simple logging class.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class Logging {

    private static final int NUMBER_OF_PROGRESS_UPDATES = 20;
    private static LoggingLevel logging_level = LoggingLevel.NONE;
    private static ProgressIndicator progress_indicator;

    /**
     * Gets the current logging level.
     *
     * @return the current logging level
     */
    public static LoggingLevel getLoggingLevel() {

        return logging_level;
    }

    /**
     * Sets the current logging level.
     *
     * @param logging_level the new logging level
     */
    public static void setLoggingLevel(LoggingLevel logging_level) {

        Logging.logging_level = logging_level;
    }

    /**
     * Outputs a message if the current logging level is greater than or equal to the given threshold level.
     *
     * @param threshold the threshold
     * @param message the message
     */
    public static void output(LoggingLevel threshold, String message) {

        if (logging_level.compareTo(threshold) >= 0) {
            System.out.println(message);
        }
    }

    /**
     * Outputs a formatted message if the current logging level is greater than or equal to the given threshold level.
     *
     * @param threshold the threshold
     * @param format the format string
     * @param values the values to be formatted
     */
    public static void output(LoggingLevel threshold, String format, String... values) {

        if (logging_level.compareTo(threshold) >= 0) {
            System.out.format(format, (Object[]) values);
        }
    }

    /**
     * Initialises a new progress indicator.
     *
     * @param number_of_steps the number of steps that represent completion
     */
    public static void initialiseProgressIndicator(int number_of_steps) {

        progress_indicator = new PercentageProgressIndicator(NUMBER_OF_PROGRESS_UPDATES);
        progress_indicator.setTotalSteps(number_of_steps);
    }

    /**
     * Records another progress step if the current logging level is greater than or equal to the given threshold level.
     */
    public static void progressStep(LoggingLevel threshold) {

        if (logging_level.compareTo(threshold) >= 0) {
            progress_indicator.progressStep();
        }
    }
}
