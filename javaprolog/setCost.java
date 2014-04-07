import org.json.simple.*;
import java.util.*;

     
public class SetCost{
HashMap<String, Integer> costMap;
      

      
   public SetCost(JSONArray world, String holding) {
      
      ArrayList<String> objects = WorldFunctions.getAllObjectsInWorld(world, holding);
      costMap = new HashMap(objects.size());
      for(String obj : objects){
   	   costMap.put(obj, new Integer(0));
   	}
   }
   public SetCost(JSONArray world, String holding,int initCost) {
      
      ArrayList<String> objects = WorldFunctions.getAllObjectsInWorld(world, holding);
      costMap = new HashMap(objects.size());
      for(String obj : objects){
   	   costMap.put(obj, new Integer(initCost));
   	}
   }
         
   

   public void moveAbove(String object, JSONArray col){
      boolean above = false;
      for(int i=0; i<col.size(); i++){
         if(above && costMap.get(col.get(i))<2){
            costMap.put((String)col.get(i),2);
         }
         else if(((String)col.get(i)).contains(object)){
            above = true;
         }
      }
   
   }
   public void move(String object, JSONArray col){
      if(costMap.get(object)<2){
         costMap.put(object,2);
      }
      moveAbove(object, col);
   }
   
   public void doubleMove(String object, JSONArray col){
     
     costMap.put(object,4);
     moveAbove(object, col);
   }

   
   public int evaluateMoveAbove(String object, JSONArray col){
     int moveAboveCost = 0;
     boolean above = false;
     for(int i=0; i<col.size(); i++){
         if(above && costMap.get(col.get(i))<2){
            moveAboveCost += 2;
         }
         else if(((String)col.get(i)).contains(object)){
            above = true;
         }
      }
      return moveAboveCost;
   }
   
   public int evaluateMove(String object, JSONArray col){
      int moveCost = 0;
      if(costMap.get(object)<2){
         moveCost += 2;
      }
      moveCost += evaluateMoveAbove(object, col);
      return moveCost;
   }
   
   public void moveMin(String obj1, String obj2, JSONArray col1, JSONArray col2){
      if(evaluateMove(obj1,col1) < evaluateMove(obj2,col2)){
         move(obj1,col1);
      }
      else{
         move(obj2,col2);
      }
   }
   public int sum(){
      int cost = 0;
      for(Integer value : costMap.values()){
         if(value==-1){
            return 10000;
         }
         cost += value;
      }
      return cost;
   }
}