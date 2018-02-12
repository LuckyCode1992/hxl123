package com.mysirui.vehicle.util;



import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * 集合工具  经度表示X，纬度表示Y
 * Created by jelly.liao on 2017/5/3.
 */

public class CollectionUtil {
    private static CollectionUtil instance = new CollectionUtil();

    public static CollectionUtil getInstance() {
        return instance;
    }

    private CollectionUtil() {

    }


    public static <T> void remove(List<T> list, Func1<T, Boolean> pre) {
        List willRemove = new ArrayList();
        for (T msg : list) {
            if (pre.call(msg)) {
                willRemove.add(msg);
            }
        }
        list.removeAll(willRemove);
    }

    public static <T> List<T> build(T... objs) {
        List<T> result = new ArrayList<T>();
        for (T obj : objs) {
            result.add(obj);
        }
        return result;
    }

}
