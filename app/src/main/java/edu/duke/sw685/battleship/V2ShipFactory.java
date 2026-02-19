package edu.duke.sw685.battleship;

public class V2ShipFactory implements AbstractShipFactory<Character> {
  protected Ship<Character> createRectShip(Placement where, int w, int h, char letter, String name) {
    char orientation = where.getOrientation();
    if (orientation == 'V') {
      return new RectangleShip<>(name, where.getWhere(), w, h, letter, '*');
    } else if (orientation == 'H') {
      return new RectangleShip<>(name, where.getWhere(), h, w, letter, '*');
    } else {
      throw new IllegalArgumentException("Invalid orientation for " + name + ": " + orientation + ". Must be H or V.");
    }
  }

  @Override
  public Ship<Character> makeSubmarine(Placement where) {
    return createRectShip(where, 1, 2, 's', "Submarine");
  }

  @Override
  public Ship<Character> makeDestroyer(Placement where) {
    return createRectShip(where, 1, 3, 'd', "Destroyer");
  }

  @Override
  public Ship<Character> makeBattleship(Placement where) {
    char orientation = where.getOrientation();
    int[][] offsets;
    switch (orientation) {
      case 'U':
        offsets = new int[][]{{0,1}, {1,0}, {1,1}, {1,2}};
        break;
      case 'R':
        offsets = new int[][]{{0,0}, {1,0}, {1,1}, {2,0}};
        break;
      case 'D':
        offsets = new int[][]{{0,0}, {0,1}, {0,2}, {1,1}};
        break;
      case 'L':
        offsets = new int[][]{{0,1}, {1,0}, {1,1}, {2,1}};
        break;
      default:
        throw new IllegalArgumentException("Invalid orientation for Battleship: " + orientation + ". Must be U, R, D, or L.");
    }
    return new NonRectangleShip<>("Battleship", where.getWhere(), offsets, 'b', '*');
  }
//some offset options
  @Override
  public Ship<Character> makeCarrier(Placement where) {
    char orientation = where.getOrientation();
    int[][] offsets;
    switch (orientation) {
      case 'U':
        offsets = new int[][]{{0,0}, {1,0}, {2,0}, {2,1}, {3,0}, {3,1}, {4,1}};
        break;
      case 'R':
        offsets = new int[][]{{0,1}, {0,2}, {0,3}, {0,4}, {1,0}, {1,1}, {1,2}};
        break;
      case 'D':
        offsets = new int[][]{{0,0}, {1,0}, {1,1}, {2,0}, {2,1}, {3,1}, {4,1}};
        break;
      case 'L':
        offsets = new int[][]{{0,1}, {0,2}, {0,3}, {1,0}, {1,1}, {1,2}, {1,3}};
        break;
      default:
        throw new IllegalArgumentException("Invalid orientation for Carrier: " + orientation + ". Must be U, R, D, or L.");
    }
    return new NonRectangleShip<>("Carrier", where.getWhere(), offsets, 'c', '*');
  }
}
