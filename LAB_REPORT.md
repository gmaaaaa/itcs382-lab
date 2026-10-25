# Lab Report, Code Quality and Design Quality

Repository URL: https://github.com/gmaaaaa/itcs382-lab

## Tools

| Item | Version or name |
| --- | --- |
| JDK | Temurin 25.0.4.1+1 |
| Maven | 3.10.0 |
| IDE | Visual Studio Code 1.143.1 |
| SonarQube for IDE | 6.2.0 |
| AI tool and exact model | Claude Code, Claude Opus 5.5 (claude-opus-5-5) |

## Task 1. SonarQube on the starter code

Commit of the starter, before any change: 826296b

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | java:S2143 | ParcelService.java:1 | Use the "java.time" API for date and time. |
| 2 | java:S1128 | ParcelService.java:6 | Remove this unused import 'java.util.Date'. |
| 3 | java:S1068 | ParcelService.java:12 | Remove this unused "maxParcels" private field. |
| 4 | java:S106 | ParcelService.java:30 | Replace this use of System.out by a logger. |
| 5 | java:S4973 | ParcelService.java:42 | Strings and Boxed types should be compared using "equals()". |
| 6 | java:S106 | ParcelService.java:67 | Replace this use of System.out by a logger. |
| 7 | java:S106 | ParcelService.java:69 | Replace this use of System.out by a logger. |
| 8 | java:S2095 | ParcelService.java:76 | Use try-with-resources or close this "FileWriter" in a "finally" clause. |
| 9 | java:S108 | ParcelService.java:80 | Remove this block of code, fill it in, or add a comment explaining why it is empty. |
| 10 | java:S106 | Main.java:9 | Replace this use of System.out by a logger. |
| 11 | java:S106 | Main.java:15 | Replace this use of System.out by a logger. |
| 12 | java:S106 | Main.java:17 | Replace this use of System.out by a logger. |
| 13 | java:S106 | Main.java:22 | Replace this use of System.out by a logger. |

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 2026-10-25 |
| AI tool and exact model | Claude Code, Claude Opus 5.5 (claude-opus-5-5) |
| Commit reviewed | 826296b |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | opened the folder |
| Output file in the reviews folder | reviews/parcelpoint-review.md |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | ParcelService.java:42 | pickUp compares the parcel code with == instead of equals(), so a pickup never matches | both (AI No. 1) | java:S4973 | partly, pickup P1 5 printed 0.0 and the parcel stayed STORED. But saying it never matches is too strong. |
| 2 | ParcelService.java:76 | FileWriter is never closed, so nothing is written | both (AI No. 2) | java:S2095 | partly, the CSV file was empty after the run. But an unclosed writer does not always leave an empty file. |
| 3 | ParcelService.java:80 | Empty catch block hides a failed save | both (AI No. 3) | java:S108 | yes, the catch block is empty, so it hides the error. |
| 4 | ParcelService.java:6 | Unused import java.util.Date | both (AI No. 7) | java:S1128 | yes, Date is imported but not used. |
| 5 | ParcelService.java:12 | Unused field maxParcels, so the limit is never enforced | both (AI No. 8) | java:S1068 | yes, the code never reads maxParcels. |
| 6 | ParcelService.java:1 | Use the java.time API for date and time | SonarQube | java:S2143 | partly, Date is not used. We only need to remove the import. |
| 7 | ParcelService.java:30, 67, 69; Main.java:9, 15, 17, 22 | System.out is used instead of a logger | SonarQube | java:S106 | partly, System.out is used, but the menu and reports need to print text. |
| 8 | Main.java:11-17 | No input check, so a missing argument or a bad number crashes the program | AI (No. 4) | probable bug | yes, store P1 crashed because some inputs were missing. |
| 9 | ParcelService.java:49 | pickUp returns 0 for an unknown code, the same value as a free pickup | AI (No. 5) | probable bug | yes, an unknown code also prints Fee: 0.0. |
| 10 | Main.java:8 | The Scanner is never closed | AI (No. 6) | resource leak (the Java extension also warns, SonarQube does not) | yes, the Scanner is not closed, but the program ends after the loop. |
| 11 | Parcel.java:13; ParcelService.java:28, 44, 68 | Status values are repeated string literals | AI (No. 9) | code smell | yes, STORED is used twice. The AI also wrote No. 28 instead of No. 27. |
| 12 | ParcelService.java:53, 57, 59, 61 | Magic numbers for the free days and the rates | AI (No. 10) | code smell | yes, 3 is repeated and the fee numbers have no names. |
| 13 | Main.java:6-27 | main is too complex | AI (No. 11) | code smell | partly, main has many branches, but the AI does not clearly explain why this is a problem. |
| 14 | Main.java:11 | split(" ") makes empty tokens when there are two spaces | AI (No. 12) | probable bug | yes, two spaces made the program read 6501 as the size. |
| 15 | ParcelService.java:24-37 | storeParcel finds a locker, sets the status, sends an SMS and saves the file | AI (No. 13) | procedural cohesion | partly, it does several things, but they all help store a parcel. |
| 16 | ParcelService.java:39-50 | pickUp frees the locker, sets the status, saves the file and computes the fee | AI (No. 14) | procedural cohesion | partly, it does several things, but they all help finish a pickup. |
| 17 | ParcelService.java:64-72 | printParcels(boolean) prints two different reports chosen by a flag | AI (No. 15) | logical cohesion | yes, the flag chooses which report to print. |
| 18 | Main.java:6-27 | main selects unrelated actions by the command word | AI (No. 16) | logical cohesion | yes, the command chooses the action. This is normal for a menu. |
| 19 | ParcelService.java:9 | ParcelService does lockers, fees, SMS, console output and CSV saving | AI (No. 17) | weak class cohesion | yes, one class handles lockers, fees, messages, reports and files. |
| 20 | ParcelService.java:52-62 | fee uses no attribute of ParcelService | AI (No. 18) | weak class cohesion | yes, fee uses size and days, not the service fields. |
| 21 | Parcel.java:3-27 | Parcel has a status but no methods to change it | AI (No. 19) | weak class cohesion | partly, another class changes status. This shows weak encapsulation, but does not prove weak cohesion. |
| 22 | ParcelService.java:28, 44, 68 | ParcelService reads and writes Parcel.status directly | AI (No. 20) | content coupling | yes, ParcelService changes status directly. |
| 23 | Main.java:19, 21 | Main passes the flag forStaff to printParcels | AI (No. 21) | control coupling | yes, the flag changes what the method does. |
| 24 | ParcelService.java:46 | fee receives the whole Parcel but uses only its size | AI (No. 22) | stamp coupling | partly, fee only needs the size, but both methods are in the same class. |
| 25 | Main.java:13-15 | Main gets back a whole Locker only to read its number | AI (No. 23) | stamp coupling | partly, Main only needs the locker number, but storeParcel needs the Parcel object. |
| 26 | Main.java:22 | Main reaches through getLockers() to count free lockers | AI (No. 24) | strong coupling (reach-through) | yes, Main gets the lockers and counts the free ones itself. |
| 27 | ParcelService.java:41-42 | locker.getParcel().getCode() reaches through Locker to Parcel | AI (No. 25) | strong coupling (reach-through) | yes, it gets the parcel from the locker, then gets its code. The code uses two separate statements. |
| 28 | Locker.java:5; Parcel.java:6; ParcelService.java:15-17, 26, 56, 58 | Size is an int code 1, 2, 3 that several classes must read the same way | AI (No. 26) | connascence of convention | yes, the classes must use 1, 2 and 3 with the same meaning. |
| 29 | Parcel.java:13; ParcelService.java:28, 44, 68 | Two classes must agree on the exact status strings | AI (No. 27) | connascence of convention | yes, both classes need the same status words. A typo still compiles. |
| 30 | Main.java:13; ParcelService.java:15-17 | Parcel(String, String, int) and Locker(int, int) take arguments of the same type | AI (No. 28) | connascence of position | yes, swapping code and studentId still compiles. |
| 31 | ParcelService.java:36, 49 | null means no free locker and 0 means not found | AI (No. 29) | connascence of convention | partly, Main checks null, but it treats 0 as a fee instead of saying the parcel was not found. |
| 32 | Parcel.java:7 | The field status is public | AI (No. 30) | field that is not private | yes, status is public. |
| 33 | ParcelService.java:20-22 | getLockers() returns the internal list | AI (No. 31) | internal collection returned | yes, other code can clear the list from getLockers(). |
| 34 | Locker.java:25-27 | setParcel lets any caller skip the free and size check | AI (No. 32) | weak encapsulation | yes, setParcel does not check if the locker is free or big enough. |

