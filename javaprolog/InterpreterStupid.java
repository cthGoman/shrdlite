
import java.util.*;

import gnu.prolog.term.Term;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class InterpreterStupid{
	InterpreterStupid(JSONArray world, String holding, JSONObject objects){
		
	}
	public List<Goal> interpret(Term input){
		LinkedList<Goal> gl = new LinkedList<Goal>();
		Goal g = new Goal();
		// put the white ball in a large box on the floor
		g.addStatement(0,new Statement("ontop","e","l"));
		g.addStatement(0,new Statement("ontop","l","floor-1"));
		g.addStatement(1,new Statement("ontop","e","l"));
		g.addStatement(1,new Statement("ontop","l","floor-2"));
		g.addStatement(2,new Statement("ontop","e","l"));
		g.addStatement(2,new Statement("ontop","l","floor-3"));
		g.addStatement(3,new Statement("ontop","e","l"));
		g.addStatement(3,new Statement("ontop","l","floor-4"));
		g.addStatement(4,new Statement("ontop","e","l"));
		g.addStatement(4,new Statement("ontop","l","floor-5"));
		g.addStatement(5,new Statement("ontop","e","k"));
		g.addStatement(5,new Statement("ontop","k","floor-1"));
		g.addStatement(6,new Statement("ontop","e","k"));
		g.addStatement(6,new Statement("ontop","k","floor-2"));
		g.addStatement(7,new Statement("ontop","e","k"));
		g.addStatement(7,new Statement("ontop","k","floor-3"));
		g.addStatement(8,new Statement("ontop","e","k"));
		g.addStatement(8,new Statement("ontop","k","floor-4"));
		g.addStatement(9,new Statement("ontop","e","k"));
		g.addStatement(9,new Statement("ontop","k","floor-5"));
		
      Goal g2 = new Goal();
      g2.addStatement(0,new Statement("ontop","l","floor-2"));
      g2.addStatement(0,new Statement("ontop","k","floor-3"));
      g2.addStatement(0,new Statement("ontop","m","floor-4"));
      
      g2.addStatement(1,new Statement("ontop","l","floor-0"));
      g2.addStatement(1,new Statement("ontop","k","floor-2"));
      g2.addStatement(1,new Statement("ontop","m","floor-4"));      
      gl.add(g2);
      
      
		return gl;
	}
}