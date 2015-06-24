package uk.ac.standrews.cs.util.dataset;

import java.util.List;

public interface Selector {

    boolean select(List<String> record, DataSet dataSet);
}
