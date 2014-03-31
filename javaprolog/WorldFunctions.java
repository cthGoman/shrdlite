import org.json.simple.JSONArray;

public class WorldFunctions{

   public static JSONArray copy(JSONArray world){
      
      JSONArray tempWorld = new JSONArray();
      
      for(int k=0;k<world.size();k++){
                 
         JSONArray goalColTemp= new JSONArray();
         goalColTemp.addAll((JSONArray) world.get(k));
      
         tempWorld.add(goalColTemp);
      
      }
   
      return tempWorld;
   }
   
   public static String getTopObjectWorldColumn(JSONArray world, int column){
     return (String) ((JSONArray) world.get(column)).get(((JSONArray) world.get(column)).size()-1);
   }
   
   public static void removeTopObjectWorldColumn(JSONArray world, int column){
     ((JSONArray) world.get(column)).remove(((JSONArray) world.get(column)).size()-1);
   }

   public static boolean WorldColumnContains(JSONArray world, String object, int column){
     return ((JSONArray) world.get(column)).contains(object);
   }
}