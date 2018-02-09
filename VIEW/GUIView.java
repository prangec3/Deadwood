import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

public class GUIView extends View
{
  GUIFrame gui;
  public GUIView(Board model)
  {
    gui = new GUIFrame(model);
  }

  public void Update(String str)
  {
    gui.Update(str);
  }
  public String GetUserInput()
  {
    return gui.GetUserInput();
  }
  public void Run()
  {
    gui.Run();
  }
  public void SignalDone()
  {
    gui.SignalDone();
  }

  public void changed(String str)
  {
    switch(str)
    {
      case Board.quit_cmd:
        SignalDone();

      break;
      case Board.init_cmd:
        GameInit();
      break;

      default:
        Update(str);
    }
  }

  public void GameInit()
  {
    gui.GameInit();
  }


  private class GUIFrame extends JFrame
  {
    boolean alreadyInit = false;
    boolean debug = false;
    Board model;
    JPanel mainPanel, boardPanel, playerInfoPanel;
    ArrayList<JLabel> Jplayers = new ArrayList<JLabel>();
    JTextArea textfield, playerinfoTextArea;
    JTextField userInput;
    JLabel userLabel;
    JLabel playerInfoLabel;
    JButton submit, b_who, b_where, b_act, b_help;
    JButton b_end, b_two, b_three, b_rehearse, b_randy, b_endday, b_endgame;
    JButton b_upgrade, b_move, b_work;
    JScrollPane scroll, playerInfoScroll;

    private final int mainWidth = 1600;
    private final int mainHeight = 1080;

    private void setupBackgroundByJLabel()
    {
      try
      {

        JLabel picLabel = new JLabel(new ImageIcon(ImageIO.read(new File("Board/board.jpg"))));
        picLabel.setHorizontalAlignment(JLabel.LEFT);
        picLabel.setVerticalAlignment(JLabel.TOP);
        this.setContentPane(picLabel);
      }
      catch(Exception ex)
      {
        System.exit(0);
      }
    }



    public GUIFrame(Board theModel)
    {

      model = theModel;
      // Set main window stuffs
      this.setSize(mainWidth,mainHeight);
      this.setResizable(false);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setTitle("DeadwoodTM");


      setupMainPanel();
      setupPlayerInfoPanel();


    }
    private void setupPlayerInfoPanel()
    {
      playerInfoLabel = new JLabel("Player Information");
      playerinfoTextArea = new JTextArea(40,25);
      playerinfoTextArea.setEditable(false);
      playerInfoScroll = new JScrollPane(playerinfoTextArea);
      playerInfoScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

      playerInfoPanel = new JPanel();
      playerInfoPanel.add(playerInfoLabel);
      playerInfoPanel.add(playerInfoScroll);
      // Allow panel to be included
      this.add(playerInfoPanel, 0, 0);
      playerInfoPanel.setLayout(new FlowLayout());
      playerInfoPanel.setLocation(1225, 110);
      playerInfoPanel.setSize(350,850);

    }



    private void updatePlayerInfo()
    {
      String res = "";
      ArrayList<Player> plist = model.GetPlayerList();
      if (plist == null)
        return;
      if (plist.size() == 0)
        return;

      for (Player p : plist)
      {
        res += p.GetName() + "\n" +
          "Cash: $" + p.GetMoney() + "   Credits/Fame: " + p.GetFame() + "\n" +
          (p.HasRole() ? "Working " + p.GetRoleName() + ": \"" + p.GetRoleDescription()+"\"\n"+
            "Rehearsal Tokens: " + p.GetRehearsal(): "")+
          "\n\n";
      }

      playerinfoTextArea.setText(res);


    }

