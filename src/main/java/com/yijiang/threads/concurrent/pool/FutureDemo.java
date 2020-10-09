package com.yijiang.threads.concurrent.pool;


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
        for (int i = 0; i < 20; i++) {
            submit = threadPoolExecutor.submit(new CallableDemo());
            System.out.println( threadPoolExecutor.getActiveCount() );
        }
        /*threadPoolExecutor.in*/
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
