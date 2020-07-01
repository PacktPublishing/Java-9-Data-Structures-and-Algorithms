package com.example.datastructure;

/**
 * Created by debasishc on 29/7/16.
 */
public class SimplePowerRemainderComputation {
    public static long computeRemainder(long base, long power, long divisor){
        long baseRaisedToPower = 1;
        for(long i=1;i<=power;i++){
            baseRaisedToPower *= base;
        }
        return baseRaisedToPower % divisor;
    }

    public static long computeRemainderCorrected(long base, long power, long divisor){
        long baseRaisedToPower = 1;
        for(long i=1;i<=power;i++){
            baseRaisedToPower *= base;
            baseRaisedToPower %= divisor;
        }
        return baseRaisedToPower;
    }

    public static long computeRemainderUsingEBS(long base, long power, long divisor){
        long baseRaisedToPower = 1;
        long powerBitsReversed = 0;
        int numBits=0;
        while(power>0){
            powerBitsReversed <<= 1;
            powerBitsReversed += power & 1;
            power >>>= 1;
            numBits++;
        }
        while (numBits-->0){
            if(powerBitsReversed%2==1){
                baseRaisedToPower *= baseRaisedToPower * base;
            }else{
                baseRaisedToPower *= baseRaisedToPower;
            }
            baseRaisedToPower %= divisor;
            powerBitsReversed>>>=1;
        }
        return baseRaisedToPower;
    }
    public static void main(String [] args){
        System.out.println(computeRemainderUsingEBS(13, 10_000_000, 7));

        long startTime = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            computeRemainderCorrected(13, 10_000_000, 7);
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        startTime = System.currentTimeMillis();
        for(int i=0;i<1000;i++){
            computeRemainderUsingEBS(13, 10_000_000, 7);
        }
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }
}
