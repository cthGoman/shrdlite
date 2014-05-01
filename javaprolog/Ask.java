import java.util.*;

public class Ask{
	public static String question(ArrayList<String> treeStrings){
   
      ArrayList<String> yesList = new ArrayList<String>();
		ArrayList<String> noList = new ArrayList<String>();
      
      LinkedList<String> object0 = new LinkedList<String>(); //temp
		ArrayList<LinkedList<LinkedList<String>>> objectList = new ArrayList<LinkedList<LinkedList<String>>>();
      ArrayList<LinkedList<String>> relationList = new ArrayList<LinkedList<String>>();

      for(int i = 0;i < treeStrings.size();i++){
         Tree tree = new Tree(treeStrings.get(i));
         tree.getMasterNode().createObjects(objectList.get(i),relationList.get(i));
      }
      int indexOfLongestList = 0;
      int lengthLongestList = 0;
      for(LinkedList<LinkedList<String>> list : objectList){
         list.addFirst(object0); //add the 0 object to the first element of the list
         if(list.size() >= lengthLongestList){
            lengthLongestList = list.size();
            indexOfLongestList = relationList.indexOf(list);
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
      for(int i = 0;i < lengthLongestList-1;i++){
         if(nonMatchingFound){
            break;
         }
         testRelation = relationList.get(indexOfLongestList).get(i);
         testObject1 = objectList.get(indexOfLongestList).get(i);
         testObject2 = objectList.get(indexOfLongestList).get(i+1);
         
         for(int j = 0;j < objectList.size() ;j++){
            if(objectList.get(j).size() > i){  
               compareRelation = relationList.get(j).get(i);
               compareObject1 = objectList.get(j).get(i);
               compareObject2 = objectList.get(j).get(i);
               if(!compareStatement(testRelation,testObject1,testObject2,
                                   compareRelation,compareObject1,compareObject2)){
                  nonMatchingRelation = testRelation;
                  nonMatchingObject1 =  testObject1;
                  nonMatchingObject2 =  testObject2;
                  questionIndex = i;
                  nonMatchingFound = true;
                  break;
              }
            }
         }
      }
      
      
		//sort add lists to yes and no lists
      for(int i = 0;i < objectList.size() ;i++){
         compareRelation = relationList.get(i).get(questionIndex);
         compareObject1 = objectList.get(i).get(questionIndex);
         compareObject2 = objectList.get(i).get(questionIndex);
          if(!compareStatement(nonMatchingRelation,nonMatchingObject1,nonMatchingObject2,
                                   compareRelation,compareObject1,compareObject2)){
            yesList.add(treeStrings.get(i));  
          }else{
            noList.add(treeStrings.get(i));
          }
      }
      
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
      RelativeObject = nonMatchingObject1.get(0);
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
		return "Should i put the " + movingObject + " " + nonMatchingRelation + " the " + RelativeObject;
      
	}
   private static boolean compareStatement(String relation1,LinkedList<String> object11,LinkedList<String> object12,
                                    String relation2,LinkedList<String> object21,LinkedList<String> object22){
      
      return relation1.equals(relation2) && compareObject(object11,object21) && compareObject(object12,object22);
      
   }
   private static boolean compareObject(LinkedList<String> object1,LinkedList<String> object2){
      
      boolean typeSame;
      boolean sizeSame;
      boolean colorSame;
      if((object1.get(0).equals("anyform") || object2.get(1).equals("anyform"))){  // if the size are not explained in one of the objects they could be the same
         sizeSame = true;
      } 
      else{
         sizeSame = object1.get(0).equals(object2.get(0));
      }  
      typeSame = object1.get(0).equals(object2.get(0));
      if((object1.get(1).equals("-") || object2.get(1).equals("-"))){  // if the size are not explained in one of the objects they could be the same
         sizeSame = true;
      } 
      else{
         sizeSame = object1.get(1).equals(object2.get(1));
      }
      
      if((object1.get(2).equals("-") || object2.get(2).equals("-"))){  // if the size are not explained in one of the objects they could be the same
         colorSame = true;
      } 
      else{
         colorSame = object1.get(2).equals(object2.get(2));
      }
      
      
      return typeSame && sizeSame && colorSame;
   }  
   
    
}