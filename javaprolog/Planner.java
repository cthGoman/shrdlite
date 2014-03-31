
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.util.List;
import java.util.ArrayList;

public class Planner{
   
   private JSONArray world;
   private String holding;
   private JSONObject objects;

	public Planner(JSONArray worldIn, String holdingIn, JSONObject objectsIn){
		world=worldIn;
      holding=holdingIn;
      objects=objectsIn;
	}
	public Plan solve(Goal g,JSONArray goalWorld, String goalHolding){
      Plan plan=new Plan();
      // plan.add("test");
      
      String actHolding = holding;
      
      int horizon = 1;
      
      JSONArray actWorld = new JSONArray();
            
      for(int i=0;i<world.size();i++){
              
         JSONArray goalColTemp= new JSONArray();
         goalColTemp.addAll((JSONArray) world.get(i));
 
         actWorld.add(goalColTemp);
 
      }
      
      
      while (!actWorld.equals(goalWorld)){
         
         for (int i=0;i<horizon;i++){
            
            int bestPickColumn = 0;
            Heuristic bestPick = new Heuristic(100,100);
            
            for (int j=0;j<world.size();j++){
               //Loop over columns
               
               
               //Check which object to pick
               String tempHolding = actHolding;
               JSONArray tempWorld = new JSONArray();
               for(int k=0;k<actWorld.size();k++){
                 
                  JSONArray goalColTemp= new JSONArray();
                  goalColTemp.addAll((JSONArray) actWorld.get(k));
    
                  tempWorld.add(goalColTemp);
    
               }
               
            
               
               
               if (((JSONArray) tempWorld.get(j)).size()>0) {
                  //If current column has an object
                  
                  tempHolding= (String) ((JSONArray) tempWorld.get(j)).get(((JSONArray) tempWorld.get(j)).size()-1);
                  
                  ((JSONArray) tempWorld.get(j)).remove(((JSONArray) tempWorld.get(j)).size()-1);
                  
//                   System.out.println("goalWorld " + goalWorld);
//                   System.out.println("goalHolding " + goalHolding);
//                   
//                   System.out.println("tempWorld " + tempWorld);
//                   System.out.println("tempHolding " + tempHolding);
                  
                  Heuristic currPick = new Heuristic(tempWorld,tempHolding,goalWorld,goalHolding);
//                   
//                   System.out.println("currPick cost " + currPick.getCost());
                  
                  
                  if (currPick.isBetter(bestPick)){
                     bestPickColumn=j;
                     bestPick=currPick;
                  }
                     
//                    System.out.println("currPickCost " + currPick.getCost());
//                   System.out.println("tempHolding "+tempHolding);
//                   System.out.println("tempWorld "+tempWorld);
//                   System.out.println("current column " +j);
               }
               
             }
//              System.out.println("bestPickColumn: " + bestPickColumn);
//              System.out.println("");
//              System.out.println(world);
//              System.out.println(goalWorld);
//              System.out.println(actWorld);
//              System.out.println(actHolding);
             
             actHolding= (String) ((JSONArray) actWorld.get(bestPickColumn)).get(((JSONArray) actWorld.get(bestPickColumn)).size()-1);
             ((JSONArray) actWorld.get(bestPickColumn)).remove(((JSONArray) actWorld.get(bestPickColumn)).size()-1);
             
             plan.add("pick " + bestPickColumn);
             
            
            
            if (actWorld.equals(goalWorld) && actHolding.equals(goalHolding)){
               break;
             }
             
             
            int bestDropColumn = 0;
            Heuristic bestDrop = new Heuristic(100,100);
            
            for (int j=0;j<world.size();j++){
               //Loop over columns
               
               
               //Check which column to drop
               String tempHolding = actHolding;
               JSONArray tempWorld = new JSONArray();
               for(int k=0;k<world.size();k++){
                 
                  JSONArray goalColTemp= new JSONArray();
                  goalColTemp.addAll((JSONArray) actWorld.get(k));
    
                  tempWorld.add(goalColTemp);
    
               }
               
            
               
               
               if (!tempHolding.isEmpty()) {
                  //If an object is hold
                  
                  
                  
                  ((JSONArray) tempWorld.get(j)).add(tempHolding);
                  tempHolding= "";
                  
                  Heuristic currDrop = new Heuristic(tempWorld,tempHolding,goalWorld,goalHolding);

                  
                  if (currDrop.isBetter(bestDrop) && j!=bestPickColumn && Constraints.isWorldAllowed(tempWorld,tempHolding,objects)){
                     bestDropColumn=j;
                     bestDrop=currDrop;
                  }
                     
//                   System.out.println("currDropCost " + currDrop.getCost());
//                   System.out.println("tempHolding "+tempHolding);
//                   System.out.println("tempWorld "+tempWorld);
//                   System.out.println("current column " +j);
               }
               
             }
             
//              System.out.println("bestDropColumn " + bestDropColumn);
//              System.out.println("");
//              System.out.println(world);
//              System.out.println(goalWorld);
             
             
             
             ((JSONArray) actWorld.get(bestDropColumn)).add(actHolding);
             actHolding= "";
//              System.out.println(actWorld);
             
             plan.add("drop " + bestDropColumn);
             
//              System.out.println(plan);
             if (actWorld.equals(goalWorld)){
               break;
             }
             
             
            
            
         }
      
      
      }
      
      
      
      
      

      
//       for (int i=0; i<world.size(); i++)
//       {
//    
//          JSONArray columnTemp = (JSONArray) world.get(i);
//          
//          if (columnTemp.contains(g.get(0).get(0).get(1)))
//          {
//             plan.add("pick " + i);
//          }
// 
//       }
//       
//       for (int i=0; i<world.size(); i++)
//       {
//    
//          JSONArray columnTemp = (JSONArray) world.get(i);
//          
//          if (columnTemp.isEmpty())
//          {
//             plan.add("drop " + i);
//             break;
//          }
// 
//       }

   
		return plan;
	}
}