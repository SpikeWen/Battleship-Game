package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NoCollisionRuleCheckerTest {

  @Test
  public void test_no_collision() {
    NoCollisionRuleChecker<Character> checker = new NoCollisionRuleChecker<>(null);
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker);
    Ship<Character> sub1 = factory.makeSubmarine(new Placement("A0V"));
    board.tryAddShip(sub1);
    Ship<Character> dst1 = factory.makeDestroyer(new Placement("C3H"));
    assertNull(checker.checkMyRule(dst1, board));

  }

  @Test
  public void test_collision() {
    NoCollisionRuleChecker<Character> checker = new NoCollisionRuleChecker<>(null);
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, checker);
    Ship<Character> dst1 = factory.makeDestroyer(new Placement("A0H"));
    board.tryAddShip(dst1);

    Ship<Character> sub1 = factory.makeSubmarine(new Placement("A0H"));
    String result = checker.checkMyRule(sub1, board);
    assertNotNull(result);
    assertEquals("That placement is invalid: the ship overlaps another ship.", result);
    Ship<Character> sub2 = factory.makeSubmarine(new Placement("A0V"));
    assertNotNull(checker.checkMyRule(sub2, board));
    assertEquals("That placement is invalid: the ship overlaps another ship.", checker.checkMyRule(sub2, board));
  }



  @Test
  public void test_CombinedRulesValid() {
    // Create chain: NoCollision -> InBounds -> null
    InBoundsRuleChecker<Character> inBounds = new InBoundsRuleChecker<>(null);
    NoCollisionRuleChecker<Character> noCollision = new NoCollisionRuleChecker<>(inBounds);
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, noCollision);
    Ship<Character> sub1 = factory.makeSubmarine(new Placement("A0V"));
    board.tryAddShip(sub1);
    Ship<Character> dst1 = factory.makeDestroyer(new Placement("C3V"));
    assertNull(noCollision.checkPlacement(dst1, board));
  }


  @Test
  public void test_combined_rules_multiple_ships() {
    // Create chain: NoCollision -> InBounds -> null
    InBoundsRuleChecker<Character> inBounds = new InBoundsRuleChecker<>(null);
    NoCollisionRuleChecker<Character> noCollision = new NoCollisionRuleChecker<>(inBounds);
    
    V1ShipFactory factory = new V1ShipFactory();
    Board<Character> board = new BattleShipBoard<Character>(10, 20, noCollision);


    Ship<Character> sub1 = factory.makeSubmarine(new Placement("A0V"));
    board.tryAddShip(sub1);
    Ship<Character> dst1 = factory.makeDestroyer(new Placement("C3H"));
    board.tryAddShip(dst1);
    Ship<Character> bat1 = factory.makeBattleship(new Placement("E5V"));
    board.tryAddShip(bat1);

    Ship<Character> car1 = factory.makeCarrier(new Placement("A7V"));
    assertNull(noCollision.checkPlacement(car1, board));
//collisions
    Ship<Character> car2 = factory.makeCarrier(new Placement("A0H"));
    assertNotNull(noCollision.checkPlacement(car2, board));
    assertEquals("That placement is invalid: the ship overlaps another ship.", noCollision.checkPlacement(car2, board));
    Ship<Character> sub2 = factory.makeSubmarine(new Placement("C4V"));
    assertNotNull(noCollision.checkPlacement(sub2, board));
    assertEquals("That placement is invalid: the ship overlaps another ship.", noCollision.checkPlacement(sub2, board));    
    Ship<Character> sub3 = factory.makeSubmarine(new Placement("F5V"));
    assertNotNull(noCollision.checkPlacement(sub3, board));
    assertEquals("That placement is invalid: the ship overlaps another ship.", noCollision.checkPlacement(sub3, board));
  }
}