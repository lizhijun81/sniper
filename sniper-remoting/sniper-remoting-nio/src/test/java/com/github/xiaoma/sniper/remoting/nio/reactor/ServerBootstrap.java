package com.github.xiaoma.sniper.remoting.nio.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by machunxiao on 2017/5/11.
 */
public class ServerBootstrap {

    private static final Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);

    private final String host;
    private final int port;

    public ServerBootstrap(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            Acceptor acceptor = new Acceptor();
            acceptor.bind(host, port);
            acceptor.start();
            logger.info("server listening at {}:{}.", host, port);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void main(String[] args) {

        String host = "localhost";
        int port = 7788;

        ServerBootstrap server = new ServerBootstrap(host, port);

        server.start();

    }
}
