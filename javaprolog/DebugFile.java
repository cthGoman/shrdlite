import java.io.*;

public class DebugFile{
	private static PrintWriter writer;
	public static void start(){
		try{
			writer = new PrintWriter("DebugFile.txt", "UTF-8");
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,e);
		}
	}
	public static void print(String s){
		writer.print(s);
	}
	public static void println(String s){
		writer.println(s);
	}
	public static void stop(){
		writer.flush();
		writer.close();
	}
}