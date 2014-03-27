import org.json.simple.JSONArray;

public class Heuristic{

	public static int heuristic(JSONArray world, String holding, JSONArray goalWorld, String goalHolding) {
		
		int cost = 0;
      
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
            cost += tempCost;
         
          }
      
      }

      
		return cost;
		
	}
}
