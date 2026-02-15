package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class TextPlayerTest {

  /**
   * Helper method to create a TextPlayer for testing
   * 
   * @param w is the board width
   * @param h is the board height
   * @param inputData is the input string
   * @param bytes is the output stream
   * @return a TextPlayer for testing
   */
  private TextPlayer createTextPlayer(int w, int h, String inputData, OutputStream bytes) {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h);
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

    player.doOnePlacement();
    assertEquals(expected, bytes.toString());
  }

  @Test
  void test_doPlacementPhase() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(4, 3, "A0V\n", bytes);

    player.doPlacementPhase();
    
    String output = bytes.toString();
    
    assertTrue(output.contains("  0|1|2|3"));
    assertTrue(output.contains("Player A: you are going to place the following ships"));
    assertTrue(output.contains("2 \"Submarines\" ships that are 1x2"));
    assertTrue(output.contains("3 \"Destroyers\" that are 1x3"));
    assertTrue(output.contains("3 \"Battleships\" that are 1x4"));
    assertTrue(output.contains("2 \"Carriers\" that are 1x6"));
    
    // Check that output contains the placement prompt
    assertTrue(output.contains("Player A where do you want to place a Destroyer?"));
    assertTrue(output.contains("A d| | |  A"));
  }
}