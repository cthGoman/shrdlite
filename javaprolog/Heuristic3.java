import org.json.simple.*;
import java.util.List;
import java.util.ArrayList;


public class Heuristic3{
   private SetCost setCost;
   
   public Heuristic3(JSONArray world, String holding){
      setCost = new SetCost(world, holding, -1);
      
   }

	public Heuristic3(JSONArray world, String holding, Goal goal, JSONObject objects) {
		
      setCost = new SetCost(world, holding, -1);
      
    for (ArrayList<Statement> listOfStatement : goal){//Loop over all rows 

         SetCost rowSetCost = new SetCost(world, holding);
         
         for (Statement statement : listOfStatement) {//Loop over every statement in row
            int columnNr1 = WorldFunctions.getColumnNumber(world,statement.get(1));
            int place1 = WorldFunctions.getPlaceInColumn(world,statement.get(1));
            String obj1 = statement.get(1);
            JSONArray column1 = (JSONArray)world.get(columnNr1);
            
            int columnNr2 = WorldFunctions.getColumnNumber(world,statement.get(2));
            int place2 = WorldFunctions.getPlaceInColumn(world,statement.get(2));
            String obj2 = statement.get(2);
            JSONArray column2 = (JSONArray)world.get(columnNr2);

         
            if ("ontop".equals(statement.get(0).toLowerCase()) || "inside".equals(statement.get(0).toLowerCase())){
               
               if(columnNr1==columnNr2){//in the same column
                  if (!WorldFunctions.getObjectBelow(world,obj1).equals(obj2)){//But not ontop
                     if(place1 < place2){ //object 1 below object 2
                        rowSetCost.move(obj1,column1);
                     }
                     else{ //move objects above object 2 and move object 1 twice
                        rowSetCost.moveAbove(obj2,column2);
                        rowSetCost.doubleMove(obj1,column1);
                     }   
                  }
               } 
               else{//objects are in different columns
                  rowSetCost.moveAbove(obj2,column2);
                  rowSetCost.move(obj1,column1);
               }
            }
            else if ("above".equals(statement.get(0).toLowerCase())){
               
               if(columnNr1==columnNr2){//in the same column
                  if (place1<place2){//But object 1 is below
                     rowSetCost.move(obj1,column1);
                  }
               } 
               else{//objects are in different columns
                  rowSetCost.move(obj1,column1);
                  column2.add(obj1);
                  if(!Constraints.isColumnAllowed(column2,objects)){ //and see if column is allowed
                     column2.remove(obj1);
                     rowSetCost.move(WorldFunctions.getTopObjectWorldColumn(world,columnNr2),column2);
                  }
                  else{
                     column2.remove(obj1);
                  }
               }
            }
            else if ("below".equals(statement.get(0).toLowerCase())){
               
               if(columnNr2==columnNr1){//in the same column
                  if (place2<place1){//But object 2 is below
                     rowSetCost.move(obj2,column2);
                  }
            
               } 
               else{//objects are in different columns
                  rowSetCost.move(obj2,column2);
                  column1.add(obj2);
                  if(!Constraints.isColumnAllowed(column1,objects)){ //and see if column is allowed
                     column1.remove(obj2);
                     rowSetCost.move(WorldFunctions.getTopObjectWorldColumn(world,columnNr1),column1);
                  }
                  else{
                     column1.remove(obj2);
                  }
               }
            }
            else if ("beside".equals(statement.get(0).toLowerCase())){
               
               if(columnNr1==columnNr2){//in the same column
                  rowSetCost.moveMin(obj1,obj2,column1,column2);
               }
               else if((columnNr1-columnNr2 > 1) || (columnNr2-columnNr1 > 1)){//objects are further away
                  rowSetCost.moveMin(obj1,obj2,column1,column2);
               }
            }
            else if ("rightof".equals(statement.get(0).toLowerCase())){
               
               if(world.size()-1==columnNr2 && columnNr1==0){//both objects are in the wrong edges
                  
                  rowSetCost.move(obj1,column1);
                  rowSetCost.move(obj2,column2);
                  
               }
               else if(columnNr1==0){
                  rowSetCost.move(obj1,column1);
               } 
               else if(world.size()-1==columnNr2){
                  rowSetCost.move(obj2,column2);;
               } 
               else if(columnNr1<=columnNr2){//object 1 left of object 2
                  rowSetCost.moveMin(obj1,obj2,column1,column2);
               }
               
            }
            else if ("leftof".equals(statement.get(0).toLowerCase())){
               
               if(world.size()-1==columnNr1 && columnNr2==0){
                  
                  rowSetCost.move(obj1,column1);
                  rowSetCost.move(obj2,column2);
                  
                  
               } 
               else if(columnNr2==0){
                  rowSetCost.move(obj2,column2);;
               } 
               else if(world.size()-1==columnNr1){
                  rowSetCost.move(obj1,column1);
               } 

               else if(columnNr2<=columnNr1){//object 1 right of object 2
                  rowSetCost.moveMin(obj1,obj2,column1,column2);
               }   
            }
            else if ("hold".equals(statement.get(0).toLowerCase()) && !holding.equals(obj2)){
               rowSetCost.moveAbove(obj2,column2);            
            }
         }
         if(rowSetCost.sum()<setCost.sum()){
            setCost=rowSetCost;
         }
         
    }
		
	}
   
   public int getCost(){
      return setCost.sum();
   }
   
   
   public boolean isBetter(Heuristic3 compHeu){
   
      if (compHeu.getCost()>setCost.sum()){
         return true;
      }

      else{
         return false;
      }
   
   
   }
}
