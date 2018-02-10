package fuzik.com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ello
        //假装自己在主干git 开发
        //假装自己在修改bug
        //假装做了很多事情哦，，，，

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.aliyun.com");
                    URLConnection uc = url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    long time = uc.getDate();//取得网站日期时间
                    Date date = new Date(time);// 转换为标准时间对象
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);// 输出北京时间
                    String format = sdf.format(date);
                    Log.d("-------123--",time+"");
                    Log.d("-------123--",format+"");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
