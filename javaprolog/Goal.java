import java.util.List;
import java.util.ArrayList;


public class Goal extends ArrayList<ArrayList<Statement>>{
    
    public Goal(ArrayList<Statement> newRow) {

      this.add(newRow);
          
    }
    public String toString()
    {
      String output;
      
      return "true";
    }
    
    public void addRow(ArrayList<Statement> newRow){
      this.add(newRow);
    }
}