package technician.ifb.com.ifptecnician.amc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.ImagePickerActivity;
import technician.ifb.com.ifptecnician.LoginActivity;
import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.VolleyMultipartRequest;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.alert.Alertwithicon;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.barcode.BarcodeCamera;
import technician.ifb.com.ifptecnician.barcode.SerialNoScan;
import technician.ifb.com.ifptecnician.ebill.CreateBill;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.CheckSerialIsActive;
import technician.ifb.com.ifptecnician.service.GenerateDigitalICR;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.AsyncFindmodel;
import technician.ifb.com.ifptecnician.utility.DateTimeFilter;
import technician.ifb.com.ifptecnician.utility.Hidekeyboard;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

import static com.android.volley.Request.Method.POST;

public class Create_customer extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener
          ,Checkserial.AsyncResponses{

    TextView tv_customer,tv_amc,tv_product,tv_price,tv_error;
    View view_customer,view_amc,view_product;

    ImageView iv_customer,iv_product,iv_amc,iv_price;
    LinearLayout ll_customer_view,ll_product_view,ll_amc_view,ll_price_view;
    SessionManager sessionManager;
    String Tech_Mobile,FrCode,PartnerId;
    TextView tv_icr_error;
    EditText et_icrno;
    SharedPreferences prefdetails;
    String icrno="";
    private static final int SCANSERIALNO=215;
    public static final int ICR_IMAGE_CAPTURE = 217;
    public static final int CHECK_IMAGE_CAPTURE =219;
    String validNumber = "[0-9]+";
    public static String amc_serialno="";

    byte[] ICR_IMAGE_CAPTURE_data = null ,CHECK_IMAGE_CAPTURE_data= null;

    ImageView lv_camera_icr,lv_camera_check;
    boolean imagestatus=false;

    //Customer Tab
    EditText et_customer_id,et_custname,et_cust_address,et_cust_phone_no,et_cust_email,et_cust_new_address,et_cust_alt_mobile;
    String customer_id,custname,cust_address,cust_phone_no,cust_email,  altno ,address;

    String alt_status="false",ads_status="false";

    // Product Tab
    EditText et_product_name,et_serial_no;
    String product_name,serial_no,product="";

    //Amc Tab
    Spinner spnr_contract_type,spnr_contract_type_details,spnr_combo;
    String contract_type="",contract_type_details="",fgcode="",contract_type_name="";
    TextView et_amc_amount;
    String amount="0";
    ArrayList<ContractTypeDetailsModel> detailsModels = new ArrayList<>();
    ArrayList<ComboTypeModel> combomodels = new ArrayList<>();
    ArrayList<ContractTypeModel> typeModels = new ArrayList<>();
    EditText et_discount;

    // price,
    EditText et_chequeno,et_bankname,et_upino,et_cust_new_house_no;
    TextView tv_icr_date,tv_check_date,tv_digital_icr_error;
    TextView et_tax,et_gross,et_discounts,et_net_amount,tv_physical_icr_header,tv_digital_icr_header,tv_digital_icr_no;
    String disamount="0",TaxAmount="0",GrossAmount="0", paymenttype="Cash",net_amount,house_no="";
    String date_type,chequedate="",chequedates="",chequeno="",bankname="",upino="";
    LinearLayout ll_checque,ll_online,ll_physical_icr,ll_verifyicr,ll_digital_icr;
    ImageView iv_digital_reload;
    JSONArray jsonArray;
    String renewdate, icrstatus="false",icrtype="physical";
    Toolbar toolbar;
    String icrdate="";
    final Calendar myCalendar = Calendar.getInstance();
    Button btn_amc_submit ;
    Bitmap imagebitmap;
    String token="V0hBVFNBUFA6d2FVU0VS";
    RecyclerView rv_combo;
    List<ComboDetailsModel> models;
    ComboDetailsAdapter detailsAdapter;

    LinearLayout ll_dis;

    // new address section

    EditText et_cust_new_postal,et_cust_new_state,et_cust_new_street,et_cust_new_more_address_details,et_cust_new_landmark,et_cust_new_city;
    Spinner spnr_area;
    String cust_new_postal="",cust_new_state="",cust_new_street="",cust_new_more_address_details="",cust_new_landmark="",
            cust_new_city="",area="",newaddressflag="";
    List<String> area_list;
    LinearLayout ll_change_address,ll_offler_details;
    ImageView iv_offler_details;
    Switch aSwitch;
    TextView tv_postal,tv_dop,tv_warrenty_des,tv_warranty_edate,tv_warrenty_type;
    private RequestQueue rQueue;
    String tss;
    Dbhelper dbhelper;
    String data_json,RegionCode="",zzsubcat="";
    String zzpurchase_date,warrantydesc,zzdeact_ib,warranty_edate,warrenty_type;
    boolean motor_wrnty=false,amc_wrnty=false,exten_wrnty=false,super_wrnty=false,labure_wrnty=false,offeropen=true;
    String curnt_date="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Create AMC");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefdetails = getSharedPreferences("amcdetails", 0);
        sessionManager=new SessionManager(getApplicationContext());
        dbhelper=new Dbhelper(getApplicationContext());

        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            Tech_Mobile=user.get(SessionManager.KEY_mobileno);
            FrCode=user.get(SessionManager.KEY_FrCode);
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            RegionCode=user.get(SessionManager.KEY_RegionCode);


        }catch (Exception e){

            e.printStackTrace();
        }
       // renewdate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        tss= Valuefilter.getdate();

        init();


    }

    @SuppressLint("SetTextI18n")
    public void init(){

        tv_error=findViewById(R.id.tv_error_message);
        lv_camera_icr=findViewById(R.id.lv_camera_icr);
        lv_camera_check=findViewById(R.id.lv_camera_check);

        lv_camera_icr.setOnClickListener(this);
        lv_camera_check.setOnClickListener(this);

        tv_customer=findViewById(R.id.tv_customer);
        tv_amc=findViewById(R.id.tv_amc);
        tv_product=findViewById(R.id.tv_product);
        tv_price=findViewById(R.id.tv_price);

        ll_customer_view=findViewById(R.id.ll_customer_view);
        ll_product_view=findViewById(R.id.ll_product_view);
        ll_amc_view=findViewById(R.id.ll_amc_view);
        ll_price_view=findViewById(R.id.ll_price_view);
        btn_amc_submit=findViewById(R.id.btn_amc_submit);
        //customer Tab

        et_customer_id=findViewById(R.id.et_customer_id);
        et_custname=findViewById(R.id.et_custname);
        et_cust_address=findViewById(R.id.et_cust_address);
        et_cust_phone_no=findViewById(R.id.et_cust_phone_no);
        et_cust_email=findViewById(R.id.et_cust_email);

        et_cust_new_address=findViewById(R.id.et_cust_new_address);
        et_cust_alt_mobile=findViewById(R.id.et_cust_alt_mobile);
        et_cust_new_house_no=findViewById(R.id.et_cust_new_house_no);
        rv_combo=findViewById(R.id.rv_combo);

        models= new ArrayList<>();

        detailsAdapter=new ComboDetailsAdapter(this, models);
        rv_combo.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_combo.setLayoutManager(mLayoutManager);
        rv_combo.setItemAnimator(new DefaultItemAnimator());
        // listView.addItemDecoration(new MyDividerItemDecoration(this,DividerItemDecoration.VERTICAL, 36));
        rv_combo.setAdapter(detailsAdapter);


        et_customer_id.setEnabled(false);
        et_custname.setEnabled(false);
        et_cust_address.setEnabled(false);
        et_cust_phone_no.setEnabled(false);
        et_cust_email.setEnabled(false);

        // Product Tab
        et_product_name=findViewById(R.id.et_product_name);
        et_serial_no=findViewById(R.id.et_serial_no);

        et_product_name.setEnabled(false);
        et_serial_no.setEnabled(false);
        ll_dis=findViewById(R.id.ll_dis);
        tv_dop=findViewById(R.id.tv_dop);;
        tv_warrenty_des=findViewById(R.id.tv_warrenty_des);
        tv_warranty_edate=findViewById(R.id.tv_warranty_edate);
        tv_warrenty_type=findViewById(R.id.tv_warrenty_type);

        //amc section

        ll_offler_details=findViewById(R.id.ll_offler_details);
        ll_offler_details.setVisibility(View.GONE);
        iv_offler_details=findViewById(R.id.iv_offler_details);
        iv_offler_details.setOnClickListener(this);
        et_icrno=findViewById(R.id.et_icrno);
        tv_icr_error=findViewById(R.id.tv_icr_error);
        et_amc_amount=findViewById(R.id.et_amc_amount);
        et_discount=findViewById(R.id.et_discount);

        et_discount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                boolean handled=false;

                if (actionId == EditorInfo.IME_ACTION_DONE){

                   // Hidekeyboard.hideSoftKeyboard(Create_customer.this);

                    disamount=et_discount.getText().toString();

                    if (disamount.length()==0){

                        disamount="0";
                        getbestprice();

                    }
                    else {

                    double intamount=Double.parseDouble(amount);
                    double intdisamount=Double.parseDouble(disamount);

                    double maxdis=0.1*intamount;
                    if (intdisamount <= maxdis){

                        getbestprice();
                        handled=true;

                    }

                    else{

                        disamount="0";
                        et_discount.setText("");
                        Alertwithicon alertwithicon=new Alertwithicon();
                        alertwithicon.showDialog(Create_customer.this,"Discount Amount Error","Discount amount less then 10% of Base Price",false);
                    }
                    }
                }
                return handled;
            }
        });




        spnr_contract_type=findViewById(R.id.spnr_contract_type);
       // spnr_contract_type.setOnItemSelectedListener(this);
        spnr_contract_type_details=findViewById(R.id.spnr_contract_type_details);
        spnr_combo=findViewById(R.id.spnr_combo);
      //  spnr_contract_type_details.setOnItemSelectedListener(this);
        et_tax=findViewById(R.id.et_tax_amount);
        et_gross=findViewById(R.id.et_gross);
        et_discounts=findViewById(R.id.et_discounts);
        et_net_amount=findViewById(R.id.et_net_amount);

        // price

        ll_online=findViewById(R.id.ll_online);
        et_upino=findViewById(R.id.et_upino);

        ll_checque=findViewById(R.id.ll_checque);
        ll_checque.setVisibility(View.GONE);

        et_chequeno=findViewById(R.id.et_chequeno);
        et_bankname=findViewById(R.id.et_bank_name);

        iv_customer=findViewById(R.id.iv_cust);
        iv_product=findViewById(R.id.iv_product);
        iv_amc=findViewById(R.id.iv_amc);
        iv_price=findViewById(R.id.iv_price);

        toolbar.setTitle("Customer Details");
        tv_customer.setTextColor(getResources().getColor(R.color.apptextcolor));
        tv_product.setTextColor(getResources().getColor(R.color.black));
        tv_amc.setTextColor(getResources().getColor(R.color.black));
        tv_price.setTextColor(getResources().getColor(R.color.black));

        iv_customer.setBackgroundResource(R.drawable.ic_person_black_24dp);
        iv_product.setBackgroundResource(R.drawable.ic_product_black);
        iv_amc.setBackgroundResource(R.drawable.ic_handsack_inactive);
        iv_price.setBackgroundResource(R.drawable.sales_icon);



        ll_customer_view.setVisibility(View.VISIBLE);
        ll_product_view.setVisibility(View.GONE);
        ll_amc_view.setVisibility(View.GONE);
        ll_price_view.setVisibility(View.GONE);

        tv_icr_date=findViewById(R.id.tv_icr_date);
        tv_icr_date.setOnClickListener(this);

        tv_check_date=findViewById(R.id.tv_check_date);
        tv_check_date.setOnClickListener(this);


        tv_physical_icr_header=findViewById(R.id.tv_physical_icr_header);
        tv_digital_icr_header=findViewById(R.id.tv_digital_icr_header);
        tv_digital_icr_no=findViewById(R.id.tv_digital_icr_no);

        ll_physical_icr=findViewById(R.id.ll_physical_icr);
        ll_verifyicr=findViewById(R.id.ll_verifyicr);
        ll_digital_icr=findViewById(R.id.ll_digital_icr);
        iv_digital_reload=findViewById(R.id.iv_digital_reload);
        iv_digital_reload.setOnClickListener(this);

        tv_digital_icr_error=findViewById(R.id.tv_digital_icr_error);

        //new address

        et_cust_new_postal=findViewById(R.id.et_cust_new_postal);
        et_cust_new_state=findViewById(R.id.et_cust_new_state);
        et_cust_new_street=findViewById(R.id.et_cust_new_street);
        et_cust_new_more_address_details=findViewById(R.id.et_cust_new_more_address_details);
        et_cust_new_landmark=findViewById(R.id.et_cust_new_landmark);
        et_cust_new_city=findViewById(R.id.et_cust_new_city);

        spnr_area=findViewById(R.id.spnr_area);
        spnr_area.setOnItemSelectedListener(this);

        et_cust_new_postal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cust_new_postal=et_cust_new_postal.getText().toString();

                if (cust_new_postal.length() ==6){

                    area_list=new ArrayList<>();

                    Getcustomerdetails(cust_new_postal);
                }


            }
        });


        ll_change_address=findViewById(R.id.ll_change_address);



        ll_change_address.setVisibility(View.GONE);
        aSwitch =findViewById(R.id.switch_address);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b){

                    ll_change_address.setVisibility(View.VISIBLE);

                    newaddressflag="X";
                }

                else {

                    ll_change_address.setVisibility(View.GONE);
                    newaddressflag="";

                }
            }
        });

       // new Compressimage().execute();

        updatetextbox();

      // tv_postal.setText("Postal Code "+ Html.fromHtml(" <font color= #FF0000> <sup> * </sup> </font>"));

      //  tv_postal.setText("Postal Code "+ Html.fromHtml("<![CDATA[<font color= #FF0000> <sup> * </sup> </font>]]>"));

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.iv_offler_details:

                if (offeropen){
                    ll_offler_details.setVisibility(View.VISIBLE);
                    offeropen=false;
                    if (models.size() ==0 ){
                        GetComboDetails();
                    }
                }
                else {

                    ll_offler_details.setVisibility(View.GONE);
                    offeropen=true;
                }





                break;

            case R.id.lv_camera_icr:

                view_image("icr");

                break;

            case R.id.lv_camera_check:

                view_image("cheque");

                break;

            case R.id.tv_icr_date:

                DatePickerDialog datePickerDialog=new DatePickerDialog(Create_customer.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                Calendar calendar2 = Calendar.getInstance();
                int year    = calendar2.get(Calendar.YEAR);
                int month   = calendar2.get(Calendar.MONTH);
                int today   = calendar2.get(Calendar.DAY_OF_MONTH);

                if(today == 1){

                    calendar2.set(year, month -1, 26);

                }

                if (today < 26){

                    calendar2.set(year,month,2);
                }
                else {

                    calendar2.set(year,month,26);
                }

                datePickerDialog.getDatePicker().setMinDate(calendar2.getTimeInMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

                date_type="icr";

                break;


            case R.id.tv_check_date:

                DatePickerDialog datePickerDialogs=new DatePickerDialog(Create_customer.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogs.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialogs.show();

                date_type="cheque";

                break;

            case R.id.iv_digital_reload:



            break;

            default:
                break;

        }
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (parent.getId() == R.id.spnr_contract_type) {

            ContractTypeModel typeModel = (ContractTypeModel) parent.getSelectedItem();

            contract_type = typeModel.getId();

            GetContract_type_detail(fgcode);

        }

        if (parent.getId() == R.id.spnr_contract_type_details) {

            ContractTypeDetailsModel detailsModel = (ContractTypeDetailsModel) parent.getSelectedItem();

            contract_type_details = detailsModel.getId();

            contract_type_name=detailsModel.getName();



            System.out.println("contract_type_details"+contract_type_details);


            System.out.println("contract_type_name-->"+contract_type_name);

            if (contract_type_details.equals("")){

                et_discount.setText("");
                disamount="0";

            }

           else if (contract_type_name.equals("Motor Warranty 10 years")){

                ll_dis.setVisibility(View.GONE);
                et_discount.setText("");
                disamount="0";
                Getprice();

            }

           else if(RegionCode.equals("22") || RegionCode.equals("19")){

                if (product.equals("MW") || zzsubcat.equals("TL")){

                    ll_dis.setVisibility(View.GONE);
                   // getcombotype();
                }
                else {

                    ll_dis.setVisibility(View.VISIBLE);
                }

                et_discount.setText("");
                disamount="0";
                Getprice();

            }
           else {

                ll_dis.setVisibility(View.VISIBLE);

                et_discount.setText("");
                disamount="0";
                Getprice();
            }

        }

        if (parent.getId() == R.id.spnr_area){

            area=parent.getSelectedItem().toString();

            System.out.println("area-->"+area);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void updatetextbox(){


        try{

            prefdetails = getSharedPreferences("amcdetails", 0);
            String ticketdetails = prefdetails.getString("amcdetails", "");

            System.out.println("details"+ticketdetails);
            JSONObject object = new JSONObject(ticketdetails);
            //Customer Tab
            customer_id=object.getString("custid");
            custname=object.getString("custname");
            cust_address=object.getString("custaddress");
            cust_phone_no=object.getString("custphone");
            cust_email=object.getString("custemail");
            altno=object.getString("altphone");

            et_customer_id.setText(customer_id);
            et_custname.setText(custname);
            et_cust_address.setText(cust_address);
            et_cust_phone_no.setText(cust_phone_no);
            et_cust_email.setText(cust_email);

            //Product Tab
            product_name=object.getString("product_name");
            product=object.getString("product");
            serial_no=object.getString("serialno");

            et_product_name.setText(product_name);
            et_serial_no.setText(serial_no);
            et_cust_alt_mobile.setText(altno);

            fgcode=object.getString("productid");


            getcustdetails(customer_id);




        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    private void getcustdetails(String customer_id) {

        if (CheckConnectivity.getInstance(this).isOnline()){

           // String URL = AllUrl.baseUrl+"sa/customers/search?contact=&zzfranch="+FrCode+"&zzsoldto="+customer_id;

            String URL =  AllUrl.baseUrl+"sa/customers/search?contact=&zzfranch=&zzsoldto="+customer_id+"&serialNumber="+serial_no;

            System.out.println("URL-->"+URL);

            try {

                RequestQueue queue= Volley.newRequestQueue(Create_customer.this);

                StringRequest request=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{


                            System.out.println("response-->"+response);

                            JSONObject object =new JSONObject(response);
                            String Status=object.getString("Status");
                            if (Status.equals("true")){

                               // JSONObject jsonObject=new JSONObject(object.getString("Data"));

                                JSONArray jsonArray=object.getJSONArray("Data");

                                for (int i=0;i<jsonArray.length();i++){

                                  JSONObject jsonObject=jsonArray.getJSONObject(i);

                                  zzpurchase_date=jsonObject.getString("zzpurchase_date");

                                  warrantydesc=jsonObject.getString("warrantydesc");
                                  zzdeact_ib=jsonObject.getString("zzdeact_ib");
                                  warranty_edate=jsonObject.getString("warranty_edate");
                                  warrenty_type=jsonObject.getString("zz0010");
                                  zzsubcat=jsonObject.getString("zzsubcat");

                                  tv_dop.setText(zzpurchase_date);
                                  tv_warrenty_des.setText(warrantydesc);




                                  if (warranty_edate.equals("2/2/1900 12:00:00 AM")){

                                      tv_warranty_edate.setText("");

                                  }
                                  else {

                                      tv_warranty_edate.setText(warranty_edate);
                                  }

                                  tv_warrenty_type.setText(warrenty_type);


                                  if (warrenty_type.equals("SW")){

                                      motor_wrnty=true;
                                  }
                                  else {

                                      motor_wrnty=false;
                                  }

                                  System.out.println("zzpurchase_date -->"+ zzpurchase_date);
                                  System.out.println("warrantydesc --> "+ warrantydesc);
                                  System.out.println("zzdeact_ib --> "+ zzdeact_ib);



                                  // Amc validation
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a",Locale.getDefault());
                                    Date c = Calendar.getInstance().getTime();
                                    String today = simpleDateFormat.format(c);
                                  /*  try {
                                        Date date1 = simpleDateFormat.parse(warranty_edate);
                                        Date date2 = simpleDateFormat.parse(today);

                                        assert date1 != null;
                                        assert date2 != null;
                                        long day= DateTimeFilter.printDifference(date1, date2);
                                     System.out.println("Diffrence--> "+day);

                                     if (day < 30){

                                        amc_wrnty=true;
                                     }
                                     else {

                                         amc_wrnty=false;
                                     }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }*/


                                    // Super Warrenty and extended warrenty validation

                                    try {
                                        Date date1 = simpleDateFormat.parse(zzpurchase_date);
                                        Date date2 = simpleDateFormat.parse(today);

                                        assert date1 != null;
                                        assert date2 != null;
                                        long day= DateTimeFilter.printDifference(date1, date2);
                                        System.out.println("Super Warrenty --> "+day);

                                        if (day < 90){

                                            super_wrnty=true;
                                            exten_wrnty=true;
                                        }
                                        else {

                                            super_wrnty=false;
                                            exten_wrnty=false;
                                        }

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }



                                }

                                GetContractType(product);
                            }
                            else {

                              //  prices.onsuccess("false","");
                            }
                        }catch (Exception e){

                            e.printStackTrace();

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {



                    }
                });

                int socketTimeout = 5000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                request.setRetryPolicy(policy);
                queue.add(request);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void Customer_view(View v){

        toolbar.setTitle("Customer Details");
        tv_customer.setTextColor(getResources().getColor(R.color.apptextcolor));
        tv_product.setTextColor(getResources().getColor(R.color.black));
        tv_amc.setTextColor(getResources().getColor(R.color.black));
        tv_price.setTextColor(getResources().getColor(R.color.black));

        iv_customer.setBackgroundResource(R.drawable.ic_person_black_24dp);
        iv_product.setBackgroundResource(R.drawable.ic_product_black);
        iv_amc.setBackgroundResource(R.drawable.ic_handsack_inactive);
        iv_price.setBackgroundResource(R.drawable.sales_icon);

        ll_customer_view.setVisibility(View.VISIBLE);
        ll_product_view.setVisibility(View.GONE);
        ll_amc_view.setVisibility(View.GONE);
        ll_price_view.setVisibility(View.GONE);
    }

    public void Product_view(View v){

        toolbar.setTitle("Product Details");
        tv_customer.setTextColor(getResources().getColor(R.color.black));
        tv_product.setTextColor(getResources().getColor(R.color.apptextcolor));
        tv_amc.setTextColor(getResources().getColor(R.color.black));
        tv_price.setTextColor(getResources().getColor(R.color.black));

        iv_customer.setBackgroundResource(R.drawable.ic_person_inactive);
        iv_product.setBackgroundResource(R.drawable.ic_product_red);
        iv_amc.setBackgroundResource(R.drawable.ic_handsack_inactive);
        iv_price.setBackgroundResource(R.drawable.sales_icon);

        ll_customer_view.setVisibility(View.GONE);
        ll_product_view.setVisibility(View.VISIBLE);
        ll_amc_view.setVisibility(View.GONE);
        ll_price_view.setVisibility(View.GONE);

    }

    public void Amc_view(View v){

        toolbar.setTitle("AMC");

        tv_customer.setTextColor(getResources().getColor(R.color.black));
        tv_product.setTextColor(getResources().getColor(R.color.black));
        tv_amc.setTextColor(getResources().getColor(R.color.apptextcolor));
        tv_price.setTextColor(getResources().getColor(R.color.black));

        iv_customer.setBackgroundResource(R.drawable.ic_person_inactive);
        iv_product.setBackgroundResource(R.drawable.ic_product_black);
        iv_amc.setBackgroundResource(R.drawable.ic_handshake);
        iv_price.setBackgroundResource(R.drawable.sales_icon);

        ll_customer_view.setVisibility(View.GONE);
        ll_product_view.setVisibility(View.GONE);
        ll_amc_view.setVisibility(View.VISIBLE);
        ll_price_view.setVisibility(View.GONE);
    }

    public void price_view(View view){

        toolbar.setTitle("Payment");
        tv_customer.setTextColor(getResources().getColor(R.color.black));
        tv_product.setTextColor(getResources().getColor(R.color.black));
        tv_amc.setTextColor(getResources().getColor(R.color.black));
        tv_price.setTextColor(getResources().getColor(R.color.apptextcolor));

        iv_customer.setBackgroundResource(R.drawable.ic_person_inactive);
        iv_product.setBackgroundResource(R.drawable.ic_product_black);
        iv_amc.setBackgroundResource(R.drawable.ic_handsack_inactive);
        iv_price.setBackgroundResource(R.drawable.sales_red);

        ll_customer_view.setVisibility(View.GONE);
        ll_product_view.setVisibility(View.GONE);
        ll_amc_view.setVisibility(View.GONE);
        ll_price_view.setVisibility(View.VISIBLE);
    }

    public void  IcrnoCheck() {
        final String URL = AllUrl.baseUrl+"service-contract/icr/validate";

        if (CheckConnectivity.getInstance(this).isOnline()) {
            try {

                showProgressDialog();
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("ICRNo", icrno);
                jsonBody.put("Franchise_id", FrCode);


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
                                JSONObject object = new JSONObject(new String(response.data));
                                //  {"Status":"FAILED","Data":"Mobile no already exist"}

                                System.out.println("object-->"+object);

                                String message=object.getString("Message");
                                if (message.equals("Success")){

                                    icrstatus="true";
                                }
                                else {
                                    icrstatus="false";
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_icr_error.setText(message);
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

            error_showpop("No Internet Connection");

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

    public void VerifyIcr(View v){

        icrno=et_icrno.getText().toString();

        if (icrno.length()==0){

            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(Create_customer.this,"ICR Number Empty","Please enter ICR Number",false);

        }
        else{
            IcrnoCheck();
        }

    }

    public void GetContractType(String product){

        //String url=AllUrl.baseUrl+"service-contract/contract-type";

        String url=AllUrl.baseUrl+"service-contract/contract-type?model.ContractTypeByPrdCode.PrdCode="+product;


        if (CheckConnectivity.getInstance(this).isOnline()) {

          // showProgressDialog();
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                             //   progressBar.setVisibility(View.GONE);

                              //  hideProgressDialog();

                                JSONArray jsonArray=new JSONArray(response);

                                typeModels.clear();
                                typeModels.add(new ContractTypeModel("Select Contract Type", ""));

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    String ContractName= obj.getString("ContractName");
                                   switch (ContractName){

                                       case "AMC":


                                           typeModels.add(new ContractTypeModel(obj.getString("ContractName"), obj.getString("ContractCode")));

//                                           if (amc_wrnty){
//
//                                           }

                                           break;

                                       case "Extended Warranty":

                                           if (exten_wrnty){
                                               typeModels.add(new ContractTypeModel(obj.getString("ContractName"), obj.getString("ContractCode")));

                                           }
                                           break;

                                       case "Labour AMC":
                                           typeModels.add(new ContractTypeModel(obj.getString("ContractName"), obj.getString("ContractCode")));


                                           break;

                                       case "Super Warranty":

                                           if (super_wrnty){

                                               typeModels.add(new ContractTypeModel(obj.getString("ContractName"), obj.getString("ContractCode")));

                                           }

                                           break;
                                       case "Motor Warranty":

                                           if (motor_wrnty){

                                               typeModels.add(new ContractTypeModel(obj.getString("ContractName"), obj.getString("ContractCode")));

                                           }
                                           break;

                                      }
//                                    typeModels.add(new ContractTypeModel(obj.getString("ContractName"), obj.getString("ContractCode")));


                                }


                                ArrayAdapter recheck_adapter = new ArrayAdapter(Create_customer.this, android.R.layout.simple_spinner_item, typeModels);
                                recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //Setting the ArrayAdapter data on the Spinner
                                spnr_contract_type.setAdapter(recheck_adapter);
                                spnr_contract_type.setOnItemSelectedListener(Create_customer.this);


                            }catch (Exception e){

                                e.printStackTrace();
                            }




                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //progressBar.setVisibility(View.GONE);

                }
            });
// Add the request to the RequestQueue.

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);


        }

    }

    public void GetContract_type_detail(String FGCode){

        final String URL = AllUrl.baseUrl+"service-contract/contract-type-detail";

        if (CheckConnectivity.getInstance(this).isOnline()) {
            try {

                showProgressDialog();
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("FGCode", FGCode);
                jsonBody.put("ContractCode",contract_type);



                System.out.println(jsonBody.toString());

                final String mRequestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
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

                                JSONArray jsonArray=new JSONArray(new String(response.data));



                                detailsModels.clear();
                                detailsModels.add(new ContractTypeDetailsModel("Select Contract Type Details", ""));


                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    JSONObject jsonObject=obj.getJSONObject("ContractType");

                                    detailsModels.add(new ContractTypeDetailsModel(jsonObject.getString("ContractName"),jsonObject.getString("ContractCode")));
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        ArrayAdapter recheck_adapter = new ArrayAdapter(Create_customer.this, android.R.layout.simple_spinner_item, detailsModels);
                                        recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        //Setting the ArrayAdapter data on the Spinner
                                        spnr_contract_type_details.setAdapter(recheck_adapter);
                                        spnr_contract_type_details.setOnItemSelectedListener(Create_customer.this);
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

            error_showpop("No Internet Connection");

        }
    }

    public void Getprice(){

            final String URL = AllUrl.baseUrl+"service-contract/price/base-price";


            if (CheckConnectivity.getInstance(this).isOnline()) {
                try {

                    showProgressDialog();
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("FGCode",fgcode);
                    jsonBody.put("ContractTypeCode",contract_type);
                    jsonBody.put("AMCTypeCode",contract_type_details);

                    final String mRequestBody = jsonBody.toString();

                    System.out.println("body-->"+mRequestBody);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG_RESPONSE", response);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_RESPONSE", error.toString());
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

                                    System.out.println("response-->"+new String(response.data));

                                    JSONArray jsonArray=new JSONArray(new String(response.data));

                                    System.out.println("response-->"+jsonArray.toString());

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                                        amount=jsonObject.getString("BasePrice");
                                        TaxAmount=jsonObject.getString("TaxAmount");
                                        GrossAmount=jsonObject.getString("GrossAmount");

                                    }

                                    if (jsonArray.length()==0){
                                        amount="Not Found";
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            et_amc_amount.setText("\u20B9 "+amount);
                                            et_tax.setText("\u20B9 "+TaxAmount);
                                            et_gross.setText("\u20B9 "+GrossAmount);

                                            et_discounts.setText("- "+"\u20B9 "+disamount);

                                            try{


                                               double netamount= Double.parseDouble(amount) - Double.parseDouble(disamount);

                                                et_net_amount.setText("\u20B9 "+ netamount);
                                            }catch (Exception e){

                                                e.printStackTrace();
                                            }



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
                error_showpop("No Internet Connection");


            }

    }


    public void getcombotype(){

            final String URL = AllUrl.baseUrl+"sa/combo/type";

      //  https://sapsecurity.ifbhub.com/api/sa/combo/type
            if (CheckConnectivity.getInstance(this).isOnline()) {
                try {


                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("prdcode", product);
                    jsonBody.put("Regioncode","");
                    jsonBody.put("AMCID", contract_type_details);
                    jsonBody.put("Brcode","11002457");

                    System.out.println(jsonBody.toString());

                    final String mRequestBody = jsonBody.toString();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG_RESPONSE", response);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_RESPONSE", error.toString());
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

                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);

                                try {

                                    JSONObject jsonObject=new JSONObject(new String(response.data));



                                    JSONObject object=jsonObject.getJSONObject("response");
                                    String status=object.getString("Status");

                                    if (status.equals("true")){

                                        JSONArray jsonArray=jsonObject.getJSONArray("data");



                                        combomodels.clear();
                                        combomodels.add(new ComboTypeModel("Select combo offer", ""));


                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject obj = jsonArray.getJSONObject(i);

                                            combomodels.add(new ComboTypeModel(obj.getString("ComboType"),obj.getString("ComboType")));
                                        }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                ArrayAdapter combo_adapter = new ArrayAdapter(Create_customer.this, android.R.layout.simple_spinner_item, combomodels);
                                                combo_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                //Setting the ArrayAdapter data on the Spinner
                                                spnr_combo.setAdapter(combo_adapter);
                                                spnr_combo.setOnItemSelectedListener(Create_customer.this);
                                            }
                                        });

                                    }
                                    else {


                                        combomodels.clear();
                                        combomodels.add(new ComboTypeModel("Select combo offer", ""));

                                        ArrayAdapter combo_adapter = new ArrayAdapter(Create_customer.this, android.R.layout.simple_spinner_item, combomodels);
                                        combo_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        //Setting the ArrayAdapter data on the Spinner
                                        spnr_combo.setAdapter(combo_adapter);
                                        spnr_combo.setOnItemSelectedListener(Create_customer.this);

                                    }

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

                error_showpop("No Internet Connection");

            }

    }


    public void GetComboDetails(){

        final String URL = AllUrl.baseUrl+"sa/combo/type/details";


        if (CheckConnectivity.getInstance(this).isOnline()) {
            try {

                showProgressDialog();
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JSONObject jsonBody = new JSONObject();

                jsonBody.put("ComboType","C005");
                jsonBody.put("AMCId",contract_type_details);

                final String mRequestBody = jsonBody.toString();

                System.out.println("body-->"+mRequestBody);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_RESPONSE", response);


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
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

                                JSONObject jsonObject=new JSONObject(new String(response.data));



                                JSONObject object=jsonObject.getJSONObject("response");
                                String status=object.getString("Status");

                                if (status.equals("true")) {


                                    List<ComboDetailsModel> items = new Gson().fromJson(jsonObject.getString("data").toString(), new TypeToken<List<ComboDetailsModel>>() {
                                    }.getType());

                                    models.clear();
                                    models.addAll(items);
                                    // refreshing recycler view

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            detailsAdapter.notifyDataSetChanged();
                                        }
                                    });


                                }
                                else{


                                }


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
            error_showpop("No Internet Connection");


        }

    }

    public void updateprice(View v){

       // Hidekeyboard.hideSoftKeyboard(Create_customer.this);
        disamount=et_discount.getText().toString();


        if (disamount.length()==0){

            disamount="0";
            getbestprice();
        }

        else{


        double intamount=Double.parseDouble(amount);
        double intdisamount=Double.parseDouble(disamount);

        double maxdis=0.1*intamount;
        if (intdisamount <= maxdis){

            getbestprice();

        }

        else{


            disamount="0";
            et_discount.setText("");
            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(Create_customer.this,"Discount Amount Error","Discount amount less then 10% of Base Price",false);
          }
         }

    }

    public void getbestprice(){

        if (contract_type.length()==0){


            error_showpop("Please select Amc type");
        }
        else if (contract_type_details.length()==0){

            error_showpop("Please select Amc type details");

        }
        else {

            String url=AllUrl.baseUrl+"service-contract/price/base-price";

            if (CheckConnectivity.getInstance(this).isOnline()) {
                try {

                    showProgressDialog();
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("FGCode",fgcode);
                    jsonBody.put("ContractTypeCode",contract_type);
                    jsonBody.put("AMCTypeCode",contract_type_details);
                    jsonBody.put("DiscountAmount",disamount);

                    final String mRequestBody = jsonBody.toString();

                    System.out.println("body-->"+mRequestBody);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG_RESPONSE", response);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_RESPONSE", error.toString());
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

                                    System.out.println("response-->"+new String(response.data));

                                    JSONArray jsonArray=new JSONArray(new String(response.data));

                                    System.out.println("response-->"+jsonArray.toString());

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                                        amount=jsonObject.getString("BasePrice");
                                        TaxAmount=jsonObject.getString("TaxAmount");
                                        GrossAmount=jsonObject.getString("GrossAmount");
                                        //   [{"BasePrice":"2100.00","lblBasePrice":"Base Price","TaxAmount":"360.00","lblTaxAmount":"Tax Amount","GrossAmount":"2360.00","lblGrossAmount":"Gross Amount"}]

                                    }

                                    if (jsonArray.length()==0){
                                        amount="Not Found";
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            et_amc_amount.setText("\u20B9 "+amount);
                                            et_tax.setText("\u20B9 "+TaxAmount);
                                            et_gross.setText("\u20B9 "+GrossAmount);
                                            et_discounts.setText("- "+"\u20B9 "+disamount);

                                            double netamount= Double.parseDouble(amount) - Double.parseDouble(disamount);

                                            et_net_amount.setText("\u20B9 "+ netamount);

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

                error_showpop("No Internet Connection");

            }
        }




      }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_cash:
                if (checked)

                    ll_checque.setVisibility(View.GONE);
                    ll_online.setVisibility(View.GONE);
                    paymenttype="Cash";

                  clearcheque();
                  clearonline();

                    break;
            case R.id.radio_cheque:
                if (checked)

                  //  Toast.makeText(this, "Comming Soon", Toast.LENGTH_SHORT).show();

                    paymenttype="Cheque";
                    ll_checque.setVisibility(View.VISIBLE);
                    ll_online.setVisibility(View.GONE);
                    clearonline();

                    break;

            case R.id.radio_online:

                if (checked)

                  //  Toast.makeText(this, "Comming Soon", Toast.LENGTH_SHORT).show();

                    paymenttype="online";
                    ll_checque.setVisibility(View.GONE);
                    ll_online.setVisibility(View.VISIBLE);
                    clearcheque();
                    break;
        }
    }

    public void icrtypeclick(View view){

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_physical:
                if (checked)

                    icrstatus="false";
                    icrtype="physical";
                    tv_digital_icr_header.setVisibility(View.GONE);
                    tv_digital_icr_no.setVisibility(View.GONE);
                    tv_physical_icr_header.setVisibility(View.VISIBLE);
                    ll_physical_icr.setVisibility(View.VISIBLE);
                    ll_verifyicr.setVisibility(View.VISIBLE);
                    ll_digital_icr.setVisibility(View.GONE);
                    tv_digital_icr_error.setVisibility(View.GONE);
                     tv_icr_error.setVisibility(View.VISIBLE);



                break;
            case R.id.radio_digital:
                if (checked)
                    icrstatus="false";
                    icrtype="digital";
                tv_digital_icr_header.setVisibility(View.VISIBLE);
                tv_digital_icr_no.setVisibility(View.VISIBLE);
                ll_digital_icr.setVisibility(View.VISIBLE);
                tv_physical_icr_header.setVisibility(View.GONE);
                ll_physical_icr.setVisibility(View.GONE);
                ll_verifyicr.setVisibility(View.GONE);
                tv_digital_icr_error.setVisibility(View.VISIBLE);
                iv_digital_reload.setVisibility(View.GONE);
                tv_icr_error.setVisibility(View.GONE);

                System.out.println("RegionCode-->  "+RegionCode);

                GenerateDigitalICR digitalICR=new GenerateDigitalICR();

                showProgressDialog();


                digitalICR.getstring(new GenerateDigitalICR.VolleyCallback() {
                    @Override
                    public void onSuccess(String status,String result) {

                        try{

                            if (status.equals("true")) {

                                System.out.println(result);
                                hideProgressDialog();
                                JSONObject jsonObject = new JSONObject(result);
                                tv_digital_icr_error.setText(jsonObject.getString("Message"));

                                String statuss = jsonObject.getString("Status");
                                if (statuss.equals("true")) {

                                    String str = jsonObject.getString("Message");

                                    System.out.println("str-->"+str);

                                    str = str.replaceAll("\\D+", "");
                                    tv_digital_icr_no.setText(str);
                                    icrno = str;


                                    icrstatus = "true";
                                } else {
                                    iv_digital_reload.setVisibility(View.VISIBLE);
                                }
                            }
                            else {

                            }


                        }catch (Exception e){

                            e.printStackTrace();
                        }

                    }
                }, Create_customer.this,RegionCode,PartnerId);

                break;

        }

    }

    public void submitdata(View v){

       // uploadImage();

      //  Hidekeyboard.hideSoftKeyboard(Create_customer.this);

        altno=et_cust_alt_mobile.getText().toString();
        address=et_cust_new_address.getText().toString();

        if (icrtype.equals("physical")){
            icrno=et_icrno.getText().toString();
        }

        chequeno=et_chequeno.getText().toString();
        bankname=et_bankname.getText().toString();
        upino=et_upino.getText().toString();

        cust_new_landmark=et_cust_new_landmark.getText().toString();
        cust_new_more_address_details=et_cust_new_more_address_details.getText().toString();
        cust_new_street=et_cust_new_street.getText().toString();
        house_no=et_cust_new_house_no.getText().toString();

        if (altno.length()==0 || altno.length()==10){

           alt_status="success";
        }
        else {

           alt_status="false";

        }

        if (newaddressflag.equals("X")){

            if (cust_new_postal.length()!=6){

                Alertwithicon alertwithicon=new Alertwithicon();
                alertwithicon.showDialog(Create_customer.this,"Postal Code Empty ","Please enter 6 digit postal code",false);

                ads_status="false";

            }

            else if (area.length()==0){

                Alertwithicon alertwithicon=new Alertwithicon();
                alertwithicon.showDialog(Create_customer.this,"Area Section empty","Please Select Area",false);
                ads_status="false";

            }

            else if (cust_new_street.length()==0){

                Alertwithicon alertwithicon=new Alertwithicon();
                alertwithicon.showDialog(Create_customer.this,"Building Name/Street name empty","Please Enter Building Name/Street Name",false);

                ads_status="false";
            }

            else if (house_no.length()==0){

                Alertwithicon alertwithicon=new Alertwithicon();
                alertwithicon.showDialog(Create_customer.this,"House No Empty","Please Enter House No",false);

                ads_status="false";
            }

            else {

                ads_status="true";
            }

        }
        else{

            ads_status="true";

        }



        if (ads_status=="false"){


        }
        else if (serial_no.length() !=18){

            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(Create_customer.this,"Contract Type Details Empty","Please  Select Contract Type Details",false);

        }

        else if(contract_type_name.equals("Select Contract Type Details")){


            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(Create_customer.this,"Contract Type Details Empty","Please  Select Contract Type Details",false);


        }

        else if (GrossAmount.equals("0")){

            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(Create_customer.this,"Gross Amount Error ","Please Click On Amc Tab\n1.Select Amc type\n2. Select Contract Type Details",false);

        }

        else if (alt_status.equals("false")){

            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(Create_customer.this,"Alternate mobile number error","alternate mobile number always 10 digit",false);

        }

        else if (icrdate.length()==0){

            Alertwithicon alertwithicon=new Alertwithicon();
            alertwithicon.showDialog(Create_customer.this,"ICR Date Empty","ICR date has to be between 1st of current month to "+tss,false);

        }

        else if(icrtype.equals("physical")){

             if (icrno.length()==0){
                Alertwithicon alertwithicon=new Alertwithicon();
                alertwithicon.showDialog(Create_customer.this,"ICR Number Empty","Please verify ICR number",false);

            }

            else if (icrstatus.equals("false")){

                Alertwithicon alertwithicon=new Alertwithicon();
                alertwithicon.showDialog(Create_customer.this,"ICR Number error","Please verify ICR number",false);

            }

            else  if (paymenttype.equals("Cash")){

                createdata();

            }
             else if (paymenttype.equals("Cheque")){

                 if (chequeno.length()==0){

                     Alertwithicon alertwithicon=new Alertwithicon();
                     alertwithicon.showDialog(Create_customer.this,"Cheque No Blank","Please enter cheque No",false);

                 }

                 else if (chequedate.length()==0){

                     Alertwithicon alertwithicon=new Alertwithicon();
                     alertwithicon.showDialog(Create_customer.this,"Cheque Date Blank","Please enter cheque date",false);

                 }
                 else if (bankname.length()==0){

                     Alertwithicon alertwithicon=new Alertwithicon();
                     alertwithicon.showDialog(Create_customer.this,"Bank Name Blank","Please enter bank name",false);

                 }
                 else {

                     createdata();
                 }

             }


        }

        else if (icrtype.equals("digital")){

              if (icrstatus.equals("false")){

                Alertwithicon alertwithicon=new Alertwithicon();
                alertwithicon.showDialog(Create_customer.this,"ICR Number error","Please verify ICR number",false);

            }

            else  if (paymenttype.equals("Cash")){

                createdata();

            }

              else if (paymenttype.equals("Cheque")){

                  if (chequeno.length()==0){

                      Alertwithicon alertwithicon=new Alertwithicon();
                      alertwithicon.showDialog(Create_customer.this,"Cheque No Blank","Please enter cheque No",false);

                  }

                  else if (chequedate.length()==0){

                      Alertwithicon alertwithicon=new Alertwithicon();
                      alertwithicon.showDialog(Create_customer.this,"Cheque Date Blank","Please enter cheque date",false);

                  }
                  else if (bankname.length()==0){

                      Alertwithicon alertwithicon=new Alertwithicon();
                      alertwithicon.showDialog(Create_customer.this,"Bank Name Blank","Please enter bank name",false);

                  }
                  else {

                      createdata();
                  }

              }

        }



//        else if (paymenttype.equals("online")){
//
//
//            if (upino.length()==0){
//
//                Alertwithicon alertwithicon=new Alertwithicon();
//                alertwithicon.showDialog(Create_customer.this,"Enter UPI Ref No Blank","Please enter UPI Ref No",false);
//
//            }
//            else {
//                createdata();
//            }
//
//        }



        else {


//            Alertwithicon alertwithicon=new Alertwithicon();
//            alertwithicon.showDialog(Create_customer.this,"Please Goto Amc ","Please enter gross amount ",true);
       }
    }

    public void createdata(){
        try{

            jsonArray=new JSONArray();
            JSONObject object=new JSONObject();

           // not blank in crm
            object.put("customer_code",customer_id);
            object.put("customer_name",custname);
            object.put("rcn",cust_phone_no);
            object.put("altno",altno);
            object.put("email",cust_email);
            object.put("serial_no",serial_no);
            object.put("fg_product",fgcode);
            object.put("amc_purchased_date",renewdate);
            //frcode in dso code
            object.put("dso_code",FrCode);
            // contant type details
            object.put("amc_extended_wty",contract_type_name);
            // // contant type details
            object.put("description",contract_type_name);
            object.put("discount",disamount);

            // base price in sale_price
            object.put("sale_price",amount);
            object.put("total_tax",TaxAmount);
            object.put("gross_value",GrossAmount);
            object.put("cess","0");
            object.put("ICRNO",icrno);


            // ------- others value ------

            object.put("TechCode",PartnerId);
            object.put("PayMentMode",paymenttype);
            object.put("Status","");
            object.put("CreatedBy",PartnerId);

            object.put("chequeno",chequeno);
            object.put("chequedate",chequedate);
            object.put("bankname",bankname);
            object.put("upirefno",upino);

            object.put("newaddressflag",newaddressflag);
            object.put("house_no",house_no);
            object.put("pincode",cust_new_postal);
            object.put("state",cust_new_state);
            object.put("area_hou_flat_no",area);
            object.put("buld_street_name",cust_new_street);
            object.put("moreaddressdtls",cust_new_more_address_details);
            object.put("landmark",cust_new_landmark);
            object.put("city",cust_new_city);
            object.put("address",cust_address);
            object.put("Frcode",FrCode);

            Submitfinaldata(object);

            System.out.println("data_jsonobject--> "+object);

//
//            JSONObject json=new JSONObject();
//            json.put("AMCRequisition",object);
//
//            JSONArray jsonArrays=new JSONArray();
//
//            for (int i=0;i<2;i++){
//
//                if (i==0){
//
//                    JSONObject jsonObject=new JSONObject();
//                    jsonObject.put("RefId","");
//                    jsonObject.put("fileType","");
//                    jsonObject.put("fileRcrd",ICR_IMAGE_CAPTURE_data);
//                    jsonObject.put("file_name","ICR NO");
//                    jsonObject.put("CreatedBy",PartnerId);
//                    jsonArrays.put(jsonObject);
//                }
//
//
//                if (i==1){
//
//                    JSONObject jsonObject=new JSONObject();
//                    jsonObject.put("RefId","");
//                    jsonObject.put("fileType","");
//                    jsonObject.put("fileRcrd",CHECK_IMAGE_CAPTURE_data);
//                    jsonObject.put("file_name","");
//                    jsonObject.put("CreatedBy",PartnerId);
//                    jsonArrays.put(jsonObject);
//
//                }
//
//
//            }
//
//            JSONObject jsons=new JSONObject();
//
//            jsons.put("AMCRequisitionDatafile",jsonArrays);
//
//            jsonArray.put(json);
           // jsonArray.put(object);

 //System.out.println("AMC data-->"+jsonArray);

            data_json=object.toString();

            btn_amc_submit.setBackgroundColor(Color.GRAY);
            btn_amc_submit.setClickable(false);

//            System.out.println("data_jsonarray-->"+jsonArray);


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void Submitfinaldata(JSONObject jsonObject){


        if (CheckConnectivity.getInstance(this).isOnline()) {
            try {

                showProgressDialog();
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                final String mRequestBody = jsonObject.toString();

                System.out.println("body-->"+mRequestBody);
                String url=AllUrl.baseUrl+"sa/service-contract/creation";

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
                                alertwithicon.showDialog(Create_customer.this,"Data Submit Error","Please try again later",false);

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
                                       succesdialog(message);

                                   }
                               });



                              btn_amc_submit.setBackgroundColor(Color.GRAY);
                              btn_amc_submit.setClickable(false);

                              dbhelper.insert_amc_read_data(serial_no,cust_phone_no,altno,data_json);
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
              alertwithicon.showDialog(Create_customer.this,"No Internet Connection","Please check your internet Connection and try again",false);
        }

    }

    public void scan_serialno(View v) {

        Intent i = new Intent(Create_customer.this, SerialNoScan.class);
        startActivityForResult(i, SCANSERIALNO);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case (SCANSERIALNO):

                if (resultCode == RESULT_OK) {

                    String datas = data.getStringExtra("barcode");
                    if (!datas.equals("")) {

                        String srno = datas;

                        if (checkSerialNo(srno)) {
                            checkserialno(serial_no);
                        }

                    }

                }
                break;

            case ICR_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");

                    System.out.println("uri-->"+uri);

                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        // Bitmap converetdImage = getResizedBitmap(bitmap, 80);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                        ICR_IMAGE_CAPTURE_data= byteArrayOutputStream.toByteArray();

                        System.out.println("ICR_IMAGE_CAPTURE_data-->"+ICR_IMAGE_CAPTURE_data);
                        lv_camera_icr.setImageBitmap(bitmap);
                        imagestatus=true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;


            case CHECK_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");

                    System.out.println("uri-->"+uri);

                    try {


                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        // Bitmap converetdImage = getResizedBitmap(bitmap, 80);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                        CHECK_IMAGE_CAPTURE_data= byteArrayOutputStream.toByteArray();

                        lv_camera_check.setImageBitmap(bitmap);

                        imagestatus=true;


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void checkserialno(String srno) {

         if (CheckConnectivity.getInstance(this).isOnline()) {

             CheckSerialIsActive checkSerialIsActive = new CheckSerialIsActive();

             checkSerialIsActive.Checkserial(new CheckSerialIsActive.Serialresult() {
                 @Override
                 public void onsuccess(String status, String data) {

                     if (status.equals("true")) {


                           btn_amc_submit.setBackgroundColor(Color.GRAY);
                           btn_amc_submit.setClickable(false);


                     } else {

                         amc_serialno=srno;

                         new Checkserial(Create_customer.this, Create_customer.this).execute("");

                     }

                 }
             }, Create_customer.this, srno);

         }

     }

    private void launchCameraIntent(String type) {
        Intent intent = new Intent(Create_customer.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 16); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 9);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);

        if (type.equals("cheque")){
            startActivityForResult(intent, CHECK_IMAGE_CAPTURE);
        }
        else if(type.equals("icr")){
            startActivityForResult(intent, ICR_IMAGE_CAPTURE);
        }


    }

    public void openchequecamera(View view){

        launchCameraIntent("cheque");

    }

    public void openicrcamera(View view){

        launchCameraIntent("icr");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void  view_image(String type){

        // android.R.style.Theme_Black_NoTitleBar_Fullscreen
        // android.R.style.Theme_Black_NoTitleBar_Fullscreen
        final Dialog image_dialog = new Dialog(Create_customer.this);
        image_dialog.setContentView(R.layout.item_view_fillsizeimage);
        ImageView image_cancel;
        TextView textView;
        textView =image_dialog.findViewById(R.id.item_text_image);
        PhotoView item_image_view;
        image_cancel=image_dialog.findViewById(R.id.image_cancel);
        item_image_view=image_dialog.findViewById(R.id.item_image_view);

        image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_dialog.dismiss();
            }
        });

        try{

            if(CHECK_IMAGE_CAPTURE_data != null && type.equals("cheque")){

                textView.setText("Cheque Photo");

                Bitmap bmp = BitmapFactory.decodeByteArray(CHECK_IMAGE_CAPTURE_data, 0,CHECK_IMAGE_CAPTURE_data.length);
                item_image_view.setImageBitmap(bmp);

            }
            else if (ICR_IMAGE_CAPTURE_data!=null && type.equals("icr")){

                textView.setText("ICR No Photo");

                Bitmap bmp = BitmapFactory.decodeByteArray(ICR_IMAGE_CAPTURE_data, 0, ICR_IMAGE_CAPTURE_data.length);
                item_image_view.setImageBitmap(bmp);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        image_dialog.show();

    }

    private void updateLabel() {

        if (date_type.equals("icr")) {
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            tv_icr_date.setText(sdf.format(myCalendar.getTime()));
            icrdate = sdf.format(myCalendar.getTime());
            String myFormats = "yyyymmdd"; //In which you need put here
            SimpleDateFormat sdfs = new SimpleDateFormat(myFormat, Locale.US);
            renewdate = sdfs.format(myCalendar.getTime());
            // milestone=myCalendar.getTimeInMillis();

        }
        else if (date_type.equals("cheque")){


            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            tv_check_date.setText(sdf.format(myCalendar.getTime()));
            chequedate = sdf.format(myCalendar.getTime());
            String myFormats = "yyyymmdd"; //In which you need put here
            SimpleDateFormat sdfs = new SimpleDateFormat(myFormats, Locale.US);
            chequedates = sdfs.format(myCalendar.getTime());

        }

    }

    public void clearcheque(){

        et_chequeno.setText("");
        chequeno="";
        tv_check_date.setText("");
        chequedate="";
        bankname="";
        et_bankname.setText("");
    }

    public void clearonline(){

        upino="";
        et_upino.setText("");

    }

    @SuppressLint("StaticFieldLeak")
    public class Compressimage extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try {

                Thread.sleep(3000);
                imagebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                ByteArrayOutputStream byteArrayOutputStreams = new ByteArrayOutputStream();
                imagebitmap .compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStreams);

                ICR_IMAGE_CAPTURE_data= byteArrayOutputStreams.toByteArray();
                CHECK_IMAGE_CAPTURE_data=byteArrayOutputStreams.toByteArray();


            } catch (Exception e) {

            }
            return null;
        }
        protected void onPostExecute(Void v) {


        }
    }

    public void Getcustomerdetails(String cust_new_postal){

        String url=AllUrl.baseUrl+"wa/find-area?PinCode="+cust_new_postal;


        if (CheckConnectivity.getInstance(this).isOnline()) {

            // progressBar.setVisibility(View.VISIBLE);

            showProgressDialog();
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                //   progressBar.setVisibility(View.GONE);

                                hideProgressDialog();

                                System.out.println("get address -->"+response);
                                JSONArray jsonArray=new JSONArray(response);


                                if (jsonArray.length()==0){

                                    Alertwithicon alertwithicon=new Alertwithicon();
                                    alertwithicon.showDialog(Create_customer.this,"Postal Code Error","Postal Code is not valid. Please enter valid postal code",false);

                                    et_cust_new_state.setText("");
                                    et_cust_new_city.setText("");

                                }



                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    cust_new_state=obj.getString("State");
                                    cust_new_city=obj.getString("City");

                                    et_cust_new_state.setText(cust_new_state);
                                    et_cust_new_city.setText(cust_new_city);
                                    area_list.add(obj.getString("Area"));


                                 }

                                ArrayAdapter area_adapter = new ArrayAdapter(Create_customer.this, android.R.layout.simple_spinner_item, area_list);
                                area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //Setting the ArrayAdapter data on the Spinner
                                spnr_area.setAdapter(area_adapter);
                                spnr_area.setOnItemSelectedListener(Create_customer.this);


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


                            hideProgressDialog();
                            Alertwithicon alertwithicon=new Alertwithicon();
                            alertwithicon.showDialog(Create_customer.this,"Postal Code Error","Postal Code Not Found",false);

                            et_cust_new_state.setText("");
                            et_cust_new_city.setText("");

                        }
                    });

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    HashMap<String, String> headers = new HashMap<String, String>();
                   // headers.put("Bearer Token", "V0hBVFNBUFA6d2FVU0VS");

                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                    //return super.getHeaders();
                }
            };
