package technician.ifb.com.ifptecnician;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.text.Html;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;

import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.language.LocaleHelper;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;


public class SplashScreen extends AppCompatActivity {

    private static final String TAG = SplashScreen.class.getSimpleName();

    private BroadcastReceiver mReceivedLocation;
    Dbhelper dbhelper;
    ProgressBar progressBar;
    private static final int REQUEST= 100;
    SessionManager sessionManager;
//    SharedPreferences preferences;
    Context mContext = this;
    TextView tv_version;
    String name,version,downloadurl;
    String password;
    String mobile_no,PartnerId;
    String  details;
//    Alert alert=new Alert();
    String versionname="";
   // InternetSpeed speed;
    Context context;
    Resources resources;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        dbhelper=new Dbhelper(getApplicationContext());
        sessionManager=new SessionManager(getApplicationContext());

        progressBar=findViewById(R.id.taskProcessing);

        context = LocaleHelper.setLocale(SplashScreen.this, "hi");
        resources = context.getResources();
        versionname=AllUrl.APP_Version;

        init();

        if (Build.VERSION.SDK_INT >= 23) {

            String[] PERMISSIONS = {Manifest.permission.CAMERA,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.NFC,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.WRITE_CONTACTS,
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission. RECEIVE_SMS,
                    Manifest.permission.INSTALL_PACKAGES,
                    Manifest.permission.CALL_PHONE

            };


            if (!hasPermissions(mContext, PERMISSIONS)) {

                ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );

            } else {

                appversion();

            }
        }
        else {

           appversion();

        }

    }

    protected void onDestroy() {
        if (this.mReceivedLocation != null) {
            unregisterReceiver(this.mReceivedLocation);
            this.mReceivedLocation = null;
        }
        super.onDestroy();
    }

    @SuppressLint({"InlinedApi"})
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), "location_mode");
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (locationMode != 0) {
            return true;

        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                appversion();


            } else {

                if (Build.VERSION.SDK_INT <= 22) {
                    appversion();
                }

                Toast.makeText(mContext, "PERMISSIONS Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

//    ----------------------- back press  ---------------------------------------
    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("ⓘ Exit ! " + getString(R.string.app_name));
        alertDialogBuilder
                .setMessage(Html.fromHtml("<p style='text-align:center;'></p><h3 style='text-align:center;'>Click Yes to Exit !</h4>"))
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);

                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void init()
    {
        tv_version=findViewById(R.id.tv_version);

    }

//    -------------------------- check for update apk ----------------------

    public void appversion() {

//
//        try{
//
//        details =Build.VERSION.RELEASE
//                +"MODEL:"+Build.MODEL;
//        System.out.println("Device Details"+details);
//       // SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences sharedPreferences=getSharedPreferences(AllUrl.MOBILE_DETAILS,0);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//
//        editor.putString("Mobile_details",details);
//        editor.apply();
//
//
//        }catch (Exception e){
//
//            e.printStackTrace();
//        }



        if (CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
           // String url =AllUrl.baseUrl+"appVersion/getVersion";

            String url=AllUrl.baseUrl+"appVersion/getVersiondevt";

            System.out.println("url--->"+url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // if response is null
                            if (response.equals("")){
                                checksession();
                            }

                            progressBar.setVisibility(View.GONE);

                            System.out.println("Splash screen -->"+ response);

                            try{

                                //  respose

                                JSONObject object=new JSONObject(response);

                                String status=object.getString("Status");
                                if (status.equals("true")){

                                   JSONArray jsonArray=new JSONArray(object.getString("Data"));

                                    for (int i=0;i<jsonArray.length();i++) {

                                        JSONObject obj = jsonArray.getJSONObject(i);

                                        name=obj.getString("Name");
                                        version=obj.getString("Version");
                                        downloadurl=obj.getString("ApiUrl");
                                     //   String dbversion=dbhelper.getappversion();

                                     //   System.out.println("dbversion"+dbversion);

                                        if (version.equals(versionname)){
                                            checksession();

                                        }

                                        // if db app version is null
                                        else if (versionname.equals("")){

                                            checksession();
                                        }
                                        // if db app version and server app version not same

                                        else {

                                            // go to download page and download
                                            startActivity(new Intent(SplashScreen.this,CheckApk.class)
                                                     .putExtra("name",name)
                                                     .putExtra("version",version)
                                                     .putExtra("downloadurl",downloadurl));
                                        }
                                    }

                                }

                                else {

                                    checksession();

                                }

                            }catch (Exception e){

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressBar.setVisibility(View.GONE);
                            checksession();
                        }
                    });

                }
            });


            int socketTimeout = 5000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);


        }
        else {

            // if no internet
            checksession();
        }
    }

    // ----------------------------- check user is login or not -----------------------------------
    public void checksession(){

              // if user is login
               if (sessionManager.isLoggedIn())
               {

                   try{

                       // ----------------------  get all session value  from sessionManager -------------------------

                       HashMap<String, String> user = sessionManager.getUserDetails();
                       password=user.get(SessionManager.KEY_passsword);
                       String url=user.get(SessionManager.KEY_PROFILE_URL);
                       String Aadhar=user.get(SessionManager.KEY_AadharNo);
                       String blood=user.get(SessionManager.KEY_BloodGroup);



                       if (password.equals("IFB@2019")){

                           //startActivity(new Intent(SplashScreen.this,ChangePassword.class));
                         //  ceratealert();

                           startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                           overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                           finish();
                       }
                       else if (url.length() == 0){

                           startActivity(new Intent(SplashScreen.this,Profile.class));
                           overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                           finish();
                       }
                       else if(Aadhar.equals("")){

                           startActivity(new Intent(SplashScreen.this,Profile.class));
                           overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                           finish();
                       }
                       else if (blood.length() == 0){

                           startActivity(new Intent(SplashScreen.this,Profile.class));
                           overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                           finish();
                       }
                       else {

                           UsersLogin();

                       }

                   }catch(Exception e){

                       e.printStackTrace();
                   }

               }
               // if user login status false
               else{
                   startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                   overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                   finish();
               }
    }

    public void ceratealert(){

        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Reset Your Password");
        alertDialogBuilder.setMessage("This is auto generated password , Please change your password");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        startActivity(new Intent(SplashScreen.this,ChangePassword.class));
                    }
                });

