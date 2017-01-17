package com.github.xiaoma.sniper.core.serialize;

import java.io.IOException;

/**
 * Created by machunxiao on 17/1/17.
 */
public interface ObjectOutput {

    void writeObject(Object obj) throws IOException;


}
