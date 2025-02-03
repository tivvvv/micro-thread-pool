package com.tiv;

import com.tiv.reject.ThrowRejectHandle;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(4, 2, 500, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2), new ThrowRejectHandle());
        for (int i = 0; i < 6; i++) {
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(Thread.currentThread().getName());
            });
        }
        System.out.println("主线程没有被阻塞");
    }
}
