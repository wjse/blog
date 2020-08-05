package com.k66.concurrent.t06.list;

import com.k66.concurrent.t06.Constans;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TestLinkedList {
    static List<UUID> m = new LinkedList<>();
    static int count = Constans.COUNT;
    static UUID[] values = new UUID[count];
    static final int THREAD_COUNT = Constans.THREAD_COUNT;

    static{
        for(int i = 0 ; i < count ; i++){
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
                m.add(values[i]);
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
                    m.get(10);
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
