package com.k66.concurrent.t06.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 容量为0的queue，用于线程数据通信
 * 必须要等另一个线程就绪时才能放入数据
 * 功能同Exchanger
 * 不同于Exchanger在于不需要两个线程同时阻塞等待
 * 看似没用，其实在线程池内大量运用
 */
public class SynchronusQueue1 {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> strs = new SynchronousQueue<>();

        new Thread(() -> {
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.SECONDS.sleep(2);
        strs.put("aaa");
        System.out.println(strs.size());
    }
}
