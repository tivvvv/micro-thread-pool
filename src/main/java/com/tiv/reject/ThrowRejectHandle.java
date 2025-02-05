package com.tiv.reject;

import com.tiv.ThreadPool;

/**
 * 当任务被拒绝时，该处理器会抛出异常。
 */
public class ThrowRejectHandle implements RejectHandle {

    /**
     * 抛出异常表示任务被拒绝。
     *
     * @param rejectCommand 被拒绝的任务
     * @param threadPool    线程池实例
     */
    @Override
    public void reject(Runnable rejectCommand, ThreadPool threadPool) {
        throw new RuntimeException("阻塞队列满了");
    }
}
