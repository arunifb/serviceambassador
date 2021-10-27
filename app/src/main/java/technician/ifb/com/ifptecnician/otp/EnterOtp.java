package technician.ifb.com.ifptecnician.otp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.ChangePassword;
import technician.ifb.com.ifptecnician.LoginActivity;
import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterOtp extends AppCompatActivity {

    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    EditText otp_edit_box1;
    TextView verify_otp_btn;
    String otp;
    String mobile;
    ProgressBar progressBar;
    LinearLayout ll_forgot_passpassword,ll_nointernet;
    TextView tv_error;
    TextView otp_mobile;
    TextView tv_resend;
    String PartnerId, FrCode, EmailId, Name, passsword, mobileno;
    Dbhelper dbhelper;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    String mobile_details;
    String type;
    Alert alert=new Alert();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Verification Code");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sessionManager = new SessionManager(getApplicationContext());
        otp_edit_box1=findViewById(R.id.otp_edit_box1);

        verify_otp_btn=findViewById(R.id.verify_otp_btn);

        otp_mobile=findViewById(R.id.otp_mobile);

        tv_resend=findViewById(R.id.tv_resend);

        progressBar=findViewById(R.id.taskProcessing);
        ll_nointernet=findViewById(R.id.ll_nointernet);
        tv_error=findViewById(R.id.tv_error_message);

        sharedPreferences= getSharedPreferences(AllUrl.MOBILE_DETAILS,0);
        mobile_details=sharedPreferences.getString("Mobile_details","");
        dbhelper = new Dbhelper(getApplicationContext());



        mobile=getIntent().getExtras().getString("mobile");
        type=getIntent().getExtras().getString("type");



        otp_mobile.setText("Registered Mobile +91 "+mobile);
        verify_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyotp();

            }
        });



        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Getotp();

            }
        });


        startSmsUserConsent();

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            //    Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             //   Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();

                showpop("Verification code received failed");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
           //     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                textViewMessage.setText(
