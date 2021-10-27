package technician.ifb.com.ifptecnician.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.alert.Alertwithicon;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.amc.Create_customer;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.service.GetvaccinationType;
import technician.ifb.com.ifptecnician.service.Getvaccinecategories;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.troublesehoot.Troublesehooting;

public class AddVaccineDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {

    Spinner spnr_vac_name,spnr_vac_type;
    EditText et_first_location,et_snd_location;

    String vac_name="",vac_type="",first_date="",snd_date="",first_lacation="",snd_location="",
           calender_type,Partnerid;

    ProgressBar taskProcessing;
    LinearLayout ll_nointernet;
    TextView tv_error_message,tv_secand_calender,tv_first_calender;
    final Calendar myCalendar = Calendar.getInstance();
    SessionManager sessionManager;
    ArrayList<String> al_vac_name=new ArrayList<>();
    ArrayList<String> al_vac_type=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Vaccine Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sessionManager=new SessionManager(getApplicationContext());

        try{
            HashMap<String,String> user=sessionManager.getUserDetails();
            Partnerid=user.get(SessionManager.KEY_PartnerId);

            getvacccinename();
            getvacccinetype();

        }catch (Exception e){

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

        taskProcessing=findViewById(R.id.taskProcessing);
        ll_nointernet=findViewById(R.id.ll_nointernet);
        tv_error_message=findViewById(R.id.tv_error_message);
        spnr_vac_name=findViewById(R.id.spnr_vac_name);
        spnr_vac_type=findViewById(R.id.spnr_vac_type);
        tv_secand_calender=findViewById(R.id.tv_snd_calender);
        tv_first_calender=findViewById(R.id.tv_frst_calender);
        tv_first_calender.setOnClickListener(this);
        tv_secand_calender.setOnClickListener(this);

        et_first_location=findViewById(R.id.et_first_lacation);
        et_snd_location=findViewById(R.id.et_snd_loaction);

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (calender_type.equals("first_calender")) {

            String dataformet = "yyyy-MM-dd HH:mm";
            SimpleDateFormat dateFormats = new SimpleDateFormat(dataformet, Locale.ENGLISH);
            tv_first_calender.setText(dateFormats.format(myCalendar.getTime()));
            first_date=dateFormats.format(myCalendar.getTime());


        } else if (calender_type.equals("se_calender")) {


            String dataformet = "yyyy-MM-dd HH:mm";
            SimpleDateFormat dateFormat = new SimpleDateFormat(dataformet, Locale.ENGLISH);

            tv_secand_calender.setText(dateFormat.format(myCalendar.getTime()));
            snd_date=dateFormat.format(myCalendar.getTime());

        }

    }


