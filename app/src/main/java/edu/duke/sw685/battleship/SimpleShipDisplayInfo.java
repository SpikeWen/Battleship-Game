package edu.duke.sw685.battleship;

//one characterfor unhit positions or hit positions
public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {
  private T myData;
  private T onHit;

  /**
   * Constructs a SimpleShipDisplayInfo
   * 
   * @param myData is the info to display when not hit
   * @param onHit is the info to display when hit
   */
  public SimpleShipDisplayInfo(T myData, T onHit) {
    this.myData = myData;
    this.onHit = onHit;
  }

  @Override
  public T getInfo(Coordinate where, boolean hit) {
    if (hit) {
      return onHit;
    }
    return myData;
  }
}