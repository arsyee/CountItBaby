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
    private Dim[][] mGrid = null;
    private Dim mGridMargin;

    // Number of possible images and ID of the currently selected one
    private int mPossibleImageCount;
    private int mCurrentImageID;

    Canvas(Dim canvasDim, int possibleImageCount) {
        mCanvasDim = canvasDim;
        mIconDim = new Dim(Settings.IMAGE_SIZE, Settings.IMAGE_SIZE);
        mPossibleImageCount = possibleImageCount;
    }

    void generateQuestion(int solution) throws GridTooSmallException {
        if (Settings.DEBUG_MODE) {
            mCurrentImageID = 0;
        } else {
            mCurrentImageID = RandomHelper.intBetween(1, mPossibleImageCount - 1);
        }
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
        int x = mCanvasDim.X() / mIconDim.X() - 1;
        int y = mCanvasDim.Y() / mIconDim.Y() - 1;
        mGrid = new Dim[x][y];
        int gridSizeX = x * mIconDim.X();
        int gridSizeY = y * mIconDim.Y();
        mGridMargin = new Dim((mCanvasDim.X() - gridSizeX) / 2, (mCanvasDim.Y() - gridSizeY) / 2);
        Log.d(TAG, String.format("clearGrid created new grid: %d,%d; margin: %s", x, y, mGridMargin));
    }

    private void generateGrid(int solution) throws GridTooSmallException {
        if (mGrid == null || mGrid.length < 1 || mGrid.length * mGrid[0].length < solution) {
            throw new GridTooSmallException(mGrid == null || mGrid.length < 1 ? 0 : mGrid.length * mGrid[0].length);
        }

        while (solution-- > 0) {
            Dim inPos = new Dim(RandomHelper.intBetween(mIconDim.X() / 2 * -1,
                                                        mIconDim.X() / 2),
                                RandomHelper.intBetween(mIconDim.Y() / 2 * -1,
                                                        mIconDim.Y() / 2));
            if (Settings.NO_ADJUSTMENT) inPos = new Dim(0, 0);
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
        int collosionsFound = -1;
        int iteration = 0;
        while (collosionsFound != 0) {
            iteration++;
            Log.d(TAG, String.format("collosionsFound loop entered: %d (%d)", iteration, collosionsFound));
            if (iteration > 100) break;
            collosionsFound = 0;
            for (int i = 0; i < mGrid.length; ++i) {
                for (int j = 0; j < mGrid[i].length; ++j) {
                    Dim current = mGrid[i][j];
                    if (current == null) continue;
                    for (int iOffset = -1; iOffset <= 1; ++iOffset) {
                        for (int jOffset = -1; jOffset <= 1; ++jOffset) {
                            if (resolveCollosionIfNeeded(i, j, i + iOffset, j + jOffset)) collosionsFound++;
                        }
                    }
                }
            }
        }
    }

    private boolean resolveCollosionIfNeeded(int i, int j, int otherI, int otherJ) {
        boolean changed = false;
        Dim current  = mGrid[i][j];
        if (i == otherI && j == otherJ) return false;
        if (otherI < 0 || otherJ < 0) return false;
        if (otherI >= mGrid.length || otherJ >= mGrid[i].length) return false;
        Dim other = mGrid[otherI][otherJ];
        if (other == null) //noinspection ConstantConditions
            return changed;
        if (otherI < i && other.X() > current.X() || otherI > i && other.X() < current.X()) {
            current.setX((current.X() + other.X()) / 2);
            other.setX(current.X());
            changed = true;
        }
        if (otherJ < j && other.Y() > current.Y() || otherJ > j && other.Y() < current.Y()) {
            current.setY((current.Y() + other.Y()) / 2);
            other.setY(current.Y());
            changed = true;
        }
        return changed;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    private static boolean gridPosOccupied(Dim[][] grid, int x, int y) {
        if (x < 0 || y < 0) return true;
        if (x >= grid.length || y >= grid[x].length ) return true;
        return grid[x][y] != null;
    }

    private List<Dim> retrieveCoordinates() {
        ArrayList<Dim> result = new ArrayList<>();
        for (int i = 0; i < mGrid.length; ++i) {
            for (int j = 0; j < mGrid[i].length; ++j) {
                if (mGrid[i][j] == null) continue;
                Dim item = new Dim(i * mIconDim.X() + mGrid[i][j].X() + mGridMargin.X(),
                                   j * mIconDim.Y() + mGrid[i][j].Y() + mGridMargin.Y());
                result.add(item);
                item.tag = String.format("%d,%d -> %s", i, j, mGrid[i][j]);
            }
        }
        return result;
    }

}
