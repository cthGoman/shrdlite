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
   
   public static boolean isWorldAllowed(State worldState, JSONObject objects) {

      if(!(worldState.getHolding()==null || worldState.getHolding().length()<2)){
         return false;
      }
      
      for(ArrayList<String> column : worldState.getWorld()){
         if(column.size()>1){
            if(!isColumnAllowed(column, objects)){
               return false;
            }
         }
      } 
      return true;
		
	}
   
    public static boolean isColumnAllowed(ArrayList<String> column, JSONObject objects){
   
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
      
      if(statement.get(1).equals(statement.get(2))){
         //System.out.println("fail 1");
         return false;
      }

      // ------------------------ "On top of"-statements ------------------------//          
      if(statement.get(0).contains("ontop")){
      
         if(sizeB.equals("small") && sizeA.equals("large")){
            //System.out.println("fail 2");
            return false;
         }
         
         if(formA.equals("ball") && !formB.contains("floor")){ // Balls only on top of floor (or inside boxes)
            //System.out.println("fail 3");
            return false;
         }
         
         if(formB.equals("ball")){           // Balls cannot support anything
            //System.out.println("fail 4");
            return false;
         }
     
         if(formA.contains("floor")){        // Floor has to be below everything else
            //System.out.println("fail 5");
            return false;
         }
         if(formB.equals("box") && !(formA.equals("table") || formA.equals("plank") || formA.equals("brick")) ){
            return false;      
         }   
         
         if(formB.equals("box") && formA.equals("table") && (!sizeA.equals(sizeB)) ){
            //System.out.println("fail 6");
            return false;
         }         
         if(formB.equals("box") && formA.equals("plank") && (!sizeA.equals(sizeB)) ){
            //System.out.println("fail 7");
            return false;
         }
         if(formB.equals("box") && formA.equals("brick") && (!sizeA.equals(sizeB)) && (!sizeA.equals("large")) ){
            //System.out.println("fail 8");
            return false;
         }                            
      }
      
      
      // ------------------------ "Inside"-statements ------------------------//   
      else if(statement.get(0).contains("inside")){
         if(!formB.equals("box")){
            //System.out.println("fail 9");
            return false;
         }
         if(formA.equals("ball") && !formB.equals("box")){
            //System.out.println("fail 10");
            return false;
         }
         if(sizeA.equals("large") && sizeB.equals("small")){
            //System.out.println("fail 11");
            return false;
         }
         if((formA.equals("pyramid") || formA.equals("plank")) && (sizeA.equals(sizeB)) ){
            //System.out.println("fail 12");
            return false;
         }
      }
      
      // ------------------------ "Above"-statements ------------------------//      
      else if(statement.get(0).contains("above")){
         if(sizeB.equals("small") && sizeA.equals("large")){
            //System.out.println("fail 13");
            return false;
         }
         if(formA.contains("floor")){        // Floor has to be below everything else
            //System.out.println("fail 14");
            return false;
         }
         if(formB.contains("ball")){        // Balls cannot be under anything else
            return false;
      }
      
      // ------------------------ "Under"-statements ------------------------//           
      else if(statement.get(0).contains("under")){
         if(formB.contains("floor")){        // Floor has to be below everything else
            //System.out.println("fail 15");
            return false;
         }
         if(sizeA.equals("small") && sizeB.equals("large")){
            //System.out.println("fail 16");
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
            //System.out.println("fail 17");
            return false;
         }
         if(formB.contains("floor")){
            //System.out.println("fail 18");
            return false;
         }
         
      }
               
      return true;      // All checks passed
   }
   
   public static boolean isGoalRowAllowed(List<Statement> goalRow){
      
      String[] relationAB = new String[goalRow.size()];
      String[] idObjectA = new String[goalRow.size()];
      String[] idObjectB = new String[goalRow.size()];
      
      for(int i=0;i<goalRow.size();i++){
         relationAB[i]=goalRow.get(i).get(0);
         idObjectA[i]=goalRow.get(i).get(1);
         idObjectB[i]=goalRow.get(i).get(2);
      }

      for(int i=0;i<goalRow.size();i++){
         // ------------------------ "On top of"-statements ------------------------//          
         if(relationAB[i].contains("ontop")){
            for(int j=0;j<goalRow.size();j++){
               if(idObjectA[i].equals(idObjectB[j]) && idObjectB[i].equals(idObjectA[j]) && (relationAB[j].contains("above") || relationAB[j].contains("ontop") || relationAB[j].contains("inside"))){
                  return false;
               }
               if(idObjectA[i].equals(idObjectA[j]) && idObjectB[i].equals(idObjectB[j]) && relationAB[j].contains("under")){
                  return false;
               }      
            }
                                     
         }
      
         // ------------------------ "Inside"-statements ------------------------//   
         else if(relationAB[i].contains("inside")){
        
         }
      
         // ------------------------ "Above"-statements ------------------------//      
         else if(relationAB[i].contains("above")){
            for(int j=0;j<goalRow.size();j++){
               if(idObjectA[i].equals(idObjectB[j]) && idObjectB[i].equals(idObjectA[j]) && (relationAB[j].contains("above") || relationAB[j].contains("ontop"))){
                  return false;
               }
               if(idObjectA[i].equals(idObjectA[j]) && idObjectB[i].equals(idObjectB[j]) && relationAB[j].contains("under")){
                  return false;
               }                 
            }      
         }
      
         // ------------------------ "Under"-statements ------------------------//           
         else if(relationAB[i].contains("under")){
            for(int j=0;j<goalRow.size();j++){
               if(idObjectA[i].equals(idObjectB[j]) && idObjectB[i].equals(idObjectA[j]) && relationAB[j].contains("under")){
                  return false;
               }
               if(idObjectA[i].equals(idObjectA[j]) && idObjectB[i].equals(idObjectB[j]) && (relationAB[j].contains("above") || relationAB[j].contains("ontop"))){
                  return false;
               }  
            }      
         }
      
         // ------------------------ "Beside"-statements ------------------------//           
         else if(relationAB[i].contains("beside")){
            for(int j=0;j<goalRow.size();j++){
               if(idObjectA[i].equals(idObjectA[j]) && idObjectB[i].equals(idObjectB[j]) && (relationAB[j].contains("under") || relationAB[j].contains("above") || relationAB[j].contains("ontop"))){
                  return false;
               }
               if(idObjectA[i].equals(idObjectB[j]) && idObjectB[i].equals(idObjectA[j]) && (relationAB[j].contains("under") || relationAB[j].contains("above") || relationAB[j].contains("inside") || relationAB[j].contains("ontop"))){
                  return false;
               }    
            }      
  
         }
      
         // ------------------------ "Left of"-statements ------------------------//           
         else if(relationAB[i].contains("leftof")){
            for(int j=0;j<goalRow.size();j++){
               if(idObjectA[i].equals(idObjectB[j]) && idObjectB[i].equals(idObjectA[j]) && relationAB[j].contains("leftof")){
                  return false;
               }
               if(idObjectA[i].equals(idObjectA[j]) && idObjectB[i].equals(idObjectB[j]) && (relationAB[j].contains("rightof") || relationAB[j].contains("under") || relationAB[j].contains("above") || relationAB[j].contains("ontop"))){
                  return false;
               }
               if(idObjectA[i].equals(idObjectB[j]) && idObjectB[i].equals(idObjectA[j]) && (relationAB[j].contains("under") || relationAB[j].contains("above") || relationAB[j].contains("inside") || relationAB[j].contains("ontop"))){
                  return false;
               }                                
            }            
         }
        
         // ------------------------ "Right of"-statements ------------------------//         
         else if(relationAB[i].contains("rightof")){
            for(int j=0;j<goalRow.size();j++){
               if(idObjectA[i].equals(idObjectB[j]) && idObjectB[i].equals(idObjectA[j]) && relationAB[j].contains("rightof")){
                  return false;
               }
               if(idObjectA[i].equals(idObjectA[j]) && idObjectB[i].equals(idObjectB[j]) && (relationAB[j].contains("leftof") || relationAB[j].contains("under") || relationAB[j].contains("above") || relationAB[j].contains("ontop"))){
                  return false;
               }
               if(idObjectA[i].equals(idObjectB[j]) && idObjectB[i].equals(idObjectA[j]) && (relationAB[j].contains("under") || relationAB[j].contains("above") || relationAB[j].contains("inside") || relationAB[j].contains("ontop"))){
                  return false;
               }                               
            }            
         }


     
         /////////////ENDS////////////////
      

         
      }
      return true;
   }
}