    public void getvacccinename(){

        Getvaccinecategories getvaccinecategories=new Getvaccinecategories();

        getvaccinecategories.getvaccat(new Getvaccinecategories.Onresult() {
            @Override
            public void onsuccess(String status, String result) {

                if (status.equals("true")){

                    try{

                        al_vac_name=new ArrayList<>();
                        JSONArray array=new JSONArray(result);
                        for (int i=0;i<array.length();i++){
                            JSONObject jsonObject= array.getJSONObject(i);
                            al_vac_name.add(jsonObject.getString("Name"));

                            ArrayAdapter arrayAdapter = new ArrayAdapter(AddVaccineDetails.this, android.R.layout.simple_spinner_item, al_vac_name);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            spnr_vac_name.setAdapter(arrayAdapter);
                            spnr_vac_name.setOnItemSelectedListener(AddVaccineDetails.this);


                        }

                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }
                else {


                }

            }
        },AddVaccineDetails.this);

    }

    public void getvacccinetype(){


        GetvaccinationType getvaccinationType=new GetvaccinationType();

        getvaccinationType.getvactype(new GetvaccinationType.Onresult() {
            @Override
            public void onsuccess(String status, String result) {

                if (status.equals("true")){

                    try{

                        al_vac_type=new ArrayList<>();
                        JSONArray array=new JSONArray(result);
                        for (int i=0;i<array.length();i++){
                            JSONObject jsonObject= array.getJSONObject(i);
                            al_vac_type.add(jsonObject.getString("Type"));

                            ArrayAdapter arrayAdapter = new ArrayAdapter(AddVaccineDetails.this, android.R.layout.simple_spinner_item, al_vac_type);
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //Setting the ArrayAdapter data on the Spinner
                            spnr_vac_type.setAdapter(arrayAdapter);
                            spnr_vac_type.setOnItemSelectedListener(AddVaccineDetails.this);


                        }

                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }
                else {


                }

            }
        },AddVaccineDetails.this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_frst_calender:

                calender_type="first_calender";

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddVaccineDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                //following line to restrict future date selection
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();



                break;

            case R.id.tv_snd_calender:

                calender_type="se_calender";
                DatePickerDialog datePickerDialogs = new DatePickerDialog(AddVaccineDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                //following line to restrict future date selection
                datePickerDialogs.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialogs.show();

                break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){

            case R.id.spnr_vac_name:

                vac_name=adapterView.getItemAtPosition(i).toString();

                break;

            case R.id.spnr_vac_type:

                vac_type=adapterView.getItemAtPosition(i).toString();

                break;
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void checkblank(){


       first_lacation= et_first_location.getText().toString();
       snd_location=et_snd_location.getText().toString();

        if (vac_name.equals("")){


            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(AddVaccineDetails.this,"Validation Error","Please select vaccine name",false);


        }
        else if(vac_type.equals("")){


            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(AddVaccineDetails.this,"Validation Error","Please select vaccine type",false);

        }
        else if(first_date.equals("")){


            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(AddVaccineDetails.this,"Validation Error","Please select first doesc date",false);


        }
        else if (first_lacation.equals("")){


            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(AddVaccineDetails.this,"Validation Error","Please enter 1st does location ",false);

        }
        else {

            try{

                JSONObject jsonObject=new JSONObject();
                jsonObject.put("PartnerId",Partnerid);
                jsonObject.put("VaccineId",vac_name);
                jsonObject.put("Dateof1stDose",first_date);
                jsonObject.put("Dateof2stDose",snd_date);
                jsonObject.put("VaccinationAt",snd_location);
                jsonObject.put("Type",vac_type);

                submitvalue(jsonObject);

            }catch (Exception e){

                e.printStackTrace();
            }
        }
    }


    public void addvaccine(View v){

        checkblank();
    }


    public void submitvalue(JSONObject jsonObject){


        if (CheckConnectivity.getInstance(this).isOnline()) {
            try {

                showProgressDialog();
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                final String mRequestBody = jsonObject.toString();

                System.out.println("body-->"+mRequestBody);
                String url= AllUrl.baseUrl+"sa/vaccine/addRequest";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);

                        System.out.println("LOG_RESPONSE--> "+response);

                        hideProgressDialog();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                hideProgressDialog();
                                Alertwithicon alertwithicon=new Alertwithicon();
                                alertwithicon.showDialog(AddVaccineDetails.this,"Data Submit Error","Please try again later",false);

                            }
                        });
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        hideProgressDialog();
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);


                            try{

                                JSONObject object=new JSONObject(new String(response.data));

                                System.out.println("responseString-->"+object.toString());

                                String message=  object.getString("Message");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        //  showalert(Create_customer.this,message,"",true);

                                        if(snd_location.length()==0){

                                            succesdialog(message);

                                        }
                                        else{
                                            snd_location="";
                                            checkblank();
                                        }


                                    }
                                });

//                                btn_amc_submit.setBackgroundColor(Color.GRAY);
//                                btn_amc_submit.setClickable(false);


                            }catch(Exception e){

                                e.printStackTrace();
                            }

//                            }
//                            else {
//
//                                btn_amc_submit.setBackgroundColor(Color.RED);
//                                btn_amc_submit.setClickable(true);
//                                Alertwithicon alertwithicon=new Alertwithicon();
//                                alertwithicon.showDialog(Create_customer.this,"Data Submit Error","Please try again later",false);
//
//
//                            }


                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));



                    }
                };

                requestQueue.add(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(AddVaccineDetails.this,"No Internet Connection","Please check your internet Connection and try again",false);
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


    public void succesdialog(String msg) {
        final Dialog dialog = new Dialog(AddVaccineDetails.this);

        dialog.setContentView(R.layout.successdialog);
        // Set dialog title

        TextView tv_message,tv_ok;
        tv_message = dialog.findViewById(R.id.tv_message);
        tv_ok = dialog.findViewById(R.id.tv_ok);

        tv_message.setText(msg);

        ImageView iv_no = dialog.findViewById(R.id.vdialog_iv_getcontact);
        Button btn_call = dialog.findViewById(R.id.vdialog_btn_call);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                startActivity(new Intent(AddVaccineDetails.this, MainActivity.class));

            }
        });

        dialog.show();
        dialog.setCancelable(false);


    }

}