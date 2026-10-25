1. Files read: Bike.java, ConsoleMenu.java, CsvRideStore.java, Fleet.java, Main.java, MemberType.java, RentalService.java, Ride.java, RideStore.java, Station.java (all in bikeshare-good/src/main/java/bikeshare/).

2. Findings

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
|---|---|---|---|---|---|
| 1 | 1 Code quality (probable bug) | CsvRideStore.java:18-19 | `memberId` comes straight from user input (ConsoleMenu.java:47, split only on whitespace), so an ID such as `a,b` adds an extra comma and shifts every column in rides.csv. | Should fix | Reject commas in memberId when it is read, or quote/escape each field before `String.join`. |
| 2 | 1 Code quality (probable bug) | MemberType.java:15 (input from ConsoleMenu.java:65) | `bahtPerMinute * minutes` can overflow `int`. For example, 1,000,000,000 minutes for STAFF gives a negative fee, and `parseMinutes` accepts that value. | Should fix | Use `Math.multiplyExact`, or set an upper limit on minutes in `RentalService.returnBike`. |
| 3 | 1 Code quality (smell: duplicated code) | MemberType.java:18-24 and Station.java:14-20 | `fromText` is written twice with the same logic (upper-case, `valueOf`, wrap the exception). | Note | Move it into one shared generic helper, such as `parseEnum(Class<E>, String, String label)`. |
| 4 | 2 Coupling (stamp) | ConsoleMenu.java:53-54 / RentalService.java:27 | `returnBike` returns the whole `Ride` record, but ConsoleMenu reads only `bikeId`, `minutes` and `fee` (3 of its 5 fields). | Note | Accept this as a small receipt value, or return a receipt type that holds only those three fields. |
| 5 | 2 Coupling (stamp) | RentalService.java:37 / CsvRideStore.java:17-19 | `RideStore.save(Ride)` takes a composite record. CsvRideStore uses all of its fields, so this is stamp coupling with no unused data. | Note | No change needed. Keep `Ride` as the unit of persistence. |
| 6 | 2 Coupling (reach-through / Law of Demeter) | RentalService.java:36, RentalService.java:39 | `rental.bike().id()` and `rental.bike().returnTo(station)` go through the `Rental` record to call `Bike`. Rental is private and nested, so the effect stays inside the class. | Note | Give `Rental` methods `bikeId()` and `returnTo(Station)` that delegate to the bike. |
| 7 | 2 Coupling (hidden dependency / tramp data) | RentalService.java:11-12, Main.java:13 | RentalService builds its own concrete `Fleet` from an `int bikesPerStation` that it only passes on. Main has to know how Fleet is built, and Fleet cannot be swapped. | Note | Pass a `Fleet` into the RentalService constructor instead of `bikesPerStation`. |
| 8 | 2 Connascence (convention/meaning) | ConsoleMenu.java:9-10 vs Station.java:6 and MemberType.java:6 | The HELP text hard-codes "Library, Canteen, Dorm" and "student\|staff". If a station or member type is added or renamed, the help text silently goes wrong. | Should fix | Build the station and member-type lists in HELP from `Station.values()` and `MemberType.values()`. |
| 9 | 2 Connascence (position) | RentalService.java:36 vs Ride.java:3 | `new Ride(...)` takes 5 positional arguments, including two `String`s (memberId, bikeId) and two `int`s (minutes, fee). Swapping either pair still compiles. | Note | Build the Ride with a static factory, such as `Ride.of(memberId, rental, minutes)`, so the paired types cannot be swapped silently. |
| 10 | 2 Connascence (position/convention) | CsvRideStore.java:18-19 | The CSV column order (memberId, type, bikeId, minutes, fee) is an unwritten convention, and the file has no header row. Any reader has to know the order. | Note | Write a header line when the file is created. |
| 11 | 2 Connascence (type/convention) | ConsoleMenu.java:31-32 vs CsvRideStore.java:23, RentalService.java:18/21/29/33, Station.java:18, MemberType.java:22 | ConsoleMenu has to know exactly which exception types its dependencies throw, including the store's `UncheckedIOException`, which reaches it through RentalService. It also shows the domain's `getMessage()` text directly to the user. | Note | Have RentalService throw one domain exception type, such as `RentalException`, and have ConsoleMenu catch only that. |
| 12 | 2 Connascence (algorithm/value) | RentalService.java:22-23 and 38-39 vs Bike.java:6, 21-28 | Whether a bike is rented is stored twice: in the `rentals` map and in `Bike.rented`. RentalService must keep both in step by hand. | Note | Make one of the two the only source of truth. For example, let Fleet or Bike answer "rented" from the rental record. |

Items checked and not found:
- Unclosed resources: not found. The Scanner is in try-with-resources at Main.java:14, and `Files.writeString` closes its own stream.
- Objects compared with == instead of equals(): not found. Bike.java:18 compares `Station` enums with ==, which is correct.
- Unused imports, fields or parameters: not found.
- Empty blocks: not found. The empty private constructor at Main.java:9-10 is the usual idiom for a class that should not be instantiated.
- Many repeated string literals: not found.
- Long or complex methods: not found.
- Methods with procedural or weaker cohesion: not found. Every method is functional or sequential.
- Classes that represent more than one thing: not found.
- Control, common or content coupling: not found.
- Classes that depend on many classes: not found. RentalService depends on 6 types and ConsoleMenu on 4.
- Non-private fields: not found.
- Operations that return an internal mutable collection: not found. `Fleet.bikes` and `RentalService.rentals` are never exposed.

3. Number of findings per check
- Check 1 (code quality): 3
- Check 2 (cohesion, coupling, connascence): 9. These are 0 method cohesion, 0 class cohesion, 4 coupling (Nos. 4-7), 5 connascence (Nos. 8-12), 0 visibility and 0 exposed collections.
