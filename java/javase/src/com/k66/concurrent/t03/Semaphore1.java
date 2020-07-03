package com.k66.concurrent.t03;

import java.util.concurrent.Semaphore;

/**
 * "信号灯"
 * 用于"限流"，最多允许多少个线程同时运行
 * "车道和收费站"
 */
public class Semaphore1 {

    public static void main(String[] args) {
        Semaphore s = new Semaphore(1);//允许多少线程来参考我这"信号灯" ， 支持公平锁

        new Thread(() -> {
            try {
                s.acquire(); //阻塞，取得permits，取得后当Semaphore的permits为0时，其他线程无法取得
                System.out.println("T1 running...");
                Thread.sleep(200);
                System.out.println("T1 running...");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                s.release(); //释放,Semaphore的permits从0变回1，其他线程可以取得
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("T2 running...");
                Thread.sleep(200);
                System.out.println("T2 running...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                s.release();
            }
        }).start();
    }
}
