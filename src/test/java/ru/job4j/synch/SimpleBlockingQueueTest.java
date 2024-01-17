package ru.job4j.synch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SimpleBlockingQueueTest {

    @Test
    public void whenOfferAndPoll() throws InterruptedException {
        SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(1);
        List<Integer> expected = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        List<Integer> actual = new ArrayList<>();
        Thread producer = new Thread(
                () -> {
                    try {
                        for (int i : expected) {
                            simpleBlockingQueue.offer(i);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }

                }
        );
        Thread consumer = new Thread(
                () -> {
                    try {
                        actual.add(simpleBlockingQueue.poll());
                        actual.add(simpleBlockingQueue.poll());
                        actual.add(simpleBlockingQueue.poll());
                        actual.add(simpleBlockingQueue.poll());
                        actual.add(simpleBlockingQueue.poll());
                        actual.add(simpleBlockingQueue.poll());
                        System.out.println(actual);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
        );
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void whenOfferThenPoll() throws InterruptedException {
        List<Integer> expected = new ArrayList<>(List.of(0, 1, 2, 3, 4, 5));
        final ArrayList<Integer> actual = new ArrayList<>();
        final SimpleBlockingQueue<Integer> simpleBlockingQueue = new SimpleBlockingQueue<>(6);
        Thread producer = new Thread(
                () -> {
                    for (int i : expected) {
                        try {
                            simpleBlockingQueue.offer(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        producer.start();
        Thread consumer = new Thread(
                () -> {
                    while (!simpleBlockingQueue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            actual.add(simpleBlockingQueue.poll());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        Assertions.assertEquals(expected, actual);
    }
}