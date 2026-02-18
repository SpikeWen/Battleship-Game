package edu.duke.sw685.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.EOFException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
  int moveRemaining;
  int sonarRemaining;

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
    this.moveRemaining = 3;
    this.sonarRemaining = 3;
    setupShipCreationMap();
    setupShipCreationList();
  }

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

    // If no special actions left, go straight to fire
    if (moveRemaining <= 0 && sonarRemaining <= 0) {
      doFire(enemyBoard);
      return;
    }

    // Show action menu and get choice
    while (true) {
      out.println("Possible actions for Player " + name + ":\n");
      out.println(" F Fire at a square");
      if (moveRemaining > 0) {
        out.println(" M Move a ship to another square (" + moveRemaining + " remaining)");
      }
      if (sonarRemaining > 0) {
        out.println(" S Sonar scan (" + sonarRemaining + " remaining)");
      }
      out.println("\nPlayer " + name + ", what would you like to do?");

      String choice = inputReader.readLine();
      if (choice == null) {
        throw new EOFException("End of input reached");
      }
      choice = choice.trim().toUpperCase();

      if (choice.equals("F")) {
        doFire(enemyBoard);
        return;
      } else if (choice.equals("M") && moveRemaining > 0) {
        if (doMove()) {
          return;
        }
        // If move failed, re-display board and re-prompt
        out.print(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", "Player " + enemyName + "'s ocean"));
        out.println();
      } else if (choice.equals("S") && sonarRemaining > 0) {
        doSonar(enemyBoard);
        return;
      } else {
        out.println("Invalid choice, please try again.");
      }
    }
  }

  /**
   * Fire at enemy board (extracted from old playOneTurn)
   */
  protected void doFire(Board<Character> enemyBoard) throws IOException {
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
    if (hitShip == null) {
      out.println("Bro! You missed!");
    } else {
      out.println("OMG! You hit a " + hitShip.getName() + "!");
    }
  }

  /**
   * Move a ship to a new location, preserving damage at relative positions.
   * Returns true if move succeeded, false if it failed (invalid selection).
   */
  protected boolean doMove() throws IOException {
    // Prompt for which ship to move
    Coordinate shipCoord;
    try {
      shipCoord = readCoordinate("Player " + name + ", which ship do you want to move?");
    } catch (IllegalArgumentException e) {
      out.println("That coordinate is invalid.");
      return false;
    }
    Ship<Character> oldShip = theBoard.getShipAt(shipCoord);
    if (oldShip == null) {
      out.println("There is no ship at that coordinate.");
      return false;
    }

    // Get old ship's coordinates sorted and hit status
    ArrayList<Coordinate> oldCoords = new ArrayList<>();
    for (Coordinate c : oldShip.getCoordinates()) {
      oldCoords.add(c);
    }
    Collections.sort(oldCoords, (a, b) -> {
      if (a.getRow() != b.getRow()) return a.getRow() - b.getRow();
      return a.getColumn() - b.getColumn();
    });
    ArrayList<Boolean> hitStatus = new ArrayList<>();
    for (Coordinate c : oldCoords) {
      hitStatus.add(oldShip.wasHitAt(c));
    }

    // Prompt for new placement
    Placement newPlacement;
    try {
      newPlacement = readPlacement("Player " + name + ", where do you want to move it to?");
    } catch (IllegalArgumentException e) {
      out.println("That placement is invalid: " + e.getMessage());
      return false;
    }

    // Create new ship
    String shipName = oldShip.getName();
    Ship<Character> newShip;
    try {
      newShip = shipCreationFns.get(shipName).apply(newPlacement);
    } catch (IllegalArgumentException e) {
      out.println("That placement is invalid: " + e.getMessage());
      return false;
    }

    // Remove old ship, try to add new ship
    theBoard.removeShip(oldShip);
    String addResult = theBoard.tryAddShip(newShip);
    if (addResult != null) {
      // Failed — put old ship back
      theBoard.tryAddShip(oldShip);
      out.println(addResult);
      return false;
    }

    // Restore damage at same relative positions
    ArrayList<Coordinate> newCoords = new ArrayList<>();
    for (Coordinate c : newShip.getCoordinates()) {
      newCoords.add(c);
    }
    Collections.sort(newCoords, (a, b) -> {
      if (a.getRow() != b.getRow()) return a.getRow() - b.getRow();
      return a.getColumn() - b.getColumn();
    });
    for (int i = 0; i < hitStatus.size(); i++) {
      if (hitStatus.get(i)) {
        newShip.recordHitAt(newCoords.get(i));
      }
    }

    moveRemaining--;
    return true;
  }

  /**
   * Perform a sonar scan on the enemy board
   */
  protected void doSonar(Board<Character> enemyBoard) throws IOException {
    Coordinate center;
    try {
      center = readCoordinate("Player " + name + ", where do you want to scan?");
    } catch (IllegalArgumentException e) {
      out.println("That coordinate is invalid.");
      sonarRemaining--;
      return;
    }

    // Count ships in diamond pattern (Manhattan distance <= 3)
    LinkedHashMap<String, Integer> counts = new LinkedHashMap<>();
    counts.put("Submarines", 0);
    counts.put("Destroyers", 0);
    counts.put("Battleships", 0);
    counts.put("Carriers", 0);

    for (int dr = -3; dr <= 3; dr++) {
      for (int dc = -(3 - Math.abs(dr)); dc <= (3 - Math.abs(dr)); dc++) {
        int r = center.getRow() + dr;
        int c = center.getColumn() + dc;
        if (r < 0 || r >= enemyBoard.getHeight() || c < 0 || c >= enemyBoard.getWidth()) {
          continue;
        }
        Ship<Character> ship = enemyBoard.getShipAt(new Coordinate(r, c));
        if (ship != null) {
          String key = ship.getName() + "s";
          counts.put(key, counts.getOrDefault(key, 0) + 1);
        }
      }
    }

    for (String shipType : counts.keySet()) {
      int count = counts.get(shipType);
      if (count == 1) {
        out.println(shipType + " occupy " + count + " square");
      } else {
        out.println(shipType + " occupy " + count + " squares");
      }
    }
    sonarRemaining--;
  }

  protected void setupShipCreationMap() {
    shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
  }

  protected void setupShipCreationList() {
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }

  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    if(s==null){
      throw new EOFException("End of input reached");
    }
    return new Placement(s);
  }

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

  public void doPlacementPhase() throws IOException {
    out.print(view.displayMyOwnBoard());

    out.print("Player " + name + ": you are going to place the following ships.\n");
    out.print("For submarines and destroyers, type the coordinate of the upper left\n");
    out.print("side of the ship, followed by H (horizontal) or V (vertical).\n");
    out.print("For battleships and carriers, use U (up), R (right), D (down), or L (left).\n");
    out.print("The coordinate names the upper-left corner of the bounding rectangle.\n");
    out.print("\n");
    out.print("2 \"Submarines\" ships that are 1x2\n");
    out.print("3 \"Destroyers\" that are 1x3\n");
    out.print("3 \"Battleships\" that have a special shape (use U/R/D/L)\n");
    out.print("2 \"Carriers\" that have a special shape (use U/R/D/L)\n");
    out.print("\n");

    for (String shipName : shipsToPlace) {
      doOnePlacement(shipName, shipCreationFns.get(shipName));
    }
  }
}
