package com.tiv;

import com.tiv.reject.RejectHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池实现.
 */
public class ThreadPool {

    private final int maxSize; // 最大线程数
    private final int corePoolSize; // 核心线程数
    private final int timeout; // 空闲线程存活时间
    private final TimeUnit timeUnit; // 时间单位
    private final BlockingQueue<Runnable> blockingQueue; // 阻塞队列
    private final RejectHandle rejectHandle; // 拒绝策略

    /**
     * 构造函数, 初始化线程池参数.
     *
     * @param maxSize       最大线程数
     * @param corePoolSize  核心线程数
     * @param timeout       空闲线程存活时间
     * @param timeUnit      时间单位
     * @param blockingQueue 阻塞队列
     * @param rejectHandle  拒绝策略
     */
    public ThreadPool(int maxSize, int corePoolSize, int timeout, TimeUnit timeUnit, BlockingQueue<Runnable> blockingQueue, RejectHandle rejectHandle) {
        this.maxSize = maxSize;
        this.corePoolSize = corePoolSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.blockingQueue = blockingQueue;
        this.rejectHandle = rejectHandle;
    }

    List<Thread> coreList = new ArrayList<>(); // 核心线程列表
    List<Thread> supportList = new ArrayList<>(); // 支持线程列表

    /**
     * 提交任务到线程池.
     *
     * @param command 待执行的任务
     */
    public void execute(Runnable command) {
        // 如果核心线程数未达到上限, 创建并启动新的核心线程
        if (coreList.size() < corePoolSize) {
            Thread thread = new CoreThread();
            coreList.add(thread);
            thread.start();
        }

        // 尝试将任务放入阻塞队列
        if (blockingQueue.offer(command)) {
            return;
        }

        // 如果总线程数未达到最大值, 创建并启动新的支持线程
        if (coreList.size() + supportList.size() < maxSize) {
            Thread thread = new SupportThread();
            supportList.add(thread);
            thread.start();
        }

        // 如果任务仍然无法放入阻塞队列, 则调用拒绝策略处理
        if (!blockingQueue.offer(command)) {
            rejectHandle.reject(command, this);
        }
    }

    /**
     * 核心线程类, 负责从阻塞队列中取任务并执行.
     */
    class CoreThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // 从阻塞队列中取任务并执行
                    Runnable command = blockingQueue.take();
                    command.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 支持线程类, 负责在空闲时间内从阻塞队列中取任务并执行.
     */
    class SupportThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    // 在指定时间内从阻塞队列中取任务并执行
                    Runnable command = blockingQueue.poll(timeout, timeUnit);
                    if (command == null) {
                        // 如果超时未取到任务, 结束线程
                        break;
                    }
                    command.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // 打印线程结束信息
            System.out.println(Thread.currentThread().getName() + "线程结束了");
        }
    }

    /**
     * 获取阻塞队列.
     *
     * @return 阻塞队列
     */
    public BlockingQueue<Runnable> getBlockingQueue() {
        return blockingQueue;
    }
}
