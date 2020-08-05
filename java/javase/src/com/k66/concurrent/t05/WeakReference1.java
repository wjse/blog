package com.k66.concurrent.t05;

import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class WeakReference1 {


    static class M{

        /**
         * gc调用
         * @throws Throwable
         */
        @Override
        protected void finalize() throws Throwable {
            System.out.println("finalize");
        }
    }

    /**
     * 强引用
     */
    static class NormalReference{

        public static void main(String[] args) throws IOException {
            M m = new M();
            m = null; //help gc
            System.gc();
            System.in.read();
        }
    }

    /**
     * 软引用
     */
    static class SoftReference1 {

        public static void main(String[] args) {
            //先设置运行参数
            SoftReference<byte[]> m = new SoftReference(new byte[1024 * 1024 * 10]);
            System.out.println(m.get());
            System.gc();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(m.get());

            //再分配一个数组，heap将装不下，这时候系统会垃圾回收，先回收一次，如果不够，会把软引用干掉
            byte[] b = new byte[1024 * 1024 * 10];
            System.out.println(m.get());
        }
    }

    /**
     * 弱引用
     */
    static class WeakReference2{
        public static void main(String[] args) {
            WeakReference<M> m = new WeakReference<>(new M());
            System.out.println(m.get());
            System.gc();
            System.out.println(m.get());
        }
    }

    /**
     * 虚引用
     */
    static class PhantomReference1{

        static ReferenceQueue<M> Q = new ReferenceQueue<>();
        static List<Object> L = new ArrayList<>();

        public static void main(String[] args) {
            PhantomReference<M> m = new PhantomReference<>(new M() , Q);

            new Thread(() -> {
                while (true){
                    L.add(new byte[1024 * 1024]);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(m.get());
                }
            }).start();

            new Thread(() -> {
                while(true){
                    Reference<? extends M> poll = Q.poll();
                    if(null != poll){
                        System.out.println("虚引用对象被jvm回收了");
                    }
                }
            }).start();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
