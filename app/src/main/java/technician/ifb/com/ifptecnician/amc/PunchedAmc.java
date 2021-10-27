package technician.ifb.com.ifptecnician.amc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.TaskListActivity;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

public class PunchedAmc extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        Punch_amc_adapter.Altclick,Punch_amc_adapter.Rcnclick,Punch_amc_adapter.JobsAdapterListener{


   Spinner spnr_search_criteria;
   String search_criteria,selected_date;
   EditText et_cust_search;
   TextView tv_select_name,tv_selectdate;

   LinearLayout ll_texttype,ll_date_type,ll_search,ll_main_input;

   Calendar calendar=Calendar.getInstance();

   Toolbar toolbar;

   RecyclerView lv_punched_amc_list;

   SessionManager sessionManager;
   String Tech_mobile_no,FrCode,PartnerId;

    private List<Punched_amc_model> models;
    public Punched_amc_model model;
    Punch_amc_adapter tasklist_adapter;

    Dbhelper dbhelper;
    LinearLayout ll_nointernet,ll_nodata;

    TextView tv_smssend;



    String [] search_criteria_array={" -- select criteria -- ","ICR Number","AMC Number","Customer ID","Status","Date","Create Date","Start Date","End Date","Product Id","Mobile Number","Alt. Mobile Number"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punched_amc);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Punched AMC");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dbhelper=new Dbhelper(getApplicationContext());
        sessionManager=new SessionManager(getApplicationContext());
        ll_nointernet=findViewById(R.id.ll_nointernet);


        try{

            HashMap<String, String> user = sessionManager.getUserDetails();

            Tech_mobile_no=user.get(SessionManager.KEY_mobileno);
            FrCode=user.get(SessionManager.KEY_FrCode);
            PartnerId=user.get(SessionManager.KEY_PartnerId);

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

        spnr_search_criteria= findViewById(R.id.spnr_search_criteria);
        tv_select_name=findViewById(R.id.tv_select_name);
        tv_selectdate=findViewById(R.id.tv_selectdate);
        et_cust_search=findViewById(R.id.et_cust_search);
        ll_texttype=findViewById(R.id.ll_texttype);
        ll_date_type=findViewById(R.id.ll_date_type);
        ll_search=findViewById(R.id.ll_search);
        ll_main_input=findViewById(R.id.ll_main_input);

        tv_smssend=findViewById(R.id.tv_error_message);

        ArrayAdapter punched_adapoter=new ArrayAdapter(PunchedAmc.this, android.R.layout.simple_dropdown_item_1line,search_criteria_array );
        punched_adapoter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spnr_search_criteria.setAdapter(punched_adapoter);
        spnr_search_criteria.setOnItemSelectedListener(this);

        lv_punched_amc_list=findViewById(R.id.lv_punched_amc_list);


        models= new ArrayList<>();

        tasklist_adapter=new Punch_amc_adapter(this, models, this,this,this);
        lv_punched_amc_list.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        lv_punched_amc_list.setLayoutManager(mLayoutManager);
        lv_punched_amc_list.setItemAnimator(new DefaultItemAnimator());
        lv_punched_amc_list.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        lv_punched_amc_list.setAdapter(tasklist_adapter);
        ll_nodata=findViewById(R.id.ll_nodata);

        GetpunchedAmc();

    }


