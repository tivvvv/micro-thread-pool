package com.tiv.reject;

import com.tiv.ThreadPool;

/**
 * 拒绝策略接口, 定义了如何处理被拒绝的任务.
 */
public interface RejectHandle {

    /**
     * 处理被拒绝的任务.
     *
     * @param rejectCommand 被拒绝的任务
     * @param threadPool    线程池实例
     */
    void reject(Runnable rejectCommand, ThreadPool threadPool);
}
