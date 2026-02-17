package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NonRectangleShipTest {

  @Test
  void test_getName() {
    int[][] offsets = {{0,0}, {1,0}, {1,1}};
    NonRectangleShip<Character> ship = new NonRectangleShip<>("TestShip", new Coordinate(0, 0), offsets, 't', '*');
    assertEquals("TestShip", ship.getName());
  }

  @Test
  void test_makeCoords() {
    int[][] offsets = {{0,1}, {1,0}, {1,1}, {1,2}};
    NonRectangleShip<Character> ship = new NonRectangleShip<>("Battleship", new Coordinate(2, 3), offsets, 'b', '*');
    // Should occupy (2,4), (3,3), (3,4), (3,5)
    assertTrue(ship.occupiesCoordinates(new Coordinate(2, 4)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(3, 3)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(3, 4)));
    assertTrue(ship.occupiesCoordinates(new Coordinate(3, 5)));
    assertFalse(ship.occupiesCoordinates(new Coordinate(2, 3)));
    assertFalse(ship.occupiesCoordinates(new Coordinate(0, 0)));
  }

  @Test
  void test_hit_and_display() {
    int[][] offsets = {{0,0}, {0,1}, {1,0}};
    NonRectangleShip<Character> ship = new NonRectangleShip<>("Test", new Coordinate(0, 0), offsets, 't', '*');
    // Not hit yet
    assertFalse(ship.isSunk());
    assertEquals('t', ship.getDisplayInfoAt(new Coordinate(0, 0), true));
    // Enemy sees null when not hit
    assertNull(ship.getDisplayInfoAt(new Coordinate(0, 0), false));
    // Hit one
    ship.recordHitAt(new Coordinate(0, 0));
    assertTrue(ship.wasHitAt(new Coordinate(0, 0)));
    assertEquals('*', ship.getDisplayInfoAt(new Coordinate(0, 0), true));
    // Enemy sees 't' when hit
    assertEquals('t', ship.getDisplayInfoAt(new Coordinate(0, 0), false));
    assertFalse(ship.isSunk());
    // Hit all
    ship.recordHitAt(new Coordinate(0, 1));
    ship.recordHitAt(new Coordinate(1, 0));
    assertTrue(ship.isSunk());
  }
}
