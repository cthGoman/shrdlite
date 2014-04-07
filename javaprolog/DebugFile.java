import java.io.*;
import java.util.*;

public class DebugFile{
	private static int active = 0;
	private static LinkedList<PrintWriter> writers = new LinkedList<PrintWriter>();
	public static void start(){
		try{
			active = writers.size();
			File f = new File("DebugFile" + File.separator + active + ".txt");
			writers.add(new PrintWriter(f, "UTF-8"));
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,e);
		}
	}
	public static void print(String s){
		writers.get(active).print(s);
	}
	public static void println(String s){
		writers.get(active).println(s);
	}
	public static void stop(){
		writers.get(active).flush();
		writers.get(active).close();
	}
}