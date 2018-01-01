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
    Dim mGridMargin;

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
        clearGrid();
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

    private void clearGrid() {
        int x = mCanvasDim.X() / mIconDim.X() - 1 - 4;
        int y = mCanvasDim.Y() / mIconDim.Y() - 1 - 4;
        mGrid = new Dim[x][y];
        int gridSizeX = x * mIconDim.X();
        int gridSizeY = y * mIconDim.Y();
        mGridMargin = new Dim((mCanvasDim.X() - gridSizeX) / 2, (mCanvasDim.Y() - gridSizeY) / 2);
        Log.d(TAG, "clearGrid created new grid: " + x + "," + y + "; margin: " + mGridMargin);
    }

    private void generateGrid(int solution) throws GridTooSmallException {
        if (mGrid == null || mGrid.length < 1 || mGrid.length * mGrid[0].length < solution) {
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
            // move new item to an adjacent cell
            gridPos.setX(gridPos.X()+adjPos.X());
            gridPos.setY(gridPos.Y()+adjPos.Y());
            // if it was moved, set inner position towards the original position
            if (adjPos.X() < 0) inPos.setX(mIconDim.X() / 2);
            if (adjPos.X() > 0) inPos.setX(mIconDim.X() / 2 * -1);
            if (adjPos.Y() < 0) inPos.setY(mIconDim.Y() / 2);
            if (adjPos.Y() > 0) inPos.setY(mIconDim.Y() / 2 * -1);
            mGrid[gridPos.X()][gridPos.Y()] = inPos;
        }
        // adjust innerPosition:s not to overlap
        int findCollosion = -1;
        int iteration = 0;
        while (findCollosion != 0) {
            iteration++;
            Log.d(TAG,"FINDCOLLOSION LOOP ENTERED: " + iteration + " (" + findCollosion + ")");
            if (iteration > 100) break;
            findCollosion = 0;
            for (int i = 0; i < mGrid.length; ++i) {
                for (int j = 0; j < mGrid[i].length; ++j) {
                    if (mGrid[i][j] == null) continue;
                    if (j > 0) {
                        if (mGrid[i][j - 1] != null && mGrid[i][j - 1].Y() > mGrid[i][j].Y()) {
                            Log.d(TAG, "VCollosion detected: (" + i + "," + j + ") - (" + i + "," + (j - 1) + ") -> " + mGrid[i][j] + " - " + mGrid[i][j - 1]);
                            mGrid[i][j].setY((mGrid[i][j].Y() + mGrid[i][j - 1].Y()) / 2);
                            mGrid[i][j - 1].setY(mGrid[i][j].Y());
                            Log.d(TAG, "VCollosion resolved: " + mGrid[i][j] + " - " + mGrid[i][j - 1]);
                            findCollosion++;
                        }
                    }
                    if (i > 0) {
                        if (mGrid[i - 1][j] != null && mGrid[i - 1][j].X() > mGrid[i][j].X()) {
                            Log.d(TAG, "HCollosion detected: (" + i + "," + j + ") - (" + (i - 1) + "," + j + ") -> " + mGrid[i][j] + " - " + mGrid[i - 1][j]);
                            mGrid[i][j].setX((mGrid[i][j].X() + mGrid[i - 1][j].X()) / 2);
                            mGrid[i - 1][j].setX(mGrid[i][j].X());
                            Log.d(TAG, "HCollosion resolved: " + mGrid[i][j] + " - " + mGrid[i - 1][j]);
                            findCollosion++;
                        }
                    }
                    if (i > 0 && j > 0) {
                        if (mGrid[i - 1][j - 1] != null && mGrid[i - 1][j - 1].X() > mGrid[i][j].X() && mGrid[i - 1][j - 1].Y() > mGrid[i][j].Y()) {
                            Log.d(TAG, "DCollosion detected: (" + i + "," + j + ") - (" + (i - 1) + "," + (j - 1) + ") -> " + mGrid[i][j] + " - " + mGrid[i - 1][j - 1]);
                            mGrid[i][j].setX((mGrid[i][j].X() + mGrid[i - 1][j - 1].X()) / 2);
                            mGrid[i][j].setY((mGrid[i][j].Y() + mGrid[i - 1][j - 1].Y()) / 2);
                            mGrid[i - 1][j - 1].setX(mGrid[i][j].X());
                            Log.d(TAG, "DCollosion resolved: " + mGrid[i][j] + " - " + mGrid[i - 1][j]);
                            findCollosion++;
                        }
                    }
                    if (i > 0 && j < mGrid[i].length - 1) {
                        if (mGrid[i - 1][j + 1] != null && mGrid[i - 1][j + 1].X() > mGrid[i][j].X() && mGrid[i - 1][j + 1].Y() > mGrid[i][j].Y()) {
                            Log.d(TAG, "dCollosion detected: (" + i + "," + j + ") - (" + (i - 1) + "," + (j + 1) + ") -> " + mGrid[i][j] + " - " + mGrid[i - 1][j + 1]);
                            mGrid[i][j].setX((mGrid[i][j].X() + mGrid[i - 1][j + 1].X()) / 2);
                            mGrid[i][j].setY((mGrid[i][j].Y() + mGrid[i - 1][j + 1].Y()) / 2);
                            mGrid[i - 1][j + 1].setX(mGrid[i][j].X());
                            Log.d(TAG, "dCollosion resolved: " + mGrid[i][j] + " - " + mGrid[i - 1][j + 1]);
                            findCollosion++;
                        }
                    }
                }
            }
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
                Dim item = new Dim(i * mIconDim.X() + mGrid[i][j].X() + mGridMargin.X(),
                                   j * mIconDim.Y() + mGrid[i][j].Y() + mGridMargin.Y());
                result.add(item);
                item.tag = new StringBuilder().append(i).append(",").append(j).append(" -> ")
                        .append(mGrid[i][j]).toString();
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
