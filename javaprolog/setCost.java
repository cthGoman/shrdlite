import org.json.simple.*;
import java.util.List;
import java.util.ArrayList;
     
public class SetCost{
HashMap<String, Integer> costMap;
      

      
   public SetCost(JSONArray world, String holding) {
      
      ArrayList<String> objects = getAllObjectsInWorld(world, holding);
      costMap = new HashMap(objects.size());
      for(String obj : objects){
   	   costMap.put(obj, new Integer(0));
   	}
   }
         
   public SetCost(JSONObject objects,Integer initCost) {
      obj = objects;
      objCosts = new ArrayList<Integer>(objects.size());
      for(int i = 0; i < obj.size(); i++){
         objCosts.add(initCost);
      }
   }
   
   public int evaluateMove(String object, JSONArray col){
   
   return 0;
   }
   
   public void moveAbove(String object, JSONArray col){
      boolean above = false;
      for(int i=0; i<col.size(); i++){
         if(above){
            
         }
         else if(((String)col.get(i)).contains(object)){
            above = true;
         }
      }
   
   }
}