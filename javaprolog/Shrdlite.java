
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
   public static long startTime=0;
	public static void main(String[] args) throws PrologException, ParseException, IOException {
		startTime = System.currentTimeMillis();

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
		ArrayList<String> tstrs = new ArrayList<String>();
		if(!QuestionFile.haveQuestion()){
			List<Term> trees = parser.parseSentence("command", utterance);
			result.put("trees", tstrs);
			for (Term t : trees) {
				tstrs.add(t.toString());
			}
		}else{
			List<Term> answer = parser.parseSentence("answer", utterance);
			if(answer.size() > 0){
				if(answer.get(0).toString().equals("yes")){
					tstrs = QuestionFile.getYesList();
					QuestionFile.reset();
				}else if(answer.get(0).toString().equals("no")){
					tstrs = QuestionFile.getNoList();
					QuestionFile.reset();
				}else{
					result.put("output", "That's not a yes or no answer!");
				}
			}else{
				result.put("output", "That's not a yes or no answer!");
			}
			result.put("trees", tstrs);
		}
		if (tstrs.isEmpty()) {
			result.put("output", "Parse error!");
		} else {
			List<Goal> goals = new ArrayList<Goal>();
			Interpreter interpreter = new Interpreter(world, holding, objects);
			ArrayList<String> treesWithGoal = new ArrayList<String>();
			for (String tree : tstrs) {
				for (Goal goal : interpreter.interpret(tree)) {
					goals.add(goal);
					treesWithGoal.add(tree.replace("(-)","-")); //max one goal per tree possible, so this is ok.
				}
			}
			result.put("goals", goals);
			if (goals.isEmpty()) {
				result.put("output", "Impossible task!");
			} else if (goals.size() > 1) {
				String question = Ask.question(treesWithGoal);
				QuestionFile.writeQuestion(question);
				result.put("output", "Ambiguity!");
//				result.put("output", question);
			} else {
// 				if (holding==null){
// 					holding="";
// 				}
				Planner planner = new Planner(world, holding, objects);
				Plan plan = null;
//             // System.out.println(goals.get(0).get(0).get(0).get(0));
            long endTime   = System.currentTimeMillis();
			   long totalTime2 = endTime - startTime;
// 				if (goals.get(0).get(0).get(0).get(0).equals("hold")){
// 					plan = planner.solve(goals.get(0),result);
//                // System.out.println("tjoho");
// 				}else{
					plan = planner.solve4(goals.get(0),result);
// 				}
				result.put("plan", plan);
				if (plan.isEmpty()) {
					result.put("output", "Planning error!");
				} else {
					endTime   = System.currentTimeMillis();
					long totalTime = endTime - startTime;
					result.put("output", "Success! " + totalTime + " "+ totalTime2 + " " + plan.size());
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

