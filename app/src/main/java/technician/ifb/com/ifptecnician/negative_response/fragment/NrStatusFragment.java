package technician.ifb.com.ifptecnician.negative_response.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.fragment.app.Fragment;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.LoginActivity;
import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.TaskListActivity;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.negative_response.fragment.model.RecheckreasonModel;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.Valuefilter;


public class NrStatusFragment extends Fragment implements AdapterView.OnItemSelectedListener,View.OnClickListener{

    Spinner nr_status_spnr_status;
    String rechack_code;
    String recheckreason="";
    String recheck_date;
    String CallTypes;
    Button nr_status_btn_submit;
    String TicketNo;
    LinearLayout nr_ll_select_date;
    final Calendar myCalendar = Calendar.getInstance();
    String recheckdate="";
    TextView nr_tv_select_date;
    SessionManager sessionManager;
    String franchise_bp="",CallType="",serial_no="",odu_serial_no="",DOP="",DOI="",Status="E0010",status_reason="",notes="",eta="",rechecked_date="",rescheduled_date="",ICR_no="",ICR_Date="",spare_flag="",product_code="",spare_serno="",pending_flag="",quantity="",unit="",itemno="",zz_bp="",zz_residence="",zz_members="",zz_fl,zz_flbrand,zz_mt5fl,zz_tl,zz_tlbrand,zz_mt5tl,zz_mw,zz_mwbrand,zz_mt5mw,zz_dw,zz_dwbrand,zz_mt5dw,zz_cd,zz_cdbrand,zz_mt5cd,zz_ac,zz_acbrand,zz_mt5ac,zz_bih,zz_bihbrand,zz_mt5bih,zz_bio,zz_biobrand,zz_mt5bio,status_flag="",remarks;
    SharedPreferences sharedPreferences,sharedPreferencesmobile;
    String PartnerId,FrCode,mobile_no,name;
    Dbhelper dbhelper;
    String currentdate,ChangeDate;
    String mobile_details;
    ProgressBar progressBar;
    LinearLayout ll_nointernet;
    String details;
    JSONArray finaljsonArray;
    TextView tv_error_message;


    ArrayList<RecheckreasonModel> recheckreasonModels=new ArrayList<>();
    private static final String TAG="NR%20Status%20Fragment";
    String tss;
    ErrorDetails errorDetails;

    public NrStatusFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_nr_status,container,false);

       
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        tss = dfs.format(c);

        sharedPreferencesmobile= getActivity().getSharedPreferences(AllUrl.MOBILE_DETAILS,0);
        mobile_details=sharedPreferencesmobile.getString("Mobile_details","");

        errorDetails=new ErrorDetails();

        nr_status_spnr_status=view.findViewById(R.id.nr_status_spnr_status);
        nr_status_btn_submit=view.findViewById(R.id.nr_status_btn_submit);
        nr_status_btn_submit.setOnClickListener(this);
        dbhelper=new Dbhelper(getContext());

        nr_ll_select_date=view.findViewById(R.id.nr_ll_select_date);
        nr_ll_select_date.setOnClickListener(this);
        nr_tv_select_date=view.findViewById(R.id.nr_tv_select_date);
        currentdate=Valuefilter.getdate();
        tv_error_message=view.findViewById(R.id.tv_error_message);
        progressBar=view.findViewById(R.id.taskProcessing);
        ll_nointernet=view.findViewById(R.id.ll_nointernet);
        sessionManager=new SessionManager(getContext());

        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            FrCode=user.get(SessionManager.KEY_FrCode);
            mobile_no=user.get(SessionManager.KEY_mobileno);
            name=user.get(SessionManager.KEY_Name);
        }
        catch (Exception e){

            e.printStackTrace();
        }

        // get the all ticket details
        updateStringvalue();

        // get all reckeck reason from server
        get_recheck_reason();

        return view;

    }

