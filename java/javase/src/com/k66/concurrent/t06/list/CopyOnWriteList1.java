package com.k66.concurrent.t06.list;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteList1 {

    public static void main(String[] args) {
        List<String> list = new CopyOnWriteArrayList<>(); //写时复制 ，写少读多的业务场景，写时加锁，读时不加锁
                            //ArrayList<>();//会产生并发问题
                            //Vector();
        Random r = new Random();
        Thread[] ths = new Thread[100];

        for(int i = 0 ; i < ths.length ; i++){
            ths[i] = new Thread(() -> {
                for(int j = 0; j < 1000; j++){
                    list.add("a" + r.nextInt(10000));
                }
            });
        }

        runAndComouteTime(ths);
    }

    static void runAndComouteTime(Thread[] ths){
        long s1 = System.currentTimeMillis();
        Arrays.asList(ths).forEach(t -> t.start());
        Arrays.asList(ths).forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(System.currentTimeMillis() - s1 + "ms");
    }
}
