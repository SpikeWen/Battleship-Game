package edu.duke.sw685.battleship;

/**
 * The Placement class represents where a ship is placed on the board,
 * including its starting coordinate and orientation.
 */
public class Placement {
  private final Coordinate where;
  private final char orientation;

  /**
   * Constructs a Placement with the specified coordinate and orientation
   * 
   * @param where is the starting coordinate of the placement
   * @param orientation is the orientation ('H' for horizontal, 'V' for vertical)
   */
  public Placement(Coordinate where, char orientation) {
    this.where = where;
    this.orientation = Character.toUpperCase(orientation);
  }

  /**
   * Constructs a Placement from a string descriptor like "A0V" or "B3H"
   * 
   * @param descr is the string description of the placement (e.g., "A0V", "B3H")
   * @throws IllegalArgumentException if the string is not a valid placement
   */
  public Placement(String descr) {
    if (descr == null || descr.length() != 3) {
      throw new IllegalArgumentException("Placement string must be exactly 3 characters, but is " + descr);
    }
    
    // Use Coordinate's constructor for the first two characters
    String coordString = descr.substring(0, 2);
    this.where = new Coordinate(coordString);
    
    // Get the orientation from the third character
    char orient = descr.charAt(2);
    this.orientation = Character.toUpperCase(orient);
    
    // Validate orientation (for now, only H and V are valid)
    if (this.orientation != 'H' && this.orientation != 'V') {
      throw new IllegalArgumentException("Orientation must be H or V, but is " + orient);
    }
  }

  public Coordinate getWhere() {
    return where;
  }

  public char getOrientation() {
    return orientation;
  }

  /**
   * Checks if this placement is equal to another object
   * 
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Placement p = (Placement) o;
      return where.equals(p.where) && orientation == p.orientation;
    }
    return false;
  }

  /**
   * Returns a string representation of this placement
   * 
   * @return a string in the format "coordinate+orientation"
   */
  @Override
  public String toString() {
    return where.toString() + orientation;
  }

  /**
   * Returns a hash code for this placement
   * 
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}