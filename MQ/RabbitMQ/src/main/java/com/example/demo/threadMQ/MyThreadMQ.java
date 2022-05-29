package com.example.demo.threadMQ;


import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author PC
 */
public class MyThreadMQ {

    public static void main(String[] args) {
        LinkedBlockingDeque<JSONObject> blockingDeque = new LinkedBlockingDeque<>();


        Thread product = new Thread(new Runnable() {


            @lombok.SneakyThrows
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "沙河");
                jsonObject.put("age", "23");
                blockingDeque.offer(jsonObject);
            }
        }, "生产者");

        product.start();


        Thread consumer = new Thread(new Runnable() {
            @lombok.SneakyThrows
            @Override
            public void run() {

                while (true) {
                    JSONObject jsonObject = blockingDeque.poll();
                    if (jsonObject != null) {

                        System.out.println(Thread.currentThread().getName() + "获得的数据:" + jsonObject.toString());
                    }
                }

            }
        }, "消费者");

        consumer.start();


        System.out.println("======================");

        LinkedBlockingQueue<JSONObject> queue = new LinkedBlockingQueue<>();

    }
}
