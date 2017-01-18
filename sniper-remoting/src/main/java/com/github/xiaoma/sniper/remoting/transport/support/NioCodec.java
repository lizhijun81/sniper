package com.github.xiaoma.sniper.remoting.transport.support;

import com.github.xiaoma.sniper.core.URL;
import com.github.xiaoma.sniper.core.serialize.Serialization;
import com.github.xiaoma.sniper.core.utils.ReflectionUtils;
import com.github.xiaoma.sniper.remoting.Channel;
import com.github.xiaoma.sniper.remoting.Request;
import com.github.xiaoma.sniper.remoting.Response;
import com.github.xiaoma.sniper.remoting.codec.AbstractCodec;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by machunxiao on 17/1/17.
 */
public class NioCodec extends AbstractCodec {

    @Override
    public byte[] encode(Channel channel, ByteBuffer buffer, Object message) throws IOException {
        if (message instanceof Request) {
            return encodeRequest(channel, (Request) message);
        } else if (message instanceof Response) {
            return encodeResponse(channel, (Response) message);
        }
        return getSerialization(channel).serialize(channel.getUrl(), message);
    }

    /**
     * ByteBuffer 字节分布：
     * <p>
     * <pre>
     *     Header:
     *
     *     00-01 : magic
     *        02 : version
     *        03 : flag
     *     04-11 : request id
     *     12-15 : body content length
     *
     * </pre>
     *
     * @param channel
     * @param buffer
     * @return
     * @throws IOException
     */
    @Override
    public Object decode(Channel channel, ByteBuffer buffer) throws IOException {
        short type = buffer.getShort();     // 00-01
        byte version = buffer.get();        //    02
        byte flag = buffer.get();           //    03
        long requestId = buffer.getLong();  // 04-11
        int bodyLength = buffer.getInt();   // 11-15
        byte[] body = new byte[bodyLength]; // body
        buffer.get(body);

        byte dataType = flag;  // request、response、other

        if (dataType == 0) {
            return decodeRequest(channel, body);
        } else {
            return decodeResponse(channel, body);
        }
    }

    private Object decodeResponse(Channel channel, byte[] body) throws IOException {
        return null;
    }

    private byte[] encodeResponse(Channel channel, Response response) throws IOException {
        return null;
    }

    private Object decodeRequest(Channel channel, byte[] body) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(body);
             ObjectInput objectInput = createInput(bais)
        ) {
            Request request = new Request();

            long requestId = objectInput.readLong();
            String interfaceName = objectInput.readUTF();
            String methodName = objectInput.readUTF();

            String parameterTypes = objectInput.readUTF();

            Object[] arguments = null;
            Map<String, String> attachments = null;

            int argumentLength = objectInput.readInt();
            if (argumentLength > 0) {
                arguments = decodeArguments(parameterTypes, objectInput, channel);
            }
            int attachmentSize = objectInput.readInt();
            if (attachmentSize > 0) {
                attachments = new HashMap<>(attachmentSize);
                for (int i = 0; i < attachmentSize; i++) {
                    String key = objectInput.readUTF();
                    String val = objectInput.readUTF();
                    attachments.put(key, val);
                }
            }
            request.setRequestId(requestId);
            request.setInterfaceName(interfaceName);
            request.setMethodName(methodName);
            request.setParameterTypes(parameterTypes);
            request.setArguments(arguments);
            request.setAttachments(attachments);
            return request;
        }
    }

    private byte[] encodeRequest(Channel channel, Request request) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutput objectOutput = createOutput(baos)
        ) {

            objectOutput.writeLong(request.getRequestId());
            objectOutput.writeUTF(request.getInterfaceName());
            objectOutput.writeUTF(request.getMethodName());
            objectOutput.writeUTF(request.getParameterTypes());

            Serialization serialization = getSerialization(channel);
            URL url = channel.getUrl();

            // arguments
            Object[] arguments = request.getArguments();
            if (arguments != null && arguments.length > 0) {
                for (Object argument : arguments) {
                    serialization.serialize(url, argument);
                }
            }

            // attachments
            Map<String, String> attachments = request.getAttachments();
            if (attachments == null || attachments.size() == 0) {
                objectOutput.writeInt(0);
            } else {
                objectOutput.writeInt(attachments.size());
                for (Map.Entry<String, String> entry : attachments.entrySet()) {
                    objectOutput.writeUTF(entry.getKey());
                    objectOutput.writeUTF(entry.getValue());
                }
            }

            objectOutput.flush();
            baos.flush();
            return baos.toByteArray();
        }
    }

    private Object[] decodeArguments(String parameterTypeDescriptions, ObjectInput objectInput, Channel channel) throws IOException {
        if (parameterTypeDescriptions == null || parameterTypeDescriptions.length() == 0) {
            return null;
        }
        URL url = channel.getUrl();
        Serialization serialization = getSerialization(channel);
        Class<?>[] parameterTypes;
        try {
            parameterTypes = ReflectionUtils.forNames(parameterTypeDescriptions);
        } catch (ClassNotFoundException e) {
            throw new IOException("ClassNotFoundException: ", e);
        }
        Object[] arguments = new Object[parameterTypes.length];
        for (int i = 0; i < arguments.length; i++) {
            try {
                byte[] data = (byte[]) objectInput.readObject();
                arguments[i] = serialization.deserialize(url, data, parameterTypes[i]);
            } catch (ClassNotFoundException e) {
                throw new IOException("ClassNotFoundException: ", e);
            }
        }
        return arguments;
    }

}
