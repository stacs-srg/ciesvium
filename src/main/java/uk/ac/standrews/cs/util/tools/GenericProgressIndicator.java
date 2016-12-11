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

import java.util.function.*;

/**
 * Progress indicator that passes on progress step information to a given consumer.
 *
 * @author Masih Hajiarab Derkani (m@derkani.org)
 */
public class GenericProgressIndicator extends ProgressIndicator {

    private Consumer<Double> progress_consumer;

    /**
     * Creates a progress indicator with a given progress consumer.
     *
     * @param number_of_updates the number of progress updates to be indicated
     * @param progress_consumer the progress consumer
     */
    public GenericProgressIndicator(final int number_of_updates, Consumer<Double> progress_consumer) {

        super(number_of_updates);
        this.progress_consumer = progress_consumer;
    }

    /**
     * Indicates progress to a given proportion of completion.
     *
     * @param proportion_complete the proportion complete
     */
    @Override
    public void indicateProgress(final double proportion_complete) {

        if (progress_consumer != null) {
            progress_consumer.accept(proportion_complete);
        }
    }
}
