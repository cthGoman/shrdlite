import java.util.*;
import org.json.simple.JSONObject;


public class PlanTreeState2 implements Comparable<PlanTreeState2>{
   private State state;
   private PlanTreeState2 parentState;
   private Set<PlanTreeState2> childrenStates;
   private Integer depth;
   private Heuristic3 stateHeu;
   private String myMove;
   private boolean isEvaluated;
   private boolean isAllowed;
   private boolean isSolution;
   private boolean hasParent;
   
   public PlanTreeState2(State inState, Goal goal, JSONObject objects){
      state = inState;
      depth = 0;
      hasParent = false;
      parentState = this;
      stateHeu = new Heuristic3(inState);
      childrenStates = new HashSet<PlanTreeState2>();
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
   public PlanTreeState2(State inState, PlanTreeState2 parentState, Goal goal, JSONObject objects, String move, TreeSet<PlanTreeState2> unevaluatedStates){
      state = inState;
      hasParent = false;
      myMove = move;
      stateHeu = new Heuristic3(inState);
      setParent(parentState, unevaluatedStates);
      childrenStates = new HashSet<PlanTreeState2>();
      if (Constraints.isWorldAllowed(inState, objects)){
         stateHeu = new Heuristic3(inState, goal, objects);
//          System.out.println("State: "+state+" cost: "+stateHeu.getCost());
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
               for(PlanTreeState2 childState : childrenStates){
                  if(childState.isAllowed() && !childState.isSolution()){
                     if(childState.isEvaluated()){
                        childState.setEvaluated(nowEvaluated, maxCost);
                     }
                  }
               }
         }
         else if(!isEvaluated && nowEvaluated){
            isEvaluated = nowEvaluated;
            for(PlanTreeState2 childState : childrenStates){
               
               if(!childState.isEvaluated()){
                  childState.setEvaluated(nowEvaluated, maxCost);
               }
            }
         }
         isEvaluated = nowEvaluated;
      }
      else{
         isEvaluated = true;
         for(PlanTreeState2 childState : childrenStates){
               
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
   
   public PlanTreeState2 getParent(){
      return parentState;
   }
   
   public Set<PlanTreeState2> getChildren(){
      return childrenStates;
   }
   
   public void setParent(PlanTreeState2 newParent, TreeSet<PlanTreeState2> unevaluatedStates){
//       if(hasParent){
//          parentState.removeChild(this);
//       }
      parentState = newParent;
      parentState.addChild(this);
      hasParent = true;
      setDepth(parentState.getDepth()+1,unevaluatedStates);

   }
   
   public void addChild(PlanTreeState2 child){
      childrenStates.add(child);
   }
   
   public void setDepth(int newDepth, TreeSet<PlanTreeState2> unevaluatedStates){
      depth = newDepth;
      if(unevaluatedStates.contains(this)){
         unevaluatedStates.remove(this);
         unevaluatedStates.add(this);
      }
      if(childrenStates!=null){
         for(PlanTreeState2 childState : childrenStates){
            if(childState.getParent().getState().equals(state))
               childState.setDepth(newDepth+1, unevaluatedStates);
            else if(childState.getParent().getDepth()>depth)
               childState.setParent(this, unevaluatedStates);
         }
      }
   }
   
   public void removeChild(PlanTreeState2 child){
      childrenStates.remove(child);
   }
   
   public boolean hasUnEvaluatedChild(){
      if(childrenStates!=null){
         for(PlanTreeState2 childState : childrenStates){
            if(!childState.isEvaluated()){
               return true;
            }
         }
      }
      return false;
      
   }
   public boolean hasUnevaluatedChild(){
      if(childrenStates!=null){
         for(PlanTreeState2 childState : childrenStates){
            if(!childState.isEvaluated()){
               return true;
            }
         }
      }
      
      return false;
      
   }
   
   public PlanTreeState2 getBestUnevaluatedChild(){
      PlanTreeState2 bestChildState = this;
      
      for(PlanTreeState2 childState : childrenStates){
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
   
   @Override
   public int compareTo(PlanTreeState2 stateIn){
      if(depth+stateHeu.getCost() < stateIn.getDepth()+stateIn.getHeuristic().getCost()){
         return -1;
      }
      else if(depth+stateHeu.getCost() == stateIn.getDepth()+stateIn.getHeuristic().getCost()){
         if(depth==stateIn.getDepth()){
            if(state.hashCode()<stateIn.getState().hashCode())
               return -1;
            else if(state.hashCode()>stateIn.getState().hashCode())
               return 1;
            else if(stateIn.getState().equals(state))
               return 0;
         }
         else if(depth>stateIn.getDepth()){
            return -1;
         }
      }
      return 1;
   }
}