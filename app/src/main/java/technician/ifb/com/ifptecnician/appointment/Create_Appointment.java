package technician.ifb.com.ifptecnician.appointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static technician.ifb.com.ifptecnician.utility.Valuefilter.Parsedate;

public class Create_Appointment extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        View.OnClickListener {


    SharedPreferences prefdetails;
    String ticketdetails,Medium;
    TextView tv_calltype, tv_ticketno, tv_address, tv_callbook, tv_status, tv_rcnno, tv_servicetype, tv_custname,
    tv_callbookdate,tv_escal,tv_modelname,tv_pending_status,tv_problemdescription;
    ImageView iv_call;
    LinearLayout ll_medium,ll_select_date,ll_select_time;
    String Franchise,Product,CallBookDate,AssignDate,
            CustomerCode,CustomerName,PinCode,TelePhone,RCNNo,ProblemDescription,PendingReason,
            Street, City, State, Address, ServiceType,ChangeDate,flagstatus="",slot;

    String[] slot_array = {"Select Slot","First Half","Second Half"};
    Spinner spnr_slot;
    JSONArray finaljsonArray;
    String water_source="0",tds_level="0",water_code="0",jobstarttime="";
    Button btn_slot_submit;
    String TechnicianCode, TicketNo, franchise_bp, CallType, SerialNo="000000000000000000", odu_serial_no, DOY, Status,
    status_reason="", notes="", eta="", rechecked_date="", rescheduled_date="", ICR_no="", ICR_Date="", spare_flag="",
    product_code="", spare_serno="", pending_flag="", quantity="", unit="", itemno="", zz_bp="", zz_residence="",
    zz_members="", zz_fl="", zz_flbrand="", zz_mt5fl="", zz_tl="", zz_tlbrand="", zz_mt5tl="", zz_mw="", zz_mwbrand="",
    zz_mt5mw="", zz_dw="", zz_dwbrand="", zz_mt5dw="", zz_cd="", zz_cdbrand="", zz_mt5cd="", zz_ac="", zz_acbrand="",
     zz_mt5ac="",  zz_bih="",  zz_bihbrand="",  zz_mt5bih="",  zz_bio="",  zz_biobrand="",  zz_mt5bio="",  status_flag="",
     remarks,DOP="00000000",DOI="00000000";
    String datestatus,oduserno,proces_type;
    String sdate="00000000",stime="000000";
    TextView tv_select_date,tv_select_time;
    final Calendar myCalendar = Calendar.getInstance();
    SessionManager sessionManager;
    EditText et_remark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__appointment);


        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Create Appointment");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sessionManager=new SessionManager(getApplicationContext());
        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            TechnicianCode=user.get(SessionManager.KEY_PartnerId);
