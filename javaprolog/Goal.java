import java.util.List;
import java.util.ArrayList;

public class Goal extends ArrayList<ArrayList<Statement>>{
	// This class is not protected from being externally edited.
	// To impose such a protection one must override the great diversity
	// of add, set and remove functions in ArrayList.
	public Goal(){}
	public Goal(ArrayList<Statement> newCondition) {
		add(newCondition);
	}
	public boolean addStatement(int position,Statement s){
		if(position < 0){
			return false;
		}
		for(int i = this.size() ; i <= position ; i++ ){
			add(new ArrayList<Statement>());
		}
		get(position).add(s);
		return true;
	}
	public boolean cloneCondition(int position){
		if(position < 0 || position >= this.size()){
			return false;
		}
		ArrayList<Statement> toBeCloned = get(position);
		ArrayList<Statement> clone = new ArrayList<Statement>();
		for(int i = 0 ; i < toBeCloned.size() ; i++){
			clone.add((Statement)(toBeCloned.get(i).clone()));
		}
		add(position,clone);
		return true;
	}
	
	public void addCondition(ArrayList<Statement> newCondition){
		add(newCondition);
	}
}