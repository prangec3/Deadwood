import java.util.*;

public class ConsoleView extends View
{
  Scanner input = new Scanner(System.in);
  private boolean notGameOver_andEverbodyDies = true;
  public void Update(String str)
  {
    System.out.printf("%s\n\n", str);
  }
  public String GetUserInput()
  {
    System.out.printf("> ");
    return input.nextLine();
  }

  public void changed(String str)
  {
    /* this is an update from the "Model": print str */
    switch(str)
    {
      case Board.quit_cmd:
        SignalDone();

      break;

      default:
        Update(str);
    }

  }

  public void Run()
  {
    while (notGameOver_andEverbodyDies)
    {
      broadcast(GetUserInput());
    }
  }

  public void SignalDone()
  {
    input.close();
    notGameOver_andEverbodyDies = false;
  }
}
