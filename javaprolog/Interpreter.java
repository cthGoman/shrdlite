
import java.util.*;

import gnu.prolog.term.Term;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Interpreter{
	Interpreter(JSONArray world, String holding, JSONObject objects){
		
	}
	public List<Goal> interpret(Term input){
		Tree tree = new Tree(input.toString().replace("(-)","-"));
		return new LinkedList<Goal>();
	}
}