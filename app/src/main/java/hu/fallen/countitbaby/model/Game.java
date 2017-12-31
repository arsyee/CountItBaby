package hu.fallen.countitbaby.model;

import android.util.Log;

import java.util.List;

import hu.fallen.countitbaby.helpers.CoordinateRandomizer;
import hu.fallen.countitbaby.helpers.Dim;

public class Game {
    private static final String TAG = Game.class.getCanonicalName();

    private int mSolution;
    private Canvas mCanvas;
    private Controls mControls;

    public Game(Dim canvasDim, int length) {
        Log.d(TAG, "Game(" + canvasDim + ", " + length + ")");
        mCanvas = new Canvas(canvasDim, length);
        mControls = new Controls();
        generateQuestion();
    }

    public boolean checkSolution(int number) {
        return number == mSolution;
    }

    public void generateQuestion() {
        mSolution = CoordinateRandomizer.generateSolution();
        mControls.calculateButtons(mSolution);
        mCanvas.generateQuestion(mSolution);
    }

    public List<Integer> getVisibleButtonIndexes() {
        return mControls.getVisibleButtonIndexes();
    }

    public Dim getCoordinate(int i) {
        return mCanvas.getCoordinate(i);
    }

    public int getImageId() {
        return mCanvas.getImageId();
    }
}
