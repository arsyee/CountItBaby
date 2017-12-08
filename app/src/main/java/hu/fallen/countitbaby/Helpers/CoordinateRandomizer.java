package hu.fallen.countitbaby.Helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoordinateRandomizer {

    private static final String TAG = CoordinateRandomizer.class.toString();

    public static class Dim {
        int x;
        int y;

        public Dim(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private Random mRandom;
    private Dim mCanvasDims;
    private Dim mIconDims;

    public CoordinateRandomizer(Random random, Dim canvasDimensions, Dim iconDimensions) {
        mRandom = random;
        mCanvasDims = canvasDimensions;
        mIconDims = iconDimensions;
    }

    public List<Dim> getCoordinates(int newSolution) {
        int smallestCounter = 100;
        ArrayList<Dim> result = new ArrayList<>(newSolution);
        for (int i = 0; i < newSolution; ++i) {
            int iconCenterX;
            int iconCenterY;
            int counter = 100;
            outer: do {
                iconCenterX = mRandom.nextInt(mCanvasDims.getX() - mIconDims.getX()) + mIconDims.getX() / 2;
                iconCenterY = mRandom.nextInt(mCanvasDims.getY() - mIconDims.getY()) + mIconDims.getY() / 2;
                Log.d(TAG, "Random coordinates for " + i + " to " + iconCenterX + "," + iconCenterY + " (" + counter + ")");
                for (int j = 0; j < i; ++j) {
                    if (Math.abs(iconCenterX-result.get(j).getX()) < mIconDims.getX() &&
                            Math.abs(iconCenterY-result.get(j).getY()) < mIconDims.getY()) {
                        Log.d(TAG,"Collides with " + j);
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
}
