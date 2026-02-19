package edu.duke.sw685.battleship;

import java.util.function.Function;
public class BoardTextView {
  private final Board<Character> toDisplay;

  public BoardTextView(Board<Character> toDisplay) {
    this.toDisplay = toDisplay;
    if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
      throw new IllegalArgumentException(
          "Board must be no larger than 10x26, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
    }
  }

  protected String makeHeader() {
    StringBuilder ans = new StringBuilder("  ");
    String sep = "";
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

  /**
   * Display this board's "my own board" on the left and enemy's board on the right
   * 
   * @param enemyView is the enemy's board view
   * @param myHeader is the header for my board
   * @param enemyHeader is the header for enemy's board
   * @return a string with both boards side by side
   */
public String displayMyBoardWithEnemyNextToIt(BoardTextView enemyView, String myHeader, String enemyHeader) {
    StringBuilder result = new StringBuilder();
    String myBoard = displayMyOwnBoard();
    String enemyBoard = enemyView.displayEnemyBoard();
    // Get lines from mine and enemy's
    String[] myLines = myBoard.split("\n");
    String[] enemyLines = enemyBoard.split("\n");
    // Have proper column positions
    int width = toDisplay.getWidth();
    int myHeaderCol = 5;
    int enemyHeaderCol = 2 * width + 22; 
    //for header
    result.append(makeSpaces(myHeaderCol)).append(myHeader);  
    result.append(makeSpaces(enemyHeaderCol)).append(enemyHeader).append("\n");
    // board side by side
    int enemyBoardCol = 2 * width + 19; 
    for (int i = 0; i < myLines.length; i++) {
      result.append(myLines[i]);
      result.append(makeSpaces(enemyBoardCol - myLines[i].length()));
      result.append(enemyLines[i]);
      result.append("\n");
    }
    
    return result.toString();
}
 //helper to make some spaces
  private String makeSpaces(int count) {
    StringBuilder spaces = new StringBuilder();
    for (int i = 0; i < count; i++) {
      spaces.append(" ");
    }
    return spaces.toString();
  }
}