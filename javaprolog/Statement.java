
import java.util.*;

public class Statement extends ArrayList<String>{

	
	public Statement(String spacialOperator,String arg1,String arg2){
      super.add(spacialOperator);
		super.add(arg1);
		super.add(arg2);
	}
	// This prevents anyone from adding an additional string to the statement.
	// Should also be done for a lot of other methods in arraylist, but nevermind
	public boolean add(String s){
		return false;
	}
	public Object clone(){
		return new Statement(get(0),get(1),get(2));
	}
}