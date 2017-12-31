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

class Canvas {
    private static final String TAG = Canvas.class.getCanonicalName();

    private Dim mCanvasDim;
    private Dim mIconDim;

    private int mSolution;
    private List<Dim> mCoordinates;

    private int mImageCount;
    private int mImageId;

    Canvas(Dim canvasDim, int imageCount) {
        mCanvasDim = canvasDim;
        mIconDim = new Dim(0, 0);
        mImageCount = imageCount;
    }

    void generateQuestion() {
        mSolution = CoordinateRandomizer.getSolution();
        mImageId = CoordinateRandomizer.getRandom(mImageCount);
        mCoordinates = CoordinateRandomizer.getCoordinates(mSolution, mCanvasDim, mIconDim);
    }

    int getSolution() {
        return mSolution;
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
}
