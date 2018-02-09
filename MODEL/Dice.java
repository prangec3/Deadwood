// ToDo: Implementation
import java.util.*;
import java.util.Random;

public class Dice {
  private Random roller = new Random();

  /*rolls one standard 6 sided die one time and returns the value of the roll*/
  public int Roll() {
    return roller.nextInt(6)+1;
  }

  /*rolls the inputted number of dice and returns those values stored in an int[]*/
  public int[] Rolls(int num_rolls) {
    int[] rollValues = new int[num_rolls];
    int count = 0;
    while(count < num_rolls){
      rollValues[count] = roller.nextInt(6)+1;
      count++;
    }
    return rollValues;
  }

  /* method to test Dice class methods*/
  public void testDice(){
    System.out.println("testing single rolls: ");
    System.out.println(Roll());
    System.out.println(Roll());
    System.out.println(Roll());
    System.out.println("testing multiple rolls: ");
    int[] stuff = Rolls(5);
    System.out.println("  " + stuff[0]);
    System.out.println("  " + stuff[1]);
    System.out.println("  " + stuff[2]);
    System.out.println("  " + stuff[3]);
    System.out.println("  " + stuff[4]);
  }
}
