package com.mysirui.vehicle.framework;

import com.mysirui.vehicle.dataModel.StatusItem;

import java.util.List;

/**
 * Created by marlin on 2017/8/21.
 */

public interface IStatusListener {

    void onStatusChange(List<StatusItem> statusItemList);

}
