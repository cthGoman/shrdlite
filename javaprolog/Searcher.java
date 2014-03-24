

public class Searcher{
	String line;
	int pointer = 0;
	String deliminators;
	
	public Searcher(String s){
		line = s;
	}
	public void useDelimiter(String s){
		deliminators = s;
	}
	public boolean hasNext(){
		pointer == length(line) - 1;
	}
	public String next(){
		for(int i = 0 ; i < length(deliminators) ; i++ ){
			indexOf(, pointer)
		}
	}
}