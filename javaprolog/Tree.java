
import java.util.*;
import java.util.regex.Pattern;

public class Tree{
	public Tree(String tree){
		Searcher sc = new Searcher(tree);
		sc.useDelimiter("(),");
		Node masterNode = new Node(sc.next(),null)
      Node currentNode = masterNode;
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
}