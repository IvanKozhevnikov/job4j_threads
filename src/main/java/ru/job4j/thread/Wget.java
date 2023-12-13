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
                    String fileName = url.substring(url.lastIndexOf('/') + 1);
                    var file = new File(fileName);
                    try (var in = new URL(url).openStream();
                         var out = new FileOutputStream(file)) {
                        var dataBuffer = new byte[speed];
                        long start = System.currentTimeMillis();
                        int bytesRead;
                        int downloadData = 0;
                        while ((bytesRead = in.read(dataBuffer, 0, speed)) != -1) {
                            downloadData += bytesRead;
                            out.write(dataBuffer, 0, bytesRead);
                            if (downloadData >= speed) {
                                long finish = System.currentTimeMillis();
                                long time = finish - start;
                                if (time < 1000) {
                                    Thread.sleep(1000 - time);
                                }
                                downloadData = 0;
                                start = System.currentTimeMillis();
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
