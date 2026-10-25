# Lab Report, Code Quality and Design Quality

Repository URL:

## Tools

| Item | Version or name |
| --- | --- |
| JDK | |
| Maven | |
| IDE | |
| SonarQube for IDE | |
| AI tool and exact model | |

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
| 1 | ParcelService.java:42 | pickUp compares the parcel code with == instead of equals(), so a pickup never matches | both (AI No. 1) | java:S4973 | yes, in step C2 `pickup P1 5` printed Fee: 0.0 instead of 30.0 and P1 stayed STORED |
| 2 | ParcelService.java:76 | FileWriter is never closed, so nothing is written | both (AI No. 2) | java:S2095 | yes, parcels.csv was empty (0 bytes) after the C2 run |
| 3 | ParcelService.java:80 | Empty catch block hides a failed save | both (AI No. 3) | java:S108 | yes, lines 80-81 catch IOException and do nothing |
| 4 | ParcelService.java:6 | Unused import java.util.Date | both (AI No. 7) | java:S1128 | yes, Date is not used anywhere in the file |
| 5 | ParcelService.java:12 | Unused field maxParcels, so the limit is never enforced | both (AI No. 8) | java:S1068 | yes, nothing reads maxParcels |
| 6 | ParcelService.java:1 | Use the java.time API for date and time | SonarQube | java:S2143 | yes, it comes from the unused Date import, so removing the import also fixes it |
| 7 | ParcelService.java:30, 67, 69; Main.java:9, 15, 17, 22 | System.out is used instead of a logger | SonarQube | java:S106 | partly, printing is the output of a command-line program, but the SMS message at ParcelService.java:30 is printed from inside the service |
| 8 | Main.java:11-17 | No input check, so a missing argument or a bad number crashes the program | AI (No. 4) | probable bug | yes, typing `store P1` threw ArrayIndexOutOfBoundsException at Main.java:13 |
| 9 | ParcelService.java:49 | pickUp returns 0 for an unknown code, the same value as a free pickup | AI (No. 5) | probable bug | yes, `pickup X9 10` printed Fee: 0.0 |
| 10 | Main.java:8 | The Scanner is never closed | AI (No. 6) | resource leak (the Java extension also warns, SonarQube does not) | yes, but low risk because it wraps System.in and the program ends right after |
| 11 | Parcel.java:13; ParcelService.java:28, 44, 68 | Status values are repeated string literals | AI (No. 9) | code smell | yes, "STORED" appears twice, but the AI points to No. 28 for the enum when it means No. 27 |
| 12 | ParcelService.java:53, 57, 59, 61 | Magic numbers for the free days and the rates | AI (No. 10) | code smell | yes, 3 appears four times |
| 13 | Main.java:6-27 | main is too complex | AI (No. 11) | code smell | partly, main is only 22 lines, so "too complex" is vague |
| 14 | Main.java:11 | split(" ") makes empty tokens when there are two spaces | AI (No. 12) | probable bug | yes, `store  P1 6501 2` (two spaces) read 6501 as the size and printed No free locker |
| 15 | ParcelService.java:24-37 | storeParcel finds a locker, sets the status, sends an SMS and saves the file | AI (No. 13) | procedural cohesion | yes, the steps are related only by their order |
| 16 | ParcelService.java:39-50 | pickUp frees the locker, sets the status, saves the file and computes the fee | AI (No. 14) | procedural cohesion | yes, the same pattern as storeParcel |
| 17 | ParcelService.java:64-72 | printParcels(boolean) prints two different reports chosen by a flag | AI (No. 15) | logical cohesion | yes, forStaff selects which report runs |
| 18 | Main.java:6-27 | main selects unrelated actions by the command word | AI (No. 16) | logical cohesion | yes, but this is normal for a command loop |
| 19 | ParcelService.java:9 | ParcelService does lockers, fees, SMS, console output and CSV saving | AI (No. 17) | weak class cohesion | yes, five different jobs in one class |
| 20 | ParcelService.java:52-62 | fee uses no attribute of ParcelService | AI (No. 18) | weak class cohesion | yes, it uses only parcel.getSize() |
| 21 | Parcel.java:3-27 | Parcel has a status but no methods to change it | AI (No. 19) | weak class cohesion | yes, ParcelService changes status at lines 28 and 44 |
| 22 | ParcelService.java:28, 44, 68 | ParcelService reads and writes Parcel.status directly | AI (No. 20) | content coupling | yes, status is a public field written from another class |
| 23 | Main.java:19, 21 | Main passes the flag forStaff to printParcels | AI (No. 21) | control coupling | yes, the flag changes what the method does |
| 24 | ParcelService.java:46 | fee receives the whole Parcel but uses only its size | AI (No. 22) | stamp coupling | partly, both methods are in the same class, so it is not coupling between classes |
| 25 | Main.java:13-15 | Main gets back a whole Locker only to read its number | AI (No. 23) | stamp coupling | partly, storeParcel does need the whole Parcel, only the returned Locker is stamp coupling |
| 26 | Main.java:22 | Main reaches through getLockers() to count free lockers | AI (No. 24) | strong coupling (reach-through) | yes, Main does domain logic on ParcelService's lockers |
| 27 | ParcelService.java:41-42 | locker.getParcel().getCode() reaches through Locker to Parcel | AI (No. 25) | strong coupling (reach-through) | yes, a chain of two calls |
| 28 | Locker.java:5; Parcel.java:6; ParcelService.java:15-17, 26, 56, 58 | Size is an int code 1, 2, 3 that several classes must read the same way | AI (No. 26) | connascence of convention | yes, the comments at Locker.java:5 and Parcel.java:6 repeat the meaning |
| 29 | Parcel.java:13; ParcelService.java:28, 44, 68 | Two classes must agree on the exact status strings | AI (No. 27) | connascence of convention | yes, a typo such as "STORE" would still compile |
| 30 | Main.java:13; ParcelService.java:15-17 | Parcel(String, String, int) and Locker(int, int) take arguments of the same type | AI (No. 28) | connascence of position | yes, swapping code and studentId still compiles |
| 31 | ParcelService.java:36, 49 | null means no free locker and 0 means not found | AI (No. 29) | connascence of convention | yes, Main must know both special values |
| 32 | Parcel.java:7 | The field status is public | AI (No. 30) | field that is not private | yes, it is the only field that is not private |
| 33 | ParcelService.java:20-22 | getLockers() returns the internal list | AI (No. 31) | internal collection returned | yes, a caller can call getLockers().clear() |
| 34 | Locker.java:25-27 | setParcel lets any caller skip the free and size check | AI (No. 32) | weak encapsulation | yes, the check lives only in ParcelService.java:26 |

