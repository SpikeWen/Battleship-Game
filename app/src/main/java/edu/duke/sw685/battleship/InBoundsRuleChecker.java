package edu.duke.sw685.battleship;

//Checks that a ship is entirely within the bounds of the board
public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> {

  /**
   * Constructs an InBoundsRuleChecker
   * 
   * @param next is the next rule checker in the chain
   */
  public InBoundsRuleChecker(PlacementRuleChecker<T> next) {
    super(next);
  }

  /**
   * Check if the ship is entirely within the board bounds
   * 
   * @param theShip is the ship to check
   * @param theBoard is the board to check against
   * @return true if all coordinates are in bounds, false otherwise
   */
  @Override
  protected boolean checkMyRule(Ship<T> theShip, Board<T> theBoard) {
    for (Coordinate c : theShip.getCoordinates()) {
      if (c.getRow() < 0 || c.getRow() >= theBoard.getHeight() ||
          c.getColumn() < 0 || c.getColumn() >= theBoard.getWidth()) {
        return false;
      }
    }
    return true;
  }
}