package com.mysirui.vehicle.framework;

import com.mysirui.vehicle.framework.MsgCoder;

/**
 * Created by marlin on 2017/8/15.
 */

public interface IBleMsgCoder<T> extends MsgCoder<T> {

    String getUUIDOfChar(T msg);

}
