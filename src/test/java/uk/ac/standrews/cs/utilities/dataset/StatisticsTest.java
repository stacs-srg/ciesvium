package uk.ac.standrews.cs.utilities.dataset;

import org.junit.Test;
import uk.ac.standrews.cs.utilities.tables.Means;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class StatisticsTest {

    private static final double EPSILON = 0.0000001;

    @Test
    public void meanOfEmptyListIsNaN() {

        assertTrue(Double.isNaN(Means.calculateMean(new ArrayList<>())));
    }

    @Test
    public void meansAreCorrect() {

        assertEquals(1.0, Means.calculateMean(Arrays.asList(1.0)), EPSILON);
        assertEquals(1.5, Means.calculateMean(Arrays.asList(1.0, 2.0)), EPSILON);
        assertEquals(2.0, Means.calculateMean(Arrays.asList(1.0, 2.0, 3.0)), EPSILON);
        assertEquals(-4.9, Means.calculateMean(Arrays.asList(1.0, 2.0, 3.0, -25.6)), EPSILON);
    }
}