//  ---------------------------------------  Get ticket details and add into sting  ----------------------------------
    public void updateStringvalue(){

        try{
            sharedPreferences = getActivity().getSharedPreferences("details", 0);
            details = sharedPreferences.getString("details", "");

            System.out.println("details"+details);

            JSONObject object = new JSONObject(details);
             TicketNo = object.getString("TicketNo");
             franchise_bp=object.getString("Franchise");
             CallType = object.getString("CallType");
             CallTypes=Valuefilter.getCallTypes(CallType);
             serial_no=object.getString("serial_no");
             zz_bp=object.getString("CustomerCode");
             eta="00000000";
             odu_serial_no=object.getString("odu_ser_no");
             DOP= Valuefilter.Parsedate(object.getString("DOP"));
             DOI=Valuefilter.Parsedate(object.getString("DOI"));
             ChangeDate=object.getString("ChangeDate");

        }

        catch (Exception e){

            e.printStackTrace();
        }
    }



    //  ---------------------   recheck spinner selectd value update  -----------------------------------------

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        RecheckreasonModel model= (RecheckreasonModel) parent.getSelectedItem();
        rechack_code= model.getPid();
        recheckreason=model.getPname();
        System.out.println(rechack_code+" ---- "+recheckreason);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // ---------------------------- All type of click -------------------------------------------------------

    @Override
    public void onClick(View v) {

        switch (v.getId()){


            // click on submit button

            case R.id.nr_status_btn_submit:
                checkblank();

                break;

            // click on recheck date section
            case R.id.nr_ll_select_date:
                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                //following line to restrict future date selection
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                // datePickerDialog.getDatePicker().setMinDate();
                datePickerDialog.show();

                break;
        }

    }

    //  -----------------------------------  Checking blank value -------------------------------------------------
    public void checkblank(){


        if (recheckreason.equals("")){

            showmesage("Recheck reason not found");

        }
         else if(recheckreason.equals("-- select recheck reason --"))
        {

            showmesage("Plaese select recheck reason");

        }

       else if (recheckdate.equals("")){

            showmesage("Plaese select recheck date");
        }
   //   ------------------------  show confirm dialog if user fill all the value ------------------------------
        else{

            showConfirmDialog(getContext(),"Service Order Update","Do you want to submit");
        }
    }

    // --------------------------   getting the recheck reason date from datepicker --------------------------
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

  // --------------------  update recheck date into text view --------------------------------------------
    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        nr_tv_select_date.setText(sdf.format(myCalendar.getTime()));
        recheckdate= nr_tv_select_date.getText().toString();
        String dataformet="yyyyMMdd";
        SimpleDateFormat dateFormats=new SimpleDateFormat(dataformet,Locale.ENGLISH);
        recheck_date= dateFormats.format(myCalendar.getTime());
        System.out.println("recheck_date"+recheck_date);
       }


    // -------------------------  create jsonarray file for sending data ------------------------------------

    public void createjson(String TechnicianCode,String orderid,String franchise_bp,String CallType,String serial_no,String odu_serial_no,String DOP,String DOI,String Status,String status_reason,String notes,String eta,String rechecked_date,String rescheduled_date,String ICR_no,String ICR_Date,String spare_flag,String product_code,String spare_serno,String pending_flag,String quantity,String unit,String itemno,String zz_bp,String zz_residence,String zz_members,String zz_fl,String zz_flbrand,String zz_mt5fl,String zz_tl,String zz_tlbrand,String zz_mt5tl,String zz_mw,String zz_mwbrand,String zz_mt5mw,String zz_dw,String zz_dwbrand,String zz_mt5dw,String zz_cd,String zz_cdbrand,String zz_mt5cd,String zz_ac,String zz_acbrand,String zz_mt5ac,String zz_bih,String zz_bihbrand,String zz_mt5bih,String zz_bio,String zz_biobrand,String zz_mt5bio,String status_flag,String remarks,String rechack_code){

        try{

            finaljsonArray=new JSONArray();

            JSONObject jsonObject=new JSONObject();

            // TechnicianCode,orderid,franchise_bp,CallType,serial_no,
            jsonObject.put("TechnicianCode",TechnicianCode);
            jsonObject.put("TicketNo",orderid);
            jsonObject.put("franchise_bp",franchise_bp);
            jsonObject.put("CallType",CallType);
            jsonObject.put("serial_no",serial_no);

            // odu_serial_no,DOP,DOI,Status,status_reason,
            jsonObject.put("odu_ser_no","");
            jsonObject.put("DOP",DOP);
            jsonObject.put("DOI",DOI);
            jsonObject.put("Status","E0010");
            jsonObject.put("status_reason",status_reason);

            // notes,eta,rechecked_date,rescheduled_date,

            jsonObject.put("notes",notes);
            jsonObject.put("eta",eta);
            jsonObject.put("rechecked_date",rechecked_date);
            jsonObject.put("rescheduled_date","");

            // ICR_no,ICR_Date,spare_flag,product_code,

            jsonObject.put("ICR_no","");
            jsonObject.put("ICR_Date","");
            jsonObject.put("spare_flag","");
            jsonObject.put("product_code","");

            // spare_serno,pending_flag,quantity,unit,itemno,

            jsonObject.put("spare_serno","");
            jsonObject.put("pending_flag","");
            jsonObject.put("quantity","");
            jsonObject.put("unit","");
            jsonObject.put("itemno","");

            // zz_bp,zz_residence,zz_members,zz_fl,zz_flbrand,
            jsonObject.put("zz_bp",zz_bp);
            jsonObject.put("zz_residence","");
            jsonObject.put("zz_members","");
            jsonObject.put("zz_fl","");
            jsonObject.put("zz_flbrand","");

            // zz_mt5fl,zz_tl,zz_tlbrand,
            jsonObject.put("zz_mt5fl","");
            jsonObject.put("zz_tl","");
            jsonObject.put("zz_tlbrand","");

            // zz_mt5tl,zz_mw,zz_mwbrand,zz_mt5mw,zz_dw,

            jsonObject.put("zz_mt5tl","");
            jsonObject.put("zz_mw","");
            jsonObject.put("zz_mwbrand","");
            jsonObject.put("zz_mt5mw","");
            jsonObject.put("zz_dw","");

            // zz_dwbrand,zz_mt5dw,zz_cd,zz_cdbrand,zz_mt5cd,
            jsonObject.put("zz_dwbrand","");
            jsonObject.put("zz_mt5dw","");
            jsonObject.put("zz_cd","");
            jsonObject.put("zz_cdbrand","");
            jsonObject.put("zz_mt5cd","");

            // zz_ac,zz_acbrand,zz_mt5ac,zz_bih,zz_bihbrand,

            jsonObject.put("zz_ac","");
            jsonObject.put("zz_acbrand","");
            jsonObject.put("zz_mt5ac","");
            jsonObject.put("zz_bih","");
            jsonObject.put("zz_bihbrand","");

            // zz_mt5bih,zz_bio,zz_biobrand,zz_mt5bio,status_flag,remarks

            jsonObject.put("zz_mt5bih","");
            jsonObject.put("zz_bio","");
            jsonObject.put("zz_biobrand","");
            jsonObject.put("zz_mt5bio","");
            jsonObject.put("status_flag","");
            jsonObject.put("remarks","");
            jsonObject.put("recheck_rsn",rechack_code);

           finaljsonArray.put(jsonObject);
           System.out.println(finaljsonArray.toString());

            nr_status_btn_submit.setEnabled(false);

            if (CheckConnectivity.getInstance(getActivity()).isOnline()) {

               submitfinaldata(finaljsonArray);
            }
            else {

                dbhelper.insert_read_data(TicketNo,currentdate,ChangeDate,Status,recheckreason);
                boolean status=dbhelper.insert_offlineservice_data(TicketNo,currentdate,finaljsonArray.toString(),details);
                tv_error_message.setVisibility(View.VISIBLE);
                tv_error_message.setText("Offline Data Saved Successfully");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        tv_error_message.setAnimation(null);
                        tv_error_message.setVisibility(View.GONE);
                        startActivity(new Intent(getContext(),MainActivity.class));
                    }
                }, 2000);

            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }

  // --------------------------- showing confirm dialog --------------------------------------------------
    public void showConfirmDialog(Context context,String title,String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        String positiveText = context.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        createjson(PartnerId,TicketNo,franchise_bp,CallTypes,serial_no,odu_serial_no,DOP,DOI,Status,status_reason,notes,eta,recheck_date,rescheduled_date,ICR_no,ICR_Date,spare_flag,product_code,spare_serno,pending_flag,quantity,unit,itemno,zz_bp,zz_residence,zz_members,zz_fl,zz_flbrand,zz_mt5fl,zz_tl,zz_tlbrand,zz_mt5tl,zz_mw,zz_mwbrand,zz_mt5mw,zz_dw,zz_dwbrand,zz_mt5dw,zz_cd,zz_cdbrand,zz_mt5cd,zz_ac,zz_acbrand,zz_mt5ac,zz_bih,zz_bihbrand,zz_mt5bih,zz_bio,zz_biobrand,zz_mt5bio,status_flag,remarks,rechack_code);
                        dialog.dismiss();
                    }


                });

        String negativeText = context.getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                        dialog.dismiss();


                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

  //   ---------------------  submit data into server ----------------------------------------
    public void submitfinaldata(JSONArray jsonArray){

        String url=AllUrl.baseUrl+"ZTECHSO/AddNewZTechSO";

        System.out.println(url);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        final String requestBody = jsonArray.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                System.out.println(response);

                 if (response.equals("200")){

                     // popup submit
                     dbhelper.insert_read_data(TicketNo,currentdate,ChangeDate,Status,recheckreason);

                     tv_error_message.setVisibility(View.VISIBLE);
                     tv_error_message.setText("Data Saved Successfully");

                     Handler handler = new Handler();
                     handler.postDelayed(new Runnable() {
                         public void run() {
                             // yourMethod();
                             tv_error_message.setAnimation(null);
                             tv_error_message.setVisibility(View.GONE);

                             startActivity(new Intent(getContext(),MainActivity.class));
                         }
                     }, 2000);
                 }

                 else {
                     // popup status
                     dbhelper.insert_read_data(TicketNo,currentdate,ChangeDate,Status,recheckreason);
                     boolean status=dbhelper.insert_offlineservice_data(TicketNo,currentdate,finaljsonArray.toString(),details);
                     tv_error_message.setVisibility(View.VISIBLE);
                     tv_error_message.setText("Offline Data Saved Successfully");

                     Handler handler = new Handler();
                     handler.postDelayed(new Runnable() {
                         public void run() {
                             // yourMethod();
                             tv_error_message.setAnimation(null);
                             tv_error_message.setVisibility(View.GONE);
                             startActivity(new Intent(getContext(),MainActivity.class));
                         }
                     }, 2000);
                 }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
              // popup status
                dbhelper.insert_read_data(TicketNo,currentdate,ChangeDate,Status,recheckreason);
                boolean status=dbhelper.insert_offlineservice_data(TicketNo,currentdate,finaljsonArray.toString(),details);
                tv_error_message.setVisibility(View.VISIBLE);
                tv_error_message.setText("Offline Data Saved Successfully");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        tv_error_message.setAnimation(null);
                        tv_error_message.setVisibility(View.GONE);
                        startActivity(new Intent(getContext(),MainActivity.class));
                    }
                }, 2000);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);