2.1 Name two problems that only the AI review found.

The AI found two problems. First, ParcelService changes Parcel.status directly. This is content coupling. Second, the flag in printParcels chooses which report to print. This is control coupling.

2.2 Name one problem that only SonarQube found, or write none.

Only SonarQube reported java:S106. It found System.out and suggested using a logger, for example at Main.java:9.

2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.

Finding No. 22 is partly right. The fee method gets a whole Parcel but only needs its size. But fee and pickUp are in the same class. So this is not a problem between two classes like the prompt asked.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | Fixed | 283b5d4 |
| 2 | Fixed | 283b5d4 |
| 3 | Fixed | de3db53 |
| 4 | Kept | The SMS message is part of the program's output, and moving it to Main would be a bigger change. |
| 5 | Fixed | 3651fe9 |
| 6 | Kept | The staff report is part of the program's output, and moving it to Main would be a bigger change. |
| 7 | Kept | The list report is part of the program's output, and moving it to Main would be a bigger change. |
| 8 | Fixed | 007ed15 |
| 9 | Fixed | a27c6dc |
| 10 | Kept | Printing in Main is the output of a command-line program. |
| 11 | Kept | Printing in Main is the output of a command-line program. |
| 12 | Kept | Printing in Main is the output of a command-line program. |
| 13 | Kept | Printing in Main is the output of a command-line program. |

