public abstract class View extends Observer implements Listener
{
  public abstract void Update(String str);
  public abstract String GetUserInput();
  public abstract void Run();
  public abstract void SignalDone();
}
