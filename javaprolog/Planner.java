
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
          
          if (plan.size()>100){
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


   public Plan solve2(Goal goal,JSONObject result){
      Plan plan = new Plan();
      
      if (goal.fulfilled(world,holding)){
         plan.add("No plan needed");
         return plan;
      }
      Set<State> visitedWorlds = new HashSet<State>();
      Queue stateQueue = new LinkedList<TreeState>();
      State initState = new State(world,holding);
      TreeState initTreeState = new TreeState(initState);
      visitedWorlds.add(initState);
      stateQueue.add(initTreeState);
      TreeState goalState=null;
      int thousands = 1;
      while(!stateQueue.isEmpty() && goalState==null) {
         TreeState currentState = (TreeState)stateQueue.remove();
         for (int j=0;j<world.size();j++){
            //Loop over columns
            State tempState = new State(currentState.getState());
            String tempMove = tempState.pickDropColumn(j);
            
            if(visitedWorlds.size()>1000*thousands){
               System.out.println("Nr of States: "+1000*thousands);
               thousands++;
            }
            if(!tempState.equals(currentState.getState())){
//                if(tempMove.contains("drop")){
//                   if(!visitedWorlds.contains(tempState)){
//                      visitedWorlds.add(tempState);
//                      TreeState newState = new TreeState(tempState,currentState, tempMove);
//                      if(goal.fulfilled(tempState))
//                         goalState = newState;
//                      else
//                         stateQueue.add(newState);
//                   }                
//                }
               if(Constraints.isColumnAllowed(tempState.getColumn(j),objects)){
                  if(!visitedWorlds.contains(tempState)){
                     visitedWorlds.add(tempState);
                     TreeState newState = new TreeState(tempState,currentState, tempMove);
                     if(goal.fulfilled(tempState))
                        goalState = newState;
                     else
                        stateQueue.add(newState);
                  }               
               }
            }
         }
      }
      // BFS uses Queue data structure     
		
		if(goalState!=null){
         TreeState currentState = goalState;
         while(currentState.getParent()!=null){
            plan.add(0,currentState.getMove());
            currentState = currentState.getParent();
         }
      }
      instances = visitedWorlds.size();    
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

		while(!stateStack.isEmpty() && (!foundGoalstate || numberOfFoundGoalStates<1 )) {
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
   
   public Plan solve5(Goal goalIn,JSONObject result){
      Goal goal = new Goal(goalIn);
      State startState = new State(world,holding);
      PlanTree2 planningTree = new PlanTree2(startState, goal, objects);
      Plan plan = planningTree.getPlan();
      return plan;   
   }
   
   public Plan solve6(Goal goalIn,JSONObject result){
      
      State startState = new State(world,holding);
      PlanTree3 planningTree = new PlanTree3(startState, new Goal(goalIn), objects);
      Plan plan = planningTree.getPlan();
      return plan;   
   }
   
   private class TreeState{
      private TreeState parent;
      private String myMove;
      private State state;
      
      private TreeState(State stateIn, TreeState parentIn, String move){
         state = stateIn;
         parent = parentIn;
         myMove = move;      
      }
      
      private TreeState(State stateIn){
         state = stateIn;
         parent = null;
         myMove = null;      
      }
      
      private State getState(){
         return state;
      }
      
      private String getMove(){
         return myMove;
      }
      
      private TreeState getParent(){
         return parent;
      }
   }
}

