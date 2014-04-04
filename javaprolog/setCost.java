import org.json.simple.*;
import java.util.List;
import java.util.ArrayList;
     
public class setCost{
private ArrayList<Integer> objCosts;
private JSONObject obj;
      
   public setCost(JSONObject objects,ArrayList<Integer> costList) {
      obj = objects;
      objCosts = costList;
   }
         
   public setCost(JSONObject objects,Integer initCost) {
      obj = objects;
      objCosts = new ArrayList<Integer>(objects.size());
      for(int i = 0; i < obj.size(); i++){
         objCosts.add(initCost);
      }
   }
   
   public int evaluateMove(String object, JSONArray col){
   
   
   }
}