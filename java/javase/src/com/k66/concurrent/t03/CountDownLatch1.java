package com.k66.concurrent.t03;

import java.util.concurrent.CountDownLatch;

/**
 * "门栓"，作用类似于线程join，但比join灵活
 */
public class CountDownLatch1 {

    public static void main(String[] args) {
        usingJoin();
        usingCountDownLatch();
    }

    static void usingJoin(){
        Thread[] threads = new Thread[100];
        for(int i = 0 ; i < threads.length ; i++){
            threads[i] = new Thread(() -> {
                int result = 0;
                for(int j = 0 ; j < 10000 ; j++){
                    result += j;
                }
            });
        }

        for(int i = 0 ; i < threads.length ; i++){
            threads[i].start();
        }

        for(int i = 0 ; i < threads.length ; i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("end join");
    }

    static void usingCountDownLatch(){
        Thread[] threads = new Thread[100];
        CountDownLatch latch = new CountDownLatch(threads.length);

        for(int i = 0 ; i < threads.length ; i++){
            threads[i] = new Thread(() -> {
                int result = 0;
                for(int j = 0 ; j < 10000 ; j++){
                    result += j;
                }
                latch.countDown();//原来基础上-1
            });
        }

        for(int i = 0 ; i < threads.length ; i++){
            threads[i].start();
        }

        try {
            latch.await();//门栓在此处将门栓住，初始门栓有个数为threads.length,执行一个线程countDown,当门栓数为0时，开门
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end latch");
    }
}
