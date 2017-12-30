package hu.fallen.countitbaby.model;

public class Settings {
    // TODO [Settings] Create SettingsActivity
    // TODO [Settings]   -> upper and lower bounds for number of drawings
    // TODO [Settings]   -> randomize button order
    // TODO [Settings]   -> show only limited number of buttons

    private static final Settings INSTANCE = new Settings();

    private Settings() {}

    public static Settings instance() {
        return INSTANCE;
    }

    public int min() {
        return 1;
    }

    public int max() {
        return 5;
    }
}
