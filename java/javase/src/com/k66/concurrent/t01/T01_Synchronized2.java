package com.k66.concurrent.t01;

public class T01_Synchronized2 implements Runnable{

    private /*volatile*/ int count = 100;

    @Override
    public synchronized void run() {//synchronized后就没有必要加volatile，synchronized既保证了原子性又保证了可见性
        count--;
        System.out.println(Thread.currentThread().getName() + " count = " + count);
    }

    public static void main(String[] args) {
        T01_Synchronized2 s = new T01_Synchronized2();
        for(int i = 0 ; i < 100 ; i++){
            new Thread(s , "THREAD" + i).start();
        }
    }
}
