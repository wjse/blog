package com.k66.concurrent.t01;

import java.util.concurrent.TimeUnit;

/**
 * 同步方法和非同步方法是否可以同时调用
 * 答：必须可以
 */
public class T01_Synchronized3 {

    public synchronized void m1(){
        System.out.println(Thread.currentThread().getName() + " m1 start...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m1 end...");
    }

    public void m2(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " m2");
    }

    public static void main(String[] args) {
        T01_Synchronized3 t = new T01_Synchronized3();

        new Thread(t::m1 , "t1").start();
        new Thread(t::m2 , "t2").start();
    }

    static class Account{
        String name;
        double balance;

        public synchronized void set(String name , double balance){
            this.name = name;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.balance = balance;
        }

        public /*synchronized*/ double get(String name){
            if(name.equals(this.name)){
                return balance;
            }
            return 0D;
        }

        public static void main(String[] args) {
            Account a = new Account();
            new Thread(() -> a.set("wj" , 100D)).start();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(a.get("wj"));

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(a.get("wj"));
        }
    }
}
