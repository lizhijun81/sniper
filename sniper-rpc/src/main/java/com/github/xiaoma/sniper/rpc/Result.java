package com.github.xiaoma.sniper.rpc;

import java.util.Map;

/**
 * Created by machunxiao on 17/1/6.
 */
public interface Result {

    Object getValue();

    Throwable getException();

    boolean hasException();

    /**
     * Recreate.
     * <p>
     * <code>
     * if (hasException()) {
     *    throw getException();
     * } else {
     *    return getValue();
     * }
     * </code>
     *
     * @return result.
     * @throws if has exception throw it.
     */
    Object recreate() throws Throwable;

    Map<String, String> getAttachments();

    String getAttachment(String key);

    String getAttachment(String key, String defaultValue);
}
