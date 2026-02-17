package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import org.junit.jupiter.api.parallel.ResourceAccessMode;

public class AppTest {


  @Disabled("Needs V2 input/output files with action menu selections")
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
    // Each turn needs "F\n" to select Fire action
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

}