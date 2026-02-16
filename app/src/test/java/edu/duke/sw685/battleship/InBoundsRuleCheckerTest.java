package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class InBoundsRuleCheckerTest {
  @Test
  public void test_in_boundsValid() {
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker, 'X'); 
    Ship<Character> sub1 = factory.makeSubmarine(new Placement("A0V"));
    assertNull(checker.checkMyRule(sub1, board));   
    Ship<Character> dst1 = factory.makeDestroyer(new Placement("B5V"));
    assertNull(checker.checkMyRule(dst1, board));
  }

  @Test
  public void test_outOfBoundsRight() {
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker, 'X'); 

    Ship<Character> sub = factory.makeSubmarine(new Placement("A9H"));
    String result = checker.checkMyRule(sub, board);
    assertEquals("That placement is invalid: the ship goes off the right of the board.", result);
  }

  @Test
  public void test_outOfBoundsBottom() {
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker, 'X');  

    Ship<Character> car = factory.makeCarrier(new Placement("P0V"));
    String result = checker.checkMyRule(car, board);
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", result);
  }

  @Test
  public void test_outOfBoundsTop() {
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker, 'X'); 
    
    // Create a ship with negative row
    Ship<Character> ship = new RectangleShip<Character>("testship", 
                                                          new Coordinate(-1, 0), 
                                                          1, 2, 's', '*');
    String result = checker.checkMyRule(ship, board);
    assertEquals("That placement is invalid: the ship goes off the top of the board.", result);
  }

  @Test
  public void test_out_of_bounds_left() {
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker, 'X');  
    Ship<Character> ship = new RectangleShip<Character>("testship", 
                                                          new Coordinate(0, -1), 
                                                          1, 2, 's', '*');
    String result = checker.checkMyRule(ship, board);
    assertEquals("That placement is invalid: the ship goes off the left of the board.", result);
  }
}