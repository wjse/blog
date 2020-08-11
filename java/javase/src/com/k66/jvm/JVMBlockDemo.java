package com.k66.jvm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JVMBlockDemo {
    private static List<int[]> bigObj = new ArrayList<>();
    private static List<char[]> bigCharObj = new ArrayList<>();

    public static int[] gen1M(){
        return new int[1024 * 256];
    }

    public static char[] gen1MChar(){
        return new char[1024 * 256];
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("start = [" + new Date() + "]");
        Thread.sleep(10000);
        System.out.println("start = [" + new Date() + "]");

        int i = 0;
        while (i < 1000){
            System.out.println("start = [" + new Date() + "]" + i);
            if(i == 0){
                Thread.sleep(3001);
                System.out.println("start = [" + new Date() + "]");
            }else{
                Thread.sleep(20001);
            }
            bigObj.add(gen1M());
            i++;
        }
    }
}
