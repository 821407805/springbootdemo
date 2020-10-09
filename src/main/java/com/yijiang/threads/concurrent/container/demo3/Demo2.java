package com.yijiang.threads.concurrent.container.demo3;

import java.util.concurrent.LinkedBlockingQueue;

public class Demo2 {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<String> strings = new LinkedBlockingQueue<>();
        //往队列里存元素

        //实际上调用的是offer，区别是在队列满的时候，add会报异常
        strings.add("111");
        //对列如果满了，直接入队失败
        strings.offer("111");
        //在队列满的时候，会进入阻塞的状态
        strings.put("111");

        //从队列中取元素
        //直接调用poll，唯一的区别即使remove会抛出异常，而poll在队列为空的时候直接返回null
        String remove = strings.remove();
        strings.poll();
        //在队列为空的时候，会进入等待的状态
        strings.take();

    }
}
