package technician.ifb.com.ifptecnician.notification;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.core.app.NotificationCompat;
import technician.ifb.com.ifptecnician.NotificationView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.CallNoticatication;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class BackgroundService extends Service {
    private String LOG_TAG = null;
    Dbhelper dbhelper;
    SessionManager sessionManager;
    String  PartnerId;
    Cursor cursor;

    @Override
    public void onCreate() {
        super.onCreate();
        LOG_TAG = "app_name";
        Log.i(LOG_TAG, "service created");
        dbhelper=new Dbhelper(BackgroundService.this);
        sessionManager=new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "In onStartCommand");
        //ur actual code
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Wont be called as service is not bound
        Log.i(LOG_TAG, "In onBind");
        return null;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(LOG_TAG, "In onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroyed");
    }


    public void callapi(){


        if (CheckConnectivity.getInstance(BackgroundService.this).isOnline()) {


            // Toast.makeText(CallNoticatication.this, "api call", Toast.LENGTH_SHORT).show();
            System.out.println("call api");
            String url = AllUrl.baseUrl + "DashBoard/getDashBoardCount?model.dashboard.PartnerId=" + PartnerId + "";


            RequestQueue queue = Volley.newRequestQueue(BackgroundService.this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject object = new JSONObject(response);

                                String status = object.getString("Status");
                                String Message = object.getString("Message");
                                if (status.equals("true")) {


                                    JSONArray jsonArray = new JSONArray(object.getString("Data"));
                                    System.out.println(jsonArray.toString());
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject objs = jsonArray.getJSONObject(i);
                                        cursor = dbhelper.getalldashboard();

                                        if (cursor != null) {

                                            if (cursor.moveToFirst()) {

                                                do {

                                                    final int recordid = cursor.getInt(cursor.getColumnIndex("recordid"));
                                                    String Name = cursor.getString(cursor.getColumnIndex("Name"));
                                                    String Count = cursor.getString(cursor.getColumnIndex("Count"));

                                                    switch (Name) {

                                                        case "Total":
                                                            int current = Integer.parseInt(objs.getString("Total"));
                                                            int total_ = Integer.parseInt(Count);

                                                            showNotification();
                                                            if (current > total_){

                                                                showNotification();

                                                            }
                                                            break;


                                                        default:

                                                            break;

                                                    }
                                                } while (cursor.moveToNext());
                                            }
                                        }

                                    }
                                } else {

                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);


        }
    }

    public void showNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification_name)
                        .setContentTitle("One New Job Assign To You !!!")
                        .setContentText("Assign Call")
                        .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sharp));

        Intent notificationIntent = new Intent(this,NotificationView.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}