package com.github.xiaoma.rpc.core.net.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by chunxiao on 2016/5/14.
 */
public class Server implements Runnable {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);
            while (!Thread.interrupted()) {
                new Thread(new Handler(ss.accept())).start();
                // or single-thread,or a thread pool
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Handler implements Runnable {

        final static int MAX_INPUT = 100;
        final Socket s;

        Handler(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            try {
                byte[] input = new byte[MAX_INPUT];
                s.getInputStream().read(input);
                byte[] output = process(input);
                s.getOutputStream().write(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private byte[] process(byte[] cmd) {
            return null;
        }
    }
}
