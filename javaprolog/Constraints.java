import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class Constraints{

	public static boolean isAllowed(JSONArray world, String holding, JSONObject objects) {
	   
      boolean isAllowed = true;
      
      isAllowed = (holding==null || holding.length()<2);
      
      for(int i=0;i<world.length();i++){
      
      
      } 
      
      return isAllowed;
		
	}
}
