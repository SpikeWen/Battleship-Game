package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SimpleShipDisplayInfoTest {
  @Test
  public void test_getInfo() {
    SimpleShipDisplayInfo<Character> info = new SimpleShipDisplayInfo<Character>('s', '*');
    
    Coordinate c1 = new Coordinate(0, 0);
    Coordinate c2 = new Coordinate(3, 5);
    assertEquals('s', info.getInfo(c1, false));
    assertEquals('*', info.getInfo(c1, true));
    assertEquals('s', info.getInfo(c2, false));
    assertEquals('*', info.getInfo(c2, true));
  }

  @Test
  public void test_getInfo_with_string() {
    SimpleShipDisplayInfo<String> info = new SimpleShipDisplayInfo<String>("unhit_part", "hit_part");
    Coordinate c = new Coordinate(1, 2);
    assertEquals("unhit_part", info.getInfo(c, false));
    assertEquals("hit_part", info.getInfo(c, true));
  }
}