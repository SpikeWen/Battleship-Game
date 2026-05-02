# Battleship

A text-based Battleship game for two players, written in Java 21. Each player can be human or computer-controlled. Players place ships on a 10×20 board, then alternate turns firing, moving ships, and using sonar scans until one side is sunk.

## Requirements

- Java 21
- Gradle (wrapper included)

## Build & Run

```bash
./gradlew run
```

```bash
./gradlew test
```

## Gameplay

### Setup

At startup, each player chooses **human (h)** or **computer (c)**. Human players place ships manually by entering a coordinate and orientation (e.g. `A0V`). The computer places ships automatically.

### Board size

10 columns × 20 rows. Columns are numbered `0–9`, rows are lettered `A–T`.

### Ships (Version 2)

| Ship | Size | Symbol | Orientations |
|------|------|--------|--------------|
| Submarine | 1×2 | `s` | H, V |
| Destroyer | 1×3 | `d` | H, V |
| Battleship | T-shape (4 cells) | `b` | U, R, D, L |
| Carrier | Z-shape (6 cells) | `c` | U, R, D, L |

Each player places: 2 Submarines, 3 Destroyers, 3 Battleships, 2 Carriers.

### Placement format

```
<row><col><orientation>
```

Examples: `A0V` (row A, col 0, vertical), `C3H` (row C, col 3, horizontal), `E5U` (row E, col 5, facing up).

### Turn actions

Each turn the current player sees their own board and a masked view of the enemy board, then chooses:

| Key | Action | Uses |
|-----|--------|------|
| `F` | Fire at a coordinate | Unlimited |
| `M` | Move one of your ships to a new position | 3 per game |
| `S` | Sonar scan — reveals all ships within a diamond radius around a coordinate | 3 per game |

The game ends when all ships of one player are sunk.

### Computer player

The computer fires through every square sequentially (row by row, left to right) and skips the Move/Sonar actions.

## Project structure

```
app/src/main/java/edu/duke/sw685/battleship/
├── App.java                  -- entry point, game loop
├── TextPlayer.java           -- human player logic
├── ComputerPlayer.java       -- AI player
├── BattleShipBoard.java      -- board implementation
├── BoardTextView.java        -- text rendering
├── V1ShipFactory.java        -- rectangle ships only
├── V2ShipFactory.java        -- adds T/Z shaped ships
├── RectangleShip.java
├── NonRectangleShip.java
├── PlacementRuleChecker.java -- chain-of-responsibility validators
├── InBoundsRuleChecker.java
├── NoCollisionRuleChecker.java
└── ...
```
