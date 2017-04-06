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

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataSetTest {

    private static final String NON_EMPTY_DATA_SET_FILE_NAME = "csv_test_data.csv";
    private static final String EMPTY_DATA_SET_FILE_NAME = "csv_empty_test_data.csv";
    private static final char DELIMITER = ',';

    private DataSet non_empty_data_set;
    private DataSet empty_data_set;

    @Before
    public void dataSetCreatedWithoutError() throws IOException {

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(NON_EMPTY_DATA_SET_FILE_NAME))) {

            non_empty_data_set = new DataSet(reader, DELIMITER);
        }

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(EMPTY_DATA_SET_FILE_NAME))) {

            empty_data_set = new DataSet(reader);
        }
    }

    @Test
    public void dataSetHasCorrectNumberOfRecords() throws IOException {

        assertEquals(9, non_empty_data_set.getRecords().size());
    }

    @Test
    public void dataSetEmptyDataSetHasCorrectNumberOfRecords() throws IOException {

        assertEquals(0, empty_data_set.getRecords().size());
    }

    @Test
    public void dataSetRecordHasCorrectNumberOfFields() throws IOException {

        assertEquals(4, non_empty_data_set.getRecords().get(0).size());
    }

    @Test
    public void dataSetHasExpectedLabels() throws IOException {

        List<String> labels = non_empty_data_set.getColumnLabels();
        assertEquals(4, labels.size());
        assertEquals("id", labels.get(0));
        assertTrue(labels.contains("col2"));
        assertTrue(labels.contains("col3"));
        assertTrue(labels.contains("col4"));
    }

    @Test
    public void dataSetFilteredGivesExpectedNumberOfResults() throws IOException {

        DataSet filtered_data_set = non_empty_data_set.select((record, original_csv) -> {

            String job = original_csv.getValue(record, "col4");
            return job.contains("jkl");
        });

        assertEquals(5, filtered_data_set.getRecords().size());
    }

    @Test
    public void dataSetFilteredContainsExpectedData() throws IOException {

        DataSet filtered_data_set = non_empty_data_set.select((record, original_csv) -> original_csv.getValue(record, "id").equals("5"));

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals("def", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
        assertEquals("ghi", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col3"));
    }

    @Test
    public void dataSetReadsCommaCorrectly() throws IOException {

        DataSet filtered_data_set = non_empty_data_set.select((record, original_csv) -> original_csv.getValue(record, "id").equals("7"));

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals("def, xyz", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
    }

    @Test
    public void dataSetReadsFieldWithQuotesAndNoCommaCorrectly() {

        DataSet filtered_data_set = non_empty_data_set.select((record, original_csv) -> original_csv.getValue(record, "id").equals("8"));

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals("def", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
    }

    @Test
    public void dataSetReadsEntireFieldWithEscapedQuotesCorrectly() {

        DataSet filtered_data_set = non_empty_data_set.select((record, original_csv) -> original_csv.getValue(record, "id").equals("9"));

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals("\"def\"", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
    }

    @Test
    public void dataSetReadsFieldIncludingEscapedQuotesCorrectly() {

        DataSet filtered_data_set = non_empty_data_set.select((record, original_csv) -> original_csv.getValue(record, "id").equals("10"));

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals("\"def\" ghi", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
    }

    @Test
    public void dataSetReadsUnescapedQuotesWhenNotFirstCharCorrectly() throws IOException {

        DataSet filtered_data_set = non_empty_data_set.select((record, original_csv) -> original_csv.getValue(record, "id").equals("11"));

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals(" \"def\" x", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
    }

    @Test
    public void dataSetReadsBackslashesCorrectly() throws IOException {

        DataSet filtered_data_set = non_empty_data_set.select((record, original_csv) -> original_csv.getValue(record, "id").equals("6"));

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals("ghi \\ xyz", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col3"));
    }

    @Test
    public void dataSetProjectedGivesExpectedResults() throws IOException {

        DataSet projected_data_set = non_empty_data_set.project(() -> Arrays.asList("id", "col3", "col4"));

        assertEquals(9, projected_data_set.getRecords().size());
        assertEquals(3, projected_data_set.getRecords().get(0).size());
        assertEquals("jkl", projected_data_set.getValue(projected_data_set.getRecords().get(0), "col4"));
    }

    @Test
    public void dataSetFilteredAndProjectedGivesExpectedResults() throws IOException {

        DataSet filtered_data_set = non_empty_data_set.select((record, original_csv) -> original_csv.getValue(record, "id").equals("5"));

        DataSet projected_data_set = filtered_data_set.project(() -> Arrays.asList("id", "col3", "col4"));

        assertEquals(1, projected_data_set.getRecords().size());
        assertEquals(3, projected_data_set.getRecords().get(0).size());
        assertEquals("jkl", projected_data_set.getValue(projected_data_set.getRecords().get(0), "col4"));
    }

    @Test
    public void dataSetRoundTripGivesExpectedResults() throws IOException {

        Path temp_path = Files.createTempFile("cvs_test", ".csv");

        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(temp_path))) {

            non_empty_data_set.print(writer);
        }

        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(temp_path))) {

            non_empty_data_set = new DataSet(reader, DELIMITER);

            dataSetReadsCommaCorrectly();
            dataSetReadsBackslashesCorrectly();

            dataSetReadsFieldWithQuotesAndNoCommaCorrectly();
            dataSetReadsEntireFieldWithEscapedQuotesCorrectly();
            dataSetReadsFieldIncludingEscapedQuotesCorrectly();
            dataSetReadsUnescapedQuotesWhenNotFirstCharCorrectly();
        }
    }
}
