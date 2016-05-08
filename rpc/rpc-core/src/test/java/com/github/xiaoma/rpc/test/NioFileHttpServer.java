package com.github.xiaoma.rpc.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Created by chunxiao on 2016/5/8.
 */
public class NioFileHttpServer {
    private ByteBuffer contentBuffer;
    private int port = 8888;

    public NioFileHttpServer(ByteBuffer data, String encoding, String mimeType, int port) {
        this.port = port;
        String header = "HTTP/1.1 200 OK \r\n"
                + "Server: NioFileHttpServer\r\n"
                + "Content-Length: " + data.limit() + "\r\n"
                + "Content-Type: " + mimeType + "\r\n\r\n";
        byte[] headerData = header.getBytes(Charset.forName("US-ASCII"));
        ByteBuffer buffer = ByteBuffer.allocate(data.limit() + headerData.length);
        buffer.put(headerData);
        buffer.put(data);
        buffer.flip();
        this.contentBuffer = buffer;
    }

    public void run() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        InetSocketAddress localPort = new InetSocketAddress(port);
        serverChannel.bind(localPort);
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                try {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel channel = server.accept();
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        if (buffer.hasRemaining()) {
                            client.write(buffer);
                        } else {
                            // 结束工作
                            client.close();
                        }
                    } else if (key.isReadable()) {
                        // 解析http首部
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(4096);
                        client.read(buffer);
                        key.interestOps(SelectionKey.OP_WRITE);
                        key.attach(contentBuffer.duplicate());
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    key.cancel();
                    key.channel().close();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            String fileName = args[0];
            String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
            Path file = FileSystems.getDefault().getPath(fileName);
            byte[] data = Files.readAllBytes(file);
            ByteBuffer input = ByteBuffer.wrap(data);
            int port = Integer.parseInt(args[1]);
            String encoding = args[2];
            NioFileHttpServer server = new NioFileHttpServer(input, encoding, contentType, port);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
