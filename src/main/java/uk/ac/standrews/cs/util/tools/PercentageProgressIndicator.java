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

/**
 * A progress indicator that prints percentage completion.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public class PercentageProgressIndicator extends ProgressIndicator {

    private static final int HUNDRED_PERCENT = 100;

    /**
     * Creates a progress indicator.
     *
     * @param number_of_updates the number of progress updates to be indicated
     */
    public PercentageProgressIndicator(final int number_of_updates) {
        super(number_of_updates);
    }

    /**
     * Prints the current proportion of completion as a percentage to standard out.
     *
     * @param proportion_complete the current proportion of completion
     */
    public void indicateProgress(final double proportion_complete) {

        System.out.println(Math.round(proportion_complete * HUNDRED_PERCENT) + "%");
    }
}
