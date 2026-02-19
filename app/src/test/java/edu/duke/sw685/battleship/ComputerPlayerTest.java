package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class ComputerPlayerTest {

  private ComputerPlayer createComputer(int w, int h, ByteArrayOutputStream bytes) {
    PrintStream out = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
    V2ShipFactory factory = new V2ShipFactory();
    return new ComputerPlayer("Bot", board, out, factory);
  }

  @Test
  void test_doPlacementPhase_placesAllShips() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ComputerPlayer bot = createComputer(10, 20, bytes);
    bot.doPlacementPhase();
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(0, 0)));
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(0, 2)));
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(3, 0)));
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(7, 0)));
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(9, 0)));
    // No output
    assertEquals("", bytes.toString());
  }

  @Test
  void test_playOneTurn_firesSequentially() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ComputerPlayer bot = createComputer(10, 20, bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 3, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
//fire at the coordinates in order: A0, A1, A2, B0, B1, B2, C0, C1, C2
    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertTrue(bytes.toString().contains("Bot missed at A0"));
    bytes.reset();


    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertTrue(bytes.toString().contains("Bot missed at A1"));
    bytes.reset();


  
    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertTrue(bytes.toString().contains("Bot missed at A2"));
    bytes.reset();
    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertTrue(bytes.toString().contains("Bot missed at B0"));
  }

  @Test
  void test_playOneTurn_reportsHit() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ComputerPlayer bot = createComputer(10, 20, bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    V2ShipFactory factory = new V2ShipFactory();
    enemyBoard.tryAddShip(factory.makeSubmarine(new Placement("A0V")));
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertTrue(bytes.toString().contains("Bot hit your Submarine at A0!"));
  }

  @Test
  void test_playOneTurn_exhausted() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ComputerPlayer bot = createComputer(10, 20, bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(2, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    // Fire all 4 place on board
    bot.playOneTurn(enemyBoard, enemyView, "P");
    bot.playOneTurn(enemyBoard, enemyView, "P");
    bot.playOneTurn(enemyBoard, enemyView, "P");
    bot.playOneTurn(enemyBoard, enemyView, "P");
    bytes.reset();
    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertEquals("", bytes.toString());
  }
}
