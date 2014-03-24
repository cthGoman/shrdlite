import java.util.List;
import java.util.ArrayList;


public class Goal{
    
    private ArrayList<ArrayList<Statement>> pddl;
    
    public Goal(ArrayList<Statement> newRow) {
      pddl = new ArrayList<ArrayList<Statement>>();
      pddl.add(newRow);
          
    }
    public String toString()
    {
      String output;
      
      return "true";
    }
    
    public void addRow(ArrayList<Statement> newRow){
      pddl.add(newRow);
    }
}