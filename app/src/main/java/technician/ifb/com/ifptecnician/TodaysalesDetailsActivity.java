package technician.ifb.com.ifptecnician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.adapter.Essential_add_adapter;
import technician.ifb.com.ifptecnician.adapter.Pending_item_adapter;
import technician.ifb.com.ifptecnician.adapter.ShelflifeAdapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.Essential_add_model;
import technician.ifb.com.ifptecnician.model.PendingSpare;
import technician.ifb.com.ifptecnician.model.Shelflifemodel;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

public class TodaysalesDetailsActivity extends AppCompatActivity {

    TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,
            tv_rcnno,tv_servicetype,tv_custname,tv_closedate,item_tstv_model,tv_sms_response;
    ImageView iv_call;
    TextView tv_techname,today_tv_address;
    TextView tv_model;

    String calltype,customer,rcnno,callbookdate,ticketno,status,address,closedate,techname,pincode;
    SharedPreferences prefdetails;
    ListView lv_shelflife;
    public ArrayList<Shelflifemodel> shelflifemodels = new ArrayList<>();
    public Shelflifemodel shelflifemodel;
    ShelflifeAdapter shelflifeAdapter;
    Cursor cursor;
    Dbhelper dbhelper;
    String currentdata;
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    String refno,smstype;
    ProgressBar taskProcessing;
    LinearLayout ll_nointernet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todaysales_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefdetails = getSharedPreferences("details", 0);
        dbhelper=new Dbhelper(getApplicationContext());
        Date c = Calendar.getInstance().getTime();
        currentdata=df.format(c);
        init();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void init(){

        tv_calltype=findViewById(R.id.item_tstv_calltype);
        tv_ticketno=findViewById(R.id.item_tstv_ticketno);
        tv_address=findViewById(R.id.item_tstv_address);
        tv_callbook=findViewById(R.id.item_tstv_callbookdate);
        tv_status=findViewById(R.id.item_tstv_status);
        tv_rcnno=findViewById(R.id.item_tstv_rcnno);
        tv_servicetype=findViewById(R.id.item_tstv_servicetype);
        tv_custname=findViewById(R.id.item_tstv_custname);
        iv_call=findViewById(R.id.item_tsiv_call);
        tv_sms_response=findViewById(R.id.tv_error_message);
        tv_closedate=findViewById(R.id.item_tstv_close);
        item_tstv_model=findViewById(R.id.item_tstv_model);
        ll_nointernet=findViewById(R.id.ll_nointernet);
        lv_shelflife=findViewById(R.id.today_lv_shelflife);
        tv_techname=findViewById(R.id.tv_techname);
        tv_model=findViewById(R.id.tv_model);
        taskProcessing=findViewById(R.id.taskProcessing);

        updatevalue();

    }

    public void updatevalue(){

        try {

            prefdetails = getSharedPreferences("details", 0);
            String details = prefdetails.getString("details", "");
            JSONObject object1 = new JSONObject(details);
            ticketno = object1.getString("TicketNo");
            calltype = object1.getString("CallType");
            address = object1.getString("Address");
            address=address.replace(","," ");
            refno=object1.getString("RefId");

            callbookdate=object1.getString("CallBookDate");
            tv_model.setText(object1.getString("Model"));
            closedate = object1.getString("ClosedDate");

            techname=object1.getString("TechName");

            status = object1.getString("Status");
            rcnno = object1.getString("RCNNo");
            String ServiceType = object1.getString("ServiceType");
            customer = object1.getString("CustomerName");


            tv_calltype.setText(calltype.substring(0,1));
            tv_ticketno.setText(ticketno);
            tv_address.setText(address.replace(","," "));
            tv_callbook.setText(callbookdate);
            tv_status.setText(object1.getString("Status"));
            tv_rcnno.setText(rcnno);
            tv_servicetype.setText(object1.getString("ServiceType"));
            tv_custname.setText(customer);
            tv_closedate.setText(object1.getString("ClosedDate"));
            item_tstv_model.setText(object1.getString("Product"));


            tv_techname.setText(techname);
            getpendingdata(ticketno);

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public void callcustomer(View view){

        if (!rcnno.equals("")){

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + rcnno));// Initiates the Intent
            startActivity(intent);
        }

    }

