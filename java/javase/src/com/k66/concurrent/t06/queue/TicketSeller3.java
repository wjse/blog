package com.k66.concurrent.t06.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 有N张火车票，每张票都有一个编号
 * 同时有10个窗口对外售票
 * 写一个模拟程序
 *
 * 分析下面的程序可能会产生哪些问题？
 * 重复销售？超量销售？
 */
public class TicketSeller3 {
    static List<String> tickets = new LinkedList<>();

    static{
        for(int i = 0; i < 1000 ; i++) tickets.add("票编号： " + i);
    }

    /**
     * 依然产生超卖问题
     * @param args
     */
    public static void main(String[] args) {
        for(int i = 0 ; i< 10 ; i++){
            new Thread(() -> {
                synchronized (tickets) {
                    while (tickets.size() > 0) {//原子操作
                        //此处并非原子操作，需要对整个代码块加锁
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("销售了--" + tickets.remove(0));//原子操作
                    }
                }
            }).start();
        }
    }
}
