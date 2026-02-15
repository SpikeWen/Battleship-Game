package edu.duke.sw685.battleship;

// if the ship's coordinates collision with the other ships
public class NoCollisionRuleChecker<T> extends PlacementRuleChecker<T> {

  public NoCollisionRuleChecker(PlacementRuleChecker<T> next) {
    super(next);
  }

//rule specification
  @Override
  protected boolean checkMyRule(Ship<T> theShip, Board<T> theBoard) {
    for (Coordinate c : theShip.getCoordinates()) {
      if (theBoard.whatIsAt(c) != null) {
        return false;
      }
    }
    return true;
  }
}