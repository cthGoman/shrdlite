import java.util.*;
import org.json.simple.JSONObject;

public class PlanTree2{

   private HashMap<State,PlanTreeState2> stateMap;
   private Set<PlanTreeState2> solutions;
   private PlanTreeState2 currentState;
   private Goal goal;
   private JSONObject objects;
   private Plan plan;
   private int previousMaxCost;
   private TreeSet<PlanTreeState2> unevaluatedStates;
      
   public PlanTree2(State initState, Goal goalIn, JSONObject objectsIn ){
      
      plan = new Plan();
      stateMap = new HashMap<State, PlanTreeState2>();
      solutions = new HashSet<PlanTreeState2>();
      unevaluatedStates = new TreeSet<PlanTreeState2>();
      goal = goalIn;
      objects = objectsIn;
      PlanTreeState2 initPlanState = new PlanTreeState2(initState, goal, objects);
      stateMap.put(initState,initPlanState);
      unevaluatedStates.add(initPlanState);
      if(initPlanState.isSolution()){
         plan.add("No plan needed");
      }
      else{

//          int maxDepth = 100;

         while(unevaluatedStates.first().getDepth()+unevaluatedStates.first().getHeuristic().getCost() < maxCost()){
//             System.out.println("Depth: "+ unevaluatedStates.first().getDepth()+ " Cost: "+ unevaluatedStates.first().getHeuristic().getCost()+" maxCost: "+maxCost());
//             System.out.println("currentState: "+unevaluatedStates.first().getState());
            PlanTreeState2 tempState = unevaluatedStates.first();
            generateChildren(unevaluatedStates.pollFirst());
            
         }
         
         generatePlan();
      }
      
   }

   public Plan getPlan(){
      return plan;
   }
   
   public ArrayList<Integer> getCosts(){
      
      ArrayList<Integer> costList = new ArrayList<Integer>();
      PlanTreeState2 currentState = getBestSolution();
      
      if(getBestSolution()!=null){
         while(currentState.hasParent()){
            costList.add(0,currentState.getHeuristic().getCost());;
            currentState = currentState.getParent();
         }
      }   

   return costList;
   }
   
   private void generateChildren(PlanTreeState2 treeState){
      
      
      for(int i=0; i < treeState.getState().getWorld().size(); i++){ //loop over every column for pick/drop

         State tempState = new State(treeState.getState());
         
         String tempMove = tempState.pickDropColumn(i); 
         
         
         
         if(Constraints.isWorldAllowed(tempState, objects) && !treeState.getState().equals(tempState)){

            if(!stateMap.containsKey(tempState)){ //check if the PlanTree already contains the state
               PlanTreeState2 tempPlanTreeState = new PlanTreeState2(tempState, treeState, goal, objects, tempMove, unevaluatedStates); //add it if not
               stateMap.put(tempState,tempPlanTreeState);
               if(tempPlanTreeState.isSolution()){
   //                System.out.println("New solution: "+tempPlanTreeState.getDepth());
                  solutions.add(tempPlanTreeState);
               }
               else{
                  unevaluatedStates.add(tempPlanTreeState);
               }
               
            }
            else if(treeState.getDepth()<stateMap.get(tempState).getDepth()){ //add it if it previously was in a deeper layer
               PlanTreeState2 tempPlanTreeState = stateMap.get(tempState);
               tempPlanTreeState.setParent(treeState, unevaluatedStates);
               tempPlanTreeState.setMove(tempMove);
               tempPlanTreeState.getState().setRobotPos(i);
            }
            else{
               PlanTreeState2 tempPlanTreeState = stateMap.get(tempState);
               treeState.addChild(tempPlanTreeState);
            }
         }   
         
      }
   }
   
   private void generatePlan(){
      PlanTreeState2 currentState = getBestSolution();
      
      if(currentState!=null){
         while(currentState.hasParent()){
//             System.out.print(" "+currentState.getHeuristic().getCost());
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
      PlanTreeState2 solution = getBestSolution();
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
   
   private PlanTreeState2 getBestSolution(){
      PlanTreeState2 bestSolution = unevaluatedStates.first();      
      if(solutions!=null){
         
         for(PlanTreeState2 tempSolution : solutions){
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
      for(PlanTreeState2 tempPlanState : stateMap.values()){
         if(!tempPlanState.isEvaluated()){
            nrOfEvaluatedStates++;
         }
      }
      return nrOfEvaluatedStates;
   }

}