
import java.util.*;
import java.util.regex.Pattern;

public class Tree{
	public Tree(String tree){
		Scanner sc = new Scanner(tree);
		sc.useDelimiter(Pattern.compile(",|\\(|\\)"));
		while(sc.hasNext()){
			String s = sc.next();
			if(s != ""){
				javax.swing.JOptionPane.showMessageDialog(null,s);
			}
		}
	}
}