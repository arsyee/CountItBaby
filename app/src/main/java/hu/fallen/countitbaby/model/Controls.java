package hu.fallen.countitbaby.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hu.fallen.countitbaby.helpers.CoordinateRandomizer;

/**
 * Class represents the Controls, i.e. the Buttons to select the Solution.
 * Properties:
 *   - number of visible buttons
 */

class Controls {

    private static final String TAG = Controls.class.getCanonicalName();
    private List<Integer> mVisibleButtons = null;

    Controls() {
        clear();
    }

    private void clear() {
        mVisibleButtons = new ArrayList<>();
    }

    void calculateButtons(int mSolution) {
        clear();
        int size = Settings.instance().max();
        int required = Settings.instance().getNumButtons();
        mVisibleButtons.add(mSolution-1);
        while (--required > 0) {
            int showButton = CoordinateRandomizer.getRandom(Settings.instance().max()) % size;
            while (mVisibleButtons.contains(showButton)) showButton = (showButton + 1) % size;
            int position = CoordinateRandomizer.getRandom(mVisibleButtons.size() + 1);
            Log.d(TAG, "Adding " + showButton + "@" + position);
            mVisibleButtons.add(position, showButton);
        }
    }

    List<Integer> getVisibleButtonIndexes() {
        return mVisibleButtons;
    }
}
