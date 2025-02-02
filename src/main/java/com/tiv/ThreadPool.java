package com.tiv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadPool {

    private int maxSize = 16;

    private int corePoolSize = 10;

    private int timeout = 1;

    private TimeUnit timeUnit = TimeUnit.SECONDS;

    BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(1024);

    private final Runnable coreTask = () -> {
        while (true) {
            try {
                Runnable command = blockingQueue.take();
                command.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private final Runnable supportTask = () -> {
        while (true) {
            try {
                Runnable command = blockingQueue.poll(timeout, timeUnit);
                if (command == null) {
                    break;
                }
                command.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.printf("线程%s结束了%n", Thread.currentThread().getName());
    };

    List<Thread> coreList = new ArrayList<>();

    List<Thread> supportList = new ArrayList<>();

    void execute(Runnable command) {
        if (coreList.size() < corePoolSize) {
            Thread thread = new Thread(coreTask);
            coreList.add(thread);
            thread.start();
        }
        if (blockingQueue.offer(command)) {
            return;
        }

        if (coreList.size() + supportList.size() < maxSize) {
            Thread thread = new Thread(supportTask);
            supportList.add(thread);
            thread.start();
        }
        if (!blockingQueue.offer(command)) {
            throw new RuntimeException("阻塞队列已满");
        }
    }
}
