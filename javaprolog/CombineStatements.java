import java.util.List;
import java.util.ArrayList;

public class CombineStatements{


   static public Goal combine(ArrayList<ArrayList<Statement>> statements){
      
      ArrayList<ArrayList<Statement>> currentStatementList = new  ArrayList<ArrayList<Statement>> ();    
   
      for(int i = 0 ; i < statements.get(0).size();i++){
         ArrayList<Statement> tempStatementList = new  ArrayList<Statement>();
         tempStatementList.add(statements.get(0).get(i));
         currentStatementList.add(tempStatementList);
      }
      ArrayList<ArrayList<Statement>> newStatementList;
      for(int i = 0 ; i < statements.size()-1; i++){
      
         newStatementList = new ArrayList<ArrayList<Statement>>(); 
      
         for(int j = 0; j < currentStatementList.size();j++){    
         
            for(int k = 0; k < statements.get(i+1).size();k++){
               if(currentStatementList.get(j).get(currentStatementList.get(j).size()-1).get(2).equals(statements.get(i+1).get(k).get(1))){
                  ArrayList<Statement> tempStatementList = new ArrayList<Statement>(currentStatementList.get(j));
                  tempStatementList.add(statements.get(i+1).get(k));
                  newStatementList.add(tempStatementList);
               }
            }
         }
         currentStatementList = newStatementList;
      }
      Goal goal = new Goal();
      
      for(int i = 0; i < currentStatementList.size();i++){
      
      goal.addCondition(currentStatementList.get(i));
      }
      
      return goal;
   
   }

}