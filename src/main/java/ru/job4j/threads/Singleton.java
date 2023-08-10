package ru.job4j.threads;

public class Singleton {
    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
            if (instance == null) {
                instance = new Singleton();

        }
        return instance;
    }

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(Singleton::getInstance);
        t1.start();
        Thread t2 = new Thread(Singleton::getInstance);
        t2.start();
        t1.join();
        t2.join();
    }
}