package bikeshare;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class CsvRideStore implements RideStore {
    private final Path file;

    public CsvRideStore(Path file) {
        this.file = file;
    }

    @Override
    public void save(Ride ride) {
        String line = String.join(",", ride.memberId(), ride.memberType().name(), ride.bikeId(),
                String.valueOf(ride.minutes()), String.valueOf(ride.fee())) + System.lineSeparator();
        try {
            Files.writeString(file, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save the ride to " + file, e);
        }
    }
}
