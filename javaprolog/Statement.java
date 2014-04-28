
import java.util.*;

public class Statement extends ArrayList<String>{
// One should protect this file form the use of the inherited methods of ArrayList.
// but since this file isn't ment to be used by others we dont't fix this.
	public Statement(String spatialOperator,String arg1,String arg2){
      super.add(spatialOperator);
		super.add(arg1);
		super.add(arg2);
	}
	public Object clone(){
		return new Statement(get(0),get(1),get(2));
	}
}