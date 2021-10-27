package technician.ifb.com.ifptecnician;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.otp.EnterMobile;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.SimInfo;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    ErrorDetails errorDetails=new ErrorDetails();
    private EditText musername;
    SessionManager sessionManager;
    SharedPreferences sharedPreferences;
    private EditText mPasswordView;
    String username, password;
    String PartnerId, FrCode, EmailId, Name, passsword, mobileno;
    Dbhelper dbhelper;
    LinearLayout ll_login;
    LinearLayout ll_forgot_passpassword,ll_nointernet;
    ProgressBar progressBar;
    Alert alert=new Alert();
    TextView tv_error;
    String tss ;
    String mobile_details="";
    ArrayList<SimInfo> simInfos=new ArrayList<>();
    final ArrayList<Integer> simCardList = new ArrayList<>();
    int totalsim;
    String dbversion="";
    String [] blood_array={"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    String blood="",aadhar="",driving="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(getApplicationContext());
        sharedPreferences= getSharedPreferences(AllUrl.MOBILE_DETAILS,0);
       // mobile_details=sharedPreferences.getString("Mobile_details","");

        System.out.println("mobile_details--->"+mobile_details);
        dbhelper = new Dbhelper(getApplicationContext());


        dbversion=AllUrl.APP_Version;

        musername =  findViewById(R.id.username);
        mPasswordView =  findViewById(R.id.password);
        ll_forgot_passpassword= findViewById(R.id.ll_forgot_passpassword);
        progressBar=findViewById(R.id.taskProcessing);
        ll_nointernet=findViewById(R.id.ll_nointernet);
        tv_error=findViewById(R.id.tv_error_message);
        // open no network layout

        if (CheckConnectivity.getInstance(this).isOnline()) {

            ll_nointernet.setVisibility(View.GONE);
        }
        else {

            ll_nointernet.setVisibility(View.VISIBLE);
        }
      // get current date
        tss= Valuefilter.getdate();

        // click on forgot password
        ll_forgot_passpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));

            }
        });

        ll_login = findViewById(R.id.ll_login);



        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(LoginActivity.this);
            }
        });
        musername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (musername.getText().toString().length() == 10) {

                    mPasswordView.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // click on login button
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check blank value
                checkblank();

            }
        });


    }


    // check all blank value

    public void checkblank() {
        
        username = musername.getText().toString();
        password = mPasswordView.getText().toString();

        if (username.length() != 10) {

            showpop("Mobile No is invalid!!");

        } else if (password.length() == 0) {

            showpop("Please enter Password");

        } else {

            UsersLogin();

        }
    }

    // --------------------- close keyboard ------------------------------------------

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


    // ------------------------ After login get essential ------------------------------------

    public void getess(String FrCode) {

        if (CheckConnectivity.getInstance(this).isOnline()) {
            ll_nointernet.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = AllUrl.baseUrl + "addacc?addacc.FrCode=" + FrCode;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);



                            try {
                                JSONArray jsonArray = new JSONArray(response);


                                dbhelper.deleteess();
                                dbhelper.insert_ess_data("-- select essential --", "", "" , "0", "0", "", "");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject essobj = jsonArray.getJSONObject(i);


                                    if(essobj.getString("accessories_stock").equals("0") && essobj.getString("additives_stock").equals("0")){

                                    }
                                    else {

                                        if (Boolean.valueOf(dbhelper.insert_ess_data(essobj.getString("ComponentDescription"), essobj.getString("Component"), "Z" + essobj.getString("ItemType"), essobj.getString("accessories_stock"), essobj.getString("additives_stock"), essobj.getString("ShelfLife"), "")).booleanValue()) {

                                        }
                                    }

                                }

                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();


                            } catch (Exception e) {
                                e.printStackTrace();
                               // errorDetails.Errorlog(LoginActivity.this,"Login%api", TAG,"exception",url,mobileno,"0000000000","L",mobile_details,dbversion,tss);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    errorDetails.Errorlog(LoginActivity.this,"Login%api", TAG,"Essential%20List%20Not%20Found",url,mobileno,"0000000000","L",mobile_details,dbversion,tss);
                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {

            ll_nointernet.setVisibility(View.VISIBLE);
        }
    }


    //  -------------------------- Check the user is valid or not --------------------------------

    public void UsersLogin() {


        if (CheckConnectivity.getInstance(this).isOnline()) {

            long imagename = System.currentTimeMillis();

            //login api url
            String url = AllUrl.baseUrl+"user/getvaliduser?" +
                    "model.user.mobileno="+username+"" +
                    "&model.user.password="+password+"";

            // calling api via volley string request


            System.out.println("url  --- >" +url);

            RequestQueue queue = Volley.newRequestQueue(this);
            progressBar.setVisibility(View.VISIBLE);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            System.out.println("Login--->response"+response);

                            progressBar.setVisibility(View.GONE);

                            try{

                                // after getting josn response

                                JSONObject object=new JSONObject(response);

                                String status=object.getString("Status");

                                if (status.equals("true")){

                                    JSONArray jsonArray=new JSONArray(object.getString("Data"));


                                    System.out.println(jsonArray.length());
                                    if (jsonArray.length() == 1) {

                                        for (int i = 0; i < 1; i++) {


                                            JSONObject obj = jsonArray.getJSONObject(i);
                                            String IsActive=obj.getString("IsActive");

                                            if (IsActive.equals("True")) {

                                                PartnerId = obj.getString("PartnerId");

                                                if (PartnerId.equals("")) {

                                                    errorDetails.Errorlog(LoginActivity.this, "Click%20On%Login", TAG, "PartnerId%20Is%20Null", "PartnerId%20Missing", username, "0000000000", "E", mobile_details, dbversion, tss);

                                                   showpop("No valid account linked, Please Contact Administrator");

                                                } else {
                                                    passsword = obj.getString("Password");
                                                    String AadharNo=obj.getString("AadharNo");
                                                    String purl = obj.getString("URL");
                                                    String blood =obj.getString("BloodGroup");
                                                    String PANNo=obj.getString("PANNo");
                                                    String RegionCode =obj.getString("RegionCode");

                                                    FrCode = obj.getString("FrCode");
                                                    EmailId = obj.getString("EmailId");
                                                    Name = obj.getString("Name");
                                                    passsword = obj.getString("Password");
                                                    mobileno = obj.getString("Mobileno");

                                                    errorDetails.Errorlog(LoginActivity.this, "Login%20Success", TAG, "Login%20Success", "Login%20Success", mobileno, "0000000000", "L", mobile_details, dbversion, tss);
                                                    // crete user login session
                                                    sessionManager.createLoginSession(PartnerId, FrCode, EmailId, Name, passsword, mobileno, purl,obj.toString(),RegionCode,PANNo,AadharNo,blood);

                                                    // geting all essential
                                                    if (passsword.equals("IFB@2019")){

                                                        ceratealert();
                                                    }
                                                    else if (purl.length() == 0){

                                                        startActivity(new Intent(LoginActivity.this,Profile.class));
                                                    }


                                                    else if(AadharNo.length()==0){

                                                        startActivity(new Intent(LoginActivity.this,Profile.class));

                                                      }

                                                    else if (blood.length() == 0){

                                                        startActivity(new Intent(LoginActivity.this,Profile.class));
                                                    }


                                                    else {

                                                    getess(FrCode);

                                                    }

                                                }
                                            }

                                            else {

                                             // if user is imactive show alert message
                                                alert.showDialog(LoginActivity.this,"Error","Your account has been deactivated.So Please contact your administrator");;

                                            }
                                        }
                                    }
                                    else {
                                      // if multiple accounts found with same mobile no
                                        alert.showDialog(LoginActivity.this,"Error","Unable to Login Due to Multiple Account Detected. So Please contact your administrator");;

                                    }
                                }
                                else {

                                    // if user enter wrong user id and password
                                   // Toast.makeText(LoginActivity.this, "Please Enter Valid Mobileno And Password or Technician Inactive", Toast.LENGTH_SHORT).show();
                                    showpop("Please Enter Valid Mobileno And Password ");

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


                            System.out.println(error);


                            if (error instanceof NetworkError) {

                                showpop("NetworkError ,Please try after some times. ");

                            } else if (error instanceof ServerError) {

                                showpop("ServerError ,Please try after some times.");

                            } else if (error instanceof AuthFailureError) {

                                showpop("AuthFailureError ,Please try after some times.");

                            } else if (error instanceof ParseError) {

                                showpop("ParseError ,Please try after some times.");

                            } else if (error instanceof TimeoutError) {

                                showpop("Time out error ,Please try after some times.");
                            }

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
            // if no internet found open no internet layout
          ll_nointernet.setVisibility(View.VISIBLE);

        }
    }

    // for showing all messagein top

    public void showpop(String text){

        tv_error.setText(text);
        tv_error.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_down);
        tv_error.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error.setAnimation(null);
                tv_error.setVisibility(View.GONE);
            }
        }, 5000);
    }

    public void  opensimpopup(){

        final Dialog Simdialog = new Dialog(LoginActivity.this);

        Simdialog.setContentView(R.layout.popupforselectsim);

        LinearLayout ll_sim_one,ll_sim_2;

        TextView popup_tv_simone_operator,popup_tv_simtwo_operator;

        popup_tv_simone_operator=Simdialog.findViewById(R.id.popup_tv_simone_operator);

        popup_tv_simtwo_operator=Simdialog.findViewById(R.id.popup_tv_simtwo_operator);


        //  System.out.println(simInfos.size());

        for (int s=0;s<simInfos.size();s++){

            if (s==0){
                popup_tv_simone_operator.setText(simInfos.get(s).getDisplay_name());
            }
            else if(s==1){

                popup_tv_simtwo_operator.setText(simInfos.get(s).getDisplay_name());
            }
        }

        ll_sim_one=Simdialog.findViewById(R.id.popup_ll_simone);
        ll_sim_2=Simdialog.findViewById(R.id.popup_ll_simtwo);

        ll_sim_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Simdialog.dismiss();
                // send_sms_from_sim_one();

                int smsToSendFrom = simCardList.get(0);
                send_sms_by_sub_id(smsToSendFrom);

            }
        });
        ll_sim_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Simdialog.dismiss();
                int smsToSendFrom = simCardList.get(1);
                send_sms_by_sub_id(smsToSendFrom);


            }
        });

        Simdialog.show();
    }

    @SuppressLint("MissingPermission")
    public void Total_sim_details(){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            try{

                TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                totalsim=manager.getPhoneCount();

              //  System.out.println(totalsim);
                if(totalsim==2){


                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                        try{


                            SubscriptionManager subscriptionManager;
                            subscriptionManager = SubscriptionManager.from(LoginActivity.this);

                            final List<SubscriptionInfo> subscriptionInfoList = subscriptionManager
                                    .getActiveSubscriptionInfoList();
                            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {

                                int subscriptionId = subscriptionInfo.getSubscriptionId();
                                simCardList.add(subscriptionId);
                                simInfos.add(new SimInfo(subscriptionId, subscriptionInfo.getCarrierName().toString(),subscriptionInfo.getNumber(), subscriptionInfo.getSimSlotIndex()));


                            }
                        }catch (Exception e){

                            e.printStackTrace();
                        }

                    }

                }
                else{

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void send_sms_by_sub_id(int res){

        String DELIVERED = "SMS_DELIVERED";

        String lastFiveDigits = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            try {
                String SENT = "SMS_SENT";
                PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
                PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 1, new Intent(DELIVERED), 0);

                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        unregisterReceiver(this);

                        int resultCode = getResultCode();
                        switch (resultCode) {
                            case Activity.RESULT_OK:
//                                Toast.makeText(getBaseContext(), "SMS Sent Successfully", Toast.LENGTH_LONG).show();


                                break;

                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:

                                Toast.makeText(getBaseContext(), "GENERIC_FAILURE",
                                        Toast.LENGTH_SHORT).show();

                                break;

                            case SmsManager.RESULT_ERROR_NO_SERVICE:

                                Toast.makeText(getBaseContext(), "NO_SERVICE",
                                        Toast.LENGTH_SHORT).show();

                                break;

                            case SmsManager.RESULT_ERROR_NULL_PDU:

                                Toast.makeText(getBaseContext(), "NULL_PDU",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case SmsManager.RESULT_ERROR_RADIO_OFF:

                                Toast.makeText(getBaseContext(), "NULL_PDU",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            default:

                                break;

                        }

                    }
                }, new IntentFilter(SENT));



                //---when the SMS has been delivered---
                registerReceiver(new BroadcastReceiver(){
                    @Override
                    public void onReceive(Context arg0, Intent arg1) {
                        switch (getResultCode())
                        {
                            case Activity.RESULT_OK:
                                Toast.makeText(getBaseContext(), "SMS delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case Activity.RESULT_CANCELED:
                                Toast.makeText(getBaseContext(), "SMS not delivered",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new IntentFilter(DELIVERED));
                //assign your desired sim to send sms, or user selected choice
                SmsManager.getSmsManagerForSubscriptionId(res)
                        .sendTextMessage("6296783789", null, "call korbe " + lastFiveDigits, sentIntent, deliveredIntent);

            }catch (Exception e){

                e.printStackTrace();

            }
        }
    }

    public void gotomobile(View view){

        startActivity(new Intent(LoginActivity.this,EnterMobile.class)
                .putExtra("type","login"));
    }

    public void ceratealert(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Reset Your Password");
        alertDialogBuilder.setMessage("This is auto generated password , Please change your password");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                               startActivity(new Intent(LoginActivity.this,ChangePassword.class));
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void createaadharDialog(){

        final Dialog profile_dialog = new Dialog(LoginActivity.this);
        profile_dialog.setContentView(R.layout.dialog_update_profile);
       // image_dialog.setTitle("Update Profile");
        TextView tv_error;
        EditText et_aadhar,et_driving;
        Spinner spnr_blood;
        Button btn_update;


        et_aadhar=profile_dialog.findViewById(R.id.et_aadhar);
        et_driving=profile_dialog.findViewById(R.id.et_driving);
        spnr_blood=profile_dialog.findViewById(R.id.spnr_blood_group);
        btn_update=profile_dialog.findViewById(R.id.btn_update);
        tv_error=profile_dialog.findViewById(R.id.tv_error);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, blood_array);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_blood.setAdapter(aa);

        spnr_blood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

             blood =  blood_array[i].toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                aadhar=et_aadhar.getText().toString();
                driving=et_driving.getText().toString();

                if (aadhar.length() !=12){

                    tv_error.setText("Please enetr aadhar details");
                }
                else if (driving.length()==0){

                    tv_error.setText("Please enetr driving Licence ");
                }
                else if (blood.length()==0){

                    tv_error.setText("Please Select Blood Group ");

                }
                else {

                    updatedetails(driving,blood,aadhar);
                }
            }
        });

        profile_dialog.show();
    }


    public void updatedetails(String pan,String blood,String aadhar) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            String url = AllUrl.baseUrl+"profile/partner/?PartnerId="+PartnerId+"&PANNo="+pan+"&BloodGroup="+blood+"&AadharNo="+aadhar;

            System.out.println("get all sales-->  " + url);

            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            System.out.println("post--->response" + response);

                            try{

                                String res=response.replaceAll("^\"|\"$", "");

                                System.out.println("res-->"+res);
                                if (res.equals("Data updated successfully")){


                                }
                                else {


                                }
                            }catch (Exception e){

                            }


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


        } else {

            Toast.makeText(this, "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

