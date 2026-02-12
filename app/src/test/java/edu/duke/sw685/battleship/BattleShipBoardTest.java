package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
  @Test
  public void test_width_and_height() {
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20);
    assertEquals(10, b1.getWidth());
    assertEquals(20, b1.getHeight());
  }

  @Test
  public void test_invalid_dimensions() {
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, 0));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(0, 20));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, -5));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(-8, 20));
  }

  /**
   * Helper method to check what is at each coordinate on the board
   * 
   * @param b is the board to check
   * @param expected is a 2D array of expected values
   */
  private <T> void checkWhatIsAtBoard(BattleShipBoard<T> b, T[][] expected) {
    for (int row = 0; row < b.getHeight(); row++) {
      for (int col = 0; col < b.getWidth(); col++) {
        Coordinate c = new Coordinate(row, col);
        assertEquals(expected[row][col], b.whatIsAt(c));
      }
    }
  }

  @Test
  public void test_whatIsAt_empty_board() {
    BattleShipBoard<Character> b = new BattleShipBoard<Character>(3, 3);
    Character[][] expected = {
      {null, null, null},
      {null, null, null},
      {null, null, null}
    };
    checkWhatIsAtBoard(b, expected);
  }

  @Test
  public void test_tryAddShip() {
    BattleShipBoard<Character> b = new BattleShipBoard<Character>(3, 3);
    
    // Add a ship at (1, 1)
    Coordinate c1 = new Coordinate(1, 1);
    Ship<Character> s1 = new BasicShip(c1);
    assertTrue(b.tryAddShip(s1));
    
    Character[][] expected1 = {
      {null, null, null},
      {null, 's', null},
      {null, null, null}
    };
    checkWhatIsAtBoard(b, expected1);
    
    // Add another ship at (0, 2)
    Coordinate c2 = new Coordinate(0, 2);
    Ship<Character> s2 = new BasicShip(c2);
    assertTrue(b.tryAddShip(s2));
    
    Character[][] expected2 = {
      {null, null, 's'},
      {null, 's', null},
      {null, null, null}
    };
    checkWhatIsAtBoard(b, expected2);
    
    // Add a third ship at (2, 0)
    Coordinate c3 = new Coordinate(2, 0);
    Ship<Character> s3 = new BasicShip(c3);
    assertTrue(b.tryAddShip(s3));
    
    Character[][] expected3 = {
      {null, null, 's'},
      {null, 's', null},
      {'s', null, null}
    };
    checkWhatIsAtBoard(b, expected3);
  }
}