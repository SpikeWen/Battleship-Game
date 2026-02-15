package edu.duke.sw685.battleship;

public abstract class PlacementRuleChecker<T> {
  private final PlacementRuleChecker<T> next;

  public PlacementRuleChecker(PlacementRuleChecker<T> next) {
    this.next = next;
  }

 //to check if this specific rule is well-implemented, true means well-implemented, false means not well-implemented
  protected abstract boolean checkMyRule(Ship<T> theShip, Board<T> theBoard);

  /**
   * Check if a placement is valid according to this rule and all subsequent rules
   * 
   * @param theShip is the ship to check
   * @param theBoard is the board to check against
   * @return true if all rules are satisfied, false otherwise
   */
  public boolean checkPlacement (Ship<T> theShip, Board<T> theBoard) {
    //if we fail our own rule: stop the placement is not legal
    if (!checkMyRule(theShip, theBoard)) {
      return false;
    }
    //other wise, ask the rest of the chain.
    if (next != null) {
      return next.checkPlacement(theShip, theBoard); 
    }
    //if there are no more rules, then the placement is legal
    return true;
  }

}