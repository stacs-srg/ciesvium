/*
 * Copyright 2019 Systems Research Group, University of St Andrews:
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
package uk.ac.standrews.cs.utilities.dataset;

import org.junit.Test;
import uk.ac.standrews.cs.utilities.tables.Means;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class StatisticsTest {

    private static final double EPSILON = 0.0000001;

    @Test
    public void meanOfEmptyListIsNaN() {

        assertTrue(Double.isNaN(Means.calculateMean(new ArrayList<>())));
    }

    @Test
    public void meansAreCorrect() {

        assertEquals(1.0, Means.calculateMean(Arrays.asList(1.0)), EPSILON);
        assertEquals(1.5, Means.calculateMean(Arrays.asList(1.0, 2.0)), EPSILON);
        assertEquals(2.0, Means.calculateMean(Arrays.asList(1.0, 2.0, 3.0)), EPSILON);
        assertEquals(-4.9, Means.calculateMean(Arrays.asList(1.0, 2.0, 3.0, -25.6)), EPSILON);
    }
}
