package com.k66.concurrent.t04;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞式容器
 * 观察者模式
 */
public class BlockContainer1<T> {
    final private LinkedList<T> list = new LinkedList<>();
    static final private int MAX = 10;
    private int count = 0;

    public synchronized void put(T t){
        while (list.size() == MAX){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list.add(t);
        ++count;
        this.notifyAll();//通知消费者进程进行消费
    }

    public synchronized T get(){
        T t;
        while (list.size() == 0){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        t = list.removeFirst();
        count--;
        this.notifyAll();//通知生产者开始生产
        return t;
    }

    public static void main(String[] args) {
        BlockContainer1<String> c = new BlockContainer1<>();
        for(int i = 0 ; i < 10 ; i++){
            new Thread(() -> {
                for(int j = 0 ; j < 5 ; j++){
                    System.out.println(Thread.currentThread().getName() + " " + c.get());
                }
            } , "c" + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0 ; i < 2 ; i++){
            new Thread(() -> {
                for(int j = 0 ; j < 25 ; j++){
                    c.put(Thread.currentThread().getName() + " " + j);
                }
            } , "p" + i).start();
        }
    }
}
