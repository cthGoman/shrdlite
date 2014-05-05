import org.json.simple.*;
import java.util.*;


public class Heuristic3{
   private StateCost stateCost;
   private State worldState;
   
   public Heuristic3(State worldState){
      stateCost = new StateCost(worldState, -1);
      
   }

	public Heuristic3(State stateIn, Goal goal, JSONObject objects) {
		worldState = stateIn;
      stateCost = new StateCost(worldState, -1);
      
    for (ArrayList<Statement> listOfStatement : goal){//Loop over all rows 

         StateCost rowStateCost = new StateCost(worldState);
         
         for (Statement statement : listOfStatement) {//Loop over every statement in row
            String obj1 = statement.get(1);
            int columnNr1 = worldState.getColumnNumber(obj1);
            int place1 = worldState.getPlaceInColumn(obj1);
           // ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
            
            String obj2 = statement.get(2);
            int columnNr2 = worldState.getColumnNumber(obj2);
            int place2 = worldState.getPlaceInColumn(obj2);
            // ArrayList<String> column2 = worldState.getWorld().get(columnNr2);


            if ("ontop".equals(statement.get(0).toLowerCase()) || "inside".equals(statement.get(0).toLowerCase())){
               
               if(columnNr1==columnNr2){//in the same column
                  if (!worldState.getObjectBelow(obj1).equals(obj2)){//But not ontop
                     if(place1 < place2){ //object 1 below object 2
                        ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                        rowStateCost.move(obj1,column1);
                     }
                     else{ //move objects above object 2 and move object 1 twice
                        ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                        ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                        rowStateCost.moveAbove(obj2,column2);
                        rowStateCost.doubleMove(obj1,column1);
                     }   
                  }
               }
               else if(columnNr1==-1){//object 1 is in holding
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  
                  if(place2 < 0){
                     if(column2.size()>0){
                        rowStateCost.moveAbove(obj2,column2);
                     }
                     else{
                        rowStateCost.dropObj(obj1);
                     }
                  }
                  else if(column2.get(column2.size()-1).equals(obj2)){
                     rowStateCost.dropObj(obj1);
                  }
                  else{
                     rowStateCost.dropMoveObj(obj1);
                  }
                  rowStateCost.moveAbove(obj2,column2);
               }
               else if(columnNr2==-1){//object 2 is in holding
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  rowStateCost.dropObj(obj2);
                  rowStateCost.move(obj1, column1);
               } 
               else{//objects are in different columns
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  rowStateCost.moveAbove(obj2,column2);
                  rowStateCost.move(obj1,column1);
               }
            }
            else if ("above".equals(statement.get(0).toLowerCase())){
               evaluateAbove(obj1,obj2,objects,rowStateCost,columnNr1,columnNr2,place1,place2);

//                if(columnNr1==columnNr2){//in the same column
//                   if (place1<place2){//But object 1 is below
//                      ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
//                      rowStateCost.move(obj1,column1);
//                   }
//                }
//                else if(columnNr1==-1){//obj 1 in holding
//                   ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
//                   column2.add(obj1);
//                   if(Constraints.isColumnAllowed(column2,objects)){
//                      column2.remove(obj1); 
//                      rowStateCost.dropObj(obj1);
//                   }
//                   else{
//                      column2.remove(obj1); 
//                      rowStateCost.dropMoveObj(obj1);
//                   }
//                     
//                }
//                else if(columnNr2==-1){//obj 2 in holding
//                   ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
//                   rowStateCost.dropObj(obj2);
//                   rowStateCost.move(obj1,column1);
//                } 
//                else{//objects are in different columns
//                   ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
//                   rowStateCost.move(obj1,column1);
//                }
            }
            else if ("under".equals(statement.get(0).toLowerCase())){
               evaluateAbove(obj2,obj1,objects,rowStateCost,columnNr2,columnNr1,place2,place1);
               
//                if(columnNr2==columnNr1){//in the same column
//                   ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
//                   if (place2<place1){//But object 2 is below
//                      rowStateCost.move(obj2,column2);
//                   }
//             
//                }
//                else if(columnNr2==-1){//obj 2 in holding
//                   
//                   rowStateCost.dropObj(obj2);
//                   
//                }
//                else if(columnNr1==-1){//obj 1 in holding
//                   ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
//                   rowStateCost.dropObj(obj1);
//                   rowStateCost.move(obj2,column2);
//                } 
//                else{//objects are in different columns
//                   ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
//                   rowStateCost.move(obj2,column2);
//                }
            }
            else if ("beside".equals(statement.get(0).toLowerCase())){
               
               if(columnNr1==columnNr2){//in the same column
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  rowStateCost.moveMin(obj1,obj2,column1,column2);
               }
               else if(columnNr1==-1){//obj 1 in holding
                  rowStateCost.dropObj(obj1);
               }
               else if(columnNr2==-1){//obj 2 in holding
                  rowStateCost.dropObj(obj2);
               }
               else if((columnNr1-columnNr2 > 1) || (columnNr2-columnNr1 > 1)){//objects are further away
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  rowStateCost.moveMin(obj1,obj2,column1,column2);
               }
            }
            else if ("rightof".equals(statement.get(0).toLowerCase())){
               
               if(worldState.getWorld().size()-1==columnNr2 && columnNr1==0){//both objects are in the wrong edges
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  rowStateCost.move(obj1,column1);
                  rowStateCost.move(obj2,column2);
                  
               }
               else if(columnNr1==0){
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  rowStateCost.move(obj1,column1);
                  if(columnNr2==-1)
                     rowStateCost.dropObj(obj2);
               } 
               else if(worldState.getWorld().size()-1==columnNr2){
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  rowStateCost.move(obj2,column2);
                  if(columnNr1==-1)
                     rowStateCost.dropObj(obj1);
               } 
               else if(columnNr1==-1){
                  rowStateCost.dropObj(obj1);
               }
               else if(columnNr2==-1){
                  rowStateCost.dropObj(obj2);
               } 
               else if(columnNr1<=columnNr2){//object 1 left of object 2
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  rowStateCost.moveMin(obj1,obj2,column1,column2);
               }
               
            }
            else if ("leftof".equals(statement.get(0).toLowerCase())){
               
               if(worldState.getWorld().size()-1==columnNr1 && columnNr2==0){
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  rowStateCost.move(obj1,column1);
                  rowStateCost.move(obj2,column2);
                  
                  
               } 
               else if(columnNr2==0){
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  rowStateCost.move(obj2,column2);
                  if(columnNr1==-1)
                     rowStateCost.dropObj(obj1);
               } 
               else if(worldState.getWorld().size()-1==columnNr1){
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  rowStateCost.move(obj1,column1);
                  if(columnNr2==-1)
                     rowStateCost.dropObj(obj2);
               }
               else if(columnNr1==-1){
                  rowStateCost.dropObj(obj1);
               }
               else if(columnNr2==-1){
                  rowStateCost.dropObj(obj2);
               }

               else if(columnNr2<=columnNr1){//object 1 right of object 2
                  ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  rowStateCost.moveMin(obj1,obj2,column1,column2);
               }   
            }
            else if ("hold".equals(statement.get(0).toLowerCase()) && !worldState.getHolding().equals(obj2)){
               if(!worldState.getHolding().equals(obj2)){
                  ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                  rowStateCost.moveAbove(obj2,column2);
                  if(!worldState.getHolding().isEmpty())
                     rowStateCost.dropObj(worldState.getHolding());
               }
            }
            
            
            
         }
         if(rowStateCost.sum()>0){
            if(!worldState.getHolding().isEmpty()){
               rowStateCost.dropObj(worldState.getHolding());
            }
         }
         
         if(rowStateCost.sum()<stateCost.sum()){
            stateCost=rowStateCost;
         }
         
    }
		
	}
   
