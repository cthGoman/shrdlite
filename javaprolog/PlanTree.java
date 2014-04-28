import java.util.*;
import org.json.simple.JSONObject;

public class PlanTree{
   private Set<PlanTreeState> maxedStates;

   private HashMap<State,PlanTreeState> stateMap;
   private Set<PlanTreeState> solutions;
   private PlanTreeState currentState;
   private Goal goal;
   private JSONObject objects;
   private Plan plan;
   private int previousMaxCost;
      
   public PlanTree(State initState, Goal goalIn, JSONObject objectsIn ){
      maxedStates = new HashSet<PlanTreeState>();
      
      plan = new Plan();
      stateMap = new HashMap<State, PlanTreeState>();
      solutions = new HashSet<PlanTreeState>();
      goal = goalIn;
      objects = objectsIn;
      PlanTreeState initPlanState = new PlanTreeState(initState, goal, objects);
      stateMap.put(initState,initPlanState);
      
      currentState = initPlanState;
      if(currentState.isSolution()){
         plan.add("No plan needed");
      }
      else{
         generateChildren(currentState);
//          int maxDepth = 100;
         
         while(currentState.hasUnevaluatedChild() || currentState.hasParent()){
            // System.out.println("Depth: "+currentState.getDepth()+" cost: "+currentState.getHeuristic().getCost());
            // System.out.println("Max cost: " +maxCost());
            // System.out.println("Depth: "+currentState.getDepth()+" Nr of unevaluated States: " + nrOfUnevaluatedStates()+ " Total Nr of States: " + stateMap.size());
            if(!currentState.hasUnevaluatedChild()){
               currentState.setEvaluated(true, maxCost());
               currentState = currentState.getParent();
            }
//             else if(currentState.getDepth()>maxDepth){
//                maxedStates.add(currentState);
//                currentState.setEvaluated(true, maxCost());
//                currentState = currentState.getParent();
//             }            
            else if(maxCost() > currentState.getBestUnevaluatedChild().getHeuristic().getCost()+currentState.getBestUnevaluatedChild().getDepth()){
               currentState = currentState.getBestUnevaluatedChild();
               if(currentState.getChildren().size()<1)
                  generateChildren(currentState);
            }
            else{
               currentState.setEvaluated(true, maxCost());
               if(currentState.hasParent())
                  currentState = currentState.getParent();
            }
         
         }
         
         generatePlan();
      }
      
   }

   public Plan getPlan(){
      return plan;
   }
   
   public ArrayList<Integer> getCosts(){
      
      ArrayList<Integer> costList = new ArrayList<Integer>();
      PlanTreeState currentState = getBestSolution();
      
      if(getBestSolution()!=null){
         while(currentState.hasParent()){
            costList.add(0,currentState.getHeuristic().getCost());;
            currentState = currentState.getParent();
         }
      }   

   return costList;
   }
   
   private void generateChildren(PlanTreeState treeState){
      
      for(int i=0; i < treeState.getState().getWorld().size(); i++){ //loop over every column for pick/drop
         if(i!=currentState.getState().getRobotPos()){
            State tempState = new State(treeState.getState());
            String tempMove;
            tempMove = tempState.pickDropColumn(i); 
   
            if(!containState(tempState)){ //check if the PlanTree already contains the state
               if(Constraints.isWorldAllowed(tempState, objects)){
                  PlanTreeState tempPlanTreeState = new PlanTreeState(tempState, treeState, goal, objects, tempMove, maxCost()); //add it if not
                  stateMap.put(tempState,tempPlanTreeState);
                  if(tempPlanTreeState.isSolution()){
      //                System.out.println("New solution: "+tempPlanTreeState.getDepth());
                     solutions.add(tempPlanTreeState);
                  }
               }
            }
            else if(treeState.getDepth()<stateMap.get(tempState).getDepth()){ //add it if it previously was in a deeper layer
               int before = stateMap.size();
               PlanTreeState tempPlanTreeState = stateMap.get(tempState);
               tempPlanTreeState.setParent(treeState, maxCost());
               tempPlanTreeState.setMove(tempMove);
               if(tempPlanTreeState.getDepth()+tempPlanTreeState.getHeuristic().getCost()<maxCost()){
                  if(!tempPlanTreeState.isSolution()){
                     tempPlanTreeState.setEvaluated(false, maxCost());
                  }
               }
            }
         }
      }
   }
   
   private void generatePlan(){
      PlanTreeState currentState = getBestSolution();
      
      if(currentState!=null){
         while(currentState.hasParent()){
            plan.add(0,currentState.getMove());
            currentState = currentState.getParent();
         }
      }
   }
   
   
   private boolean containState(State inState){
      for(State keyState : stateMap.keySet()){
         if(keyState.equals(inState)){
            return true;
         }
      }
      return false;
   
   }
   
   private int maxCost(){
      PlanTreeState solution = getBestSolution();
      if(solution.isSolution()){
         if(solution.getDepth()!=previousMaxCost){
//             System.out.println("Lowered Max cost: "+solution.getDepth()+" Current Nr of States: " + stateMap.size());
            previousMaxCost = solution.getDepth();
            
//             int nrOfFives=0;
//             for(PlanTreeState tempState:stateMap.values()){
//                if(tempState.getChildren().size()==5)
//                   nrOfFives++;
//             }
         }
         return solution.getDepth()-1;
      }
      else{
         previousMaxCost = 10000;
         return 10000;
      }
   }
   
   private PlanTreeState getBestSolution(){
      PlanTreeState bestSolution = currentState;
      if(solutions!=null){
         
         for(PlanTreeState tempSolution : solutions){
            if(bestSolution.isSolution()){

               if(tempSolution.getDepth()<bestSolution.getDepth())
                  bestSolution = tempSolution;
            }
            else{
               bestSolution = tempSolution;
            }
         }
      }
      return bestSolution;
   }

   private int nrOfUnevaluatedStates(){
      int nrOfEvaluatedStates = 0;
      for(PlanTreeState tempPlanState : stateMap.values()){
         if(!tempPlanState.isEvaluated()){
            nrOfEvaluatedStates++;
         }
      }
      return nrOfEvaluatedStates;
   }

}