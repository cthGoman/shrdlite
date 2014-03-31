import java.util.List;
import java.util.ArrayList;

public class CombineStatements{


   static public ArrayList<ArrayList<Statement>> combine(ArrayList<ArrayList<Statement>> statements){
      
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
               if(currentStatementList.get(j).get(currentStatementList.get(j).size()-1).get(2).equals(statements.get(i+1).get(k).get(0))){
                  ArrayList<Statement> tempStatementList = new ArrayList<Statement>(currentStatementList.get(j));
                  tempStatementList.add(statements.get(i+1).get(k));
                  newStatementList.add(tempStatementList);
               }
            }
         }
         currentStatementList = newStatementList;
      }      
      return currentStatementList;
   
   }
   static public ArrayList<ArrayList<Statement>> testStatement(){
      ArrayList<ArrayList<Statement>> testList = new ArrayList<ArrayList<Statement>>();
      ArrayList<Statement> state1 = new ArrayList<Statement>();
      state1.add(new Statement("R","hold","A"));
      state1.add(new Statement("R","hold","B"));
        
      ArrayList<Statement> state2 = new ArrayList<Statement>();
      state2.add(new Statement("A","on","C"));
      state2.add(new Statement("A","on","D"));
      state2.add(new Statement("B","on","C"));
      state2.add(new Statement("B","on","D"));
        
      ArrayList<Statement> state3 = new ArrayList<Statement>();
      state3.add(new Statement("C","on","E"));
      state3.add(new Statement("C","on","F"));
      state3.add(new Statement("D","on","E"));
      state3.add(new Statement("D","on","F"));
        
      ArrayList<Statement> state4 = new ArrayList<Statement>();
      state4.add(new Statement("E","on","T"));
      state4.add(new Statement("F","on","T"));
        
      testList.add(state1);
      testList.add(state2);
      testList.add(state3);
      testList.add(state4);
      return testList;
   }
}