package hu.fallen.countitbaby.model;

public class Settings {
    // TODO [Settings] Create SettingsActivity
    // TODO [Settings]   -> upper and lower bounds for number of drawings
    // TODO [Settings]   -> randomize button order
    // TODO [Settings]   -> show only limited number of buttons

    // Settings will be hardcoded for now. I will implement SettingsActivity
    // when the settings are more or less final.

    public static final int MAXIMUM = 20;

    private int lowerBound;
    private int upperBound;
    int showButtons;
    private boolean randomizeButtons;

    private static final Settings INSTANCE = new Settings();

    private Settings() {
        reconfigure(1, 10, 6, true);
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

    public void reconfigure(int lowerBound, int upperBound, int showButtons, boolean randomizeButtons) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.showButtons = showButtons;
        this.randomizeButtons = randomizeButtons;
    }
}
