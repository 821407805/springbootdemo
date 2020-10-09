package com.yijiang.threads.concurrent.atomic.demo2;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * AtomicIntegerArray Demo
 */
public class AtomicIntegerArrayDemo {

    public static void main(String[] args) {
        int[] arr = new int[]{3, 2};
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(arr);
        System.out.println(atomicIntegerArray.addAndGet(1, 8)); // 10
        System.out.println(atomicIntegerArray.get(1));  // 10
        int i = atomicIntegerArray.accumulateAndGet(0, 2, (left, right) ->
                left * right
        );
        System.out.println(i); // 6
    }
}
