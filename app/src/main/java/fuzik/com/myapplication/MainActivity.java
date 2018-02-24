package fuzik.com.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mysirui.vehicle.SRBleSDK;
import com.sprout.frame.baseframe.utils.coder.AES;

import org.bouncycastle.util.encoders.Hex;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class MainActivity extends UIBaseActivity {


    SRBleSDK bleSDK;
    AES aes;
    String key;
    String content;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        aes = new AES();
        //   加解密 密钥
        key = "LD-FZ-1707200004";
        content = "1234567890123456";
//        bleSDK
//                .onConnect(new Action0() {
//                    @Override
//                    public void call() {
//
//                    }
//                })
//                .onLogin(new Action0() {
//                    @Override
//                    public void call() {
//
//                    }
//                })
//                .onDisconn(new Action0() {
//                    @Override
//                    public void call() {
//
//                    }
//                })
//                .start()
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        // 加密字符串
                        Log.d("--------","加密前的：" + content);
                        Log.d("--------","加密密钥：" + key);
                        // 加密方法
                        byte[] enc = aes.encrypt(content.getBytes(), key.getBytes());
                        Log.d("--------","加密后的内容：" + new String(Hex.encode(enc)));
                        aes.iv1 = new String(Hex.encode(enc));
//                        // 解密方法
//                        byte[] dec = aes.decrypt(enc, key.getBytes());
//                        System.out.println("解密后的内容：" + new String(dec));

                    }
                });
    }
}