   public int getCost(){
      return stateCost.sum();
   }
   
   
   public boolean isBetter(Heuristic3 compHeu){
   
      if (compHeu.getCost()>stateCost.sum()){
         return true;
      }

      else{
         return false;
      }
   
   
   }
   
   private void evaluateAbove(String obj1, String obj2, JSONObject objects, StateCost currentCost, int columnNr1, int columnNr2, int place1, int place2){
      ArrayList<String> allObjects = worldState.getAllObjectsInWorld();
      HashMap<String,ArrayList<String>> objectsAllowedBelow = new HashMap();
      
      if(columnNr1==columnNr2){//in the same column
         if (place1<place2){//But object 1 is below
            ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
            currentCost.move(obj1,column1);
         }
      }
      else if(columnNr1==-1){//obj 1 in holding
         ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
         column2.add(obj1);
         if(Constraints.isColumnAllowed(column2,objects)){
            column2.remove(obj1); 
            currentCost.dropObj(obj1);
         }
         else{
            column2.remove(obj1); 
            currentCost.dropMoveObj(obj1);
         }
           
      }
      else if(columnNr2==-1){//obj 2 in holding
         ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
         currentCost.dropObj(obj2);
         currentCost.move(obj1,column1);
      } 
      else{//objects are in different columns
         ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
         currentCost.move(obj1,column1);
      }
      
      for(String currentObj: allObjects){
         
         ArrayList<String> currentAllowedBelow = new ArrayList<String>();

         for(String currentBelow: allObjects){
            if(!currentObj.equals(currentBelow)){
               ArrayList<String> currentPair = new ArrayList<String>();
               currentPair.add(currentBelow);
               currentPair.add(currentObj);
               if(Constraints.isColumnAllowed(currentPair,objects))
                  currentAllowedBelow.add(currentBelow);
            }
         }
         objectsAllowedBelow.put(currentObj,currentAllowedBelow);
      }
      ArrayList<String> allowedObjects = findAllowedBetween(obj1, obj2, objectsAllowedBelow, new ArrayList<String>(), new ArrayList<String>());
      ArrayList<ArrayList<String>> sequences = findAllowedSequences(obj1, obj2, objectsAllowedBelow, new ArrayList<ArrayList<String>>(), new ArrayList<String>());
//       System.out.println("Sequences: "+sequences);
      Goal ontopGoal = generateGoal(sequences);
      Heuristic3 ontopHeu = new Heuristic3(worldState, ontopGoal, objects);
      currentCost.mergeCosts(ontopHeu.getStateCost());
      
//       if(!worldState.getHolding().equals(obj2)){
//          ArrayList<String> column2 = worldState.getColumn(columnNr2);
//          boolean tempBool = true;
//          for(int i=column2.indexOf(obj2)+1; i<column2.size() && tempBool; i++){
//             if(!allowedObjects.contains(column2.get(i))){
//                currentCost.move(column2.get(i),column2);
//                tempBool = false;
//             }
//          }
//       }     
   }
   
