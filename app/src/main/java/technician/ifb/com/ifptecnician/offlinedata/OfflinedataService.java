package technician.ifb.com.ifptecnician.offlinedata;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import androidx.annotation.Nullable;
import technician.ifb.com.ifptecnician.VolleyMultipartRequest;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

public class OfflinedataService extends IntentService {

    Dbhelper dbhelper;
    Cursor cursor;
    private RequestQueue rQueue;
    ErrorDetails errorDetails;
    String dbversion="";
    String tit="";

  public  OfflinedataService(){
        super("OfflinedataService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbhelper=new Dbhelper(OfflinedataService.this);



    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (CheckConnectivity.getInstance(OfflinedataService.this).isOnline()){
            try {

                upload_offline_deleted_spare();

                cursor=dbhelper.get_offlineurl_data();

                if (cursor != null) {

                    if (cursor.moveToFirst()) {

                        do {

                            final int recordid = cursor.getInt(cursor.getColumnIndex("recordid"));

                            String datas=cursor.getString(cursor.getColumnIndex("Fileurl"));
                            String TicketNo=cursor.getString(cursor.getColumnIndex("Ticketno"));
                            getimagedata(TicketNo);
                            System.out.println("offline ticket no"+TicketNo);
                            String type=cursor.getString(cursor.getColumnIndex("Type"));
                            submitfinaldata(datas,TicketNo);
                            Thread.sleep(6000);

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

        if (CheckConnectivity.getInstance(OfflinedataService.this).isOnline()){
            try {

               Cursor scursor;

                scursor=dbhelper.get_starttime();

                if (scursor != null) {

                    if (scursor.moveToFirst()) {

                        do {

                         //   private  static  final String TABLE_Starttime = "CREATE TABLE starttime
                            //   (recordid INTEGER PRIMARY KEY, ticketno TEXT , jobstarttime TEXT , mcheck TEXT ,
                            //   water_code TEXT , tds_level TEXT , partnerid TEXT );";



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
                            updatetime(TicketNo,jobstarttime,mcheck,water_code,tds_level,PartnerId);
                            Thread.sleep(9000);

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

    public void getimagedata(String Ticketno){

        try {

            Cursor image_cursor;
            image_cursor=dbhelper.get_offline_image_data(Ticketno);

            if (image_cursor != null) {

                if (image_cursor.moveToFirst()) {

                    //    Ticketno TEXT, Status TEXT, Serialno TEXT, DOP TEXT , DOI TEXT , Icr_date TEXT, Icr_no TEXT, Feedback TEXT, Member TEXT, House TEXT, Re_date TEXT, Re_time TEXT, Chnge_status TEXT, Pend_res TEXT,Pend_causes TEXT );";

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

        if (CheckConnectivity.getInstance(this).isOnline()) {
            String  upload_URL= AllUrl.baseUrl+"doc/uploaddoc?";

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            System.out.println(response);
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();

                            System.out.println(response.statusCode);

                            if (response.statusCode==200){

                                System.out.println("Image upload done");
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
            rQueue = Volley.newRequestQueue(this);
            rQueue.add(volleyMultipartRequest);

        } else {

        }
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

                    } while ( delspare_cursor.moveToNext());

                    delspare_cursor.close();

                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void upload_deleted_spare(String url,String Ticketno) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            String uploadurl =url;
            System.out.println("upload_deleted_spare-->  " + uploadurl);

            RequestQueue queue = Volley.newRequestQueue(this);

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
                    Map<String, String>  params = new HashMap<String, String>();

                    return params;
                }
            };
            queue.add(postRequest);

        } else {

        }
    }

    public void submitfinaldata(String data,String TicketNo){

        //String url=AllUrl.baseUrl+"ZTECHSO/AddNewZTechSO";

        String url="https://sapsecurity.ifbhub.com/api/so/softcloser-by-app_updated";

        System.out.println(url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String requestBody = data;

        System.out.println("requestBody"+requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("200")){

                    dbhelper.delete_offlineurl(TicketNo);
                    errorDetails.Errorlog(OfflinedataService.this,"File%20Upload", "Offline%20service","Data%20Upload%20done",response,"",TicketNo,"S","",dbversion, Valuefilter.getdate());
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

    public void updatetime(String TicketNo,String jobstarttime,String mcheck,String water_code,String tds_level,String PartnerId){

        if (CheckConnectivity.getInstance(this).isOnline()) {

            if (jobstarttime.length()==0){

                jobstarttime="00:00";
            }

            String url = "https://sapsecurity.ifbhub.com/api/bulk-request/save/?model.OrderId="+TicketNo+"&model.StartTime="+jobstarttime+"&model.ManCheckId="+mcheck+"&model.WaterSourceId="+water_code+"&model.TDS="+tds_level+"&model.CreatedBy="+PartnerId;


            System.out.println("get all sales-->  " + url);

            RequestQueue queue = Volley.newRequestQueue(this);

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
                    Map<String, String> params = new HashMap<String, String>();

                    return params;
                }
            };
            queue.add(postRequest);


        }
    }

}
