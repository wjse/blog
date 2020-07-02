package com.k66.concurrent.t03;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 替代ReentrantLock1中synchronized
 */
public class ReentrantLock2 {

    /**
     * 可使用tryLock()进行尝试锁定,不管锁定与否，方法都将继续执行
     * 可根据tryLock()返回值来判定是否锁定
     * 也可以指定tryLock()时间，注意unlock
     */
    Lock lock = new ReentrantLock();

    void m1(){
        try {
            lock.lock();
            for(int i = 0 ; i < 10 ; i++){
                TimeUnit.SECONDS.sleep(1);
                System.out.println(i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    void m2(){
//        lock.lock();
        boolean locked = false;
        try {
            locked = lock.tryLock(5 , TimeUnit.SECONDS);
            System.out.println("m2 获得锁" + locked);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(locked){
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantLock2 rl = new ReentrantLock2();
        new Thread(rl :: m1).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(rl :: m2).start();
    }
}
