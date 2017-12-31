package hu.fallen.countitbaby.model;

import java.util.ArrayList;
import java.util.Collections;
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
        mVisibleButtons = new ArrayList<>(Settings.MAXIMUM);
    }

    void calculateButtons(int mSolution) {
        clear();
        for (int i = Settings.instance().min(); i <= Settings.instance().max(); ++i) {
            mVisibleButtons.add(i);
        }
        if (Settings.instance().isButtonOrderRandomized()) Collections.shuffle(mVisibleButtons);
        while (mVisibleButtons.size() > Settings.instance().getNumButtons()) {
            int removeCandidate = CoordinateRandomizer.getRandom(mVisibleButtons.size());
            if (mSolution == mVisibleButtons.get(removeCandidate)) {
                mVisibleButtons.remove((removeCandidate + 1) % mVisibleButtons.size());
            } else {
                mVisibleButtons.remove(removeCandidate);
            }
        }
    }

    List<Integer> getVisibleButtonIndexes() {
        return mVisibleButtons;
    }
}
