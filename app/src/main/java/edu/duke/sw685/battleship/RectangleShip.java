package edu.duke.sw685.battleship;

import java.util.HashSet;

// A  rectangular region ship
public class RectangleShip<T> extends BasicShip<T> {
  private final String name;

  /**
   * Generate the set of coordinates for a rectangle
   * 
   * @param upperLeft is the upper left corner of the rectangle
   * @param width is the width of the rectangle
   * @param height is the height of the rectangle
   * @return a HashSet of all coordinates in the rectangle
   */
  static HashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height) {
    HashSet<Coordinate> coords = new HashSet<Coordinate>();
    for (int row = upperLeft.getRow(); row < upperLeft.getRow() + height; row++) {
      for (int col = upperLeft.getColumn(); col < upperLeft.getColumn() + width; col++) {
        coords.add(new Coordinate(row, col));
      }
    }
    return coords;
  }

  /**
   * Constructs a RectangleShip
   * 
   * @param name is the name of this ship
   * @param upperLeft is the upper left coordinate
   * @param width is the width
   * @param height is the height
   * @param myDisplayInfo is the display information
   */
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, 
                       ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
    super(makeCoords(upperLeft, width, height), myDisplayInfo, enemyDisplayInfo);
    this.name = name;
  }

  /**
   * Constructs a RectangleShip with simple display info
   * 
   * @param name is the name of this ship
   * @param upperLeft is the upper left coordinate
   * @param width is the width
   * @param height is the height
   * @param data is the character to display when not hit
   * @param onHit is the character to display when hit
   */
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, 
                       T data, T onHit) {
    this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit),new SimpleShipDisplayInfo<T>(null, data));
  }

  /**
   * Convenience constructor for a 1x1 ship
   * 
   * @param name is the name of this ship
   * @param upperLeft is the coordinate
   * @param data is the character to display when not hit
   * @param onHit is the character to display when hit
   */
  /* 
  public RectangleShip( Coordinate upperLeft, T data, T onHit) {
    this("testship", upperLeft, 1, 1, data, onHit);
  }*/

  public RectangleShip(String name, Coordinate upperLeft, T data, T onHit) {
    this(name, upperLeft, 1, 1, data, onHit);
}
  /**
   * Get the name of this ship
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }
}