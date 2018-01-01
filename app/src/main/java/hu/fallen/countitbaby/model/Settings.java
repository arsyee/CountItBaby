package hu.fallen.countitbaby.model;

import android.util.Log;

public class Settings {

    private static final String TAG = Settings.class.getCanonicalName();
    // TODO [Settings] Create SettingsActivity

    // Settings will be hardcoded for now. I will implement SettingsActivity
    // when the settings are more or less final.

    public static final int MAXIMUM = 20;
    public static final int IMAGE_SIZE = 50;

    // Configuration settings (pre-conditions are enforced by reconfigure(), which is the only method writing these parameters)
    private int lowerBound; // lower bound (inclusive) for generated solution
    private int upperBound; // upper bound (inclusive) for generated solution
    private int showButtons; // number of buttons shown
    private boolean mButtonOrderRandomized;

    public static final boolean NO_ADJUSTMENT = false;
    public static final boolean DEBUG_MODE = false;

    private static final Settings INSTANCE = new Settings();

    private Settings() {
        reconfigure(10, 20, 5, false);
    }

    public static Settings instance() {
        return INSTANCE;
    }

    public int min() {
        return lowerBound;
    }

    public int max() {
        return upperBound;
    }

    public int getNumButtons() {
        return showButtons;
    }

    public boolean isButtonOrderRandomized() {
        return mButtonOrderRandomized;
    }

    public void reconfigure(int lowerBound, int upperBound, int showButtons, boolean randomizeButtons) {
        Log.d(TAG, "reconfiguring Settings");
        if (lowerBound < 0) {
            this.lowerBound = 0;
        } else if (lowerBound > MAXIMUM) {
            this.lowerBound = MAXIMUM;
        } else {
            this.lowerBound = lowerBound;
        }
        if (upperBound < this.lowerBound) {
            this.upperBound = this.lowerBound;
        } else if (upperBound > MAXIMUM) {
            this.upperBound = MAXIMUM;
        } else {
            this.upperBound = upperBound;
        }
        if (showButtons < 2) {
            if (this.lowerBound == this.upperBound) {
                this.showButtons = 1;
            } else {
                this.showButtons = 2;
            }
        } else if (showButtons > (this.upperBound - this.lowerBound + 1)) {
            this.showButtons = this.upperBound - this.lowerBound + 1;
        } else {
            this.showButtons = showButtons;
        }
        this.mButtonOrderRandomized = randomizeButtons;
    }
}
