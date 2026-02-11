package edu.duke.sw685.battleship;

public class BattleShipBoard {
    private final int width;
    private final int height;

    public BattleShipBoard(int width, int height) {
          if (width <= 0) {
          throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + width);
    }
          if (height <= 0) {
          throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + height);
    }
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
