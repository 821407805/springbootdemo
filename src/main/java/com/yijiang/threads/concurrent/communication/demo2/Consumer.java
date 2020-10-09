package com.yijiang.threads.concurrent.communication.demo2;

public class Consumer implements Runnable{

    private Medium medium;

    public Consumer(Medium medium) {
        this.medium = medium;
    }

    @Override
    public void run() {
        while (true) {
            medium.take();
        }
    }
}
