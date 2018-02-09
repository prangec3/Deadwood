// ToDo: Implementation
import java.util.*;
public class Player {
  private static final int[] upgradePriceMoney = {4, 10, 18, 28, 40};//2, 3, 4, 5, 6
  private static final int[] upgradePriceCr = {5, 10, 15, 20, 25};//2, 3, 4, 5, 6
  private static int uniqueIDcounter = 0;
  private int uniqueID;
  private int rank;
  private int money;
  private int fame;
  private int rehearsals;
  private Role currentRole;
  private Location currentLocation;
  private boolean hasMoved;
  private String name = "TestName";
  private boolean hasActed = false;


  public Player()//Patron Saint of debugging.
  {
    this.name = "Randy Rando";
    this.rank = 6;
    this.money = 1000;
    this.fame = 1000;
    this.rehearsals = 6;
    this.currentRole = null;
    this.currentLocation = null;
    this.hasMoved = false;
    uniqueID = uniqueIDcounter++;
  }
  public Player(String name, Location trailer)
  {
    this.name = name;
    this.rank = 1;
    this.money = 0;
    this.fame = 0;
    this.rehearsals = 0;
    this.currentRole = null;
    this.currentLocation = trailer;
    this.hasMoved = false;
    uniqueID = uniqueIDcounter++;
  }
  public boolean Move(Location location) {
    // Already Preapproved location (is adjacent from Board)
    if (location == null)
    {
      return false;
    }
    if(this.CanMove())
    {
      Location wantToMoveTo = location;
      if(wantToMoveTo != null)
      {
        this.currentLocation = wantToMoveTo;
        this.rehearsals = 0;  //makes sure rehearsals get reset when a player goes to a new carde
        hasMoved = true;
        //System.out.println("Successful move to "+wantToMoveTo.GetName());
        // Moved to Board.MovePlayer()
        return true;
      }
    }

    //System.out.println("You can't move because you are acting, you have moved this turn, or you requested an invalid location.");
    // Moved to Board.MovePlayer()
    return false;

  }

  public int GetID(){ return this.uniqueID; }

  public boolean Upgrade(String moneyOrCR, int level)
  {
    switch(moneyOrCR)
    {
      case "$":
      case "money":
        return upgradeMoney(level);
      case "cr":
        return upgradeCredits(level);

      default:
        return false;
    }
  }

  public Role GetRole() { return currentRole; }

  public String GetRoleName()
  {
    if (currentRole != null)
      return this.currentRole.getName();
    else
      return "";
  }

  public String GetRoleDescription()
  {
    if (currentRole != null)
      return this.currentRole.getDescription();
    else
      return "";
  }

  public String GetLocationName()
  {
    if (currentLocation != null)
      return currentLocation.GetName();
    else
      return "Nowhere";
  }
  private boolean upgradeMoney(int rank)
  {
    if(this.rank >= rank)
    {
      //System.out.println("Your rank is greater than or equal to the rank you are requesting.");
      return false;
    }
    else if(rank>6)
    {
      //System.out.println("You have requested a rank higher than the maximum rank of 6.");
      return false;
    }
    else
    {
      int price = upgradePriceMoney[rank-2];
      if(this.money>=price)
      {
        this.money -= price;
        this.rank = rank;
        //System.out.println("Successfully set rank to "+rank);
        return true;
      }
      else
      {
        //System.out.println("You don't have enough money for this upgrade.");
        //System.out.println(price+" dollars are needed. You have "+this.money);
        return false;
      }
    }
  }

  private boolean upgradeCredits(int rank)
  {
    if(this.rank >= rank)
    {
      //System.out.println("Your rank is greater than or equal to the rank you are requesting.");
      return false;
    }
    else if(rank>6)
    {
      //System.out.println("You have requested a rank higher than the maximum rank of 6.");
      return false;
    }
    else
    {
      int price = upgradePriceCr[rank-2];//Hopefully won't crash.
      if(this.fame>=price)
      {
        this.fame -= price;
        this.rank = rank;
        //System.out.println("Successfully set rank to "+rank);
        return true;
      }
      else
      {
        //System.out.println("You don't have enough credits for this upgrade.");
        //System.out.println(price+" dollars are needed. You have "+this.rank);
        return false;
      }
    }
  }


