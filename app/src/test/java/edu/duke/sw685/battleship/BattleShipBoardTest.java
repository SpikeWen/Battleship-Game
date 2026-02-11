package edu.duke.sw685.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
    @Test
    public void test_constructor() {
        BattleShipBoard b = new BattleShipBoard(10, 20);
        assertEquals(10, b.getWidth());
        assertEquals(20, b.getHeight());
    }

    @Test
    public void test_constructor_invalid() {
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(10, 0));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(0, 20));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(10, -5));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard(-8, 20));
    }
}
