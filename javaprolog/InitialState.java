
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class InitialState{
	public static void getInitialObjects(Node masterNode, JSONObject objectsInformation, ArrayList<ArrayList<String>> world){
		Node currentNode = masterNode.getChild(0);
		int k = 0;
DebugFile.println(currentNode.getValue().substring(0,8) + "\t" + k);
		while(currentNode.getValue().substring(0,8).equals("relative")){
			currentNode = currentNode.getLastChild();
			k++;
DebugFile.println(currentNode.getValue().substring(0,8) + "\t" + k);
		}
		
		currentNode = currentNode.getLastChild();
		LinkedList<String> object = new LinkedList<String>();
		for(int i = 0 ; i < currentNode.getChildren() ; i++){
			object.add(currentNode.getChild(i).getValue());
		}
		ArrayList<String> = newObjects = FindObject.match(object,objectsInformation,world);
		oldObjects = FindObject.union(oldObjects , newObjects);
	}
}