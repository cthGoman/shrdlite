
import java.util.*;
import org.json.simple.JSONObject;

public class FindObject{
   public static ArrayList<String> match(List<String> object,JSONObject objectsInformation, ArrayList<ArrayList<String>> world){
      JSONObject objectinfo ;
      String currentObject ;
      String form ;
      String size ;
      String color ;
      ArrayList<String> matchingObjects = new ArrayList<String>();
      if(object.size() > 0){
         if(object.get(0).equals("floor")){
            for(int i = 0 ; i < world.size() - 1 ; i++){
               matchingObjects.add("floor-"+i);
            }
         }
         else{
            for(int i=0;i<world.size();i++){
               for(int j=0;j<world.get(i).size();j++){
                  currentObject = world.get(i).get(j);
                  objectinfo = (JSONObject) objectsInformation.get(currentObject);
                  form = (String) objectinfo.get("form");
                  size = (String) objectinfo.get("size");
                  color = (String) objectinfo.get("color");
                  DebugFile.print(form+" "+size+" "+color+",");						
                  boolean formRight = form.equals(object.get(0)) || object.get(0).equals("anyform");
                  boolean sizeRight = size.equals(object.get(1)) || object.get(1).equals("-");
                  boolean colorRight = color.equals(object.get(2)) || object.get(2).equals("-");
                  if(formRight && sizeRight && colorRight){
                     matchingObjects.add(currentObject);
                  }
               }
            }
         }
      }
      return matchingObjects;
   }
   public static ArrayList<String> relatedObject(ArrayList<String> relativeObjects, String relation,ArrayList<String> BasicObjects, ArrayList<ArrayList<String>> world){
      ArrayList<String> fulfillingObjects = new ArrayList<String>();
      for(String bo:BasicObjects){
         int c = -1;
         int r = -1;
         if(bo.substring(0,bo.length()-1).equals("floor-")){
            c = Integer.parseInt(bo.substring(bo.length()-1,bo.length()));
         }
         for(int i = 0 ; i < world.size() && c == -1 ; i++){
            for(int j = 0 ; j < world.get(i).size() && c == -1; j++){
               if(world.get(i).get(j).equals(bo)){
                  c = i;
                  r = j;
               }
            }
         }
         if(c != world.size() - 1){
            switch (relation) {
               case "ontop":
                  if(world.get(c).size() > r + 1){
                     for(String ro:relativeObjects){
                        if(world.get(c).get(r + 1).equals(ro)){
                           fulfillingObjects.add(ro);
DebugFile.println(ro);
                        }
                     }
                  }
                  break;
               case "above":
                  for(int k = r + 1; k < world.get(c).size() ; k++){
                     world.get(c).get(k).equals("?");
                  }	
                  break;
               default:
            }
         }
      }
DebugFile.println("");
      return fulfillingObjects;
   }
}