// Add the request to the RequestQueue.

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);


        }

    }

    public void updatepin(View v){

    String postalcode= et_cust_new_postal.getText().toString();

    if(postalcode.length() == 6){

        area_list=new ArrayList<>();
        Getcustomerdetails(postalcode);
    }

    else {

        error_showpop("Please Enter Postal Code");

    }

    }

    private void uploadImage(){

        if (CheckConnectivity.getInstance(this).isOnline()) {
            String  upload_URL= "https://sapsecurity.ifbhub.com/amcDocs";

            System.out.println("upload_URL-->"+upload_URL);

          //  progressBar.setVisibility(View.VISIBLE);
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            rQueue.getCache().clear();

                            System.out.println(response.statusCode);

                          //  progressBar.setVisibility(View.GONE);

                            if (response.statusCode==200){


                            }
                            else {


                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            System.out.println("Image upload error");
                             }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    //model.AppDocUpload.
                    params.put("TicketNo",customer_id);
                    params.put("UploadDate",tss);
                    params.put("ClientDocs", "ClientDocs");
                    System.out.println(params);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();

                    // SerialPhoto
                    params.put("SerialPhoto", new DataPart("serialno_" +customer_id+ ".jpeg", ICR_IMAGE_CAPTURE_data));
                    params.put("ODUSerialPhoto", new DataPart("oduserialno_" + customer_id + ".jpeg", CHECK_IMAGE_CAPTURE_data));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(Create_customer.this);
            rQueue.add(volleyMultipartRequest);

        }
        else {

        }

    }


    public void showpop(String text){

        tv_error.setText(text);
        tv_error.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(Create_customer.this, R.anim.slide_down);
        tv_error.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error.setAnimation(null);
                tv_error.setVisibility(View.GONE);
                

            }
        }, 5000);
    }


    public void error_showpop(String text){

        tv_error.setText(text);
        tv_error.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(Create_customer.this, R.anim.slide_down);
        tv_error.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error.setAnimation(null);
                tv_error.setVisibility(View.GONE);


            }
        }, 5000);
    }


    // after scan serial no is valid or not
    public boolean checkSerialNo(String scanres){
        boolean suc;

        if (scanres.length()==18 && scanres.matches(validNumber)){
            suc=true;
        }
        else{

            suc=false;
            Alert alert=new Alert();
            alert.showDialog(Create_customer.this,"Serial no scan error !!!","Serial No Always 18 Digit. \nScan result - "+scanres +" \nsomething went wrong please try again");
        }

        return suc;
    }

    @Override
    public void modelresult(String output) {


        boolean b=output.contains("Result=1");
        boolean c=output.contains("Result=0");

        boolean d= output.contains("ProductCategory="+product);


        if (d){

            if (b){

                serial_no = amc_serialno;
                btn_amc_submit.setClickable(true);
                btn_amc_submit.setBackgroundColor(Color.RED);

            }
            else if(c){

                serial_no = amc_serialno;
                btn_amc_submit.setClickable(true);
                btn_amc_submit.setBackgroundColor(Color.RED);

            }
        }

        else {

            Alert alert=new Alert();
            alert.showDialog(Create_customer.this,"Serial no scan error !!!","Invalid Serial No");

            btn_amc_submit.setBackgroundColor(Color.GRAY);
            btn_amc_submit.setClickable(false);

        }
        }


        public void succesdialog(String msg) {
            final Dialog dialog = new Dialog(Create_customer.this);

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

                    startActivity(new Intent(Create_customer.this,MainActivity.class));

                }
            });

            dialog.show();
            dialog.setCancelable(false);


        }

}