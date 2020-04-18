/*
 * Copyright 2020 Systems Research Group, University of St Andrews:
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

import java.util.List;

/**
 * Interface for creating new columns for a dataset.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public interface Extender {

    /**
     * Gets the additional values to be appended to a given record. The containing dataset is also
     * made available in case the extender logic needs access to the column labels.
     *
     * @param record   the existing record
     * @param data_set the dataset within which the record occurs
     * @return a list of new values to be appended to the record in the extended dataset
     */
    @SuppressWarnings("UnusedDeclaration")
    List<String> getAdditionalValues(List<String> record, DataSet data_set);

    /**
     * Gets the labels for the new columns.
     *
     * @return the labels for the new columns
     */
    List<String> getColumnLabels();
}
