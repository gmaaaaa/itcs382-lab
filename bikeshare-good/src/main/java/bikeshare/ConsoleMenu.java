package bikeshare;

import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Scanner;

final class ConsoleMenu {
    private static final String HELP = "Commands: rent <memberId> <student|staff> <station>, "
            + "return <memberId> <station> <minutes>, quit. Stations: Library, Canteen, Dorm";

    private final RentalService service;
    private final Scanner in;
    private final PrintStream out;

    ConsoleMenu(RentalService service, Scanner in, PrintStream out) {
        this.service = service;
        this.in = in;
        this.out = out;
    }

    void run() {
        out.println(HELP);
        while (in.hasNextLine()) {
            String[] words = in.nextLine().trim().split("\\s+");
            if (words[0].equalsIgnoreCase("quit")) {
                return;
            }
            try {
                out.println(handle(words));
            } catch (IllegalArgumentException | IllegalStateException | UncheckedIOException e) {
                out.println("Error: " + e.getMessage());
            }
        }
    }

    private String handle(String[] words) {
        return switch (words[0].toLowerCase(Locale.ROOT)) {
            case "rent" -> rent(words);
            case "return" -> returnBike(words);
            default -> HELP;
        };
    }

    private String rent(String[] words) {
        requireWords(words, 4);
        String bikeId = service.rent(words[1], MemberType.fromText(words[2]), Station.fromText(words[3]));
        return "Member " + words[1] + " rented bike " + bikeId;
    }

    private String returnBike(String[] words) {
        requireWords(words, 4);
        Ride ride = service.returnBike(words[1], Station.fromText(words[2]), parseMinutes(words[3]));
        return "Receipt: bike " + ride.bikeId() + ", " + ride.minutes() + " minutes, fee " + ride.fee() + " baht";
    }

    private static void requireWords(String[] words, int count) {
        if (words.length != count) {
            throw new IllegalArgumentException(HELP);
        }
    }

    private static int parseMinutes(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Minutes must be a whole number: " + text, e);
        }
    }
}
