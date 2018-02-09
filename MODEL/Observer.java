// Observers can have Listeners
// Once an update occurs within Observer, he can broadcast to his listeners
//
// Example: User inputs in View -> View broadcasts this -> Controller Sees this, etc


import java.util.*;
public abstract class Observer
{
  private ArrayList<Listener> lsr = new ArrayList<Listener>();
  private String stagedStr = "";

  public void addListener(Listener obj)
  {
    lsr.add(obj);
  }

  public void rmvListener(Listener obj)
  {
    lsr.remove(obj);
  }

  protected void broadcast(String msg)
  {
    if (lsr.size() == 0) return;

    for (Listener obj : lsr)
    {
      safe_send(obj, msg);
    }
  }

  private void safe_send(Listener obj, String msg)
  {
    try
    {
      obj.changed(msg);
    }
    catch(Exception ex)
    {
      System.out.printf("Exception [Observer]: %s\n\n", ex.getMessage());
      ex.printStackTrace();
    }
  }

  protected void append_msg(String msg)
  {
    if (stagedStr == "")
      stagedStr = msg;
    else
      stagedStr += "\n" + msg;
  }

  protected void broadcast_staged()
  {
    if (stagedStr.equals("")){}
    else
    {
      broadcast(stagedStr);
      stagedStr = "";
    }
  }
}
