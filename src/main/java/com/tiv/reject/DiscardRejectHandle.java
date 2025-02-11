package com.tiv.reject;

import com.tiv.ThreadPool;

/**
 * 当任务被拒绝时, 该处理器会丢弃阻塞队列中的一个任务, 并尝试执行被拒绝的任务.
 */
public class DiscardRejectHandle implements RejectHandle {

    /**
     * 处理被拒绝的任务.
     * 该方法会从线程池的阻塞队列中移除一个任务, 然后尝试将被拒绝的任务重新提交给线程池执行.
     *
     * @param rejectCommand 被拒绝的任务
     * @param threadPool    线程池实例
     */
    @Override
    public void reject(Runnable rejectCommand, ThreadPool threadPool) {
        // 从阻塞队列中移除一个任务, 腾出空间
        threadPool.getBlockingQueue().poll();
        // 尝试执行被拒绝的任务
        threadPool.execute(rejectCommand);
    }
}