2.1 Name two problems that only the AI review found.

First, Parcel.status is a public field that ParcelService changes directly at ParcelService.java:28 and 44, which is content coupling (rows 22 and 32). Second, Main passes the flag forStaff to printParcels at Main.java:19 and 21, which is control coupling and gives printParcels logical cohesion (rows 17 and 23).

2.2 Name one problem that only SonarQube found, or write none.

java:S106: System.out is used instead of a logger at ParcelService.java:30, 67, 69 and Main.java:9, 15, 17, 22 (row 7).

2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.

AI finding No. 22 calls ParcelService.java:46 -> 52 stamp coupling, but fee and pickUp are both methods of ParcelService, so this is not an interaction between classes, which is what the prompt asked about. I checked every finding by opening the file at the line the AI gave, and I ran the program to test the bugs: `pickup P1 5` printed Fee: 0.0, `store P1` crashed with ArrayIndexOutOfBoundsException, and two spaces in a command shifted the arguments.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | | |

Number of SonarQube issues that remain after your fixes:

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| | | |
| | | |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text

```

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | | |
| AI tool and exact model | | |
| Prompt (B or C as given, or your changed prompt in full) | | |
| Builds with mvn package (yes or no) | | |
| Number of classes, interfaces, enums and records | | |
| SonarQube issues, total | | |
| SonarQube issues, by rule | | |
| AI review findings in Check 1 | | |
| AI review findings in Check 2 | | |
| Review output file in the reviews folder | | |

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

5.2 When should you not trust the AI review? Give an example from this lab.

5.3 What will you do differently in Project Phase 2 after this lab?

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| Claude Code, Claude Opus 5.5 (claude-opus-5-5), 2026-10-25 | Task 2: review of parcelpoint with Prompt A, unchanged | reviews/parcelpoint-review.md | opened each file at the line given, and ran the program to test the bugs |
| Claude Code, Claude Opus 5.5 (claude-opus-5-5), 2026-10-25 | Task 2: draft of Table 2 and answers 2.1 to 2.3 | LAB_REPORT.md, Task 2 | compared each row with Table 1, the review file and the code |
