import org.json.simple.JSONArray;

public class Heuristic{

	public static int heuristic(JSONArray world, String holding, JSONArray goalWorld, String goalHolding) {
		
		int cost = 0;
      
      for (int j=0; j<world.size();	j++){	
      
          if (!world.get(j).equals(goalWorld.get(j))){
            JSONArray worldColTemp = (JSONArray) world.get(j);
            JSONArray goalColTemp = (JSONArray) goalWorld.get(j);
         

            for (int i=0;i<worldColTemp.size();i++){
               int tempCost = 0;            
            
               if(tempCost==0 && worldColTemp.get(i)==goalColTemp.get(i)){
                  tempCost+=0;
                  } else if (tempCost!=0 && worldColTemp.get(i).equals(goalColTemp.get(i))){
                  tempCost+=4;
                  } else if (goalColTemp.contains(worldColTemp.get(i))) {
                  tempCost+=4;                  
                  } else{
                  tempCost+=2;
                  }
               
               cost += tempCost;
                              
         
            }
         
          }
      
      }
      if(holding!=goalHolding){
         cost++;
      } 
      
		return cost;
		
	}
}
