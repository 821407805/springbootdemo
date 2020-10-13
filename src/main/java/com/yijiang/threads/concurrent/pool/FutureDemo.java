package com.yijiang.threads.concurrent.pool;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName: FutureDemo
 * @Description:
 * @Author Jason
 * @Date 2020/10/4 11:12
 */
public class FutureDemo {

    public static void main(String[] args) {
        Future<String> submit = null;

        LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 2, TimeUnit.SECONDS, linkedBlockingQueue);
        threadPoolExecutor.prestartAllCoreThreads();
        ArrayList<Callable<String>> callables = new ArrayList<>();
        callables.add(new CallableDemo());
        callables.add(new CallableDemo2());
        try {
            List<Future<String>> futures = threadPoolExecutor.invokeAll(callables, 50, TimeUnit.SECONDS);
            ArrayList<String> results = new ArrayList<>();
            try {
                for (Future<String> f: futures) {
                    if(f.isCancelled()){
                        //线程中断，返回结果会丢失该批次的数据
                        System.out.println("findConcurrentFilterResult,线程中断，返回结果会丢失该批次的过滤结果");
                        continue;
                    }
                    String result = f.get();
                    if(result==null){
                        //线程中断，返回结果会丢失该批次的数据
                        System.out.println("findConcurrentFilterResult线程中断，返回结果为空，会丢失该批次的过滤结果=-=");
                        continue;
                    }
                    results.add(result);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < 20; i++) {
            submit = threadPoolExecutor.submit(new CallableDemo());
            System.out.println( threadPoolExecutor.getActiveCount() );
        }
        for (int i = 0; i < 20; i++) {
            try {
                System.out.println(submit.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
