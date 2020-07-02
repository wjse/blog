package com.k66.concurrent.t03;

import java.util.concurrent.TimeUnit;

/**
 * 锁必须可重入
 * 特别是synchronized
 * 不然子类无法调用父类
 */
public class ReentrantLock1 {

    synchronized void m1(){
        for(int i = 0 ; i< 10 ; i++){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
            if(i == 2){//提现synchronized可重入锁
                m2();
            }
        }
    }

    synchronized void m2(){
        System.out.println("m2 ....");
    }

    /**
     * m2在m1未执行完成时无法得到锁，所以无法执行
     * @param args
     */
    public static void main(String[] args) {
        ReentrantLock1 rl = new ReentrantLock1();
        new Thread(rl :: m1).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(rl :: m2).start();
    }
}
