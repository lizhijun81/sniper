package com.github.xiaoma.sniper.core;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by machunxiao on 16/12/26.
 */
@Setter
@Getter
public final class URL {

    /**
     * abc://username:password@example.com:123/path/data?key=value&key2=value2#fragid1
     * └┬┘   └───────┬───────┘ └────┬────┘ └┬┘           └─────────┬─────────┘ └──┬──┘
     * scheme  user information   host     port                 query         fragment
     */
    private final String schema;
    private final String username;
    private final String password;
    private final String host;
    private final int port;
    private final String path;
    private final Map<String, String> parameters;

    public URL(String schema, String host, int port) {
        this(schema, null, null, host, port, null, null);
    }

    public URL(String schema, String username, String password, String host, int port, String path, Map<String, String> parameters) {
        if ((username == null || username.length() == 0)
                && password != null && password.length() > 0) {
            throw new IllegalArgumentException("Invalid url, password without username!");
        }
        this.schema = schema;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.path = path;
        this.parameters = parameters;
    }


}
