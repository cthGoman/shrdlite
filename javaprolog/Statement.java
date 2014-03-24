
import java.util.*;

public class Statement extends ArrayList<String>{

	
	public Statement(StatementOperator so,String arg1,String arg2){
      this.add(so.toString());
		this.add(arg1);
		this.add(arg2);
	}
	
	
	
	
	
	public static enum StatementOperator{
		ONTOPOF,ABOVE,UNDER,BESIDE,LEFTOF,RIGHTOF,HOLD;
	}
}