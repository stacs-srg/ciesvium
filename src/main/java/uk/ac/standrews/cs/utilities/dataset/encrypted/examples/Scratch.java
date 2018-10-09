/*
 * Copyright 2018 Systems Research Group, University of St Andrews:
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
package uk.ac.standrews.cs.utilities.dataset.encrypted.examples;

import uk.ac.standrews.cs.utilities.dataset.DataSet;
import uk.ac.standrews.cs.utilities.dataset.encrypted.EncryptedDataSet;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Scratch {

    @SuppressWarnings("WeakerAccess")
    public static void main(final String[] args) {

        final List<String> headings = Arrays.asList("heading 1", "heading 2", "heading 3");

        final EncryptedDataSet source_data_set1 = new EncryptedDataSet(headings);
        source_data_set1.addRow("the", "quick", "brown", "fox");

        @SuppressWarnings("UnusedAssignment") final EncryptedDataSet source_data_set = new EncryptedDataSet(new DataSet(headings));
    }
}
