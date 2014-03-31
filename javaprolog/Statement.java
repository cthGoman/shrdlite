
import java.util.*;

public class Statement extends ArrayList<String>{

	
	public Statement(String spatialOperator,String arg1,String arg2){
      super.add(spatialOperator);
		super.add(arg1);
		super.add(arg2);
	}
	// This prevents anyone from adding an additional string to the statement.
	// Should also be done for a lot of other methods in arraylist, but nevermind
	public boolean add(String s){
		return false;
	}
}