import java.util.*;
import org.json.simple.JSONArray;

public class State{
   //private ArrayList world;
   private String holding;
   private ArrayList<ArrayList<String>> world;
   private int robotPos;
   private int hashCode;
   
   public State(JSONArray worldIn, String holdingIn){
      holding = holdingIn;
      robotPos = -1;
      world = new ArrayList<ArrayList<String>>(worldIn.size());
      for(int k=0;k<worldIn.size();k++){
         world.add(new ArrayList((JSONArray)worldIn.get(k)));
      }

   }
   
   public State(JSONArray worldIn, String holdingIn, int robotPosIn){
      holding = holdingIn;
      robotPos = robotPosIn;
      world = new ArrayList<ArrayList<String>>(worldIn.size());
      for(int k=0;k<worldIn.size();k++){
         world.add(new ArrayList((JSONArray)worldIn.get(k)));
      }
      
   }
   
   public State(State stateIn){
      holding = stateIn.getHolding();
      robotPos = stateIn.getRobotPos();
      world = new ArrayList<ArrayList<String>>(stateIn.getWorld().size());
      for(ArrayList column : stateIn.getWorld()){
         world.add(new ArrayList(column));
      }
   }
   
   public ArrayList<ArrayList<String>> getWorld(){
      return world;
   }
   
   public String getHolding(){
      return holding;
   }
   
   public int getRobotPos(){
      return robotPos;
   }
   
   public void pickColumn(int columnNr){
      if(holding.isEmpty() && world.get(columnNr).size()>0){
         holding = world.get(columnNr).get(world.get(columnNr).size()-1); //put the top object in column columnNr in holding
         world.get(columnNr).remove(world.get(columnNr).get(world.get(columnNr).size()-1)); //remove the top object in column columnNr
         robotPos = columnNr;
         }
   }
   
   public void dropColumn(int columnNr){
      if(!holding.isEmpty()){
         world.get(columnNr).add(holding);
         holding = ""; 
         robotPos = columnNr;
         }
   }
   
   public String pickDropColumn(int columnNr){
      if(holding.isEmpty() && world.get(columnNr).size()>0){
         holding = world.get(columnNr).get(world.get(columnNr).size()-1); //put the top object in column columnNr in holding
         world.get(columnNr).remove(world.get(columnNr).get(world.get(columnNr).size()-1)); //remove the top object in column columnNr
         robotPos = columnNr;
         return ("pick "+columnNr);
         }
      else if(!holding.isEmpty()){
         world.get(columnNr).add(holding);
         holding = "";
         robotPos = columnNr;
         return ("drop "+columnNr);
         }
      return "";
   }
   
   public final boolean equals(Object stateIn){
      
      if(world.equals(((State)stateIn).getWorld()) && holding.equals(((State)stateIn).getHolding())){
         return true;
      }
      return false;
   }
   
   public final boolean equals(State stateIn){
      
      if(world.equals(((State)stateIn).getWorld()) && holding.equals(((State)stateIn).getHolding())){
         return true;
      }
      return false;
   }
   
   public ArrayList<String> getAllObjectsInWorld(){
      ArrayList<String> objects = new ArrayList<String>();
      
      for(ArrayList column : world)
         objects.addAll(column);
      
      if(!holding.isEmpty())
         objects.add(holding);
      
      return objects;
   }
   
   public ArrayList<String> getColumn(int columnNr){
      return world.get(columnNr);
   }
   
   public int getColumnNumber(String obj){
      for(int k = 0; k<world.size(); k++){
         if(world.get(k).contains(obj)){
            return k;
         }
         else if(obj.contains(String.valueOf(k))){
            return k;
         }
      }
      return -1;
   }
   
   public int getPlaceInColumn(String obj){
      for(int k = 0; k<world.size(); k++){
         if(world.get(k).contains(obj)){
            return world.get(k).indexOf(obj);
         }
         else if(obj.contains(String.valueOf(k))){
            return -1;
         }
      }
      return -1;
   }
   
   public String getObjectBelow(String obj){
      for(ArrayList<String> column : world){
         if(column.contains(obj)){
            if(column.indexOf(obj)==0){
               return "floor-"+world.indexOf(column);
            }
            else{
               return column.get(column.indexOf(obj)-1);
            }
         }
      }
      return "";
   }
   
   public String getTopObjectWorldColumn(int columnNr){
     return world.get(columnNr).get(world.get(columnNr).size()-1);
   }
   
   public void removeTopObjectWorldColumn(int columnNr){
     world.get(columnNr).remove(this.getTopObjectWorldColumn(columnNr));
   }
   
   public void addObjectWorldColumn( String object, int columnNr){
     world.get(columnNr).add(object);
   }
   
   public boolean worldColumnContains(String object, int columnNr){
     return world.get(columnNr).contains(object);
   }
   
   public boolean hasHolding(){
      return !holding.isEmpty();
   }
   
   public String toString(){
      return world.toString()+","+holding;
   }
   
  @Override public int hashCode(){
      ArrayList<String> objects = getAllObjectsInWorld();
      Collections.sort(objects);
      int hashCode = 0;
      for(int i=0; i<world.size(); i++){
         for(int j=0; j<world.get(i).size(); j++){
            hashCode += i * j * Math.pow(31,objects.indexOf(world.get(i).get(j)));  
         
         }
            
      }
      return hashCode;
   }
   
   
//    public static State copy(State origState){
//    
//       JSONArray tempWorld = new ArrayList(world);
//       String tempHolding = holding;
//       State tempState = State(
//       return tempWorld;
//    }
}