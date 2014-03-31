import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.*;


public class Constraints{

	public static boolean isWorldAllowed(JSONArray world, String holding, JSONObject objects) {

      boolean isAllowed = true;
      isAllowed = (holding==null || holding.length()<2);
      System.out.println(world);
      for(int i=0; i<world.size(); i++){
         JSONArray column = (JSONArray) world.get(i);
         if(column.size()>1){
            for(int j=0; j<column.size(); j++){
               String object = (String) column.get(j);
               JSONObject objectinfo = (JSONObject) objects.get(object);
               String form = (String) objectinfo.get("form");
               String size = (String) objectinfo.get("size");
              
               if(form.contains("ball")){
                  if(j+1<column.size()){
                     System.out.println("FAIL, ball can not hold objects");
                     return false;
                  }
               if(size.contains("small")){
               
               }   
               }
            }
         }
      } 
      return isAllowed;
		
	}
   
   public static boolean isGoalRowAllowed(List<Statement> goalRow, JSONObject objects){
      boolean isAllowed = true;
      for (Statement statement:goalRow){
         // if(statement.get(0)=="ontop"){
//             if(objects.statement.get(2).form == "ball"){
//                return false;
//             }
//             if((objects.statement.get(1).form == "ball") && !(objects.statement.get(2).contains("floor"))){
//                return false;
//             }
//          
//          }      
      }
      return isAllowed;
   }
}
