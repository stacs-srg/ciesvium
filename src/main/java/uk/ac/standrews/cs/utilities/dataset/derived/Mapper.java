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

import java.util.List;

/**
 * Interface for transforming records in a dataset.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public interface Mapper {

    /**
     * Creates a new record based on an existing record.
     *
     * @param record   the existing record
     * @param labels the column labels for the dataset within which the record
     * occurs
     * @return a new record based on the existing record
     */
    List<String> mapRecord(List<String> record, List<String> labels);

    List<String> mapColumnLabels(List<String> labels);
}
