package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class InBoundsRuleCheckerTest {

  @Test
  public void test_in_bounds_vertical() {
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker);

    // Valid placements - should return true
    Ship<Character> sub1 = factory.makeSubmarine(new Placement("A0V"));
    assertTrue(checker.checkMyRule(sub1, board));
    Ship<Character> sub2 = factory.makeCarrier(new Placement("A0H"));
    assertTrue(checker.checkMyRule(sub2, board));

    Ship<Character> dst1 = factory.makeDestroyer(new Placement("B5V"));
    assertTrue(checker.checkMyRule(dst1, board));
    Ship<Character> dst2 = factory.makeCarrier(new Placement("B1H"));
    assertTrue(checker.checkMyRule(dst2, board));

    Ship<Character> car1 = factory.makeCarrier(new Placement("A0V"));
    assertTrue(checker.checkMyRule(car1, board));
    Ship<Character> car2 = factory.makeCarrier(new Placement("A0H"));
    assertTrue(checker.checkMyRule(car2, board));
    // Valid at the edge
    Ship<Character> sub3 = factory.makeSubmarine(new Placement("S9V"));
    assertTrue(checker.checkMyRule(sub3, board));
  }


  @Test
  public void test_out_of_bounds_right() {
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker);

    // Ship goes off right edge
    Ship<Character> sub = factory.makeSubmarine(new Placement("A9H"));
    assertFalse(checker.checkMyRule(sub, board));

    Ship<Character> dst = factory.makeDestroyer(new Placement("B8H"));
    assertFalse(checker.checkMyRule(dst, board));
  }
  @Test
  public void test_check_PlacementCallsChain() {
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<>(null);
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker);

    Ship<Character> sub = factory.makeSubmarine(new Placement("A0V"));
    assertTrue(checker.checkPlacement(sub, board));
    Ship<Character> dst = factory.makeDestroyer(new Placement("T0V"));
    assertFalse(checker.checkPlacement(dst, board));
  }
}