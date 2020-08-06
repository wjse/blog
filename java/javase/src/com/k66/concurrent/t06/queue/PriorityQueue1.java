package com.k66.concurrent.t06.queue;

import java.util.PriorityQueue;

/**
 * 优先级队列
 * 二叉树
 */
public class PriorityQueue1 {

    public static void main(String[] args) {
        //实现为排好顺序的树，基于obj的compare进行排序
        PriorityQueue<String> q = new PriorityQueue<>();
        q.add("c");
        q.add("e");
        q.add("a");
        q.add("d");
        q.add("z");

        System.out.println(q);

        int count = q.size();
        for(int i = 0 ; i < count ; i++){
            System.out.println(q.poll());
        }
    }
}
