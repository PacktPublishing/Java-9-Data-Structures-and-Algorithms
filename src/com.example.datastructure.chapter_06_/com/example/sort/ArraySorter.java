package com.example.sort;


import com.example.functional.LinkedList;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by debasishc on 16/9/16.
 */
public class ArraySorter {
    public static <E extends Comparable<E>> int findMin(E[] array,
                                                        int start) {
        if (start == array.length - 1) {
            return start;
        }
        int restMinIndex = findMin(array, start + 1);
        E restMin = array[restMinIndex];
        if (restMin.compareTo(array[start]) < 0) {
            return restMinIndex;
        } else {
            return start;
        }
    }

    public static <E> void swap(E[] array, int i, int j) {
        if (i == j)
            return;
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

   public static <E extends Comparable<E>> void insertElementSorted(
            E[] array, int valueIndex) {

        if (valueIndex > 0
                &&
                array[valueIndex].compareTo(array[valueIndex - 1]) <
                        0) {
            swap(array, valueIndex, valueIndex - 1);
            insertElementSorted(array, valueIndex - 1);
        }

    }

    public static <E extends Comparable<E>> void insertElementSortedNonRecursive(
            E[] array, int valueIndex) {
        while (true) {
            if (valueIndex > 0
                    && array[valueIndex]
                    .compareTo(array[valueIndex - 1]) < 0) {
                swap(array, valueIndex, valueIndex - 1);
                valueIndex = valueIndex - 1;
            } else {
                return;
            }
        }

    }

    public static <E> void quicksort(E[] array, int start, int end,
                                     Comparator<E> comparator) {
        if (end - start <= 0) {
            return;
        }
        int pivotIndex =
                (int) ((end - start) * Math.random()) + start;
        swap(array, pivotIndex, end - 1);
        //let us find the pivot.
        int i = start;
        int j = end - 1;
        boolean movingI = true;
        while (i < j) {
            if (comparator.compare(array[i], array[j]) > 0) {
                swap(array, i, j);
                movingI = !movingI;
            } else {
                if (movingI) {
                    i++;
                } else {
                    j--;
                }
            }
        }
        quicksort(array, start, i, comparator);
        quicksort(array, i + 1, end, comparator);
    }

    public static <E> void quicksort(E[] array,
                                     Comparator<E> comparator) {
        quicksort(array, 0, array.length, comparator);
    }

    public static <E> OrderedPair<LinkedList<E>, LinkedList<E>> splitByPivot(
            LinkedList<E> input, E pivot, Comparator<E> comparator) {
        if (input.isEmpty()) {
            return new OrderedPair<>(input, input);
        } else {
            OrderedPair<LinkedList<E>, LinkedList<E>> subSplit =
                    splitByPivot(input.tail(), pivot, comparator);
            if (comparator.compare(pivot, input.head()) > 0) {
                return new OrderedPair<>(subSplit._1.add(input.head()),
                        subSplit._2);
            } else {
                return new OrderedPair<>(subSplit._1,
                        subSplit._2.add(input.head()));
            }
        }

    }

    public static <E> LinkedList<E> quicksort(LinkedList<E> input,
                                              Comparator<E> comparator) {
        if (input.isEmpty()) {
            return input;
        } else {
            E pivot = input.head();
            OrderedPair<LinkedList<E>, LinkedList<E>> split =
                    splitByPivot(input.tail(), pivot, comparator);
            LinkedList<E> firstSortedPart =  quicksort(split._1, comparator);
            LinkedList<E> secondSortedPart = quicksort(split._2, comparator);
            return firstSortedPart.append(secondSortedPart.add(pivot));
        }
    }

    public static <E> OrderedPair<LinkedList<E>, LinkedList<E>> splitAtIndex(LinkedList<E> input, int index){
        if(input.isEmpty()){
            return new OrderedPair<>(input, input);
        }else if(index==0){
            return new OrderedPair<>(LinkedList.emptyList(), input);
        }else{
            OrderedPair<LinkedList<E>, LinkedList<E>> subSplit =
                    splitAtIndex(input.tail(), index - 1);
            return new OrderedPair<>(subSplit._1.add(input.head()), subSplit._2);
        }
    }

    public static <E> LinkedList<E> merge(LinkedList<E> l1, LinkedList<E> l2, Comparator<E> comparator){
        if(l1.isEmpty()){
            return l2;
        }else if(l2.isEmpty()){
            return l1;
        }else{
            E h1 = l1.head();
            E h2 = l2.head();
            if(comparator.compare(h1,h2)>0){
                return merge(l1,l2.tail(), comparator).add(h2);
            }else{
                return merge(l1.tail(),l2, comparator).add(h1);
            }
        }
    }

    public static <E> LinkedList<E> mergesort(LinkedList<E> list, Comparator<E> comparator) {
        if(list.isEmpty()|| list.getLength()==1){
            return list;
        }else {
            OrderedPair<LinkedList<E>, LinkedList<E>> split =
                    splitAtIndex(list, list.getLength() / 2);
            LinkedList<E> sortedPart1 =
                    mergesort(split._1, comparator);
            LinkedList<E> sortedPart2 =
                    mergesort(split._2, comparator);
            return merge(sortedPart1, sortedPart2, comparator);
        }
    }


    private static <E> void merge(E[] arrayL, E[] arrayR,
                                  int start, int mid, int end,
                                  E[] targetArray,
                                  Comparator<E> comparator) {
        int i = start;
        int j = mid;
        int k = start;
        while (k < end) {
            if (i == mid) {
                targetArray[k] = arrayR[j];
                j++;
            } else if (j == end) {
                targetArray[k] = arrayL[i];
                i++;
            } else if (comparator.compare(arrayL[i], arrayR[j]) > 0) {
                targetArray[k] = arrayR[j];
                j++;
            } else {
                targetArray[k] = arrayL[i];
                i++;
            }
            k++;
        }

    }

    private static <E> void mergeRecursive(E[] arrayL, E[] arrayR,
                                           int startL, int endL,
                                           int startR, int endR,
                                           E[] targetArray,
                                           int targetPosition,
                                           Comparator<E> comparator) {
        if (startL == endL && startR == endR) {
            return;
        } else if (startR == endR) {
            targetArray[targetPosition] = arrayL[startL];
            mergeRecursive(arrayL, arrayR, startL + 1, endL, startR,
                    endR, targetArray, targetPosition + 1,
                    comparator);
        } else if (startL == endL) {
            targetArray[targetPosition] = arrayR[startR];
            mergeRecursive(arrayL, arrayR, startL, endL, startR + 1,
                    endR, targetArray, targetPosition + 1,
                    comparator);
        } else if (
                comparator.compare(arrayL[startL], arrayR[startR]) >
                        0) {
            targetArray[targetPosition] = arrayR[startR];
            mergeRecursive(arrayL, arrayR, startL, endL, startR + 1,
                    endR, targetArray, targetPosition + 1,
                    comparator);
        } else {
            targetArray[targetPosition] = arrayR[startL];
            mergeRecursive(arrayL, arrayR, startL + 1, endL, startR,
                    endR, targetArray, targetPosition + 1,
                    comparator);
        }

    }

    private static <E> void merge(E[] array,
                                  int start, int mid, int end,
                                  E[] targetArray,
                                  Comparator<E> comparator) {
        merge(array, array, start, mid, end,
                targetArray, comparator);

    }

    public static <E> void mergeSort(E[] sourceArray, int start,
                                     int end,
                                     E[] tempArray,
                                     Comparator<E> comparator) {
        if (start >= end - 1) {
            return;
        }
        int mid = (start + end) / 2;
        mergeSort(sourceArray, start, mid, tempArray, comparator);
        mergeSort(sourceArray, mid, end, tempArray, comparator);

        merge(sourceArray, start, mid, end, tempArray, comparator);
        System.arraycopy(tempArray, start, sourceArray, start,
                end - start);
    }


    public static <E> E[] mergeSortNoCopy(E[] sourceArray, int start,
                                          int end,
                                          E[] tempArray,
                                          Comparator<E> comparator) {
        if (start >= end - 1) {
            return sourceArray;
        }
        int mid = (start + end) / 2;
        E[] sortedPart1 =
                mergeSortNoCopy(sourceArray, start, mid, tempArray,
                        comparator);
        E[] sortedPart2 =
                mergeSortNoCopy(sourceArray, mid, end, tempArray,
                        comparator);

        if (sortedPart2 == sortedPart1) {
            if (sortedPart1 == sourceArray) {
                merge(sortedPart1, sortedPart2, start, mid, end,
                        tempArray, comparator);
                return tempArray;
            } else {
                merge(sortedPart1, sortedPart2, start, mid, end,
                        sourceArray, comparator);
                return sourceArray;
            }
        } else {
            //in this case we store the result in sortedPart2 because it has the first portion empty
            merge(sortedPart1, sortedPart2, start, mid, end,
                    sortedPart2, comparator);
            return sortedPart2;
        }
    }

    private static <E> LinkedList<E> merge(LinkedList<E> list1,
                                           int length1,
                                           LinkedList<E> list2,
                                           Comparator<E> comparator) {
        if (length1 == 0) {
            return list2;
        } else if (list2.isEmpty()) {
            return list1;
        } else {
            E l = list1.head();
            E r = list2.head();
            if (comparator.compare(l, r) > 0) {
                return merge(list1, length1, list2.tail(), comparator)
                        .add(r);
            } else {
                return merge(list1.tail(), length1 - 1, list2,
                        comparator).add(l);
            }
        }
    }



    public static void main(String[] args) {
        Integer[] array =
                new Integer[]{10, 5, 2, 3, 78, 53, 3, 1, 1, 24, 1, 35,
                        35, 2, 67, 4, 33, 30};
        System.out.println(findMin(array, 1));

        //selectionSort(array);
        //insertionSort(array);
        //bubbleSort(array);
        //insertionSortNonRecursive(array);
        //quicksort(array, (a, b) -> a - b);
        Integer[] anotherArray = new Integer[array.length];
//        mergeSort(array, 0, array.length, anotherArray,
//                (a, b) -> a - b);
//        array = mergeSortNoCopy(array, 0, array.length, anotherArray,
//                (a, b) -> a - b);

        System.out.println(Arrays.toString(array));

//        LinkedList<Integer> list =
//                LinkedList.<Integer>emptyList().add(10).add(5).add(2)
//                        .add(3).add(78).add(53).add(3).add(1).add(1)
//                        .add(24).add(1).add(35)
//                        .add(35).add(2).add(67).add(4).add(33).add(0);
////        mergeSortLinkedList(list, (a,b)->a-b).forEach(System.out::println);
//
//        OrderedPair<LinkedList<Integer>, LinkedList<Integer>> split =
//                splitAtIndex(list, 6);
//        split._1.map((a) -> "," + a).forEach(System.out::print);
//        System.out.println();
//        split._2.map((a) -> "," + a).forEach(System.out::print);
//        System.out.println();
//        quicksort(list, (a, b) -> a - b).map((a) -> "," + a)
//                .forEach(System.out::print);
//        System.out.println();
//        mergesort(list, (a, b) -> a - b).map((a) -> "," + a)
//                .forEach(System.out::print);
    }

}
