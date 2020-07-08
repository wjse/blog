package com.k66.concurrent.t04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 阻塞线程，传统做法需要加锁，wait 或者 await
 * LockSupport可以直接阻塞某个线程
 */
public class LockSupport1 {

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            for(int i = 0 ; i < 10 ; i++){
                System.out.println(i);
                if(i == 5){
                    LockSupport.park();//当前线程停止
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        t.start();
        LockSupport.unpark(t);//t线程还没到5时，unpark执行，先于park执行，notify不能比wait前执行

//        try {
//            TimeUnit.SECONDS.sleep(8);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("after 8 senconds!");
//
//        LockSupport.unpark(t);
    }

    static class WithoutVolatile{
        List<Integer> list = new ArrayList<>();

        void add(int i){
            list.add(i);
        }

        int size(){
            return list.size();
        }

        public static void main(String[] args) {
            WithoutVolatile w = new WithoutVolatile();
            Thread t1 = new Thread(() -> {
                for(int i = 0 ; i < 10 ; i++){
                    int value = i + 1;
                    w.add(value);
                    System.out.println("add " + value);

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } , "t1");

            t1.start();

            Thread t2 = new Thread(() -> {
                while (true){
                    if(w.size() == 5){
                        break;
                    }
                }
                System.out.println("t2结束");
            } , "t2");

            t2.start();
        }
    }

    static class WithVolatile{
        volatile List<Integer> list = new ArrayList<>();

        void add(int i){
            list.add(i);
        }

        int size(){
            return list.size();
        }

        public static void main(String[] args) {
            WithVolatile w = new WithVolatile();
            Thread t1 = new Thread(() -> {
                for(int i = 0 ; i < 10 ; i++){
                    int value = i + 1;
                    w.add(value);
                    System.out.println("add " + value);

                    //去掉线程休眠，volatile一样不起作用,变量涉及引用，引用变更volatile是发现不了的
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            } , "t1");

            t1.start();

            Thread t2 = new Thread(() -> {
                while (true){
                    if(w.size() == 5){
                        break;
                    }
                }
                System.out.println("t2结束");
            } , "t2");

            t2.start();
        }
    }

    static class WithSynchronized{
        List<Integer> list = new ArrayList<>();

        synchronized void add(int i){
            list.add(i);
        }

        synchronized int size(){
            return list.size();
        }

        public static void main(String[] args) {
            WithSynchronized w = new WithSynchronized();
            Thread t1 = new Thread(() -> {
                for(int i = 0 ; i < 10 ; i++){
                    int value = i + 1;
                    w.add(value);
                    System.out.println("add " + value);

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } , "t1");

            t1.start();

            Thread t2 = new Thread(() -> {
                while (true){
                    if(w.size() == 5){
                        break;
                    }
                }
                System.out.println("t2结束");
            } , "t2");

            t2.start();
        }
    }

    static class WithSynchronizedList{
        List<Integer> list = Collections.synchronizedList(new LinkedList<>());

        void add(int i){
            list.add(i);
        }

        int size(){
            return list.size();
        }

        public static void main(String[] args) {
            WithSynchronizedList w = new WithSynchronizedList();
            Thread t1 = new Thread(() -> {
                for(int i = 0 ; i < 10 ; i++){
                    int value = i + 1;
                    w.add(value);
                    System.out.println("add " + value);

//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            } , "t1");

            t1.start();

            Thread t2 = new Thread(() -> {
                while (true){
                    if(w.size() == 5){
                        break;
                    }
                }
                System.out.println("t2结束");
            } , "t2");

            t2.start();
        }
    }

    static class WithLock{
        List<Integer> list = new ArrayList<>();

        void add(int i){
            list.add(i);
        }

        int size(){
            return list.size();
        }

        public static void main(String[] args) {
            WithLock w = new WithLock();
            Object lock = new Object();

            new Thread(() -> {
                synchronized (lock) {
                    System.out.println("t2启动");
                    if (w.size() != 5) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("t2 结束");
                    lock.notify();
                }
            }).start();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(() -> {
                System.out.println("t1 启动");
                synchronized (lock){
                    for(int i = 0 ; i < 10 ; i++){
                        w.add(i);
                        System.out.println("add" + i);

                        if(w.size() == 5){
                            lock.notify();//不释放锁
                            try {
                                lock.wait();//让出锁资源，其他线程才能执行
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    static class WithCountLatch{
        List<Integer> list = new ArrayList<>();

        void add(int i){
            list.add(i);
        }

        int size(){
            return list.size();
        }

        public static void main(String[] args) {
            WithCountLatch w = new WithCountLatch();
            CountDownLatch latch = new CountDownLatch(1);

            new Thread(() -> {
                    System.out.println("t2启动");
                    if (w.size() != 5) {
                        try {
                            latch.await();
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                    System.out.println("t2 结束");
            }).start();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(() -> {
                System.out.println("t1 启动");
                    for(int i = 0 ; i < 10 ; i++){
                        w.add(i);
                        System.out.println("add" + i);

                        if(w.size() == 5){
                            latch.countDown();
                        }

//                        try {
//                            TimeUnit.SECONDS.sleep(1);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                    }
            }).start();
        }
    }
}
