/*
 * Copyright 2015 Digitising Scotland project:
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
package uk.ac.standrews.cs.util.dataset;

import org.junit.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.Assert.*;

public class DataSetErrorTest {

    private static final String DATA_FILE_NAME = "csv_error_test_data.csv";
    private static final char DELIMITER = ',';

    @Test(expected = RuntimeException.class)
    public void fieldStartingButNotEndingWithQuoteThrowsException() throws IOException {

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(DATA_FILE_NAME))) {

            new DataSet(reader, DELIMITER);
        }
    }
}
