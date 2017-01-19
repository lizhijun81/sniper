package com.github.xiaoma.sniper.core;

/**
 * Created by machunxiao on 17/1/6.
 */
public enum Constants {
    ;

    public static final String  DEFAULT_KEY_PREFIX                 = "default.";

    public static final String  APPLICATION_KEY                    = "application";

    public static final String  TIMEOUT_KEY                        = "timeout";

    public static final int     DEFAULT_TIMEOUT                    = 1000;

    public static final String  ASYNC_KEY                          = "async";

    public static final String  RETURN_KEY                         = "return";

    public static final String  TOKEN_KEY                          = "token";

    public static final String  GROUP_KEY                          = "group";

    public static final String  PATH_KEY                           = "path";

    public static final String  INTERFACE_KEY                      = "interface";

    public static final String  VERSION_KEY                        = "version";

    public static final String  EXCHANGER_KEY                      = "exchanger";

    public static final String  DEFAULT_EXCHANGER                  = "header";

    public static final String  HEARTBEAT_KEY                      = "heartbeat";

    public static final String  HEARTBEAT_TIMEOUT_KEY              = "heartbeat.timeout";

    // read from channel,write to channel timestamp
    public static final String KEY_READ_TIMESTAMP                  = "read_timestamp";
    public static final String KEY_WRITE_TIMESTAMP                 = "write_timestamp";

    public static final String CODEC_KEY                           = "codec";
    public static final String CODEC_DEFAULT_KEY                   = "nio";
    public static final String SERIALIZATION_KEY                   = "serialization";
    public static final String DEFAULT_SERIALIZATION               = "protobuf";

    public static final String PAYLOAD_KEY                         = "payload";
    public static final int    DEFAULT_PAYLOAD                     = 8 * 1024 * 1024; // 8M

    public static final String SIDE_KEY                            = "side";
}
