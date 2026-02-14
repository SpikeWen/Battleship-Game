package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class RectangleShipTest {

  @Test
  public void test_makeCoords() {
    // Test 1x3 vertical ship
    Coordinate upperLeft1 = new Coordinate(1, 2);
    HashSet<Coordinate> coords1 = RectangleShip.makeCoords(upperLeft1, 1, 3);
    assertEquals(3, coords1.size());
    assertTrue(coords1.contains(new Coordinate(1, 2)));
    assertTrue(coords1.contains(new Coordinate(2, 2)));
    assertTrue(coords1.contains(new Coordinate(3, 2)));

    // Test 3x1 horizontal ship
    Coordinate upperLeft2 = new Coordinate(0, 0);
    HashSet<Coordinate> coords2 = RectangleShip.makeCoords(upperLeft2, 3, 1);
    assertEquals(3, coords2.size());
    assertTrue(coords2.contains(new Coordinate(0, 0)));
    assertTrue(coords2.contains(new Coordinate(0, 1)));
    assertTrue(coords2.contains(new Coordinate(0, 2)));

    // Test 2x2 square ship
    Coordinate upperLeft3 = new Coordinate(5, 5);
    HashSet<Coordinate> coords3 = RectangleShip.makeCoords(upperLeft3, 2, 2);
    assertEquals(4, coords3.size());
    assertTrue(coords3.contains(new Coordinate(5, 5)));
    assertTrue(coords3.contains(new Coordinate(5, 6)));
    assertTrue(coords3.contains(new Coordinate(6, 5)));
    assertTrue(coords3.contains(new Coordinate(6, 6)));
  }

  @Test
  public void test_occupies_coordinates() {
    // Test 1x3 ship
    RectangleShip<Character> ship = new RectangleShip<Character>("testship", 
                                                                   new Coordinate(1, 2), 
                                                                   1, 3, 's', '*');
    assertTrue(ship.occupiesCoordinates(new Coordinate(1, 2)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(2, 2)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(3, 2)));
    assertFalse(ship.occupiesCoordinates(new Coordinate(0, 2)));
    assertFalse(ship.occupiesCoordinates(new Coordinate(4, 2)));
    assertFalse(ship.occupiesCoordinates(new Coordinate(1, 1)));
    assertFalse(ship.occupiesCoordinates(new Coordinate(1, 3)));
  }

  @Test
  public void test_getName() {
    RectangleShip<Character> ship1 = new RectangleShip<Character>("submarine", 
                                                                    new Coordinate(0, 0), 
                                                                    1, 2, 's', '*');
    assertEquals("submarine", ship1.getName());

    RectangleShip<Character> ship2 = new RectangleShip<Character>("destroyer", 
                                                                    new Coordinate(2, 3), 
                                                                    1, 3, 'd', '*');
    assertEquals("destroyer", ship2.getName());
  }

  @Test
  public void test_convenience_constructor() {
    RectangleShip<Character> ship = new RectangleShip<Character>("testship", 
                                                                   new Coordinate(5, 5), 
                                                                   's', '*');
    assertTrue(ship.occupiesCoordinates(new Coordinate(5, 5)));
    assertFalse(ship.occupiesCoordinates(new Coordinate(5, 6)));
    assertFalse(ship.occupiesCoordinates(new Coordinate(6, 5)));
  }

    @Test
  public void test_isSunk() {
    RectangleShip<Character> ship = new RectangleShip<Character>("testship",
                                                                   new Coordinate(1, 2),
                                                                   1, 3, 's', '*');
    assertFalse(ship.isSunk());
    ship.recordHitAt(new Coordinate(1, 2));
    assertFalse(ship.isSunk());
    ship.recordHitAt(new Coordinate(2, 2));
    assertFalse(ship.isSunk());
    ship.recordHitAt(new Coordinate(3, 2));
    assertTrue(ship.isSunk());
  }

  @Test
  public void test_recordHitAt_and_wasHitAt() {
    RectangleShip<Character> ship = new RectangleShip<Character>("testship",
                                                                   new Coordinate(0, 0),
                                                                   1, 2, 's', '*');
    
    assertFalse(ship.wasHitAt(new Coordinate(0, 0)));
    assertFalse(ship.wasHitAt(new Coordinate(1, 0)));

    ship.recordHitAt(new Coordinate(0, 0));
    assertTrue(ship.wasHitAt(new Coordinate(0, 0)));
    ship.recordHitAt(new Coordinate(1, 0));
    assertTrue(ship.wasHitAt(new Coordinate(0, 0)));
    assertTrue(ship.wasHitAt(new Coordinate(1, 0)));
  }

    @Test
  public void test_invalid_coordinate() {
    RectangleShip<Character> ship = new RectangleShip<Character>("testship",
                                                                   new Coordinate(1, 1),
                                                                   1, 2, 's', '*');
    assertThrows(IllegalArgumentException.class, 
                 () -> ship.recordHitAt(new Coordinate(0, 0)));
    
    assertThrows(IllegalArgumentException.class, 
                 () -> ship.wasHitAt(new Coordinate(5, 5)));
  }
  @Test
  public void test_getDisplayInfoAt() {
    RectangleShip<Character> ship = new RectangleShip<Character>("testship",
                                                                   new Coordinate(0, 0),
                                                                   1, 2, 's', '*');
    

    assertEquals('s', ship.getDisplayInfoAt(new Coordinate(0, 0)));
    ship.recordHitAt(new Coordinate(0, 0));
    assertEquals('*', ship.getDisplayInfoAt(new Coordinate(0, 0)));

    assertThrows(IllegalArgumentException.class,() -> ship.getDisplayInfoAt(new Coordinate(5, 5)));
  }

}