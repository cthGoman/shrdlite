
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Tree{
	private Node masterNode;
	private Node currentNode;
	public Tree(String tree){
		Searcher sc = new Searcher(tree);
		sc.useDelimiter("(),");
		masterNode = new Node(sc.next(),null);
		currentNode = masterNode;
		while(sc.hasNext()){
			String s = sc.next();
			if(s.equals("(")){
			}
			else if(s.equals(")")){
				currentNode = currentNode.getParent();
			}
			else if(s.equals(",")){
				currentNode = currentNode.getParent();
			}
			else{
				currentNode = currentNode.addChild(s);
			}        
		}
	}
	public void fixInitialSide(JSONObject objectsInformation, ArrayList<ArrayList<String>> world){
		LinkedList<LinkedList<String>> objects = new LinkedList<LinkedList<String>>();
		LinkedList<String> relations = new LinkedList<String>();
		masterNode.getChild(0).createObjects(objects,relations);
		
		DebugFile.println("Objects size: " + objects.size());
		for(int i = 0 ; i < objects.size() ;i++){
			for(String s:objects.get(i)){
				DebugFile.print(s + " ");
			}
			DebugFile.println("");
			if(i < relations.size()){
				DebugFile.print(relations.get(i));
			}
		}
		
		LinkedList<String> possibilities = new LinkedList<String>();
		for(ArrayList<String> als:world){
			for(String s:als){
				possibilities.add(s);
			}
		}
		
		
//		JSONObject objectinfo = (JSONObject) objects.get(topobject);
//		String form = (String) objectinfo.get("form");
	}
}