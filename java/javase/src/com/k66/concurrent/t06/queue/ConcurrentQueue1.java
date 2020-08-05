package com.k66.concurrent.t06.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentQueue1 {

    public static void main(String[] args) {
        Queue<String> strs = new ConcurrentLinkedQueue<>();
        for(int i = 0 ; i < 10 ; i++){
            strs.offer("a" + i);
        }

        System.out.println(strs);
        System.out.println(strs.size());

        System.out.println(strs.poll());
        System.out.println(strs.size());

        System.out.println(strs.peek());
        System.out.println(strs.size());
    }
}
