package hu.fallen.countitbaby.model;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class Model_Control_UnitTest {
    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void test_5_buttons_between_1_10_contains_solution() throws Exception {
        int min = 1;
        int max = 10;
        int numButtons = 5;
        boolean randomized = false;
        Settings.instance().reconfigure(min, max, numButtons, randomized);
        Controls testObject = new Controls();
        for (int i = 0; i < 100; ++i) { // random test, repeat it 100 times!
            for (int solution = min; solution <= max; ++solution) { // assume all possible solutions
                boolean solutionFound = false;
                testObject.calculateButtons(solution);
                assertTrue(testObject.getVisibleButtonIndexes().size() == numButtons);
                for (int visibleButton : testObject.getVisibleButtonIndexes()) {
                    if (visibleButton == solution) solutionFound = true;
                    assertTrue(visibleButton >= min);
                    assertTrue(visibleButton <= max);
                }
                assertTrue(solutionFound);
            }
        }
    }

    @Test
    public void test_5_buttons_between_10_20_contains_solution() throws Exception {
        int min = 10;
        int max = 20;
        int numButtons = 5;
        boolean randomized = false;
        Settings.instance().reconfigure(min, max, numButtons, randomized);
        Controls testObject = new Controls();
        for (int i = 0; i < 100; ++i) { // random test, repeat it 100 times!
            for (int solution = min; solution <= max; ++solution) { // assume all possible solutions
                boolean solutionFound = false;
                testObject.calculateButtons(solution);
                assertTrue(testObject.getVisibleButtonIndexes().size() == numButtons);
                for (int visibleButton : testObject.getVisibleButtonIndexes()) {
                    if (visibleButton == solution) solutionFound = true;
                    assertTrue(visibleButton >= min);
                    assertTrue(visibleButton <= max);
                }
                assertTrue(solutionFound);
            }
        }
    }

    @Test
    public void test_10_buttons_between_1_20_ordered() throws Exception {
        int min = 1;
        int max = 20;
        int numButtons = 10;
        boolean randomized = false;
        Settings.instance().reconfigure(min, max, numButtons, randomized);
        Controls testObject = new Controls();
        for (int i = 0; i < 100; ++i) { // random test, repeat it 100 times!
            for (int solution = min; solution <= max; ++solution) { // assume all possible solutions
                int previous = -1;
                testObject.calculateButtons(solution);
                for (int visibleButton : testObject.getVisibleButtonIndexes()) {
                    assertTrue(previous < visibleButton);
                    previous = visibleButton;
                }
            }
        }
    }

    @Test
    public void test_20_buttons_between_1_20_randomized() throws Exception {
        // 20! = 2.432902e+18
        // probability of ordered list is almost 0
        int min = 1;
        int max = 20;
        int numButtons = 20;
        boolean randomized = true;
        Settings.instance().reconfigure(min, max, numButtons, randomized);
        Controls testObject = new Controls();
        for (int i = 0; i < 100; ++i) { // random test, repeat it 100 times!
            for (int solution = min; solution <= max; ++solution) { // assume all possible solutions
                int previous = -1;
                boolean lowerFound = false;
                boolean higherFound = false;
                boolean sameFound = false;
                testObject.calculateButtons(solution);
                for (int visibleButton : testObject.getVisibleButtonIndexes()) {
                    if (previous != -1) {
                        if (previous < visibleButton) higherFound = true;
                        if (previous > visibleButton) lowerFound = true;
                        if (previous == visibleButton) sameFound = true;
                    }
                    previous = visibleButton;
                }
                assertTrue(lowerFound);
                assertTrue(higherFound);
                assertFalse(sameFound);
            }
        }
    }

}