//                        String.format("%s - %s", getString(R.string.received_message), message));

                getOtpFromMessage(message);
            }
        }
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message

        System.out.println("message"+message);
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
           // otpText.setText(matcher.group(0));
            otp_edit_box1.setText(matcher.group(0));
            verify_otp_btn.setBackgroundResource(R.color.apptextcolor);
            verify_otp_btn.setClickable(true);

        }
    }

    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }

                    @Override
                    public void onFailure() {

                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }


    public void verifyotp() {


        otp=otp_edit_box1.getText().toString();

        if (CheckConnectivity.getInstance(this).isOnline()) {

         //   https://apistage.ifbhub.com/api/user/otp-request/validate?model.user.Mobileno=9330295590&model.user.OTPCode=90531

            //login api url
            String url = "https://apistage.ifbhub.com/api/user/otp-request/validate?model.user.Mobileno="+mobile+"&model.user.OTPCode="+otp;

            // calling api via volley string request

            System.out.println(url);
            RequestQueue queue = Volley.newRequestQueue(this);
            //  progressBar.setVisibility(View.VISIBLE);

            showProgressDialog();
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            System.out.println("Login--->response"+response);

                            //  progressBar.setVisibility(View.GONE);
                            hideProgressDialog();


                            System.out.println("response--->"+response);

                            try {

                                // after getting josn response

                                JSONObject object = new JSONObject(response);

                                String status = object.getString("Status");

                                if (status.equals("true")) {

                                    if (type.equals("login")) {

                                        JSONArray jsonArray = new JSONArray(object.getString("Data"));

                                        if (jsonArray.length() == 1) {

                                            for (int i = 0; i < 1; i++) {


                                                JSONObject obj = jsonArray.getJSONObject(i);
                                                String IsActive = obj.getString("IsActive");

                                                if (IsActive.equals("True")) {

                                                    PartnerId = obj.getString("PartnerId");

                                                    // check the tech. partnerid

                                                    if (PartnerId.equals("")) {

                                                        //   errorDetails.Errorlog(LoginActivity.this, "Click%20On%Login", TAG, "PartnerId%20Is%20Null", "PartnerId%20Missing", username, "0000000000", "E", mobile_details, dbversion, tss);

                                                        // if parter id blank show a message
                                                        showpop("No valid account linked, Please Contact Administrator");

                                                    } else {
                                                        // user successfully login

                                                        FrCode = obj.getString("FrCode");
                                                        EmailId = obj.getString("EmailId");
                                                        Name = obj.getString("Name");
                                                        passsword = obj.getString("Password");
                                                        mobileno = obj.getString("Mobileno");
                                                        String url = obj.getString("URL");

                                                        String AadharNo=obj.getString("AadharNo");
                                                        String blood =obj.getString("BloodGroup");
                                                        String PANNo=obj.getString("PANNo");

                                                        // errorDetails.Errorlog(LoginActivity.this, "Login%20Success", TAG, "Login%20Success", "Login%20Success", mobileno, "0000000000", "LOGIN", mobile_details, dbversion, tss);
                                                        // crete user login session
                                                        sessionManager.createLoginSession(PartnerId, FrCode, EmailId, Name, passsword, mobileno, url,obj.toString(),"",PANNo,AadharNo,blood);

                                                        // geting all essential
                                                        if (passsword.equals("IFB@2019")) {

                                                            ceratealert();
                                                        } else {

                                                            getess(FrCode);
                                                        }

                                                    }
                                                } else {

                                                    // if user is imactive show alert message
                                                    alert.showDialog(EnterOtp.this, "Error", "Your account has been deactivated.So Please contact your administrator");
                                                    ;

                                                }
                                            }
                                        } else {
                                            // if multiple accounts found with same mobile no
                                            alert.showDialog(EnterOtp.this, "Error", "Unable to Login Due to Multiple Account Detected. So Please contact your administrator");
                                            ;

                                        }
                                    }
                                    else {

                                        startActivity(new Intent(EnterOtp.this,ChangePassword.class));
                                    }

                                }

                                else {

                                    // if user enter wrong user id and password
                                    // Toast.makeText(LoginActivity.this, "Please Enter Valid Mobileno And Password or Technician Inactive", Toast.LENGTH_SHORT).show();
                                    showpop("Please Enter Valid Verification Code ");

                                }

                            }catch (Exception e){

                                e.printStackTrace();

                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressBar.setVisibility(View.GONE);
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

        }
        else {
            // if no internet found open no internet layout
            ll_nointernet.setVisibility(View.VISIBLE);

        }
    }

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public void showpop(String text){

        tv_error.setText(text);
        tv_error.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(EnterOtp.this, R.anim.slide_down);
        tv_error.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error.setAnimation(null);
                tv_error.setVisibility(View.GONE);
            }
        }, 5000);
    }



    public void Getotp() {

        if (CheckConnectivity.getInstance(this).isOnline()) {


            //login api url
            String url = "https://apistage.ifbhub.com/api/user/otp-request?model.user.Mobileno="+mobile;

            // calling api via volley string request

            RequestQueue queue = Volley.newRequestQueue(this);
            //  progressBar.setVisibility(View.VISIBLE);

            showProgressDialog();
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            System.out.println("Login--->response"+response);

                            //  progressBar.setVisibility(View.GONE);
                            hideProgressDialog();


                            System.out.println("response--->"+response);

                            try{


                                String res=response.replaceAll("^\"|\"$", "");
                                if (res.equals("Success")){

//                                    startActivity(new Intent(EnterMobile.this,EnterOtp.class)
//                                            .putExtra("mobile",mobile));
//                                    finish();

                                    showpop("OTP sent successfully ");
                                }

                            }catch (Exception e){

                                e.printStackTrace();

                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressBar.setVisibility(View.GONE);
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

        }
        else {
            // if no internet found open no internet layout
            ll_nointernet.setVisibility(View.VISIBLE);

        }
    }

    public void ceratealert(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This is auto generated password , Please change your password");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(EnterOtp.this, ChangePassword.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void getess(String FrCode) {

        if (CheckConnectivity.getInstance(this).isOnline()) {
            ll_nointernet.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = AllUrl.baseUrl + "addacc?addacc.FrCode=" + FrCode;

            System.out.println("get all sales-->  " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            System.out.println("ess details-->" + response);

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                System.out.println(jsonArray.toString());

                                dbhelper.deleteess();
                                dbhelper.insert_ess_data("-- select essential --", "", "" , "0", "0", "", "");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject essobj = jsonArray.getJSONObject(i);

                                    if (Boolean.valueOf(dbhelper.insert_ess_data(essobj.getString("ComponentDescription"), essobj.getString("Component"), "Z" + essobj.getString("ItemType"), essobj.getString("accessories_stock"), essobj.getString("additives_stock"),essobj.getString("ShelfLife"), "")).booleanValue()) {
                                        System.out.println("offline data save" + i);
                                    }
                                }

                                startActivity(new Intent(EnterOtp.this, MainActivity.class));
                                finish();


                            } catch (Exception e) {
                                e.printStackTrace();
                              //  errorDetails.Errorlog(LoginActivity.this,"Login%api", TAG,"exception",url,mobileno,"0000000000","L",mobile_details,dbversion,tss);
                                startActivity(new Intent(EnterOtp.this, MainActivity.class));
                                finish();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    startActivity(new Intent(EnterOtp.this, MainActivity.class));
                    finish();
                    //errorDetails.Errorlog(LoginActivity.this,"Login%api", TAG,"Essential%20List%20Not%20Found",url,mobileno,"0000000000","L",mobile_details,dbversion,tss);
                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {

            ll_nointernet.setVisibility(View.VISIBLE);
        }
    }


}