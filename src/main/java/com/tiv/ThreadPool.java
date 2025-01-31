package com.tiv;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {

    BlockingQueue<Runnable> commandList = new ArrayBlockingQueue<>(1024);

    Thread thread = new Thread(() -> {
        while (true) {
            try {
                Runnable command = commandList.take();
                command.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }, "单一线程");

    {
        thread.start();
    }

    void execute(Runnable command) {
        boolean offer = commandList.offer(command);
    }
}
