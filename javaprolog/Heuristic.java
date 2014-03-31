import org.json.simple.JSONArray;
import java.util.List;
import java.util.ArrayList;


public class Heuristic{
   private int cost;
   private int colCost;
   private ArrayList<Integer> tempColCostList = new ArrayList<Integer>();
   private int misplaced;
   
   public Heuristic(int costIn,int colCostIn){
      cost=costIn;
      colCost=colCostIn;
   }

	public Heuristic(JSONArray world, String holding, JSONArray goalWorld, String goalHolding) {
		
		cost = 0;
      colCost = 0;
      misplaced = 0;
      
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
                     misplaced++;
                  }
                  else if(goalColTemp.size()>i){
                     if(!worldColTemp.get(i).equals(goalColTemp.get(i))){
                        tempCost+=4; //If the object is in the right column but in the wrong place
                        misplaced++;
                     }
                  }
               
               }
              else if(!goalHolding.isEmpty()){ 
                   if(goalHolding.equals(worldColTemp.get(i))){
                  tempCost+=1; //If the object should be in holding
                  misplaced++;
                   }   
               }
              else{
                  tempCost+=2; //If the object is in the wrong column
                  misplaced++;
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
      
      //Cost for the object in holding
      if(!holding.isEmpty()){
         
         for (int j=0; j<goalWorld.size(); j++){ //loop over columns in goal world
         
            for (int i=0; i< ((JSONArray) goalWorld.get(j)).size(); i++){ //loop over objects in the column
            
               if (     ((JSONArray) goalWorld.get(j)).get(i).equals(holding)   ){ //Check if the object in goal world is the same as in holding
               
                  if (tempColCostList.get(j)==0 && ((JSONArray) world.get(j)).size() == i ){
                     cost += 1; //add cost for object can directly be dropped in the right position
                     misplaced++;
                  }else {
                     cost += 3; //add cost if the object has to be dropped in the wrong position
                     misplaced++;
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
   
   public int getMisplaced(){
      return misplaced;
   }
   
   public boolean isBetter(Heuristic compHeu){
   
      if ((compHeu.getCost()>cost) ||
      (compHeu.getCost()==cost && compHeu.getMisplaced()>misplaced) ||
      (compHeu.getCost()==cost && compHeu.getMisplaced()==misplaced && compHeu.getColCost()>colCost)){
         return true;
      }
      else if ((compHeu.getCost()==cost && compHeu.getMisplaced()==misplaced && compHeu.getColCost()==colCost) && Math.random()>0.5){
         return true;
      }
      else{
         return false;
      }
   
   
   }
}
