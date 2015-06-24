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

    private static final String DATA_FILE_NAME = "csv_test_data.csv";

    private DataSet dataSet;

    @Before
    public void CSVCreatedWithoutError() throws IOException {

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(DATA_FILE_NAME))) {

            dataSet = new DataSet(reader);
        }
    }

    @Test
    public void CSVHasCorrectNumberOfRecords() throws IOException {

        assertEquals(9, dataSet.getRecords().size());
    }

    @Test
    public void CSVRecordHasCorrectNumberOfFields() throws IOException {

        assertEquals(13, dataSet.getRecords().get(0).size());
    }

    @Test
    public void CSVHasExpectedLabels() throws IOException {

        List<String> labels = dataSet.getColumnLabels();
        assertEquals(13, labels.size());
        assertEquals("id", labels.get(0));
        assertTrue(labels.contains("year"));
        assertTrue(labels.contains("hisco_labels"));
        assertTrue(labels.contains("flag"));
    }

    @Test
    public void CSVFilteredByJobTitleGivesExpectedResults() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {
            public boolean select(List<String> record, DataSet original_csv) {

                String job = original_csv.getValue(record, "jobtitle");
                return job.contains("gent");
            }
        });

        assertEquals(4, filtered_dataSet.getRecords().size());
    }

    @Test
    public void CSVFilteredContainsExpectedData() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {
            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("2115");
            }
        });

        assertEquals(1, filtered_dataSet.getRecords().size());
        assertEquals("gentleman", filtered_dataSet.getValue(filtered_dataSet.getRecords().get(0), "jobtitle"));
        assertEquals("1909", filtered_dataSet.getValue(filtered_dataSet.getRecords().get(0), "year"));
    }

    @Test
    public void CSVReadsCommaCorrectly() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {
            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("999");
            }
        });

        assertEquals(1, filtered_dataSet.getRecords().size());
        assertEquals("abc, def", filtered_dataSet.getValue(filtered_dataSet.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVReadsQuotesCorrectly() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {
            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("998");
            }
        });

        assertEquals(1, filtered_dataSet.getRecords().size());
        assertEquals("\"quoted string\"", filtered_dataSet.getValue(filtered_dataSet.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVReadsBackslashesCorrectly() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {
            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("997");
            }
        });

        assertEquals(1, filtered_dataSet.getRecords().size());
        assertEquals("abc \\ def", filtered_dataSet.getValue(filtered_dataSet.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVProjectedGivesExpectedResults() throws IOException {

        DataSet projected_dataSet = new DataSet(dataSet, new Projector() {
            @Override
            public List<String> getProjectedColumnLabels() {
                return Arrays.asList("id", "jobtitle", "flag");
            }
        });

        assertEquals(9, projected_dataSet.getRecords().size());
        assertEquals(3, projected_dataSet.getRecords().get(0).size());
        assertEquals("gentleman", projected_dataSet.getValue(projected_dataSet.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVFilteredAndProjectedGivesExpectedResults() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {
            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("1886");
            }
        });

        DataSet projected_dataSet = new DataSet(filtered_dataSet, new Projector() {
            @Override
            public List<String> getProjectedColumnLabels() {
                return Arrays.asList("id", "jobtitle", "flag");
            }
        });

        assertEquals(1, projected_dataSet.getRecords().size());
        assertEquals(3, projected_dataSet.getRecords().get(0).size());
        assertEquals("independent means", projected_dataSet.getValue(projected_dataSet.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVRoundTripGivesExpectedResults() throws IOException {

        Path tempFile = Files.createTempFile("cvs_test", ".csv");

        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(tempFile))) {

            dataSet.print(writer);
        }

        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(tempFile))) {

            dataSet = new DataSet(reader);

            CSVReadsCommaCorrectly();
            CSVReadsBackslashesCorrectly();
            CSVReadsQuotesCorrectly();
        }
    }
}
