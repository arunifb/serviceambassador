package technician.ifb.com.ifptecnician.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import technician.ifb.com.ifptecnician.MyApplication;
import technician.ifb.com.ifptecnician.NotificationView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.notification.NotificationServiceBroadcastReceiver;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class CallNoticatication extends Service {

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Dbhelper dbhelper;
    SessionManager sessionManager;
    String  PartnerId;
    Cursor cursor,as_Cursor;
    public static final int notify = 60000;  //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;
    public int counter = 0;
    Context context;
    HashMap<String,String> hashMap=new HashMap<>();
    JSONArray jsonArray=new JSONArray();

    public CallNoticatication(Context applicationContext) {
        super();
        context = applicationContext;
        Log.i("HERE", "here service created!");
    }

    public CallNoticatication() {
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return  null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbhelper=new Dbhelper(getApplicationContext());
        sessionManager=new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, NotificationServiceBroadcastReceiver.class);
        this.sendBroadcast(broadcastIntent);
        System.out.println("destroy ");

    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        System.out.println("service start");
        super.onTaskRemoved(rootIntent);

    }

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 30000, 1000*5*60); //
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                GetAssignCallData(PartnerId,"Assigned");
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }




    public void showNotification(int size) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification_name)
                        .setContentTitle(size +" New Job Assign To You !!!")
                        .setContentText("Assign Call")
                        .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sharp));

        Intent notificationIntent = new Intent(this,NotificationView.class)
                .putExtra("model",jsonArray.toString());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
        startForeground(1, new Notification());
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground(int size)
    {
        String NOTIFICATION_CHANNEL_ID = "ifb.notification";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    private void GetAssignCallData(String PartnerId, String sta) {


        if (CheckConnectivity.getInstance(this).isOnline()) {

            String   url = AllUrl.baseUrl + "ServiceOrder/getServiceOrder?TechCode=" + PartnerId + "&Status=" + sta + "";
            System.out.println(url);
            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response == null) {

                                return;
                            }
                            else {

                                try {


                                    boolean status=false;

                                    for (int i = 0; i < response.length(); i++) {

                                        JSONObject object1 = response.getJSONObject(i);

                                        String AssignDate = object1.getString("AssignDate");
                                        AssignDate="02/04/2020 12:46:09 PM";
                                       // showNotification(3);

                                        try {

                                            Calendar now = Calendar.getInstance();
                                            now.add(Calendar.MINUTE, -30);
                                            Date x = now.getTime();

                                            SimpleDateFormat formats=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
                                            Date dts=formats.parse(AssignDate);

                                            System.out.println(x +" "+dts);

                                            if (x.after(dts)) {
                                                status=true;
                                                hashMap.put(""+i,object1.toString());

                                                jsonArray.put(object1);

                                                }
                                            else {

                                                System.out.println("else");
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }

                                    if (status){

                                        try{
                                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                                                startMyOwnForeground(hashMap.size());
                                            else
                                                showNotification(hashMap.size());
                                        }catch (Exception e){

                                            e.printStackTrace();
                                        }



                                    }


                                } catch (Exception e) {

                                    e.printStackTrace();

                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            MyApplication.getInstance().addToRequestQueue(request);
        }

    }

}
