package com.k66.concurrent.t02;

public class DoubleCheckLock {

    private static final DoubleCheckLock INSTANCE = new DoubleCheckLock();
    private static DoubleCheckLock INSTANCE1;

    private DoubleCheckLock(){}

    /**
     * 饿汉式
     * @return
     */
    public static DoubleCheckLock getInstance(){
        return INSTANCE;
    }

    /**
     * 懒汉式
     * @return
     */
    public static DoubleCheckLock getInstance1(){
        if(null == INSTANCE1){
            INSTANCE1 = new DoubleCheckLock();
        }
        return INSTANCE1;
    }

    /**
     * 单锁
     * @return
     */
    public static synchronized DoubleCheckLock getInstance2(){
        if(null == INSTANCE1){
            INSTANCE1 = new DoubleCheckLock();
        }
        return INSTANCE1;
    }

    /**
     * 双重检查需要加volatile，否则会出现指令重排序
     */
    private volatile static DoubleCheckLock INSTANCE2;

    /**
     * 双重检查，绝对线程安全
     * @return
     */
    public static DoubleCheckLock getInstance3(){
        if(null == INSTANCE2){
            synchronized (DoubleCheckLock.class){
                if(null == INSTANCE2){
                    INSTANCE2 = new DoubleCheckLock();
                }
            }
        }
        return INSTANCE2;
    }
}
