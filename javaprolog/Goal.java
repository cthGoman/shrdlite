import java.util.List;
import java.util.ArrayList;

public class Goal extends ArrayList<ArrayList<Statement>>{  
	public Goal(){}
	public Goal(ArrayList<Statement> newRow) {
		this.add(newRow);
	}
	public boolean addStatement(int position,Statement s){
		if(position < 0){
			return false;
		}
		for(int i = this.size() ; i < position ; i++ ){
			addRow(new ArrayList<Statement>());
		}
		get(position).add(s);
		return true;
	}
	public void addRow(ArrayList<Statement> newRow){
		this.add(newRow);
	}
}