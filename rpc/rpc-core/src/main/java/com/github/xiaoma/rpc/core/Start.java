package com.github.xiaoma.rpc.core;

import com.github.xiaoma.rpc.core.impl.LogHandler;
import com.github.xiaoma.rpc.core.impl.TimeHandler;

/**
 * Created by chunxiao on 2016/5/9.
 */
public class Start {
    private static Notifier notifier = Notifier.getInstance();

    public static void main(String[] args) {
        try {
            LogHandler logHandler = new LogHandler();
            TimeHandler timeHandler = new TimeHandler();
            ServerHandler serverHandler = new ServerHandler();

            notifier.addListener(logHandler);
            notifier.addListener(timeHandler);
            notifier.addListener(serverHandler);

            System.out.println("Server starting ...");
            Server server = new Server(5000);
            Thread t = new Thread(server);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
