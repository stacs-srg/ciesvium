/*
 * Copyright 2021 Systems Research Group, University of St Andrews:
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

import uk.ac.standrews.cs.utilities.archive.QuickSort;
import uk.ac.standrews.cs.utilities.dataset.DataSet;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract superclass for datasets derived from existing datasets via a sequence of relational-style transformations.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
@SuppressWarnings("unused")
public abstract class DerivedDataSet extends DataSet {

    @SuppressWarnings("WeakerAccess")
    protected static final String ID_COLUMN_LABEL = "ID";

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
     * @throws IOException if the derived dataset cannot be obtained
     */
    protected abstract DataSet getDerivedDataSet(DataSet source_data_set) throws IOException;

    @SuppressWarnings("WeakerAccess")
    public static Extender addIdColumn() {

        return new Extender() {

            int record_count = 1;

            @Override
            public List<String> getAdditionalValues(final List<String> record, final DataSet data_set) {

                final List<String> result = new ArrayList<>();
                result.add(String.valueOf(record_count++));
                return result;
            }

            @Override
            public List<String> getColumnLabels() {

                return Collections.singletonList(ID_COLUMN_LABEL);
            }
        };
    }

    @SuppressWarnings("WeakerAccess")
    public static Projector moveIdColumnToFirst(final List<String> source_column_labels) {

        return () -> {
            final List<String> result = new ArrayList<>();

            result.add(ID_COLUMN_LABEL);
            result.addAll(source_column_labels.stream().filter(s -> !s.equals(ID_COLUMN_LABEL)).collect(Collectors.toList()));

            return result;
        };
    }

    public static DataSet renumber(final DataSet data_set) {

        final List<String> source_labels = data_set.getColumnLabels();
        return data_set.project(removeFirstColumn(source_labels)).extend(addIdColumn()).project(moveIdColumnToFirst(source_labels));
    }

    public static DataSet removeDuplicates(final DataSet data_set) {

        final DataSet result = new DataSet(data_set.getColumnLabels());

        final Set<String> processed_rows = new HashSet<>();

        for (final List<String> record : data_set.getRecords()) {

            final String flattened = flatten(record);
            if (!processed_rows.contains(flattened)) {
                result.addRow(record);
                processed_rows.add(flattened);
            }
        }

        return result;
    }

    public static DataSet sort(final DataSet data_set) {

        final DataSet result = new DataSet(data_set.getColumnLabels());

        // Make a map from flattened records to records.
        final Map<String, Pair<Integer, List<String>>> map = new HashMap<>();

        for (final List<String> record : data_set.getRecords()) {

            final String flattened = flatten(record);
            if (!map.containsKey(flattened)) {
                map.put(flattened, new Pair<>(0, record));
            }
            map.get(flattened).x++;
        }

        // Sort the flattened records.
        final List<String> sorted = new ArrayList<>(map.keySet());
        new QuickSort<>(sorted, String::compareTo).sort();

        // Retrieve the structured records.
        for (final String flattened : sorted) {
            final Pair<Integer, List<String>> pair = map.get(flattened);
            for (int i = 0; i < pair.x; i++) {
                result.addRow(pair.y);
            }
        }

        return result;
    }

    static class Pair<X, Y> {

        X x;
        final Y y;

        Pair(final X x, final Y y) {
            this.x = x;
            this.y = y;
        }
    }

    private static String flatten(final List<String> record) {

        return String.join("",record);
    }

    private static Projector removeFirstColumn(final List<String> labels) {

        return () -> labels.subList(1, labels.size());
    }
}
