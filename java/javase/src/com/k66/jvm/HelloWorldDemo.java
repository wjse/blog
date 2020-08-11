package com.k66.jvm;

import java.io.File;

public class HelloWorldDemo {

    private final int i = 0;
    private static int k = 0;
    private final static int s = 0;

    private Object obj = new Object();
    private int sss = 0;

    public void methodOne(int i){
        int j = 0;
        int sum = i + j;
        Object acb = obj;
        long start = System.currentTimeMillis();
        methodTwo();
    }

    public void methodTwo(){
        File file = new File("");
    }

    public static void methodThree(){

    }

    public void m(){
        // Object obj1 = new 8M();
        // Object obj2 = new 1M();
        // Object obj3 = new 8M();
        //obj1 obj2 obj3 都会被调用
    }

    public static void main(String[] args) {
        HelloWorldDemo demo = new HelloWorldDemo();
        demo.methodOne(1);
        demo.methodOne(1);
        demo.methodOne(1);
        demo.methodOne(1);
    }
}
