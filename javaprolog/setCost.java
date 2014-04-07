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
         
   
   public int evaluateMove(String object, JSONArray col){
   
   return 0;
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
   
   }
}