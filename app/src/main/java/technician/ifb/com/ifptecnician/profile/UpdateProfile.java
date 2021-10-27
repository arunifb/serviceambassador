package technician.ifb.com.ifptecnician.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.ChangePassword;
import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.Profile;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.TaskListActivity;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText et_pan,et_aadhar;
    String pan,aadhar,blood,PartnerId,imageurl="";
    String FrCode, EmailId, Name, passsword, mobileno,alldata;
    Button btn_update;
    TextView tv_error_message;
    SessionManager sessionManager;
    CircularImageView civ_pimage;
    SharedPreferences prefdetails;
    String [] blood_array={"A+","A-","B+","B-","AB+","AB-","O+","O-"};
    Spinner spnr_blood_group;
    ProgressBar taskProcessing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Update Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sessionManager= new SessionManager(getApplicationContext());
        prefdetails=getSharedPreferences("profile",0);
        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            alldata=user.get(SessionManager.KEY_ALL_DATA);

                JSONObject jsonObject=new JSONObject(alldata);

                blood=jsonObject.getString("BloodGroup");
                aadhar=jsonObject.getString("AadharNo");
                pan=jsonObject.getString("PANNo");

        }
        catch (Exception e){

            e.printStackTrace();
        }

        init();
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void init(){

        et_pan=findViewById(R.id.et_pan);
        et_aadhar=findViewById(R.id.et_aadhar);
       // et_blood=findViewById(R.id.et_blood);
        spnr_blood_group=findViewById(R.id.spnr_blood_group);
        spnr_blood_group.setOnItemSelectedListener(UpdateProfile.this);


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, blood_array);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_blood_group.setAdapter(aa);


        btn_update=findViewById(R.id.btn_update);
        tv_error_message=findViewById(R.id.tv_error_message);

        et_aadhar.setText(aadhar);
        et_pan.setText(pan);

        if (blood.equals("A+")){

            spnr_blood_group.setSelection(0);
        }
        else if (blood.equals("A+")){

            spnr_blood_group.setSelection(1);
        }
        else if (blood.equals("B+")){

            spnr_blood_group.setSelection(2);
        }
        else if (blood.equals("B-")){

            spnr_blood_group.setSelection(3);
        }
        else if (blood.equals("AB+")){

            spnr_blood_group.setSelection(4);
        }
        else if (blood.equals("AB-")){

            spnr_blood_group.setSelection(5);
        }
        else if (blood.equals("O+")){

            spnr_blood_group.setSelection(6);
        }

        else if (blood.equals("O-")){

            spnr_blood_group.setSelection(7);
        }
        civ_pimage=findViewById(R.id.civ_pimage);

        try {

            if (imageurl.equals("")){

            }
            else{

                Picasso.get()
                        .load(imageurl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                        .into(civ_pimage);
            }

        }
        catch (Exception e){


            e.printStackTrace();
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkblank();
            }
        });
    }


    public void checkblank(){

        pan=et_pan.getText().toString();
        aadhar=et_aadhar.getText().toString();
       // blood=et_blood.getText().toString();


        if (pan.length()==0){

            showsmspopup("Please Enter Driving Licence No");
        }
        else if(aadhar.length()!=12){

            showsmspopup("Please Enter valid Aadhar Card No");

        }
        else if (blood.length()==0){
            showsmspopup("Please Enter Blood Group");

        }
        else {

            updatedetails();
        }

    }

    public void updatedetails() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            blood=blood.replace("+","%2b");
            blood=blood.replace("-","%2D");

            String url = AllUrl.baseUrl+"profile/partner/?PartnerId="+PartnerId+"&PANNo="+pan+"&BloodGroup="+blood+"&AadharNo="+aadhar;

            System.out.println("get all sales-->  " + url);

            RequestQueue queue = Volley.newRequestQueue(this);
            showProgressDialog();
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            System.out.println("post--->response" + response);
                            // dbhelper.insert_offline_image_data(TicketNo,seraialno_image_data,oduserialno_image_data,invoice_image_data,tss,"upload");
                            hideProgressDialog();

                            try{

                                String res=response.replaceAll("^\"|\"$", "");

                                System.out.println("res-->"+res);
                                if (res.equals("Data updated successfully")){

                                   gotonext();
                                }
                                else {

                                    Alert alert = new Alert();
                                    alert.showDialog(UpdateProfile.this, "Profile Update", "Please Try Again Later");
                                }
                            }catch (Exception e){

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Alert alert = new Alert();

                            alert.showDialog(UpdateProfile.this, "Profile Update", "Please Try Again Later");
                            hideProgressDialog();

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
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


    public void showsmspopup(String message){

        tv_error_message.setText(message);

        tv_error_message.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(UpdateProfile.this, R.anim.slide_down);
        tv_error_message.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error_message.setAnimation(null);
                tv_error_message.setVisibility(View.GONE);
            }
        }, 3000);
    }



    public void gotonext(){


        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);
        builder.setTitle("Profile Update");

        builder.setMessage("Profile Update Successfully");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                        dialog.dismiss();

                        startActivity(new Intent(UpdateProfile.this, MainActivity.class));
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()== R.id.spnr_blood_group){

            blood =  blood_array[position];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


