import java.util.*;
import org.json.simple.JSONObject;


public class PlanTreeState{
   private State state;
   private PlanTreeState parentState;
   private Set<PlanTreeState> childrenStates;
   private Integer depth;
   private Heuristic3 stateHeu;
   private String myMove;
   private boolean isEvaluated;
   private boolean isAllowed;
   private boolean isSolution;
   private boolean hasParent;
   
   public PlanTreeState(State inState, Goal goal, JSONObject objects){
      state = inState;
      depth = 0;
      hasParent = false;
      parentState = this;
      stateHeu = new Heuristic3(inState);
      childrenStates = new HashSet<PlanTreeState>();
      if (Constraints.isWorldAllowed(inState, objects)){
         stateHeu = new Heuristic3(inState, goal, objects);
         isAllowed = true;
         if(goal.fulfilled(state)){
            isSolution = true;
            isEvaluated = true;
            
         }
         else{
            isSolution = false;
         }
      }
      else{
      isAllowed = false;
      isSolution = false;
      isEvaluated = true;
      }
      
   }
   public PlanTreeState(State inState, PlanTreeState parentState, Goal goal, JSONObject objects, String move, int maxCost){
      state = inState;
      hasParent = false;
      myMove = move;
      setParent(parentState, maxCost);
      stateHeu = new Heuristic3(inState);
      childrenStates = new HashSet<PlanTreeState>();
      if (Constraints.isWorldAllowed(inState, objects)){
         stateHeu = new Heuristic3(inState, goal, objects);
         isAllowed = true;
         if(goal.fulfilled(state)){
            isSolution = true;
            isEvaluated = true;
         }
         else{
            isSolution = false;
         }
      }
      else{
      isAllowed = false;
      isSolution = false;
      isEvaluated = true;
      }
      
   }

   public boolean isEvaluated(){
      return isEvaluated;
   }
   
   public void setEvaluated(boolean nowEvaluated, int maxCost){
      if(stateHeu.getCost()+depth < maxCost){
         if(isEvaluated && !nowEvaluated && hasParent){ //if now needs to be reevaluated, so does the parent
            isEvaluated = nowEvaluated;
            parentState.setEvaluated(nowEvaluated, maxCost);
               for(PlanTreeState childState : childrenStates){
                  if(childState.isAllowed() && !childState.isSolution()){
                     if(childState.isEvaluated()){
                        childState.setEvaluated(nowEvaluated, maxCost);
                     }
                  }
               }
         }
         else if(!isEvaluated && nowEvaluated){
            isEvaluated = nowEvaluated;
            for(PlanTreeState childState : childrenStates){
               
               if(!childState.isEvaluated()){
                  childState.setEvaluated(nowEvaluated, maxCost);
               }
            }
         }
         isEvaluated = nowEvaluated;
      }
      else{
         isEvaluated = true;
         for(PlanTreeState childState : childrenStates){
               
            if(!childState.isEvaluated()){
               childState.setEvaluated(nowEvaluated, maxCost);
            }
         }
      }
   }

   public boolean isAllowed(){
      return isAllowed;
   }
   
   public boolean isSolution(){
      return isSolution;
   }

   public boolean hasParent(){
      return hasParent;
   }
   
   public Integer getDepth(){
      return depth;
   }
   
   public String getMove(){
      return myMove;
   }
   
   public State getState(){
      return state;
   }
   
   public Heuristic3 getHeuristic(){
      return stateHeu;
   }
   
   public PlanTreeState getParent(){
      return parentState;
   }
   
   public Set<PlanTreeState> getChildren(){
      return childrenStates;
   }
   
   public void setParent(PlanTreeState newParent, int maxCost){
      if(hasParent){
         parentState.removeChild(this);
      }
      parentState = newParent;
      parentState.addChild(this);
      hasParent = true;
      setDepth(parentState.getDepth()+1);
      
      if(parentState.isEvaluated() && !isEvaluated){ //reevaluate parent if this state needs to be evaluated
         parentState.setEvaluated(isEvaluated, maxCost);
      }
   }
   
   public void addChild(PlanTreeState child){
      childrenStates.add(child);
   }
   
   public void setDepth(int newDepth){
      depth = newDepth;
      if(childrenStates!=null){
         for(PlanTreeState childState : childrenStates){
            childState.setDepth(newDepth+1);
         }
      }
   }
   
   public void removeChild(PlanTreeState child){
      childrenStates.remove(child);
   }
   
   public boolean hasUnEvaluatedChild(){
      if(childrenStates!=null){
         for(PlanTreeState childState : childrenStates){
            if(!childState.isEvaluated()){
               return true;
            }
         }
      }
      return false;
      
   }
   public boolean hasUnevaluatedChild(){
      if(childrenStates!=null){
         for(PlanTreeState childState : childrenStates){
            if(!childState.isEvaluated()){
               return true;
            }
         }
      }
      
      return false;
      
   }
   
   public PlanTreeState getBestUnevaluatedChild(){
      PlanTreeState bestChildState = this;
      
      for(PlanTreeState childState : childrenStates){
         if(!childState.isEvaluated()){
            if(bestChildState.getState().equals(getState())){
               bestChildState = childState;
            }
            else if(childState.getHeuristic().isBetter(bestChildState.getHeuristic())){
               bestChildState = childState;
            }
         }
      }
      
      return bestChildState;
      
   }
   
   public void setMove(String move){
      myMove = move;
   }
}