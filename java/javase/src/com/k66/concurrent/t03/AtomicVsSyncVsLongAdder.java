package com.k66.concurrent.t03;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class AtomicVsSyncVsLongAdder {
    static long count2 = 0L;
    static AtomicLong count1 = new AtomicLong(0L);
    static LongAdder count3 = new LongAdder();

    public static void main(String[] args) throws InterruptedException {
        final int tNum = 10000;
        final int count = 100000;

        Thread[] threads = new Thread[tNum];

        for(int i = 0 ; i < threads.length; i++){
            threads[i] = new Thread(() -> {
                for(int k = 0 ; k < count ; k++){
                    count1.incrementAndGet();
                }
            });
        }

        long start = System.currentTimeMillis();

        for(Thread t : threads){ t.start();}

        for(Thread t : threads){ t.join();}

        long end = System.currentTimeMillis();

        System.out.println("Atomic : " + count1.get() + " time : " + (end - start));

        final Object lock = new Object();

        for(int i = 0 ; i < threads.length ; i++){
            threads[i] = new Thread(() ->{
                for(int k = 0 ; k < count ; k++){
                    synchronized (lock){
                        count2++;
                    }
                }
            });
        }

        start = System.currentTimeMillis();

        for(Thread t : threads){ t.start();}

        for(Thread t : threads){ t.join();}

        end = System.currentTimeMillis();

        System.out.println("Sync : " + count2 + " time : " + (end - start));

        for(int i = 0 ; i < threads.length ; i++){
            threads[i] = new Thread(() ->{
                for(int k = 0 ; k < count ; k++){
                    count3.increment();
                }
            });
        }

        start = System.currentTimeMillis();

        for(Thread t : threads){ t.start();}

        for(Thread t : threads){ t.join();}

        end = System.currentTimeMillis();

        System.out.println("LongAddr : " + count3.longValue() + " time : " + (end - start));
    }
}
