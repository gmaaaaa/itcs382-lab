package bikeshare;

import java.util.Locale;

public enum Station {
    LIBRARY("Library"), CANTEEN("Canteen"), DORM("Dorm");

    private final String label;

    Station(String label) {
        this.label = label;
    }

    public static Station fromText(String text) {
        try {
            return valueOf(text.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown station: " + text, e);
        }
    }

    @Override
    public String toString() {
        return label;
    }
}
