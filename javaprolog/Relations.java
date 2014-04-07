import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class Relations{


   public static Goal relation(JSONObject objects,ArrayList<ArrayList<String>> world,Tree tree, ArrayList<String> object0){
   
      //DebugFile.start();   
      LinkedList<LinkedList<String>> objectsDescriptionList = new LinkedList<LinkedList<String>>();
      LinkedList<String> relations = new LinkedList<String>();
      tree.getMasterNode().createObjects(objectsDescriptionList,relations);
         
      ArrayList<ArrayList<Statement>> statementsList = new ArrayList<ArrayList<Statement>>();
         
      ArrayList<Statement> statements = new ArrayList<Statement>();
         
      ArrayList<ArrayList<String>> objectsList = new ArrayList<ArrayList<String>>();
      ArrayList<String> objectArray;
      //DebugFile.println("object0" + object0.toString());
      //DebugFile.println("objectsDescriptionList" + objectsDescriptionList.toString());
      //DebugFile.println("relations" + relations.toString());
      
     
      
      for(int i = 0; i < objectsDescriptionList.size(); i++){
         objectArray = FindObject.match(objectsDescriptionList.get(i),objects,world);
         objectsList.add(objectArray);
      }
     //DebugFile.println("objectList" + objectsList.toString());    
     //DebugFile.stop();
     
     Goal goal = new Goal();
     
     if(relations.size() != 0 && objectsList.size() != 0 && object0.size() != 0){
      for(int i = 0;i < object0.size();i++){
         for(int j = 0; j < objectsList.get(0).size() ; j++){ 
              
            Statement statement = new Statement(relations.get(0),object0.get(i),objectsList.get(0).get(j));
            statements.add(statement);
              
         }            
      }
      statementsList.add(statements);
         
      for(int i = 0;i < objectsList.size()-1 ; i++){
         statements = new ArrayList<Statement>();
         for(int j = 0; j < objectsList.get(i).size();j++){
            for(int k = 0; k < objectsList.get(i+1).size(); k++){
               
               Statement statement = new Statement(relations.get(i+1),objectsList.get(i).get(j),objectsList.get(i+1).get(k));
               statements.add(statement);
              
               
            }
         }
         statementsList.add(statements);
      }
            
      
      goal = CombineStatements.combine(statementsList);
      }
      
      return goal; 
   
   
   }





}