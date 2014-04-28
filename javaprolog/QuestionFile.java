import java.io.*;
import java.util.*;

public class QuestionFile{
	private static void write(String s){
		PrintWriter writer;
		try{
			File f = new File("Question" + File.separator + "QuestionFile.txt");
			writer = new PrintWriter(f, "UTF-8");
			writer.print(s);
			writer.flush();
			writer.close();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,e);
		}
	}
	public static void writeQuestion(String s){
		write(s);
	}
	public static void reset(){
		write("what can I do for you today?");
	}
	public static boolean haveQuestion(){
		return true;
	}
	public static ArrayList<String> getYesList(){
		return new ArrayList<String>();
	}
	public static ArrayList<String> getNoList(){
		return new ArrayList<String>();
	}
	public static void saveInfo(ArrayList<String> yesList, ArrayList<String> noList){
		
	}
}