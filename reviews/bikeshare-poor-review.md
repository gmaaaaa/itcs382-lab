1. Files read: bikeshare-poor/src/main/java/bikeshare/BikeShare.java. It is the only Java file under that folder.

2. Findings. All paths are relative to `bikeshare-poor/src/main/java/bikeshare/`.

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
|---|---|---|---|---|---|
| 1 | 1 Code quality | BikeShare.java:13-14 | `bikeMember` and `bikeMemberType` are sized with the literal `6`. That size does not come from `bikeStation` (line 12). If someone adds a bike to line 12, lines 53-54 throw `ArrayIndexOutOfBoundsException`. | Should fix | Size both arrays with `bikeStation.length`. |
| 2 | 1 Code quality | BikeShare.java:71-72 | The member ID the user typed (line 43) is written into `rides.csv` without escaping. An ID that contains a comma or a quote breaks the CSV row. | Should fix | Reject member IDs that contain commas or quotes at line 46, or escape them before writing. |
| 3 | 1 Code quality | BikeShare.java:76-77 | When a bike is returned, `bikeMember[b]` is cleared but `bikeMemberType[b]` is not. A parked bike keeps the old member type. | Note | Reset `bikeMemberType[b]` together with `bikeMember[b]` at line 77. |
| 4 | 1 Code quality | BikeShare.java:69-76 | The receipt is printed (line 69) before the ride is saved. If saving fails (line 73), only a message is printed, the bike is still marked returned (line 76), and the ride record is lost. | Note | Save the ride before printing the receipt, and decide on purpose what should happen when the save fails. |
| 5 | 1 Code quality | BikeShare.java:45, 62 | The literal `"Station (Library, Canteen, Dorm)"` appears twice. It also repeats the station names in `stations` (line 11) by hand, so the two must be kept in sync. | Note | Build the prompt once from the `stations` array. |
| 6 | 1 Code quality | BikeShare.java:68 | The fee rates `2` and `3` baht per minute are magic numbers. | Should fix | Replace them with named constants such as `STUDENT_RATE` and `STAFF_RATE`. |
| 7 | 1 Code quality | BikeShare.java:40-82 | `handle` is about 43 lines long with a cyclomatic complexity of about 16. It has two unrelated branches, a loop, two catch blocks, a ternary, and compound conditions of 3 terms (line 46) and 5 terms (line 64). | Should fix | Split it into separate `rent()` and `returnBike()` methods. |
| 8 | 2 Cohesion | BikeShare.java:40 | The cohesion of `handle(boolean rent)` is **logical**. A flag picks between two unrelated activities, renting and returning. | Should fix | Replace it with two methods, `rent()` and `returnBike()`. |
| 9 | 2 Coupling | BikeShare.java:28, 30 → 40 | `main` passes `true` or `false` to steer what `handle` does. This is **control coupling**. | Should fix | Call `rent()` or `returnBike()` directly from `main`. |
| 10 | 2 Class cohesion | BikeShare.java:8-15 | `BikeShare` represents several things at once: the console UI (`main`, `ask`), the station list, the bike inventory, member records, the fee policy (line 68) and CSV storage (lines 70-75). Every method is static, so the class works as a procedural module, not as an object with its own attributes. | Should fix | Move the work into classes such as `Bike`, `Station`, `FeePolicy` and `RideLog`, and keep `BikeShare` for console input and output only. |
| 11 | 2 Coupling | BikeShare.java:11-15 (used at 21, 24, 51-55, 64, 68, 71, 76-77, 86, 90-91) | All methods read and write shared mutable globals (`stations`, `bikeStation`, `bikeMember`, `bikeMemberType`, `in`). Because the fields are `public static`, any class can change them too. This is **common coupling**. | Must fix | Make the state private instance fields, or pass it to the methods that need it. |
| 12 | 2 Connascence | BikeShare.java:12-14 (used at 52-54, 68, 71, 76-77) | `bikeStation`, `bikeMember` and `bikeMemberType` are parallel arrays. One bike's data is linked only by sharing the same index `b`. This is **connascence of position**. | Should fix | Create a `Bike` class with station, member and memberType fields, and keep a list of `Bike` objects. |
| 13 | 2 Connascence | BikeShare.java:52, 64, 95 (checked at 46, 64) | The value `-1` has two hidden meanings. In `bikeStation` it means "rented" (lines 52, 64). From `findStation` it means "not found" (line 95), and callers check it as `station < 0`. This is **connascence of convention (meaning)**. | Should fix | Use a named constant or an explicit rented flag on the bike, and return `Optional` or throw an exception from `findStation`. |
| 14 | 2 Connascence | BikeShare.java:9-10, 14, 44, 68 | Member types are int codes `1` and `2`. The prompt text "1 = student, 2 = staff" (line 44) repeats those values by hand, and line 68 treats every type that is not `STUDENT` as staff. This is **connascence of convention (meaning)**. | Should fix | Replace the int codes with a `MemberType` enum that holds its own rate. |
| 15 | 2 Encapsulation | BikeShare.java:11-15 | `stations`, `bikeStation`, `bikeMember`, `bikeMemberType` and `in` are public, mutable and not final. Any caller can change the internal arrays, or replace them entirely. | Must fix | Make these fields private, and expose read-only accessors only where they are needed. |
| 16 | 2 Encapsulation | BikeShare.java:40, 84, 89 | The internal helpers `handle`, `ask` and `findStation` are public. | Note | Make them private. |

Items I looked for and did not find:
- **Resources never closed:** not found. The Scanner is closed in `finally` at line 36, and the PrintWriter uses try-with-resources at line 70.
- **Objects compared with ==:** not found. Strings use `equals` (lines 25, 27, 29) and `equalsIgnoreCase` (line 91). The `==` at line 51 compares ints.
- **Unused imports or fields:** not found.
- **Empty blocks:** not found.
- **Unused parameters:** only `args` at line 17, which the `main` signature requires, so it is not reported.
- **Methods with procedural or weaker cohesion, other than `handle`:** not found. `main` is a sequential command loop, and `ask` and `findStation` are functional.
- **Coupling between project classes:** not found. There is only one class, so there are no stamp or content coupling interactions to judge. Coupling findings 9 and 11 are between methods of `BikeShare` and through its public globals.
- **Dependence on many classes, or reaching through one object to call another:** not found. The class uses only Scanner, PrintWriter, FileWriter, IOException and System.
- **Operations that return an internal collection:** not found. There are no getters, but finding 15 exposes the arrays directly instead.

3. Findings per check:
- Check 1 (code quality): 7 (Nos. 1-7)
- Check 2 (cohesion, coupling, connascence, encapsulation): 9 (Nos. 8-16)
