package hu.fallen.countitbaby.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hu.fallen.countitbaby.helpers.RandomHelper;
import hu.fallen.countitbaby.helpers.Dim;
import hu.fallen.countitbaby.helpers.SpiralGrid;

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

    // real DIP sizes
    private Dim mCanvasDim;
    private Dim mIconDim;

    // list of coordinates to be passed for presentation
    private List<Dim> mCoordinates;

    // internal representation of the Coordinates, used for presentation
    Dim[][] mGrid = null;

    // Number of possible images and ID of the currently selected one
    private int mPossibleImageCount;
    private int mCurrentImageID;

    Canvas(Dim canvasDim, int possibleImageCount) {
        mCanvasDim = canvasDim;
        mIconDim = new Dim(Settings.IMAGE_SIZE, Settings.IMAGE_SIZE);
        mPossibleImageCount = possibleImageCount;
    }

    void generateQuestion(int solution) throws GridTooSmallException {
        mCurrentImageID = RandomHelper.getRandom(mPossibleImageCount);
        generateGrid(solution);
        mCoordinates = retrieveCoordinates();
    }


    Dim getCoordinate(int i) {
        if (i >= 0 && i < mCoordinates.size()) return mCoordinates.get(i);
        return null;
    }

    int getImageId() {
        return mCurrentImageID;
    }

    public void generateGrid(int solution) throws GridTooSmallException {
        mGrid = new Dim[mCanvasDim.X() / mIconDim.X()][mCanvasDim.Y() / mIconDim.Y()];

        if (mGrid.length < 1 || mGrid.length * mGrid[0].length < solution) {
            throw new GridTooSmallException(mGrid.length < 1 ? 0 : mGrid.length * mGrid[0].length);
        }

        while (solution-- > 0) {
            Dim inPos = new Dim(RandomHelper.intBetween(mIconDim.X() / 2 * -1,
                                                        mIconDim.X() / 2),
                                RandomHelper.intBetween(mIconDim.Y() / 2 * -1,
                                                        mIconDim.Y() / 2));
            Dim gridPos = new Dim(RandomHelper.getRandom(mGrid.length),
                                  RandomHelper.getRandom(mGrid[0].length));
            Dim adjPos = new Dim(0, 0);
            int iteration = 0;
            while (gridPosOccupied(mGrid, gridPos.X() + adjPos.X(), gridPos.Y() + adjPos.Y())) {
                adjPos = SpiralGrid.getGridAdjustment(++iteration, inPos);
            }
            gridPos.setX(gridPos.X()+adjPos.X());
            gridPos.setY(gridPos.Y()+adjPos.Y());
            // adjust innerPosition not to overlap
            mGrid[gridPos.X()][gridPos.Y()] = inPos;
        }
    }

    private static boolean gridPosOccupied(Dim[][] grid, int x, int y) {
        if (x < 0 || y < 0) return true;
        if (x >= grid.length || y >= grid[x].length ) return true;
        return grid[x][y] != null;
    }

    private List<Dim> retrieveCoordinates() {
        ArrayList<Dim> result = new ArrayList<Dim>();
        for (int i = 0; i < mGrid.length; ++i) {
            for (int j = 0; j < mGrid[i].length; ++j) {
                if (mGrid[i][j] == null) continue;
                Dim item = new Dim(i * mIconDim.X() + mGrid[i][j].X(), j * mIconDim.Y() + mGrid[i][j].Y());
                result.add(item);
            }
        }
        return result;
    }

    public List<Dim> getCoordinates_old(int solution, Dim canvasDimensions) {
        int boundX = canvasDimensions.X() - mIconDim.X();
        int boundY = canvasDimensions.Y() - mIconDim.Y();
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
                    if (Math.abs(iconX-result.get(j).X()) < mIconDim.X() &&
                            Math.abs(iconY-result.get(j).Y()) < mIconDim.Y()) {
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