Number of SonarQube issues that remain after your fixes: 7

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| Connascence of convention (Table 2 row 28) | Replaced the int size codes 1, 2 and 3 with a Size enum in Locker, Parcel, ParcelService and Main. | 30b41a0 |
| Content coupling (Table 2 row 22) | Made Parcel.status private, used a ParcelStatus enum, and added markStored() and markPickedUp() so that only Parcel changes its status. | b8c82da |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text
Commands: store <code> <studentId> <size 1-3>, pickup <code> <days>, list, staff, quit
SMS to 6501: parcel P1 is in locker 2
Locker 2
Fee: 30.0
P1 6501 PICKED_UP
Free lockers: 3

parcels.csv:
P1,PICKED_UP
```

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | 2026-10-25 | 2026-10-25 |
| AI tool and exact model | Claude Code, Claude Opus 5.5 (claude-opus-5-5) | Claude Code, Claude Opus 5.5 (claude-opus-5-5) |
| Prompt (B or C as given, or your changed prompt in full) | B as given | C as given |
| Builds with mvn package (yes or no) | yes | yes |
| Number of classes, interfaces, enums and records | 1 (1 class) | 11 (6 classes, 1 interface, 2 enums, 2 records; Rental is a private record inside RentalService) |
| SonarQube issues, total | 16 | 1 |
| SonarQube issues, by rule | java:S106: 10, java:S2386: 4, java:S3776: 1, java:S1141: 1 | java:S106: 1 |
| AI review findings in Check 1 | 7 | 3 |
| AI review findings in Check 2 | 9 | 9 |
| Review output file in the reviews folder | reviews/bikeshare-poor-review.md | reviews/bikeshare-good-review.md |

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

No, SonarQube did not report cohesion or coupling directly. It reported java:S2386, 'Make this member "protected".', for the public static arrays at BikeShare.java:11–14. It also reported java:S3776, "Refactor this method to reduce its Cognitive Complexity from 20 to the 15 allowed.", for handle(boolean rent). These rules point out public fields and complex code, but do not name the design problems.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

Yes. Both versions have 9 findings in Check 2, but the findings are different. For bikeshare-poor, the AI reported weak class cohesion because BikeShare holds the menu, the bike records, the fee rule and the file saving in one class (BikeShare.java:8-15). The good version's review did not report this problem.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

I would choose bikeshare-good. I can add VISITOR(4) to MemberType and add visitor to the menu help text. In bikeshare-poor, I need to change the number codes, input check and fee calculation in several places. If I forget to change the fee calculation, visitors will pay the staff rate.

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

SonarQube finds bugs and code smells. In parcelpoint, it found strings compared with ==, a FileWriter that was not closed and an empty catch block. In this lab, it did not report content coupling from Parcel.status or control coupling from the forStaff flag. In bikeshare-poor, it reported the public static arrays but did not call this a coupling problem.

5.2 When should you not trust the AI review? Give an example from this lab.

I should not trust an AI finding without checking the code. For example, finding No. 22 called fee stamp coupling, but fee and pickUp are in the same class. This does not match the prompt, which asked about coupling between classes. I should check the code and the course definitions before accepting a finding.

5.3 What will you do differently in Project Phase 2 after this lab?

I will use enums for categories like member types and keep fields private. I will use separate methods instead of boolean flags that choose between different jobs. I will use both SonarQube and an AI review to help find problems. I will check the findings myself before changing the code or adding them to the report.

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Claude Code, Claude Opus 5.5 (claude-opus-5-5), 2026-10-25 | Task 2: Prompt A review, and a first draft of Table 2 | reviews/parcelpoint-review.md | I went through every row of Table 2, changed the "Correct?" judgments where I disagreed, and decided the final answers. The bugs were tested by running the program. |
| Claude Code, Claude Opus 5.5 (claude-opus-5-5), 2026-10-25 | Task 3: code fixes | parcelpoint/src/main/java/parcelpoint | Each fix was built and run. I checked the SonarQube panel. |
| Claude Code, Claude Opus 5.5 (claude-opus-5-5), 2026-10-25 | Task 4: both BikeShare versions, their Prompt A reviews, and a first draft of answers 4.1–4.3 | bikeshare-poor, bikeshare-good, reviews | Both versions were built and run. I checked the SonarQube counts. |
| Claude Code, Claude Opus 5.5 (claude-opus-5-5), 2026-10-25 | Task 5: a first draft of answers 5.1–5.3 | LAB_REPORT.md Task 5 | Compared with my results in Tasks 1 to 4. |
| ChatGPT, Astra 6, 2026-10-25 | Explained the instructions and suggested clearer wording | Wording of answers in Tasks 2, 4 and 5 | I chose which suggestions to use. |
