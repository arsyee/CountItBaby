package hu.fallen.countitbaby.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hu.fallen.countitbaby.model.Settings;

public class CoordinateRandomizer {

    // TODO Scale up/down images based on count
    // TODO Improved algorithm to avoid overlaps
    // TODO Increase maximum number of images to at least 20

    private static final String TAG = CoordinateRandomizer.class.toString();

    private static final Random RANDOM = new Random();

    public static List<Dim> getCoordinates(int newSolution, Dim canvasDimensions, Dim iconDimensions) {
        int smallestCounter = 100;
        ArrayList<Dim> result = new ArrayList<>(newSolution);
        for (int i = 0; i < newSolution; ++i) {
            int iconCenterX;
            int iconCenterY;
            int counter = 100;
            outer: do {
                iconCenterX = RANDOM.nextInt(canvasDimensions.getX() - iconDimensions.getX()) + iconDimensions.getX() / 2;
                iconCenterY = RANDOM.nextInt(canvasDimensions.getY() - iconDimensions.getY()) + iconDimensions.getY() / 2;
                // Log.d(TAG, "Random coordinates for " + i + " to " + iconCenterX + "," + iconCenterY + " (" + counter + ")");
                for (int j = 0; j < i; ++j) {
                    if (Math.abs(iconCenterX-result.get(j).getX()) < iconDimensions.getX() &&
                            Math.abs(iconCenterY-result.get(j).getY()) < iconDimensions.getY()) {
                        // Log.d(TAG,"Collides with " + j);
                        continue outer;
                    }
                }
                break;
            } while (--counter > 0);
            if (counter < smallestCounter) smallestCounter = counter;
            result.add(new Dim(iconCenterX, iconCenterY));
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
