import java.io.*;
import java.util.*;

/**Creats files in the DebugFile folder with prints from the program.<br>
*This class shouldn't be used anywhere in the final version.*/
public class DebugFile{
	/**An internal int that keeps track of which file that the information should be written to.*/
	private static int active = 0;
	/**All PrintWriters currently in use.*/
	private static LinkedList<PrintWriter> writers = new LinkedList<PrintWriter>();
	/**Initalize a new file to write to.<br>
	*Only one can be open at a time*/
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
	*@param o The Object that is converted to the printed String.*/
	public static void print(Object o){
		writers.get(active).print(o.toString());
	}
	/**Print a String to file and adding new line.
	*@param o The Object that is converted to the printed String.*/
	public static void println(Object o){
		writers.get(active).println(o.toString());
	}
	/**Close a file and makes all output to it.<br>
	*Must be done before execution is stopped, or no info will appear in the file.*/
	public static void stop(){
		writers.get(active).flush();
		writers.get(active).close();
	}
}