package com.k66.concurrent.t05;

import java.util.concurrent.TimeUnit;

public class ThreadLocal1 {
    // volatile static Person p = new Person();
    static ThreadLocal<Person> tl = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.println(p.name);
            System.out.println(tl.get());
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // p.name = "李四";
            tl.set(new Person());
        }).start();
    }

    static class Person{
        String name = "张三";
    }
}
