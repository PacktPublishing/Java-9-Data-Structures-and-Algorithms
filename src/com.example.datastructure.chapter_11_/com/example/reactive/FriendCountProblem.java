package com.example.reactive;

import com.example.datastructure.Array;
import com.example.search.ArraySearcher;
import com.example.sort.ArraySorter;
import com.example.searchtree.AVLTree;
import com.example.tree.BinaryTree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by debasishc on 17/1/17.
 */
public class FriendCountProblem {
    private static final String USER_LIST_FILE = "ulist";
    private static final String EDGES_PATH = "com-orkut.ungraph.txt";
    private static final String OUTPUT_FILE_PATH = "output";

    public static void main(String [] args)
            throws Exception {
        long start = System.currentTimeMillis();
        FileReader userListReader = new FileReader(USER_LIST_FILE);

        int count = 0;

        while(true){

            int lineValue = userListReader.readIntFromText();
            if(lineValue==0){
                break;
            }
            count++;
        }

        Integer [] keys = new Integer[count];
        AtomicInteger [] values = new AtomicInteger[count];

        userListReader = new FileReader(USER_LIST_FILE);


        int index = 0;

        while(true){

            int uid = userListReader.readIntFromText();
            if(uid==0){
                break;
            }
            keys[index] = uid;
            values[index] =  new AtomicInteger(0);
            index++;

        }
        ArraySorter.quicksort(keys,(a,b)->a-b);
        ProducerConsumerQueue<Integer> queue = new ProducerConsumerQueue<>(4092, 2, (v)->{
            int pos  = ArraySearcher.binarySearch(keys,v);
            if(pos<0){
                return;
            }
            values[pos].incrementAndGet();
        });
        FileReader edgeListFileReader = new FileReader(EDGES_PATH);
        while(true){
            int val = edgeListFileReader.readIntFromText();
            if(val == 0){
                break;
            }
            queue.produce(val);
        }
        queue.markCompleted();
        queue.joinThreads();

        PrintStream out = new PrintStream(OUTPUT_FILE_PATH);
        for(int i=0;i<count;i++){
            out.println(keys[i] +" : "+values[i].get());
        }
        out.flush();
        System.out.println("Time taken: "+(System.currentTimeMillis() - start));
    }
}
