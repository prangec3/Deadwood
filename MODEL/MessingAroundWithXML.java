import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;
public class MessingAroundWithXML {

// for testing
  public static void main(String argv[]) {

    /*Works decent; loads cards into deck*/
     Set[] setArray = loadingSets();
     Card[] deck = loadingCards();
     Location trailer = loadingTrailer();
     Office office = loadingOffice();


  }


    public static Location loadingTrailer(){

        try{

          /* Getting xml info through doc builder */
            File fXmlFile = new File("Board/board.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList trailerNodeList = doc.getElementsByTagName("trailer");
            Node trailerNode = trailerNodeList.item(0);

            /* Acquire all info from the trailer */
            if (trailerNode.getNodeType() == Node.ELEMENT_NODE){
              String[] neighborArray = neighborHelperFunc((Element) trailerNode, "neighbor", null);
              int[] returnInt = {991, 248, 194, 201};
              Location returnLocation = new Location("trailer", neighborArray, returnInt);

              return returnLocation;

            }

      } catch (Exception e){
        System.out.println("Well we tried");
      }
        return null;
    }

    public static Office loadingOffice(){

      try {

          File fXmlFile = new File("Board/board.xml");
          DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
          Document doc = dBuilder.parse(fXmlFile);
          doc.getDocumentElement().normalize();
          NodeList officeNodeList = doc.getElementsByTagName("office");
          Node officeNode = officeNodeList.item(0);

          if (officeNode.getNodeType() == Node.ELEMENT_NODE){

              Element officeElement =(Element) officeNode;
              NodeList upgradeList = officeElement.getElementsByTagName("upgrade");
              String[] neighborArray = neighborHelperFunc((Element) officeNode, "neighbor", null);
              Element upgradeCurElement = (Element) officeNode;
              NodeList areaList = upgradeCurElement.getElementsByTagName("area");

              int[] xyhw = new int[4];
              int[][] a = new int[areaList.getLength()][4];
              for (int i = 0; i < areaList.getLength(); i++){
                  Element areaElement = (Element) areaList.item(i);
                  a[i][0] = Integer.parseInt(areaElement.getAttribute("x"));
                  a[i][1] = Integer.parseInt(areaElement.getAttribute("y"));
                  a[i][2] = Integer.parseInt(areaElement.getAttribute("h"));
                  a[i][3] = Integer.parseInt(areaElement.getAttribute("w"));
              }


              Upgrade[] upgradeArray = new Upgrade[upgradeList.getLength()];
              for (int i = 0; i < upgradeList.getLength(); i++){
                  upgradeCurElement = (Element) upgradeList.item(i);
                  upgradeArray[i] = new Upgrade(Integer.parseInt(upgradeCurElement.getAttribute("level")),upgradeCurElement.getAttribute("currency")
                ,Integer.parseInt(upgradeCurElement.getAttribute("amt")), a[i]);
              }


              Office returnOffice = new Office("office", neighborArray, upgradeArray);

              return returnOffice;

          }


      } catch (Exception e) {
      e.printStackTrace();
      }

      return null;

    }

    public static Set[] loadingSets(){
      /* Gets info from each set and loads info into appropriate objcets */
      Set tempSet;
      Role tempRole;

      try {
          Element setElement;
          Element currentSetElement, sceneElement;
          NodeList partList, sceneList, sceneContent, takeList;
          String[] attList1 = {"number"};
          String[] attList2 = {"x","y","h","w"};
          String[] attList3 = {"name", "level"};
          File fXmlFile = new File("Board/board.xml");
          DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
          Document doc = dBuilder.parse(fXmlFile);
          doc.getDocumentElement().normalize();
          NodeList setNodeList = doc.getElementsByTagName("set");
          Set[] returnArray = new Set[setNodeList.getLength()];

          /* For each set, there will be a number of parts and a number of takes*/
          for (int temp = 0; temp < setNodeList.getLength(); temp++) {
              Set currentSet;
              int areaCount = 1;
              String[] attList = {"name"};
              Node setNode = setNodeList.item(temp);
              if (setNode.getNodeType() == Node.ELEMENT_NODE) {
                currentSetElement = (Element) setNode;
                partList = currentSetElement.getElementsByTagName("part");
                takeList = currentSetElement.getElementsByTagName("take");

                int[][] areaTrack = areaHelperFunc((Element) setNode, "area", attList2);
                Part[] partArray = new Part[partList.getLength()];
                Take[] takeArray = new Take[takeList.getLength()];

                String[] neighborArray = neighborHelperFunc((Element) setNode, "neighbor", attList);
                currentSet = new Set(((Element) setNode).getAttribute("name"), neighborArray, areaTrack[0]);


  /***************************************** dealing with each TAKE acquires appropriate data and adds it to correct place */
                for (int i = 0; i < takeList.getLength(); i++){
                  Element takeElement = (Element) takeList.item(i);
                  Take takeForArray = new Take(Integer.parseInt(takeElement.getAttribute("number")), areaTrack[areaCount++]);
                  takeArray[i] = takeForArray;
                }


  /*************************DEALING WITH EACH PART, acquires appropriate data and adds it to correct place */
                for (int i = 0; i < partList.getLength(); i++){
                  Element partElement = (Element) partList.item(i);
                  NodeList lineList = partElement.getElementsByTagName("line");
                  Element lineElement = (Element) lineList.item(0);
                  NodeList lineContent = lineElement.getChildNodes();
                  String linePlease =  ((Node)lineContent.item(0)).getNodeValue().trim();
                  Part partForArray = new Part(partElement.getAttribute("name"), linePlease,
                  Integer.parseInt(partElement.getAttribute("level")), areaTrack[areaCount++]);
                  partArray[i] = partForArray;
                  partForArray = null;
              }

                currentSet.addPartandTake(partArray, takeArray);
                returnArray[temp] = currentSet;

              }




          }
          return returnArray;

    } catch (Exception e) {
      e.printStackTrace();
      }

      return null;

    }


/* Helps sort data for neighbors */
  public static String[] neighborHelperFunc(Element setNode, String someString, String[] someAtt){
      String[] returnArray;
      Element currentSetElement = (Element)setNode;
      NodeList genericList = currentSetElement.getElementsByTagName(someString);
      NodeList genericLineList = currentSetElement.getElementsByTagName("line");
      returnArray = new String[genericList.getLength()];

      for (int i = 0; i < genericList.getLength(); i++){
          Element n = (Element) genericList.item(i);
          NodeList nContent = n.getChildNodes();
          returnArray[i] = n.getAttribute("name");
      }


      return returnArray;
}




public static int[][] areaHelperFunc(Element setNode, String someString, String[] someAtt){
    Element currentSetElement = (Element)setNode;
    NodeList genericList = currentSetElement.getElementsByTagName(someString);
    int[][]newArr=new int[genericList.getLength()][4];
      for (int i = 0; i < genericList.getLength(); i++){
          Element n = (Element) genericList.item(i);
          NodeList nContent = n.getChildNodes();
          for (int j = 0; j < someAtt.length; j++){
            newArr[i][j] = Integer.parseInt(n.getAttribute(someAtt[j]));
          }

      }
    return newArr;
}



/* Acquires info for each card and loads it into appropriate object */
  public static Card[] loadingCards(){
    Card tempCard;
    Role tempRole;
    ArrayList<Role> roleList;
    try {
        Element cardElement;
        Element currentCardElement, sceneElement;
        NodeList partList, sceneList, sceneContent;
        File fXmlFile = new File("Board/cards.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList cardNodeList = doc.getElementsByTagName("card");

        Card[] deck = new Card[cardNodeList.getLength()];

        for (int temp = 0; temp < cardNodeList.getLength(); temp++) {
            Node cardNode = cardNodeList.item(temp);
            if (cardNode.getNodeType() == Node.ELEMENT_NODE) {

                  cardElement = (Element) cardNode;
                  currentCardElement = (Element)cardNode;
                  partList = currentCardElement.getElementsByTagName("part");
                  sceneList = currentCardElement.getElementsByTagName("scene");
                  NodeList areaList = currentCardElement.getElementsByTagName("area");
                  int[] xyhw = new int[4];
                  int[][] a = new int[areaList.getLength()][4];

                  for (int i = 0; i < areaList.getLength(); i++){
                    Element areaElement = (Element) areaList.item(i);
                    a[i][0] = Integer.parseInt(areaElement.getAttribute("x"));
                    a[i][1] = Integer.parseInt(areaElement.getAttribute("y"));
                    a[i][2] = Integer.parseInt(areaElement.getAttribute("h"));
                    a[i][3] = Integer.parseInt(areaElement.getAttribute("w"));

                  }



                  sceneElement = (Element)sceneList.item(0);
                  sceneContent = sceneElement.getChildNodes();

                  roleList = new ArrayList<Role>();


                  for (int i = 0; i < partList.getLength(); i++){

                      Element partElement = (Element) partList.item(i);
                      NodeList lineList = partElement.getElementsByTagName("line");
                      Element lineElement = (Element) lineList.item(0);
                      NodeList lineContent = lineElement.getChildNodes();
                      String linePlease =  ((Node)lineContent.item(0)).getNodeValue().trim();

                      tempRole = new Role(Integer.parseInt(partElement.getAttribute("level")),  partElement.getAttribute("name"), linePlease, a[i]);

                      roleList.add(tempRole);

                  }

                  /* Actually make card... */
                    tempCard = new Card(cardElement.getAttribute("name"), Integer.parseInt(cardElement.getAttribute("budget")),
                    cardElement.getAttribute("img"), ((Node)sceneContent.item(0)).getNodeValue().trim(),
                    Integer.parseInt(sceneElement.getAttribute("number")), roleList);
                    deck[temp] = tempCard;

            }


        }

    return deck;

    } catch (Exception e) {
    e.printStackTrace();
    }


return null;
  }


}
