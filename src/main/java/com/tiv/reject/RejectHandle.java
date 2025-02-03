package com.tiv.reject;

import com.tiv.ThreadPool;

public interface RejectHandle {
    void reject(Runnable rejectCommand, ThreadPool threadPool);
}
