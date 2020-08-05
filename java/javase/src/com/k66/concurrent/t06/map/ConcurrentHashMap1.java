package com.k66.concurrent.t06.map;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CountDownLatch;

public class ConcurrentHashMap1 {

    public static void main(String[] args) {
        Map<String , String> map = new ConcurrentHashMap<>();
        // Map<String , String> map = new ConcurrentSkipListMap<>(); //高并发并且排序，为什么没有ConcurrentTreeMap?因为CAS对树加锁实现太复杂了。

        Random r = new Random();
        Thread[] ths = new Thread[100];
        CountDownLatch latch = new CountDownLatch(ths.length);
        long start = System.currentTimeMillis();
        for(int i = 0 ; i < ths.length; i++){
            ths[i] = new Thread(() -> {
                for(int j = 0 ; j < 10000; j++){
                    map.put("a" + r.nextInt(100000) , "a" + r.nextInt(100000));
                }
                latch.countDown();
            });
        }

        Arrays.asList(ths).forEach(t -> t.start());

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(System.currentTimeMillis() - start + "ms");

        //-------------------------------------------------------------

        CountDownLatch rlatch = new CountDownLatch(ths.length);
        start = System.currentTimeMillis();
        for(int i = 0 ; i < ths.length; i++){
            ths[i] = new Thread(() -> {
                for(int j = 0 ; j < 10000; j++){
                    map.get("a" + r.nextInt(100000));
                }
                rlatch.countDown();
            });
        }

        Arrays.asList(ths).forEach(t -> t.start());

        try {
            rlatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(System.currentTimeMillis() - start + "ms");
    }
}
