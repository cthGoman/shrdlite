
import java.util.*;

public class Statement{
	StatementOperator operator;
	String[] arguments = new String[2];
	
	public Statement(StatementOperator so,String arg1,String arg2){
		operator = so;
		arguments[0] = arg1;
		arguments[1] = arg2;
	}
	
	
	
	
	
	public static enum StatementOperator{
		ONTOPOF,ABOVE,UNDER,BESIDE,LEFTOF,RIGHTOF,HOLD;
	}
}