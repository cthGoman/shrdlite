
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class InitialState{
	public static ArrayList<String> getInitialObjects(Node masterNode, JSONObject objectsInformation, ArrayList<ArrayList<String>> world){
		Node currentNode = masterNode.getChild(0);
		int l = Math.min(currentNode.getValue().length(),8);
		while(currentNode.getValue().substring(0,l).equals("relative")){
			currentNode = currentNode.getLastChild();
			l = Math.min(currentNode.getValue().length(),8);
		}
		ArrayList<String> oldObjects = FindObject.match(currentNode.createObject(),objectsInformation,world);
DebugFile.println(""+oldObjects);
		currentNode = currentNode.getParent();
		String relation = currentNode.getChild(0).getValue();
		currentNode = currentNode.getParent();
		while(currentNode != null){
			ArrayList<String> newObjects = FindObject.match(currentNode.createObject(),objectsInformation,world);
DebugFile.println(""+newObjects);
			oldObjects = FindObject.relatedObject(newObjects,relation,oldObjects,world);
DebugFile.println(""+oldObjects);
			currentNode = currentNode.getParent();
			relation = currentNode.getChild(0).getValue();
			currentNode = currentNode.getParent();
		}
		return oldObjects;
	}
}