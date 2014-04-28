import java.util.*;

public class Ask{
	public static String question(ArrayList<String> trees){
		
		ArrayList<String> yesList = new ArrayList<String>();
		ArrayList<String> noList = new ArrayList<String>();
		QuestionFile.saveInfo(yesList,noList);
		return "?";
	}
}