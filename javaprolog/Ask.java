import java.util.*;

public class Ask{
	public static String question(ArrayList<String> treeStrings){
DebugFile.start(); //debuggfile
      ArrayList<String> yesList = new ArrayList<String>();
		ArrayList<String> noList = new ArrayList<String>();
      
DebugFile.println("treeStrings" + treeStrings);     
		ArrayList<LinkedList<LinkedList<String>>> objectList = new ArrayList<LinkedList<LinkedList<String>>>();
      ArrayList<LinkedList<String>> relationList = new ArrayList<LinkedList<String>>();
      Tree tree = new Tree(treeStrings.get(0));
      for(int i = 0;i < treeStrings.size();i++){
         tree = new Tree(treeStrings.get(i));
         LinkedList<LinkedList<String>> emptyObject = new LinkedList<LinkedList<String>>();
         LinkedList<String> emptyRelationList = new LinkedList<String>();
         
         tree.getMasterNode().createObjects(emptyObject,emptyRelationList);
         objectList.add(emptyObject);
         relationList.add(emptyRelationList);
      }
      
      LinkedList<String> object0 = new LinkedList<String>(tree.getMasterNode().getChild(0).createObject());
DebugFile.println("object0: " + object0);       
      int indexOfLongestList = 0;
      int lengthLongestList = 0;
      for(LinkedList<LinkedList<String>> list : objectList){
         list.addFirst(object0); //add the 0 object to the first element of the list
         if(list.size() >= lengthLongestList){
            lengthLongestList = list.size();
            indexOfLongestList = objectList.indexOf(list);
         }
      }
      boolean questionFound = false;
      
      String nonMatchingRelation = "ser du det här är något fel";
      LinkedList<String> nonMatchingObject1 = new LinkedList<String>();
      LinkedList<String> nonMatchingObject2 = new LinkedList<String>();
      
      String testRelation;
      LinkedList<String> testObject1 = new LinkedList<String>();
      LinkedList<String> testObject2 = new LinkedList<String>();
      
      String compareRelation;
      LinkedList<String> compareObject1 = new LinkedList<String>();
      LinkedList<String> compareObject2 = new LinkedList<String>();
      
      int questionIndex = -1;
      boolean nonMatchingFound = false;
      //find a non matching statement
DebugFile.println("indexOfLongestList" + indexOfLongestList);
DebugFile.println("lengthLongestList" + lengthLongestList);
      for(int i = 0;i < lengthLongestList-1;i++){
         if(nonMatchingFound){
            break;
         }
         testRelation = relationList.get(indexOfLongestList).get(i);
         testObject1 = objectList.get(indexOfLongestList).get(i);
         testObject2 = objectList.get(indexOfLongestList).get(i+1);
         
         for(int j = 0;j < objectList.size() ;j++){
            if(objectList.get(j).size()-1 > i){  
               compareRelation = relationList.get(j).get(i);
               compareObject1 = objectList.get(j).get(i);
               compareObject2 = objectList.get(j).get(i+1);
              
               if(!compareStatement(testRelation,testObject1,testObject2,
                                   compareRelation,compareObject1,compareObject2)){
                  nonMatchingRelation = testRelation;
                  nonMatchingObject1 =  testObject1;
                  nonMatchingObject2 =  testObject2;
                  questionIndex = i;
                  nonMatchingFound = true;
                  break;
              }
            }else{
               nonMatchingRelation = testRelation;
               nonMatchingObject1 =  testObject1;
               nonMatchingObject2 =  testObject2;
               questionIndex = i;
               nonMatchingFound = true;
               break;
            }
         }
      }
DebugFile.println("nonMatchingRelation: " + nonMatchingRelation);      
DebugFile.println("nonMatchingObject1: " + nonMatchingObject1);
DebugFile.println("nonMatchingObject2: " + nonMatchingObject2);
DebugFile.println("");
DebugFile.println("questionIndex: " + questionIndex);
		//sort add lists to yes and no lists
      for(int i = 0;i < objectList.size() ;i++){
         if(objectList.get(i).size() - 1 > questionIndex){
         compareRelation = relationList.get(i).get(questionIndex);
         compareObject1 = objectList.get(i).get(questionIndex);
         compareObject2 = objectList.get(i).get(questionIndex+1);
         DebugFile.println("");
         DebugFile.println("compareRelation: " + compareRelation); 
         DebugFile.println("compareObject1: " + compareObject1); 
         DebugFile.println("compareObject2: " + compareObject2); 
          if(compareStatement(nonMatchingRelation,nonMatchingObject1,nonMatchingObject2,
                                   compareRelation,compareObject1,compareObject2)){
            yesList.add(treeStrings.get(i));  
          }else{
            noList.add(treeStrings.get(i));
          }
          }else{noList.add(treeStrings.get(i));}
      }
DebugFile.println("yesList: " + yesList);
DebugFile.println("noList: " + noList);      
		QuestionFile.saveInfo(yesList,noList);
      String movingObject;
      String RelativeObject;
      if(nonMatchingObject1.get(0).equals("anyform")){
         movingObject = "object";
      }else{
      movingObject = nonMatchingObject1.get(0);
      }
      if(nonMatchingObject1.get(0).equals("anyform")){
         RelativeObject = "object";
      }else{
      RelativeObject = nonMatchingObject2.get(0);
      }
      if(!nonMatchingObject1.get(2).equals("-")){
         movingObject = nonMatchingObject1.get(2) + " " + movingObject;
      }
      if(!nonMatchingObject1.get(1).equals("-")){
         movingObject = nonMatchingObject1.get(1) + " " + movingObject;
      }
      if(!nonMatchingObject2.get(2).equals("-")){
         RelativeObject = nonMatchingObject2.get(2) + " " + RelativeObject;
      }
      if(!nonMatchingObject2.get(1).equals("-")){
         RelativeObject = nonMatchingObject2.get(1) + " " + RelativeObject;
      }
DebugFile.println("question: " + "Should I put the " + movingObject + " " + nonMatchingRelation + " the " + RelativeObject +"?");           
DebugFile.stop(); //debuggfile
		return "Should i put the " + movingObject + " " + nonMatchingRelation + " the " + RelativeObject + "?";
      
	}
   private static boolean compareStatement(String relation1,LinkedList<String> object11,LinkedList<String> object12,
                                    String relation2,LinkedList<String> object21,LinkedList<String> object22){
      
      return relation1.equals(relation2) && compareObject(object11,object21) && compareObject(object12,object22);
      
   }
   private static boolean compareObject(LinkedList<String> object1,LinkedList<String> object2){
      boolean typeSame;
      boolean sizeSame;
      boolean colorSame;   
      
         typeSame = object1.get(0).equals(object2.get(0));
         sizeSame = object1.get(1).equals(object2.get(1));
         colorSame = object1.get(2).equals(object2.get(2));
      
      return typeSame && sizeSame && colorSame;
   }  
   
    
}