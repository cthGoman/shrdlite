import org.json.simple.*;
import java.util.*;


public class Heuristic4{
   private StateCost stateCost;
   private State worldState;
   private Goal goal;
   private HashMap<String,ArrayList<String>> objectsAllowedBelow;
   private JSONObject objects;
   
   public Heuristic4(State worldState){
      stateCost = new StateCost(worldState, -1);
      
   }

	public Heuristic4(State stateIn, Goal goalIn, JSONObject objectsIn) {
      objects = objectsIn;
		goal = goalIn;
      worldState = stateIn;
      stateCost = new StateCost(worldState, -1);
      ArrayList<Integer> rowCosts = new ArrayList<Integer>();
      
    for (int i = 0; i<goal.size(); i++){//Loop over all rows 
         

         StateCost rowStateCost = new StateCost(worldState);
         
         if(containsAboveOrUnder(goal.get(i))){ //If the goal row contains above or under
            ArrayList<Statement> listOfStatement = goal.get(i);

            for(int j = 0; j<listOfStatement.size() && containsAboveOrUnder(listOfStatement); j++){
               
               Statement statement = listOfStatement.get(j);
               String obj1 = statement.get(1);
               int columnNr1 = worldState.getColumnNumber(obj1);
               int place1 = worldState.getPlaceInColumn(obj1);
               
               String obj2 = statement.get(2);
               int columnNr2 = worldState.getColumnNumber(obj2);
               int place2 = worldState.getPlaceInColumn(obj2);
               
               if ("above".equals(statement.get(0).toLowerCase()) && !statement.get(2).contains("floor")){
                  evaluateAbove(obj1,obj2,objects,rowStateCost,columnNr1,columnNr2,place1,place2, i, goal.get(i).indexOf(statement));
   
               }
               else if ("under".equals(statement.get(0).toLowerCase())){
                  evaluateAbove(obj2,obj1,objects,rowStateCost,columnNr2,columnNr1,place2,place1, i, goal.get(i).indexOf(statement));
               }
               else if(statement.get(2).contains("floor")){
                  // System.out.println("above the floor: "+statement);
                  listOfStatement.remove(statement);
                  if(!containsAboveOrUnder(listOfStatement)){
                     goal.add(listOfStatement);
                  }
               }
               
            }
            rowCosts.add(1000);
         }
         else{ //If the goal row doesn't contain above or under
            ArrayList<Statement> listOfStatements = goal.get(i);   
            ArrayList<Integer> unevaluatedStatements = new ArrayList<Integer>(listOfStatements.size());
            for(int j = 0; j<listOfStatements.size(); j++){
               unevaluatedStatements.add(j);
            }
            
            while(unevaluatedStatements.size()>0){//Loop over every statement in row
               int j = unevaluatedStatements.get(0);
               Statement statement = listOfStatements.get(j);
               
               String obj1 = statement.get(1);
               int columnNr1 = worldState.getColumnNumber(obj1);
               int place1 = worldState.getPlaceInColumn(obj1);
              // ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
               
               String obj2 = statement.get(2);
               int columnNr2 = worldState.getColumnNumber(obj2);
               int place2 = worldState.getPlaceInColumn(obj2);
               // ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
               
               rowStateCost.addStatementIndices(obj1, obj2, j);
   
               if ("ontop".equals(statement.get(0).toLowerCase()) || "inside".equals(statement.get(0).toLowerCase())){
                  
                  if(columnNr1==columnNr2){//in the same column
                     if (!worldState.getObjectBelow(obj1).equals(obj2)){//But not ontop
                        if(place1 < place2){ //object 1 below object 2
                           ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                           rowStateCost.move(obj1, column1, unevaluatedStatements);
                        }
                        else{ //move objects above object 2 and move object 1 twice
                           ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                           ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                           rowStateCost.moveAbove(obj2, column2, unevaluatedStatements);
                           rowStateCost.doubleMove(obj1, column1, unevaluatedStatements);
                        }  
                     }
                     else if(rowStateCost.getObjCost(obj1)>0 || rowStateCost.getObjCost(obj2)>0){
                        ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                        rowStateCost.doubleMove(obj1,column1, unevaluatedStatements);
                     }
                  }
                  else if(columnNr1==-1){//object 1 is in holding
                     ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                     
                     if(place2 < 0){
                        if(column2.size()>0){
                           rowStateCost.moveAbove(obj2,column2, unevaluatedStatements);
                        }
                        else{
                           rowStateCost.dropObj(obj1);
                        }
                     }
                     else if(column2.get(column2.size()-1).equals(obj2)){
                        if(rowStateCost.getObjCost(obj2)==0)
                           rowStateCost.dropObj(obj1);
                        else
                           rowStateCost.dropMoveObj(obj1);
                     }
                     else{
                        rowStateCost.dropMoveObj(obj1);
                     }
                     rowStateCost.moveAbove(obj2, column2, unevaluatedStatements);
                  }
                  else if(columnNr2==-1){//object 2 is in holding
                     ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                     rowStateCost.dropObj(obj2);
                     rowStateCost.move(obj1, column1, unevaluatedStatements);
                  } 
                  else{//objects are in different columns
                     ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                     ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                     rowStateCost.moveAbove(obj2,column2, unevaluatedStatements);
                     rowStateCost.move(obj1,column1, unevaluatedStatements);
                  }
               }

               else if ("beside".equals(statement.get(0).toLowerCase())){
                  
                  if(columnNr1==columnNr2){//in the same column
                     ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                     ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                     rowStateCost.moveMin(obj1,obj2,column1,column2, unevaluatedStatements);
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
                     rowStateCost.moveMin(obj1,obj2,column1,column2, unevaluatedStatements);
                  }
               }
               else if ("rightof".equals(statement.get(0).toLowerCase())){
                  
                  if(worldState.getWorld().size()-1==columnNr2 && columnNr1==0){//both objects are in the wrong edges
                     ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                     ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                     rowStateCost.move(obj1,column1, unevaluatedStatements);
                     rowStateCost.move(obj2,column2, unevaluatedStatements);
                     
                  }
                  else if(columnNr1==0){
                     ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                     rowStateCost.move(obj1,column1, unevaluatedStatements);
                     if(columnNr2==-1)
                        rowStateCost.dropObj(obj2);
                  } 
                  else if(worldState.getWorld().size()-1==columnNr2){
                     ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                     rowStateCost.move(obj2,column2, unevaluatedStatements);
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
                     rowStateCost.moveMin(obj1,obj2,column1,column2, unevaluatedStatements);
                  }
                  
               }
               else if ("leftof".equals(statement.get(0).toLowerCase())){
                  
                  if(worldState.getWorld().size()-1==columnNr1 && columnNr2==0){
                     ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                     ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                     rowStateCost.move(obj1,column1, unevaluatedStatements);
                     rowStateCost.move(obj2,column2, unevaluatedStatements);
                     
                     
                  } 
                  else if(columnNr2==0){
                     ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                     rowStateCost.move(obj2,column2, unevaluatedStatements);
                     if(columnNr1==-1)
                        rowStateCost.dropObj(obj1);
                  } 
                  else if(worldState.getWorld().size()-1==columnNr1){
                     ArrayList<String> column1 = worldState.getWorld().get(columnNr1);
                     rowStateCost.move(obj1,column1, unevaluatedStatements);
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
                     rowStateCost.moveMin(obj1,obj2,column1,column2, unevaluatedStatements);
                  }   
               }
               else if ("hold".equals(statement.get(0).toLowerCase()) && !worldState.getHolding().equals(obj2)){
                  if(!worldState.getHolding().equals(obj2)){
                     ArrayList<String> column2 = worldState.getWorld().get(columnNr2);
                     rowStateCost.takeObj(obj2,column2, unevaluatedStatements);
                     if(!worldState.getHolding().isEmpty())
                        rowStateCost.dropObj(worldState.getHolding());
                  }
               }
               
//                System.out.println("Indices: "+unevaluatedStatements+ " remove index: "+j);
               unevaluatedStatements.remove(0);
            
            }
            if(rowStateCost.sum()>2){
               if(!worldState.getHolding().isEmpty()){
                  rowStateCost.dropObj(worldState.getHolding());
               }
            }
            
            if(rowStateCost.sum()<stateCost.sum()){
               stateCost=rowStateCost;
            }
            rowCosts.add(rowStateCost.sum());

         }

         
          
      }
//       System.out.println("goal.size(): "+goal.size());
      for(int i = goal.size()-1; i>=0; i--){
         if(rowCosts.get(i)>(stateCost.sum()))
            goal.remove(i);
      }

         
	}
      
