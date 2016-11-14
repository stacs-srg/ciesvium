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

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

public abstract class DataSetProcessor {

    protected DataSet processed_data_set;
    private PrintStream print_stream = System.out;

    public DataSetProcessor() throws IOException {

        Selector selector = getSelector();
        Projector projector = getProjector();
        Mapper mapper = getMapper();
        Extender extender = getExtender();

        processed_data_set = getSourceDataSet();

        if (selector != null) {
            processed_data_set = new DataSet(processed_data_set, selector);
        }

        if (projector != null) {
            processed_data_set = new DataSet(processed_data_set, projector);
        }

        if (mapper != null) {
            processed_data_set = new DataSet(processed_data_set, mapper);
        }

        if (extender != null) {
            processed_data_set = new DataSet(processed_data_set, extender);
        }
    }

    public void outputRecords() throws IOException {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getPrintStream());
        processed_data_set.print(outputStreamWriter);
        outputStreamWriter.flush();
    }

    public DataSet getProcessedDataSet() throws IOException {

        return processed_data_set;
    }

    protected Selector getSelector() {
        return null;
    }

    protected Projector getProjector() {
        return null;
    }

    protected Mapper getMapper() {
        return null;
    }

    protected Extender getExtender() {
        return null;
    }

    protected abstract DataSet getSourceDataSet() throws IOException;

    public PrintStream getPrintStream() {

        return print_stream;
    }

    public void setPrintStream(PrintStream print_stream) {

        this.print_stream = print_stream;
    }
}
