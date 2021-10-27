package technician.ifb.com.ifptecnician.offlinedata;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.VolleyMultipartRequest;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.service.DashbordApi;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

import static android.content.Context.NOTIFICATION_SERVICE;

public class OfflineService extends Worker {

    Dbhelper dbhelper;
    private static final Integer NOTIFICATION_ID = 123;
    ErrorDetails errorDetails;
    String dbversion="";
    private RequestQueue rQueue;
    String Title="Service Order Update";
    Cursor cursor,offcursor,cur_image,offline_cursor;
    SessionManager sessionManager;
    String Partnerid;
    String Assigndata,Pendingdata;
    private NotificationManager notificationManager;

    public OfflineService(
            @NonNull Context context,
            @NonNull WorkerParameters parameters) {
        super(context, parameters);
        notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);

        dbhelper=new Dbhelper(getApplicationContext());
        dbversion=AllUrl.APP_Version;


        errorDetails=new ErrorDetails();
        sessionManager=new SessionManager(getApplicationContext());

    }

    @NonNull
    @Override
    public Result doWork() {

        setForegroundAsync(createForegroundInfo());
        doWorkInBackground();

        return Result.success();
    }

    private void doWorkInBackground() {

        for(int i =0;i<10000;i++){
          //  Log.i("----Tag",""+ String.valueOf(i));
            if(isStopped()){
                Log.i("----------Tag","stopped");
                cancelNotification();
                break;
            }

            if (sessionManager.isLoggedIn() ){

                if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

                    offcursor=dbhelper.get_offlineurl_data();
                    cur_image=dbhelper.get_all_offline_imagedata();
                    offline_cursor=dbhelper.get_offline_data();

                    int off_count=offcursor.getCount();
                    int image_count=cur_image.getCount();
                    int offlinecount=offline_cursor.getCount();

                    if (off_count > 0){
                        checkofflinedata();
                    }
                    else {


                        if (image_count > 0){

                            updaloadimage();

                        }
                        else if (offlinecount > 0){

                            uploadoffline();

                        }

                    }

                    HashMap<String, String> map = sessionManager.getUserDetails();
                    Partnerid = map.get(SessionManager.KEY_PartnerId);

                }
                else {

                    // updateNotification("No internet Connection");
                }

            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }



    private void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager)
                getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

    }

    private void updateNotification(String s) {

      //  Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/sharp");

        String channelId = getApplicationContext().getString(R.string.notification_channel_id);
        NotificationCompat.Builder notification = getNotificationBuilder(getApplicationContext(),channelId);
        notification.setContentText(s);
//        notification.setColor(Color.RED);
//        notification.setSound(sound);
        NotificationManagerCompat.from(getApplicationContext()).notify(NOTIFICATION_ID,notification.build());


    }

    @NonNull
    private ForegroundInfo createForegroundInfo() {
        // Build a notification using bytesRead and contentLength

        Context context = getApplicationContext();
        String id = context.getString(R.string.notification_channel_id);
        String title = context.getString(R.string.notification_title);
        String cancel = context.getString(R.string.cancel_download);
        // This PendingIntent can be used to cancel the worker
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(id);
        }

        NotificationCompat.Builder notification = getNotificationBuilder(getApplicationContext(),id);

       // return new ForegroundInfo(NOTIFICATION_ID, notification.build());

        return  null;
    }

    private NotificationCompat.Builder getNotificationBuilder(Context applicationContext, String id) {

        Intent notificationIntent = new Intent(applicationContext, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext,
                0,notificationIntent,0);


        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/sharp");
               // notification.setSound(sound);

        return new NotificationCompat.Builder(applicationContext, id)
                .setContentTitle(Title)
                .setTicker("title")
                .setSmallIcon(R.drawable.ic_notification_name)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSound(sound)
                .setAutoCancel(false);


        // Add the cancel action to the notification which can
        // be used to cancel the worker
//                .addAction(android.R.drawable.ic_delete, "cancel", intent);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel(String id) {
        // Create a Notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test";//getString(R.string.channel_name);
            String description = "Service order";//getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager)getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void status(String str) {
        SpannableStringBuilder spn = new SpannableStringBuilder(str + '\n');
        spn.setSpan(new ForegroundColorSpan(getApplicationContext().getResources().getColor(R.color.apptextcolor)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // receiveText.append(spn);
       // updateNotification(spn.toString());

    }

    private void disconnect() {

//        service.disconnect();
    }

    public void checkofflinedata(){

        try {

            offcursor=dbhelper.get_offlineurl_data();

            if (offcursor != null) {

                if (offcursor.moveToFirst()) {

                    do {

                        final int recordid = offcursor.getInt(offcursor.getColumnIndex("recordid"));

                        String datas=offcursor.getString(offcursor.getColumnIndex("Fileurl"));
                        String TicketNo=offcursor.getString(offcursor.getColumnIndex("Ticketno"));

                        System.out.println("offline ticket no"+TicketNo);
                        String type=offcursor.getString(offcursor.getColumnIndex("Type"));
                        submitfinaldata(datas,TicketNo);
                        getimagedata(TicketNo);

                    } while (offcursor.moveToNext());
                    offcursor.close();
                }
            }
            else {



            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void submitfinaldata(String data,String TicketNo){

       String url=AllUrl.baseUrl+"ZTECHSO/AddNewZTechSO";

      // String url=AllUrl.baseUrl+"so/softcloser-by-app";

        upload_offline_deleted_spare();

        getstarttime();

        System.out.println(url);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final String requestBody = data;

        System.out.println("requestBody"+requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("200")){

                    Title="Service Order Update";
                  //  updateNotification("Offline ticket "+TicketNo+" data upload successfully ");

                    dbhelper.delete_offlineurl(TicketNo);
                    errorDetails.Errorlog(getApplicationContext(),"File%20Upload", "Offline%20service","Data%20Upload%20done",response,"",TicketNo,"S","",dbversion, Valuefilter.getdate());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);

    }

    public void upload_offline_deleted_spare(){

        try{
            Cursor delspare_cursor;
            delspare_cursor=dbhelper.get_offline_deletespare_data();

            if (delspare_cursor !=null){

                if (delspare_cursor.moveToFirst()) {

                    //    Ticketno TEXT, Status TEXT, Serialno TEXT, DOP TEXT , DOI TEXT , Icr_date TEXT, Icr_no TEXT, Feedback TEXT, Member TEXT, House TEXT, Re_date TEXT, Re_time TEXT, Chnge_status TEXT, Pend_res TEXT,Pend_causes TEXT );";
                    do {

                        String Ticketno=delspare_cursor.getString(delspare_cursor.getColumnIndex("Ticketno"));
                        String url=delspare_cursor.getString(delspare_cursor.getColumnIndex("Url"));
                        upload_deleted_spare(url,Ticketno);

                        try{
                            Thread.sleep(7000);
                        }catch (Exception e){

                            e.printStackTrace();
                        }


                    } while ( delspare_cursor.moveToNext());

                    delspare_cursor.close();

                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void upload_deleted_spare(String url,String Ticketno) {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            String uploadurl =url;
            System.out.println("upload_deleted_spare-->  " + uploadurl);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest postRequest = new StringRequest(Request.Method.POST,uploadurl,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            dbhelper.delete_deletespare_data(Ticketno);

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {

                    return new HashMap<String, String>();
                }
            };
            queue.add(postRequest);

        } else {

        }
    }

    public void getstarttime(){


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()){
            try {

                Cursor scursor;

                scursor=dbhelper.get_starttime();

                if (scursor != null) {

                    if (scursor.moveToFirst()) {

                        do {

                            String TicketNo=scursor.getString(scursor.getColumnIndex("ticketno"));

                            System.out.println(TicketNo);


                            String jobstarttime=scursor.getString(scursor.getColumnIndex("jobstarttime"));
                            String mcheck=scursor.getString(scursor.getColumnIndex("mcheck"));
                            String water_code=scursor.getString(scursor.getColumnIndex("water_code"));
                            String tds_level=scursor.getString(scursor.getColumnIndex("tds_level"));
                            String PartnerId=scursor.getString(scursor.getColumnIndex("partnerid"));

                            if (mcheck.length()==0){
                                mcheck="NA";
                            }
                            if (tds_level.length()==0){
                                tds_level="NA";
                            }


                            try{

                                Thread.sleep(9000);
                            }catch (Exception e){
                                e.printStackTrace();

                            }

                            updatetime(TicketNo,jobstarttime,mcheck,water_code,tds_level,PartnerId);


                        } while (cursor.moveToNext());

                    }
                }
                else {

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void updatetime(String TicketNo,String jobstarttime,String mcheck,String water_code,String tds_level,String PartnerId){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            if (jobstarttime.length()==0){

                jobstarttime="00:00";
            }

            String url = AllUrl.baseUrl+"bulk-request/save/?model.OrderId="+TicketNo+"&model.StartTime="+jobstarttime+"&model.ManCheckId="+mcheck+"&model.WaterSourceId="+water_code+"&model.TDS="+tds_level+"&model.CreatedBy="+PartnerId;


            System.out.println("get all sales-->  " + url);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            dbhelper.delete_starttime(TicketNo);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {

                    return new HashMap<String, String>();
                }
            };
            queue.add(postRequest);


        }
    }

    public void getimagedata(String Ticketno){

        try {

            Cursor image_cursor;
            image_cursor=dbhelper.get_offline_image_data(Ticketno);

            if (image_cursor != null) {

                if (image_cursor.moveToFirst()) {

                    do {

                        byte [] Serialnodata=image_cursor.getBlob(image_cursor.getColumnIndex("Serialnoimage"));
                        byte [] Oduserialnodata=image_cursor.getBlob(image_cursor.getColumnIndex("Oduserialnoimage"));
                        byte [] Invioceimage=image_cursor.getBlob(image_cursor.getColumnIndex("Invioceimage"));
                        byte [] Signimage=image_cursor.getBlob(image_cursor.getColumnIndex("Signimage"));

                        try{

                            if (Serialnodata ==null && Oduserialnodata == null && Invioceimage ==null &&  Signimage==null ){

                            }

                            else{

                                String status=image_cursor.getString(image_cursor.getColumnIndex("Status"));

                                if (status.equals("notupload")){

                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                    String ts = dfs.format(c);
                                    uploadImage(Ticketno,Serialnodata,Oduserialnodata,Invioceimage,Signimage,ts);
                                }

                            }


                        }catch (Exception e){

                            e.printStackTrace();
                        }
                    } while ( image_cursor.moveToNext());

                    image_cursor.close();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void uploadImage(String TicketNo,byte [] seraialno_image_data,byte [] oduserialno_image_data,byte [] invoice_image_data ,byte [] Signdata,String tss){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {
            String  upload_URL= AllUrl.baseUrl+"doc/uploaddoc?";

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {


                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();


                            if (response.statusCode==200){

                                dbhelper.delete_image_data(TicketNo);

//                            Toast.makeText(CustomerDetailsActivity.this, "Image Upload done", Toast.LENGTH_SHORT).show();

                            }
                            else {

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    //model.AppDocUpload.
                    params.put("TicketNo",TicketNo);
                    params.put("UploadDate",tss);
                    params.put("ClientDocs", "ClientDocs");
                    System.out.println(params);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("InvoicePhto", new DataPart("invoicephoto_" + TicketNo +".jpeg", invoice_image_data));
                    params.put("ODUSerialPhoto", new DataPart("oduserialno_" + TicketNo + ".jpeg", oduserialno_image_data));
                    params.put("SerialPhoto", new DataPart("serialno_" +TicketNo+ ".jpeg", seraialno_image_data));
                    params.put("SignaturePhoto", new DataPart("signature_" +TicketNo+ ".jpeg",Signdata));
                    System.out.println(params);
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(getApplicationContext());
            rQueue.add(volleyMultipartRequest);

        } else {

        }
    }

    public void  getDashborddata(){


        String url=AllUrl.baseUrl+"DashBoard/getDashBoardCount?model.dashboard.PartnerId="+Partnerid;

        RequestQueue queue=Volley.newRequestQueue(getApplicationContext());


        StringRequest request=new StringRequest(Request.Method.GET, url, response -> {


            try {

                JSONObject object=new JSONObject(response);

                dbhelper.deletedashboard();

                String status=object.getString("Status");
                String Message=object.getString("Message");
                if (status.equals("true")){

                    JSONArray jsonArray = new JSONArray(object.getString("Data"));

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject objs = jsonArray.getJSONObject(i);
                        dbhelper.insert_dashboarddata("Total",objs.getString("Total"));
                        dbhelper.insert_dashboarddata("Assigned",objs.getString("Assigned"));
                        dbhelper.insert_dashboarddata("Pending",objs.getString("Pending"));
                        dbhelper.insert_dashboarddata("Resolved(softclosure)",objs.getString("SoftClosed"));
                        dbhelper.insert_dashboarddata("Closed",objs.getString("Closed"));
                        dbhelper.insert_dashboarddata("RequestCancellation",objs.getString("ReqCan"));
                        dbhelper.insert_dashboarddata("Cancelled",objs.getString("Cancelled"));
                        dbhelper.insert_dashboarddata("Negative Response",objs.getString("NegResFrCut"));

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                System.out.println(error.toString());
            }
        });


        int socketTimeout = 10000; //10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);

    }

    public void Beforeload(){

        try{

            cursor=dbhelper.getalldashboard();

            if (cursor != null) {

                if (cursor.moveToFirst()) {

                    do {

                       // final int recordid = cursor.getInt(cursor.getColumnIndex("recordid"));
                        String Name = cursor.getString(cursor.getColumnIndex("Name"));
                        String Count = cursor.getString(cursor.getColumnIndex("Count"));

                        switch (Name) {



                            case "Assigned":
                                 Assigndata=Count;
                                break;

                            case "Pending":

                               Pendingdata=Count;
                               //updateNotification("Assign "+Assigndata+" Pending "+Pendingdata);

                                break;
                            default:

                                break;

                        }
                    } while (cursor.moveToNext());

                    cursor.close();
                }
            } else {

               // updateNotification("No Records Found");
               // Toast.makeText(getContext(), "No Record Found", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void updaloadimage(){


        try {

            Cursor image_cursor;
            image_cursor=dbhelper.get_all_offline_imagedata();

            if (image_cursor != null) {

                if (image_cursor.moveToFirst()) {

                    do {

                        String Ticketno=image_cursor.getString(image_cursor.getColumnIndex("Ticketno"));

                        byte [] Serialnodata=image_cursor.getBlob(image_cursor.getColumnIndex("Serialnoimage"));
                        byte [] Oduserialnodata=image_cursor.getBlob(image_cursor.getColumnIndex("Oduserialnoimage"));
                        byte [] Invioceimage=image_cursor.getBlob(image_cursor.getColumnIndex("Invioceimage"));
                        byte [] Signimage=image_cursor.getBlob(image_cursor.getColumnIndex("Signimage"));

                        try{

                            if (Serialnodata ==null && Oduserialnodata == null && Invioceimage ==null &&  Signimage==null ){

                            }

                            else{

                                String status=image_cursor.getString(image_cursor.getColumnIndex("Status"));

                                if (status.equals("notupload")){

                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                    String ts = dfs.format(c);
                                    uploadImage(Ticketno,Serialnodata,Oduserialnodata,Invioceimage,Signimage,ts);
                                }

                            }


                        }catch (Exception e){

                            e.printStackTrace();
                        }
                    } while ( image_cursor.moveToNext());

                    image_cursor.close();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadoffline() {

        Cursor cursor;
        cursor=dbhelper.get_offline_data();

        if (cursor != null){

            if (cursor.moveToFirst()){

                do{

//                    values.put("ticketno",ticketno);
//                    values.put("submitdata",submitdata);
//                    values.put("type",type);
//                    values.put("url",url);

                    String ticketno=cursor.getString(cursor.getColumnIndex("ticketno"));
                    String data=cursor.getString(cursor.getColumnIndex("submitdata"));
                    String type=cursor.getString(cursor.getColumnIndex("type"));
                    String url=cursor.getString(cursor.getColumnIndex("url"));

                    if (type.equals("POST")){
                        updatepostdata(ticketno,data,url);

                    }


                }while (cursor.moveToNext());
            }
        }

    }

    public void updatepostdata(String ticketno,String data,String url){



        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {



            String requestbody=data;

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try {



                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestbody == null ? null : requestbody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";

                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);

                        try {

                         dbhelper.delete_offline_data(ticketno);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            queue.add(request);

        }


    }
}
