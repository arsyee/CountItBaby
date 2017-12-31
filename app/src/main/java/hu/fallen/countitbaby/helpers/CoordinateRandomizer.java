package hu.fallen.countitbaby.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hu.fallen.countitbaby.model.Settings;

public class CoordinateRandomizer {

    // TODO [Postponed] Scale up/down images based on count - maybe introduce scaled-up drawables to avoid programatical scaling?
    // TODO Improved algorithm to avoid overlaps

    private static final String TAG = CoordinateRandomizer.class.getCanonicalName();

    private static final Random RANDOM = new Random();

    public static List<Dim> getCoordinates(int solution, Dim canvasDimensions, Dim iconDimensions) {
        int boundX = canvasDimensions.getX() - Settings.instance().getImageSize();
        int boundY = canvasDimensions.getY() - Settings.instance().getImageSize();
        int smallestCounter = 100;
        ArrayList<Dim> result = new ArrayList<>(solution);
        for (int i = 0; i < solution; ++i) {
            int iconX;
            int iconY;
            int counter = 100;
            outer: do {
                iconX = RANDOM.nextInt(boundX);
                iconY = RANDOM.nextInt(boundY);
                for (int j = 0; j < i; ++j) {
                    if (Math.abs(iconX-result.get(j).getX()) < Settings.instance().getImageSize() &&
                        Math.abs(iconY-result.get(j).getY()) < Settings.instance().getImageSize()) {
                        // Log.d(TAG, "Collides with " + j);
                        continue outer;
                    }
                }
                break;
            } while (--counter > 0);
            Log.d(TAG, "Random coordinates for " + i + " to " + iconX + "," + iconY + " (" + counter + ")");
            if (counter < smallestCounter) smallestCounter = counter;
            result.add(new Dim(iconX, iconY));
        }
        Log.d(TAG, "Smallest counter: " + smallestCounter);
        return result;
    }

    public static int getSolution() {
        int max = Settings.instance().max();
        int min = Settings.instance().min();
        int solution = RANDOM.nextInt(max - min + 1) + min;
        return solution;
    }

    public static int getRandom(int bound) {
        return RANDOM.nextInt(bound);
    }
}
