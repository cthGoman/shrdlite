
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
	public Plan solve(Goal g,JSONArray goalWorld, String goalHolding){
      Plan plan=new Plan();
      
      String actHolding = holding;
      
      JSONArray actWorld = WorldFunctions.copy(world);
            
      int bestPickColumn = 0;
      int bestDropColumn = 0;
      
      ArrayList<Integer> bannedPickColumns = new ArrayList<Integer>();
      
      boolean foundDrop = true;
      while (!actWorld.equals(goalWorld)){
         
         
            
         if (!foundDrop){
            plan.remove(plan.size()-1);
            bannedPickColumns.add(bestPickColumn);
         }
         else{
            bannedPickColumns = new ArrayList<Integer>();
         }
         
         bestPickColumn = 0;
         Heuristic bestPick = new Heuristic(100,100);
         
         for (int j=0;j<world.size();j++){
            //Loop over columns
            
            
            //Check which object to pick
            String tempHolding = actHolding;
            JSONArray tempWorld = WorldFunctions.copy(actWorld);
            
         
            
            
            if (((JSONArray) tempWorld.get(j)).size()>0) {
               //If current column has an object
               
               //Pick up the top object in column j
               tempHolding= WorldFunctions.getTopObjectWorldColumn(tempWorld,j);
               WorldFunctions.removeTopObjectWorldColumn(tempWorld,j);
               
               //Calculate the cost for the current pick
               Heuristic currPick = new Heuristic(tempWorld,tempHolding,goalWorld,goalHolding);

               
               
               if (currPick.isBetter(bestPick)){
                  bestPickColumn=j;
                  bestPick=currPick;
               }  
            }
            
          }
          
          actHolding= (String) ((JSONArray) actWorld.get(bestPickColumn)).get(((JSONArray) actWorld.get(bestPickColumn)).size()-1);
          ((JSONArray) actWorld.get(bestPickColumn)).remove(((JSONArray) actWorld.get(bestPickColumn)).size()-1);
          
          plan.add("pick " + bestPickColumn);
          
         
         
         if (actWorld.equals(goalWorld) && actHolding.equals(goalHolding)){
            break;
          }
          
          
         bestDropColumn = 0;
         Heuristic bestDrop = new Heuristic(100,100);
         foundDrop = false;
         
         for (int j=0;j<world.size();j++){
            //Loop over columns
            
            
            //Check which column to drop
            String tempHolding = actHolding;
            JSONArray tempWorld = WorldFunctions.copy(actWorld);

            
            if (!tempHolding.isEmpty()) {
               //If an object is hold
               
               
               
               ((JSONArray) tempWorld.get(j)).add(tempHolding);
               tempHolding= "";
               
               Heuristic currDrop = new Heuristic(tempWorld,tempHolding,goalWorld,goalHolding);

               
               if (currDrop.isBetter(bestDrop) && j!=bestPickColumn && Constraints.isWorldAllowed(tempWorld,tempHolding,objects)){
                  bestDropColumn=j;
                  bestDrop=currDrop;
                  foundDrop = true;
               }
                  
//                   System.out.println("currDropCost " + currDrop.getCost());
//                   System.out.println("tempHolding "+tempHolding);
//                   System.out.println("tempWorld "+tempWorld);
//                   System.out.println("current column " +j);
            }
            
          }
          
//              System.out.println("bestDropColumn " + bestDropColumn);
//              System.out.println("");
//              System.out.println(world);
//              System.out.println(goalWorld);
          
          
          if (foundDrop){
             ((JSONArray) actWorld.get(bestDropColumn)).add(actHolding);
             actHolding= "";
             plan.add("drop " + bestDropColumn);
          }
          
//              System.out.println(plan);
          if (actWorld.equals(goalWorld) || plan.size()>30){
            break;
          }
          
          
         
         
      
   
      
      }
      
      
   
		return plan;
	}
}