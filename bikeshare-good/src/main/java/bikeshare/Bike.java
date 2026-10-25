package bikeshare;

final class Bike {
    private final String id;
    private Station station;
    private boolean rented;

    Bike(String id, Station station) {
        this.id = id;
        this.station = station;
    }

    String id() {
        return id;
    }

    boolean isAvailableAt(Station place) {
        return !rented && station == place;
    }

    void rentOut() {
        rented = true;
    }

    void returnTo(Station place) {
        station = place;
        rented = false;
    }
}
