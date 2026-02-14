package edu.duke.sw685.battleship;

/**
 * Factory for creating Version 1 ships (all rectangular)
 */
public class V1ShipFactory implements AbstractShipFactory<Character> {

  /**
   * Helper method to create a ship with the given parameters
   * 
   * @param where is the placement (location and orientation)
   * @param w is the width
   * @param h is the height
   * @param letter is the letter to display for the ship
   * @param name is the name of the ship
   * @return the symbol shape of this ship
   */
  protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name) {
    char orientation = where.getOrientation();
    if (orientation == 'V') {
      return new RectangleShip<Character>(name, where.getWhere(), w, h, letter, '*');
    } else if (orientation == 'H') {
      return new RectangleShip<Character>(name, where.getWhere(), h, w, letter, '*');
    } else {
      throw new IllegalArgumentException("Invalid orientation: " + orientation);
    }
  }

  @Override
  public Ship<Character> makeSubmarine(Placement where) {
    return createShip(where, 1, 2, 's', "Submarine");
  }
  @Override
  public Ship<Character> makeBattleship(Placement where) {
    return createShip(where, 1, 4, 'b', "Battleship");
  }
  @Override
  public Ship<Character> makeCarrier(Placement where) {
    return createShip(where, 1, 6, 'c', "Carrier");
  }
  @Override
  public Ship<Character> makeDestroyer(Placement where) {
    return createShip(where, 1, 3, 'd', "Destroyer");
  }
}