package uk.ac.standrews.cs.util.dataset;

import java.io.*;

public abstract class DerivedDataSet extends DataSet {

    protected DerivedDataSet() throws IOException {

        init(processDataSet(getSourceDataSet()));
    }

    protected abstract DataSet getSourceDataSet() throws IOException;
    protected abstract DataSet processDataSet(DataSet source_data_set);
}