  public boolean TakeRole(String role)
  {
    try
    {
      if(role!=null && this.currentLocation instanceof Set)//The internet told me that instanceof is bad but what can you do?
      {
        Set currSet = (Set) this.currentLocation;
        if(!currSet.getWrappedUp())
        {
           Role wantedRole = currSet.getRole(role);
          if(wantedRole==null)
          {
            return false;//Wanted role wasn't there.
          }
          else if(wantedRole.getOccupant()!=null)
          {
            return false;//Wanted role is occupied
          }
          else if(wantedRole.getRank()>this.rank)
          {
            return false;//Wanted role is a higher rank than player.
          }
          else
          {
            wantedRole.setOccupant(this);//We should be fine.
            this.currentRole = wantedRole;
            return true;
          }
        }
      }
      else
      {
        return false;
      }
      return false;
    }
    catch (Exception ex)
    {
      System.out.printf("Exception [Player.TakeRole]: " + ex.getMessage() + "\n\n");
      System.out.printf("Got : \"%s\"\n",role);
      return false;
    }
  }

  public void SetRole(Role role)//Should only ever be null.
  {
    this.currentRole = role;
  }


  public boolean Act()
  {

    try
    {
      Dice die = new Dice();
      hasActed = true;
      if(this.currentLocation instanceof Set && this.currentRole != null)
      {

        Set currentSet = (Set) this.currentLocation;

        int roll = die.Roll();
        //System.out.println("You rolled a "+roll);

        if(roll+this.rehearsals >= currentSet.getScene().getBudget())
        {
          //Successful roll
          if(this.currentRole.getIsOnCard())//Role is on card. +2$
          {
            this.fame+=2;
          }
          else//Good roll, off card. +1 fame, +1 dollar.
          {
            this.money++;
            this.fame++;
          }
          currentSet.decShotCount();
          return true;
        }
        else//Bad roll
        {
          if(!this.currentRole.getIsOnCard())
          {
            this.money+=1;
          }
          return false;
        }
      }
      else
      {
        return false;
      }
    }
    catch (Exception ex)
    {
      System.out.printf("Exception [Player.Act]: " + ex.getMessage() + "\n\n");
      return false;
    }

    // here
  }


public boolean rehearse()
{
  if (hasActed)
    return false;


  if(this.currentLocation instanceof Set && this.CanAct())
  {
    Set currSet = (Set) this.currentLocation;
    // Current_Rehearsal is 4
    // Budget is 5
    //

    if(currSet.getScene().getBudget() == this.rehearsals+1)
    {
      return false;//Player has enough rehearsals and MUST act.
    }
    else
    {
      rehearsals++;//Player rehearsed.
      hasActed = true;
      return true;
    }
  }
  else
  {
    return false;//Player not acting and not on a set.
  }
}

  public int CalcScore()
  {
    return money + fame + (rank*5);
  }

  public boolean CanMove()
  {
    return !hasMoved && (currentRole == null) && !hasActed;
  }
  public boolean CanAct()
  {
    return (currentRole != null) && (!hasActed) && (!hasMoved);
  }
  public void SendToTrailer(Location trailer)
  {
    this.currentLocation = trailer;
  }

  public void SignalTurn()
  {
    this.hasMoved = false;
    this.hasActed = false;
  }

  public int GetMoney()
  {
    return money;
  }

  public void addMoney(int money)
  {
    this.money += money;
  }

  public int GetFame()
  {
    return fame;
  }


public String GetName(){
  return name;
}


public Location GetLocation(){
  return currentLocation;
}

public void setRehearsals(int val)
{
  this.rehearsals = val;
}

public int getRank(){
  return this.rank;
}

public boolean HasRole()
{
  return this.currentRole != null;
}

public boolean IsMainRole()
{
  return currentRole.OnCard();
}

public int GetRehearsal()
{
  return this.rehearsals;
}
}
