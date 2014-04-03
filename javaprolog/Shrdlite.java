
// First compile the program:
// javac -cp gnuprologjava-0.2.6.jar:json-simple-1.1.1.jar:. Shrdlite.java

// Then test from the command line:
// java -cp gnuprologjava-0.2.6.jar;json-simple-1.1.1.jar;. Shrdlite < ../examples/small.json

import java.util.List;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import gnu.prolog.term.Term;
import gnu.prolog.vm.PrologException;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Shrdlite {

	public static void main(String[] args) throws PrologException, ParseException, IOException {
         long startTime = System.currentTimeMillis();

         
        
        JSONObject jsinput   = (JSONObject) JSONValue.parse(readFromStdin());
        JSONArray  utterance = (JSONArray)  jsinput.get("utterance");
        JSONArray  world     = (JSONArray)  jsinput.get("world");
        String     holding   = (String)     jsinput.get("holding");
        JSONObject objects   = (JSONObject) jsinput.get("objects");
        
        

        JSONObject result = new JSONObject();
        result.put("utterance", utterance);

        // This is how to get information about the top object in column 1:
        //JSONArray column1 = (JSONArray) world.get(0);
        //String topobject = (String) column1.get(column1.size() - 1);
        //JSONObject objectinfo = (JSONObject) objects.get(topobject);
        //String form = (String) objectinfo.get("form");

        DCGParser parser = new DCGParser("shrdlite_grammar.pl");
        List<Term> trees = parser.parseSentence("command", utterance);
        List tstrs = new ArrayList();
        result.put("trees", tstrs);
        for (Term t : trees) {
            tstrs.add(t.toString());
        }

        if (trees.isEmpty()) {
            result.put("output", "Parse error!");

        } else {
            
            
// //             List<Goal> goals = new ArrayList<Goal>();
// //             Interpreter interpreter = new Interpreter(world, holding, objects);
                       
            //for (Term tree : trees) {
            //    for (Goal goal : interpreter.interpret(tree)) {
            //        goals.add(goal);
            //    }  
            //}
            
            
            List<Goal> goals = new ArrayList<Goal>();
            InterpreterStupid interpreter = new InterpreterStupid(world, holding, objects);
            for (Term tree : trees) {
	            for (Goal goal : interpreter.interpret(tree)) {
		            // goals.add(goal);
	            }
            }
            
            // System.out.println(goals);
                         

            //Generate a test goal
            ArrayList<Statement> row= new ArrayList<Statement>();
            

            row.add(new Statement("ONTOP","g","k"));
            row.add(new Statement("above","e","g"));
            row.add(new Statement("above","l","g"));
            
            

            goals.add(new Goal(row));
            
            
            result.put("goals", goals);

            if (goals.isEmpty()) {
                result.put("output", "Interpretation error!");

            } else if (goals.size() > 1) {
                result.put("output", "Ambiguity error!");

            } else {
               if (holding==null){
                  holding="";
               }
                Planner planner = new Planner(world, holding, objects);
                Plan plan = null;
                if (goals.get(0).get(0).get(0).equals("hold")){
                  plan = planner.solve(goals.get(0),result);
                }else{
                  plan = planner.solve2(goals.get(0),result);
                }
                
                result.put("plan", plan);
                 
					 
                if (plan.isEmpty()) {
                    result.put("output", "Planning error!");
                } else {
                    long endTime   = System.currentTimeMillis();
                    long totalTime = endTime - startTime;
                    result.put("output", "Success! " + totalTime);
                }
            }
        }

        System.out.print(result);
        
        
        System.exit(0);
        
    }

    public static String readFromStdin() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder data = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            data.append(line).append('\n');
        }
        return data.toString();
    }

}

