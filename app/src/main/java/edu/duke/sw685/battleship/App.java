package edu.duke.sw685.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

//entry of the game,create teo player
public class App {
  private final TextPlayer player1;
  private final TextPlayer player2;
  private final PrintStream out;

  /**
   * Constructs an App with two players
   * 
   * @param player1 is the first player
   * @param player2 is the second player
   */
  public App(TextPlayer player1, TextPlayer player2) {
    this.player1 = player1;
    this.player2 = player2;
    this.out = player1.out;
  }

  /**
   * Performs the placement phase for both players
   * 
   * @throws IOException if there is an error reading input
   */
  public void doPlacementPhase() throws IOException {
    player1.doPlacementPhase();
    player2.doPlacementPhase();
  }

   //Perform the attacking phase until one player loses
  public void doAttackingPhase() throws IOException {
    while (true) {
      // Player 1's turn
      player1.playOneTurn(player2.theBoard, player2.view, player2.name);
      if (player2.theBoard.allShipsSunk()) {
        out.println("Player " + player1.name + " won the game!");
        return;
      }
      // Player 2's turn
      player2.playOneTurn(player1.theBoard, player1.view, player1.name);
      if (player1.theBoard.allShipsSunk()) {
        out.println("Player " + player2.name + " won the game!");
        return;
      }
    }
  }




  /**
   * Main entry point for the application
   * 
   * @param args command line arguments 
   * @throws IOException if there is an error reading input
   */
  public static void main(String[] args) throws IOException {
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20);
    Board<Character> b2 = new BattleShipBoard<Character>(10, 20);
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    PrintStream out = System.out;
    V1ShipFactory factory = new V1ShipFactory();
    
    TextPlayer player1 = new TextPlayer("A", b1, input, System.out, factory);
    TextPlayer player2 = new TextPlayer("B", b2, input, System.out, factory);
    
    App app = new App(player1, player2);
    app.doPlacementPhase();
    app.doAttackingPhase();
}
}