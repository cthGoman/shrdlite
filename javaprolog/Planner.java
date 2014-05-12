
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.*;
import java.util.ArrayList;

public class Planner{
   
   private JSONArray world;
   private String holding;
   private JSONObject objects;
   public static int instances;

	public Planner(JSONArray worldIn, String holdingIn, JSONObject objectsIn){
		world=worldIn;
      holding=holdingIn;
      objects=objectsIn;
	}


  
   
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

		while(!stateStack.isEmpty() && (!foundGoalstate || numberOfFoundGoalStates<10 )) {
         if (!foundGoalstate)
            state = (JSONArray) stateStack.peek();
         
         String holding = new String();   
			JSONArray child  = (JSONArray) WorldFunctions.getBestUnvisitedWorld2(state,goal,visitedWorlds,objects,pickFrom,dropIn);
         foundGoalstate=false;

			if(child != null) {
            visitedWorlds.add(child);
				stateStack.push(child);
            foundGoalstate = goal.fulfilled(child,"");
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
         if (loopPlan.size()<plan.size())
            plan=loopPlan;
      }
      instances = visitedWorlds.size();
      return plan;
   }   
   
   public Plan solve5(Goal goal,JSONObject result){
      State startState = new State(world,holding);
      PlanTree2 planningTree = new PlanTree2(startState, goal, objects);
      Plan plan = planningTree.getPlan();
      return plan;   
   }
   
   public Plan solve6(Goal goal,JSONObject result){
      State startState = new State(world,holding);
      PlanTree3 planningTree = new PlanTree3(startState, goal, objects);
      Plan plan = planningTree.getPlan();
      return plan;   
   }
}

