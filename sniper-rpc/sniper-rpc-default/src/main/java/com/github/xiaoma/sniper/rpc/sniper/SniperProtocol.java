package com.github.xiaoma.sniper.rpc.sniper;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.RemotingException;
import com.github.xiaoma.sniper.remoting.exchange.*;
import com.github.xiaoma.sniper.remoting.exchange.support.ExchangeHandlerAdapter;
import com.github.xiaoma.sniper.rpc.*;
import com.github.xiaoma.sniper.rpc.protocol.AbstractProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.xiaoma.sniper.rpc.support.ProtocolUtils.serviceKey;

/**
 * Created by machunxiao on 17/1/10.
 */
public class SniperProtocol extends AbstractProtocol {

    private static final int DEFAULT_PORT = 9876;
    private static final Map<String, ExchangeServer> serverMap = new ConcurrentHashMap<>(); // <host:port,Exchanger>


    private ExchangeHandler handler = new ExchangeHandlerAdapter() {

        @Override
        public Object reply(ExchangeChannel channel, Object request) throws RemotingException {
            if (request instanceof Invocation) {
                Invocation inv = (Invocation) request;
                Invoker<?> invoker = getInvoker(channel, inv);
                RpcContext.getContext().setRemoteAddress(channel.getRemoteAddress());
                return invoker.invoke(inv);
            }
            throw new RemotingException(channel, request.getClass().getName() + ": " + request + ", channel: consumer: " + channel.getRemoteAddress() + " --> provider: " + channel.getLocalAddress());
        }

        @Override
        public void connected(Channel channel) throws RemotingException {

        }

        @Override
        public void disconnected(Channel channel) throws RemotingException {

        }

        @Override
        public void sent(Channel channel, Object message) throws RemotingException {

        }

        @Override
        public void received(Channel channel, Object message) throws RemotingException {

        }

        @Override
        public void caught(Channel channel, Throwable exception) throws RemotingException {

        }

    };

    @Override
    public int getDefaultPort() {
        return DEFAULT_PORT;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        URL url = invoker.getUrl();
        String key = serviceKey(url);
        SniperExporter<T> exporter = new SniperExporter<>(invoker, key, exporterMap);
        exporterMap.put(key, exporter);
        openServer(url);
        return exporter;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> clazz, URL url) throws RpcException {
        SniperInvoker<T> invoker = new SniperInvoker<>(clazz, url, getClients(url), invokers);
        invokers.add(invoker);
        return invoker;
    }

    private ExchangeClient[] getClients(URL url) {
        ExchangeClient[] clients = new ExchangeClient[1];
        for (int i = 0; i < clients.length; i++) {
            clients[i] = initClient(url);
        }
        return clients;
    }

    private ExchangeClient initClient(URL url) {
        try {
            ExchangeClient client = Exchangers.connect(url, handler);
            return client;
        } catch (RemotingException ex) {
            throw new RpcException(ex);
        }
    }

    private void openServer(URL url) {
        String key = url.getAddress();
        ExchangeServer server = serverMap.get(key);
        if (server == null) {
            serverMap.put(key, createServer(url));
        } else {
            server.reset(url);
        }
    }

    private ExchangeServer createServer(URL url) {
        ExchangeServer server;
        try {
            server = Exchangers.bind(url, handler);
            return server;
        } catch (RemotingException e) {
            throw new RpcException("Fail to start server(url: " + url + ") " + e.getMessage(), e);
        }
    }

    private Invoker<?> getInvoker(Channel channel, Invocation inv) throws RemotingException {

        String serviceKey = serviceKey(channel.getUrl());

        Exporter<?> exporter = exporterMap.get(serviceKey);
        if (exporter == null) {
            throw new RemotingException(channel, "");
        }
        return exporter.getInvoker();
    }


}
