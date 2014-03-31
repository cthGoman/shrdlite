import java.util.*;
import org.json.simple.*;

public class GenerateAndTest{

   public static JSONArray generateWorld(Goal goal, JSONArray world, String holding, JSONObject objectsIn, int worldIterations){
   JSONArray goalWorld = (JSONArray) WorldFunctions.copy(world);
   ArrayList<String> grabbedObjects = new ArrayList();
   
   for(int i=0; i < goal.size(); i++){ //Loop over rows in goal
      
      
      for(int j=0; j<goal.get(i).size(); j++){ //Loop over columns in goal
         
            
          for(int k=0; k<goalWorld.size(); k++){   //Loop over columns in the world
            
            //Check if the column in the world contains any of the objects in the statement
             if(WorldFunctions.worldColumnContains(goalWorld, goal.get(i).get(j).get(1), k) ||
                WorldFunctions.worldColumnContains(goalWorld, goal.get(i).get(j).get(2), k) ||
                goal.get(i).get(j).get(1).contains(""+k) || goal.get(i).get(j).get(1).contains(""+k)){
                  
                while(((JSONArray) goalWorld.get(k)).size()>0){
                   String tempObject = new String(WorldFunctions.getTopObjectWorldColumn(goalWorld, k));
                   grabbedObjects.add(tempObject);
                   WorldFunctions.removeTopObjectWorldColumn(goalWorld, k);                   
                  
                }
               
             }
            
          }
         
      }
      
   }
   Heuristic goalWorldHeu = new Heuristic(100);
   JSONArray strippedWorld = WorldFunctions.copy(goalWorld);
   
//    System.out.println(grabbedObjects);
   
   for(int i=0;i<=worldIterations;i++){
      ArrayList<String> tempGrabbedObjects = new ArrayList(grabbedObjects);
      JSONArray tempGoalWorld = WorldFunctions.copy(strippedWorld);
      
      for (int j=0; j<grabbedObjects.size(); j++){
         Random generator = new Random();
         int k = generator.nextInt(tempGrabbedObjects.size());
         generator = new Random();
         int c = generator.nextInt(goalWorld.size());
         
         WorldFunctions.addObjectWorldColumn(tempGrabbedObjects.get(k),tempGoalWorld, c);
//          System.out.println("tempGrabbedObjects.get(k) "+tempGrabbedObjects.get(k));
         tempGrabbedObjects.remove(k);
         
//          System.out.println("tempGoalWorld "+tempGoalWorld + " " + c);
         
      }
      Heuristic tempHeu = new Heuristic(world, holding, tempGoalWorld, "");
//       System.out.println("constraints " +Constraints.isWorldAllowed(tempGoalWorld,"",objectsIn));
//       System.out.println("fulfilled " +goal.fulfilled(tempGoalWorld));
//       System.out.println("heuristic " +tempHeu.isBetter(goalWorldHeu));
      
      
      if(Constraints.isWorldAllowed(tempGoalWorld,"",objectsIn) && goal.fulfilled(tempGoalWorld) && tempHeu.isBetter(goalWorldHeu)){
//          System.out.println("kanon!");
         goalWorld = WorldFunctions.copy(tempGoalWorld);
         goalWorldHeu=tempHeu;       
      }
      
   }
   
   return goalWorld;
   }
   
   
   public static String generateGoalHolding(Goal goals){
   String goalHolding = new String("");
   
   return goalHolding;
   }

}