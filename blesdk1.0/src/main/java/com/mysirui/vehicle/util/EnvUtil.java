package com.mysirui.vehicle.util;

import android.app.Application;

/**
 * Created by marlin on 2017/7/3.
 */

public class EnvUtil {

    private static Application application;

    public static void setApplication(Application application) {
        EnvUtil.application = application;
    }


    public static Application getApplication() {
        return application;
    }

}
