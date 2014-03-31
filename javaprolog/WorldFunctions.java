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
   
   public static void addObjectWorldColumn( String object,JSONArray world, int column){
     ((JSONArray) world.get(column)).add(object);
   }

   public static boolean worldColumnContains(JSONArray world, String object, int column){
     return ((JSONArray) world.get(column)).contains(object);
   }
   
   public static int getColumnNumber(JSONArray world, String object){
     for(int k=0;k<world.size();k++){
      if (((JSONArray) world.get(k)).contains(object)){
         return k;
      }
     }
     return -1;
   }
   
   public static int getPlaceInColumn(JSONArray world, String object){
     for(int k=0;k<world.size();k++){
      if (((JSONArray) world.get(k)).contains(object)){
         return ((JSONArray) world.get(k)).indexOf(object);
      }
     }
     return -1;
   }
   
   public static String getObjectBelow(JSONArray world, String object){
     for(int k=0;k<world.size();k++){
      if (((JSONArray) world.get(k)).contains(object)){
         
         int place=((JSONArray) world.get(k)).indexOf(object);
         
         if (place==0){
            return "floor-"+k;
         }else{
            return (String) ((JSONArray) world.get(k)).get(place-1);
         }
         
      }
     }
     return "";
   }
}