      public double getCost(){
         int mySum = stateCost.sum();
         double epsilon = mySum/10;
               
         return (1+epsilon)*mySum;
      }
      
      public double getCost(int depth, int maxCost){
         double mySum = stateCost.sum();
         double epsilon = mySum/10;
      
         return (1+epsilon)*mySum;
      }
      
      public boolean isBetter(Heuristic4 compHeu){
      
         if (compHeu.getCost()>stateCost.sum()){
            return true;
         }
   
         else{
            return false;
         }
      
      
      }
      
      private boolean containsAboveOrUnder(ArrayList<Statement> listIn){
         for(Statement statement : listIn){
            if("above".equals(statement.get(0).toLowerCase()))
               return true;
            else if("under".equals(statement.get(0).toLowerCase()))
               return true;
         }
         return false;
      }
      
      private void evaluateAbove(String obj1, String obj2, JSONObject objects, StateCost currentCost, int columnNr1, int columnNr2, int place1, int place2, int goalRowIdx, int statementIdx){
         
         ArrayList<Statement> goalRow = new ArrayList<Statement>(goal.get(goalRowIdx));
         if(objectsAllowedBelow == null){
            objectsAllowedBelow = new HashMap();
            ArrayList<String> allObjects = worldState.getAllObjectsInWorld();
            
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
         }   
         ArrayList<String> allowedObjects = findAllowedBetween(obj1, obj2, objectsAllowedBelow, new ArrayList<String>(), new ArrayList<String>());
         ArrayList<ArrayList<String>> sequences = findAllowedSequences(obj1, obj2, objectsAllowedBelow, new ArrayList<ArrayList<String>>(), new ArrayList<String>());
   //       System.out.println("Sequences: "+sequences);
         Goal ontopGoal = generateGoal(sequences, goalRow, statementIdx);
         
         if (ontopGoal.isEmpty()){
            return;
         }
   //       System.out.println("ontopGoal.size(): "+ontopGoal.size()+" example: "+ontopGoal.get(0));
   //       System.out.println("goal.size(): "+goal.size());
         goal.addAll(ontopGoal);
         // System.out.print(" goal.size() efter: "+goal.size());  
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
      
      private Goal generateGoal(ArrayList<ArrayList<String>> sequences, ArrayList<Statement> goalRow, int statementIdx){
         Goal tempGoal = new Goal();
         
   
         goalRow.remove(statementIdx);
         
         for(int i = 0; i<sequences.size();i++){
            ArrayList<Statement> tempRow = new ArrayList<Statement>(goalRow);
            for(int j = 1; j<sequences.get(i).size(); j++){
               for(int k = j-1; k>=0; k--){
                  if(tempRow.contains(new Statement("above",sequences.get(i).get(k),sequences.get(i).get(j)))){
                     tempRow.remove(new Statement("above",sequences.get(i).get(k),sequences.get(i).get(j)));
                     if(!tempRow.contains(new Statement("ontop",sequences.get(i).get(j-1),sequences.get(i).get(j))))
                        tempRow.add(new Statement("ontop",sequences.get(i).get(j-1),sequences.get(i).get(j)));
                  }
                  else if(tempRow.contains(new Statement("below",sequences.get(i).get(j),sequences.get(i).get(k)))){
                     tempRow.remove(new Statement("below",sequences.get(i).get(j),sequences.get(i).get(k)));
                     if(!tempRow.contains(new Statement("ontop",sequences.get(i).get(j-1),sequences.get(i).get(j))))
                        tempRow.add(new Statement("ontop",sequences.get(i).get(j-1),sequences.get(i).get(j)));
                  }
               }   
               
               if(!tempRow.contains(new Statement("ontop",sequences.get(i).get(j-1),sequences.get(i).get(j))))
                  tempRow.add(new Statement("ontop",sequences.get(i).get(j-1),sequences.get(i).get(j)));
            }
            if(Constraints.isGoalRowAllowed(tempRow, objects))
               tempGoal.add(tempRow);
         }
   
         return tempGoal;
      } 
      
      public StateCost getStateCost(){
         return stateCost;
      }
   
   
   
   
   //----------------------------------------------------------------------------------//
   private class StateCost{
      HashMap<String, Integer> costMap;
      HashMap<String, ArrayList<Integer>> statementMap;
   
         
      public StateCost(State stateIn) {
         this(stateIn, 0);
      }
      public StateCost(State stateIn,int initCost) {
         
         ArrayList<String> objects = stateIn.getAllObjectsInWorld();
         costMap = new HashMap(objects.size());
         statementMap = new HashMap(objects.size());
         for(String obj : objects){
      	   costMap.put(obj, new Integer(initCost));
            statementMap.put(obj, new ArrayList<Integer>(3));
      	}
         
         for(int i=0; i<stateIn.getWorld().size(); i++){
            statementMap.put("floor-"+i,new ArrayList<Integer>(2));
            costMap.put("floor-"+i,new Integer(0));
         }
         statementMap.put("robot-"+0,new ArrayList<Integer>(2));
         costMap.put("robot-"+0,0);
      }
      
      public void addStatementIndices(String obj1, String obj2, Integer i){
         ArrayList<Integer> obj1Indices = statementMap.get(obj1);
         if(!obj1Indices.contains(i)){
            obj1Indices.add(i);
            statementMap.put(obj1,obj1Indices);
         }
         
         ArrayList<Integer> obj2Indices = statementMap.get(obj2);
         if(!obj2Indices.contains(i)){
            obj2Indices.add(i);
            statementMap.put(obj2,obj2Indices);
         }
      }      
   
      public void moveAbove(String object, ArrayList<String> col, ArrayList<Integer> unevaluatedStatements){
         boolean above;
         if(object.contains("floor"))
            above = true;
            
         else
            above = false;
            
         for(int i=0; i<col.size(); i++){
            if(above && costMap.get(col.get(i))<2){
               costMap.put(col.get(i),2);
               for(Integer j : statementMap.get(col.get(i))){
                  if(!unevaluatedStatements.contains(j))
                     unevaluatedStatements.add(j);
               }
            }
            else if(col.get(i).contains(object)){
               above = true;
            }
         }
      
      }
      public void move(String object, ArrayList<String> col, ArrayList<Integer> unevaluatedStatements){
         if(costMap.get(object)<2){
            costMap.put(object,2);
            for(Integer j : statementMap.get(object)){
               if(!unevaluatedStatements.contains(j))
                  unevaluatedStatements.add(j);
            }
         }
         moveAbove(object, col, unevaluatedStatements);
      }
      
      
      public void doubleMove(String object, ArrayList<String> col, ArrayList<Integer> unevaluatedStatements){
        
        costMap.put(object,4);
        moveAbove(object, col, unevaluatedStatements);
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
      
      public void moveMin(String obj1, String obj2, ArrayList<String> col1, ArrayList<String> col2, ArrayList<Integer> unevaluatedStatements){
         if(evaluateMove(obj1,col1) < evaluateMove(obj2,col2)){
            move(obj1,col1, unevaluatedStatements);
         }
         else{
            move(obj2,col2, unevaluatedStatements);
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
      
      public void takeObj(String obj, ArrayList<String> col, ArrayList<Integer> unevaluatedStatements){
         if(costMap.get(obj) < 1){
            costMap.put(obj,1);
         }
         moveAbove(obj,col, unevaluatedStatements);
      }
      
      public int getObjCost(String obj){
         return costMap.get(obj);      
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

     
