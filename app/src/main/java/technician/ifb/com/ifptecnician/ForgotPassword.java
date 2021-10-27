package technician.ifb.com.ifptecnician;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.utility.SimInfo;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{

    EditText et_mobileno;
    Button btn_forgot_password;
    LinearLayout ll_main;
    String mobile;
    ProgressBar progressBar;
   TextView tv_popup;
    private static final String TAG = ForgotPassword.class.getSimpleName();
    Alert alert=new Alert();
    LinearLayout ll_sms_true,ll_sms_false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();

    }

    public void init(){

        et_mobileno=findViewById(R.id.et_mobileno);
        btn_forgot_password=findViewById(R.id.btn_forgot_password);
        btn_forgot_password.setEnabled(false);
        ll_main=findViewById(R.id.ll_main);
        ll_main.setOnClickListener(this);
        progressBar=findViewById(R.id.taskProcessing);
        tv_popup=findViewById(R.id.tv_error_message);
        ll_sms_true=findViewById(R.id.layout_sms_true);
        ll_sms_false=findViewById(R.id.layout_sms_false);
        et_mobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (et_mobileno.getText().toString().length() == 10) {

                    btn_forgot_password.setEnabled(true);
                    btn_forgot_password.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_shape));
                    btn_forgot_password.setOnClickListener(ForgotPassword.this);
                    mobile=et_mobileno.getText().toString();
                    hideSoftKeyboard(ForgotPassword.this);


                }
                else {
                    btn_forgot_password.setEnabled(false);
                    btn_forgot_password.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_disable));
                    btn_forgot_password.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        et_mobileno.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    if (et_mobileno.getText().toString().length() == 10) {
                        mobile=et_mobileno.getText().toString();
                        hideSoftKeyboard(ForgotPassword.this);
                    }

                    handled = true;
                }
                return handled;
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id. btn_forgot_password:
                checkblank();
                break;
            case R.id.ll_main:
                hideSoftKeyboard(ForgotPassword.this);
                break;
        }
    }


    public void checkblank(){

        if (mobile.length() == 10){
            getprofile();

        }
    }


    public void getprofile(){

        if (CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = AllUrl.baseUrl+"sa-profile/getProfile/?user.Mobileno=" + mobile;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            try {

                                JSONObject jsonObject=new JSONObject(response);

                                System.out.println("Response-->"+response);


                                String status=jsonObject.getString("Status");

                                if (status.equals("true")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    if (jsonArray.length() == 1) {

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject obj = jsonArray.getJSONObject(i);

                                            String PartnerId = obj.getString("PartnerId");
                                            if (PartnerId.equals("")) {

                                                alert.showDialog(ForgotPassword.this, "Error", "No valid account linked, Please Contact Administrator");


                                            } else {

                                                callsms("", "", mobile);
                                                //  Toast.makeText(ForgotPassword.this, "Valid user", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }

                                    else {

                                        alert.showDialog(ForgotPassword.this, "Error", "Unable to Login Due to Multiple Account Detected. So Please contact your administrator");

                                    }

                                }
                                else {

                                        // if user is imactive show alert message
                                        alert.showDialog(ForgotPassword.this,"Error","No valid account linked, Please Contact Administrator");;
                                }



                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //progressbar.setVisibility(View.GONE);
                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {

            Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    public static void hideSoftKeyboard(Activity activity) {

        try {

            if (activity != null) {
                InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void callsms(String refno,String smstype,String Destination){


        smstype="FOG";
//        Destination="9674699648";
        refno="1402EC721EA01EEA94F93CD4CA0A6B7A";

        if (CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url =AllUrl.baseUrl+"SMS/RepeatFeedbackSMS?model.feddbacksms.RefId="+refno+"&model.feddbacksms.SMSType="+smstype+"&model.feddbacksms.Destination="+Destination+"";

            System.out.println(url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);



                            System.out.println("response-->   "+response);


                            if (response.equals("")){


                                showsmsalert("false");
                            }


                            try{

                                JSONObject object=new JSONObject(response);

                                String status=object.getString("Status");

                                if (status.equals("true")){

                                    showsmsalert("true");

                                }

                                else {

//                                    showsmspopup("SMS Failed.Plesae try after some time");

                                    showsmsalert("false");
                                }

                            }catch (Exception e){

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    showsmsalert("false");


                }
            });

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }

        else {

            showsmspopup("No internet connection");
        }

    }



    public void showsmspopup(String message){

        tv_popup.setText(message);

        tv_popup.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(ForgotPassword.this, R.anim.slide_down);
        tv_popup.startAnimation(animation);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_popup.setAnimation(null);
                tv_popup.setVisibility(View.GONE);
            }
        }, 5000);
    }

    public void showsmsalert(String status){

        if (status.equals("true")){

            ll_sms_true.setVisibility(View.VISIBLE);
            ll_sms_false.setVisibility(View.GONE);

            Animation animation;
            animation = AnimationUtils.loadAnimation(ForgotPassword.this, R.anim.slide_down);
            ll_sms_true.startAnimation(animation);


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    ll_sms_true.setAnimation(null);
                    ll_sms_true.setVisibility(View.GONE);
                }
            }, 5000);

        }
        else {

            ll_sms_true.setVisibility(View.GONE);
            ll_sms_false.setVisibility(View.VISIBLE);
            Animation animation;
            animation = AnimationUtils.loadAnimation(ForgotPassword.this, R.anim.slide_down);
            ll_sms_false.startAnimation(animation);


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    ll_sms_false.setAnimation(null);
                    ll_sms_false.setVisibility(View.GONE);
                }
            }, 5000);
        }
    }
}
