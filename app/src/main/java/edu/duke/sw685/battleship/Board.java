package edu.duke.sw685.battleship;

public interface Board<T> {
    public int getWidth();
    public int getHeight();
    //add ship to board and return true, but also can be false(fail to add)
    public String tryAddShip(Ship<T> toAdd);
    //to see where is this location, null or on one ship
    public T whatIsAt(Coordinate where);
}

