package com.example.reactive;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by debasishc on 22/6/15.
 */
public class FileReader {
    ByteBuffer buf= ByteBuffer.allocate(65536);
    FileChannel channel;
    int readCount = 0;

    public FileReader(String filename) throws FileNotFoundException {
        channel = new FileInputStream(filename).getChannel();
        buf.clear();
    }

    public int readIntFromText() throws IOException {
        int value = 0;
        while(true){
            if(readCount<=0){
                buf.clear();
                readCount = channel.read(buf);
                if(readCount<0){
                    break;
                }
                buf.flip();
            }
            byte nextChar = buf.get();
            readCount--;

            if(nextChar>='0' && nextChar<='9') {
                value = value * 10 + (nextChar - '0');
            }else{
                break;
            }

        }
        return value;
    }

    public static void main(String [] args) throws Exception{
        long startTime = System.currentTimeMillis();
        FileReader reader = new FileReader("com-orkut.ungraph.txt");
        while(true){
            int val = reader.readIntFromText();
            if(val == 0){
                break;
            }

        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time Take: "+(endTime-startTime));

    }

}
