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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProgressIndicatorTest {

    @Test
    public void indicatorShowsExpectedNumberOfSteps() {

        assertEquals(5, getNumberOfIndicatorSteps(5, 100, 100));
        assertEquals(10, getNumberOfIndicatorSteps(5, 50, 100));
        assertEquals(0, getNumberOfIndicatorSteps(0, 100, 100));
        assertEquals(0, getNumberOfIndicatorSteps(0, 0, 100));
        assertEquals(0, getNumberOfIndicatorSteps(5, 0, 100));
    }

    @Test
    public void indicatorShowsExpectedValue() {

        assertEquals(0.4, getLastProportionComplete(5, 100, 40), 0.00001);
    }

    private int getNumberOfIndicatorSteps(int number_of_updates, int total_configured_steps, int total_actual_steps) {

        TestProgressIndicator1 indicator = new TestProgressIndicator1(number_of_updates);

        indicator.setTotalSteps(total_configured_steps);

        for (int i = 0; i < total_actual_steps; i++) {
            indicator.progressStep();
        }

        return indicator.invocation_count;
    }

    private double getLastProportionComplete(int number_of_updates, int total_configured_steps, int total_actual_steps) {

        TestProgressIndicator2 indicator = new TestProgressIndicator2(number_of_updates);

        indicator.setTotalSteps(total_configured_steps);

        for (int i = 0; i < total_actual_steps; i++) {
            indicator.progressStep();
        }

        return indicator.last_value;
    }

    class TestProgressIndicator1 extends ProgressIndicator {

        int invocation_count = 0;

        TestProgressIndicator1(int number_of_updates) {
            super(number_of_updates);
        }

        @Override
        public void indicateProgress(double proportion_complete) {

            invocation_count++;
        }
    }

    class TestProgressIndicator2 extends ProgressIndicator {

        double last_value = 0;

        TestProgressIndicator2(int number_of_updates) {
            super(number_of_updates);
        }

        @Override
        public void indicateProgress(double proportion_complete) {

            last_value = proportion_complete;
        }
    }
}
