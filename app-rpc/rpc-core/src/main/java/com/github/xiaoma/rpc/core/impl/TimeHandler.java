package com.github.xiaoma.rpc.core.impl;

import com.github.xiaoma.rpc.core.EventAdapter;
import com.github.xiaoma.rpc.core.RpcRequest;
import com.github.xiaoma.rpc.core.RpcResponse;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by chunxiao on 2016/5/9.
 */
public class TimeHandler extends EventAdapter {

    @Override
    public void onWrite(RpcRequest request, RpcResponse response) throws Exception {
        String command = request.getRequestId() + "";
        String time = null;
        Date date = new Date();
        if (command.equals("GB")) {
            DateFormat cnDate = DateFormat.getDateInstance(DateFormat.FULL, Locale.CHINA);
            time = cnDate.format(date);
        } else {
            DateFormat cnDate = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
            time = cnDate.format(date);
        }
        response.setResult(time.getBytes());
    }
}
