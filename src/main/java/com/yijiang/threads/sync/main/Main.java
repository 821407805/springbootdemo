package com.yijiang.threads.sync.main;

import com.yijiang.threads.sync.busi.DealBusi;
import com.yijiang.threads.sync.busi.QryBusi;
import com.yijiang.threads.sync.busi.impl.DealBusiImpl;
import com.yijiang.threads.sync.busi.impl.QryBusiImpl;
import com.yijiang.threads.sync.thread.Consumer;
import com.yijiang.threads.sync.thread.Producer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        QryBusi qryBusi = new QryBusiImpl();
        DealBusi dealBusi = new DealBusiImpl();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20));
        LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>(10);
        for (int i = 0; i < 10; i++) {
            try {
                runnables.put(new Consumer(dealBusi,runnables));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Producer producer = new Producer(qryBusi,runnables,threadPoolExecutor);
        new Thread(producer).start();
    }

}
