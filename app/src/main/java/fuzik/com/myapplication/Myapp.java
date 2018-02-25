package fuzik.com.myapplication;


import com.mysirui.vehicle.SRBleSDK;
import com.sprout.frame.baseframe.base.App;

public class Myapp extends App {
    @Override
    public void onCreate() {
        super.onCreate();
        SRBleSDK.iniWithApplication(this);
    }
}
