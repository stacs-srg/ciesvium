package uk.ac.standrews.cs.util.dataset;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

public abstract class DataSetProcessor {

    protected DataSet processed_data_set;

    public DataSetProcessor() throws IOException {

        Selector selector = getSelector();
        Projector projector = getProjector();

        processed_data_set = getSourceDataSet();

        if (selector != null) {
            processed_data_set = new DataSet(processed_data_set, selector);
        }

        if (projector != null) {
            processed_data_set = new DataSet(processed_data_set, projector);
        }
    }

    public void outputRecords() throws IOException {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getPrintStream());
        processed_data_set.print(outputStreamWriter);
        outputStreamWriter.flush();

        getPrintStream().println("number of lines selected: " + processed_data_set.getRecords().size());
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

    protected abstract DataSet getSourceDataSet() throws IOException;

    protected PrintStream getPrintStream() {
        return System.out;
    }
}
