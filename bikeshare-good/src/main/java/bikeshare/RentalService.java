package bikeshare;

import java.util.HashMap;
import java.util.Map;

public final class RentalService {
    private final Fleet fleet;
    private final RideStore rideStore;
    private final Map<String, Rental> rentals = new HashMap<>();

    public RentalService(int bikesPerStation, RideStore rideStore) {
        this.fleet = new Fleet(bikesPerStation);
        this.rideStore = rideStore;
    }

    public String rent(String memberId, MemberType memberType, Station station) {
        if (rentals.containsKey(memberId)) {
            throw new IllegalStateException("Member " + memberId + " already has a bike");
        }
        Bike bike = fleet.availableAt(station)
                .orElseThrow(() -> new IllegalStateException("No bike available at " + station));
        bike.rentOut();
        rentals.put(memberId, new Rental(bike, memberType));
        return bike.id();
    }

    public Ride returnBike(String memberId, Station station, int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Minutes must be more than 0");
        }
        Rental rental = rentals.get(memberId);
        if (rental == null) {
            throw new IllegalStateException("Member " + memberId + " has no bike");
        }
        MemberType memberType = rental.memberType();
        Ride ride = new Ride(memberId, memberType, rental.bike().id(), minutes, memberType.feeFor(minutes));
        rideStore.save(ride);
        rentals.remove(memberId);
        rental.bike().returnTo(station);
        return ride;
    }

    private record Rental(Bike bike, MemberType memberType) {
    }
}
