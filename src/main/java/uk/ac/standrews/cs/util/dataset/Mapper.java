package uk.ac.standrews.cs.util.dataset;

import java.util.*;

public interface Mapper {

    List<String> map(List<String> record, DataSet dataSet);
}
