package com.tiv.reject;

import com.tiv.ThreadPool;

public class ThrowRejectHandle implements RejectHandle {
    @Override
    public void reject(Runnable rejectCommand, ThreadPool threadPool) {
        throw new RuntimeException("阻塞队列满了");
    }
}
