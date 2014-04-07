import org.json.simple.*;
import java.util.List;
import java.util.ArrayList;
     
public class SetCost{
private ArrayList<Integer> objCosts;
private JSONObject obj;
      

      
   public SetCost(JSONObject objects,ArrayList<Integer> costList) {
      obj = objects;
      objCosts = costList;
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
}