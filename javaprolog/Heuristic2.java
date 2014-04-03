import org.json.simple.*;
import java.util.List;
import java.util.ArrayList;


public class Heuristic2{
   private int cost;
   private int colCost;
   private ArrayList<Integer> tempColCostList = new ArrayList<Integer>();
   private int misplaced;
   private int inTheWay;
   
   public Heuristic2(int input){
      cost=input;
      colCost=input;
      misplaced = input;
      inTheWay=input;
      
   }

	public Heuristic2(JSONArray world, String holding, Goal goal, JSONObject objects) {
		
		cost = 1000;
      colCost = 0;
      misplaced = 0;
      inTheWay = 0;
      
    for (ArrayList<Statement> listOfStatement : goal){//Loop over all rows 
         int rowCost = 0;
         
         for (Statement statement : listOfStatement) {//Loop over every statement in row
         
            if ("ontop".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(column1==column2){//in the same column
                  if (!WorldFunctions.getObjectBelow(world,statement.get(1)).equals(statement.get(2))){//But not ontop
                     rowCost += 4;
                  }
               } 
               else if(column1==-1){//object 1 is in holding
                  if(((JSONArray)world.get(column2)).size()==(place2+1)){//object 2 is the top object in its column
                     rowCost += 1;
                  }
                  else{
                     rowCost += 3;
                  }
               }
               else if(column2==-1){//Object 2 is in holding
                  rowCost += 3;
               }
               else{//objects are in different columns
                  rowCost += 2;
               }
            }
            else if ("above".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(column1==column2){//in the same column
                  if (place1<place2){//But object 1 is below
                     rowCost += 4;
                  }
               } 
               else if(column1==-1){//object 1 is in holding
                  ((JSONArray)world.get(column2)).add(statement.get(1));
                  if(Constraints.isColumnAllowed((JSONArray)world.get(column2),objects)){//is column allowed
                     rowCost += 1;
                  }
                  else{
                     rowCost += 3;
                  }
                 
                 ((JSONArray)world.get(column2)).remove(statement.get(1));
               }
               else if(column2==-1){//Object 2 is in holding
                  rowCost += 3;
               }
               else{//objects are in different columns
                  rowCost += 2;
               }
            }
            else if ("below".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(column2==column1){//in the same column
                  if (place2<place1){//But object 2 is below
                     rowCost += 4;
                  }
               } 
               else if(column2==-1){//object 2 is in holding
                  ((JSONArray)world.get(column1)).add(statement.get(2));
                  if(Constraints.isColumnAllowed((JSONArray)world.get(column1),objects)){//is column allowed
                     rowCost += 1;
                  }
                  else{
                     rowCost += 3;
                  }
                 
                 ((JSONArray)world.get(column1)).remove(statement.get(2));
               }
               else if(column1==-1){//Object 1 is in holding
                  rowCost += 3;
               }
               else{//objects are in different columns
                  rowCost += 2;
               }
            }
            else if ("beside".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(column1==column2){//in the same column
                  
                  rowCost += 2;
                  
               } 
               else if(column1==-1){//object 1 is in holding
                  // if((world.size()-1)>column2){
//                      world.get(column2+1).add(statement.get(1));
//                      if(isColumnAllowed(world.get(column1),objects)){//is column allowed
//                      rowCost += 1;
//                      }
//                      
//                      world.get(column2+1).remove(statement.get(1));
//                   }
                  
                  rowCost += 1;
                  
               }
               else if(column2==-1){//Object 2 is in holding
                  rowCost += 1;
               }
               else if((column1-column2 > 1) || (column2-column1 > 1)){//objects are further away
                  rowCost += 2;
               }
            }
            else if ("rightof".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(world.size()-1==column2 && column1==0){
                  
                  rowCost += 4;
                  
               } 
               else if(column1<column2){//object 1 left of object 2
                  
                  rowCost += 2;
                  
               }
               else if(column2==-1 || column1==-1){//Object 1 or 2 is in holding
                  rowCost += 1;
               }
               
            }
            else if ("leftof".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(world.size()-1==column1 && column2==0){
                  
                  rowCost += 4;
                  
               } 
               else if(column2<column1){//object 1 right of object 2
                  
                  rowCost += 2;
                  
               }
               else if(column2==-1 || column1==-1){//Object 1 or 2 is in holding
                  rowCost += 1;
               }
               
            }
         }
         if(rowCost<cost){
            cost=rowCost;
         }
         
    }
		
	}
   
   public int getCost(){
      return cost;
   }
   
   public int getColCost(){
      return colCost;
   }
   
   public int getMisplaced(){
      return misplaced;
   }
   
   public int getInTheWay(){
      return inTheWay;
   }
   
   public boolean isBetter(Heuristic compHeu){
   
      if ((compHeu.getCost()>cost) ||
      (compHeu.getCost()==cost && compHeu.getMisplaced()>misplaced) ||
      (compHeu.getCost()==cost && compHeu.getMisplaced()==misplaced && compHeu.getInTheWay()>inTheWay) ||
      (compHeu.getCost()==cost && compHeu.getMisplaced()==misplaced && compHeu.getInTheWay()==inTheWay && compHeu.getColCost()>colCost)){
         return true;
      }

      else{
         return false;
      }
   
   
   }
}