    private void setupMainPanel()
    {
      mainPanel = new JPanel();

      // Setup background image
      setupBackgroundByJLabel();



      // Panel Elements
      textfield = new JTextArea(5, 55);
      textfield.setEditable(false);
      scroll = new JScrollPane(textfield);
      scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

      userInput = new JTextField("", 30);
      userLabel = new JLabel("Enter a command: ");
      submit  = new JButton("Submit");
      b_who   = new JButton("My Info");
      b_where = new JButton("Where am I");
      b_act   = new JButton("Act");
      b_help  = new JButton("Help!");
      b_end = new JButton("End Turn");
      b_two = new JButton("Two Players");
      b_three = new JButton("Three Players");
      b_rehearse = new JButton("Rehearse");

      b_upgrade = new JButton("Upgrade");
      b_move = new JButton("Move my piece");
      b_work = new JButton("Work a role");


      // Debugging buttons to assist
      b_randy = new JButton("!Go Randy!");
      b_endday = new JButton("!End Day!");
      b_endgame = new JButton("!End Game!");

      pairButtonListeners();
      mainPanel.add(scroll);
      mainPanel.add(userLabel);
      mainPanel.add(userInput);
      mainPanel.add(submit);
      // mainPanel.add(b_who);
      // mainPanel.add(b_where); - Not needed in GUI anymore
      mainPanel.add(b_move);
      mainPanel.add(b_end);
      mainPanel.add(b_work);
      mainPanel.add(b_act);
      mainPanel.add(b_rehearse);
      mainPanel.add(b_help);
      mainPanel.add(b_two);
      mainPanel.add(b_three);


      mainPanel.add(b_upgrade);

      mainPanel.add(b_randy);
      mainPanel.add(b_endday);
      mainPanel.add(b_endgame);

      setButtonVis(false);
      disableDebug();


      // Allow panel to be included
      this.add(mainPanel);
      mainPanel.setLocation(320, 900);
      // mainWidth is 1600 (x pixels)
      mainPanel.setSize(750,750);

    }

    private void setButtonVis(boolean val)
    {
      userLabel.setVisible(val);
      userInput.setVisible(val);
      submit.setVisible(val);
      b_who.setVisible(val);
      b_where.setVisible(val);
      b_act.setVisible(val);
      b_help.setVisible(val);
      b_end.setVisible(val);
      b_rehearse.setVisible(val);
      b_upgrade.setVisible(val);
      b_move.setVisible(val);
      b_work.setVisible(val);
    }

    private void enableDebug()
    {
      this.setSize(mainWidth,mainHeight+20);
      b_randy.setVisible(true);
      b_endday.setVisible(true);
      b_endgame.setVisible(true);
    }

    private void disableDebug()
    {
      this.setSize(mainWidth,mainHeight);
      b_randy.setVisible(false);
      b_endday.setVisible(false);
      b_endgame.setVisible(false);
    }

