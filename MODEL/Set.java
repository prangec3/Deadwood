import java.util.*;
public class Set extends Location {
  private Card scene;
  private int shotCount;
  private boolean sceneIsVisible;
  private boolean wrappedUp = false;
  private String[] neighborMap;
  private Take[] takeList;
  private String name;
  private Part[] partList;
  // private int[] xyhw;

  Set(String n, String[] n2, int[] x){
    super(n, n2, x); // applies to Location
    // this.name = n;
    // this.neighborMap = n2;
    // this.xyhw = x;
  }

  public void printInfo(){
    System.out.printf("\n\n------Set name: %s-----------------------------\n", name);
    String[] x = {"x", "y", "h", "w"};
    for (int k = 0; k < 4; k++){
      System.out.printf("\t%s: %s", x[k], xyhw[k]);
    }
    System.out.println();
    for (int i = 0; i < neighborMap.length; i++){
      System.out.println("Neighbor: " + neighborMap[i]);
    }

    System.out.println("--------Takes---------");
    for (int i = 0; i < takeList.length; i++){
      takeList[i].printInfo();
    }
    System.out.println("----------Parts---------");
    for (int i = 0; i < partList.length; i++){
      partList[i].printInfo();
    }
  }

  public int getMaxTakes()
  {
    return takeList.length;
  }


  public boolean Scene()
  {
    if (sceneIsVisible)
    {
      return false;
    }
    else
    {
      scene.SeeCard(xyhw[0], xyhw[1]);
      sceneIsVisible = true;
      return true;
    }
  }

  public Take[] returnTakeArray(){
    return takeList;
  }

  public Card getScene()
  {
    return scene;
  }
  public void setScene(Card scene)
  {
    this.scene = scene;
    this.wrappedUp = false;
    this.sceneIsVisible = false;
  }

    public boolean getWrappedUp()
    {
      return this.wrappedUp;
    }

    public Role getRole(String wantedRole)
    {
      Role wanted = null;
      for(Part curr: partList)
      {
        if(curr.getName().equalsIgnoreCase(wantedRole))
          wanted = curr;
      }
      for(Role curr: scene.getRoles())
      {
        if(curr.getName().equalsIgnoreCase(wantedRole))
          wanted = curr;
      }
      return wanted;
    }

    public ArrayList<Part> getParts()
    {
      ArrayList<Part> r = new ArrayList<Part>();
      for (Part p : partList)
        r.add(p);
      return r;
    }



    public void decShotCount()
    {
      try
      {
        shotCount--;
        takeList[shotCount].hideTake();
        if(shotCount<=0)
        {
          scene.FlipCard();
          this.wrappedUp = true;
          this.wrapUp();
          shotCount = 0;
          broadcast(Board.wrapup_cmd);
        }
      }
      catch(Exception ex)
      {
        System.out.println("Exception [Set.decShotCount]: " + ex.getMessage());
      }
    }

    public void setShotCount(int val)
    {
      this.shotCount = val;
      for (int i = 0; i < val; ++i)
      {
        takeList[i].showTake();
      }
    }

    public int getShotCount()
    {
      return this.shotCount;
    }
    private void wrapUp()
    {
      try
      {
        if(this.scene.payOnCard())
        {
          this.payOffCard();
        }
        this.scene = null;
      }
      catch (Exception ex)
      {
        System.out.println("Exception [Set.wrapUp]: " + ex.getMessage());
      }
    }

    private void payOffCard()
    {
      try
      {
        for(Role curr: partList)
        {
          if(curr.getOccupant() != null)
          {
            int rank = curr.getRank();
            curr.getOccupant().addMoney(rank);
            curr.getOccupant().SetRole(null);
            curr.getOccupant().setRehearsals(0);
            curr.setOccupant(null);
          }
        }
      }
      catch (Exception ex)
      {
        System.out.println("Exception [Set.payOffCard]: " + ex.getMessage());
      }
    }

    public void setWrappedUp(boolean val)
    {
      this.wrappedUp = val;
    }


  public void addPartandTake(Part[] p, Take[] t){
    this.partList = p;
    this.takeList = t;
  }



}
