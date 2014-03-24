import java.util.List;
import java.util.ArrayList;


public class Goal{
    
    //private ArrayList<ArrayList<Statement>>  = new ArrayList<new ArrayList<Statement>()>()
    private ArrayList<ArrayList<Statement>> PDDL;
    public Goal() {
        
    }
    public String toString()
    {
      return "true";
    }
    
    public void addRow(ArrayList<Statement> newRow){
         pddl.add(newRow);
    }
}