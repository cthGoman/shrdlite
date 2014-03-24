

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
		return pointer < line.length();
	}
	public String next(){
		int j = line.length();
		for(int i = 0 ; i < deliminators.length() ; i++ ){
			int k = line.indexOf(deliminators.charAt(i), pointer);
			if(k >= 0){
				j = Math.min(j, k);
			}
		}
		String s;
		if(j > pointer){
			s = line.substring(pointer,j);
			pointer = j;
		}else if(j == pointer){
			s = "" + line.charAt(pointer);
			pointer++;
		}else{
			s = line.substring(pointer);
			pointer = line.length();
		}
		return s;
	}
}