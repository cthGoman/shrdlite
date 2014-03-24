
import java.util.*;

public class Statement{
	final StatementOperator operator;
	final ArrayList<String> arguments = new ArrayList<String>();
	
	public Statement(StatementOperator so,ArrayList<String> arg){
		operator = so;
		for(String s:arg){
			arguments.add(s);
		}
	}
	
	
	
	
	public static enum StatementOperator{
		ONTOPOF,ABOVE,UNDER,BESIDE,LEFTOF,RIGHTOF;
	}
}