1. Files read: parcelpoint/src/main/java/parcelpoint/Locker.java, Main.java, Parcel.java, ParcelService.java

2. Findings (all files are in parcelpoint/src/main/java/parcelpoint/)

| No. | Check | Code location (file:line) | Finding | Severity | Suggested fix |
|---|---|---|---|---|---|
| 1 | 1 Code quality | ParcelService.java:42 | `parcel.getCode() == code` compares Strings with ==. `words[1]` from `split` in Main.java:17 is a new String, so a pickup never matches and always returns 0. | Must fix | Use `parcel.getCode().equals(code)`. |
| 2 | 1 Code quality | ParcelService.java:76 | `FileWriter` is never closed or flushed, so it leaks a file handle and parcels.csv probably stays empty. | Must fix | Open the writer in try-with-resources. |
| 3 | 1 Code quality | ParcelService.java:80-81 | The `catch (IOException e) {}` block is empty, so a failed save is silently ignored. | Should fix | Log the exception or rethrow it. |
| 4 | 1 Code quality | Main.java:11-17 | There is no input check. `words[1..3]` can throw ArrayIndexOutOfBoundsException and `Integer.parseInt` can throw NumberFormatException, and either one crashes the program. | Should fix | Check `words.length` and catch NumberFormatException before using the arguments. |
| 5 | 1 Code quality | ParcelService.java:49 | `pickUp` returns 0 when no parcel is found, which is the same value as a free pickup, so Main.java:17 prints "Fee: 0.0" for an unknown code. | Should fix | Return a distinct result, or throw, when the code is not found. |
| 6 | 1 Code quality | Main.java:8 | The `Scanner` is never closed. | Note | Wrap the Scanner in try-with-resources. |
| 7 | 1 Code quality | ParcelService.java:6 | Unused import `java.util.Date`. | Should fix | Remove the import. |
| 8 | 1 Code quality | ParcelService.java:12 | The field `maxParcels` is never read, so the limit is never enforced. | Should fix | Enforce the limit in `storeParcel` or delete the field. |
| 9 | 1 Code quality | Parcel.java:13; ParcelService.java:28, 44, 68 | Status values are magic string literals ("NEW", "STORED", "PICKED_UP"), and "STORED" is repeated. | Note | Replace them with named constants or an enum (see No. 28). |
| 10 | 1 Code quality | ParcelService.java:53, 57, 59, 61 | Magic numbers: the free-day limit `3` is repeated 4 times, and the rates 10.0/15.0/20.0 are inline. | Note | Pull them out into named constants. |
| 11 | 1 Code quality | Main.java:6-27 | `main` is the most complex method: a 5-branch if/else chain plus a ternary, mixing parsing, dispatch and output. No other method is too long (not found). | Note | Move each command branch into its own private method. |
| 12 | 1 Code quality | Main.java:11 | `split(" ")` produces empty tokens when words are separated by more than one space, which shifts the arguments. | Note | Use `split("\\s+")`. |
| 13 | 2 Method cohesion | ParcelService.java:24-37 | `storeParcel` has procedural cohesion. It assigns a locker, changes the status, records the parcel, sends an SMS and saves to a file. These steps are related only by the order they run in. | Should fix | Move notification and persistence into separate collaborators that `storeParcel` calls. |
| 14 | 2 Method cohesion | ParcelService.java:39-50 | `pickUp` has procedural cohesion. It frees the locker, changes the status, saves to a file and computes the fee in one method. | Should fix | Keep the fee calculation and persistence out of the pickup step. |
| 15 | 2 Method cohesion | ParcelService.java:64-72 | `printParcels(boolean)` has logical cohesion: a flag chooses between two different reports. | Should fix | Split it into `printStaffReport()` and `printWaitingParcels()`. |
| 16 | 2 Method cohesion | Main.java:6-27 | `main` has logical cohesion: the command word selects unrelated actions inside one method. | Note | Send each command to its own handler method. |
| 17 | 2 Class cohesion | ParcelService.java:9 (see 24, 30, 52, 64, 74) | The class represents several things: locker allocation, the fee policy, SMS notification, console reporting and CSV persistence. | Should fix | Move fees, notification and persistence into their own classes. |
| 18 | 2 Class cohesion | ParcelService.java:52-62 | `fee` uses no attribute of ParcelService and works only on `parcel.getSize()`. | Should fix | Move `fee` to Parcel or to a separate FeePolicy class. |
| 19 | 2 Class cohesion | Parcel.java:3-27 | Parcel owns `status` but has no behaviour. All of its status changes happen in ParcelService (28, 44). | Note | Add methods such as `markStored()` and `markPickedUp()` to Parcel. |
| 20 | 2 Coupling | ParcelService.java:28, 44, 68 -> Parcel.java:7 | Content coupling: ParcelService reads and writes Parcel's internal `status` field directly. | Must fix | Make `status` private and change it through Parcel methods. |
| 21 | 2 Coupling | Main.java:19, 21 -> ParcelService.java:64 | Control coupling: Main passes the flag `forStaff`, which controls the logic inside `printParcels`. | Should fix | Call two separate methods instead of passing a flag. |
| 22 | 2 Coupling | ParcelService.java:46 -> ParcelService.java:52 | Stamp coupling: `fee` receives the whole Parcel but uses only its size. | Note | Pass only the size, or make `fee` a method of Parcel. |
| 23 | 2 Coupling | Main.java:13-15 -> ParcelService.java:24 | Stamp coupling in both directions: Main builds and passes a whole Parcel, and gets back a whole Locker only to read `getNumber()`. | Note | Return the locker number, or an Optional of it, instead of the Locker. |
| 24 | 2 Coupling | Main.java:22 | Reach-through: Main goes through `service.getLockers()` to call `Locker::isFree` and counts free lockers itself, which is domain logic. | Should fix | Add `ParcelService.countFreeLockers()`. |
| 25 | 2 Coupling | ParcelService.java:41-42 | Reach-through: `locker.getParcel().getCode()` goes through Locker to reach Parcel. | Note | Add `Locker.holds(String code)`. |
| 26 | 2 Connascence | Locker.java:5; Parcel.java:6; ParcelService.java:15-17, 26, 56, 58; Main.java:9, 13 | Connascence of convention: size is an int where 1 = small, 2 = medium and 3 = large, and four classes must share that meaning. | Should fix | Use a `Size` enum so that only connascence of type remains. |
| 27 | 2 Connascence | Parcel.java:13; ParcelService.java:28, 44, 68 | Connascence of convention: the two classes must agree on the exact status strings, and a typo compiles silently. | Should fix | Use a `ParcelStatus` enum. |
| 28 | 2 Connascence | Main.java:13 -> Parcel.java:9; ParcelService.java:15-17 -> Locker.java:8 | Connascence of position: `Parcel(String, String, int)` and `Locker(int, int)` take arguments of the same type, so swapping code and studentId, or number and size, still compiles. | Note | Use the `Size` enum for size and a factory or builder with named steps. |
| 29 | 2 Connascence | ParcelService.java:36, 49 -> Main.java:15, 17 | Connascence of convention: `null` means "no free locker" and `0` means "not found", and Main must know these special values. | Note | Return Optional, or throw, instead of sentinel values. |
| 30 | 2 Encapsulation | Parcel.java:7 | The field `status` is `public`. It is the only field that is not private. | Must fix | Make it private and add a getter. |
| 31 | 2 Encapsulation | ParcelService.java:20-22 | `getLockers()` returns the internal `lockers` list, so callers can add, remove or clear lockers. | Should fix | Return `List.copyOf(lockers)` or `Collections.unmodifiableList(lockers)`. |
| 32 | 2 Encapsulation | Locker.java:25-27 | The public `setParcel` lets any caller put any parcel into a locker without the free or size check in ParcelService.java:26. | Note | Move the free and size check into a `Locker.store(Parcel)` method. |

Not found:
- Common coupling between classes.
- A class that depends on many other classes. Main uses only ParcelService, Parcel and Locker.
- Connascence of algorithm between classes.
- Connascence of name that could be made weaker.
- Methods with cohesion weaker than logical (coincidental).
- Problems in the Locker methods, the Parcel methods, the ParcelService constructor, `getLockers` and `saveToFile`. Their cohesion is functional or sequential.

3. Number of findings per check
- Check 1 (Code quality): 12 findings (No. 1-12)
- Check 2 (Cohesion, coupling and connascence): 20 findings (No. 13-32). These break down as 4 on method cohesion, 3 on class cohesion, 6 on coupling, 4 on connascence and 3 on encapsulation.
