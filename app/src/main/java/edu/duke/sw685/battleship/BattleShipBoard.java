package edu.duke.sw685.battleship;
import java.util.ArrayList;
public class BattleShipBoard<T> implements Board<T> {
    private final int width;
    private final int height;
    final ArrayList<Ship<T>> myShips;
    private final PlacementRuleChecker<T> placementChecker;

 /**
   * Constructs a BattleShipBoard with the specified width
   * and height
   * @param w is the width of the newly constructed board.
   * @param h is the height of the newly constructed board.
   * @param placementChecker is the rule checker for validating placements
   * @throws IllegalArgumentException if the width or height are less than or equal to zero.
   */
  //the default one
    public BattleShipBoard(int w, int h) {
     this(w, h, new NoCollisionRuleChecker<T>(new InBoundsRuleChecker<T>(null)));
    }

    public BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementChecker) {
          if (w <= 0) {
          throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + w);
    }
          if (h <= 0) {
          throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + h);
    }
        this.width = w;
        this.height = h;
        this.myShips = new ArrayList<Ship<T>>();
        this.placementChecker = placementChecker;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String tryAddShip(Ship<T> toAdd) {
        String result = placementChecker.checkPlacement(toAdd, this);
        if (result != null) {
            return result;
        }
        myShips.add(toAdd);
        return null;

  
  }
  
    public T whatIsAt(Coordinate where) {
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(where)) {
        return s.getDisplayInfoAt(where);
      }
    }
    return null;
  }
}
