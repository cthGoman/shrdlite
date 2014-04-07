
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
      
      if (goal.get(0).get(0).get(0).equals("hold")){
         Goal newGoal = new Goal();
         newGoal.addStatement(0,goal.get(0).get(0));
         goal = newGoal;
      }
      String goalHolding = GenerateAndTest.generateGoalHolding(goal);
      JSONArray goalWorld = GenerateAndTest.generateWorld(goal,world,holding,goalHolding,objects,10000);
      
      result.put("output", ""+goalWorld+ " " + goalHolding);
     

      String actHolding = holding;
      JSONArray actWorld = WorldFunctions.copy(world);
            
      int bestPickColumn = 0;
      int bestDropColumn = 0;
      
      ArrayList<Integer> bannedPickColumns = new ArrayList<Integer>();
      
      boolean foundDrop = true;
      while (!(actWorld.equals(goalWorld) && actHolding.equals(goalHolding) )){
         
         
            
         if (!foundDrop){
            plan.remove(plan.size()-1);
            bannedPickColumns.add(bestPickColumn);
         }
         else{
            bannedPickColumns = new ArrayList<Integer>();
         }
         
         bestPickColumn = 0;
         Heuristic bestPick = new Heuristic(100);
         
         if (actHolding.isEmpty()) {
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
          
         }
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
            result.put("output", ""+goalWorld+" Planning error" + " " + goalHolding);
            break;
          }

      }
      
      
      if (plan.isEmpty()){
         plan.add("No plan needed");
      }
		return plan;
	}



// This is how to get information about the top object in column 1:
//JSONArray column1 = (JSONArray) world.get(0);
//String topobject = (String) column1.get(column1.size() - 1);
//JSONObject objectinfo = (JSONObject) objects.get(topobject);
//String form = (String) objectinfo.get("form");

   
   
   
   public Plan solve2(Goal goal,JSONObject result){
      Plan plan = new Plan();
      
      if (goal.fulfilled(world,holding)){
         plan.add("No plan needed");
         return plan;
      }
      
      JSONArray goalWorld = new JSONArray();
      if (!holding.isEmpty()){
         for (int j=0;j<world.size();j++){
            //Loop over columns
            
            JSONArray tempWorld = WorldFunctions.copy(world);
            WorldFunctions.addObjectWorldColumn(holding,tempWorld,j);
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
      // System.out.println("before BFS " + totalTime);
      
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
      
      // System.out.println("number of visited worlds " + visitedWorlds.size());
      
      
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
            plan.add("planning error." + goalWorld + " ");
            return plan;
          }
          
      }
      
      endTime   = System.currentTimeMillis();
      totalTime = endTime - Shrdlite.startTime;
      // System.out.println("before DFS after BFS " + totalTime);
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
         
         foundGoalstate=false;
         
         // System.out.println("child " + child);
			if(child != null) {
            visitedWorlds.add(child);
				stateStack.push(child);
            foundGoalstate = goal.fulfilled(child,"");
            // System.out.println("foundGoalstate " + foundGoalstate);
            plan.add("pick " + pickFrom[0]);
            plan.add("drop " + dropIn[0]);
			}
			else {
				stateStack.pop();
			}
		}
      
      endTime   = System.currentTimeMillis();
      totalTime = endTime - Shrdlite.startTime;
      // System.out.println("after DFS " + totalTime);
      
      return plan;
   }
   
   
   
   public Plan solve3(Goal goal,JSONObject result){
      Plan tempPlan = new Plan();
      Plan plan = new Plan();
      
      if (goal.fulfilled(world,holding)){
         plan.add("No plan needed");
         return plan;
      }
      boolean found=false;
      int j=0;
      if (!holding.isEmpty()){
         
         
         while (j<world.size() && !found){
            //Loop over columns
            
            JSONArray tempWorld = WorldFunctions.copy(world);
            WorldFunctions.addObjectWorldColumn(holding,tempWorld,j);
            if (Constraints.isWorldAllowed(tempWorld,"",objects)){
              world=tempWorld;
              tempPlan.add("drop "+j);
              found=true;
            }
            j++;
          }
      }
      plan=tempPlan;
      if (goal.fulfilled(world,"")){
         return plan;
      }
      
      
      
      ArrayList<Plan> listOfPlans = new ArrayList<Plan>();
      int numberOfFoundGoalStates = 0;
      

      
      //DFS lowest cost first
      Stack stateStack = new Stack();
      Stack planStack = new Stack();
      Set visitedWorlds = new HashSet<JSONArray>();
      boolean foundGoalstate = false;
      int[] pickFrom = {0};
      int[] dropIn = {0};
      
		stateStack.push(world);
		visitedWorlds.add(world);
      JSONArray state = world;

		while(!stateStack.isEmpty() && (!foundGoalstate || numberOfFoundGoalStates<20 )) {
         if (!foundGoalstate)
            state = (JSONArray) stateStack.peek();
         
         String holding = new String();   
			JSONArray child  = (JSONArray) WorldFunctions.getBestUnvisitedWorld2(state,goal,visitedWorlds,objects,pickFrom,dropIn);
         foundGoalstate=false;
         
         // System.out.println("child " + child);
			if(child != null) {
            visitedWorlds.add(child);
				stateStack.push(child);
            foundGoalstate = goal.fulfilled(child,"");
            // System.out.println("foundGoalstate " + foundGoalstate);
            tempPlan.add("pick " + pickFrom[0]);
            tempPlan.add("drop " + dropIn[0]);
            if (foundGoalstate){
               numberOfFoundGoalStates++;
               listOfPlans.add(tempPlan);
               state=world;
               stateStack= new Stack();
               stateStack.add(state);
               tempPlan =new Plan();
               if (found)
                  tempPlan.add("drop "+j);
            }
            
			}
			else {
				stateStack.pop();
            if (tempPlan.size()>1){
               tempPlan.remove(tempPlan.size()-1);
               tempPlan.remove(tempPlan.size()-1);
            }
			}
		}
      
      if (numberOfFoundGoalStates==0){
            plan = new Plan();
            plan.add("planning error.");
            return plan;
      }
      plan = listOfPlans.get(0);
      for (Plan loopPlan : listOfPlans){
         // System.out.println("lenth of plan "+loopPlan.size());
//          System.out.println("plan "+loopPlan);
         if (loopPlan.size()<plan.size())
            plan=loopPlan;
      }
      return plan;
   }
}

