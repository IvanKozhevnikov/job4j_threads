package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        Thread thread = new Thread(
                () -> {

                    System.out.println("Run");
                    var startAt = System.currentTimeMillis();
                    var file = new File("tmp.xml");
                    try (var in = new URL(url).openStream();
                         var out = new FileOutputStream(file)) {
                        System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
                        var dataBuffer = new byte[speed];
                        int bytesRead;
                        double countMemory = 0;
                        while ((bytesRead = in.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                            double downloadAt = System.nanoTime();
                            out.write(dataBuffer, 0, bytesRead);
                            double bytes = Files.size(file.toPath()) - countMemory;
                            double timeNano = System.nanoTime() - downloadAt;
                            double timeMilliseconds = timeNano / 1000000;
                            double byteSecond = bytes / timeMilliseconds;
                            double timePause = (byteSecond / 1000) - timeMilliseconds;
                            countMemory = Files.size(file.toPath());
                            long timePauseRounded = (long) timePause;
                            System.out.println("байт:                           " + bytes);
                            System.out.println("время загрузки:                 " + timeNano);
                            System.out.println("время загрузки в миллисекундах: " + timeMilliseconds);
                            System.out.println("байт в миллисекунду:            " + byteSecond);
                            System.out.println("время задержки не округлённое:  " + timePause);
                            System.out.println("время задержки:                 " + timePauseRounded);
                            System.out.println("---------------------------------------------------");
                            System.out.println("");
                            System.out.println("---------------------------------------------------");
                            if (speed <= 6000) {
                                Thread.sleep(timePauseRounded);
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        System.out.println(Files.size(file.toPath()) + " bytes");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        thread.start();
    }

    public static boolean isURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean validate(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("you must specify the root folder and the extension to search for");
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException, MalformedURLException, URISyntaxException {
        validate(args);
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }
}
