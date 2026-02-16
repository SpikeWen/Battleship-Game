package edu.duke.sw685.battleship;
import java.util.ArrayList;
import java.util.HashSet;
public class BattleShipBoard<T> implements Board<T> {
    private final int width;
    private final int height;
    final ArrayList<Ship<T>> myShips;
    private final PlacementRuleChecker<T> placementChecker;
    final HashSet<Coordinate> enemyMisses;
    final T missInfo;

 /**
   * Constructs a BattleShipBoard with the specified width
   * and height
   * @param w is the width of the newly constructed board.
   * @param h is the height of the newly constructed board.
   * @param placementChecker is the rule checker for validating placements
   * @throws IllegalArgumentException if the width or height are less than or equal to zero.
   */
  //the default one
    public BattleShipBoard(int w, int h, T missInfo) {
     this(w, h, new NoCollisionRuleChecker<T>(new InBoundsRuleChecker<T>(null)), missInfo);
    }
    //the default one without missInfo,like X
    public BattleShipBoard(int w, int h) {
     this(w, h, new NoCollisionRuleChecker<T>(new InBoundsRuleChecker<T>(null)), null);
    }
    public BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementChecker, T missInfo) {
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
        this.enemyMisses = new HashSet<Coordinate>();
        this.missInfo = missInfo;
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
  
    public T whatIsAt(Coordinate where, boolean isSelf) {
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(where)) {
        return s.getDisplayInfoAt(where,isSelf);
      }
    }
    if(!isSelf && enemyMisses.contains(where)){
      return missInfo;
    }
    return null;
  }
//to see where is this location, null or on one ship, but return the ship
  @Override
  public Ship<T> fireAt(Coordinate c) {
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(c)) {
        s.recordHitAt(c);
        return s;
      }
    }
    enemyMisses.add(c);
    return null;
  }
  @Override
  public T whatIsAtForSelf(Coordinate where) {
    return whatIsAt(where, true);
  }

  @Override
  public T whatIsAtForEnemy(Coordinate where) {
    return whatIsAt(where, false);
  }


}
