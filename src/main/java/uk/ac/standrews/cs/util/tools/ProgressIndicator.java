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

import java.util.concurrent.atomic.*;

/**
 * Created by graham on 14/05/2014.
 */
public abstract class ProgressIndicator {

    private final AtomicInteger number_of_updates;
    private final AtomicInteger number_of_steps_since_last_update = new AtomicInteger();
    private final AtomicInteger total_steps = new AtomicInteger();
    private final AtomicInteger number_of_steps_completed = new AtomicInteger();
    private final AtomicInteger number_of_steps_per_update = new AtomicInteger();

    private double proportion_complete;

    public ProgressIndicator(final int number_of_updates) {

        this.number_of_updates = new AtomicInteger(number_of_updates);
        number_of_steps_since_last_update.set(0);
    }

    public void setTotalSteps(final int total_steps) {

        this.total_steps.set(total_steps);
        number_of_steps_per_update.set(total_steps / number_of_updates.get());
    }

    public void progressStep() {

        number_of_steps_completed.getAndIncrement();
        number_of_steps_since_last_update.getAndIncrement();

        if (number_of_steps_since_last_update.get() >= number_of_steps_per_update.get()) {

            proportion_complete = (double) number_of_steps_completed.get() / (double) total_steps.get();
            number_of_steps_since_last_update.set(0);

            indicateProgress(proportion_complete);
        }
    }

    public double getProportionComplete() {

        return proportion_complete;
    }

    public abstract void indicateProgress(final double proportion_complete);
}
