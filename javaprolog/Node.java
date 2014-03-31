
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
	public Node getChild(int position){
		if(position < 0 || position >= children.size()){
			return null;
		}
		return children.get(position);
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
}