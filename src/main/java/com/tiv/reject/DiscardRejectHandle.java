package com.tiv.reject;

import com.tiv.ThreadPool;

public class DiscardRejectHandle implements RejectHandle {
    @Override
    public void reject(Runnable rejectCommand, ThreadPool threadPool) {
        threadPool.getBlockingQueue().poll();
        threadPool.execute(rejectCommand);
    }
}
