import java.util.List;
import java.util.ArrayList;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Goal extends ArrayList<ArrayList<Statement>>{  
	public Goal(){}
	public Goal(ArrayList<Statement> newRow) {
		this.add(newRow);
	}
	public boolean addStatement(int position,Statement s){
		if(position < 0){
			return false;
		}
		for(int i = this.size() ; i <= position ; i++ ){
			addRow(new ArrayList<Statement>());
		}
		get(position).add(s);
		return true;
	}
	public void addRow(ArrayList<Statement> newRow){
		this.add(newRow);
	}
   
   public boolean fulfiled(JSONArray world){
      boolean fulfilled = true;
      
      for (ArrayList<Statement> listOfStatement : this){//Loop over all rows 
         fulfilled = true;
         
         for (Statement statement : listOfStatement) {//Loop over every statement in row
         
            if (statement.get(0).equals("ontop")){
               int column = WorldFunctions.getColumnNumber(world,statement.get(1));
               int place = WorldFunctions.getPlaceInColumn(world,statement.get(1));

               if (!WorldFunctions.getObjectBelow(world,statement.get(1)).equals(statement.get(2))){
                  fulfilled = false;
               }
               
            }

         }        
         
      
      
      }
      
      return fulfilled;
   }
   
   
}