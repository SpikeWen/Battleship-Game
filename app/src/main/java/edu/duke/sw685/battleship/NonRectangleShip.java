package edu.duke.sw685.battleship;

import java.util.HashSet;

public class NonRectangleShip<T> extends BasicShip<T> {
  private final String name;

  // Generate coordinates from an upper-left corner and a set of row/col offsets
  static HashSet<Coordinate> makeCoords(Coordinate upperLeft, int[][] offsets) {
    HashSet<Coordinate> coords = new HashSet<>();
    for (int[] offset : offsets) {
      coords.add(new Coordinate(upperLeft.getRow() + offset[0], upperLeft.getColumn() + offset[1]));
    }
    return coords;
  }

  public NonRectangleShip(String name, Coordinate upperLeft, int[][] offsets, T data, T onHit) {
    super(makeCoords(upperLeft, offsets),
          new SimpleShipDisplayInfo<T>(data, onHit),
          new SimpleShipDisplayInfo<T>(null, data));
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }
}
