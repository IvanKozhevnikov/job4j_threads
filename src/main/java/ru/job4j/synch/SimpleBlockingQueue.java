package ru.job4j.synch;


import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

    private int maxSize;

    @GuardedBy("this")
    private final Queue<T> queue = new LinkedList<>();

    public SimpleBlockingQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    public synchronized void offer(T value) throws InterruptedException {
        while (queue.size() == maxSize) {
            wait();
        }
        queue.offer(value);
        notify();
    }

    public synchronized T poll() throws InterruptedException {
        while (queue.size() == 0) {
            wait();
        }
        T element = queue.poll();
        notify();
        return element;
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
