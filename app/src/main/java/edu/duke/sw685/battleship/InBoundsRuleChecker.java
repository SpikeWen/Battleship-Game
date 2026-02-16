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
  protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
    for (Coordinate c : theShip.getCoordinates()) {
      if (c.getRow() < 0 )
        {
            return "That placement is invalid: the ship goes off the top of the board.";
        }
      if (c.getColumn() < 0)
        {
            return "That placement is invalid: the ship goes off the left of the board.";
        }
      if (c.getRow() >= theBoard.getHeight())
        {
            return "That placement is invalid: the ship goes off the bottom of the board.";
        }
      if (c.getColumn() >= theBoard.getWidth())
        {
            return "That placement is invalid: the ship goes off the right of the board.";
        }
    }
    return null;
  }
}