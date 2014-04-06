
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
      
      for(int i=0;i<world.size();i++){
         for(int j=0;j<world.get(i).size();j++){
            currentObject = world.get(i).get(j);
            objectinfo = (JSONObject) objectsInformation.get(currentObject);
            form = (String) objectinfo.get("form");
            size = (String) objectinfo.get("size");
            color = (String) objectinfo.get("color");
            boolean formRight = form.equals(object.get(0)) || object.get(0).equals("anyform");
            boolean sizeRight = size.equals(object.get(1)) || object.get(1).equals("-");
            boolean colorRight = color.equals(object.get(2)) || object.get(2).equals("-");
            if(formRight && sizeRight && colorRight){
               matchingObjects.add(currentObject);
               
            }
            
         }
         
         
      }
      
      
      return matchingObjects;//		JSONObject objectinfo = (JSONObject) objects.get(topobject);
//		String form = (String) objectinfo.get("form");


	}
}