   private ArrayList<String> findAllowedBetween(String objAbove, String objBelow, HashMap<String,ArrayList<String>> objectsAllowedBelow, ArrayList<String> allowedObjects, ArrayList<String> evaluatedObjects){
      evaluatedObjects.add(objAbove);
      boolean allowed = false;
      if(objectsAllowedBelow.get(objAbove).contains(objBelow))
         allowed = true;
         
      
      for(String objAllowedBelow : objectsAllowedBelow.get(objAbove)){
         if(!evaluatedObjects.contains(objAllowedBelow) && !objAllowedBelow.equals(objBelow)){
            allowedObjects = findAllowedBetween(objAllowedBelow, objBelow, objectsAllowedBelow, allowedObjects, evaluatedObjects);
            if(allowedObjects.contains(objAllowedBelow))
               allowed = true;
         }
      }
      if(allowed)
         allowedObjects.add(objAbove);
         
      return allowedObjects;
   }
   
   private ArrayList<ArrayList<String>> findAllowedSequences(String objAbove, String objBelow, HashMap<String,ArrayList<String>> objectsAllowedBelow, ArrayList<ArrayList<String>> allowedSequences, ArrayList<String> currentSequence){
      currentSequence.add(objAbove);

      if(objectsAllowedBelow.get(objAbove).contains(objBelow) && !allowedSequences.contains(currentSequence)){
         currentSequence.add(objBelow);
         allowedSequences.add(new ArrayList<String>(currentSequence));
         currentSequence.remove(objBelow);
      }
         
      
      for(String objAllowedBelow : objectsAllowedBelow.get(objAbove)){
         if(!currentSequence.contains(objAllowedBelow) && !objAllowedBelow.equals(objBelow)){
            allowedSequences = findAllowedSequences(objAllowedBelow, objBelow, objectsAllowedBelow, allowedSequences, currentSequence);
         }
      }
      
      currentSequence.remove(objAbove);   
      return allowedSequences;
   }
   
