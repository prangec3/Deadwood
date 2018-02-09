import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class Card {
  private ArrayList<Role> roles;
  private int budget;
  private String name;
  private String description;
  private String img;
  private int sceneNumber;
  public JPanel cardJPanel;
  private JLabel cardJLabel;


  public Card(String name,int budget, String img, String description, int sceneNumber, ArrayList<Role> roles){
    this.roles = roles;
    for (Role r : roles)
    {
      r.setOnCard();
    }
    this.budget = budget;
    this.name = name;
    this.sceneNumber = sceneNumber;
    this.description = description;
    this.img = img;
    this.cardJPanel = new JPanel();
    this.cardJPanel.setSize(205,115);
    this.cardJPanel.setLocation(20,459);
    this.cardJPanel.setOpaque(false);
    this.cardJPanel.setVisible(false);
    try{
    cardJLabel =  new JLabel(new ImageIcon(ImageIO.read(new File("Board/cards/" + this.img))));
    this.cardJLabel.setBounds(0,0,205,115);
    this.cardJPanel.add(this.cardJLabel);
    } catch(Exception ex)
    {
      System.out.printf("Exception [Card.Constructor]: %s\n", ex.getMessage());
      ex.printStackTrace();
    }
  }

  public Card(){}

  public void SeeCard(int x, int y)
  {
    try
    {
      cardJPanel.setLocation(x,y);
      cardJLabel.setIcon(new ImageIcon(ImageIO.read(new File("Board/cards/" + this.img))));
      cardJPanel.setVisible(true);
    }
    catch(Exception ex)
    {
      System.out.printf("Exception [Card.SeeCard]: %s\n", ex.getMessage());
      ex.printStackTrace();
    }
  }

  public void HideCard()
  {
    cardJPanel.setVisible(false);
    // try
    // {
    //   cardJLabel.setIcon(new ImageIcon(ImageIO.read(new File("Board/cards/" + this.img))));
    //   cardJPanel.setVisible(false);
    // }
    // catch (Exception ex)
    // {
    //   System.out.printf("Exception [Card.HideCard]: %s\n", ex.getMessage());
    //   ex.printStackTrace();
    // }
  }

  public void FlipCard()
  {
    try
    {
      cardJLabel.setIcon(new ImageIcon(ImageIO.read(new File("Board/cards/back.png"))));
    }

    catch(Exception ex)
    {
      System.out.printf("Exception [Card.FlipCard]: %s\n", ex.getMessage());
      ex.printStackTrace();
    }
  }
  public boolean payOnCard()
  {
    try
    {

      Dice die = new Dice();
      ArrayList<Player> payroll = new ArrayList<Player>();
      for(int i = roles.size()-1; i>=0; i--)//Goes through the list of roles backwards to grab their actors. Backwards because role ranks are in ascending order.
      {
        Player hold = this.roles.get(i).getOccupant();//Gets the occupant of the current scene.
        if(hold != null)
        {
          payroll.add(hold);
        }
      }
      if(payroll.isEmpty())//No players on the card.
      {
        return false;
      }
      else
      {
        int[] money = die.Rolls(this.budget);
        int place = 0;//Tracks number of dice distributed.
        int hold = 0;//Tracks player being paid.
        while(place<this.budget)
        {
          payroll.get(hold).addMoney(money[place]);
          place++;
          hold = (hold+1)%payroll.size();//Rolls over to position 0 if the end of the list is reached.
        }
        for(Role curr: roles)
        {
          if(curr.getOccupant()!=null)
          {
            curr.getOccupant().SetRole(null);
            curr.getOccupant().setRehearsals(0);
          }
          curr.setOccupant(null);
        }
        return true;
      }
    }
    catch (Exception ex)
    {
      System.out.println("Exception: [Card.payOnCard]: " + ex.getMessage());
      return false;
    }
  }


  public int getBudget()
  {
    return budget;
  }
  public void printInfo(){
    System.out.printf("\nCard Name: %s\timg: %s\n", this.name, this.img);
    System.out.printf("Scene number: %d\nDescription: %s\n", this.sceneNumber, this.description);

    for (int i = 0; i < roles.size(); i++){
      Role node = (Role)roles.get(i);
      node.printInfo();
}

  }

  public String GetName()
  {
    return name;
  }
  public void PayoutOnCard(){}

  public ArrayList<Role> getRoles()
  {
    return roles;
  }
}
