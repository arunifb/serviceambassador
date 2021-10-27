package technician.ifb.com.ifptecnician;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.PasswordValidator;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener{

    EditText new_password,confirm_password,old_password;
    Button btn_change_password;
    String new_pass,confirm_pass,old_pass;
    SessionManager sessionManager;
    String PartnerId,oldpass,mobile_no;
    TextView tv_oldpass_error;
    TextView tv_confirm_error,tv_newpass_error;
    boolean oldstatus,newstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sessionManager= new SessionManager(getApplicationContext());


        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            oldpass=user.get(SessionManager.KEY_passsword);
            mobile_no=user.get(SessionManager.KEY_mobileno);

        }
        catch (Exception e){

            e.printStackTrace();
        }

        init();


    }

    public void init(){

        new_password=findViewById(R.id.new_password);
        confirm_password=findViewById(R.id.confirm_password);
        btn_change_password=findViewById(R.id.btn_change_password);
        btn_change_password.setOnClickListener(this);
        old_password=findViewById(R.id.old_password);
        tv_oldpass_error=findViewById(R.id.tv_oldpass_error);
        tv_confirm_error=findViewById(R.id.tv_confirm_error);
        tv_newpass_error=findViewById(R.id.tv_newpass_error);
        btn_change_password.setEnabled(false);
        btn_change_password.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_disable));


        old_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                old_pass=old_password.getText().toString();

                if (!old_pass.equals(oldpass)){

                    tv_oldpass_error.setText("Please enter Old Password");
                    tv_oldpass_error.setTextColor(Color.RED);
                }
                else {
                    tv_oldpass_error.setText("");
                    checkblank();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                new_pass=new_password.getText().toString();
                PasswordValidator validator=new PasswordValidator();
                boolean sta= validator.validate(new_pass);
                System.out.println(sta);

                if (sta){
                    tv_newpass_error.setText("");
                }
                else {

                    tv_newpass_error.setTextColor(Color.RED);
                    tv_newpass_error.setText("Use 8 or more characters with a mix of 1 capital letters,1 numbers & 1 symbols");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                confirm_pass=confirm_password.getText().toString();

                    if (new_pass.equals(confirm_pass)) {

                        checkblank();
                        tv_confirm_error.setText("");

                    } else {

                        tv_confirm_error.setText("Those passwords did't match. Try again");
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_change_password:
                Changepassword();
                break;
        }

    }

    public void checkblank(){

        if (old_password.getText().length() == 0){
            tv_oldpass_error.setText("Please Enter old password");

            tv_oldpass_error.setTextColor(Color.RED);
            Toast.makeText(this, "Please Enter old password", Toast.LENGTH_SHORT).show();
        }
        else if (new_password.getText().length() == 0){

            tv_newpass_error.setText("Please Enter New Password");
            tv_newpass_error.setTextColor(Color.RED);
        }

        else if (confirm_password.getText().length() == 0){

            tv_confirm_error.setText("Please Enter Confirm Password");
            tv_confirm_error.setTextColor(Color.RED);
        }
        else if (!confirm_pass.equals(new_pass)){

            tv_newpass_error.setText("Please Enter New Password");
            tv_newpass_error.setTextColor(Color.RED);
        }
        else{

            btn_change_password.setEnabled(true);
            btn_change_password.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_shape));

        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void Changepassword() {

        if (CheckConnectivity.getInstance(this).isOnline()) {
            long imagename = System.currentTimeMillis();
            String url = AllUrl.baseUrl+"password/changepassword?" +
                    "model.changedpassword.PartnerId="+PartnerId+"" +
                    "&model.changedpassword.Currpwd="+ old_pass+"" +
                    "&model.changedpassword.Newpwd="+confirm_pass;

            System.out.println("get all sales-->  " + url);

            RequestQueue queue = Volley.newRequestQueue(this);
            showProgressDialog();
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            System.out.println("post--->response"+response);
                            // dbhelper.insert_offline_image_data(TicketNo,seraialno_image_data,oduserialno_image_data,invoice_image_data,tss,"upload");
                            hideProgressDialog();


                          //  {"changedpassword":null,"Data":null,"Message":"Data updated successfully","Status":true}

                            try{
                                JSONObject object=new JSONObject(response);

                                String status=object.getString("Status");
                                if (status.equals("true")){
                                    sessionManager.logoutUser();
                                }
                                else {
                                    Alert alert=new Alert();

                                    alert.showDialog(ChangePassword.this,"Change Password","Password Not Change Successfully\nPlease Try Again ");
                                }

                            }catch (Exception e){

                            }

                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressDialog();
                            Alert alert=new Alert();

                            alert.showDialog(ChangePassword.this,"Change Password","Password Not Change Successfully\nPlease Try Again ");

                        // error
                            //  Log.d("Error.Response", response);
                            //  dbhelper.insert_offline_image_data(TicketNo,seraialno_image_data,oduserialno_image_data,invoice_image_data,tss,"notupload");

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
//                    params.put("model.AppDocUpload.TicketNo",TicketNo);
//                    params.put("model.AppDocUpload.SerialPhoto",serialno_image);
//                    params.put("model.AppDocUpload.ODUSerialPhoto",oduserialno_image);
//                    params.put("model.AppDocUpload.InvoicePhto",invoice_image);
//                    params.put("model.AppDocUpload.UploadDate",tss);

                    return params;
                }
            };
            queue.add(postRequest);


        } else {

            Toast.makeText(this, "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
            //  dbhelper.insert_offline_image_data(TicketNo,seraialno_image_data,oduserialno_image_data,invoice_image_data,tss,"notupload");

        }
    }

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