//        RequestQueue queue = Volley.newRequestQueue(this);
//        JsonArrayRequest request;
//        request = new JsonArrayRequest(Request.Method.POST,url,jsonArray, new Response.Listener<JSONArray>() {
//            @SuppressWarnings("unchecked")
//            @Override
//            public void onResponse(JSONArray response) {
//                //your logic to handle response
//                System.out.println(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //your logic to handle error
//                System.out.println("error comes");
//            }
//        }) {
//
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                /* This is very important to pass along */
//                params.put("Content-Type","application/json");
//                //other headers if any
//                return params;
//            }
//
//        };
//
//
//        int socketTimeout = 10000; //10 seconds -
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        request.setRetryPolicy(policy);
//        queue.add(request);

    }

// --------------------------  for showing  all message in top ---------------------------------------------
    public void showmesage(String message){

        tv_error_message.setText(message);

        tv_error_message.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        tv_error_message.startAnimation(animation);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error_message.setAnimation(null);
                tv_error_message.setVisibility(View.GONE);
            }
        }, 5000);
    }

    // --------------------- getting recheck reason from server --------------------------------------------

    public void get_recheck_reason(){

        if (CheckConnectivity.getInstance(getContext()).isOnline()) {

            nr_status_btn_submit.setClickable(true);

            ll_nointernet.setVisibility(View.GONE);


            long imagename = System.currentTimeMillis();

            //login api url
            String url = AllUrl.baseUrl+"PendingReasone/getPendingReasone";
            System.out.println(url);

            RequestQueue queue = Volley.newRequestQueue(getContext());
            progressBar.setVisibility(View.VISIBLE);
            StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            System.out.println("recheck response--> "+response);

                            progressBar.setVisibility(View.GONE);

                            try{

                                // after getting josn response

                                JSONObject object=new JSONObject(response);

                                String status=object.getString("Status");

                                if (status.equals("true")){

                                    JSONArray jsonArray=new JSONArray(object.getString("Data"));

                                       recheckreasonModels.add(new RecheckreasonModel("-- select recheck reason --",""));
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject obj = jsonArray.getJSONObject(i);
                                            String P_Name=obj.getString("P_Name");
                                            String P_Code=obj.getString("P_Code");
                                            recheckreasonModels.add(new RecheckreasonModel(P_Name,P_Code));

                                        }

                                       setvalueintorecheckspinner();

                                }
                                else {

                                    // if user enter wrong user id and password
                                    // Toast.makeText(LoginActivity.this, "Please Enter Valid Mobileno And Password or Technician Inactive", Toast.LENGTH_SHORT).show();
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

        } else {

            // if no internet found open no internet layout
            ll_nointernet.setVisibility(View.VISIBLE);

            nr_status_btn_submit.setClickable(false);

        }
    }

  //  -----------------------------  after getting recheck reason value update into spinner  ------------------------

    public void setvalueintorecheckspinner(){

        ArrayAdapter recheck_adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, recheckreasonModels);
        recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        nr_status_spnr_status.setAdapter(recheck_adapter);
        nr_status_spnr_status.setOnItemSelectedListener(this);

    }

}
