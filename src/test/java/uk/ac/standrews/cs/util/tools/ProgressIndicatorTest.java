package uk.ac.standrews.cs.util.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProgressIndicatorTest {

    @Test
    public void indicatorShowsExpectedNumberOfSteps() {

        assertEquals(5, getNumberOfIndicatorSteps(5, 100, 100));
        assertEquals(10, getNumberOfIndicatorSteps(5, 50, 100));
        assertEquals(0, getNumberOfIndicatorSteps(0, 100, 100));
        assertEquals(0, getNumberOfIndicatorSteps(0, 0, 100));
        assertEquals(0, getNumberOfIndicatorSteps(5, 0, 100));
    }

    @Test
    public void indicatorShowsExpectedValue() {

        assertEquals(0.4, getLastProportionComplete(5, 100, 40), 0.00001);
    }

    private int getNumberOfIndicatorSteps(int number_of_updates, int total_configured_steps, int total_actual_steps) {

        TestProgressIndicator1 indicator = new TestProgressIndicator1(number_of_updates);

        indicator.setTotalSteps(total_configured_steps);

        for (int i = 0; i < total_actual_steps; i++) {
            indicator.progressStep();
        }

        return indicator.invocation_count;
    }

    private double getLastProportionComplete(int number_of_updates, int total_configured_steps, int total_actual_steps) {

        TestProgressIndicator2 indicator = new TestProgressIndicator2(number_of_updates);

        indicator.setTotalSteps(total_configured_steps);

        for (int i = 0; i < total_actual_steps; i++) {
            indicator.progressStep();
        }

        return indicator.last_value;
    }

    class TestProgressIndicator1 extends ProgressIndicator {

        int invocation_count = 0;

        TestProgressIndicator1(int number_of_updates) {
            super(number_of_updates);
        }

        @Override
        public void indicateProgress(double proportion_complete) {

            invocation_count++;
        }
    }

    class TestProgressIndicator2 extends ProgressIndicator {

        double last_value = 0;

        TestProgressIndicator2(int number_of_updates) {
            super(number_of_updates);
        }

        @Override
        public void indicateProgress(double proportion_complete) {

            last_value = proportion_complete;
        }
    }
}
