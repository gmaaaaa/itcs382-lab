package bikeshare;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class BikeShare {
    public static final int STUDENT = 1;
    public static final int STAFF = 2;
    public static String[] stations = {"Library", "Canteen", "Dorm"};
    public static int[] bikeStation = {0, 0, 1, 1, 2, 2};
    public static String[] bikeMember = new String[6];
    public static int[] bikeMemberType = new int[6];
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.print("Command (rent, return, quit): ");
                if (!in.hasNextLine()) {
                    return;
                }
                String command = in.nextLine().trim();
                if (command.equals("quit")) {
                    return;
                } else if (command.equals("rent")) {
                    handle(true);
                } else if (command.equals("return")) {
                    handle(false);
                } else {
                    System.out.println("Unknown command");
                }
            }
        } finally {
            in.close();
        }
    }

    public static void handle(boolean rent) {
        try {
            if (rent) {
                String member = ask("Member ID");
                int type = Integer.parseInt(ask("Member type (1 = student, 2 = staff)"));
                int station = findStation(ask("Station (Library, Canteen, Dorm)"));
                if (member.isEmpty() || (type != STUDENT && type != STAFF) || station < 0) {
                    System.out.println("Invalid member ID, member type or station");
                    return;
                }
                for (int b = 0; b < bikeStation.length; b++) {
                    if (bikeStation[b] == station) {
                        bikeStation[b] = -1;
                        bikeMember[b] = member;
                        bikeMemberType[b] = type;
                        System.out.println("Bike " + (b + 1) + " rented at " + stations[station]);
                        return;
                    }
                }
                System.out.println("No bike available at " + stations[station]);
            } else {
                int b = Integer.parseInt(ask("Bike number")) - 1;
                int station = findStation(ask("Station (Library, Canteen, Dorm)"));
                int minutes = Integer.parseInt(ask("Ride time in minutes"));
                if (b < 0 || b >= bikeStation.length || bikeStation[b] != -1 || station < 0 || minutes < 0) {
                    System.out.println("Invalid bike, station or minutes");
                    return;
                }
                int fee = minutes * (bikeMemberType[b] == STUDENT ? 2 : 3);
                System.out.println("Receipt: bike " + (b + 1) + ", " + minutes + " minutes, " + fee + " baht");
                try (PrintWriter out = new PrintWriter(new FileWriter("rides.csv", true))) {
                    out.println(bikeMember[b] + "," + bikeMemberType[b] + "," + (b + 1) + ","
                            + stations[station] + "," + minutes + "," + fee);
                } catch (IOException e) {
                    System.out.println("Could not save the ride: " + e.getMessage());
                }
                bikeStation[b] = station;
                bikeMember[b] = null;
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a whole number");
        }
    }

    public static String ask(String label) {
        System.out.print(label + ": ");
        return in.hasNextLine() ? in.nextLine().trim() : "";
    }

    public static int findStation(String name) {
        for (int s = 0; s < stations.length; s++) {
            if (stations[s].equalsIgnoreCase(name)) {
                return s;
            }
        }
        return -1;
    }
}
