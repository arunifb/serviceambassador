package technician.ifb.com.ifptecnician.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telecom.Call;

import technician.ifb.com.ifptecnician.service.CallNoticatication;

public class NotificationServiceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       // context.startService(new Intent(context, CallNoticatication.class));
        System.out.println("NotificationServiceBroadcastReceiver call");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, CallNoticatication.class));
        } else {
            context.startService(new Intent(context, CallNoticatication.class));
        }
    }


}
