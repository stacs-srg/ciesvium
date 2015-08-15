/*
 * Copyright 2015 Digitising Scotland project:
 * <http://digitisingscotland.cs.st-andrews.ac.uk/>
 *
 * This file is part of the module record_classification.
 *
 * record_classification is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * record_classification is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with record_classification. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.standrews.cs.util.tools;

import uk.ac.standrews.cs.digitising_scotland.util.PercentageProgressIndicator;
import uk.ac.standrews.cs.digitising_scotland.util.ProgressIndicator;

public class Logging {

    private static final int NUMBER_OF_PROGRESS_UPDATES = 20;
    private static InfoLevel info_level = InfoLevel.NONE;
    private static ProgressIndicator progress_indicator;

    public static InfoLevel getInfoLevel() {

        return info_level;
    }

    public static void setInfoLevel(InfoLevel info_level) {

        Logging.info_level = info_level;
    }

    public static void output(InfoLevel threshold, String message) {

        if (info_level.compareTo(threshold) >= 0) {
            System.out.println(message);
        }
    }

    public static void output(InfoLevel threshold, String message, String... values) {

        if (info_level.compareTo(threshold) >= 0) {
            System.out.format(message, values);
        }
    }

    public static void setProgressIndicatorSteps(int number_of_steps) {

        progress_indicator = new PercentageProgressIndicator(NUMBER_OF_PROGRESS_UPDATES);
        progress_indicator.setTotalSteps(number_of_steps);
    }

    public static void progressStep() {

        if (info_level != InfoLevel.NONE) {
            progress_indicator.progressStep();
        }
    }
}
