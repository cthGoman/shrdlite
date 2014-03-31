import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.*;


public class Constraints{

	public static boolean isWorldAllowed(JSONArray world, String holding, JSONObject objects) {

      if(!(holding==null || holding.length()<2)){
         return false;
      }
      
      for(int i=0; i<world.size(); i++){
         JSONArray column = (JSONArray) world.get(i);
         if(column.size()>1){
            for(int j=0; j<column.size(); j++){
               String object = (String) column.get(j);
               JSONObject objectinfo = (JSONObject) objects.get(object);
               String form = (String) objectinfo.get("form");
               String size = (String) objectinfo.get("size");
              
               if(form.contains("ball")){
                  if(j+1<column.size()){  // Balls can not support anything
                     return false;     
                  }
                  if(j-1>=0){
                     String objectBelow = (String) column.get(j-1);
                     JSONObject objectBelowinfo = (JSONObject) objects.get(objectBelow);
                     String formBelow = (String) objectBelowinfo.get("form");
                     
                     if(!formBelow.contains("box")){  // Balls can only be supported by boxes or floor
                        return false;
                     }
                  }
               }
               if(size.contains("small")){   // Small objects cannot support large objects
                  if(j+1<column.size()){
                     String objectAbove = (String) column.get(j+1);
                     JSONObject objectAboveinfo = (JSONObject) objects.get(objectAbove);
                     String sizeAbove = (String) objectAboveinfo.get("size");
                     if(sizeAbove.contains("large")){
                        return false;
                     }
                  }   
               }
               if(form.contains("box")){  // Boxes cannot contain pyramids or planks of the same size
                  if(j+1<column.size()){
                     String objectAbove = (String) column.get(j+1);
                     JSONObject objectAboveinfo = (JSONObject) objects.get(objectAbove);
                     String sizeAbove = (String) objectAboveinfo.get("size");
                     String formAbove = (String) objectAboveinfo.get("form");
                     
                     if(formAbove.contains("pyramid") || formAbove.contains("plank")){
                        if(sizeAbove.equals((String) objectinfo.get("size"))){
                           return false;
                        }
                     }
                  } 
                  if(j-1>=0){
                     String objectBelow = (String) column.get(j-1);
                     JSONObject objectBelowinfo = (JSONObject) objects.get(objectBelow);
                     String formBelow = (String) objectBelowinfo.get("form");
                     String sizeBelow = (String) objectBelowinfo.get("size");
                   
                     // Boxes can only be supported by tables or planks of the same size, but large boxes can also be supported by large bricks.
                     boolean sizeCondition = (sizeBelow.equals(size));
                     boolean formCondition = (formBelow.contains("table") || formBelow.contains("plank") || formBelow.contains("box"));
                     if(!formCondition){  
                        if(!(formBelow.contains("brick") && size.contains("large") && sizeBelow.contains("large"))){
                           return false;
                        }
                     }
                     if(formCondition && !sizeCondition && !formBelow.contains("box")){
                        return false;
                     }  
                  }
               }
            }
         }
      } 
      return true;
		
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
