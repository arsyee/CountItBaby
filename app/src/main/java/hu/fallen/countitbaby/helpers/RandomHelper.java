package hu.fallen.countitbaby.helpers;

import java.util.Random;

import hu.fallen.countitbaby.model.Settings;

public class RandomHelper {

    // TODO [Postponed] Scale up/down images based on count - maybe introduce scaled-up drawables to avoid programatical scaling?

    private static final String TAG = RandomHelper.class.getCanonicalName();

    private static final Random RANDOM = new Random();

    public static int intBetween(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static int generateSolution() {
        return intBetween(Settings.instance().min(), Settings.instance().max());
    }

    public static int getRandom(int bound) {
        return RANDOM.nextInt(bound);
    }
}
