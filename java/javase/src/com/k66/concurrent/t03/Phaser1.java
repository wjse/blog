package com.k66.concurrent.t03;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * "阶段"，结合CountDownLatch与CyclicBarrier
 * 按照不同的阶段，来执行线程
 * "遗传算法"可能会用到Phaser
 */
public class Phaser1 {

    static Random r = new Random();
    static MarriagePhaser phaser = new MarriagePhaser();

    static void milliSleep(int milli){
        try {
            TimeUnit.MILLISECONDS.sleep(milli);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟婚礼
     * 1-等所有人到齐
     * 2-所有人吃饭
     * 3-新郎新娘洞房
     * 4-所有人离开
     * @param args
     */
    public static void main(String[] args) {
        phaser.bulkRegister(7);//注册7个人

        for(int i = 0 ; i < 5 ; i++){
            new Thread(new Person("P" + i)).start();
        }
        new Thread(new Person("新郎")).start();
        new Thread(new Person("新娘")).start();
    }

    /**
     * 自定义Phaser
     */
    static class MarriagePhaser extends Phaser{

        /**
         * 前进步骤
         * 对应Person run()里的4个步骤
         * @param phase 当前阶段
         * @param registeredParties 人数（线程数）
         * @return
         */
        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase){
                case 0:
                    System.out.println("所有人到齐了！" + registeredParties);
                    System.out.println();
                    return false;
                case 1:
                    System.out.println("所有人吃完了！" + registeredParties);
                    System.out.println();
                    return false;
                case 2:
                    System.out.println("所有人离开了！" + registeredParties);
                    System.out.println();
                    return false;
                case 3:
                    System.out.println("婚礼结束！新郎新娘洞房！" + registeredParties);
                    return true;
                default:
                    return true;
            }
        }
    }

    static class Person implements Runnable{
        String name;

        public Person(String name){
            this.name = name;
        }

        public void arrive(){
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 到达!\n" , name);
            phaser.arriveAndAwaitAdvance();
        }

        public void eat(){
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 吃完!\n" , name);
            phaser.arriveAndAwaitAdvance();
        }

        public void sex(){
            if("新郎".equals(name) || "新娘".equals(name)){
                milliSleep(r.nextInt(1000));
                System.out.printf("%s 洞房!\n" , name);
                phaser.arriveAndAwaitAdvance();
            }else{
                phaser.arriveAndDeregister();//注销行为，不进入下一个阶段
            }
        }

        public void leave(){
            milliSleep(r.nextInt(1000));
            System.out.printf("%s 离开!\n" , name);
            phaser.arriveAndAwaitAdvance();
        }

        @Override
        public void run() {
            arrive();
            eat();
            sex();
            leave();
        }
    }
}
