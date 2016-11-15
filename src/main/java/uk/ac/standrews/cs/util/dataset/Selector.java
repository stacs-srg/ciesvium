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

import java.util.List;

/**
 * Interface for selecting records from a dataset.
 *
 * @author Graham Kirby (graham.kirby@st-andrews.ac.uk)
 */
public interface Selector {

    /**
     * Determines whether a given record should be selected. The containing dataset is also
     * made available in case the selection logic needs access to the column labels.
     *
     * @param record the record to be considered
     * @param data_set the dataset within which the record occurs
     * @return true if the record should be selected
     */
    boolean select(List<String> record, DataSet data_set);
}
