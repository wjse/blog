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

    static class Test{
        static Exchanger<String> q = new Exchanger<>();
        static String[] strs = new String[]{"A","B","C","D","E","F","G"};
        static String[] nums = new String[]{"1","2","3","4","5","6","7"};

        public static void main(String[] args) {

            new Thread(() -> {
                for(int i = 0 ; i < nums.length ; i++){
                    try {
                        String s = nums[i];
                        s = q.exchange(s);
                        System.out.println(Thread.currentThread().getName() + " " + s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } , "t1").start();

            new Thread(() -> {
                for(int i = 0 ; i < strs.length ; i++){
                    try {
                        String s = strs[i];
                        s = q.exchange(s);
                        System.out.println(Thread.currentThread().getName() + " " + s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } , "t2").start();
        }
    }
}