   private Goal generateGoal(ArrayList<ArrayList<String>> sequences){
      Goal goal = new Goal();
      for(int i = 0; i<sequences.size();i++){
         for(int j = 1; j<sequences.get(i).size(); j++){
            goal.addStatement(i, new Statement("ontop",sequences.get(i).get(j-1),sequences.get(i).get(j)));
         }
      }
      return goal;
   } 
   
   public StateCost getStateCost(){
      return stateCost;
   }
   
   //----------------------------------------------------------------------------------//
   private class StateCost{
      HashMap<String, Integer> costMap;
         
   
         
      public StateCost(State stateIn) {
         ArrayList<String> objects = stateIn.getAllObjectsInWorld();
         costMap = new HashMap(objects.size());
         for(String obj : objects){
      	   costMap.put(obj, new Integer(0));
      	}
      }
      public StateCost(State stateIn,int initCost) {
         
         ArrayList<String> objects = stateIn.getAllObjectsInWorld();
         costMap = new HashMap(objects.size());
         for(String obj : objects){
      	   costMap.put(obj, new Integer(initCost));
      	}
      }
            
      
   
      public void moveAbove(String object, ArrayList<String> col){
         boolean above;
         if(object.contains("floor"))
            above = true;
            
         else
            above = false;
            
         for(int i=0; i<col.size(); i++){
            if(above && costMap.get(col.get(i))<2){
               costMap.put(col.get(i),2);
            }
            else if(col.get(i).contains(object)){
               above = true;
            }
         }
      
      }
      public void move(String object, ArrayList<String> col){
         if(costMap.get(object)<2){
            costMap.put(object,2);
         }
         moveAbove(object, col);
      }
      
      public void doubleMove(String object, ArrayList<String> col){
        
        costMap.put(object,4);
        moveAbove(object, col);
      }
   
      
      public int evaluateMoveAbove(String object, ArrayList<String> col){
        int moveAboveCost = 0;
        boolean above = false;
        for(String s : col){
            if(above && costMap.get(s)<2){
               moveAboveCost += 2;
            }
            else if(s.contains(object)){
               above = true;
            }
         }
         return moveAboveCost;
      }
      
      public int evaluateMove(String object, ArrayList<String> col){
         int moveCost = 0;
         if(costMap.get(object)<2){
            moveCost += 2;
         }
         moveCost += evaluateMoveAbove(object, col);
         return moveCost;
      }
      
      public void moveMin(String obj1, String obj2, ArrayList<String> col1, ArrayList<String> col2){
         if(evaluateMove(obj1,col1) < evaluateMove(obj2,col2)){
            move(obj1,col1);
         }
         else{
            move(obj2,col2);
         }
      }
      
      public void dropObj(String obj){
         if(costMap.get(obj) < 1){
            costMap.put(obj,1);
         }
      }
      
      public void dropMoveObj(String obj){
         if(costMap.get(obj) < 3){
            costMap.put(obj,3);
         }
      }
      
      public void takeObj(String obj, ArrayList<String> col){
         if(costMap.get(obj) < 1){
            costMap.put(obj,1);
         }
         moveAbove(obj,col);
      }
      
      public int sum(){
         int cost = 0;
         for(Integer value : costMap.values()){
            if(value==-1){
               return 10000;
            }
            cost += value;
         }
         return cost;
      }
      
      public void mergeCosts(StateCost costIn){
         HashMap<String,Integer> costMapIn = costIn.getCostMap();
         for(String obj : costMap.keySet()){
            if (costMapIn.get(obj)>costMap.get(obj)){
               costMap.put(obj,costMapIn.get(obj));
            }
         }
      }
      
      public HashMap<String,Integer> getCostMap(){
         return costMap;
      }
   }
}

     
