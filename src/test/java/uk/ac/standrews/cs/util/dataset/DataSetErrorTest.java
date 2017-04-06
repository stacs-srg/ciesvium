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
package uk.ac.standrews.cs.util.dataset;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

public class DataSetErrorTest {

    private static final String DATA_FILE_NAME1 = "csv_error_test_data1.csv";
    private static final String DATA_FILE_NAME2 = "csv_error_test_data2.csv";
    private static final char DELIMITER = ',';

    @Test(expected = RuntimeException.class)
    public void fieldStartingButNotEndingWithQuoteThrowsException() throws IOException {

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(DATA_FILE_NAME1))) {

            new DataSet(reader, DELIMITER);
        }
    }

    @Test(expected = RuntimeException.class)
    public void quotedFieldContainingUnescapedQuotesThrowsException() throws IOException {

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(DATA_FILE_NAME2))) {

            new DataSet(reader, DELIMITER);
        }
    }
}
