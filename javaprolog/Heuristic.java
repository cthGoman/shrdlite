import org.json.simple.JSONArray;
import java.util.List;
import java.util.ArrayList;


public class Heuristic{
   private int cost;
   private int colCost;
   private ArrayList<Integer> tempColCostList = new ArrayList<Integer>();
   
   public Heuristic(int costIn,int colCostIn){
      cost=costIn;
      colCost=colCostIn;
   }

	public Heuristic(JSONArray world, String holding, JSONArray goalWorld, String goalHolding) {
		
		cost = 0;
      colCost = 0;
      
      //Loop over all columns                 
      for (int j=0; j<world.size();	j++){	
      
          //If the column differs check differences
          if (!world.get(j).equals(goalWorld.get(j))){
            JSONArray worldColTemp = (JSONArray) world.get(j);
            JSONArray goalColTemp = (JSONArray) goalWorld.get(j);
         
            int tempCost = 0;
            
            for (int i=0;i<worldColTemp.size();i++){
               
               
               // If the object in world is in the right column            
               if(goalColTemp.contains(worldColTemp.get(i))){
                  if(tempCost!=0){
                     tempCost+=4; //If something below the object needs to be moved
                  }
                  else if(worldColTemp.size()>i){
                     if(!worldColTemp.get(i).equals(goalColTemp.get(i))){
                        tempCost+=4; //If the object is in the right column but in the wrong place
                     }
                  }
               
               }
              else if(!goalHolding.isEmpty()){ 
                   if(goalHolding.equals(worldColTemp.get(i))){
                  tempCost+=1; //If the object should be in holding
                   }   
               }
              else{
                  tempCost+=2; //If the object is in the wrong column
              }
            
            }
            
            if(tempCost>colCost){
               colCost=tempCost; //The column with the highest cost
            }
            
            cost += tempCost; //Add the column cost to the total cost
            tempColCostList.add(tempCost);
          } else {
            tempColCostList.add(0);
          }
      
      
      
      
      
      }
      
      if(!holding.isEmpty()){
         
         for (int j=0; j<goalWorld.size(); j++){
            for (int i=0; i< ((JSONArray) goalWorld.get(j)).size(); i++){
               if (     ((JSONArray) goalWorld.get(j)).get(i).equals(holding)   ){
                  if (tempColCostList.get(j)==0 && ((JSONArray) world.get(j)).size() == i ){
                     cost += 1;
                  }else {
                     cost += 3;
                  }
               }
            }
         }
         
      }

		
	}
   
   public int getCost(){
      return cost;
   }
   
   public int getColCost(){
      return colCost;
   }
   
   public boolean isBetter(Heuristic compHeu){
   
      if ((compHeu.getCost()>cost) ||(compHeu.getCost()==cost && compHeu.getColCost()>colCost)){
         return true;
      }
      else{
         return false;
      }
   
   
   }
}
