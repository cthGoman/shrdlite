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
         int rowInTheWay = 0;
         
         for (Statement statement : listOfStatement) {//Loop over every statement in row
         
            if ("ontop".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(column1==column2){//in the same column
                  if (!WorldFunctions.getObjectBelow(world,statement.get(1)).equals(statement.get(2))){//But not ontop
                     rowCost += 4;
                     rowInTheWay += (Math.abs(place1-place2)-1); //add the objects between the objects in the statement as in the way
                     rowInTheWay += ((JSONArray)world.get(column1)).size()-1-Math.max(place1,place2); //add the objects above the top statement object as in the way
                     
                  }
               } 
               else{//objects are in different columns
                  rowCost += 2;
                  rowInTheWay += ((JSONArray)world.get(column1)).size()-1-place1; //add the objects above the statement objects as in the way
                  rowInTheWay += ((JSONArray)world.get(column2)).size()-1-place2;
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
                     rowInTheWay += (Math.abs(place1-place2)-1); //add the objects between the objects in the statement as in the way
                     rowInTheWay += ((JSONArray)world.get(column1)).size()-1-Math.max(place1,place2); //add the objects above the top statement object as in the way
                  }
               } 
               else{//objects are in different columns
                  rowCost += 2;
                  rowInTheWay += ((JSONArray)world.get(column1)).size()-1-place1; //add the objects above the object 1 as in the way
                  ((JSONArray)world.get(column2)).add(statement.get(1)); //add object 1 to column 2
                  if(!Constraints.isColumnAllowed((JSONArray)world.get(column2),objects)){ //and see if column is allowed
                     rowInTheWay ++;
                  }
                  ((JSONArray)world.get(column2)).remove(statement.get(1));
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
                  rowInTheWay += (Math.abs(place1-place2)-1); //add the objects between the objects in the statement as in the way
                  
                  rowInTheWay += ((JSONArray)world.get(column1)).size()-1-Math.max(place1,place2); //add the objects above the top statement object as in the way
            
               } 
               else{//objects are in different columns
                  rowCost += 2;
                  rowInTheWay += ((JSONArray)world.get(column2)).size()-1-place2; //add the objects above the object 2 as in the way
                  ((JSONArray)world.get(column1)).add(statement.get(2)); //add object 2 to column 1
                  if(!Constraints.isColumnAllowed((JSONArray)world.get(column1),objects)){ //and see if column is allowed
                     rowInTheWay ++; //if not say it's in the wa
                  }
                  ((JSONArray)world.get(column1)).remove(statement.get(2));
               }
            }
            else if ("beside".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(column1==column2){//in the same column
                  rowCost += 2;
                  rowInTheWay += ((JSONArray)world.get(column1)).size()-1-Math.max(place1,place2); //add the objects above the top statement object as in the way
               }
               else if((column1-column2 > 1) || (column2-column1 > 1)){//objects are further away
                  rowCost += 2;
                  Math.min(((JSONArray)world.get(column1)).size()-1-place1,((JSONArray)world.get(column2)).size()-1-place2);
               }
            }
            else if ("rightof".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(world.size()-1==column2 && column1==0){//both objects are in the wrong edges
                  
                  rowCost += 4;
                  rowInTheWay+=(((JSONArray)world.get(column1)).size()-1-place1);
                  rowInTheWay+=(((JSONArray)world.get(column2)).size()-1-place2);
                  
               }
               else if(column1==0){
                  rowCost += 2;
                  rowInTheWay+=(((JSONArray)world.get(column1)).size()-1-place1);
               } 
               else if(world.size()-1==column2){
                  rowCost += 2;
                  rowInTheWay+=(((JSONArray)world.get(column2)).size()-1-place2);
               } 
               else if(column1<column2){//object 1 left of object 2
                  
                  rowCost += 2;
                  rowInTheWay+=Math.min(((JSONArray)world.get(column1)).size()-1-place1,((JSONArray)world.get(column2)).size()-1-place2);
                  
               }
               
            }
            else if ("leftof".equals(statement.get(0).toLowerCase())){
               int column1 = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int column2 = WorldFunctions.getColumnNumber(world,statement.get(2));
               int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
               
               if(world.size()-1==column1 && column2==0){
                  
                  rowCost += 4;
                  rowInTheWay+=(((JSONArray)world.get(column1)).size()-1-place1);
                  rowInTheWay+=(((JSONArray)world.get(column2)).size()-1-place2);
                  
               } 
               else if(column2==0){
                  rowCost += 2;
                  rowInTheWay+=(((JSONArray)world.get(column2)).size()-1-place2);
               } 
               else if(world.size()-1==column1){
                  rowCost += 2;
                  rowInTheWay+=(((JSONArray)world.get(column1)).size()-1-place1);
               } 

               else if(column2<column1){//object 1 right of object 2
                  
                  rowCost += 2;
                  rowInTheWay+=Math.min(((JSONArray)world.get(column1)).size()-1-place1,((JSONArray)world.get(column2)).size()-1-place2);
               }

               
            }
         }
         if(rowCost<cost || (rowCost==cost && rowInTheWay < inTheWay)){
            cost=rowCost;
            inTheWay = rowInTheWay;
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
   
   public boolean isBetter(Heuristic2 compHeu){
   
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
