package edu.duke.sw685.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class TextPlayer {
  final Board<Character> theBoard;
  final BoardTextView view;
  final BufferedReader inputReader;
  final PrintStream out;
  final AbstractShipFactory<Character> shipFactory;
  final String name;

  /**
   * Constructs a TextPlayer
   * 
   * @param name is the player's name 
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
    return new Placement(s);
  }

  /**
   * Does one placement: reads a placement, creates a ship, adds it to the board,
   * and displays the board
   * 
   * @throws IOException if there is an error reading input
   */
  public void doOnePlacement() throws IOException {
    Placement p = readPlacement("Player " + name + " where do you want to place a Destroyer?");
    Ship<Character> s = shipFactory.makeDestroyer(p);
    theBoard.tryAddShip(s);
    out.print(view.displayMyOwnBoard());
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
    
    // Place one ship for now
    doOnePlacement();
  }
}