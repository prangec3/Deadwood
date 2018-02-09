public class Upgrade{
  int level;
  String currency;
  int amt;
  int[] xyhw;

  Upgrade(int l, String c, int a, int[] x){
    this.level = l;
    this.currency = c;
    this.amt = a;
    this.xyhw = x;
  }

  public void printInfo(){
        System.out.print("Currency: " + this.currency);
        System.out.print(" Level: " + this.level);
        System.out.println(" Amount: " + this.amt);
        String[] x = {"x", "y", "h", "w"};
        for (int k = 0; k < 4; k++){
          System.out.printf("\t%s: %s", x[k], xyhw[k]);
        }
          System.out.println("\n");
  }


}
