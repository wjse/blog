package com.k66.concurrent.t01;

import java.util.concurrent.TimeUnit;

/**
 * synchronized可重入概念
 */
public class T01_Synchronized4 {

    synchronized void m() {
        System.out.println("m start");
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("m end");
    }

    static class TT extends T01_Synchronized4{
        @Override
        synchronized void m() {
            System.out.println("child m start");
            super.m();
            System.out.println("child m end");
        }
    }

    public static void main(String[] args) {
        new TT().m();
    }
}

