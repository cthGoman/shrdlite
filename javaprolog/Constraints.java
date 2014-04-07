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
            if(!isColumnAllowed(column, objects)){
               return false;
            }
         }
      } 
      return true;
		
	}
   
   
   public static boolean isColumnAllowed(JSONArray column, JSONObject objects){
   
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
      return true;
          
   
   }
   
   public static boolean isStatementAllowed(Statement statement, JSONObject objects){
      
      String sizeA;
      String sizeB;
      String formA;
      String formB;
     
      
      if(!(statement.get(1).contains("floor") || statement.get(1).contains("robot"))){
         JSONObject objectAInfo = (JSONObject) objects.get(statement.get(1));
         sizeA = (String) objectAInfo.get("size");
         formA = (String) objectAInfo.get("form");
      }
      else{
         sizeA = "undefined";
         formA = statement.get(1).substring(0,5);
      }
         
      if(!(statement.get(2).contains("floor") || statement.get(2).contains("robot"))){
         JSONObject objectBInfo = (JSONObject) objects.get(statement.get(2));
         sizeB = (String) objectBInfo.get("size");
         formB = (String) objectBInfo.get("form");
      }
      else{
         sizeB = "undefined";
         formB = statement.get(2).substring(0,5);
      }

      // ------------------------ "On top of"-statements ------------------------//          
      if(statement.get(0).contains("ontop")){
      
         if(sizeB.equals("small") && sizeA.equals("large")){
            return false;
         }
         
         if(formA.equals("ball") && !formB.contains("floor")){ // Balls only on top of floor (or inside boxes)
            return false;
         }
         
         if(formA.contains("floor")){        // Floor has to be below everything else
            return false;
         }
           
      }
      
      
      // ------------------------ "Inside"-statements ------------------------//   
      else if(statement.get(0).contains("inside")){
         if(!formB.equals("box")){
            return false;
         }
         if(formA.equals("ball") && !formA.equals("box")){
            return false;
         }
      }
      
      // ------------------------ "Above"-statements ------------------------//      
      else if(statement.get(0).contains("above")){
         if(sizeB.equals("small") && sizeA.equals("large")){
            return false;
         }
         if(formA.contains("floor")){        // Floor has to be below everything else
            return false;
         }
      }
      
      // ------------------------ "Under"-statements ------------------------//           
      else if(statement.get(0).contains("under")){
         if(formB.contains("floor")){        // Floor has to be below everything else
            return false;
         }
         if(sizeA.equals("small") && sizeB.equals("large")){
            return false;
         }
      }
      
      // ------------------------ "Beside"-statements ------------------------//           
      else if(statement.get(0).contains("beside")){
           
      }
      
      // ------------------------ "Left of"-statements ------------------------//           
      else if(statement.get(0).contains("leftof")){
            
      }
        
      // ------------------------ "Right of"-statements ------------------------//         
      else if(statement.get(0).contains("rightof")){
            
      }

      // ------------------------ "Hold"-statements ------------------------//          
      else if(statement.get(0).contains("hold")){
         if(!formA.contains("robot")){
            return false;
         }
         if(formB.contains("floor")){
            return false;
         }
         
      }
               
      return true;      // All checks passed
   }
}
