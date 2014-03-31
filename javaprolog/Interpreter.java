
import java.util.*;

import gnu.prolog.term.Term;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Interpreter{
	JSONObject objects;
	Interpreter(JSONArray world, String holding, JSONObject objects){
		this.objects = objects;
	}
	public List<Goal> interpret(Term input){
		Tree tree = new Tree(input.toString().replace("(-)","-"));
		tree.fixInitialSide(objects);
		
		
		
		
		return new LinkedList<Goal>();
	}
}