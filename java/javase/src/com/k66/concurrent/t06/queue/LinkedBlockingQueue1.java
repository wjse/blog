package com.k66.concurrent.t06.queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列,天生的生产/消费模型
 */
public class LinkedBlockingQueue1 {
    static BlockingQueue<String> strs = new LinkedBlockingQueue<>();
    static Random r = new Random();

    public static void main(String[] args) {
        new Thread(() -> {
            for(int i = 0 ; i < 100; i++){
                try {
                    strs.put("a" + i); //阻塞式 ， 区别于offer
                    TimeUnit.MILLISECONDS.sleep(r.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } , " p1").start();

        for(int i = 0 ; i< 5 ; i++){
            new Thread(() -> {
                for(;;){
                    try {
                        System.out.println(Thread.currentThread().getName() + " take -" + strs.take()); //阻塞式
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } , "c" + i).start();
        }
    }
}