//            FrCode=user.get(SessionManager.KEY_FrCode);
//
//            System.out.println("FrCode-->>>> "+FrCode);
//            mobile_no=user.get(SessionManager.KEY_mobileno);
//            name=user.get(SessionManager.KEY_Name);
        }
        catch (Exception e){

            e.printStackTrace();
        }

        prefdetails = getSharedPreferences("details", 0);

        init();
    }


    public void init(){

        tv_calltype=findViewById(R.id.tv_calltype);
        tv_custname=findViewById(R.id.tv_custname);
        tv_callbookdate=findViewById(R.id.tv_callbookdate);
        tv_servicetype=findViewById(R.id.tv_servicetype);
        tv_ticketno=findViewById(R.id.tv_ticketno);
        tv_rcnno=findViewById(R.id.tv_rcnno);
        iv_call=findViewById(R.id.iv_call);
        tv_status=findViewById(R.id.tv_status);
        tv_escal=findViewById(R.id.tv_escal);
        tv_modelname=findViewById(R.id.tv_modelname);
        tv_pending_status=findViewById(R.id.tv_pending_status);
        tv_problemdescription=findViewById(R.id.tv_problemdescription);
        ll_medium=findViewById(R.id.ll_medium);

        spnr_slot=findViewById(R.id.spnr_slot);
        spnr_slot.setOnItemSelectedListener(this);
        btn_slot_submit=findViewById(R.id.btn_slot_submit);
        btn_slot_submit.setOnClickListener(this);

        ll_select_date=findViewById(R.id.ll_select_date);
        ll_select_time=findViewById(R.id.ll_select_time);

        ll_select_date.setOnClickListener(this);
        ll_select_time.setOnClickListener(this);
        ll_select_time.setClickable(false);

        tv_select_date=findViewById(R.id.tv_select_date);
        tv_select_time=findViewById(R.id.tv_select_time);
        et_remark=findViewById(R.id.et_remark);


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, slot_array);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_slot.setAdapter(aa);

        update_All_TextBox_Value();
    }

    public void update_All_TextBox_Value(){

        try {

            // getting value from share pref
            prefdetails = getSharedPreferences("details", 0);
            ticketdetails = prefdetails.getString("details", "");

            System.out.println("details"+ticketdetails);

            JSONObject object1 = new JSONObject(ticketdetails);

            TicketNo = object1.getString("TicketNo");
            CustomerName = object1.getString("CustomerName");
            RCNNo = object1.getString("RCNNo");
            tv_modelname.setText(object1.getString("Model"));
            CallType = object1.getString("CallType");
            SerialNo=object1.getString("serial_no");
            proces_type=processtype(CallType);
            Street=object1.getString("Street");
            City=object1.getString("City");
            State=object1.getString("State");
            DOP=Parsedate(object1.getString("DOP"));
            Product=object1.getString("Product");
            SerialNo=object1.getString("serial_no");
            Medium=object1.getString("Medium");
            franchise_bp=object1.getString("Franchise");
            oduserno=object1.getString("odu_ser_no");
            odu_serial_no=object1.getString("odu_ser_no");
            CustomerCode=object1.getString("CustomerCode");
            if(Medium.equals("Phone") || Medium.equals("CSR Portal")  ){

                ll_medium.setVisibility(View.VISIBLE);
            }

            if(DOP.equals("19000101")){
                DOP="00000000";
            }



            if(DOI.equals("19000101")){
                DOI="00000000";
            }
            CustomerCode=object1.getString("CustomerCode");


            ChangeDate=object1.getString("ChangeDate");

            Address = object1.getString("Address");
            PinCode=object1.getString("PinCode");
            CallBookDate = object1.getString("CallBookDate");
            Status = object1.getString("Status");
            Franchise=object1.getString("Franchise");
            AssignDate=object1.getString("AssignDate");
            RCNNo = object1.getString("RCNNo");
            ServiceType = object1.getString("ServiceType");
            CustomerName = object1.getString("CustomerName");

            String priorit = object1.getString("Priority");

            if (priorit.equals("")){
                tv_escal.setText("");
            }
            else {
                tv_escal.setText("Escalate ");
            }


            ProblemDescription=object1.getString("ProblemDescription");
            if (!ProblemDescription.equals("")){

                tv_problemdescription.setText(object1.getString("ProblemDescription"));
            }
            else
            {
                // tv_problemdescription.setVisibility(View.GONE);
                // tv_modelname.setText();

            }

            tv_calltype.setText(CallType.substring(0, 1));


            tv_ticketno.setText(TicketNo);
            PinCode=object1.getString("PinCode");

            String fulladress=Address.replace("\\s+", "");

//            tv_callbook.setText(CallBookDate);


            tv_status.setText(Status);
            tv_rcnno.setText(RCNNo);
            tv_servicetype.setText(ServiceType);
            tv_custname.setText(CustomerName);

            // if customer mobile no is null hode call button

            if (RCNNo.equals("")){

                iv_call.setVisibility(View.GONE);
            }

            // call to customer
            iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + RCNNo));// Initiates the Intent
                    startActivity(intent);
                }
            });


            PendingReason=object1.getString("PendingReason");

            if (!PendingReason.equals("")){

                tv_pending_status.setText(object1.getString("PendingReason"));
            }

            else{
                tv_pending_status.setVisibility(View.GONE);

            }

            Product=object1.getString("Product");


            String product=object1.getString("Product");

            if (product.equals("AC"))
            {
                tv_modelname.setText("Air Conditioner");
            }
            else  if (product.equals("CD"))
            {
                tv_modelname.setText("Clothes Dryer");
            }
            else  if (product.equals("DW"))
            {
                tv_modelname.setText("Dish Washer");
            }
            else  if (product.equals("IND"))
            {
                tv_modelname.setText("Industrial Dish washer");
            }
            else  if (product.equals("KA"))
            {
                tv_modelname.setText("Kitchen Appliances");
            }

            else  if (product.equals("RF"))
            {
                tv_modelname.setText("Refrigerator");
            }
            else  if (product.equals("ST"))
            {
                tv_modelname.setText("Stabilizer");
            }
            else  if (product.equals("WD"))
            {
                tv_modelname.setText("Washer Dryer");
            }
            else  if (product.equals("WM"))
            {
                tv_modelname.setText("Washing Machine");
            }
            else  if (product.equals("WP"))
            {
                tv_modelname.setText("Water Purifier");
            }

            else  if (product.equals("MW"))
            {
                tv_modelname.setText("Microwave Oven");
            }


            else {

                tv_modelname.setText(product);
            }



            //  tv_Model.setText(object1.getString("Model"));

            String machinestatus = object1.getString("MachinStatus");

            if (!machinestatus.equals("AMC")) {

             //   ll_amc_view.setVisibility(View.GONE);

            }


        } catch (Exception e) {

            e.printStackTrace();
        }

        // getting all pevois image

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.spnr_slot) {
            //tree_name=parent.getItemAtPosition(position).toString();
            slot =  slot_array[position];

            System.out.println("slot-->"+slot);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {


        if (v.getId()==R.id.btn_slot_submit){

            checkblank();

        }

        if (v.getId()==R.id.ll_select_date){
            choose_re_date();
        }

        if (v.getId()==R.id.ll_select_time){

            choose_time();
        }
    }


    public void choose_re_date(){

        datestatus = "redate";
        DatePickerDialog datePickerDialog = new DatePickerDialog(Create_Appointment.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
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



         if(datestatus.equals("redate")){

            tv_select_date.setText(sdf.format(myCalendar.getTime()));

//            errorDetails.Errorlog(CustomerDetailsActivity.this,"Change%20Re_scheduled%20Date", TAG,"Status%20Tab",tv_select_date.getText().toString(),mobile_no,TicketNo,"U",mobile_details,dbversion,tss);

            String dataformet="yyyyMMdd";
            SimpleDateFormat dateFormat=new SimpleDateFormat(dataformet,Locale.ENGLISH);

            sdate= dateFormat.format(myCalendar.getTime());

            System.out.println("sdate-->"+sdate);

            ll_select_time.setClickable(true);

        }




    }


    public void choose_time(){

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(Create_Appointment.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                try{

                    String minut,hours;

                    if (selectedHour < 10){

                        hours="0"+selectedHour;
                    }
                    else
                    {
                        hours=""+selectedHour;
                    }
                    if (selectedMinute < 10){

                        minut="0"+selectedMinute;
                    }
                    else
                    {
                        minut=""+selectedMinute;
                    }

                    stime=hours+""+minut+"00";

                 //   errorDetails.Errorlog(CustomerDetailsActivity.this,"Click%20On%20Re_scheduled%20Time", TAG,"Status%20Tab",stime,mobile_no,TicketNo,"U",mobile_details,dbversion,tss);


                    tv_select_time.setText( hours + ":" + minut);



                    //    All_click.add("Select%20Re_scheduled%20Time"+hours + ":" + minut);
                }
                catch (Exception e){

                    e.printStackTrace();
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void checkblank(){

       remarks= et_remark.getText().toString();

       if (sdate.equals("00000000")){

           System.out.println("sdate-->"+sdate);


           Toast.makeText(this, "Please select Date", Toast.LENGTH_SHORT).show();
       }
       else if (slot.equals("Select Slot")){

           Toast.makeText(this, "Please select slot", Toast.LENGTH_SHORT).show();
       }
       else if (remarks.length()==0){

           Toast.makeText(this, "Please select Remarks", Toast.LENGTH_SHORT).show();
       }
       else {

           createjson();
       }
    }

    public void createjson(){

        try{

            finaljsonArray =new JSONArray();

            JSONObject jsonObject=new JSONObject();

//            if (finaljsonArray.length()==0 && Status.equals("E0008")){
//
//                status_flag="H";
//            }
//            else{
//                status_flag="";
//            }

            // TechnicianCode,orderid,franchise_bp,CallType,serial_no,
            jsonObject.put("TechnicianCode",TechnicianCode);
            jsonObject.put("TicketNo",TicketNo);
            jsonObject.put("franchise_bp",franchise_bp);
            jsonObject.put("CallType",proces_type);
            jsonObject.put("serial_no",SerialNo);

            // odu_serial_no,DOP,DOI,Status,status_reason,
            jsonObject.put("odu_serial_no",odu_serial_no);
            jsonObject.put("DOP",DOP);
            jsonObject.put("DOI",DOI);
            jsonObject.put("Status","E0007");
            jsonObject.put("status_reason",status_reason);

            // notes,eta,rechecked_date,rescheduled_date,

            jsonObject.put("notes",notes);
            jsonObject.put("eta",eta);
            jsonObject.put("rechecked_date",rechecked_date);
            jsonObject.put("rescheduled_date",rescheduled_date);

            // ICR_no,ICR_Date,spare_flag,product_code,

            jsonObject.put("ICR_no",ICR_no);
            jsonObject.put("ICR_Date",ICR_Date);
            jsonObject.put("spare_flag",spare_flag);
            jsonObject.put("product_code",product_code);

            // spare_serno,pending_flag,quantity,unit,itemno,

            jsonObject.put("spare_serno",spare_serno);
            jsonObject.put("pending_flag",pending_flag);
            jsonObject.put("quantity",quantity);
            jsonObject.put("unit",unit);
            jsonObject.put("itemno",itemno);

            // zz_bp,zz_residence,zz_members,zz_fl,zz_flbrand,
            jsonObject.put("zz_bp",CustomerCode);
            jsonObject.put("zz_residence",zz_residence);
            jsonObject.put("zz_members",zz_members);
            jsonObject.put("zz_fl",zz_fl);
            jsonObject.put("zz_flbrand",zz_flbrand);

            // zz_mt5fl,zz_tl,zz_tlbrand,
            jsonObject.put("zz_mt5fl",zz_mt5fl);
            jsonObject.put("zz_tl",zz_tl);
            jsonObject.put("zz_tlbrand",zz_tlbrand);

            // zz_mt5tl,zz_mw,zz_mwbrand,zz_mt5mw,zz_dw,

            jsonObject.put("zz_mt5tl",zz_mt5tl);
            jsonObject.put("zz_mw",zz_mw);
            jsonObject.put("zz_mwbrand",zz_mwbrand);
            jsonObject.put("zz_mt5mw",zz_mt5mw);
            jsonObject.put("zz_dw",zz_dw);

            // zz_dwbrand,zz_mt5dw,zz_cd,zz_cdbrand,zz_mt5cd,
            jsonObject.put("zz_dwbrand",zz_dwbrand);
            jsonObject.put("zz_mt5dw",zz_mt5dw);
            jsonObject.put("zz_cd",zz_cd);
            jsonObject.put("zz_cdbrand",zz_cdbrand);
            jsonObject.put("zz_mt5cd",zz_mt5cd);

            // zz_ac,zz_acbrand,zz_mt5ac,zz_bih,zz_bihbrand,

            jsonObject.put("zz_ac",zz_ac);
            jsonObject.put("zz_acbrand",zz_acbrand);
            jsonObject.put("zz_mt5ac",zz_mt5ac);
            jsonObject.put("zz_bih",zz_bih);
            jsonObject.put("zz_bihbrand",zz_bihbrand);

            // zz_mt5bih,zz_bio,zz_biobrand,zz_mt5bio,status_flag,remarks

            jsonObject.put("zz_mt5bih",zz_mt5bih);
            jsonObject.put("zz_bio",zz_bio);
            jsonObject.put("zz_biobrand",zz_biobrand);
            jsonObject.put("zz_mt5bio",zz_mt5bio);
            jsonObject.put("status_flag","H");
            jsonObject.put("remarks","");
            jsonObject.put("recheck_rsn","");



            // new
            jsonObject.put("lv_1stresp_app_slot",slot);///stop first
            jsonObject.put("lv_1stresp_app_date",sdate); /// date
            jsonObject.put("lv_1stresp_app_remark",remarks); //remarks
            jsonObject.put("lv_job_start_time",jobstarttime); //start
            jsonObject.put("lv_water_type",water_code);
            jsonObject.put("lv_TDS_level",tds_level);


            finaljsonArray.put(jsonObject);

            System.out.println(finaljsonArray.toString());

            //   System.out.println(totallinecount +" and "+finaljsonArray.length() );


            // inactive submit button
            btn_slot_submit.setClickable(false);

            if (CheckConnectivity.getInstance(this).isOnline()) {

                System.out.println("finaljsonArray-->"+finaljsonArray);
                // submit data into server


                submitfinaldata(finaljsonArray);
               // dbhelper.insert_read_data(TicketNo,tss,ChangeDate,Status,spnr_status);
            }
            else {
             //   dataforoffline();
            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }



    public void submitfinaldata(JSONArray jsonArray){

        //  String url=AllUrl.baseUrl+"ZTECHSO/AddNewZTechSO";

        String url="https://sapsecurity.ifbhub.com/api/so/softcloser-by-app";

        System.out.println(url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String requestBody = jsonArray.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                //  System.out.println(response);zzzz

                if (response.equals("200")){

                    Toast.makeText(Create_Appointment.this, "Data Submit Successfully Done ", Toast.LENGTH_SHORT).show();

                    // if data insert success then insert submit ticket detais
//                    dbhelper.insert_read_data(TicketNo,tss,ChangeDate,Status,spnr_status);
//
//                    closeticket("Data Saved SuccessFully");
//                    errorDetails.Errorlog(CustomerDetailsActivity.this,"File%20Upload", TAG,"Data%20Upload%20done",response,mobile_no,TicketNo,"S",mobile_details,dbversion,tss);

                }
                else {
                    // if data insert success then insert submit ticket detais
//                    dbhelper.insert_read_data(TicketNo,tss,ChangeDate,Status,spnr_status);
//                    errorDetails.Errorlog(CustomerDetailsActivity.this,"File%20Upload", TAG,"Data%20upload%20error",response,mobile_no,TicketNo,"S",mobile_details,dbversion,tss);
//                    // if data submit time error find save data for offline
//                    dataforoffline();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                dbhelper.insert_read_data(TicketNo,tss,ChangeDate,Status,spnr_status);
//                errorDetails.Errorlog(CustomerDetailsActivity.this,"File%20Upload", TAG,"Data%20upload%20error","other%20errror",mobile_no,TicketNo,"S",mobile_details,dbversion,tss);
//                // if data submit time error find save data for offline
//                //  dataforoffline();

            }
        })
        {
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

                    System.out.println("responseString --->"+responseString);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);

    }


    public String processtype(String ptype){

        String processtype="";

        switch (ptype){

            case "SERVICE CALL":
                processtype="ZSER";
                break;
            case "INSTALLATION CALL":
                processtype="ZINT";
                break;
            case "MANDATORY CALLS":
                processtype="ZMAN";
                break;
            case "REINSTALLLATION ORDER":
                processtype="ZRIN";
                break;
            case "COURTESY VISIT":
                processtype="ZFOC";
                break;

            case "DEALER DEFECTIVE REQUEST":
                processtype="ZDD";
                break;
            case "REWORK ORDER" :
                processtype="ZREW";
                break;
            default:
                break;
        }
        return processtype;
    }
}