    public void  GetpunchedAmc() {
        final String URL = "https://sapsecurity.ifbhub.com/api/service-contract/getPunchedAMCByTechCode";

        if (CheckConnectivity.getInstance(this).isOnline()) {
            try {

                ll_nointernet.setVisibility(View.GONE);

                showProgressDialog();
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonBody = new JSONObject();
                //jsonBody.put("TechCode", "14361826");

                jsonBody.put("TechCode", PartnerId);

                final String mRequestBody = jsonBody.toString();

                System.out.println("icr check"+mRequestBody);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                        hideProgressDialog();


                        if (error instanceof NetworkError) {

                            showsmspopup("NetworkError ,Please try after some times. ");

                        } else if (error instanceof ServerError) {

                            showsmspopup("No data found ");
                            ll_nodata.setVisibility(View.VISIBLE);

                        } else if (error instanceof AuthFailureError) {

                            showsmspopup("AuthFailureError ,Please try after some times.");

                        } else if (error instanceof ParseError) {

                            showsmspopup("ParseError ,Please try after some times.");

                        } else if (error instanceof TimeoutError) {

                            showsmspopup("Time out errorr ,Please try after some times.");
                        }


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

                            try {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        viewpunchedamc(new String(response.data));

                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                requestQueue.add(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {


            Cursor cursor;

            cursor=dbhelper.get_punched_amc();



            if (cursor !=null && cursor.moveToFirst()){

                do{
                    String amcdata=cursor.getString(cursor.getColumnIndex("data"));


                    viewpunchedamc(amcdata);

                }while (cursor.moveToNext());


            }

            ll_nointernet.setVisibility(View.VISIBLE);

           // Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        if (adapterView.getId()==R.id.spnr_search_criteria){


            search_criteria=adapterView.getSelectedItem().toString();

            if (search_criteria.equals(" -- select criteria -- ")){

                ll_main_input.setVisibility(View.GONE);



            }
            else {

                ll_main_input.setVisibility(View.VISIBLE);
              //"ICR Number","AMC Number","Customer ID","Status","Date","Create Date","Start Date","End Date","Product Id","Mobile NNumber","Alt. Mobile Number"};

                tv_select_name.setText(search_criteria);

                if (search_criteria.equals("ICR Number") || search_criteria.equals("AMC Number") || search_criteria.equals("Customer ID") || search_criteria.equals("Product Id")
                    || search_criteria.equals("Mobile NNumber") || search_criteria.equals("Alt. Mobile Number") || search_criteria.equals("Status") ){

                    ll_texttype.setVisibility(View.VISIBLE);
                    ll_search.setVisibility(View.VISIBLE);
                    ll_date_type.setVisibility(View.GONE);

                }

                else {

                    ll_texttype.setVisibility(View.GONE);
                    ll_date_type.setVisibility(View.VISIBLE);
                    ll_search.setVisibility(View.VISIBLE);

                }

            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void selectdate(View v){


        DatePickerDialog datePickerDialog=new DatePickerDialog(PunchedAmc.this,date,
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }


    DatePickerDialog.OnDateSetListener date= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH,i1);
            calendar.set(Calendar.DAY_OF_MONTH,i2);
            updatelevel();
        }
    };



    public void updatelevel(){

            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            tv_selectdate.setText(sdf.format(calendar.getTime()));
            selected_date = sdf.format(calendar.getTime());
            String myFormats = "yyyymmdd"; //In which you need put here
            SimpleDateFormat sdfs = new SimpleDateFormat(myFormat, Locale.US);
            selected_date = sdfs.format(calendar.getTime());
            // milestone=myCalendar.getTimeInMillis();

    }


    @Override
    public void onTaskSelected(AmcleadModel tasklistModel) {

    }

    @Override
    public void onAltclick(String refno, String smstype, String altno, String ticketno) {

    }

    @Override
    public void onRcnclick(String refno, String smstype, String altno, String ticketno) {

    }



    public void viewpunchedamc(String response){



        ll_nodata.setVisibility(View.GONE);

                try {

                    List<Punched_amc_model> items = new Gson().fromJson(response.toString(), new TypeToken<List<Punched_amc_model>>() {
                    }.getType());

                    // adding contacts to contacts list

                    models.clear();
                    models.addAll(items);
                    // refreshing recycler view
                    tasklist_adapter.notifyDataSetChanged();
                    String  date= Valuefilter.getdate();

                    dbhelper.delete_amc_punched_data();
                    dbhelper.insert_punched_amc_data(response,date);




                }catch (Exception e){
                    e.printStackTrace();
                }
    }


    public void showsmspopup(String message){

        tv_smssend.setText(message);

        tv_smssend.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(PunchedAmc.this, R.anim.slide_down);
        tv_smssend.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_smssend.setAnimation(null);
                tv_smssend.setVisibility(View.GONE);
            }
        }, 3000);
    }


}