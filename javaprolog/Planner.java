
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.*;
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
	public Plan solve(Goal goal,JSONObject result){
      Plan plan=new Plan();
      
      JSONArray goalWorld = GenerateAndTest.generateWorld(goal,world,holding,objects,10000);
      String goalHolding = GenerateAndTest.generateGoalHolding(goal);
      result.put("output", ""+goalWorld);
//       System.out.println("goalWorld " + goalWorld);
//       System.out.println("goalHolding " + goalHolding);
//       

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
         Heuristic bestPick = new Heuristic(100);
         
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

               
               //Check if the current pick is the best
               if (currPick.isBetter(bestPick)){
                  bestPickColumn=j;
                  bestPick=currPick;
               }  
            }
            
          }
          
          //Update the world according to the best pick
          actHolding= WorldFunctions.getTopObjectWorldColumn(actWorld,bestPickColumn);
          WorldFunctions.removeTopObjectWorldColumn(actWorld,bestPickColumn);
          
          //Add to plan
          plan.add("pick " + bestPickColumn);
          
         
         //Check if goal is satisfied
         if (actWorld.equals(goalWorld) && actHolding.equals(goalHolding)){
            break;
          }
          
          
         bestDropColumn = 0;
         Heuristic bestDrop = new Heuristic(100);
         foundDrop = false;
         
         for (int j=0;j<world.size();j++){
            //Loop over columns
            
            
            //Check which column to drop
            String tempHolding = actHolding;
            JSONArray tempWorld = WorldFunctions.copy(actWorld);

            
            if (!tempHolding.isEmpty()) {
               //If an object is hold
               
               
               WorldFunctions.addObjectWorldColumn(tempHolding,tempWorld,j);
               tempHolding= "";
               
               Heuristic currDrop = new Heuristic(tempWorld,tempHolding,goalWorld,goalHolding);

               
               if (currDrop.isBetter(bestDrop) && j!=bestPickColumn && Constraints.isWorldAllowed(tempWorld,tempHolding,objects)){
                  bestDropColumn=j;
                  bestDrop=currDrop;
                  foundDrop = true;
               }
                 
            }
            
          }
          
          
          if (foundDrop){
             WorldFunctions.addObjectWorldColumn(actHolding,actWorld,bestDropColumn);
             actHolding= "";
             plan.add("drop " + bestDropColumn);
          }
          
          if (actWorld.equals(goalWorld)){
            break;
          }
          
          if (plan.size()>30){
            plan= new Plan();
            result.put("output", ""+goalWorld+" Planning error");
            break;
          }

      }
      
      
      if (plan.isEmpty()){
         plan.add("No plan needed");
      }
		return plan;
	}
   
   
   
   public Plan solve2(Goal goal,JSONObject result){
      //DFS lowest cost first
      Stack stateStack = new Stack();
      Stack planStack = new Stack();
      Set<JSONArray> visitedWorlds = new HashSet<JSONArray>();
      boolean foundGoalstate = false;
   
		stateStack.push(world);
		visitedWorlds.add(world);
      

		while(!stateStack.isEmpty() && !foundGoalstate) {
         JSONArray state = (JSONArray) stateStack.peek();
			JSONArray child  = (JSONArray) WorldFunctions.getUnvisitedWorld(state,visitedWorlds,objects);
         
         System.out.println("child " + child);
			if(child != null) {
            visitedWorlds.add(child);
				stateStack.push(child);
            foundGoalstate = goal.fulfilled(child);
            System.out.println("child " + child);
            // System.out.println("visitedWorlds " + visitedWorlds);
			}
			else {
				stateStack.pop();
			}
		}
      
      JSONArray goalWorld = (JSONArray) stateStack.peek();
      
      System.out.println("goalWorld " + goalWorld);
      
      return new Plan();
   }
}