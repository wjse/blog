package com.k66.concurrent.t03;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁
 */
public class ReentrantLock4 extends Thread{
    private static Lock lock = new ReentrantLock(true);

    @Override
    public void run() {
        for(int i = 0 ; i < 10 ; i++){
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName() + "获得了锁");
            }finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLock4 rl = new ReentrantLock4();
        Thread t1 = new Thread(rl);
        Thread t2 = new Thread(rl);

        t1.start();
        t2.start();
    }
}
