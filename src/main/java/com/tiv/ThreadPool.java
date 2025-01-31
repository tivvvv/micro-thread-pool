package com.tiv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {

    private int maxSize = 16;

    private int corePoolSize = 10;

    BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(1024);

    private final Runnable task = () -> {
        while (true) {
            try {
                Runnable command = blockingQueue.take();
                command.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    };

    List<Thread> coreList = new ArrayList<>();

    List<Thread> supportList = new ArrayList<>();

    void execute(Runnable command) {
        if (coreList.size() < corePoolSize) {
            Thread thread = new Thread(task);
            coreList.add(thread);
            thread.start();
        }
        if (blockingQueue.offer(command)) {
            return;
        }

        if (coreList.size() + supportList.size() < maxSize) {
            Thread thread = new Thread(task);
            supportList.add(thread);
            thread.start();
        }
        if (!blockingQueue.offer(command)) {
            throw new RuntimeException("阻塞队列已满");
        }
    }
}
