package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import org.junit.jupiter.api.parallel.ResourceAccessMode;

public class AppTest {


  @Test
  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
  void test_main() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);

    InputStream input = getClass().getClassLoader().getResourceAsStream("input.txt");
    assertNotNull(input);

    InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");
    assertNotNull(expectedStream);

    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;

    try {
      System.setIn(input);
      System.setOut(out);
      App.main(new String[0]);
    } finally {
      System.setIn(oldIn);
      System.setOut(oldOut);
    }

    String expected = new String(expectedStream.readAllBytes());
    String actual = bytes.toString();
    assertEquals(expected, actual);
  }
@Test
void test_doAttackingPhase_player1Wins() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> b1 = new BattleShipBoard<Character>(3, 3, 'X');
    Board<Character> b2 = new BattleShipBoard<Character>(3, 3, 'X');
    V1ShipFactory factory = new V1ShipFactory();

    b1.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    b2.tryAddShip(factory.makeSubmarine(new Placement("A1V")));

    // Turn order: P1 Fire A1 (hit) -> P2 Fire A0 (hit) -> P1 Fire B1 (sink) -> P1 wins
    String input = "F\nA1\nF\nA0\nF\nB1\n";
    BufferedReader inputReader = new BufferedReader(new StringReader(input));
    PrintStream out = new PrintStream(bytes, true);

    TextPlayer player1 = new TextPlayer("A", b1, inputReader, out, factory);
    TextPlayer player2 = new TextPlayer("B", b2, inputReader, out, factory);
    App app = new App(player1, player2);
    app.doAttackingPhase();
    String output = bytes.toString();
    assertTrue(output.contains("Player A won the game!"));
}

@Test
void test_doAttackingPhase_player2Wins() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> b1 = new BattleShipBoard<Character>(3, 3, 'X');
    Board<Character> b2 = new BattleShipBoard<Character>(3, 3, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    b1.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    b2.tryAddShip(factory.makeSubmarine(new Placement("A1V")));
    // P1 misses, P2 sinks P1's sub: P2 wins
    // F → P1 fires C0 (miss), F → P2 fires A0 (hit), F → P1 fires C1 (miss), F → P2 fires B0 (sink) → P2 wins
    String input = "F\nC0\nF\nA0\nF\nC1\nF\nB0\n";
    BufferedReader inputReader = new BufferedReader(new StringReader(input));
    PrintStream out = new PrintStream(bytes, true);
    TextPlayer player1 = new TextPlayer("A", b1, inputReader, out, factory);
    TextPlayer player2 = new TextPlayer("B", b2, inputReader, out, factory);
    App app = new App(player1, player2);
    app.doAttackingPhase();
    String output = bytes.toString();
    assertTrue(output.contains("Player B won the game!"));
}

@Test
void test_doPlacementPhase() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
    Board<Character> b2 = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    // 10 placements each
    String ships = "A0V\nA1V\nA2V\nA3V\nA4V\nA5V\nA6V\nA7V\nA8V\nA9V\n";
    BufferedReader inputReader = new BufferedReader(new StringReader(ships + ships));
    PrintStream out = new PrintStream(bytes, true);
    TextPlayer player1 = new TextPlayer("A", b1, inputReader, out, factory);
    TextPlayer player2 = new TextPlayer("B", b2, inputReader, out, factory);
    App app = new App(player1, player2);
    app.doPlacementPhase();
    assertNotNull(b1.getShipAt(new Coordinate(0, 0)));
    assertNotNull(b2.getShipAt(new Coordinate(0, 0)));
}

@Test
void test_createPlayerHuman() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    BufferedReader input = new BufferedReader(new StringReader("h\n"));
    TextPlayer p = App.createPlayer("A", board, input, out, factory);
    assertFalse(p instanceof ComputerPlayer);
    assertTrue(bytes.toString().contains("Will Player A be controlled by a (h)uman or (c)omputer?"));
}

@Test
void test_createPlayerComputer() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, 'X');
    V1ShipFactory factory = new V1ShipFactory();
    BufferedReader input = new BufferedReader(new StringReader("c\n"));
    TextPlayer p = App.createPlayer("B", board, input, out, factory);
    assertTrue(p instanceof ComputerPlayer);
}

@Test
@ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
void test_main_computerVsComputer() throws IOException {
    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    try {
        System.setIn(new java.io.ByteArrayInputStream("c\nc\n".getBytes()));
        System.setOut(new PrintStream(bytes, true));
        App.main(new String[0]);
    } finally {
        System.setIn(oldIn);
        System.setOut(oldOut);
    }
    String output = bytes.toString();
    assertTrue(output.contains("won the game!"));//one must win
}

}