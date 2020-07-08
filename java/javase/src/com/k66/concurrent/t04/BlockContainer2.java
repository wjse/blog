package com.k66.concurrent.t04;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockContainer2<T> {
    final LinkedList<T> list = new LinkedList<>();
    static final int MAX = 10;
    private int count = 0;

    private Lock lock = new ReentrantLock();
    private Condition producer = lock.newCondition();
    private Condition consumer = lock.newCondition();

    public void put(T t){
        try {
            lock.lock();
            while (list.size() == MAX){
                producer.await();//消费者队列进行等待
            }
            list.add(t);
            ++count;
            consumer.signalAll();//通知消费者线程进行消费
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public T get(){
        T t = null;
        try {
            lock.lock();
            while (list.size() == 0) {
                consumer.await();
            }
            t = list.removeFirst();
            count--;
            producer.signalAll();//通知生产者线程进行消费
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return t;
    }

    public static void main(String[] args) {
        BlockContainer2<String> c = new BlockContainer2<>();
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
