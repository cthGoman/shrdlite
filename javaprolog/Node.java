
import java.util.*;

public class Node{
	private String value;
	private Node parent;
	private ArrayList<Node> children = new ArrayList<Node>();
	
	public Node(String s, Node p){
		value = s;
		parent = p;
	}
	public Node addChild(String s){
		Node newchild = new Node(s,this);
      children.add(newchild);
      return newchild;
	}
   public Node getParent(){
      return parent;
   }
	public Node getLastChild(){
		if(children.size()==0){
			return null;
		}
		return children.get(children.size() - 1);
	}
	public Node getChild(int position){
		if(position < 0 || position >= children.size()){
			return null;
		}
		return children.get(position);
	}
	public int getChildren(){
		return children.size();
	}
	public String getValue(){
		return value;
	}
	public void createObjects(LinkedList<LinkedList<String>> objects, LinkedList<String> relations){
		if(value.equals("object")){
			LinkedList<String> object = new LinkedList<String>();
			value = objects.size() + "";
			object.add(value);;
			for(Node child:children){
				object.add(child.value);
			}
			objects.add(object);
		}else{
			if(value.equals("relative")){
				relations.add(children.get(0).value);
			}
			for(int i = 1 ; i < children.size() ; i++){
				children.get(i).createObjects(objects,relations);
			}
		}
	}
	public ArrayList<String> createObject(){
		ArrayList<String> object = new ArrayList<String>();
		if(value.equals("object")){
			for(Node child:children){
				object.add(child.value);
			}
		}
		if(value.equals("floor")){
			object.add(value);
		}
	return object;
	}
}