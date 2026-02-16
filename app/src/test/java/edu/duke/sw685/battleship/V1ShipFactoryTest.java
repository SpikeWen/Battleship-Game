package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V1ShipFactoryTest {

  /**
   * Helper method to check a ship's properties
   * 
   * @param testShip is the ship to check
   * @param expectedName is the expected name
   * @param expectedLetter is the expected display letter
   * @param expectedLocs are the expected coordinates the ship occupies
   */
  private void checkShip(Ship<Character> testShip, String expectedName,
                         char expectedLetter, Coordinate... expectedLocs) {
    // Check name
    assertEquals(expectedName, testShip.getName());
    for (Coordinate c : expectedLocs) {
      assertTrue(testShip.occupiesCoordinates(c));
      // 'true'  for self view
      assertEquals(expectedLetter, testShip.getDisplayInfoAt(c, true));
    }
    assertFalse(testShip.occupiesCoordinates(new Coordinate(10, 10)));
  }

  @Test
  public void test_makeSubmarine() {
    V1ShipFactory f = new V1ShipFactory();
    Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> sub = f.makeSubmarine(v1_2);
    checkShip(sub, "Submarine", 's', 
              new Coordinate(1, 2), 
              new Coordinate(2, 2));
    Placement h0_0 = new Placement(new Coordinate(0, 0), 'H');
    Ship<Character> subH = f.makeSubmarine(h0_0);
    checkShip(subH, "Submarine", 's',
              new Coordinate(0, 0),
              new Coordinate(0, 1));
  }

  @Test
  public void test_makeDestroyer() {
    V1ShipFactory f = new V1ShipFactory();
    Placement v1_2 = new Placement(new Coordinate(1, 2), 'V');
    Ship<Character> dst = f.makeDestroyer(v1_2);
    checkShip(dst, "Destroyer", 'd', 
              new Coordinate(1, 2), 
              new Coordinate(2, 2), 
              new Coordinate(3, 2));
    Placement h5_5 = new Placement(new Coordinate(5, 5), 'H');
    Ship<Character> dstH = f.makeDestroyer(h5_5);
    checkShip(dstH, "Destroyer", 'd',
              new Coordinate(5, 5),
              new Coordinate(5, 6),
              new Coordinate(5, 7));
  }

  @Test
  public void test_makeBattleship() {
    V1ShipFactory f = new V1ShipFactory();
    Placement v0_0 = new Placement(new Coordinate(0, 0), 'V');
    Ship<Character> bat = f.makeBattleship(v0_0);
    checkShip(bat, "Battleship", 'b',
              new Coordinate(0, 0),
              new Coordinate(1, 0),
              new Coordinate(2, 0),
              new Coordinate(3, 0));

    Placement h2_3 = new Placement(new Coordinate(2, 3), 'H');
    Ship<Character> batH = f.makeBattleship(h2_3);
    checkShip(batH, "Battleship", 'b',
              new Coordinate(2, 3),
              new Coordinate(2, 4),
              new Coordinate(2, 5),
              new Coordinate(2, 6));
  }

  @Test
  public void test_makeCarrier() {
    V1ShipFactory f = new V1ShipFactory();
    Placement v3_4 = new Placement(new Coordinate(3, 4), 'V');
    Ship<Character> car = f.makeCarrier(v3_4);
    checkShip(car, "Carrier", 'c',
              new Coordinate(3, 4),
              new Coordinate(4, 4),
              new Coordinate(5, 4),
              new Coordinate(6, 4),
              new Coordinate(7, 4),
              new Coordinate(8, 4));
    
    Placement h1_1 = new Placement(new Coordinate(1, 1), 'H');
    Ship<Character> carH = f.makeCarrier(h1_1);
    checkShip(carH, "Carrier", 'c',
              new Coordinate(1, 1),
              new Coordinate(1, 2),
              new Coordinate(1, 3),
              new Coordinate(1, 4),
              new Coordinate(1, 5),
              new Coordinate(1, 6));
  }

  @Test
  public void test_invalid_orientation() {
    V1ShipFactory f = new V1ShipFactory();
    Placement invalid = new Placement(new Coordinate(0, 0), 'X');
    assertThrows(IllegalArgumentException.class, () -> f.makeSubmarine(invalid));
    assertThrows(IllegalArgumentException.class, () -> f.makeDestroyer(invalid));
    assertThrows(IllegalArgumentException.class, () -> f.makeBattleship(invalid));
    assertThrows(IllegalArgumentException.class, () -> f.makeCarrier(invalid));
  }
}