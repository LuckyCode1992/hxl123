package com.mysirui.vehicle.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by marlin on 2017/8/25.
 */

public class AndroidUtil {

    private static Application application;

    public static boolean isAPPForceground(){
        return counts.get() > 0;
    }

    static AtomicInteger counts = new AtomicInteger(0) ;

    static Map<Context,Subscriber<? super Boolean>> subscriberMap = new ConcurrentHashMap<Context,Subscriber<? super Boolean>>();

    public static Observable<Boolean> appState(final Activity context){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriberMap.put(context,subscriber);
                subscriber.onNext(counts.get()>0);
            }
        });
    }

    private static void notifyAppState(boolean on){
        for(Subscriber<? super Boolean> subscriber:subscriberMap.values()){
            subscriber.onNext(on);
        }
    }

    public static Application getApplication(){return  application;}

    public static void setApplication(Application application) {
        AndroidUtil.application = application;
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if(counts.incrementAndGet()==1){
                    AndroidUtil.notifyAppState(true);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if(counts.decrementAndGet()==0){
                    AndroidUtil.notifyAppState(false);
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if(subscriberMap.containsKey(activity)){
                    RxUtil.finish(subscriberMap.get(activity));
                    subscriberMap.remove(activity);
                }
            }
        });
    }



}
