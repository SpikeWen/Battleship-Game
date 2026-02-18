package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class TextPlayerTest {

  private TextPlayer createTextPlayer(int w, int h, String inputData, OutputStream bytes) {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h,'X');
    V1ShipFactory shipFactory = new V1ShipFactory();
    return new TextPlayer("A", board, input, output, shipFactory);
  }

  @Test
  void test_read_placement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "B2V\nC8H\na4v\n", bytes);

    String prompt = "Please enter a location for a ship:";
    Placement[] expected = new Placement[3];
    expected[0] = new Placement(new Coordinate(1, 2), 'V');
    expected[1] = new Placement(new Coordinate(2, 8), 'H');
    expected[2] = new Placement(new Coordinate(0, 4), 'V');

    for (int i = 0; i < expected.length; i++) {
      Placement p = player.readPlacement(prompt);
      assertEquals(p, expected[i]);
      assertEquals(prompt + "\n", bytes.toString());
      bytes.reset();
    }
  }

  @Test
  void test_doOnePlacement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(4, 3, "A0V\n", bytes);

    String expectedPrompt = "Player A where do you want to place a Destroyer?\n";
    String expectedBoard =
        "  0|1|2|3\n" +
        "A d| | |  A\n" +
        "B d| | |  B\n" +
        "C d| | |  C\n" +
        "  0|1|2|3\n";
    String expected = expectedPrompt + expectedBoard;

    player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer"));
    assertEquals(expected, bytes.toString());
  }

  private int countOccurrences(String str, String substr) {
    int count = 0;
    int index = 0;
    index=str.indexOf(substr, index);
    while ((index )!= -1) {
      count++;
      index += substr.length();
      index = str.indexOf(substr, index);
    }
    return count;
  }

  @Test
  void test_countOccurrences() {
    assertEquals(3, countOccurrences("hello hello hello", "hello"));
    assertEquals(1, countOccurrences("hello world", "hello"));
    assertEquals(0, countOccurrences("hello world", "goodbye"));
    assertEquals(0, countOccurrences("", "test"));
    assertEquals(2, countOccurrences("aaaa", "aa"));
  }

  @Test
  void test_read_placement_eof() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "", bytes);
    assertThrows(EOFException.class, () -> player.readPlacement("Enter placement:"));
  }

  @Test
  void test_doOnePlacement_with_invalid_format() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "AAV\nA0V\n", bytes);
    player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer"));
    String output = bytes.toString();
    assertTrue(output.contains("That placement is invalid"));
    assertTrue(output.contains("A d| | | | | | | | |  A"));
  }

  @Test
  void test_doOnePlacement_with_invalid_orientation() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "A0Q\nA0V\n", bytes);
    player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer"));
    String output = bytes.toString();
    assertTrue(output.contains("That placement is invalid"));
    assertTrue(output.contains("A d| | | | | | | | |  A"));
  }

  @Test
  void test_doOnePlacement_with_out_of_bounds() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "T0V\nA0V\n", bytes);
    player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer"));
    String output = bytes.toString();
    assertTrue(output.contains("That placement is invalid: the ship goes off the bottom of the board."));
    assertTrue(output.contains("A d| | | | | | | | |  A"));
  }

  @Test
  void test_doOnePlacement_eof() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "", bytes);
    assertThrows(EOFException.class,
                 () -> player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer")));
  }

  @Test
  void testReadCoordinate() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "B2\nC8\na4\n", bytes);
    String prompt = "Enter a coordinate:";
    Coordinate c1 = player.readCoordinate(prompt);
    assertEquals(new Coordinate(1, 2), c1);
    bytes.reset();
    Coordinate c2 = player.readCoordinate(prompt);
    assertEquals(new Coordinate(2, 8), c2);
    bytes.reset();
    Coordinate c3 = player.readCoordinate(prompt);
    assertEquals(new Coordinate(0, 4), c3);
  }

  @Test
  void test_read_coordinate_eof() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "", bytes);
    assertThrows(EOFException.class, () -> player.readCoordinate("Enter:"));
  }

  // --- playOneTurn tests: now need "F\n" to select Fire action ---

  @Test
  void test_playOneTurn_fire_hit() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> enemyShip = factory.makeSubmarine(new Placement("A0V"));
    enemyBoard.tryAddShip(enemyShip);
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    // "F" to choose Fire, then "A0" coordinate
    TextPlayer player = createTextPlayer(10, 20, "F\nA0\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    assertTrue(output.contains("Possible actions for Player A"));
    assertTrue(output.contains("Player A where do you want to fire at?"));
    assertTrue(output.contains("OMG! You hit a Submarine!"));
  }

  @Test
  void test_playOneTurn_fire_miss() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    TextPlayer player = createTextPlayer(10, 20, "F\nA5\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    assertTrue(output.contains("Bro! You missed!"));
  }

  @Test
  void test_playOneTurn_fire_invalidThenValid() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    TextPlayer player = createTextPlayer(10, 20, "F\nZZ\nA5\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    assertTrue(output.contains("That coordinate is invalid"));
    assertTrue(output.contains("Bro! You missed!"));
  }

  @Test
  void test_playOneTurn_fire_outOfBoundsThenValid() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    TextPlayer player = createTextPlayer(10, 20, "F\nZ0\nA5\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    assertTrue(output.contains("That coordinate is invalid: it does not have the correct format."));
    assertTrue(output.contains("Bro! You missed!"));
  }

  @Test
  void test_playOneTurn_noSpecialActionsLeft() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    // No "F" needed — goes straight to fire when no special actions remain
    TextPlayer player = createTextPlayer(10, 20, "A5\n", bytes);
    player.moveRemaining = 0;
    player.sonarRemaining = 0;
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    assertFalse(output.contains("Possible actions"));
    assertTrue(output.contains("Bro! You missed!"));
  }

  @Test
  void test_playOneTurn_invalidChoice() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    // "X" invalid choice, then "F" to fire
    TextPlayer player = createTextPlayer(10, 20, "X\nF\nA5\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    assertTrue(output.contains("Invalid choice, please try again."));
    assertTrue(output.contains("Bro! You missed!"));
  }

  @Test
  void test_playOneTurn_eof_in_menu() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    // Empty input → readLine() returns null → EOFException in menu
    TextPlayer player = createTextPlayer(10, 20, "", bytes);
    assertThrows(EOFException.class, () -> player.playOneTurn(enemyBoard, enemyView, "B"));
  }

  // --- Sonar scan tests ---

  @Test
  void test_playOneTurn_sonar() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    enemyBoard.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    enemyBoard.tryAddShip(factory.makeDestroyer(new Placement("A2V")));
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    // "S" for sonar, "B1" as center
    TextPlayer player = createTextPlayer(10, 20, "S\nB1\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    assertTrue(output.contains("Submarines occupy 2 squares"));
    assertTrue(output.contains("Destroyers occupy 3 squares"));
    assertTrue(output.contains("Battleships occupy 0 squares"));
    assertTrue(output.contains("Carriers occupy 0 squares"));
    assertEquals(2, player.sonarRemaining);
  }

  @Test
  void test_playOneTurn_sonar_singleSquare() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    // Place submarine at far corner, scan center only touches 1 square
    enemyBoard.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    // Scan centered at D0 — only A0 is within Manhattan distance 3
    TextPlayer player = createTextPlayer(10, 20, "S\nD0\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    // A0 is at row 0, D0 is at row 3, distance = 3, so A0 is included; B0 at row 1, distance 2 also included
    assertTrue(output.contains("Submarines occupy 2 squares"));
  }

  @Test
  void test_playOneTurn_sonar_invalidCoord() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    // "S" for sonar, then invalid coordinate "ZZ"
    TextPlayer player = createTextPlayer(10, 20, "S\nZZ\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    assertTrue(output.contains("That coordinate is invalid."));
    assertEquals(2, player.sonarRemaining);
  }

  @Test
  void test_sonar_edgeOfBoard() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    enemyBoard.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    // Scan at corner A0 — diamond extends off board, should handle gracefully
    TextPlayer player = createTextPlayer(10, 20, "S\nA0\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String output = bytes.toString();
    assertTrue(output.contains("Submarines occupy"));
  }

  // --- Move ship tests ---

  @Test
  void test_playOneTurn_move_success() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    BufferedReader input = new BufferedReader(new StringReader("M\nA0\nC0V\n"));
    TextPlayer player = new TextPlayer("A", board, input, output, factory);
    // Place a submarine at A0V
    board.tryAddShip(factory.makeSubmarine(new Placement("A0V")));

    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    player.playOneTurn(enemyBoard, enemyView, "B");
    // Ship should now be at C0, D0
    assertNotNull(board.getShipAt(new Coordinate(2, 0)));
    assertNotNull(board.getShipAt(new Coordinate(3, 0)));
    assertNull(board.getShipAt(new Coordinate(0, 0)));
    assertEquals(2, player.moveRemaining);
  }

  @Test
  void test_playOneTurn_move_preservesDamage() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    // Place submarine at A0V, hit A0
    Ship<Character> sub = factory.makeSubmarine(new Placement("A0V"));
    board.tryAddShip(sub);
    sub.recordHitAt(new Coordinate(0, 0));

    BufferedReader input = new BufferedReader(new StringReader("M\nA0\nC0V\n"));
    TextPlayer player = new TextPlayer("A", board, input, output, factory);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    player.playOneTurn(enemyBoard, enemyView, "B");
    // New ship at C0V: C0 should be hit (same relative position), D0 should not
    Ship<Character> movedShip = board.getShipAt(new Coordinate(2, 0));
    assertNotNull(movedShip);
    assertTrue(movedShip.wasHitAt(new Coordinate(2, 0)));
    assertFalse(movedShip.wasHitAt(new Coordinate(3, 0)));
  }

  @Test
  void test_playOneTurn_move_noShipAtCoord() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    // No ship at A5, then choose Fire
    BufferedReader input = new BufferedReader(new StringReader("M\nA5\nF\nA0\n"));
    TextPlayer player = new TextPlayer("A", board, input, output, factory);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    player.playOneTurn(enemyBoard, enemyView, "B");
    String out = bytes.toString();
    assertTrue(out.contains("There is no ship at that coordinate."));
    assertTrue(out.contains("Bro! You missed!"));
    assertEquals(3, player.moveRemaining); // Not decremented since move failed
  }

  @Test
  void test_playOneTurn_move_invalidCoord() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    // "ZZ" is invalid coord, then choose Fire
    BufferedReader input = new BufferedReader(new StringReader("M\nZZ\nF\nA0\n"));
    TextPlayer player = new TextPlayer("A", board, input, output, factory);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    player.playOneTurn(enemyBoard, enemyView, "B");
    String out = bytes.toString();
    assertTrue(out.contains("That coordinate is invalid."));
  }

  @Test
  void test_playOneTurn_move_invalidPlacement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    board.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    // Move to invalid placement "ZZZ", then Fire
    BufferedReader input = new BufferedReader(new StringReader("M\nA0\nZZZ\nF\nA0\n"));
    TextPlayer player = new TextPlayer("A", board, input, output, factory);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    player.playOneTurn(enemyBoard, enemyView, "B");
    String out = bytes.toString();
    assertTrue(out.contains("That placement is invalid"));
  }

  @Test
  void test_playOneTurn_move_outOfBounds() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    board.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    // Try to move off board, then Fire
    BufferedReader input = new BufferedReader(new StringReader("M\nA0\nT0V\nF\nA0\n"));
    TextPlayer player = new TextPlayer("A", board, input, output, factory);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    player.playOneTurn(enemyBoard, enemyView, "B");
    String out = bytes.toString();
    assertTrue(out.contains("the ship goes off the bottom of the board"));
    // Ship should still be at original location
    assertNotNull(board.getShipAt(new Coordinate(0, 0)));
  }

  @Test
  void test_playOneTurn_move_collision() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    board.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    board.tryAddShip(factory.makeSubmarine(new Placement("A2V")));
    // Try to move first sub to where second sub is, then Fire
    BufferedReader input = new BufferedReader(new StringReader("M\nA0\nA2V\nF\nA0\n"));
    TextPlayer player = new TextPlayer("A", board, input, output, factory);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    player.playOneTurn(enemyBoard, enemyView, "B");
    String out = bytes.toString();
    assertTrue(out.contains("overlaps another ship"));
  }

  @Test
  void test_playOneTurn_move_invalidNewShipOrientation() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    board.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    // Try to move sub with orientation U (invalid for submarine), then Fire
    BufferedReader input = new BufferedReader(new StringReader("M\nA0\nA0U\nF\nA0\n"));
    TextPlayer player = new TextPlayer("A", board, input, output, factory);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    player.playOneTurn(enemyBoard, enemyView, "B");
    String out = bytes.toString();
    assertTrue(out.contains("That placement is invalid"));
  }

  // Test choosing M when moveRemaining is 0 (treated as invalid choice)
  @Test
  void test_playOneTurn_move_notAvailable() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    TextPlayer player = createTextPlayer(10, 20, "M\nF\nA5\n", bytes);
    player.moveRemaining = 0;
    player.playOneTurn(enemyBoard, enemyView, "B");
    String out = bytes.toString();
    assertTrue(out.contains("Invalid choice, please try again."));
  }

  // Test choosing S when sonarRemaining is 0
  @Test
  void test_playOneTurn_sonar_notAvailable() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    TextPlayer player = createTextPlayer(10, 20, "S\nF\nA5\n", bytes);
    player.sonarRemaining = 0;
    player.playOneTurn(enemyBoard, enemyView, "B");
    String out = bytes.toString();
    assertTrue(out.contains("Invalid choice, please try again."));
  }

  // Move a HORIZONTAL ship to cover the same-row sort comparator (lines 148-149, 191-192)
  @Test
  void test_playOneTurn_move_horizontalShip() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    // Submarine at A0H: occupies (0,0) and (0,1) — same row, different columns
    board.tryAddShip(factory.makeSubmarine(new Placement("A0H")));
    // Move to C0H
    BufferedReader input = new BufferedReader(new StringReader("M\nA0\nC0H\n"));
    TextPlayer player = new TextPlayer("A", board, input, output, factory);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    player.playOneTurn(enemyBoard, enemyView, "B");
    // Should now be at C0, C1
    assertNotNull(board.getShipAt(new Coordinate(2, 0)));
    assertNotNull(board.getShipAt(new Coordinate(2, 1)));
    assertNull(board.getShipAt(new Coordinate(0, 0)));
    assertEquals(2, player.moveRemaining);
  }

  // Sonar that yields exactly 1 square for a ship type — covers singular "square" output (lines 243-244)
  @Test
  void test_playOneTurn_sonar_singleSquareOutput() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    // Submarine at A9V: cells (0,9) and (1,9)
    // Scan center at A6 (row=0, col=6): Manhattan distance to (0,9) = 3 → included; to (1,9) = 4 → excluded
    enemyBoard.tryAddShip(factory.makeSubmarine(new Placement("A9V")));
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    TextPlayer player = createTextPlayer(10, 20, "S\nA6\n", bytes);
    player.playOneTurn(enemyBoard, enemyView, "B");
    String out = bytes.toString();
    // Exactly 1 submarine square in range
    assertTrue(out.contains("Submarines occupy 1 square"));
    assertTrue(out.contains("Destroyers occupy 0 squares"));
  }

  // doPlacementPhase full flow — covers lines 293+ of TextPlayer
  @Test
  void test_doPlacementPhase() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    // 10 ships: 2 Sub + 3 Des + 3 Battle + 2 Carrier, all placed vertically
    String input = "A0V\nA1V\nA2V\nA3V\nA4V\nA5V\nA6V\nA7V\nA8V\nA9V\n";
    BufferedReader inputReader = new BufferedReader(new StringReader(input));
    PrintStream out = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory shipFactory = new V1ShipFactory();
    TextPlayer player = new TextPlayer("A", board, inputReader, out, shipFactory);

    player.doPlacementPhase();

    String output = bytes.toString();
    assertTrue(output.contains("2 \"Submarines\" ships that are 1x2"));
    assertTrue(output.contains("3 \"Destroyers\" that are 1x3"));
    assertTrue(output.contains("3 \"Battleships\" that have a special shape (use U/R/D/L)"));
    assertTrue(output.contains("2 \"Carriers\" that have a special shape (use U/R/D/L)"));
    // Board should have all ships placed
    assertNotNull(board.getShipAt(new Coordinate(0, 0)));
    assertNotNull(board.getShipAt(new Coordinate(0, 9)));
  }
}
