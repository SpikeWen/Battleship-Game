package edu.duke.sw685.battleship;
import java.util.function.Function;
/**
 * This class handles textual display of
 * a Board (i.e., converting it to a string to show
 * to the user).
 * It supports two ways to display the Board:
 * one for the player's own board, and one for the 
 * enemy's board.
 */
public class BoardTextView {
  private final Board<Character> toDisplay;
  /**
   * Constructs a BoardView, given the board it will display.
   * 
   * @param toDisplay is the Board to display
   * @throws IllegalArgumentException if the board is larger than 10x26.  
   */
  public BoardTextView(Board<Character> toDisplay) {
    this.toDisplay = toDisplay;
    if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
      throw new IllegalArgumentException(
          "Board must be no larger than 10x26, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
    }
  }

  /**
   * For numer header line, like 0|1|2|3|4\n
   * @return the String that is the header line for the given board
   */
  protected String makeHeader() {
    StringBuilder ans = new StringBuilder("  "); // README shows two spaces at start
    String sep = ""; // start with nothing to separate, then switch to | to separate
    for (int i = 0; i < toDisplay.getWidth(); i++) {
      ans.append(sep);
      ans.append(i);
      sep = "|";
    }
    ans.append("\n");
    return ans.toString();
  }

  protected String displayAnyBoard(Function<Coordinate, Character> getSquareFn) {
    StringBuilder ans = new StringBuilder();
    ans.append(makeHeader());
    for (int row = 0; row < toDisplay.getHeight(); row++) {
      char rowLetter = (char) ('A' + row);
      ans.append(rowLetter);
      ans.append(" ");
      for (int col = 0; col < toDisplay.getWidth(); col++) {
        if (col > 0) {
          ans.append("|");
        }
        Coordinate c = new Coordinate(row, col);
        Character displayChar = getSquareFn.apply(c);
        if (displayChar == null) {
          ans.append(" ");
        } else {
          ans.append(displayChar);
        }
      }
      ans.append(" ");
      ans.append(rowLetter);
      ans.append("\n");
    }
    ans.append(makeHeader());
    return ans.toString();
  }

  public String displayMyOwnBoard() {
    return displayAnyBoard((c) -> toDisplay.whatIsAtForSelf(c));
  }

  public String displayEnemyBoard() {
    return displayAnyBoard((c) -> toDisplay.whatIsAtForEnemy(c));
  }
}