    public void getpendingdata(String ticketno){

        if ( CheckConnectivity.getInstance(this).isOnline()) {

            ll_nointernet.setVisibility(View.GONE);
            taskProcessing.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            //  String url= AllUrl.baseUrl+"spares/getspare?spareaccadd.Ticketno=2002374814&spareaccadd.Status=Pending&spareaccadd.ItemType=ZACC";
            String url=AllUrl.baseUrl+"spares/getspare?spareaccadd.Ticketno="+ticketno;

              System.out.println("get all sales-->  "+ url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                               System.out.println("Sales details-->"+response);

                            taskProcessing.setVisibility(View.GONE);

                            try {

                                JSONObject object=new JSONObject(response);

                                String status=object.getString("Status");
                                if (status.equals("true")){
                                    JSONArray jsonArray = object.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject essobj = jsonArray.getJSONObject(i);
                                        if (essobj.getString("ItemType").equals("ZSPP")) {

                                        }
                                        else {

                                            shelflifemodel=new Shelflifemodel();

                                            if (!essobj.getString("EssName").equals("")){
                                                shelflifemodel.setName(essobj.getString("EssName"));
                                            }
                                            else if (!essobj.getString("AccName").equals("")){

                                                shelflifemodel.setName(essobj.getString("AccName"));
                                            }
                                            shelflifemodel.setEsscode(essobj.getString("EssCode")+""+essobj.getString("AccCode"));
                                            shelflifemodel.setQty(essobj.getString("Qty"));
                                            shelflifemodel.setShelflife(currentdata);
                                            shelflifemodels.add(shelflifemodel);
                                        }
                                    }
                                    shelflifeAdapter = new ShelflifeAdapter(shelflifemodels, TodaysalesDetailsActivity.this);
                                    shelflifeAdapter.notifyDataSetChanged();
                                    lv_shelflife.setAdapter(shelflifeAdapter);
                                    if (shelflifemodels.size() == 0){
                                        Toast.makeText(TodaysalesDetailsActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{

                                    Toast.makeText(TodaysalesDetailsActivity.this, "No Data Found ", Toast.LENGTH_SHORT).show();

                                 }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    taskProcessing.setVisibility(View.GONE);
                }
            });

            queue.add(stringRequest);

        } else {

           ll_nointernet.setVisibility(View.VISIBLE);
        }

    }

    public void SendSms(View view){

        callsms(refno,"",rcnno);
    }

    public void callsms(String refno,String smstype,String Destination){
      //  https://crmapi.ifbhub.com/api/SMS/RepeatFeedbackSMS?model.feddbacksms.RefId=1402EC721EA01EEA94F93CD4CA0A6B7A&model.feddbacksms.SMSType=SOFCLS&model.feddbacksms.Destination=9330295590

        smstype="TOS";

        if (CheckConnectivity.getInstance(this).isOnline()) {

            taskProcessing.setVisibility(View.VISIBLE);

            RequestQueue queue = Volley.newRequestQueue(this);
            String url =AllUrl.baseUrl+"SMS/RepeatFeedbackSMS?model.feddbacksms.RefId="+refno+"&model.feddbacksms.SMSType="+smstype+"&model.feddbacksms.Destination=9330295590";

            System.out.println(url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            taskProcessing.setVisibility(View.GONE);


                            if (response.equals("")){

                            }

                            else {


                                try{

//                                    {
//                                        "RefId": "1402EC721EA01EEA8FB33F6A3A03D39C",
//                                            "SMSType": "TOS",
//                                            "Destination": "9330295590",
//                                            "Status": true
//                                    }

                                    JSONObject object=new JSONObject(response);

                                    String status=object.getString("Status");

                                    if (status.equals("true")){

                                        showsmspopup();
                                    }


                                }catch (Exception e){

                                    e.printStackTrace();
                                }
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    taskProcessing.setVisibility(View.GONE);

                }
            });

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);


        }

    }

    public void showsmspopup(){

        tv_sms_response.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(TodaysalesDetailsActivity.this, R.anim.slide_down);
        tv_sms_response.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_sms_response.setAnimation(null);
                tv_sms_response.setVisibility(View.GONE);
            }
        }, 5000);
    }
}
