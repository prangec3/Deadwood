// ToDo: Implementation
import java.util.*;
public class Location extends Observer {
  protected String name = "";
  protected String[] neighborList = {""};
  protected int[] xyhw = new int[4];

  Location(String n)
  {
    this.name = n;
  }

  Location(String n, String[] nl, int[] x){
    this.name = n;
    this.neighborList = nl;
    this.xyhw = x;
  }

  public int[] GetXYHW() { return xyhw; }
  public String[] GetAdjacentLocationNames()
  {
    return neighborList;
  }

  public boolean isAdjacent(Location room) {
     for (String name : neighborList)
     {
       if (room.GetName().toLowerCase().equals(name.toLowerCase()))
       {
         return true;
       }
     }
     return false;
   }

  // Should be Board.java logic
  //public Location GetLocationIfAdjacent(String room) { return null; }
  public Card getCard()
  {
    return null;
  }


  public String GetName()
  {
    return name;
  }
}
