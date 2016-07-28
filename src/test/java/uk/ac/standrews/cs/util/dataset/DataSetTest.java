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
    private static final char DELIMITER = ',';

    private DataSet dataSet;

    @Before
    public void CSVCreatedWithoutError() throws IOException {

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(DATA_FILE_NAME))) {

            dataSet = new DataSet(reader, DELIMITER);
        }
    }

    @Test
    public void CSVHasCorrectNumberOfRecords() throws IOException {

        assertEquals(9, dataSet.getRecords().size());
    }

    @Test
    public void CSVRecordHasCorrectNumberOfFields() throws IOException {

        assertEquals(4, dataSet.getRecords().get(0).size());
    }

    @Test
    public void CSVHasExpectedLabels() throws IOException {

        List<String> labels = dataSet.getColumnLabels();
        assertEquals(4, labels.size());
        assertEquals("id", labels.get(0));
        assertTrue(labels.contains("col2"));
        assertTrue(labels.contains("col3"));
        assertTrue(labels.contains("col4"));
    }

    @Test
    public void CSVFilteredGivesExpectedNumberOfResults() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {

            public boolean select(List<String> record, DataSet original_csv) {

                String job = original_csv.getValue(record, "col4");
                return job.contains("jkl");
            }
        });

        assertEquals(5, filtered_dataSet.getRecords().size());
    }

    @Test
    public void CSVFilteredContainsExpectedData() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {

            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("5");
            }
        });

        assertEquals(1, filtered_dataSet.getRecords().size());
        assertEquals("def", filtered_dataSet.getValue(filtered_dataSet.getRecords().get(0), "col2"));
        assertEquals("ghi", filtered_dataSet.getValue(filtered_dataSet.getRecords().get(0), "col3"));
    }

    @Test
    public void CSVReadsCommaCorrectly() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {

            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("7");
            }
        });

        assertEquals(1, filtered_dataSet.getRecords().size());
        assertEquals("def, xyz", filtered_dataSet.getValue(filtered_dataSet.getRecords().get(0), "col2"));
    }

    @Test
    public void CSVReadsFieldWithQuotesAndNoCommaCorrectly() {

        DataSet filtered_data_set = new DataSet(dataSet, new Selector() {

            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("8");
            }
        });

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals("def", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
    }

    @Test
    public void CSVReadsEntireFieldWithEscapedQuotesCorrectly() {

        DataSet filtered_data_set = new DataSet(dataSet, new Selector() {

            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("9");
            }
        });

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals("\"def\"", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
    }

    @Test
    public void CSVReadsFieldIncludingEscapedQuotesCorrectly() {

        DataSet filtered_data_set = new DataSet(dataSet, new Selector() {

            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("10");
            }
        });

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals("\"def\" ghi", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
    }

    @Test
    public void CSVReadsUnescapedQuotesWhenNotFirstCharCorrectly() throws IOException {

        DataSet filtered_data_set = new DataSet(dataSet, new Selector() {

            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("11");
            }
        });

        assertEquals(1, filtered_data_set.getRecords().size());
        assertEquals(" \"def\" x", filtered_data_set.getValue(filtered_data_set.getRecords().get(0), "col2"));
    }

    @Test
    public void CSVReadsBackslashesCorrectly() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {

            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("6");
            }
        });

        assertEquals(1, filtered_dataSet.getRecords().size());
        assertEquals("ghi \\ xyz", filtered_dataSet.getValue(filtered_dataSet.getRecords().get(0), "col3"));
    }

    @Test
    public void CSVProjectedGivesExpectedResults() throws IOException {

        DataSet projected_dataSet = new DataSet(dataSet, new Projector() {

            @Override
            public List<String> getProjectedColumnLabels() {

                return Arrays.asList("id", "col3", "col4");
            }
        });

        assertEquals(9, projected_dataSet.getRecords().size());
        assertEquals(3, projected_dataSet.getRecords().get(0).size());
        assertEquals("jkl", projected_dataSet.getValue(projected_dataSet.getRecords().get(0), "col4"));
    }

    @Test
    public void CSVFilteredAndProjectedGivesExpectedResults() throws IOException {

        DataSet filtered_dataSet = new DataSet(dataSet, new Selector() {

            public boolean select(List<String> record, DataSet original_csv) {

                return original_csv.getValue(record, "id").equals("5");
            }
        });

        DataSet projected_dataSet = new DataSet(filtered_dataSet, new Projector() {

            @Override
            public List<String> getProjectedColumnLabels() {

                return Arrays.asList("id", "col3", "col4");
            }
        });

        assertEquals(1, projected_dataSet.getRecords().size());
        assertEquals(3, projected_dataSet.getRecords().get(0).size());
        assertEquals("jkl", projected_dataSet.getValue(projected_dataSet.getRecords().get(0), "col4"));
    }

    @Test
    public void CSVRoundTripGivesExpectedResults() throws IOException {

        Path tempFile = Files.createTempFile("cvs_test", ".csv");

        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(tempFile))) {

            dataSet.print(writer);
        }

        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(tempFile))) {

            dataSet = new DataSet(reader, DELIMITER);

            CSVReadsCommaCorrectly();
            CSVReadsBackslashesCorrectly();

            CSVReadsFieldWithQuotesAndNoCommaCorrectly();
            CSVReadsEntireFieldWithEscapedQuotesCorrectly();
            CSVReadsFieldIncludingEscapedQuotesCorrectly();
            CSVReadsUnescapedQuotesWhenNotFirstCharCorrectly();
        }
    }
}
