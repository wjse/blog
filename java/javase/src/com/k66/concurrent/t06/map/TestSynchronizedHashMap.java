package com.k66.concurrent.t06.map;

import com.k66.concurrent.t06.Constans;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class TestSynchronizedHashMap {

    static Map<UUID , UUID> m = Collections.synchronizedMap(new Hashtable<>());
    static int count = Constans.COUNT;
    static UUID[] keys = new UUID[count];
    static UUID[] values = new UUID[count];
    static final int THREAD_COUNT = Constans.THREAD_COUNT;

    static{
        for(int i = 0 ; i < count ; i++){
            keys[i] = UUID.randomUUID();
            values[i] = UUID.randomUUID();
        }
    }

    static class MyThread extends Thread{
        int start;
        int gap = count / THREAD_COUNT;

        public MyThread(int start){
            this.start = start;
        }

        @Override
        public void run() {
            for(int i = start ; i < start + gap ; i++){
                m.put(keys[i] , values[i]);
            }
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Thread[] threads = new Thread[THREAD_COUNT];
        for(int i = 0 ; i < threads.length; i++){
            threads[i] = new MyThread(i * (count / THREAD_COUNT));
        }

        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(m.size());

        //------------------------------------------------------------
        start = System.currentTimeMillis();
        for(int i = 0 ; i < threads.length; i++){
            threads[i] = new Thread(() -> {
                for(int j = 0 ; j < 10000000; j++){
                    m.get(keys[10]);
                }
            });
        }

        for(Thread t : threads){
            t.start();
        }

        for(Thread t : threads){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(System.currentTimeMillis() - start + "ms");
    }
}
