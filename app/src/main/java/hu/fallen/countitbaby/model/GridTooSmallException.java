package hu.fallen.countitbaby.model;

class GridTooSmallException extends Throwable {

    private final int mGridSize;

    GridTooSmallException(int i) {
        super("Grid size: " + i);
        mGridSize = i;
    }

    public int getGridSize() {
        return mGridSize;
    }
}
