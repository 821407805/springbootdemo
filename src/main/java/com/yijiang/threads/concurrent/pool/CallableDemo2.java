package com.yijiang.threads.concurrent.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableDemo2 implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println(Thread.currentThread().getName());
        Thread.sleep(1000L);
        return "2222";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CallableDemo2 callableDemo = new CallableDemo2();
        FutureTask<String> stringFutureTask = new FutureTask<>(callableDemo);
        new Thread(stringFutureTask).start();
        System.out.println(stringFutureTask.get());
    }
}
