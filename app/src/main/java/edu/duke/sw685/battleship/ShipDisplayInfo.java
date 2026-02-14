package edu.duke.sw685.battleship;

//information for displaying a ship
public interface ShipDisplayInfo<T> {
  /**
   * Gets the display information for a given coordinate and hit status
   * @param where is the coordinate to get info for
   * @param hit is whether this coordinate has been hit
   * @return the display information
   */
  public T getInfo(Coordinate where, boolean hit);
}