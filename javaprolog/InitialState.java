
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class InitialState{
	public static ArrayList<String> getInitialObjects(Node masterNode, JSONObject objectsInformation, ArrayList<ArrayList<String>> world){
		Node currentNode = masterNode.getChild(0);
		int k = 0;
		int l = Math.min(currentNode.getValue().length(),8);
DebugFile.println(currentNode.getValue().substring(0,l) + "\t" + k);
		while(currentNode.getValue().substring(0,l).equals("relative")){
			currentNode = currentNode.getLastChild();
			k++;
			l = Math.min(currentNode.getValue().length(),8);
DebugFile.println(currentNode.getValue().substring(0,l) + "\t" + k);
		}
		ArrayList<String> firstObject = currentNode.createObject();
		ArrayList<String> oldObject = new ArrayList<String>();
		if(firstObject.size() > 0){
			if(firstObject.get(0).equals("floor")){
				for(int i = 0 ; i < world.size() - 1 ; i++){
					oldObject.add("floor-"+i);
				}
			}
		}
//		ArrayList<String> newObjects = FindObject.match(,objectsInformation,world);

// 		//oldObjects = FindObject.union(oldObjects , newObjects);
	return new ArrayList<String>();
	}
}