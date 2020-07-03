package com.k66.concurrent.t03;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * "栅栏"
 * 装满指定线程数，再调用统一的指定动作
 *
 * CyclicBarrier(int parties) //无指定后续动作
 * CyclicBarrier(int parties, Runnable barrierAction)
 *
 * 应用场景，比如需要1-访问数据库，需要2-访问网络，需要3-访问文件，如果顺序同步执行，效率可能会很低
 * 分成3个线程执行不同的操作，等3个线程全部完成，再继续下一步操作就可以使用CyclicBarrier
 */
public class CyclicBarrier1 {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(20 , () -> System.out.println("满载发车"));
//        CyclicBarrier barrier = new CyclicBarrier(20);

        for(int i = 0 ; i < 100 ; i++){
            int finalI = i;
            new Thread(() -> {
                try {
                    System.out.println(finalI);
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
