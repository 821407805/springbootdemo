package com.yijiang.threads.concurrent.atomic.demo1;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.DoubleAdder;

/**
 * AtomicInteger Demo
 */
public class Demo1 {

    private static AtomicInteger sum = new AtomicInteger(0);
    private static DoubleAdder doubleAdder = new DoubleAdder();

    public static void inCreate() {
        sum.incrementAndGet();
    }
    public static void addCreate(double d) {
        doubleAdder.add(d);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 100; j++) {
                    inCreate();
                    System.out.println("sum:"+sum);
                }
            }).start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (double j = 0; j < 10; j++) {
                    addCreate(1.0);
                    System.out.println(doubleAdder);
                }
            }).start();
        }
    }
}
