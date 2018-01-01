package hu.fallen.countitbaby.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hu.fallen.countitbaby.helpers.RandomHelper;
import hu.fallen.countitbaby.helpers.Dim;

/**
 *  Class represents the current Canvas.
 *  Properties are:
 *    - width and height of the canvas in real pixels (changes if the container is resized)
 *    - width and height of the images in real pixels (may change every time)
 *    - number of images drawn on the canvas
 *    - coordinates of the images drawn on the canvas
 */

class Canvas {
    private static final String TAG = Canvas.class.getCanonicalName();

    private Dim mCanvasDim;
    private Dim mIconDim;

    private List<Dim> mCoordinates;

    private int mImageCount;
    private int mImageId;

    Canvas(Dim canvasDim, int imageCount) {
        mCanvasDim = canvasDim;
        mIconDim = new Dim(0, 0);
        mImageCount = imageCount;
    }

    void generateQuestion(int solution) {
        mImageId = RandomHelper.getRandom(mImageCount);
        mCoordinates = getCoordinates(solution, mCanvasDim, mIconDim);
    }

    Dim getCoordinate(int i) {
        if (i >= 0 && i < mCoordinates.size()) return mCoordinates.get(i);
        return null;
    }

    Dim getIconsDim() {
        return mIconDim;
    }

    int getImageId() {
        return mImageId;
    }

    public static List<Dim> getCoordinates(int solution, Dim canvasDimensions, Dim iconDimensions) {
        int boundX = canvasDimensions.getX() - Settings.IMAGE_SIZE;
        int boundY = canvasDimensions.getY() - Settings.IMAGE_SIZE;
        int smallestCounter = 100;
        ArrayList<Dim> result = new ArrayList<>(solution);
        for (int i = 0; i < solution; ++i) {
            int iconX;
            int iconY;
            int counter = 100;
            outer: do {
                iconX = RandomHelper.getRandom(boundX);
                iconY = RandomHelper.getRandom(boundY);
                for (int j = 0; j < i; ++j) {
                    if (Math.abs(iconX-result.get(j).getX()) < Settings.IMAGE_SIZE &&
                            Math.abs(iconY-result.get(j).getY()) < Settings.IMAGE_SIZE) {
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
}
