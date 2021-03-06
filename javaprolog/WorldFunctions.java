import org.json.simple.*;
import java.util.*;

public class WorldFunctions{

   public static JSONArray copy(JSONArray world){
      
      JSONArray tempWorld = new JSONArray();
      
      for(int k=0;k<world.size();k++){
                 
         JSONArray goalColTemp= new JSONArray();
         goalColTemp.addAll((JSONArray) world.get(k));
      
         tempWorld.add(goalColTemp);
      
      }
   
      return tempWorld;
   }
   
   public static ArrayList<String> getAllObjectsInWorld(JSONArray world,String holding){
      
      ArrayList<String> listOfObjects = new ArrayList<String>();
      
      for(int k=0;k<world.size();k++){
         JSONArray goalColTemp= new JSONArray();
         goalColTemp.addAll((JSONArray) world.get(k));
         for(int i=0; i<((JSONArray)world.get(k)).size();i++){
      
            listOfObjects.add((String)goalColTemp.get(i));
         }
         
      
      }
      if (!holding.isEmpty())
         listOfObjects.add(holding);
      return listOfObjects;
   }
   
   public static String getTopObjectWorldColumn(JSONArray world, int column){
     return (String) ((JSONArray) world.get(column)).get(((JSONArray) world.get(column)).size()-1);
   }
   
   public static void removeTopObjectWorldColumn(JSONArray world, int column){
     ((JSONArray) world.get(column)).remove(((JSONArray) world.get(column)).size()-1);
   }
   
   public static void addObjectWorldColumn( String object,JSONArray world, int column){
     ((JSONArray) world.get(column)).add(object);
   }

   public static boolean worldColumnContains(JSONArray world, String object, int column){
     return ((JSONArray) world.get(column)).contains(object);
   }
   
   public static int getColumnNumber(JSONArray world, String object){
     for(int k=0;k<world.size();k++){
      if (((JSONArray) world.get(k)).contains(object)){
         return k;
      }
      else if(object.contains(String.valueOf(k))){
         return k;
      }
     }
     return -1;
   }
   
   public static int getPlaceInColumn(JSONArray world, String object){
     for(int k=0;k<world.size();k++){
      if (((JSONArray) world.get(k)).contains(object)){
         return ((JSONArray) world.get(k)).indexOf(object);
      }
     }
     return -1;
   }
   
   public static String getObjectBelow(JSONArray world, String object){
     for(int k=0;k<world.size();k++){
      if (((JSONArray) world.get(k)).contains(object)){
         
         int place=((JSONArray) world.get(k)).indexOf(object);
         
         if (place==0){
            return "floor-"+k;
         }else{
            return (String) ((JSONArray) world.get(k)).get(place-1);
         }
         
      }
     }
     return "";
   }
   
   public static JSONArray getUnvisitedWorld(JSONArray world, Set<JSONArray> visitedWorlds, JSONObject objectsIn){
      
      
      JSONArray bestUnvisitedWorld = null;
      
      for (int j=0;j<world.size();j++){
         //Loop over columns

         JSONArray tempWorld = WorldFunctions.copy(world);
         String movedObject = "";
         
         if (((JSONArray) tempWorld.get(j)).size()>0) {
            //If current column has an object
            
            //Pick up the top object in column j
            movedObject= WorldFunctions.getTopObjectWorldColumn(tempWorld,j);
            WorldFunctions.removeTopObjectWorldColumn(tempWorld,j);
            
            for (int i=0;i<world.size();i++){
            
               JSONArray strippedWorld = WorldFunctions.copy(tempWorld);
               //Place the object in each column and test
               WorldFunctions.addObjectWorldColumn(movedObject,strippedWorld,i);
      
               
               //Check if the current pick is the best
               if (!visitedWorlds.contains(strippedWorld) && Constraints.isWorldAllowed(strippedWorld,"",objectsIn)){
                  bestUnvisitedWorld=strippedWorld;
               }
            }     
         }
         
       }
      
      return bestUnvisitedWorld;
   }
   
   public static JSONArray getBestUnvisitedWorld(JSONArray world,JSONArray goalWorld, Set<JSONArray> visitedWorlds, JSONObject objectsIn, int[] pickFrom, int[] dropIn){
      
      
      JSONArray bestUnvisitedWorld = null;
      Heuristic bestPick = new Heuristic(100);
      
      for (int j=0;j<world.size();j++){
         //Loop over columns
         
         
         
         JSONArray tempWorld = WorldFunctions.copy(world);
         String movedObject = "";
         
         if (((JSONArray) tempWorld.get(j)).size()>0) {
            //If current column has an object
            
            //Pick up the top object in column j
            movedObject= WorldFunctions.getTopObjectWorldColumn(tempWorld,j);
            WorldFunctions.removeTopObjectWorldColumn(tempWorld,j);
            
            for (int i=0;i<world.size();i++){
            
               JSONArray strippedWorld = WorldFunctions.copy(tempWorld);
               //Place the object in each column and test
               WorldFunctions.addObjectWorldColumn(movedObject,strippedWorld,i);
               
               
               //Calculate the cost for the current pick
               Heuristic currPick = new Heuristic(strippedWorld,"",goalWorld,"");
      
               
               //Check if the current pick is the best
               if (currPick.isBetter(bestPick) && !visitedWorlds.contains(strippedWorld) && Constraints.isWorldAllowed(strippedWorld,"",objectsIn)){
                  bestUnvisitedWorld=strippedWorld;
                  bestPick=currPick;
                  pickFrom[0]=j;
                  dropIn[0]=i;
               }
            }     
         }
         
       }
      
      return bestUnvisitedWorld;
   }
   
   public static JSONArray getBestUnvisitedWorld2(JSONArray world,Goal goal, Set<JSONArray> visitedWorlds, JSONObject objectsIn, int[] pickFrom, int[] dropIn){
      
      
      JSONArray bestUnvisitedWorld = null;
      Heuristic2 bestPick = new Heuristic2(100);
      
      for (int j=0;j<world.size();j++){
         //Loop over columns
         
         
         
         JSONArray tempWorld = WorldFunctions.copy(world);
         String movedObject = "";
         
         if (((JSONArray) tempWorld.get(j)).size()>0) {
            //If current column has an object
            
            //Pick up the top object in column j
            movedObject= WorldFunctions.getTopObjectWorldColumn(tempWorld,j);
            WorldFunctions.removeTopObjectWorldColumn(tempWorld,j);
            
            for (int i=0;i<world.size();i++){
            
               JSONArray strippedWorld = WorldFunctions.copy(tempWorld);
               //Place the object in each column and test
               WorldFunctions.addObjectWorldColumn(movedObject,strippedWorld,i);
               
               
               //Calculate the cost for the current pick
               Heuristic2 currPick = new Heuristic2(strippedWorld,"",goal,objectsIn);
      
               // System.out.println("strippedWorld " + strippedWorld);
//                System.out.println("cost " + currPick.getCost());
               //Check if the current pick is the best
               if (currPick.isBetter(bestPick) && !visitedWorlds.contains(strippedWorld) && Constraints.isWorldAllowed(strippedWorld,"",objectsIn)){
                  bestUnvisitedWorld=strippedWorld;
                  bestPick=currPick;
                  pickFrom[0]=j;
                  dropIn[0]=i;
               }
            }     
         }
         
       }
      
      return bestUnvisitedWorld;
   }
   
}