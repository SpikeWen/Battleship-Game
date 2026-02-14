package edu.duke.sw685.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;


public class App {
  final Board<Character> theBoard;
  final BoardTextView view;
  final BufferedReader inputReader;
  final PrintStream out;

  /**
   * Constructor
   * @param theBoard is the board to use
   * @param inputSource is where to read input from
   * @param out is where to print output to
   */
  public App(Board<Character> theBoard, Reader inputSource, PrintStream out) {
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.inputReader = new BufferedReader(inputSource);
    this.out = out;
  }

  /**
   * Reads a placement from the input(user)
   * @param prompt is the prompt to display to the user
   * @return the Placement entered by the user
   */
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    return new Placement(s);
  }

  /**
   * Does one placement: reads a placement, creates a ship, adds it to the board,
   * and displays the board
   */
  public void doOnePlacement() throws IOException {
    Placement p = readPlacement("Where would you like to put your ship?");
    Ship<Character> s = new BasicShip(p.getWhere());
    theBoard.tryAddShip(s);
    out.print(view.displayMyOwnBoard());
  }

// entry of the program
  
  public static void main(String[] args) throws IOException {
    Board<Character> b = new BattleShipBoard<Character>(10, 20);
    App app = new App(b, new java.io.InputStreamReader(System.in), System.out);
    app.doOnePlacement();
  }
}