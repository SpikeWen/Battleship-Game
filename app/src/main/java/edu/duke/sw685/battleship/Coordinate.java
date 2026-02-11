package edu.duke.sw685.battleship;


public class Coordinate {
  private final int row;
  private final int column;

  /**
   * Constructs a Coordinate with the specified row and column
   * 
   * @param row is the row number
   * @param column is the column number
   */
  public Coordinate(int row, int column) {
    this.row = row;
    this.column = column;
  }

  /**
   * Constructs a Coordinate from a string descriptor
   * 
   * @param descr is a string which takes in a string like "A2" and makes the Coordinate that corresponds to that string (e.g. row=0, column =2)
   * @throws IllegalArgumentException if the string is not a valid coordinate
   */
  public Coordinate(String descr) {
    if (descr == null || descr.length() != 2) {
      throw new IllegalArgumentException("Coordinate string must be exactly 2 characters, but is " + descr);
    }
    
    String upper = descr.toUpperCase();
    char rowLetter = upper.charAt(0);
    char colChar = upper.charAt(1);
    
    // Check if row letter is valid (A-Z)
    if (rowLetter < 'A' || rowLetter > 'Z') {
      throw new IllegalArgumentException("Row must be a letter between A and Z, but is " + rowLetter);
    }
    
    // Check if column character is a digit (0-9)
    if (colChar < '0' || colChar > '9') {
      throw new IllegalArgumentException("Column must be a digit between 0 and 9, but is " + colChar);
    }
    
    this.row = rowLetter - 'A';
    this.column = colChar - '0';
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  /**
   * Checks if this coordinate is equal to another object
   * 
   * @param o the object to compare with
   * @return true if the objects are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Coordinate c = (Coordinate) o;
      return row == c.row && column == c.column;
    }
    return false;
  }

  /**
   * Returns a string representation of this coordinate
   * @return a string in the format "(row, column)"
   */
  @Override
  public String toString() {
    return "(" + row + ", " + column + ")";
  }

  /**
   * Returns a hash code for this coordinate
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}