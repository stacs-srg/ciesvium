/*
 * Copyright 2016 Digitising Scotland project:
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
