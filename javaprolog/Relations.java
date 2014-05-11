import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class Relations{


   public static Goal relation(JSONObject objects,ArrayList<ArrayList<String>> world,Tree tree, ArrayList<String> object0, int allAt){
      if(object0.size() == 0){
         return null;
      }
      //DebugFile.start();   
      LinkedList<LinkedList<String>> objectsDescriptionList = new LinkedList<LinkedList<String>>();
      LinkedList<String> relations = new LinkedList<String>();
      tree.getMasterNode().createObjects(objectsDescriptionList,relations);
         
      ArrayList<ArrayList<Statement>> statementsList = new ArrayList<ArrayList<Statement>>();
         
      ArrayList<Statement> statements = new ArrayList<Statement>();
         
      ArrayList<ArrayList<String>> objectsList = new ArrayList<ArrayList<String>>();
      ArrayList<String> objectArray;
     // DebugFile.println("object0" + object0.toString());
     // DebugFile.println("objectsDescriptionList" + objectsDescriptionList.toString());
     // DebugFile.println("relations" + relations.toString());
      
     
      
      for(int i = 0; i < objectsDescriptionList.size(); i++){
         objectArray = FindObject.match(objectsDescriptionList.get(i),objects,world);
         objectsList.add(objectArray);
      }
     //DebugFile.println("objectList" + objectsList.toString());    
     //DebugFile.stop();
     
      Goal goal = new Goal();
      if(relations.size() != 0 && objectsList.size() != 0){
         for(int i = 0;i < object0.size();i++){
            for(int j = 0; j < objectsList.get(0).size() ; j++){ 
               Statement statement = new Statement(relations.get(0),object0.get(i),objectsList.get(0).get(j));
               if(Constraints.isStatementAllowed(statement,objects)){
                  statements.add(statement);
               }
            }
         }
         statementsList.add(statements);
         for(int i = 0;i < objectsList.size()-1 ; i++){
            statements = new ArrayList<Statement>();
            for(int j = 0; j < objectsList.get(i).size();j++){
               for(int k = 0; k < objectsList.get(i+1).size(); k++){
                  Statement statement = new Statement(relations.get(i+1),objectsList.get(i).get(j),objectsList.get(i+1).get(k));
                  if(Constraints.isStatementAllowed(statement,objects)){
                     statements.add(statement);
                  }
               }
            }
            statementsList.add(statements);
         }
         if(allAt == -1){
            Goal prelGoal = CombineStatements.combine(statementsList);
			   for(ArrayList<Statement> als:prelGoal){
				   if(Constraints.isGoalRowAllowed(als)){
					   goal.addCondition(als);
				   }
			   }
         }
         else{
            ArrayList<String> allObjects;
            ArrayList<Statement> possibleStatementsForObjects;
            if(allAt == 0){
               allObjects = object0;
               possibleStatementsForObjects = statementsList.get(0);
            }
            else{
               allObjects = objectsList.get(allAt);
               possibleStatementsForObjects = statementsList.get(allAt-1); 
            }
            ArrayList<Goal> subGoals = new ArrayList<Goal>(); 
            for(int i = 0;i < allObjects.size();i++){
               ArrayList<Statement> StatementsMatchingObject = new ArrayList<Statement>();
               for(int j = 0;j < possibleStatementsForObjects.size();j++){
                  if(allAt == 0){
                     if(allObjects.get(i).equals(possibleStatementsForObjects.get(j).get(1))){
                        StatementsMatchingObject.add(possibleStatementsForObjects.get(j));
                     }
                  }
                  else{
                     if(allObjects.get(i).equals(possibleStatementsForObjects.get(j).get(2))){
                        StatementsMatchingObject.add(possibleStatementsForObjects.get(j));   
                     }
                  }
               }
               ArrayList<ArrayList<Statement>> StatementsForSubgoal = new ArrayList<ArrayList<Statement>>(statementsList);
               if(allAt == 0){
                  StatementsForSubgoal.add(allAt,StatementsMatchingObject);               
               }
               else{
                  StatementsForSubgoal.add(allAt-1,StatementsMatchingObject);     
               }
               Goal prelsubGoal =  CombineStatements.combine(StatementsForSubgoal);
               Goal subGoal = new Goal();
               for(ArrayList<Statement> als:prelsubGoal){
				      if(Constraints.isGoalRowAllowed(als)){
					      subGoal.addCondition(als);
				      }
			      }
               subGoals.add(subGoal);     
            }
            for(int i = 0;i < subGoals.size();i++){
               goal.combineGoals(subGoals.get(i));
            }
         }
      }
      return goal; 
   }
   
}