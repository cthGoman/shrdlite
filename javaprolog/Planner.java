
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.List;
import java.util.ArrayList;

public class Planner{
   
   private JSONArray world;
   private String holding;
   private JSONObject objects;

	public Planner(JSONArray worldIn, String holdingIn, JSONObject objectsIn){
		world=worldIn;
      holding=holdingIn;
      objects=objectsIn;
	}
	public Plan solve(Goal g){
      Plan plan=new Plan();
      
      

      
      for (int i=0; i<world.size(); i++)
      {
   
         JSONArray columnTemp = (JSONArray) world.get(i);
         
         if (columnTemp.contains(g.get(0).get(0).get(1)))
         {
            plan.add("pick " + i);
         }

      }
      
      for (int i=0; i<world.size(); i++)
      {
   
         JSONArray columnTemp = (JSONArray) world.get(i);
         
         if (columnTemp.isEmpty())
         {
            plan.add("drop " + i);
            break;
         }

      }

   
		return plan;
	}
}