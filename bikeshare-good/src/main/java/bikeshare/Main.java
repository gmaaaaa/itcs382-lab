package bikeshare;

import java.nio.file.Path;
import java.util.Scanner;

public final class Main {
    private static final int BIKES_PER_STATION = 2;

    private Main() {
    }

    public static void main(String[] args) {
        RentalService service = new RentalService(BIKES_PER_STATION, new CsvRideStore(Path.of("rides.csv")));
        try (Scanner in = new Scanner(System.in)) {
            new ConsoleMenu(service, in, System.out).run();
        }
    }
}
