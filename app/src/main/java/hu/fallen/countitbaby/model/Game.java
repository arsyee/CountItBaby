package hu.fallen.countitbaby.model;

import android.util.Log;

import hu.fallen.countitbaby.helpers.Dim;

public class Game {
    private static final String TAG = Game.class.getCanonicalName();
    private Canvas mCanvas;
    private Controls mControls;

    public Game(Dim canvasDim, int length) {
        Log.d(TAG, "Game(" + canvasDim + ", " + length + ")");
        mCanvas = new Canvas(canvasDim, length);
        mControls = new Controls();
        generateQuestion();
    }

    public Canvas getCanvas() {
        return mCanvas;
    }

    public boolean checkSolution(int number) {
        return number == mCanvas.getSolution();
    }

    public void generateQuestion() {
        mCanvas.generateQuestion();
        mControls.calculateButtons(mCanvas.getSolution());
    }

    public boolean isButtonVisible(int i) {
        return mControls.isButtonVisible(i);
    }

    public Dim getCoordinate(int i) {
        return mCanvas.getCoordinate(i);
    }

    public Dim getIconsDim() {
        return mCanvas.getIconsDim();
    }

    public int getImageId() {
        return getCanvas().getImageId();
    }
}
