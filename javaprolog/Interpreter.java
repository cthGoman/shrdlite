
import java.util.*;

import gnu.prolog.term.Term;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Interpreter{
	private JSONObject objects;
	private ArrayList<ArrayList<String>> worldList = new ArrayList<ArrayList<String>>();
	Interpreter(JSONArray world, String holding, JSONObject objects){
		this.objects = objects;
		for(int i = 0 ; i < world.size() ; i++){
			JSONArray column = (JSONArray)world.get(i);
			worldList.add(new ArrayList<String>());
			for(int j = 0 ; j < column.size() ; j++){
				worldList.get(i).add((String)column.get(j));
			}
		}
		ArrayList<String> hold = new ArrayList<String>();
		if(holding != null){
			hold.add(holding);
		}
		worldList.add(hold);
	}
	public List<Goal> interpret(String input){
		Tree tree = new Tree(input.replace("(-)","-"));
		ArrayList<String> object0 = InitialState.getInitialObjects(tree.getMasterNode(),objects,worldList);
		Goal goal = null;
		if(tree.getMasterNode().getValue().equals("move")){
			goal = Relations.relation(objects,worldList,tree,object0,tree.getMasterNode().getPositionOfAll());
		}else if(tree.getMasterNode().getValue().equals("take")){
			goal = new Goal();
			for(int i = 0 ; i < object0.size() ; i++){
				goal.addStatement(i, new Statement("hold","robot-0",object0.get(i)));
			}
		}
      LinkedList<Goal> goalList = new LinkedList<Goal>();
		if(goal != null && goal.size() != 0){
			goalList.add(goal);
		}
		return goalList;
	}
}