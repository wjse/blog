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

    static class Test{
        static LinkedTransferQueue<String> q = new LinkedTransferQueue<>();
        static String[] strs = new String[]{"A","B","C","D","E","F","G"};
        static String[] nums = new String[]{"1","2","3","4","5","6","7"};

        public static void main(String[] args) {
            new Thread(() -> {
                for(int i = 0 ; i < nums.length ; i++){
                    try {
                        String s = q.take();
                        System.out.println(Thread.currentThread().getName() + " " + s);
                        s = nums[i];
                        q.transfer(s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } , "t1").start();

            new Thread(() -> {
                for(int i = 0 ; i < strs.length ; i++){
                    try {
                        if(i == 0){
                            q.transfer(strs[i]);
                        }
                        String s = q.take();
                        System.out.println(Thread.currentThread().getName() + " " + s);
                        s = strs[i];
                        q.transfer(s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } , "t2").start();
        }
    }
}
