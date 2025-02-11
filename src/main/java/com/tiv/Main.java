package com.tiv;

import com.tiv.reject.DiscardRejectHandle;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 主类, 用于演示自定义线程池的功能.
 * 该类创建了一个线程池, 并提交多个任务以展示线程池的执行情况和拒绝策略的效果.
 */
public class Main {

    /**
     * 程序入口方法.
     * 创建一个自定义线程池并提交多个任务, 演示线程池的工作原理和拒绝策略的处理.
     */
    public static void main(String[] args) {
        // 创建一个自定义线程池
        ThreadPool threadPool = new ThreadPool(4, 2, 500, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2), new DiscardRejectHandle());

        // 提交10个任务到线程池
        for (int i = 0; i < 10; i++) {
            final int taskIndex = i;
            threadPool.execute(() -> {
                try {
                    // 模拟任务处理时间, 每个任务休眠1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 打印当前执行任务的线程名称和任务编号
                System.out.println(Thread.currentThread().getName() + " 执行任务 " + taskIndex);
            });
        }

        // 主线程不会被阻塞, 继续执行后续代码
        System.out.println("主线程没有被阻塞");
    }
}
