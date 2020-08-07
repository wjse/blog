package com.k66.concurrent.t03;

import java.util.concurrent.Exchanger;

/**
 * 两个线程数据交换
 * 应用场景，比如游戏中进行交易
 */
public class Exchanger1 {

    static Exchanger<String> exchanger = new Exchanger<>();

    public static void main(String[] args) {
        new Thread(() -> {
            String s = "T1";
            try {
                s = exchanger.exchange(s);// 阻塞的
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + s);
        } , "t1").start();

        new Thread(() -> {
            String s = "T2";
            try {
                s = exchanger.exchange(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " " + s);
        } , "t2").start();
    }
}
