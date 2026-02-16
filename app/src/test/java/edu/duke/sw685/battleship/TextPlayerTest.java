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
/* 
  @Test
  void test_doPlacementPhase() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String input = "A0V\n"+"A1V\n" +      // 2 submarines
                   "a0V\n"+"a1V\n"+"a2V\n" + // 3 destroyers
                   "a0V\n"+"a1V\n"+"a2V\n" + // 3 battleships
                   "a0V\n"+"a1V\n";       // 2 carriers
    
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
*/
  // Helper method to count how many times a substring appears in a string
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
//Test the countOccurrences helper method
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
    // First invalid format, then valid
    TextPlayer player = createTextPlayer(10, 20, "AAV\nA0V\n", bytes);
    
    player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer"));
    
    String output = bytes.toString();
    assertTrue(output.contains("That placement is invalid"));
    // Should still succeed after retry
    assertTrue(output.contains("A d| | | | | | | | |  A"));
  }

  @Test
  void test_doOnePlacement_with_invalid_orientation() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    // First invalid orientation, then valid
    TextPlayer player = createTextPlayer(10, 20, "A0Q\nA0V\n", bytes);
    
    player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer"));
    
    String output = bytes.toString();
    assertTrue(output.contains("That placement is invalid"));
    // Should still succeed after retry
    assertTrue(output.contains("A d| | | | | | | | |  A"));
  }

  @Test
  void test_doOnePlacement_with_out_of_bounds() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    // First out of bounds, then valid
    TextPlayer player = createTextPlayer(10, 20, "T0V\nA0V\n", bytes);
    
    player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer"));
    
    String output = bytes.toString();
    assertTrue(output.contains("That placement is invalid: the ship goes off the bottom of the board."));
    // Should still succeed after retry
    assertTrue(output.contains("A d| | | | | | | | |  A"));
  }



  @Test
  void test_doOnePlacement_eof() {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "", bytes);  // Empty = EOF
    
    assertThrows(EOFException.class, 
                 () -> player.doOnePlacement("Destroyer", player.shipCreationFns.get("Destroyer")));
  }
}