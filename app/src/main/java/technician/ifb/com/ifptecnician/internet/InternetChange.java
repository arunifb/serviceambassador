package technician.ifb.com.ifptecnician.internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetChange extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


    }

    private boolean isonline(Context context){

        try{

            ConnectivityManager manager=(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();

            return (networkInfo != null && networkInfo.isConnected());

        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }

    }

}
