
public class Tree{
	private Node masterNode = new Node(sc.next(),null);
	private Node currentNode = masterNode;
	public Tree(String tree){
		Searcher sc = new Searcher(tree);
		sc.useDelimiter("(),");
		while(sc.hasNext()){
			String s = sc.next();
			if(s == "("){
			}
			else if(s == ")"){
				currentNode = currentNode.getParent();
			}
			else if(s == ","){
				currentNode = currentNode.getParent();
			}
			else{
				currentNode = currentNode.addChild(s);
			}        
		}
	}
	fixInitialSide(JSONObject objects){
		currentNode = masterNode.getChild(0);
		LinkedList<String> Objects = new LinkedList<String>();
		
		
		
		JSONObject objectinfo = (JSONObject) objects.get(topobject);
		String form = (String) objectinfo.get("form")
	}
}