public class Office extends Location{

 // private String name = "";
 // private String[] neighborList;

 private Upgrade[] upgradeArray;

 Office(String n){
   super(n);
   int[] vals = {9,459,208,209};
   xyhw =  vals;
   // this.name = n;
 }


 Office(String n, String[] nl, Upgrade[] u){
   super(n);
   this.neighborList = nl;
   int[] vals = {9,459,208,209};
   xyhw =  vals;
   this.upgradeArray = u;
 }
//test function!
 public void printInfo(){
   System.out.printf("Name: office\n");
   System.out.println();
   System.out.printf("Area:\nx: %d y: %d h: %d w: %d\n",this.xyhw[0], this.xyhw[1], this.xyhw[2], this.xyhw[3]);

   for (int i = 0; i < neighborList.length; i++){
     System.out.println("Neighbor: " + neighborList[i]);
   }

   System.out.println("Upgrades");
   for (int i = 0; i < upgradeArray.length; i++){
     upgradeArray[i].printInfo();
   }

 }

}
