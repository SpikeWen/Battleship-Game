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

    player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer"));
    assertEquals(expected, bytes.toString());
  }

  @Test
  void test_doPlacementPhase() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "A0V\nA1V\n" +      // 2 submarines
                   "B0V\nB1V\nB2V\n" + // 3 destroyers
                   "C0V\nC1V\nC2V\n" + // 3 battleships
                   "D0V\nD1V\n";       // 2 carriers
    
    TextPlayer player = createTextPlayer(10, 20, input, bytes);

    player.doPlacementPhase();
    
    String output = bytes.toString();
    assertTrue(output.contains("  0|1|2|3|4|5|6|7|8|9"));
    assertTrue(output.contains("Player A: you are going to place the following ships"));
    assertTrue(output.contains("Player A where do you want to place a Submarine?"));
    assertTrue(output.contains("Player A where do you want to place a Destroyer?"));
    assertTrue(output.contains("Player A where do you want to place a Battleship?"));
    assertTrue(output.contains("Player A where do you want to place a Carrier?"));
    
    // countOccurrences is a helper method to count how many times a substring appears in a string
    int subCount = countOccurrences(output, "Player A where do you want to place a Submarine?");
    assertEquals(2, subCount);
    int dstCount = countOccurrences(output, "Player A where do you want to place a Destroyer?");
    assertEquals(3, dstCount);
    int batCount = countOccurrences(output, "Player A where do you want to place a Battleship?");
    assertEquals(3, batCount);
    int carCount = countOccurrences(output, "Player A where do you want to place a Carrier?");
    assertEquals(2, carCount);
  }

  // Helper method to count how many times a substring appears in a string
  private int countOccurrences(String str, String substr) {
    int count = 0;
    int index = 0;
    index=str.indexOf(substr, index);
    while (index  != -1) {

      count++;
      index += substr.length();
          index=str.indexOf(substr, index);
    }
    return count;
  }
  @Test
void test_countOccurrences() {
    String str3 = "hello world";
    assertEquals(0, countOccurrences(str3, "goodbye"));
}
}