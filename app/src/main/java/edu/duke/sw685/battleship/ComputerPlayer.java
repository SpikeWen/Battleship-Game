package edu.duke.sw685.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

public class ComputerPlayer extends TextPlayer {
  private int nextFireRow;
  private int nextFireCol;

  public ComputerPlayer(String name, Board<Character> theBoard, PrintStream out,AbstractShipFactory<Character> shipFactory) {
    // Computer doesn't read user input, pass a dummy reader
    super(name, theBoard, new BufferedReader(new StringReader("")), out, shipFactory);
    this.nextFireRow = 0;
    this.nextFireCol = 0;
  }

  @Override
  public void doPlacementPhase() throws IOException {
//10 ships on the specific place for computer
    String[] placements = {"A0V", "A2V","D0V", "D2V", "D4V","H0D", "H3D", "H6D", "J0U", "J3U"};
    int i = 0;
    for (String shipName : shipsToPlace) {
      Placement p = new Placement(placements[i]);
      Ship<Character> ship = shipCreationFns.get(shipName).apply(p);
      theBoard.tryAddShip(ship);
      i++;
    }
  }

  @Override
  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
    // Fire sequentially through all squares, row by row
    while (nextFireRow < enemyBoard.getHeight()) {
      Coordinate c = new Coordinate(nextFireRow, nextFireCol);
      // Advance position for next turn
      nextFireCol++;
      if (nextFireCol >= enemyBoard.getWidth()) {
        nextFireCol = 0;
        nextFireRow++;
      }
      Ship<Character> hitShip = enemyBoard.fireAt(c);
      char rowLetter = (char) ('A' + c.getRow());
      if (hitShip != null) {
        out.println("Player " + name + " hit your " + hitShip.getName()+ " at " + rowLetter + c.getColumn() + "!");
      } else {
        out.println("Player " + name + " missed at " + rowLetter + c.getColumn() + "!");
      }
      return;
    }
  }
}
