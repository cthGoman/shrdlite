import java.util.*;
import org.json.simple.JSONObject;


public class PlanTreeState3 implements Comparable<PlanTreeState3>{
   private State state;
   private PlanTreeState3 parentState;
   private Set<PlanTreeState3> childrenStates;
   private Integer depth;
   private Heuristic4 stateHeu;
   private String myMove;
   private boolean isEvaluated;
   private boolean isAllowed;
   private boolean isSolution;
   private boolean hasParent;
   private PlanTree3 myTree;
   
   public PlanTreeState3(State inState, Goal goal, JSONObject objects, PlanTree3 treeIn){
      this(inState, goal, objects, treeIn, true);
   }
   
   public PlanTreeState3(State inState, Goal goal, JSONObject objects, PlanTree3 treeIn, boolean withHeu){
      state = inState;
      myTree = treeIn;
      depth = 0;
      hasParent = false;
      parentState = this;
      isSolution = false;
      if(withHeu){
         stateHeu = new Heuristic4(inState);
      }
      childrenStates = new HashSet<PlanTreeState3>();
      if (Constraints.isWorldAllowed(inState, objects)){
         if(withHeu)
            stateHeu = new Heuristic4(inState, goal, objects);
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

   PlanTreeState3(State inState, PlanTreeState3 parentState, Goal goal, JSONObject objects, String move, TreeSet<PlanTreeState3> unevaluatedStates, PlanTree3 treeIn){
      this(inState, parentState, goal, objects, move, unevaluatedStates, treeIn, true);
   }

   public PlanTreeState3(State inState, PlanTreeState3 parentState, Goal goal, JSONObject objects, String move, TreeSet<PlanTreeState3> unevaluatedStates, PlanTree3 treeIn, boolean withHeu){
      state = inState;
      myTree = treeIn;
      hasParent = false;
      myMove = move;
      isSolution = false;
      if(withHeu){
         stateHeu = new Heuristic4(inState);
      }
      setParent(parentState, unevaluatedStates);
      childrenStates = new HashSet<PlanTreeState3>();
      if (Constraints.isWorldAllowed(inState, objects)){
         if(withHeu)
            stateHeu = new Heuristic4(inState, goal, objects);
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
               for(PlanTreeState3 childState : childrenStates){
                  if(childState.isAllowed() && !childState.isSolution()){
                     if(childState.isEvaluated()){
                        childState.setEvaluated(nowEvaluated, maxCost);
                     }
                  }
               }
         }
         else if(!isEvaluated && nowEvaluated){
            isEvaluated = nowEvaluated;
            for(PlanTreeState3 childState : childrenStates){
               
               if(!childState.isEvaluated()){
                  childState.setEvaluated(nowEvaluated, maxCost);
               }
            }
         }
         isEvaluated = nowEvaluated;
      }
      else{
         isEvaluated = true;
         for(PlanTreeState3 childState : childrenStates){
               
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
   
   public Heuristic4 getHeuristic(){
      return stateHeu;
   }
   
   public PlanTreeState3 getParent(){
      return parentState;
   }
   
   public Set<PlanTreeState3> getChildren(){
      return childrenStates;
   }
   
   public void setParent(PlanTreeState3 newParent, TreeSet<PlanTreeState3> unevaluatedStates){
//       if(hasParent){
//          parentState.removeChild(this);
//       }
      parentState = newParent;
      parentState.addChild(this);
      hasParent = true;
      if (unevaluatedStates!=null){
         setDepth(parentState.getDepth()+1,unevaluatedStates);
      }
      else
         setDepth(parentState.getDepth()+1);
   }
   
   public void addChild(PlanTreeState3 child){
      childrenStates.add(child);
   }
   
   public void setDepth(int newDepth, TreeSet<PlanTreeState3> unevaluatedStates){
      depth = newDepth;
      if(unevaluatedStates.contains(this)){
         unevaluatedStates.remove(this);
         unevaluatedStates.add(this);
      }
      if(childrenStates!=null){
         for(PlanTreeState3 childState : childrenStates){
            if(childState.getParent().getState().equals(state))
               childState.setDepth(newDepth+1, unevaluatedStates);
            else if(childState.getParent().getDepth()>depth)
               childState.setParent(this, unevaluatedStates);
         }
      }
   }
   
   public void setDepth(int newDepth){
      depth = newDepth;
   }
   
   public void removeChild(PlanTreeState3 child){
      childrenStates.remove(child);
   }
   
   public boolean hasUnEvaluatedChild(){
      if(childrenStates!=null){
         for(PlanTreeState3 childState : childrenStates){
            if(!childState.isEvaluated()){
               return true;
            }
         }
      }
      return false;
      
   }
   public boolean hasUnevaluatedChild(){
      if(childrenStates!=null){
         for(PlanTreeState3 childState : childrenStates){
            if(!childState.isEvaluated()){
               return true;
            }
         }
      }
      
      return false;
      
   }
   
   public PlanTreeState3 getBestUnevaluatedChild(){
      PlanTreeState3 bestChildState = this;
      
      for(PlanTreeState3 childState : childrenStates){
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
   public int compareTo(PlanTreeState3 stateIn){
      if(depth+stateHeu.getCost(depth, myTree.maxCost()) < stateIn.getDepth()+stateIn.getHeuristic().getCost(stateIn.getDepth(), myTree.maxCost())){
         return -1;
      }
      else if(depth+stateHeu.getCost(depth, myTree.maxCost()) == stateIn.getDepth()+stateIn.getHeuristic().getCost(stateIn.getDepth(), myTree.maxCost())){
         if(depth==stateIn.getDepth() && moveDist()==stateIn.moveDist()){
            
               if(state.hashCode()<stateIn.getState().hashCode())
                  return -1;
               else if(state.hashCode()>stateIn.getState().hashCode())
                  return 1;
               else if(stateIn.getState().equals(state))
                  return 0;
         }
         else if(depth==stateIn.getDepth() && moveDist()<stateIn.moveDist()){
            return -1;
         }
         else if(depth==stateIn.getDepth() && moveDist()>stateIn.moveDist()){
            return 1;
         }
         else if(depth>stateIn.getDepth()){
            return -1;
         }
      }
      return 1;
   }
   
   public int moveDist(){
      return Math.abs(parentState.getState().getRobotPos()-state.getRobotPos());
   }
   public int getTotMoveDist(){
      int totMoveDist = 0;
      PlanTreeState3 currentState = this;
      while(currentState.hasParent()){
         totMoveDist +=currentState.moveDist();
         currentState = currentState.getParent();
      }
      return totMoveDist;
   }
}