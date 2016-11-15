package uk.ac.standrews.cs.util.dataset;

import java.io.*;
import java.util.*;

public abstract class DerivedDataSet extends DataSet {

    protected static final String ID_COLUMN_LABEL = "ID";

    protected DerivedDataSet() throws IOException {

        init(processDataSet(getSourceDataSet()));
    }

    protected abstract DataSet getSourceDataSet() throws IOException;

    protected abstract DataSet processDataSet(DataSet source_data_set);

    private int record_count;

    protected Extender addIdColumn() {

        record_count = 1;

        return new Extender() {

            @Override
            public List<String> getAdditionalValues(List<String> record, DataSet dataSet) {

                List<String> result = new ArrayList<>();
                result.add(String.valueOf(record_count++));
                return result;
            }

            @Override
            public List<String> getColumnLabels() {

                return Arrays.asList(ID_COLUMN_LABEL);
            }
        };
    }

    protected Projector moveIdColumnToFirst(List<String> source_column_labels) {

        return () -> {
            List<String> result = new ArrayList<>();
            result.add(ID_COLUMN_LABEL);

            for (String source_column_label : source_column_labels) {
                if (!source_column_label.equals(ID_COLUMN_LABEL)) {
                    result.add(source_column_label);
                }
            }
            return result;
        };
    }
}
