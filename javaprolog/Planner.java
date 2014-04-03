
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
      Plan plan = new Plan();
      JSONArray goalWorld = new JSONArray();
      if (!holding.isEmpty()){
         for (int j=0;j<world.size();j++){
            //Loop over columns
            
            JSONArray tempWorld = WorldFunctions.copy(world);
            WorldFunctions.addObjectWorldColumn(holding,tempWorld,4);
            if (Constraints.isWorldAllowed(tempWorld,"",objects))
              world=tempWorld;
          }
      }
      
      // BFS uses Queue data structure     
		Queue stateQueue = new LinkedList();
		stateQueue.add(world);
      Set<JSONArray> visitedWorlds = new HashSet<JSONArray>();
      boolean foundGoalstate = false;
		
		visitedWorlds.add(world);
      
      long endTime   = System.currentTimeMillis();
      long totalTime = endTime - Shrdlite.startTime;
      System.out.println("before BFS " + totalTime);
      
		while(!stateQueue.isEmpty() && !foundGoalstate) {
         JSONArray state = (JSONArray) stateQueue.remove();
			JSONArray child=null;
			while((child=WorldFunctions.getUnvisitedWorld(state,visitedWorlds,objects))!=null) {
				visitedWorlds.add(child);
				stateQueue.add(child);
            foundGoalstate = goal.fulfilled(child,"");
            if (foundGoalstate)
               goalWorld = child;
			}
		}
      
      System.out.println("number of visited worlds " + visitedWorlds.size());
      
      
      if (!holding.isEmpty()){
         int bestDropColumn = 0;
         Heuristic bestDrop = new Heuristic(100);
         boolean foundDrop = false;
         
         for (int j=0;j<world.size();j++){
            //Loop over columns
            
            
            //Check which column to drop
            String tempHolding = holding;
            JSONArray tempWorld = WorldFunctions.copy(world);

            
            if (!tempHolding.isEmpty()) {
               //If an object is hold
               
               
               WorldFunctions.addObjectWorldColumn(tempHolding,tempWorld,j);
               tempHolding= "";
               
               Heuristic currDrop = new Heuristic(tempWorld,tempHolding,goalWorld,"");

               
               if (currDrop.isBetter(bestDrop) && Constraints.isWorldAllowed(tempWorld,tempHolding,objects)){
                  bestDropColumn=j;
                  bestDrop=currDrop;
                  foundDrop = true;
               }
                 
            }
            
          }
          if (foundDrop){
            plan.add("drop " + bestDropColumn);
          }
          else{
            plan.add("planning error.");
            return plan;
          }
          
      }
      
      endTime   = System.currentTimeMillis();
      totalTime = endTime - Shrdlite.startTime;
      System.out.println("before DFS after BFS " + totalTime);
      //DFS lowest cost first
      Stack stateStack = new Stack();
      Stack planStack = new Stack();
      visitedWorlds = new HashSet<JSONArray>();
      foundGoalstate = false;
      int[] pickFrom = {0};
      int[] dropIn = {0};
      
		stateStack.push(world);
		visitedWorlds.add(world);
      

		while(!stateStack.isEmpty() && !foundGoalstate) {
         JSONArray state = (JSONArray) stateStack.peek();
			JSONArray child  = (JSONArray) WorldFunctions.getBestUnvisitedWorld(state,goalWorld,visitedWorlds,objects,pickFrom,dropIn);
         
			if(child != null) {
            visitedWorlds.add(child);
				stateStack.push(child);
            foundGoalstate = goal.fulfilled(child,"");
            plan.add("pick " + pickFrom[0]);
            plan.add("drop " + dropIn[0]);
			}
			else {
				stateStack.pop();
			}
		}
      
      endTime   = System.currentTimeMillis();
      totalTime = endTime - Shrdlite.startTime;
      System.out.println("after DFS " + totalTime);
      
      return plan;
   }
   
   
   
   public Plan solve3(Goal goal,JSONObject result){
      Plan plan = new Plan();
      
      
      //DFS lowest cost first
      Stack stateStack = new Stack();
      Stack planStack = new Stack();
      Set visitedWorlds = new HashSet<JSONArray>();
      boolean foundGoalstate = false;
      int[] pickFrom = {0};
      int[] dropIn = {0};
      
		stateStack.push(world);
		visitedWorlds.add(world);
      

		while(!stateStack.isEmpty() && !foundGoalstate) {
         JSONArray state = (JSONArray) stateStack.peek();
			JSONArray child  = (JSONArray) WorldFunctions.getBestUnvisitedWorld2(state,goal,visitedWorlds,objects,pickFrom,dropIn);
         
			if(child != null) {
            visitedWorlds.add(child);
				stateStack.push(child);
            foundGoalstate = goal.fulfilled(child,"");
            plan.add("pick " + pickFrom[0]);
            plan.add("drop " + dropIn[0]);
			}
			else {
				stateStack.pop();
			}
		}
      
      return plan;
   }
}