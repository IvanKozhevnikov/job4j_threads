package ru.job4j.concurrent;

public class Wget {
    public static void main(String[] args) {

    Thread thread = new Thread(
            () -> {

                try {
                    for (int i = 0; i <= 101; i++) {
                        Thread.sleep(1000);
                        String j = i != 101 ? "Loading : " + i + "%" : "Loaded";
                        System.out.print("\r" + j);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    );
        thread.start();
        System.out.println("Main");
}
}
