package edu.duke.sw685.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.EOFException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

public class TextPlayer {
  final Board<Character> theBoard;
  final BoardTextView view;
  final BufferedReader inputReader;
  final PrintStream out;
  final AbstractShipFactory<Character> shipFactory;
  final String name;
  final ArrayList<String> shipsToPlace;
  final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;

  /**

   * @param name is the player's name (like "Zhangsan" or "Wangwu")
   * @param theBoard is the player's board
   * @param inputSource is where to read input from
   * @param out is where to print output to
   * @param shipFactory is the factory to create ships
   */
  public TextPlayer(String name, Board<Character> theBoard, BufferedReader inputSource, 
                    PrintStream out, AbstractShipFactory<Character> shipFactory) {
    this.name = name;
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.inputReader = inputSource;
    this.out = out;
    this.shipFactory = shipFactory;
    this.shipsToPlace = new ArrayList<String>();
    this.shipCreationFns = new HashMap<String, Function<Placement, Ship<Character>>>();
    setupShipCreationMap();
    setupShipCreationList();
  }
//read a coordinate from the user, with the given prompt
  public Coordinate readCoordinate(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    if (s == null) {
        throw new EOFException("End of input reached");
    }
    return new Coordinate(s);
}

public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
    out.print(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Player " + enemyName + "'s ocean"));
    out.println(); 
    // Get attack coordinate with error handling
    Coordinate attackCoord = null;
    while (attackCoord == null) {
        try {
            attackCoord = readCoordinate("Player " + name + " where do you want to fire at?");
            if (attackCoord.getRow() < 0 || attackCoord.getRow() >= enemyBoard.getHeight() ||
                attackCoord.getColumn() < 0 || attackCoord.getColumn() >= enemyBoard.getWidth()) {
                out.println("That coordinate is invalid: it does not have the correct format.");
                attackCoord = null;
            }
        } catch (IllegalArgumentException e) {
            out.println("That coordinate is invalid: it does not have the correct format.");
            attackCoord = null;
        }
    }
    Ship<Character> hitShip = enemyBoard.fireAt(attackCoord);
    // Report result
    if (hitShip == null) {
        out.println("Bro! You missed!");
    } else {
        out.println("OMG! You hit a " + hitShip.getName() + "!");
    }
}


  //Sets up the map from ship names to creation functions
  protected void setupShipCreationMap() {
    shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
  }

  //create a certain number of the ships and add it to the ArrayList
  protected void setupShipCreationList() {
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }

  /**
   * Reads a placement from the user
   * 
   * @param prompt is the prompt to display to the user
   * @return the Placement entered by the user
   * @throws IOException if there is an error reading input
   */
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    if(s==null){
      throw new EOFException("End of input reached");
    }
    return new Placement(s);
  }

  /**
   * Does one placement: reads a placement, creates a ship, adds it to the board,
   * and displays the board
   * @param shipName is the name of the ship to place
   * @param createFn is the function to create the ship
   * @throws IOException if there is an error reading input
   */
public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
    while (true) {
        try {
            Placement p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?");
            Ship<Character> s = createFn.apply(p);
            String result = theBoard.tryAddShip(s);
            if (result == null) {
                out.print(view.displayMyOwnBoard());
                return;
            } else {
                out.println(result);
            }
        } catch (IllegalArgumentException e) {
            out.println("That placement is invalid: " + e.getMessage());
        }
    }
}

  /**
   * Performs the placement phase for this player
   * 
   * @throws IOException if there is an error reading input
   */
  public void doPlacementPhase() throws IOException {
    // Display the empty board
    out.print(view.displayMyOwnBoard());
    
    // Print instructions
    out.print("Player " + name + ": you are going to place the following ships (which are all\n");
    out.print("rectangular). For each ship, type the coordinate of the upper left\n");
    out.print("side of the ship, followed by either H (for horizontal) or V (for\n");
    out.print("vertical).  For example M4H would place a ship horizontally starting\n");
    out.print("at M4 and going to the right.  You have\n");
    out.print("\n");
    out.print("2 \"Submarines\" ships that are 1x2\n");
    out.print("3 \"Destroyers\" that are 1x3\n");
    out.print("3 \"Battleships\" that are 1x4\n");
    out.print("2 \"Carriers\" that are 1x6\n");
    out.print("\n");
    
    // Place all ships
    for (String shipName : shipsToPlace) {
      doOnePlacement(shipName, shipCreationFns.get(shipName));
    }
  }
}