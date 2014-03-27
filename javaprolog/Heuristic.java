import org.json.simple.JSONArray;
import java.util.List;
import java.util.ArrayList;

public class Heuristic extends ArrayList<Integer>{
   private int cost;
   private int colCost;

	public Heuristic(JSONArray world, String holding, JSONArray goalWorld, String goalHolding) {
		
		cost = 0;
      colCost = 0;
      ArrayList<Integer> heuristic = new ArrayList(2);
                  
      for (int j=0; j<world.size();	j++){	
      
          if (!world.get(j).equals(goalWorld.get(j))){
            JSONArray worldColTemp = (JSONArray) world.get(j);
            JSONArray goalColTemp = (JSONArray) goalWorld.get(j);
         
            int tempCost = 0;
            
            for (int i=0;i<worldColTemp.size();i++){
                           
               if(goalColTemp.contains(worldColTemp.get(i))){
                  if(tempCost!=0){
                     tempCost+=4;
                  }
                  else if(worldColTemp.size()>=i){
                     if(worldColTemp.get(i)!=goalColTemp.get(i)){
                        tempCost+=4;
                     }
                  }
               
               }
              else if(!goalHolding.isEmpty()){ 
                   if(goalHolding.equals(worldColTemp.get(i))){
                  tempCost+=1;
                   }   
               }
              else{
                  tempCost+=2;
              }
            
            }
            
            if(tempCost>colCost){
               colCost=tempCost;
            }
            cost += tempCost;
         
          }
      
      }

		
	}
   
   public int cost(){
      return cost;
   }
   
   public int colCost(){
      return colCost;
   }
   
   public boolean isMin(Heuristic compHeu){
   
      if ((compHeu.cost()<cost) ||(compHeu.cost()==cost && compHeu.colCost()<colCost)){
         return true;
      }
      else{
         return false;
      }
   
   
   }
}
