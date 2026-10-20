# Java Applications & Data Structures

**Java coursework covering algorithms, linked structures, file processing, and desktop interfaces.**

This repository collects individual exercises rather than one application. Start with the examples below to see how the code applies core computer science concepts.

## Selected work

| Example | Source | Concepts |
| --- | --- | --- |
| Shortest-path desktop app | [RomaniaShortestPath.java](RomaniaShortestPath.java) | Dijkstra, Bellman–Ford, weighted graphs, path reconstruction, JavaFX |
| Linked-list stack and queue | [Main.java](Main.java), [SinglyList.java](SinglyList.java), [StackLL.java](StackLL.java), [QueueLL.java](QueueLL.java) | Generics, LIFO/FIFO behavior, linked nodes, command-file processing |
| Notepad and drawing tool | [MyFirstrNotepadVersion2.java](MyFirstrNotepadVersion2.java) | JavaFX events, text files, a drawing canvas, image export |
| AVL map implementation | [AVLMap.java](AVLMap.java) | Tree-based map operations and balancing |
| Student record reader | [StudentTracker.java](StudentTracker.java) | Binary file input, EOF handling, typed records |

Other files include a card game, pet-related object-oriented exercises, and web/link utilities.

## Run the stack and queue example

With a JDK installed, run these commands from the repository root:

```bash
javac -d out Main.java SinglyList.java StackLL.java QueueLL.java
java -cp out Main operations.txt
```

Expected output with the included command file:

```text
FINAL STACK (top first): 7 1
FINAL QUEUE (front first): 3 9 4
```

The program reads `PUSH`, `POP`, `ENQ`, and `DEQ` commands. A stack removes the most recently added item; a queue removes the earliest item.

## Run a JavaFX example

The desktop examples require a compatible JDK and JavaFX SDK. Set `PATH_TO_FX` to your JavaFX SDK's `lib` directory, then run:

```bash
javac --module-path "$PATH_TO_FX" --add-modules javafx.controls -d out RomaniaShortestPath.java
java --module-path "$PATH_TO_FX" --add-modules javafx.controls -cp out RomaniaShortestPath
```

Use **Run Dijkstra**, **Run Bellman-Ford**, or **Run Both** to compare paths from Arad to Bucharest using the graph encoded in the source. This is an educational graph, not a navigation service with live road distances.

For the notepad, compile its source separately with `javafx.controls,javafx.swing`. Individual exercises have different package declarations and dependencies; compile the files for the example you are using rather than all files at once.

## Review notes

The stack/queue compile-and-run instructions were checked against the included `operations.txt`. The GUI examples and other exercises have not been comprehensively tested as one suite. Coursework retains its original structure and comments.

For a smaller application with automated integration checks, see my [Python + SQLite Staff Manager](https://github.com/Stormshaper455/My-projects-/tree/main/python-sql-staff-manager).
