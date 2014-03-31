import org.json.simple.JSONArray;

public class WorldCopy{
   public static JSONArray copy(JSONArray world){
      
      JSONArray tempWorld = new JSONArray();
      
      for(int k=0;k<world.size();k++){
                 
         JSONArray goalColTemp= new JSONArray();
         goalColTemp.addAll((JSONArray) world.get(k));
    
         tempWorld.add(goalColTemp);
    
      }

   return tempWorld;
   }

}