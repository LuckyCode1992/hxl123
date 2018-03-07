package fuzik.com.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mysirui.vehicle.SRBleSDK;
import com.sprout.frame.baseframe.utils.coder.AES;

import org.bouncycastle.util.encoders.Hex;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action0;
import rx.functions.Action1;

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

    Handler handler;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        handler = new Handler();
        aes = new AES();
        //   加解密 密钥
        key = "LD-FZ-1707200004";
        content = "123";
        etJiamiQian.setText(content);
        tvJiamiHou.setText("测试");
        bleSDK = SRBleSDK.with(this)
                .onConnect(new Action0() {
                    @Override
                    public void call() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvBle.setText("连接");
                            }
                        });
                    }
                })
                .onLogin(new Action0() {
                    @Override
                    public void call() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvBle.setText("登陆");
                            }
                        });
                    }
                })
                .onDisconn(new Action0() {
                    @Override
                    public void call() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvBle.setText("未连接");
                            }
                        });
                    }
                })
                .onMessage(new Action1<String>() {
                    @Override
                    public void call(final String s) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvJiesou.setText(s);
                            }
                        });
                    }
                });

    }

    @OnClick({R.id.btn_connect, R.id.btn_send, R.id.btn_lottie, R.id.btn_fragment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                bleSDK.start("TDCM-LDFZ", "", "");
                break;
            case R.id.btn_send:
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        byte[] enc = aes.encrypt(content.getBytes(), key.getBytes());
                        byte[] encode = Hex.encode(enc);
                        Log.d("--------", "enc：" + new String(enc));
                        Log.d("--------", "encode：" + new String(encode));
                        String str = new String(encode);
                        Log.d("--------", "加密后的内容：" + str);
                        // aes.iv1 = str;
                        bleSDK.sendMsg(str);
                        e.onNext(str);
                    }
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String o) throws Exception {
                                tvJiamiHou.setText(o);
                            }
                        });
                break;
            case R.id.btn_lottie:
                Log.d("点击测试", "lottie");
                startActivity(new Intent(MainActivity.this, LottieActivity.class));
                break;
            case R.id.btn_fragment:
                startActivity(new Intent(MainActivity.this, FragmentActivity.class));
                break;
        }
    }


}