//        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });

        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void UsersLogin() {


        sessionManager=new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();

        mobile_no=user.get(SessionManager.KEY_mobileno);


        if (CheckConnectivity.getInstance(this).isOnline()) {

            long imagename = System.currentTimeMillis();

            //login api url
            String url = AllUrl.baseUrl+"user/getvaliduser?" +
                    "model.user.mobileno="+mobile_no+"" +
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

                                    if (jsonArray.length() == 1) {

                                        for (int i = 0; i < 1; i++) {


                                            JSONObject obj = jsonArray.getJSONObject(i);
                                            String IsActive=obj.getString("IsActive");

                                            if (IsActive.equals("True")) {

                                                PartnerId = obj.getString("PartnerId");

                                                // check the tech. partnerid

                                                if (PartnerId.equals("")) {


                                                    Alert alert=new Alert();


                                                    alert.showDialog(SplashScreen.this,"Error","No valid account linked, Please Contact Administrator");;


                                                    // if parter id blank show a message
                                                  //  showpop("No valid account linked, Please Contact Administrator");

                                                } else {
                                                    // user successfully login

                                                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                                                    finish();

                                                }
                                            }

                                            else {

                                                // if user is imactive show alert message
                                                Alert alert=new Alert();
                                                alert.showDialog(SplashScreen.this,"Error","Your account has been deactivated.So Please contact your administrator");;

                                            }
                                        }
                                    }
                                    else {
                                        Alert alert=new Alert();
                                        // if multiple accounts found with same mobile no
                                        alert.showDialog(SplashScreen.this,"Error","Unable to Login Due to Multiple Account Detected. So Please contact your administrator");;

                                    }
                                }
                                else {

                                    // if user enter wrong user id and password
                                    startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                                    overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                                    finish();

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
                            startActivity(new Intent(SplashScreen.this,MainActivity.class));
                            overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                            finish();

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
            // if no internet found open no internet layout

                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                finish();

        }
    }

}

