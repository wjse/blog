package com.k66.concurrent.t07;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 面试题，两个线程交替输出A1 , B2 , C3...
 */
public class T_Test {
    static final String[] STRS = new String[]{"A","B","C","D","E","F","G"};
    static final String[] NUMS = new String[]{"1","2","3","4","5","6","7"};

    /**
     * 使用synchronized
     */
    static class UsingSynchronized{

        private static Object lock = new Object();
        // private static volatile boolean t2Started = false;//保证第二线程先运行
        private static CountDownLatch latch = new CountDownLatch(1);

        public static void main(String[] args) {
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock){
                    // while(!t2Started){
                    //     try {
                    //         lock.wait();
                    //     } catch (InterruptedException e) {
                    //         e.printStackTrace();
                    //     }
                    // }
                    for(String s : STRS){
                        System.out.println(s);
                        try {
                            lock.notify(); //叫醒其他线程
                            lock.wait(); //线程阻塞
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    lock.notify(); //必须，否则无法停止程序，不管任一线程，最后都在wait，所以需要唤醒
                }
            }).start();

            new Thread(() -> {
                synchronized (lock){
                    for(String s : NUMS){
                        System.out.print(s);
                        // t2Started = true;
                        latch.countDown();
                        try {
                            lock.notify();
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    lock.notify();
                }
            }).start();
        }
    }

    /**
     * 使用LockSupport
     */
    static class UsingLockSupport{
        static Thread t1 , t2;
        public static void main(String[] args) {
            t1 = new Thread(() -> {
                for(String s : STRS){
                    System.out.println(s);
                    LockSupport.unpark(t2); //叫醒t2
                    LockSupport.park(); //t1阻塞
                }
            });

            t2 = new Thread(() -> {
                for(String s : NUMS){
                    LockSupport.park(); //t2首先阻塞，等待前一线程叫醒
                    System.out.println(s);
                    LockSupport.unpark(t1); //叫醒t1
                }
            });

            t1.start();
            t2.start();
        }
    }

    /**
     * 使用自旋锁
     */
    static class UsingReentrantLock{
        static Lock lock = new ReentrantLock();

        //指定线程，Condition相当于等待队列
        static Condition conditionT1 = lock.newCondition();
        static Condition conditionT2 = lock.newCondition();

        public static void main(String[] args) {
            new Thread(() -> {
                try {
                    lock.lock();
                    for (String s : STRS) {
                        System.out.print(s);
                        conditionT2.signal(); //唤醒T2线程
                        conditionT1.await(); //T1线程阻塞
                    }
                    conditionT1.signal();
                    conditionT2.signal();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }).start();

            new Thread(() -> {
                try {
                    lock.lock();
                    for (String s : NUMS) {
                        System.out.println(s);
                        conditionT1.signal(); //唤醒T1线程
                        conditionT2.await(); //T2线程阻塞
                    }
                    conditionT1.signal();
                    conditionT2.signal();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }).start();
        }
    }

    /**
     * 使用自定义自旋
     */
    static class UsingCAS{

        enum ReadyToRun{T1 , T2}

        static volatile ReadyToRun r = ReadyToRun.T1; //volatile保证线程可见性

        public static void main(String[] args) {
            new Thread(() -> {
                for(String s : STRS){
                    while(r != ReadyToRun.T1){} //自旋，此刻占用CPU，wait()、await()不占用CPU
                    System.out.print(s);
                    r = ReadyToRun.T2;
                }
            }).start();

            new Thread(() -> {
                for(String s : NUMS){
                    while(r != ReadyToRun.T2){} //自旋
                    System.out.println(s);
                    r = ReadyToRun.T1;
                }
            }).start();
        }
    }

    /**
     * 使用阻塞队列
     */
    static class UsingBlockingQueue{
        static BlockingQueue<String> q1 = new ArrayBlockingQueue<>(1);
        static BlockingQueue<String> q2 = new ArrayBlockingQueue<>(1);

        public static void main(String[] args) {
            new Thread(() -> {
                for(String s : STRS){
                    System.out.print(s);
                    try {
                        q1.put("ok");
                        q2.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                for(String s : NUMS){
                    try {
                        q1.take();
                        System.out.println(s);
                        q2.put("ok");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 使用传输队列，让对方打印
     */
    static class UsingTransferQueue{
        static LinkedTransferQueue<String> q = new LinkedTransferQueue<>();

        public static void main(String[] args) {
            new Thread(() -> {
                for(String s : NUMS){
                    try {
                        System.out.print(q.take()); //先做消费者
                        q.transfer(s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            new Thread(() -> {
                for(String s : STRS){
                    try {
                        q.transfer(s); //生产者
                        System.out.println(q.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
