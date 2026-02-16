package edu.duke.sw685.battleship;

import java.util.HashMap;
public abstract class BasicShip<T> implements Ship<T> {
  protected HashMap<Coordinate, Boolean> myPieces;
  protected ShipDisplayInfo<T> myDisplayInfo;
  protected ShipDisplayInfo<T> enemyDisplayInfo;
  /**
   * Constructs a BasicShip with the given coordinates
   * 
   * @param where is an Iterable of coordinates the ship occupies
   * @param myDisplayInfo is the display information for this ship
   * @param enemyDisplayInfo is the display information for this ship when it is viewed by the enemy
   */
  public BasicShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
    this.myPieces = new HashMap<Coordinate, Boolean>();
    this.myDisplayInfo = myDisplayInfo;
    this.enemyDisplayInfo = enemyDisplayInfo;
    for (Coordinate c : where) {
      this.myPieces.put(c, false);
    }
  }

  @Override
  public boolean occupiesCoordinates(Coordinate where) {
    return myPieces.containsKey(where);
  }

  @Override
  public boolean isSunk() {
    for (Boolean hit : myPieces.values()) {
      if (!hit) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void recordHitAt(Coordinate where) {
    checkCoordinateInThisShip(where);
    myPieces.put(where, true);
  }

  @Override
  public boolean wasHitAt(Coordinate where) {
    checkCoordinateInThisShip(where);
    return myPieces.get(where);
  }

  /**
   * Check if a coordinate is part of this ship
   * 
   * @param c is the coordinate to check
   * @throws IllegalArgumentException if c is not part of this ship
   */
  protected void checkCoordinateInThisShip(Coordinate c) {
    if (!myPieces.containsKey(c)) {
      throw new IllegalArgumentException("Coordinate " + c + " is not part of this ship");
    }
  }

  @Override
  public T getDisplayInfoAt(Coordinate where, boolean myShip) {
    checkCoordinateInThisShip(where);
    // TODO: this needs to look up the hit status
    if(myShip){
      return myDisplayInfo.getInfo(where, wasHitAt(where));
    }
    else{
      return enemyDisplayInfo.getInfo(where, wasHitAt(where));
    }
  }

  @Override
  public Iterable<Coordinate> getCoordinates() {
    return myPieces.keySet();
}
}