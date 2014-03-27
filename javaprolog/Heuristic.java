import org.json.simple.JSONArray;

public class Heuristic{
   private int cost;
   private int colCost;


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
                  else if(worldColTemp.size()>=i){
                     if(worldColTemp.get(i)!=goalColTemp.get(i)){
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
         
          }
      
      if(!holding.isEmpty()){
         
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
