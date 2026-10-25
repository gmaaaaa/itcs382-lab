package parcelpoint;

public enum Size {
    SMALL, MEDIUM, LARGE;

    public static Size fromNumber(int number) {
        if (number < 1 || number > values().length) {
            throw new IllegalArgumentException("Size must be 1-3");
        }
        return values()[number - 1];
    }

    public boolean fits(Size parcelSize) {
        return compareTo(parcelSize) >= 0;
    }
}
