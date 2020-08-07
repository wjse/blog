package com.k66.concurrent.t06.queue;

import java.util.concurrent.LinkedTransferQueue;

public class TransferQueue1 {

    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueue<String> strs = new LinkedTransferQueue<>();

        new Thread(() -> {
            try {
                System.out.println(strs.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        //区别于put()在于，put是一个线程放完数据即走了，而transfer会使线程阻塞，直到另一个线程取走才走。
        strs.transfer("aaa");
    }
}
