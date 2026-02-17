package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class V2ShipFactoryTest {

  private V2ShipFactory factory = new V2ShipFactory();

  // Helper to collect coordinates from a ship into a HashSet
  private HashSet<Coordinate> getShipCoords(Ship<Character> ship) {
    HashSet<Coordinate> coords = new HashSet<>();
    for (Coordinate c : ship.getCoordinates()) {
      coords.add(c);
    }
    return coords;
  }

  // Helper to check a ship has exactly the expected coordinates
  private void checkShip(Ship<Character> ship, String expectedName, Coordinate... expectedCoords) {
    assertEquals(expectedName, ship.getName());
    HashSet<Coordinate> actual = getShipCoords(ship);
    assertEquals(expectedCoords.length, actual.size());
    for (Coordinate c : expectedCoords) {
      assertTrue(actual.contains(c), "Expected " + c + " but not found in ship");
    }
  }

  // --- Submarine (same as V1, H/V only) ---
  @Test
  void test_makeSubmarine() {
    Ship<Character> sub = factory.makeSubmarine(new Placement("A0V"));
    checkShip(sub, "Submarine", new Coordinate(0,0), new Coordinate(1,0));
    Ship<Character> subH = factory.makeSubmarine(new Placement("B1H"));
    checkShip(subH, "Submarine", new Coordinate(1,1), new Coordinate(1,2));
  }

  @Test
  void test_makeSubmarine_invalidOrientation() {
    assertThrows(IllegalArgumentException.class, () -> factory.makeSubmarine(new Placement(new Coordinate(0,0), 'U')));
  }

  // --- Destroyer (same as V1, H/V only) ---
  @Test
  void test_makeDestroyer() {
    Ship<Character> des = factory.makeDestroyer(new Placement("A0V"));
    checkShip(des, "Destroyer", new Coordinate(0,0), new Coordinate(1,0), new Coordinate(2,0));
    Ship<Character> desH = factory.makeDestroyer(new Placement("A0H"));
    checkShip(desH, "Destroyer", new Coordinate(0,0), new Coordinate(0,1), new Coordinate(0,2));
  }

  @Test
  void test_makeDestroyer_invalidOrientation() {
    assertThrows(IllegalArgumentException.class, () -> factory.makeDestroyer(new Placement(new Coordinate(0,0), 'R')));
  }

  // --- Battleship (U/R/D/L) ---
  @Test
  void test_makeBattleship_Up() {
    //  *b
    //  bbb
    Ship<Character> ship = factory.makeBattleship(new Placement("A0U"));
    checkShip(ship, "Battleship",
        new Coordinate(0,1), new Coordinate(1,0), new Coordinate(1,1), new Coordinate(1,2));
  }

  @Test
  void test_makeBattleship_Right() {
    //  b
    //  bb
    //  b
    Ship<Character> ship = factory.makeBattleship(new Placement("A0R"));
    checkShip(ship, "Battleship",
        new Coordinate(0,0), new Coordinate(1,0), new Coordinate(1,1), new Coordinate(2,0));
  }

  @Test
  void test_makeBattleship_Down() {
    //  bbb
    //   b
    Ship<Character> ship = factory.makeBattleship(new Placement("A0D"));
    checkShip(ship, "Battleship",
        new Coordinate(0,0), new Coordinate(0,1), new Coordinate(0,2), new Coordinate(1,1));
  }

  @Test
  void test_makeBattleship_Left() {
    //   b
    //  bb
    //   b
    Ship<Character> ship = factory.makeBattleship(new Placement("A0L"));
    checkShip(ship, "Battleship",
        new Coordinate(0,1), new Coordinate(1,0), new Coordinate(1,1), new Coordinate(2,1));
  }

  @Test
  void test_makeBattleship_invalidOrientation() {
    assertThrows(IllegalArgumentException.class, () -> factory.makeBattleship(new Placement("A0V")));
    assertThrows(IllegalArgumentException.class, () -> factory.makeBattleship(new Placement("A0H")));
  }

  // --- Carrier (U/R/D/L) ---
  @Test
  void test_makeCarrier_Up() {
    //  c
    //  c
    //  cc
    //  cc
    //   c
    Ship<Character> ship = factory.makeCarrier(new Placement("A0U"));
    checkShip(ship, "Carrier",
        new Coordinate(0,0), new Coordinate(1,0), new Coordinate(2,0), new Coordinate(2,1),
        new Coordinate(3,0), new Coordinate(3,1), new Coordinate(4,1));
  }

  @Test
  void test_makeCarrier_Right() {
    //  *cccc
    //  ccc
    Ship<Character> ship = factory.makeCarrier(new Placement("A0R"));
    checkShip(ship, "Carrier",
        new Coordinate(0,1), new Coordinate(0,2), new Coordinate(0,3), new Coordinate(0,4),
        new Coordinate(1,0), new Coordinate(1,1), new Coordinate(1,2));
  }

  @Test
  void test_makeCarrier_Down() {
    //  c
    //  cc
    //  cc
    //   c
    //   c
    Ship<Character> ship = factory.makeCarrier(new Placement("A0D"));
    checkShip(ship, "Carrier",
        new Coordinate(0,0), new Coordinate(1,0), new Coordinate(1,1),
        new Coordinate(2,0), new Coordinate(2,1), new Coordinate(3,1), new Coordinate(4,1));
  }

  @Test
  void test_makeCarrier_Left() {
    //    ccc
    //  cccc
    Ship<Character> ship = factory.makeCarrier(new Placement("A0L"));
    checkShip(ship, "Carrier",
        new Coordinate(0,1), new Coordinate(0,2), new Coordinate(0,3),
        new Coordinate(1,0), new Coordinate(1,1), new Coordinate(1,2), new Coordinate(1,3));
  }

  @Test
  void test_makeCarrier_invalidOrientation() {
    assertThrows(IllegalArgumentException.class, () -> factory.makeCarrier(new Placement("A0V")));
    assertThrows(IllegalArgumentException.class, () -> factory.makeCarrier(new Placement("A0H")));
  }

  // --- Test with non-origin coordinates ---
  @Test
  void test_makeBattleship_nonOrigin() {
    Ship<Character> ship = factory.makeBattleship(new Placement("C2U"));
    checkShip(ship, "Battleship",
        new Coordinate(2,3), new Coordinate(3,2), new Coordinate(3,3), new Coordinate(3,4));
  }

  @Test
  void test_makeCarrier_nonOrigin() {
    Ship<Character> ship = factory.makeCarrier(new Placement("B1U"));
    checkShip(ship, "Carrier",
        new Coordinate(1,1), new Coordinate(2,1), new Coordinate(3,1), new Coordinate(3,2),
        new Coordinate(4,1), new Coordinate(4,2), new Coordinate(5,2));
  }
}
