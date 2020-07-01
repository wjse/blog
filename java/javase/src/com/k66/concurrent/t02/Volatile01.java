package com.k66.concurrent.t02;

import java.util.concurrent.TimeUnit;

public class Volatile01 {

    volatile boolean running = true;

    void m(){
        System.out.println("m start");
        while (running){

        }
        System.out.println("m end");
    }

    public static void main(String[] args) {
        Volatile01 v = new Volatile01();

        new Thread(v::m , "t1").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        v.running = false;
    }
}
