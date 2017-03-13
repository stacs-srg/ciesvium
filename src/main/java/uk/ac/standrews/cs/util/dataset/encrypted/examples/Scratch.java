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
