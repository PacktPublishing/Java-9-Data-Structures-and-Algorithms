package com.example.datastructure;

/**
 * Created by debasishc on 4/8/16.
 */
public class Array {
    public static void printAllElements(int[] anIntArray){
        for(int i=0;i<anIntArray.length;i++){
            System.out.print(anIntArray[i]+" ");
        }
        System.out.println();
    }

    public static void insertElementAtIndex(int[] array, int startIndex, int targetIndex){
        int value = array[startIndex];
        if(startIndex==targetIndex){
            return;
        }else if(startIndex < targetIndex){
            for(int i=startIndex+1;i<=targetIndex;i++){
                array[i-1]=array[i];
            }
            array[targetIndex]=value;
        }else{
            for(int i=startIndex-1;i>=targetIndex;i--){
                array[i+1]=array[i];
            }
            array[targetIndex]=value;
        }
    }

    public static int [] insertExtraElementAtIndex(int[] array, int index, int value){
        int [] newArray = new int[array.length+1];
        for(int i=0;i<index;i++){
            newArray[i] = array[i];
        }
        newArray[index]=value;
        for(int i=index+1;i<newArray.length;i++){
            newArray[i]=array[i-1];
        }
        return newArray;
    }

    public static int[] appendElement(int[] array, int value){
        return insertExtraElementAtIndex(array, array.length, value);
    }

    public static void insertIntoArray(int[] array, int numberOfElements, int index, int value){

    }

    public static void main(String [] args){
        int [] elements = {1,3,65,2,3,2,6,2,4,65,2,256,6,7,2,4};
        printAllElements(elements);
        insertElementAtIndex(elements,5,13);
        printAllElements(elements);
        insertElementAtIndex(elements,13,5);
        printAllElements(elements);
        elements = insertExtraElementAtIndex(elements, 5, -1);
        printAllElements(elements);
        elements = appendElement(elements, 5000);
        printAllElements(elements);
    }
}
