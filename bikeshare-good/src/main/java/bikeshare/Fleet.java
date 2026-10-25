package bikeshare;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class Fleet {
    private final List<Bike> bikes = new ArrayList<>();

    Fleet(int bikesPerStation) {
        for (Station station : Station.values()) {
            for (int i = 0; i < bikesPerStation; i++) {
                bikes.add(new Bike("B" + (bikes.size() + 1), station));
            }
        }
    }

    Optional<Bike> availableAt(Station station) {
        return bikes.stream().filter(bike -> bike.isAvailableAt(station)).findFirst();
    }
}
