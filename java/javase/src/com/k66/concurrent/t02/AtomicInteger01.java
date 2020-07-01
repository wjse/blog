package com.k66.concurrent.t02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicInteger01 {

    AtomicInteger count = new AtomicInteger(0);

    void m(){
        for(int i = 0 ; i < 10000 ; i++){
            count.incrementAndGet(); //相当于count ++，count ++线程不安全
        }
    }

    public static void main(String[] args) {
        AtomicInteger01 t = new AtomicInteger01();

        List<Thread> list = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            list.add(new Thread(t::m , "thread-" + i));
        }

        list.forEach(thread -> thread.start());

        list.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(t.count.get());
    }
}
