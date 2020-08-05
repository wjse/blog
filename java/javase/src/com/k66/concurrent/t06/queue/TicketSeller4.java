package com.k66.concurrent.t06.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 有N张火车票，每张票都有一个编号
 * 同时有10个窗口对外售票
 * 写一个模拟程序
 *
 * ConcurrentLinkedDeque解决并发问题
 * 并发时多考虑queue少用list
 */
public class TicketSeller4 {
    static Queue<String> tickets = new ConcurrentLinkedDeque<>();

    static{
        for(int i = 0; i < 10000 ; i++) tickets.add("票编号： " + i);
    }

    public static void main(String[] args) {
        for(int i = 0 ; i< 10 ; i++){
            new Thread(() -> {
                while(true){
                    String s = tickets.poll();
                    if(null == s){
                        break;
                    }
                    System.out.println("销售了--" + s);
                }
            }).start();
        }
    }
}
