package com.github.xiaoma.sniper.core;

import com.github.xiaoma.sniper.core.utils.NetUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by machunxiao on 16/12/26.
 */
@Setter
@Getter
public final class URL implements Serializable {

    private static final long serialVersionUID = -7893775489021228661L;
    /**
     * <pre>
     *      http://username:password@example.com:1234/path/data?key=value&key2=value2#fragid1
     *        └┬┘   └───────┬───────┘ └────┬────┘  └┬┘           └─────────┬─────────┘ └──┬──┘
     *      protocol user information    host      port                  query         fragment
     * </pre>
     */
    private final String protocol;
    private final String username;
    private final String password;
    private final String host;
    private final int port;
    private final String path;
    private final Map<String, String> parameters;

    public URL(String protocol, String host, int port) {
        this(protocol, null, null, host, port, null, null);
    }

    public URL(String protocol, String username, String password, String host, int port, String path, Map<String, String> parameters) {
        if ((username == null || username.length() == 0)
                && password != null && password.length() > 0) {
            throw new IllegalArgumentException("Invalid url, password without username!");
        }
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.path = path;
        this.parameters = parameters;
    }


    public boolean hasParameter(String key) {
        String value = getParameter(key);
        return value == null || value.trim().length() == 0;
    }

    public String getParameter(String key) {
        String value = parameters.get(key);
        if (value == null || value.length() == 0) {
            value = parameters.get(Constants.DEFAULT_KEY_PREFIX + key);
        }
        return value;
    }

    public int getParameter(String key, int defaultValue) {
        String value = getParameter(key);
        int result;
        try {
            result = Integer.parseInt(value);
        } catch (Exception ex) {
            result = defaultValue;
        }
        return result;
    }

    public long getParameter(String key, long defaultValue) {
        String value = getParameter(key);
        long result;
        try {
            result = Integer.parseInt(value);
        } catch (Exception ex) {
            result = defaultValue;
        }
        return result;
    }

    public boolean getParameter(String key, boolean defaultValue) {
        String value = getParameter(key);
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public String getParameter(String key, String defaultValue) {
        String value = getParameter(key);
        value = value == null || value.trim().length() == 0 ? defaultValue : value;
        return value;
    }

    public int getPositiveParameter(String key, int defaultValue) {
        if (defaultValue <= 0) {
            throw new IllegalArgumentException("defaultValue <= 0");
        }
        int value = getParameter(key, defaultValue);
        if (value <= 0) {
            return defaultValue;
        }
        return value;
    }

    public int getMethodParameter(String methodName, String key, int defaultValue) {
        return 0;
    }

    public String getAddress() {
        return port <= 0 ? host : host + ":" + port;
    }

    public String getIp() {
        return NetUtils.getIpByHost(host);
    }
}
