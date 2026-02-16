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
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> sub1 = factory.makeSubmarine(new Placement("A0V"));
    assertNull(b.tryAddShip(sub1));  // null means success
    Ship<Character> sub2 = factory.makeSubmarine(new Placement("A0V"));
    String result = b.tryAddShip(sub2);
    assertNotNull(result);  // should return error message
    assertEquals("That placement is invalid: the ship overlaps another ship.", result);
}

@Test
public void test_tryAddShipCollision() {
    BattleShipBoard<Character> b = new BattleShipBoard<Character>(10, 20);
    V1ShipFactory factory = new V1ShipFactory();
    Ship<Character> sub1 = factory.makeSubmarine(new Placement("A0V"));
    assertNull(b.tryAddShip(sub1));
    Ship<Character> sub2 = factory.makeSubmarine(new Placement("A0V"));
    String result = b.tryAddShip(sub2);
    assertEquals("That placement is invalid: the ship overlaps another ship.", result);
}

@Test
public void test_tryAddShipOutOfBounds() {
    BattleShipBoard<Character> b = new BattleShipBoard<Character>(10, 20);
    V1ShipFactory factory = new V1ShipFactory();
    
    Ship<Character> car = factory.makeCarrier(new Placement("P0V"));
    String result = b.tryAddShip(car);
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", result);
}

@Test
public void test_tryAddShipValid() {
    BattleShipBoard<Character> b = new BattleShipBoard<Character>(10, 20);
    V1ShipFactory factory = new V1ShipFactory();
    assertNull(b.tryAddShip(factory.makeSubmarine(new Placement("A0V"))));
}
}