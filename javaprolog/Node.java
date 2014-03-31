
import java.util.ArrayList;

public class Node{
	String value;
	Node parent;
	ArrayList<Node> children = new ArrayList<Node>();
	
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
}