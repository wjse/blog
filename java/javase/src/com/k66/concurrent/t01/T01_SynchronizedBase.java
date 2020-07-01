package com.k66.concurrent.t01;

import java.util.concurrent.TimeUnit;

/**
 * synchronized基础
 */
public class T01_SynchronizedBase {

    private int count = 10;
    private Object o = new Object();

    public void m(){
        //对象头（64位）里面拿出两位来来记录是否被锁定（mark word）
        synchronized (o){ //任何线程想要执行下面的代码，必须要先拿到o这把锁
            count--;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }

    //等价于synchronized(this)
    public synchronized void m1(){
        count++;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }

    public synchronized static void m3(){
        //这里等同于synchronized(T02_Synchronized.class)
        //锁的是class对象，class对象在同一ClassLoader为单例
    }

    public static void main(String[] args) {
        T01_SynchronizedBase t = new T01_SynchronizedBase();
        Thread t1 = new Thread(() -> {
            for(int i = 0 ; i< 5; i++){
                t.m();
                try {
                    if(i % 2 == 0){
                        TimeUnit.SECONDS.sleep(2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for(int i = 0 ; i< 5; i++){
                t.m();
                if(i % 2 == 0){
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t1.start();
        t2.start();
    }
}
