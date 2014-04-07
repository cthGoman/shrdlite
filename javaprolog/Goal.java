import java.util.List;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Goal extends ArrayList<ArrayList<Statement>>{  
	public Goal(){}
	public Goal(ArrayList<Statement> newCondition) {
		add(newCondition);
	}
	public boolean addStatement(int position,Statement s){
		if(position < 0){
			return false;
		}
		for(int i = this.size() ; i <= position ; i++ ){
			add(new ArrayList<Statement>());
		}
		get(position).add(s);
		return true;
	}
	public boolean cloneCondition(int position){
		if(position < 0 || position >= this.size()){
			return false;
		}
		ArrayList<Statement> toBeCloned = get(position);
		ArrayList<Statement> clone = new ArrayList<Statement>();
		for(int i = 0 ; i < toBeCloned.size() ; i++){
			clone.add((Statement)(toBeCloned.get(i).clone()));
		}
		add(position,clone);
		return true;
	}
	public void addCondition(ArrayList<Statement> newCondition){
		add(newCondition);
	}
   public boolean fulfilled(JSONArray world, String holding){
      for (ArrayList<Statement> listOfStatement : this){//Loop over all rows 
         boolean tempFulfilled = true;
         for (Statement statement : listOfStatement) {//Loop over every statement in row
            if ("ontop".equals(statement.get(0).toLowerCase())){
               int column = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               if (!WorldFunctions.getObjectBelow(world,statement.get(1)).equals(statement.get(2))){
                  tempFulfilled = false;
               }
            }
            else if ("above".equals(statement.get(0).toLowerCase())){
               int column = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int placeSecondObject=WorldFunctions.getPlaceInColumn(world,statement.get(2));
               int columnSecondObject = WorldFunctions.getColumnNumber(world,statement.get(2));
               if (placeSecondObject<0 || place<=placeSecondObject || column!=columnSecondObject){
                 tempFulfilled = false; 
               }
            }
            else if ("under".equals(statement.get(0).toLowerCase())){
               int column = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place = WorldFunctions.getPlaceInColumn(world,statement.get(1));
               int placeSecondObject=WorldFunctions.getPlaceInColumn(world,statement.get(2));
               int columnSecondObject = WorldFunctions.getColumnNumber(world,statement.get(2));
               if (placeSecondObject<0 || place>=placeSecondObject || column!=columnSecondObject){
                 tempFulfilled = false; 
               }
            }
            else if ("beside".equals(statement.get(0).toLowerCase())){
               int column = WorldFunctions.getColumnNumber(world,statement.get(1));
               int secondColumn=WorldFunctions.getColumnNumber(world,statement.get(2));
               if (column!=(secondColumn-1) && column!=(secondColumn+1) || column==-1 || secondColumn==-1){
                 tempFulfilled = false; 
               }
            }
            else if ("leftof".equals(statement.get(0).toLowerCase())){
               int column = WorldFunctions.getColumnNumber(world,statement.get(1));             
               int secondColumn=WorldFunctions.getColumnNumber(world,statement.get(2));
               if (column>secondColumn || column==-1 || secondColumn==-1){
                 tempFulfilled = false; 
               }
            }
            else if ("rightof".equals(statement.get(0).toLowerCase())){
               int column = WorldFunctions.getColumnNumber(world,statement.get(1));
               int secondColumn=WorldFunctions.getColumnNumber(world,statement.get(2));
               if (column<secondColumn || column==-1 || secondColumn==-1){
                 tempFulfilled = false; 
               }
            }
            else if ("hold".equals(statement.get(0).toLowerCase())){
               if (!holding.equals(statement.get(1))){
                 tempFulfilled = false; 
               }
            }
            else if ("drop".equals(statement.get(0).toLowerCase())){
               if (holding.equals(statement.get(1))){
                 tempFulfilled = false; 
               }
            }                
         }
         if (tempFulfilled){
            return tempFulfilled;
         }         
      }
      return false;
   }
}