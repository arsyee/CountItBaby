package hu.fallen.countitbaby.model;

import android.util.Log;

import java.util.List;

import hu.fallen.countitbaby.helpers.RandomHelper;
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
        mSolution = RandomHelper.generateSolution();
        mControls.calculateButtons(mSolution);
        try {
            mCanvas.generateQuestion(mSolution);
        } catch (GridTooSmallException e) {
            // TODO Canvas reported back that the grid is too small, Settings reconfiguration needed
        }
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
