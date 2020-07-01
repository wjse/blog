package com.k66.concurrent.t01;

import java.util.concurrent.TimeUnit;

/**
 * 什么是线程:
 * 进程 -> 程序
 * 线程 -> 进程中最小运行单元
 * 协程/纤程 ->
 *
 * 创建线程：
 * 继承Thread类重写run
 * 实现Runnable接口实现run
 *
 * 启动线程：
 * 1.Thread start
 * 2.Executors.newCachedThread
 *
 * 线程方法:
 * sleep -> 当前线程睡眠，让出cpu资源,sleep完回到就绪状态
 * yield -> 暂时退出，让出一下cpu，其他线程能否抢到，不管
 * join  -> 当前线程加入另一线程，当另一线程运行完成时才继续运行当前线程
 *
 * 线程状态:
 * getState()
 * new
 * runnable：ready ， running ，TimedWaiting(sleep(),wait(),LockSupport.parkNanos(),LockSupport.parkUntil()), Waiting(wait(),join(),LockSupport.park()) , Blocked(未获得锁时)
 * terminated
 *
 * 关闭线程：
 * 正常让线程结束，不要stop()线程,interrupt()打断线程来处理逻辑也是不合理的
 */
public class T01_WhatIsThread {

    void testJoin(){
        Thread t1 = new Thread(() -> {
            for(int i = 0 ; i < 10 ; i++){
                System.out.println("A" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0 ; i < 10 ; i++){
                System.out.println("B" + i);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
    }

    static class T1 extends Thread{
        @Override
        public void run() {
            for(int i = 0 ; i < 10 ; i++){
                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("T1");
            }
        }
    }

    static class MyThread extends Thread{
        @Override
        public void run() {
            System.out.println("Hello MyThread");
        }
    }

    static class MyRun implements Runnable{

        @Override
        public void run() {
            System.out.println("Hello MyRun");
        }
    }

    public static void main(String[] args) {
        new T1().start();
        for(int i = 0 ; i < 10 ; i++){
            try {
                TimeUnit.MICROSECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("main");
        }

        new MyThread().start();
        new Thread(new MyRun()).start();
        new Thread(() -> System.out.println("lambda run")).start();

        new T01_WhatIsThread().testJoin();
    }
}
