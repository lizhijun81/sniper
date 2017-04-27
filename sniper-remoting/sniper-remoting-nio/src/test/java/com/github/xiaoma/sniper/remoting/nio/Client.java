package com.github.xiaoma.sniper.remoting.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * Created by machunxiao on 2017/3/21.
 */
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        String host = "localhost";
        int port = 7788;

        int total = 20;
        final CountDownLatch latch = new CountDownLatch(total);
        for (int i = 0; i < total; i++) {
            new Thread(() -> {

                Socket socket = new Socket();
                try {
                    socket.connect(new InetSocketAddress(host, port));
                    OutputStream out = socket.getOutputStream();
                    out.write("hello world".getBytes());
                    out.flush();

                    InputStream in = socket.getInputStream();
                    byte[] data = new byte[1024];
                    int t = in.read(data);
                    System.out.println("client receive response:" + new String(data, 0, t));
                    latch.countDown();
                } catch (IOException e) {
                    logger.error("", e);
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.error("", e);

        }
    }
}