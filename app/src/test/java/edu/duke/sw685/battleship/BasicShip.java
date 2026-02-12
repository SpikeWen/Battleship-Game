package edu.duke.sw685.battleship;

/**
 * This is a placeholder implementation of Ship for testing purposes.
 * It represents a ship that occupies only one coordinate.
 * This will be replaced with a full implementation in Task 10.
 */
public class BasicShip implements Ship<Character> {
  private final Coordinate myLocation;
  public BasicShip(Coordinate where) {
    this.myLocation = where;
  }

  @Override
  public boolean occupiesCoordinates(Coordinate where) {
    return where.equals(myLocation);
  }

  @Override
  public boolean isSunk() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void recordHitAt(Coordinate where) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public boolean wasHitAt(Coordinate where) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public Character getDisplayInfoAt(Coordinate where) {
    return 's';
  }
}