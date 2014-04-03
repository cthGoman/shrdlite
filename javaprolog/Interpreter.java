
import java.util.*;

import gnu.prolog.term.Term;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Interpreter{
	private JSONObject objects;
	private ArrayList<ArrayList<String>> worldList = new ArrayList<ArrayList<String>>();
	Interpreter(JSONArray world, String holding, JSONObject objects){
DebugFile.start();
		this.objects = objects;
		for(int i = 0 ; i < world.size() ; i++){
			JSONArray column = (JSONArray)world.get(i);
			worldList.add(new ArrayList<String>());
			for(int j = 0 ; j < column.size() ; j++){
				worldList.get(i).add((String)column.get(j));
			}
		}
		ArrayList<String> hold = new ArrayList<String>();
		hold.add(holding);
		worldList.add(hold);
	}
	public List<Goal> interpret(Term input){
		Tree tree = new Tree(input.toString().replace("(-)","-"));
		InitialState.getInitialObjects(tree.getMasterNode(),objects,worldList);
		
		
		
DebugFile.stop();
		return new LinkedList<Goal>();
	}
}