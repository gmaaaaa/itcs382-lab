package bikeshare;

import java.util.Locale;

public enum MemberType {
    STUDENT(2), STAFF(3);

    private final int bahtPerMinute;

    MemberType(int bahtPerMinute) {
        this.bahtPerMinute = bahtPerMinute;
    }

    public int feeFor(int minutes) {
        return bahtPerMinute * minutes;
    }

    public static MemberType fromText(String text) {
        try {
            return valueOf(text.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown member type: " + text, e);
        }
    }
}
