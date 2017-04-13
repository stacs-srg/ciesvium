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
package uk.ac.standrews.cs.utilities.dataset.derived;

import uk.ac.standrews.cs.utilities.dataset.DataSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract superclass for datasets derived from existing datasets via a sequence of relational-style transformations.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
@SuppressWarnings("unused")
public abstract class DerivedDataSet extends DataSet {

    private static final String ID_COLUMN_LABEL = "ID";

    private int record_count;

    protected DerivedDataSet() throws IOException {

        init(getDerivedDataSet(getSourceDataSet()));
    }

    /**
     * Gets the source dataset.
     *
     * @return the source dataset
     * @throws IOException if the source dataset cannot be obtained
     */
    @SuppressWarnings("WeakerAccess")
    public abstract DataSet getSourceDataSet() throws IOException;

    /**
     * Gets the derived dataset.
     *
     * @param source_data_set the source dataset
     * @return the derived dataset
     */
    @SuppressWarnings("WeakerAccess")
    public abstract DataSet getDerivedDataSet(DataSet source_data_set);

    protected Extender addIdColumn() {

        record_count = 1;

        return new Extender() {

            @Override
            public List<String> getAdditionalValues(List<String> record, DataSet data_set) {

                List<String> result = new ArrayList<>();
                result.add(String.valueOf(record_count++));
                return result;
            }

            @Override
            public List<String> getColumnLabels() {

                return Collections.singletonList(ID_COLUMN_LABEL);
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
