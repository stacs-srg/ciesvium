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
package uk.ac.standrews.cs.util.dataset.encrypted.examples;

import uk.ac.standrews.cs.util.dataset.DataSet;
import uk.ac.standrews.cs.util.dataset.encrypted.EncryptedDataSet;

import java.util.Arrays;
import java.util.List;

public class Scratch {

    public static void main(String[] args) {

        List<String> headings = Arrays.asList("heading 1", "heading 2", "heading 3");

        EncryptedDataSet source_data_set1 = new EncryptedDataSet(headings);
        source_data_set1.addRow("the", "quick", "brown", "fox");

        EncryptedDataSet source_data_set = new EncryptedDataSet(new DataSet(headings));
    }
}
