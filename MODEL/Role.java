// ToDo: Implementation, functions
import java.util.*;
public class Role {
  private int rank;
  private String name;
  private String description;
  private boolean isOnCard;
  private Player occupant;
  private String line;
  private int[] xyhw = new int[4];

  /* constructor*/
  public Role(int r, String n, String l, int[] x){
    this.rank = r;
    this.name = n;
    this.line = l;
    this.description = l;
    this.xyhw = x;

  }

  public void printInfo(){
    System.out.printf("\t\tRank: %d\t\tName: %s\n\t\tLine: %s\n",this.rank, this.name, this.line);
    System.out.printf("\t\t\t\tX: %s Y: %s h: %s w: %s\n", xyhw[0], xyhw[1], xyhw[2], xyhw[3]);

  }

  /*returns the roles' rank */
  public int getRank(){
    return this.rank;
  }
  /*returns the roles' name */
  public String getName(){
    return this.name;
  }
  /*returns the roles' description */
  public String getDescription(){
    return this.description;
  }
  /*returns whether or not the role is on a card */
  public boolean OnCard(){
    return this.isOnCard;
  }
  /*returns the roles' occupant */
  public Player getOccupant(){
    return this.occupant;
  }

  public boolean getIsOnCard()
  {
    return this.isOnCard;
  }

  public void setOnCard()
  {
    this.isOnCard = true;
  }

  public void setOccupant(Player player)
  {
    this.occupant = player;
  }
  public int[] GetXYHW()
  {
    return xyhw;
  }
}
