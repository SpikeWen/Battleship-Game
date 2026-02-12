package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlacementTest {

  @Test
  public void test_where_and_orientation() {
    Coordinate c1 = new Coordinate(1, 2);
    Placement p1 = new Placement(c1, 'H');  
    Coordinate c2 = new Coordinate(5, 8);
    Placement p2 = new Placement(c2, 'V');
    assertEquals(c1, p1.getWhere());
    assertEquals('H', p1.getOrientation());
    assertEquals(c2, p2.getWhere());
    assertEquals('V', p2.getOrientation());
  }

  @Test
  public void test_case_insensitivity() {
    Coordinate c1 = new Coordinate(1, 2);
    Coordinate c2 = new Coordinate(1, 2);
    Placement p3 = new Placement(c1, 'h');
    Placement p4 = new Placement(c2, 'H');
    Placement p1 = new Placement(c1, 'v');
    Placement p2 = new Placement(c2, 'V');
    
    assertEquals('V', p1.getOrientation()); 
    assertEquals('V', p2.getOrientation());
    assertEquals(p1, p2); 
    assertEquals('H', p3.getOrientation());
    assertEquals('H', p4.getOrientation());
    assertEquals(p3, p4);
  }

  @Test
  public void test_equals() {
    Coordinate c1 = new Coordinate(1, 2);
    Coordinate c2 = new Coordinate(1, 2);
    Coordinate c3 = new Coordinate(3, 4);
    
    Placement p1 = new Placement(c1, 'H');
    Placement p2 = new Placement(c2, 'H');
    Placement p3 = new Placement(c1, 'V');
    Placement p4 = new Placement(c3, 'H');
    
    assertEquals(p1, p1); 
    assertEquals(p1, p2); 
    assertNotEquals(p1, p3); 
    assertNotEquals(p1, p4); 
    assertNotEquals(p1, "(1, 2)H"); 
  }

  @Test
  public void test_toString() {
    Coordinate c1 = new Coordinate(1, 2);
    Placement p1 = new Placement(c1, 'H');
    assertEquals("(1, 2)H", p1.toString());
    
    Coordinate c2 = new Coordinate(5, 8);
    Placement p2 = new Placement(c2, 'V');
    assertEquals("(5, 8)V", p2.toString());
  }

  @Test
  public void test_hashCode() {
    Coordinate c1 = new Coordinate(1, 2);
    Coordinate c2 = new Coordinate(1, 2);
    Coordinate c3 = new Coordinate(3, 4);
    
    Placement p1 = new Placement(c1, 'H');
    Placement p2 = new Placement(c2, 'H');
    Placement p3 = new Placement(c1, 'V');
    Placement p4 = new Placement(c3, 'H');
    
    assertEquals(p1.hashCode(), p2.hashCode()); 
    assertNotEquals(p1.hashCode(), p3.hashCode()); 
    assertNotEquals(p1.hashCode(), p4.hashCode()); 
  }

  @Test
  public void test_string_constructor_valid_cases() {
    Placement p1 = new Placement("A0H");
    assertEquals(new Coordinate(0, 0), p1.getWhere());
    assertEquals('H', p1.getOrientation());
    
    Placement p2 = new Placement("B3V");
    assertEquals(new Coordinate(1, 3), p2.getWhere());
    assertEquals('V', p2.getOrientation());
    
    Placement p3 = new Placement("D5h");  
    assertEquals(new Coordinate(3, 5), p3.getWhere());
    assertEquals('H', p3.getOrientation());
    
    Placement p4 = new Placement("z9v");  
    assertEquals(new Coordinate(25, 9), p4.getWhere());
    assertEquals('V', p4.getOrientation());
  }

  @Test
  public void test_string_constructor_error_cases() {
    // shoeter or longer length
    assertThrows(IllegalArgumentException.class, () -> new Placement("A0"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A0HV")); 
    // wrong coordinate part
    assertThrows(IllegalArgumentException.class, () -> new Placement("00H"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("AAV"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("@0H"));
    // wrong orientation
    assertThrows(IllegalArgumentException.class, () -> new Placement("A0X"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A0A"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A01"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A0 "));
  }
}