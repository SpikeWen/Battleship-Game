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
    // Sub1 at A0V → (0,0) and (1,0)
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(0, 0)));
    // Sub2 at A2V → (0,2) and (1,2)
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(0, 2)));
    // Des1 at D0V → (3,0),(4,0),(5,0)
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(3, 0)));
    // Battle1 at H0D: Down offsets (0,0),(0,1),(0,2),(1,1) from row7,col0
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(7, 0)));
    // Carrier1 at J0U: Up offsets (0,0),(1,0),(2,0),(2,1),(3,0),(3,1),(4,1) from row9,col0
    assertNotNull(bot.theBoard.getShipAt(new Coordinate(9, 0)));
    // No output during placement
    assertEquals("", bytes.toString());
  }

  @Test
  void test_playOneTurn_firesSequentially() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    ComputerPlayer bot = createComputer(10, 20, bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 3, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);

    // First turn: fires at A0
    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertTrue(bytes.toString().contains("Bot missed at A0"));
    bytes.reset();

    // Second turn: fires at A1
    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertTrue(bytes.toString().contains("Bot missed at A1"));
    bytes.reset();

    // Third turn: fires at A2 (last col on 3-wide board)
    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertTrue(bytes.toString().contains("Bot missed at A2"));
    bytes.reset();

    // Fourth turn: wraps to B0
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

    // Fire all 4 squares (2x2 board)
    bot.playOneTurn(enemyBoard, enemyView, "P");
    bot.playOneTurn(enemyBoard, enemyView, "P");
    bot.playOneTurn(enemyBoard, enemyView, "P");
    bot.playOneTurn(enemyBoard, enemyView, "P");
    bytes.reset();

    // 5th turn: all squares exhausted, nothing printed
    bot.playOneTurn(enemyBoard, enemyView, "P");
    assertEquals("", bytes.toString());
  }
}
