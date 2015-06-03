package uk.ac.standrews.cs.util.csv;

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

public class CSVTest {

    private static final String DATA_FILE_NAME = "csv_test_data.csv";

    private CSV csv;

    @Before
    public void CSVCreatedWithoutError() throws IOException {

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(DATA_FILE_NAME))) {

            csv = new CSV(reader);
        }
    }

    @Test
    public void CSVHasCorrectNumberOfRecords() throws IOException {

        assertEquals(9, csv.getRecords().size());
    }

    @Test
    public void CSVRecordHasCorrectNumberOfFields() throws IOException {

        assertEquals(13, csv.getRecords().get(0).size());
    }

    @Test
    public void CSVHasExpectedLabels() throws IOException {

        List<String> labels = csv.getLabels();
        assertEquals(13, labels.size());
        assertEquals("id", labels.get(0));
        assertTrue(labels.contains("year"));
        assertTrue(labels.contains("hisco_labels"));
        assertTrue(labels.contains("flag"));
    }

    @Test
    public void CSVFilteredByJobTitleGivesExpectedResults() throws IOException {

        CSV filtered_csv = new CSV(csv, new Selector() {
            public boolean select(List<String> record, CSV original_csv) {

                String job = original_csv.getValue(record, "jobtitle");
                return job.contains("gent");
            }
        });

        assertEquals(4, filtered_csv.getRecords().size());
    }

    @Test
    public void CSVFilteredContainsExpectedData() throws IOException {

        CSV filtered_csv = new CSV(csv, new Selector() {
            public boolean select(List<String> record, CSV original_csv) {

                return original_csv.getValue(record, "id").equals("2115");
            }
        });

        assertEquals(1, filtered_csv.getRecords().size());
        assertEquals("gentleman", filtered_csv.getValue(filtered_csv.getRecords().get(0), "jobtitle"));
        assertEquals("1909", filtered_csv.getValue(filtered_csv.getRecords().get(0), "year"));
    }

    @Test
    public void CSVReadsCommaCorrectly() throws IOException {

        CSV filtered_csv = new CSV(csv, new Selector() {
            public boolean select(List<String> record, CSV original_csv) {

                return original_csv.getValue(record, "id").equals("999");
            }
        });

        assertEquals(1, filtered_csv.getRecords().size());
        assertEquals("abc, def", filtered_csv.getValue(filtered_csv.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVReadsQuotesCorrectly() throws IOException {

        CSV filtered_csv = new CSV(csv, new Selector() {
            public boolean select(List<String> record, CSV original_csv) {

                return original_csv.getValue(record, "id").equals("998");
            }
        });

        assertEquals(1, filtered_csv.getRecords().size());
        assertEquals("\"quoted string\"", filtered_csv.getValue(filtered_csv.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVReadsBackslashesCorrectly() throws IOException {

        CSV filtered_csv = new CSV(csv, new Selector() {
            public boolean select(List<String> record, CSV original_csv) {

                return original_csv.getValue(record, "id").equals("997");
            }
        });

        assertEquals(1, filtered_csv.getRecords().size());
        assertEquals("abc \\ def", filtered_csv.getValue(filtered_csv.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVProjectedGivesExpectedResults() throws IOException {

        CSV projected_csv = new CSV(csv, new Projector() {
            @Override
            public List<String> getProjectedColumnLabels() {
                return Arrays.asList("id", "jobtitle", "flag");
            }
        });

        assertEquals(9, projected_csv.getRecords().size());
        assertEquals(3, projected_csv.getRecords().get(0).size());
        assertEquals("gentleman", projected_csv.getValue(projected_csv.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVFilteredAndProjectedGivesExpectedResults() throws IOException {

        CSV filtered_csv = new CSV(csv, new Selector() {
            public boolean select(List<String> record, CSV original_csv) {

                return original_csv.getValue(record, "id").equals("1886");
            }
        });

        CSV projected_csv = new CSV(filtered_csv, new Projector() {
            @Override
            public List<String> getProjectedColumnLabels() {
                return Arrays.asList("id", "jobtitle", "flag");
            }
        });

        assertEquals(1, projected_csv.getRecords().size());
        assertEquals(3, projected_csv.getRecords().get(0).size());
        assertEquals("independent means", projected_csv.getValue(projected_csv.getRecords().get(0), "jobtitle"));
    }

    @Test
    public void CSVRoundTripGivesExpectedResults() throws IOException {

        Path tempFile = Files.createTempFile("cvs_test", ".csv");

        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(tempFile))) {

            csv.print(writer);
        }

        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(tempFile))) {

            csv = new CSV(reader);

            CSVReadsCommaCorrectly();
            CSVReadsBackslashesCorrectly();
            CSVReadsQuotesCorrectly();
        }
    }
}
