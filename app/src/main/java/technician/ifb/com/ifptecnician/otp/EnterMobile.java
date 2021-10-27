package technician.ifb.com.ifptecnician.otp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import technician.ifb.com.ifptecnician.ForgotPassword;
import technician.ifb.com.ifptecnician.LoginActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;

public class EnterMobile extends AppCompatActivity {


    private static final String TAG = EnterMobile.class.getSimpleName();
    TextView btn_getotp;
    EditText et_mobile;
    String mobile="";
    ProgressBar progressBar;
    LinearLayout ll_forgot_passpassword,ll_nointernet;
    TextView tv_error;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Verify Your Mobile Number");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar=findViewById(R.id.taskProcessing);
        ll_nointernet=findViewById(R.id.ll_nointernet);
        tv_error=findViewById(R.id.tv_error_message);

        btn_getotp=findViewById(R.id.btn_getotp);
        et_mobile=findViewById(R.id.et_otp_mobile);
        btn_getotp.setClickable(false);

        type=getIntent().getExtras().getString("type");

        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                mobile=et_mobile.getText().toString();

                if (mobile.length() == 10) {

                    btn_getotp.setBackgroundResource(R.color.apptextcolor);
                    btn_getotp.setClickable(true);
                }
                else {
                    btn_getotp.setBackgroundResource(R.color.redcolor);
                    btn_getotp.setClickable(false);
                }
            }
        });

        btn_getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mobile.length() == 10){
                    getprofile();
                }

            }
        });
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

                                                Alert alert = new Alert();

                                                alert.showDialog(EnterMobile.this, "Error", "No valid account linked, Please Contact Administrator");
                                                ;

                                            } else {

                                                Getotp();

                                            }
                                        }
                                    }
                                    else {



                                        Alert alert = new Alert();
                                        alert.showDialog(EnterMobile.this, "Error", "Unable to Login Due to Multiple Account Detected. So Please contact your administrator");

                                    }



                                }
                                else {
                                    Alert alert=new Alert();
                                    // if user is imactive show alert message
                                    alert.showDialog(EnterMobile.this,"Error","No valid account linked, Please Contact Administrator");;

                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            //progressbar.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);

                        }
                    });

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {

            Toast.makeText(this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

                                 startActivity(new Intent(EnterMobile.this,EnterOtp.class)
                                         .putExtra("mobile",mobile)
                                          .putExtra("type",type));
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

                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {

                                  hideProgressDialog();
                              }
                          });
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
}