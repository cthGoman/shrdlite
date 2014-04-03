
public class Tree{
	private Node masterNode;
	public Tree(String tree){
		Searcher sc = new Searcher(tree);
		sc.useDelimiter("(),");
		masterNode = new Node(sc.next(),null);
		Node currentNode = masterNode;
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
}