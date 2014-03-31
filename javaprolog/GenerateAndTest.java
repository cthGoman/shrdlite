import java.util.List;
import java.util.ArrayList;
import org.json.simple.*;

public class GenerateAndTest{

   public static JSONArray generateWorld(Goal goal, JSONArray world, JSONObject objectsIn){
   JSONArray goalWorld = (JSONArray) WorldFunctions.copy(world);
   
   for(int i=0; i < goal.size(); i++){ //Loop over rows in goal
      
      for(int j=0; j<goal.get(i).size(); j++){ //Loop over columns in goal
         
         if (goal.get(i).get(j).get(0).equals("ontop")){ //If it is an ontop statement
            
            for(int k=0; k<world.size(); k++){   //Loop over columns in the world
            
            //Check if the column contains any of the objects in the statement
               if(WorldFunctions.worldColumnContains(world, goal.get(i).get(j).get(1), k) ||
                  WorldFunctions.worldColumnContains(world, goal.get(i).get(j).get(2), k) ||
                  goal.get(i).get(j).get(1).contains(""+k)){
               
               }
            
            }
         
         }
      }
   }
   
   return goalWorld;
   }
   
   
   public static String generateGoalHolding(Goal goals){
   String goalHolding = new String("");
   
   return goalHolding;
   }

}