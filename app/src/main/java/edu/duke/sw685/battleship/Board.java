package edu.duke.sw685.battleship;

public interface Board<T> {
    public int getWidth();
    public int getHeight();
    //add ship to board and return true, but also can be false(fail to add)
    public String tryAddShip(Ship<T> toAdd);

    //to see where is this location, null or on one ship, but return the ship
    public Ship<T> fireAt(Coordinate c);
    public T whatIsAtForSelf(Coordinate where);
    public T whatIsAtForEnemy(Coordinate where);
    //to see if all ships on the board are sunk
    public boolean allShipsSunk();
    //remove a ship from the board
    public Ship<T> removeShip(Ship<T> ship);
    //find which ship occupies a given coordinate
    public Ship<T> getShipAt(Coordinate c);
}

