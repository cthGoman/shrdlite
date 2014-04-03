
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public static class InitalState{
	public static void fixInitialSide(Node masterNode, JSONObject objectsInformation, ArrayList<ArrayList<String>> world){
		Node currentNode = masterNode;
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