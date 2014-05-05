import java.io.*;
import java.util.*;

public class QuestionFile{
<<<<<<< HEAD
=======
	public static void writeQuestion(String s){
		write(s);
	}
	public static void reset(){
		write("What can I do for you today?");
		PrintWriter writer;
		try{
			File f = new File("Question" + File.separator + "QuestionInfo.txt");
			writer = new PrintWriter(f, "UTF-8");
			writer.println(0);
			writer.flush();
			writer.close();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,e);
		}
	}
>>>>>>> origin/Interpreter
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
<<<<<<< HEAD
	public static void writeQuestion(String s){
		write(s);
	}
	public static void reset(){
		write("what can I do for you today?");
=======
	public static boolean haveQuestion(){
		try{
			Scanner sc = new Scanner(new File("Question" + File.separator + "QuestionInfo.txt"));
			String s = sc.nextLine();
			return s.charAt(0) == '1';
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,e);
		}
		return false;
	}
	public static ArrayList<String> getYesList(){
		ArrayList<String> yesList = new ArrayList<String>();
		try{
			Scanner sc = new Scanner(new File("Question" + File.separator + "QuestionInfo.txt"));
			while(sc.hasNext()){
				if(sc.next().equals("Yes:")){
					int j = sc.nextInt();
					sc.nextLine();
					for(int i = j ; i > 0 ; i--){
						String s=sc.nextLine();
						yesList.add(s);
					}
					break;
				}
			}
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,e);
		}
		return yesList;
	}
	public static ArrayList<String> getNoList(){
		ArrayList<String> noList = new ArrayList<String>();
		try{
			Scanner sc = new Scanner(new File("Question" + File.separator + "QuestionInfo.txt"));
			while(sc.hasNext()){
				if(sc.next().equals("No:")){
					int j = sc.nextInt();
					sc.nextLine();
					for(int i = j ; i > 0 ; i--){
						noList.add(sc.nextLine());
					}
					break;
				}
			}
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,e);
		}
		return noList;
	}
	public static void saveInfo(ArrayList<String> yesList, ArrayList<String> noList){
		PrintWriter writer;
		try{
			File f = new File("Question" + File.separator + "QuestionInfo.txt");
			writer = new PrintWriter(f, "UTF-8");
			writer.println(1);
			writer.println("Yes: "+yesList.size());
			for(String s:yesList){writer.println(s);}
			writer.println("No: "+noList.size());
			for(String s:noList){writer.println(s);}
			writer.flush();
			writer.close();
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,e);
		}
>>>>>>> origin/Interpreter
	}
}