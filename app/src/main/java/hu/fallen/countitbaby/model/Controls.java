package hu.fallen.countitbaby.model;

import android.util.Log;

import hu.fallen.countitbaby.helpers.CoordinateRandomizer;

/**
 * Class represents the Controls, i.e. the Buttons to select the Solution.
 * Properties:
 *   - number of visible buttons
 */

class Controls {

    private static final String TAG = Controls.class.getCanonicalName();
    private boolean[] mButtonVisible = null;

    Controls() {
        clear();
    }

    private void clear() {
        int size = Settings.instance().max();
        if (mButtonVisible == null || mButtonVisible.length != size) {
            mButtonVisible = new boolean[size];
        }
        for (int i = 0; i < size; ++i) {
            mButtonVisible[i] = false;
        }
    }

    void calculateButtons(int mSolution) {
        clear();
        int size = mButtonVisible.length;
        int required = Settings.instance().showButtons;
        mButtonVisible[mSolution-1] = true;
        while (--required > 0) {
            int showButton = CoordinateRandomizer.getRandom(Settings.instance().max()) % size;
            while (mButtonVisible[showButton]) showButton = (showButton + 1) % size;
            mButtonVisible[showButton] = true;
        }
        Log.d(TAG,"Show buttons: " + buttonsToString());
    }

    private String buttonsToString() {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        int last = -1;
        for (int i = 0; i < mButtonVisible.length; ++i) {
            if (mButtonVisible[i]) {
                if (last != -1) {
                    builder.append(last).append(", ");
                }
                last = i;
            }
        }
        builder.append(last);
        builder.append(")");
        return builder.toString();
    }

    boolean isButtonVisible(int i) {
        if (i >= mButtonVisible.length) return false;
        return mButtonVisible[i];
    }
}
