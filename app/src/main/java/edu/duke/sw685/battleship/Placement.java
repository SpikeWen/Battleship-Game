package edu.duke.sw685.battleship;

public class Placement {
  private final Coordinate where;
  private final char orientation;

  /**
   * Constructs a Placement with coordinate and orientation
   * @param where is the starting coordinate of the placement
   * @param orientation is the orientation ('H'orizontal, 'V'ertical)
   */
  public Placement(Coordinate where, char orientation) {
    this.where = where;
    this.orientation = Character.toUpperCase(orientation);
  }

  /**
   * More specific input: Placement ,like "A0V" or "B3H"
   * @param descr is the string description of the placement 
   */
  public Placement(String descr) {
    if (descr == null || descr.length() != 3) {
      throw new IllegalArgumentException("Placement string must be exactly 3 characters, but is " + descr);
    }
    
  //to get coordiante
    String coordString = descr.substring(0, 2);
    this.where = new Coordinate(coordString);
    
    //to get orientation
    char orient = descr.charAt(2);
    this.orientation = Character.toUpperCase(orient);
    
    // Validate orientation
    if (this.orientation != 'H' && this.orientation != 'V' &&
        this.orientation != 'U' && this.orientation != 'R' &&
        this.orientation != 'D' && this.orientation != 'L') {
      throw new IllegalArgumentException("Orientation must be one of H, V, U, R, D, L, but is " + orient);
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
   * @return a string in the format "coordinate+orientation"
   */
  @Override
  public String toString() {
    return where.toString() + orientation;
  }

  /**
   * Returns a hash code for this placement
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}