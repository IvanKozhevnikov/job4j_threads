package ru.job4j.concurrent;

public class B {
    public static void main(String[] args) {
        for (int i = 0; i <= 100; i++) {
            System.out.print("Loading : " + i  + "%");
            System.out.print("\rLoading : " + i  + "%");
            //System.out.println("Loading : " + i  + "%");
        }
    }
}
