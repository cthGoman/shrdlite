
import java.util.*;
import java.util.regex.Pattern;

public class Tree{
	public Tree(String tree){
      Node currentNode = new Node("",null);
      String lastName;
		Searcher sc = new Searcher(tree);
		sc.useDelimiter("(),");
		while(sc.hasNext()){
			String s = sc.next();
			if(s != ""){
				javax.swing.JOptionPane.showMessageDialog(null,s);
			}
         else if(s == "("){
            currentNode = currentNode.addChild(lastName);
         }
         else if(s == ")"){
            currentNode.addChild(lastName);
            currentNode = currentNode.parent;
         }
         else if(s == ","){
            currentNode.addChild(lastName);
         }
         else{
         lastName = s;
         }
         
          
		}
	}
}