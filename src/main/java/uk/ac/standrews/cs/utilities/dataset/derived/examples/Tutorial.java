/*
 * Copyright 2019 Systems Research Group, University of St Andrews:
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
package uk.ac.standrews.cs.utilities.dataset.derived.examples;

import uk.ac.standrews.cs.utilities.dataset.DataSet;
import uk.ac.standrews.cs.utilities.dataset.derived.DerivedDataSet;
import uk.ac.standrews.cs.utilities.dataset.derived.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Tutorial {

    // These methods are defined in order to check that website examples compile.

    public static void main(final String[] args) throws IOException {

        final DerivedDataSet set1 = new DerivedDataWithIDColumn();
        set1.getSourceDataSet().print(System.out);
        set1.print(System.out);
        System.out.println();

        final DataSet set1_5 = makeDerivedDataSet(makeDataSet());
        set1_5.print(System.out);
        System.out.println();

        final DataSet set2 = new DerivedDataWithIDColumnFirst();
        set2.print(System.out);
        System.out.println();

        final DataSet set3 = new DerivedDataWithFilteredRows();
        set3.print(System.out);
        System.out.println();

        final DataSet set4 = new DerivedDataWithFilteredRowsAndRenumbered();
        set4.print(System.out);
        System.out.println();

        final DataSet set5 = new DerivedDataWithCapitalLetters();
        set5.print(System.out);
    }

    public static DataSet makeDataSet() {

        final DataSet data = new DataSet(Arrays.asList("col1", "col2", "col3"));

        data.addRow(Arrays.asList("a", "b", "c"));
        data.addRow(Arrays.asList("d", "e", "f"));

        return data;
    }

    public static class DerivedDataWithIDColumn extends DerivedDataSet {

        protected DerivedDataWithIDColumn() throws IOException {
        }

        @Override
        public DataSet getSourceDataSet() {

            final DataSet data = new DataSet(Arrays.asList("col1", "col2", "col3"));

            data.addRow(Arrays.asList("a", "b", "c"));
            data.addRow(Arrays.asList("d", "e", "f"));

            return data;
        }

        @Override
        public DataSet getDerivedDataSet(final DataSet source_data_set) {

            return source_data_set.extend(addIdColumn());
        }
    }

    public static DerivedDataSet makeDerivedDataSet(final DataSet original) throws IOException {

        return new DerivedDataSet() {

            @Override
            public DataSet getSourceDataSet() {
                return original;
            }

            @Override
            public DataSet getDerivedDataSet(final DataSet source_data_set) {
                return source_data_set.extend(addIdColumn());
            }
        };
    }

    public static class DerivedDataWithIDColumnFirst extends DerivedDataSet {

        protected DerivedDataWithIDColumnFirst() throws IOException {
        }

        @Override
        public DataSet getSourceDataSet() throws IOException {

            return new DerivedDataWithIDColumn();
        }

        @Override
        public DataSet getDerivedDataSet(final DataSet source_data_set) {

            return source_data_set.project(moveIdColumnToFirst(source_data_set.getColumnLabels()));
        }
    }

    public static class DerivedDataWithFilteredRows extends DerivedDataSet {

        protected DerivedDataWithFilteredRows() throws IOException {
        }

        @Override
        public DataSet getSourceDataSet() throws IOException {

            return new DerivedDataWithIDColumnFirst();
        }

        @Override
        public DataSet getDerivedDataSet(final DataSet source_data_set) {

            return source_data_set.select((record, data_set) -> !record.get(2).equals("b"));
        }
    }

    public static class DerivedDataWithFilteredRowsAndRenumbered extends DerivedDataSet {

        protected DerivedDataWithFilteredRowsAndRenumbered() throws IOException {
        }

        @Override
        public DataSet getSourceDataSet() throws IOException {

            return new DerivedDataWithFilteredRows();
        }

        @Override
        public DataSet getDerivedDataSet(final DataSet source_data_set) {

            return renumber(source_data_set);
        }
    }

    public static class DerivedDataWithCapitalLetters extends DerivedDataSet {

        protected DerivedDataWithCapitalLetters() throws IOException {
        }

        @Override
        public DataSet getSourceDataSet() throws IOException {

            return new DerivedDataWithIDColumnFirst();
        }

        @Override
        public DataSet getDerivedDataSet(final DataSet source_data_set) {

            return source_data_set.map(new Mapper() {

                @Override
                public List<String> mapRecord(final List<String> record, final List<String> labels) {
                    final List<String> new_row = new ArrayList<>();

                    for (final String element : record) {
                        new_row.add(element.toUpperCase());
                    }
                    return new_row;
                }

                @Override
                public List<String> mapColumnLabels(final List<String> labels) {
                    return labels;
                }
            });
        }
    }
}
