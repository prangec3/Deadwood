import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import javax.swing.*;
import javax.imageio.ImageIO;

// Full import from alt-b
public class Take {

    int takeNumber;
    int[] xyhw;
    JPanel takeDisplaysPanel;
  JLabel takeLabel;
    public Take(int t, int[] x){
      this.takeNumber =  t;
      this.xyhw = x;
      try{
      this.takeDisplaysPanel = new JPanel();
      this.takeDisplaysPanel.setSize(x[2],x[3]);
      this.takeDisplaysPanel.setLocation(x[0], x[1]);
      this.takeDisplaysPanel.setOpaque(false);
      this.takeDisplaysPanel.setVisible(false);
  /************************************/
      this.takeLabel =   new JLabel(new ImageIcon(ImageIO.read(new File("Board/shot.png"))));
      this.takeLabel.setBounds(0,0,47,47);
      this.takeDisplaysPanel.add(this.takeLabel);

  /**************************************/

    } catch(Exception e){

    }
    }
    public int[] getXY(){
      return this.xyhw;
    }
    public void hideTake(){
      this.takeDisplaysPanel.setVisible(false);
    }

    public void showTake(){
    //  this.takeDisplaysPanel.setOpaque(true);
        this.takeDisplaysPanel.setVisible(true);
    }

    public void printInfo(){
      System.out.println("Take: " + this.takeNumber);
      String[] x = {"x", "y", "h", "w"};
      for (int k = 0; k < 4; k++){
        System.out.printf("\t%s: %s", x[k], xyhw[k]);
      }
      System.out.println();
    }

}
