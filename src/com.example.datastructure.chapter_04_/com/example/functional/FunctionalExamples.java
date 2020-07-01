package com.example.functional;

import com.example.adt.Stack;
import com.example.adt.StackImplLinkedList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;

/**
 * Created by debasishc on 27/8/16.
 */
public class FunctionalExamples {
    public static void main(String[] args)
            throws InterruptedException {
        FunctionalExamples fe = new FunctionalExamples();
        System.out.println(fe.sum_upto(10));
        System.out.println(fe.sum_upto_functional(10));
        System.out.println(fe.choose(10, 6));

        SampleFunctionalInterface sfi = (x) -> x + 1;
        int y = sfi.modify(1);

        Thread t = new Thread(() -> {
            for (int i = 0; i < 500; i++) System.out.println(i);
        });
        // t.start();
        // t.join();
        LinkedList<Integer> linkedList =
                LinkedList.<Integer>emptyList().add(5).add(3).add(0);
//        while(!(linkedList instanceof LinkedList.EmptyList)){
//            System.out.println(linkedList.head());
//            linkedList = linkedList.tail();
////        }
//        linkedList.forEach((x) -> {System.out.println(x);});
//        linkedList.forEach(System.out::println);
//        LinkedList<Integer> tranformedList = linkedList.map((x)->x*2);
//        tranformedList.forEach(System.out::println);
//        LinkedList<String> tranformedListString = linkedList.map((x)->"x*2 = "+(x*2));
//        tranformedListString.forEach(System.out::println);
//        int sum = linkedList.foldLeft(0,(a,b)->a+b);
//        System.out.println(sum);
//        int sum2 = linkedList.foldRight((a,b)->a+b, 0);
//        System.out.println(sum2);
//        LinkedList<Integer> reversedList = linkedList.foldLeft(LinkedList.emptyList(),(l,b)->l.add(b) );
//        reversedList.forEach(System.out::println);
//        LinkedList<Integer> sameList = linkedList.foldRight((b,l)->l.add(b), LinkedList.emptyList());
//        sameList.forEach(System.out::println);

//        LinkedList<Integer> rangeList = LinkedList.ofRange(1,100);
//        rangeList.forEach(System.out::println);
//        LinkedList<Integer> evenList = LinkedList.ofRange(1,100).filter((a)->a%2==0);
//        evenList.forEach(System.out::println);

//        int sumOfRange = LinkedList.ofRange(1,101).foldLeft(0, (a,b)->a+b);
//        System.out.println(sumOfRange);

//        System.out.println(factorial(100));
//        System.out.println(repeatString2("Hello",5));
//
        LinkedList<Integer> linkedList2 =
                LinkedList.<Integer>emptyList().add(6).add(8).add(9);
//        linkedList.append(linkedList2).forEach(System.out::println);

        LinkedList.ofRange(1, 10)
                .flatMap((x) -> LinkedList.ofRange(0, x))
                .forEach(System.out::println);

        Person person = new Person(new Address(
                new City(new Country("UK", "United Kingdom"),
                        "London"), "X"), "Harry Potter");
//        if(person!=null
//                && person.getAddress()!=null
//                && person.getAddress().getCity()!=null
//                && person.getAddress().getCity().getCountry()!=null){
//            System.out.println(person.getAddress().getCity().getCountry().getName());
//        }


//        Option.optionOf(person)
//                .flatMap(Person::getAddress)
//                .flatMap(Address::getCity)
//                .flatMap(City::getCountry)
//                .flatMap(Country::getName)
//                .forEach(System.out::println);
        Try.of(() -> new FileInputStream("demo"))
                .map((in) -> new InputStreamReader(in))
                .map((in) -> new BufferedReader(in))
                .map((in) -> in.readLine())
                .processException(System.err::println)
                .forEach(System.out::println);
        System.out.println(factorialRecursive(50));
        System.out.println(fibonocci(20));
        System.out.println(fibonocciNonRecursive(5));

    }

    public static BigInteger factorial(int x) {
        return LinkedList.ofRange(1, x + 1)
                .map((a) -> BigInteger.valueOf(a))
                .foldLeft(BigInteger.valueOf(1),
                        (a, b) -> a.multiply(b));
    }

    public static String repeatString(final String seed, int count) {
        return LinkedList.ofRange(1, count + 1)
                .map((a) -> seed)
                .foldLeft("", (a, b) -> a + b);
    }

    public static String repeatString2(final String seed, int count) {
        return LinkedList.ofRange(1, count + 1)
                .foldLeft("", (a, b) -> a + seed);
    }

    public static BigInteger factorialRecursive(int x) {
        if (x == 0) {
            return BigInteger.ONE;
        } else {
            return factorialRecursive(x - 1)
                    .multiply(BigInteger.valueOf(x));
        }
    }

    public static BigInteger factorialRecursiveNonRecursive(int x) {
        BigInteger prod = BigInteger.ONE;
        for (int i = 1; i <= x; i++) {
            prod = prod.multiply(BigInteger.valueOf(x));
        }
        return prod;
    }

    public static long fibonocci(int index) {
        switch (index) {
            case 0:
            case 1:
                return 1;
            default:
                return fibonocci(index - 1) + (fibonocci(index - 2));
        }
    }

    public static long fibonocciNonRecursive(int index) {
        class StackFrame {
            int index;
            int state;
            long f1;
            long f2;

            public StackFrame(int index, int state, long f1,
                              long f2) {
                this.index = index;
                this.state = state;
                this.f1 = f1;
                this.f2 = f2;
            }
        }

        Stack<StackFrame> stack =
                new StackImplLinkedList<>();
        stack.push(new StackFrame(index, 0, -1, -1));
        long value = 0;
        stackLoop:
        while (stack.peek() != null) {
            StackFrame frame = stack.pop();
            int state = frame.state;
            long f1 = frame.f1;
            long f2 = frame.f2;
            start:
            while (true) {

                switch (state) {
                    case 0:
                        switch (index) {
                            case 0:
                            case 1:
                                value = 1;
                                continue stackLoop;
                            default:
                                stack.push(
                                        new StackFrame(index, 1, f1,
                                                f2));
                                index = index - 1;
                                state = 0;
                                continue start;
                        }
                    case 1:
                        f1 = value;
                        stack.push(new StackFrame(index, 2, f1, f2));
                        index = index - 2;
                        state = 0;
                        continue start;
                    case 2:
                        f2 = value;
                        value = f1 + f2;
                        continue stackLoop;
                }
            }

        }
        return value;
    }

    public int sum_upto(int n) {
        int sum = 0;
        for (int i = 0; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    public int sum_upto_functional(int n) {
        return n == 0 ? 0 : n + sum_upto_functional(n - 1);
    }

    public long choose(long n, long r) {
        if (n < r) {
            return 0;
        } else if (r == 0) {
            return 1;
        } else if (n == r) {
            return 1;
        } else {
            return choose(n - 1, r) + choose(n - 1, r - 1);
        }
    }

}
