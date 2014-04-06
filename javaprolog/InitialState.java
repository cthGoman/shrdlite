
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
		ArrayList<String> oldObjects = FindObject.match(currentNode.createObject(),objectsInformation,world);
		currentNode = currentNode.getParent();
		String relation = currentNode.getChild(0).getValue();
		currentNode = currentNode.getParent();
		while(currentNode != null){
			ArrayList<String> newObjects = FindObject.match(currentNode.createObject(),objectsInformation,world);
			oldObjects = FindObject.relatedObject(newObjects,relation,oldObjects,world);
			currentNode = currentNode.getParent();
			relation = currentNode.getChild(0).getValue();
			currentNode = currentNode.getParent();
		}
		return oldObjects;
	}
}