package hu.fallen.countitbaby.model;

import android.util.Log;

import java.util.List;

import hu.fallen.countitbaby.helpers.CoordinateRandomizer;
import hu.fallen.countitbaby.helpers.Dim;

/**
 *  Class represents the current Canvas.
 *  Properties are:
 *    - width and height of the canvas in real pixels (changes if the container is resized)
 *    - width and height of the images in real pixels (may change every time)
 *    - number of images drawn on the canvas
 *    - coordinates of the images drawn on the canvas
 */

public class Canvas {
    private static final String TAG = Canvas.class.getCanonicalName();

    // Dimensions
    private Dim mCanvasDim;
    private Dim mIconsDim;

    private int mSolution;
    private List<Dim> mCoordinates;

    private int mImageCount;
    private int mImageId;

    public Canvas(Dim canvasDim, Dim iconsDim, int imageCount) {
        Log.d(TAG, "Canvas dimensions: " + canvasDim);
        Log.d(TAG, "Icon dimensions: " + iconsDim);
        mCanvasDim = canvasDim;
        mIconsDim = iconsDim;
        mImageCount = imageCount;
        generateQuestion();
    }

    public void generateQuestion() {
        mSolution = CoordinateRandomizer.getSolution();
        mImageId = CoordinateRandomizer.getRandom(mImageCount);
        mCoordinates = CoordinateRandomizer.getCoordinates(mSolution, mCanvasDim, mIconsDim);
    }

    public Dim getCoordinate(int i) {
        if (i >= 0 && i < mCoordinates.size()) return mCoordinates.get(i);
        return null;
    }

    public boolean checkSolution(int number) {
        return number == mSolution;
    }

    public Dim getIconsDim() {
        return mIconsDim;
    }

    public int getImageId() {
        return mImageId;
    }
}
