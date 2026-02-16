package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTextViewTest {

  /**
   * Helper method to test empty boards with different sizes
   */
  private void emptyBoardHelper(int w, int h, String expectedHeader, String expectedBody) {
    Board<Character> b1 = new BattleShipBoard<Character>(w, h, 'X');  
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
    Board<Character> wideBoard = new BattleShipBoard<Character>(11, 20, 'X'); 
    Board<Character> tallBoard = new BattleShipBoard<Character>(10, 27, 'X'); 
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(wideBoard));
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(tallBoard));
  }

  //tests after adding some ships
  @Test
  public void test_display_board_with_multiple_ships() {
    Board<Character> b = new BattleShipBoard<Character>(3, 3, 'X'); 
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

  @Test
  public void test_display_enemy_board() {
    Board<Character> b = new BattleShipBoard<Character>(4, 3, 'X');
    BoardTextView view = new BoardTextView(b);
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> dst = factory.makeDestroyer(new Placement("A3V"));
    Ship<Character> sub = factory.makeSubmarine(new Placement("B0V"));
    b.tryAddShip(dst);
    b.tryAddShip(sub);
    String myView =
        "  0|1|2|3\n" +
        "A  | | |d A\n" +
        "B s| | |d B\n" +  
        "C s| | |d C\n" +
        "  0|1|2|3\n";
    assertEquals(myView, view.displayMyOwnBoard());
    b.fireAt(new Coordinate(0, 3));  // Hit destroyer
    b.fireAt(new Coordinate(1, 0));  // Hit submarine  
    b.fireAt(new Coordinate(2, 2));  // Miss
    String enemyView =
        "  0|1|2|3\n" +
        "A  | | |d A\n" +
        "B s| | |  B\n" +
        "C  | |X|  C\n" +
        "  0|1|2|3\n";
    assertEquals(enemyView, view.displayEnemyBoard());
  }

@Test
public void test_display_my_board_with_enemy_next_to_it() {
    Board<Character> myBoard = new BattleShipBoard<Character>(4, 3, 'X');
    Board<Character> enemyBoard = new BattleShipBoard<Character>(4, 3, 'X');
    BoardTextView myView = new BoardTextView(myBoard);
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> mySub = factory.makeSubmarine(new Placement("A0V")); 
    Ship<Character> mySub2 = factory.makeSubmarine(new Placement("A2H"));  
    myBoard.tryAddShip(mySub);
    myBoard.tryAddShip(mySub2);
    Ship<Character> enemySub = factory.makeSubmarine(new Placement("A1V")); 
    enemyBoard.tryAddShip(enemySub);
    //Attack!
    enemyBoard.fireAt(new Coordinate(0, 1)); 
    enemyBoard.fireAt(new Coordinate(2, 0)); 
    
    String expected = 
        "     Your ocean                              Enemy's ocean\n" +
        "  0|1|2|3                    0|1|2|3\n" +
        "A s| |s|s A                A  |s| |  A\n" +
        "B s| | |  B                B  | | |  B\n" +
        "C  | | |  C                C X| | |  C\n" +
        "  0|1|2|3                    0|1|2|3\n";
    
    String actual = myView.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Enemy's ocean");
    assertEquals(expected, actual);
}
/* 
@Test
public void test_display_side_by_side_empty_boards() {
    Board<Character> myBoard = new BattleShipBoard<Character>(2, 2, 'X');
    Board<Character> enemyBoard = new BattleShipBoard<Character>(2, 2, 'X');
    BoardTextView myView = new BoardTextView(myBoard);
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String expected = 
        "     Player A               Player B\n" +
        "  0|1            0|1\n" +
        "A  |  A        A  |  A\n" +
        "B  |  B        B  |  B\n" +
        "  0|1            0|1\n";
    
    String actual = myView.displayMyBoardWithEnemyNextToIt(enemyView, "Player A", "Player B");
    assertEquals(expected, actual);
}*/

@Test
public void test_display_side_by_side_different_sizes() {
    // Test with different board sizes (5x5)
    Board<Character> myBoard = new BattleShipBoard<Character>(5, 5, 'X');
    Board<Character> enemyBoard = new BattleShipBoard<Character>(5, 5, 'X');
    BoardTextView myView = new BoardTextView(myBoard);
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String result = myView.displayMyBoardWithEnemyNextToIt(enemyView, "Player A", "Player B");
    assertTrue(result.contains("Player A"));
    assertTrue(result.contains("Player B"));
    String[] lines = result.split("\n");
    assertEquals(8, lines.length);
}

}