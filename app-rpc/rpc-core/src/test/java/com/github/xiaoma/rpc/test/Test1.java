package com.github.xiaoma.rpc.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.Channels;
import java.nio.channels.CompletionHandler;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by chunxiao on 2016/5/9.
 */
public class Test1 {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        SocketAddress address = new InetSocketAddress(args[0], Integer.parseInt(args[1]));
        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
        Future<Void> connected = client.connect(address);
        ByteBuffer buffer = ByteBuffer.allocate(74);
        // 等待连接完成
        connected.get();

        Future<Integer> read = client.read(buffer);

        // 其他工作

        // 等待读取完成
        read.get();

        // 回收并清空缓冲区
        buffer.flip();


        WritableByteChannel out = Channels.newChannel(System.out);
        out.write(buffer);

        client.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                buffer.flip();
                WritableByteChannel out = Channels.newChannel(System.out);
                try {
                    out.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                System.err.println(exc.getMessage());
            }
        });
    }
}
