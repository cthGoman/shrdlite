import java.io.*;
import java.util.*;

/**Creats files in the DebugFile folder with prints from the program.<br>
*This class shouldn't be used anywhere in the final version.*/
public class DebugFile{
	private static int active = 0;
	private static LinkedList<PrintWriter> writers = new LinkedList<PrintWriter>();
	/**Initalize a new file to write to.<br>
	*only one can be open at a time*/
	public static void start(){
		try{
			active = writers.size();
			File f = new File("DebugFile" + File.separator + active + ".txt");
			writers.add(new PrintWriter(f, "UTF-8"));
		}catch(Exception e){
			javax.swing.JOptionPane.showMessageDialog(null,e);
		}
	}
	/**Print a String to file without adding new line.
	*@param s The string to be printed.*/
	public static void print(String s){
		writers.get(active).print(s);
	}
	/**Print a String to file and adding new line.
	*@param s The string to be printed.*/
	public static void println(String s){
		writers.get(active).println(s);
	}
	/**Close a file and makes all output to it.<br>
	*Must be done before execution is stopped, or no info will appear in the file.*/
	public static void stop(){
		writers.get(active).flush();
		writers.get(active).close();
	}
}