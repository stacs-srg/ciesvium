package uk.ac.standrews.cs.util.csv;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

public abstract class CSVProcessor {

    protected CSV processed_csv;

    public CSVProcessor() throws IOException {

        Selector selector = getSelector();
        Projector projector = getProjector();

        processed_csv = getSourceCSV();

        if (selector != null) {
            processed_csv = new CSV(processed_csv, selector);
        }

        if (projector != null) {
            processed_csv = new CSV(processed_csv, projector);
        }
    }

    public void outputRecords() throws IOException {

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getPrintStream());
        processed_csv.print(outputStreamWriter);
        outputStreamWriter.flush();

        getPrintStream().println("number of lines selected: " + processed_csv.getRecords().size());
    }

    public CSV getProcessedCSV() throws IOException {

        return processed_csv;
    }

    protected Selector getSelector() {
        return null;
    }

    protected Projector getProjector() {
        return null;
    }

    protected abstract CSV getSourceCSV() throws IOException;

    protected PrintStream getPrintStream() {
        return System.out;
    }
}
