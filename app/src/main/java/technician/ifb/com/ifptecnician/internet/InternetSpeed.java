package technician.ifb.com.ifptecnician.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

public class InternetSpeed {



    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;


    public int getspeed(Context context) {
        int downSpeed=0;
        try {


            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                //should check null because in airplane mode it will be null
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                downSpeed = nc.getLinkDownstreamBandwidthKbps();
                int upSpeed = nc.getLinkUpstreamBandwidthKbps();

                System.out.println("downSpeed"+downSpeed +"upSpeed"+ upSpeed);


            }

        } catch (Exception e) {

            e.printStackTrace();

        }
        return downSpeed;
    }
}