    private void pairButtonListeners()
    {
      submit.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          String txt = userInput.getText();
          if (txt.equals("~debug"))
          {
            if (!debug)
              enableDebug();
            else
              disableDebug();

            debug = !debug;
          }
          else
          {
            broadcast(userInput.getText());
          }
          userInput.setText("");
        }
      });

      b_who.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("who");
        }
      });
      b_where.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("where");
        }
      });
      b_act.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("act");
        }
      });
      b_end.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("end");
        }
      });

      b_help.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("help");
        }
      });

      b_rehearse.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("rehearse");
        }
      });

      b_two.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("2");
        }
      });

      b_three.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("3");
        }
      });

      b_randy.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("~gorandy");
        }
      });

      b_endday.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("~endday");
        }
      });

      b_endgame.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          broadcast("~endgame");
        }
      });

      b_upgrade.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          String[] actual_opts = { "$", "cr" };
          String[] opts = { "Cash", "Credit/Fame" };
          String[] ranks = { "1", "2", "3", "4", "5", "6"};
          int opt_choice = -1;
          int rank_choice = -1;
          try
          {
            opt_choice = JOptionPane.showOptionDialog(
            null,
            "What would you like to spend to upgrade?",
            "DeadwoodTM - Upgrade",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opts,
            opts[0]);
          }
          catch(Exception ex)
          {
            System.out.printf("Exception [b_upgrade.actionPerformed]: %s\n", ex.getMessage());
            ex.printStackTrace();
            return;
          }

          if (opt_choice < 0)
            return;

          try
          {
            rank_choice = JOptionPane.showOptionDialog(
            null,
            "What rank would you like to upgrade to?",
            "DeadwoodTM - Upgrade",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            ranks,
            ranks[0]);
          }
          catch(Exception ex)
          {
            System.out.printf("Exception [b_upgrade.actionPerformed]: %s\n", ex.getMessage());
            ex.printStackTrace();
            return;
          }

          if (opt_choice < 0 || rank_choice < 0)
            return;

          broadcast("upgrade " + actual_opts[opt_choice] + " " + ranks[rank_choice]);

        }
      });

      b_move.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
          Location curLoc = model.GetActivePlayerLocation();
          String[] neighbors = curLoc.GetAdjacentLocationNames();
          int res = -1;
          try
          {
            res = JOptionPane.showOptionDialog(
            null,
            "Pick a place to move to",
            "DeadwoodTM - Move",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            neighbors,
            neighbors[0]);
          }
          catch(Exception ex)
          {
            System.out.printf("Exception [b_move.actionPerformed]: %s\n", ex.getMessage());
            ex.printStackTrace();
            return;
          }

          if (res < 0)
            return;

          broadcast("move " + neighbors[res].toLowerCase().trim());

        }
      });

      b_work.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){


          int choice = -1;
          Location pLoc = model.GetActivePlayerLocation();
          if (pLoc instanceof Set)
          {
            Set pSet = (Set)pLoc;
            ArrayList<Part> extraRoles = pSet.getParts();
            Card scene = pSet.getScene();
            ArrayList<Role> mainRoles = scene.getRoles();

            int size = extraRoles.size() + mainRoles.size();
            String[] opts = new String[size];
            int i = 0;
            for (Role r : mainRoles)
              opts[i++] = r.getName();

            for (Part p : extraRoles)
              opts[i++] = p.getName();


            try
            {
              choice = JOptionPane.showOptionDialog(
                null,
                "Pick which role you want to take:",
                "DeadwoodTM - Work",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opts,
                opts[0]);

              if (choice < 0)
              return;

              broadcast("work " + opts[choice].toLowerCase().trim());
            }
            catch (Exception ex)
            {
              System.out.printf("Exception [b_work.actionPerformed]: %s\n", ex.getMessage());
              ex.printStackTrace();
            }

          }
          else
          {
            textfield.setText("You are currently not on a set...");
            return;
          }


        }
      });
    }

    public void Update(String str)
    {
      if (alreadyInit)
      {
        // Forces an update on all player positions
        // And reprints the info on right side
        updatePlayerInfo();
        placePlayers();
      }

      textfield.setText(str);

      switch (model.GetCurrentPlayerID())
      {
        case 0: // blue
          textfield.setForeground(Color.BLUE);
          textfield.setBackground(Color.WHITE);
          break;
        case 1: // cyan
          textfield.setForeground(Color.CYAN);
          textfield.setBackground(Color.BLACK);
          break;
        case 2: // green
          textfield.setForeground(Color.GREEN);
          textfield.setBackground(Color.BLACK);
          break;
        case 3: // orange
          textfield.setForeground(Color.ORANGE);
          textfield.setBackground(Color.BLACK);
          break;
        case 4: // pink
          textfield.setForeground(Color.PINK);
          textfield.setBackground(Color.BLACK);
          break;
        case 5: // red
          textfield.setForeground(Color.RED);
          textfield.setBackground(Color.WHITE);
          break;
        case 6: // violet
          textfield.setForeground(Color.MAGENTA);
          textfield.setBackground(Color.WHITE);
          break;
        case 7: // yellow
          textfield.setForeground(Color.YELLOW);
          textfield.setBackground(Color.BLACK);
          break;
        default:
          // doesn't have a marker (too much Randy Rando)
          textfield.setForeground(Color.BLACK);
          textfield.setBackground(Color.WHITE);
          return;

      }
    }

    private void placePlayers()
    {
      // place the players all over the place
      for (Player peeWee : model.GetPlayerList())
      {
        int rank = peeWee.getRank();
        int pnum = peeWee.GetID();
        Location loc = peeWee.GetLocation();
        int[] coords = loc.GetXYHW();
        int xPos = coords[0];
        int yPos = coords[1];
        boolean hasRole = peeWee.HasRole();
        boolean hasMainRole = (hasRole ? peeWee.IsMainRole() : false);

        String ref;

        switch(pnum)
        {
          case 0: // blue
            ref = "b";
            break;
          case 1: // cyan
            ref = "c";
            break;
          case 2: // green
            ref = "g";
            break;
          case 3: // orange
            ref = "o";
            break;
          case 4: // pink
            ref = "p";
            break;
          case 5: // red
            ref = "r";
            break;
          case 6: // violet
            ref = "v";
            break;
          case 7: // yellow
            ref = "y";
            break;
          default:
            // doesn't have a marker (too much Randy Rando)
            return;
        }

        ref += rank + ".png";
        if (pnum+1 > Jplayers.size())
        {
          // Create player
          JLabel n = new JLabel();
          this.add(n, 2, 0);
          Jplayers.add(n);
        }
        JLabel jref = Jplayers.get(pnum);
        // jref.setLocation(xPos, yPos);
        try
        {
          jref.setIcon(new ImageIcon(ImageIO.read(new File("Board/dice/" + ref))));
          if (!hasRole)
          {
            // absolute position below the card
            int diceOffset = 40 * pnum;
            int cardOffset = 115;
            jref.setBounds(xPos + diceOffset,yPos + cardOffset,40,40);
          } else if (hasMainRole)
          {
            Role pRole = peeWee.GetRole();
            int off[] = pRole.GetXYHW();
            int xOffset = off[0];
            int yOffset = off[1];

            // Should set on main role
            jref.setBounds(xPos + xOffset, yPos + yOffset,40,40);
          }
          else // hasExtraRole
          {
            Role pRole = peeWee.GetRole();
            int off[] = pRole.GetXYHW();

            // absolute position on extra role
            jref.setBounds(off[0], off[1], 40,40);
          }
        }
        catch(Exception ex)
        {
          System.out.printf("Exception [GUIView.placePlayers]: %s\n", ex.getMessage());
          ex.printStackTrace();
        }
        jref.setVisible(true);



      }
    }

    public String GetUserInput()
    {
      return "";
    }
    public void Run(){
      // We should see our product

      this.setVisible(true);
      userInput.requestFocus();
    }
    public void GameInit()
    {
      setsAndCard();
      setButtonVis(true);
      b_two.setVisible(false);
      b_three.setVisible(false);
      alreadyInit = true;
    }

    public void SignalDone()
    {
      setButtonVis(false);
    }


    // Code from Christine - imported from alt-b
    private void setsAndCard(){

      Set[] setArray = model.GetSetArray();
      for (int i = 0; i < setArray.length; i++){
        for (int j = 0; j < setArray[i].returnTakeArray().length; j++){
          try{
            Take take_ref = setArray[i].returnTakeArray()[j];
            JPanel ref = take_ref.takeDisplaysPanel;
            this.add(ref);
            ref.setVisible(true);
            take_ref.showTake();
          } catch(Exception e3){
            System.out.println("What......y65");
          }
        }
      }


      Card[] cardDeck = model.GetCardDeck();
      for (int i = 0; i < cardDeck.length; i++){

        try{
          JPanel ref =cardDeck[i].cardJPanel;
            this.add(ref);
            //cardDeck[i].cardJPanel.setLocation(0,0);
            ref.setVisible(false);
        } catch (Exception e1){
          System.out.println("OH NO" + cardDeck[i].GetName());
        }
      }


    }
  } // End of GUIFrame



}
