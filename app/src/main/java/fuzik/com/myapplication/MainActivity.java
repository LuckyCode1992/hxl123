package fuzik.com.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mysirui.vehicle.SRBleSDK;
import com.sprout.frame.baseframe.utils.coder.AES;

import org.bouncycastle.util.encoders.Hex;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class MainActivity extends UIBaseActivity {


    SRBleSDK bleSDK;
    AES aes;
    String key;
    String content;
    @BindView(R.id.tv_ble)
    TextView tvBle;
    @BindView(R.id.btn_connect)
    Button btnConnect;
    @BindView(R.id.et_jiami_qian)
    EditText etJiamiQian;
    @BindView(R.id.tv_jiami_hou)
    TextView tvJiamiHou;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.tv_jiesou)
    TextView tvJiesou;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        aes = new AES();
        //   加解密 密钥
        key = "LD-FZ-1707200004";
        content = "1234567890123456";
        bleSDK = SRBleSDK.with(this)
                .onConnect(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .onLogin(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .onDisconn(new Action0() {
                    @Override
                    public void call() {

                    }
                });

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
                        Log.d("--------", "加密前的：" + content);
                        Log.d("--------", "加密密钥：" + key);
                        // 加密方法
                        byte[] enc = aes.encrypt(content.getBytes(), key.getBytes());
                        Log.d("--------", "加密后的内容：" + new String(Hex.encode(enc)));
                        aes.iv1 = new String(Hex.encode(enc));
//                        // 解密方法
//                        byte[] dec = aes.decrypt(enc, key.getBytes());
//                        System.out.println("解密后的内容：" + new String(dec));

                    }
                });
    }

    @OnClick({R.id.btn_connect, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
//                bleSDK.start()
                break;
            case R.id.btn_send:
//                bleSDK.
                break;
        }
    }
}
