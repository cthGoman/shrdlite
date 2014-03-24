
import java.util.*;

public class Statement extends ArrayList<String>{

	
	public Statement(String spacialOperator,String arg1,String arg2){
      this.add(spacialOperator);
		this.add(arg1);
		this.add(arg2);
	}
}