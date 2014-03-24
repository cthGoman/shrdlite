
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Planner{
	public Planner(JSONArray world, String holding, JSONObject objects){
		
	}
	public Plan solve(Goal g){
		return new Plan();
	}
}

// This is how to get information about the top object in column 1:
//JSONArray column1 = (JSONArray) world.get(0);
//String topobject = (String) column1.get(column1.size() - 1);
//JSONObject objectinfo = (JSONObject) objects.get(topobject);
//String form = (String) objectinfo.get("form");