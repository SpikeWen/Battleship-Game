package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTextViewTest {

  /**
   * Helper method to test empty boards with different sizes
   */
  private void emptyBoardHelper(int w, int h, String expectedHeader, String expectedBody) {
    Board<Character> b1 = new BattleShipBoard<Character>(w, h);
    BoardTextView view = new BoardTextView(b1);
    assertEquals(expectedHeader, view.makeHeader());
    String expected = expectedHeader + expectedBody + expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
  }

  @Test
  public void test_display_empty_2by2() {
    String expectedHeader = "  0|1\n";
    String expectedBody = 
      "A  |  A\n" +
      "B  |  B\n";
    emptyBoardHelper(2, 2, expectedHeader, expectedBody);
  }

  @Test
  public void test_display_empty_3by2() {
    String expectedHeader = "  0|1|2\n";
    String expectedBody = 
      "A  | |  A\n" +
      "B  | |  B\n";
    emptyBoardHelper(3, 2, expectedHeader, expectedBody);
  }

  @Test
  public void test_display_empty_3by5() {
    String expectedHeader = "  0|1|2\n";
    String expectedBody = 
      "A  | |  A\n" +
      "B  | |  B\n" +
      "C  | |  C\n" +
      "D  | |  D\n" +
      "E  | |  E\n";
    emptyBoardHelper(3, 5, expectedHeader, expectedBody);
  }

  @Test
  public void test_invalid_board_size() {
    Board<Character> wideBoard = new BattleShipBoard<Character>(11, 20);
    Board<Character> tallBoard = new BattleShipBoard<Character>(10, 27);
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(wideBoard));
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(tallBoard));
  }

//tests after adding some ships
@Test
public void test_display_board_with_multiple_ships() {
    Board<Character> b = new BattleShipBoard<Character>(3, 3);
    BoardTextView view = new BoardTextView(b);
    
    // Add ships at all positions
    b.tryAddShip(new RectangleShip<Character>("ship1", new Coordinate(0, 0), 's', '*'));
    b.tryAddShip(new RectangleShip<Character>("ship2", new Coordinate(0, 2), 's', '*'));
    b.tryAddShip(new RectangleShip<Character>("ship3", new Coordinate(1, 1), 's', '*'));
    b.tryAddShip(new RectangleShip<Character>("ship4", new Coordinate(2, 0), 's', '*'));
    b.tryAddShip(new RectangleShip<Character>("ship5", new Coordinate(2, 2), 's', '*'));
    
    String expectedHeader = "  0|1|2\n";
    String expected = 
        expectedHeader +
        "A s| |s A\n" +
        "B  |s|  B\n" +
        "C s| |s C\n" +
        expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
}

}