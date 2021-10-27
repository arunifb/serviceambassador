package technician.ifb.com.ifptecnician.otherticket;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.AddProductadapter;
import technician.ifb.com.ifptecnician.adapter.Essential_add_adapter;
import technician.ifb.com.ifptecnician.adapter.Pending_item_adapter;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.Add_Product_Model;
import technician.ifb.com.ifptecnician.model.Add_item_model;
import technician.ifb.com.ifptecnician.model.Essential_add_model;
import technician.ifb.com.ifptecnician.model.PendingSpare;
import technician.ifb.com.ifptecnician.model.Spare_Pending_model;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;

import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.service.GenerateDigitalICR;
import technician.ifb.com.ifptecnician.service.GetSparePrice;
import technician.ifb.com.ifptecnician.serviceorder.SpareCodeList;
import technician.ifb.com.ifptecnician.serviceorder.SpareList;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.sohistory.Sohistory_adapter;
import technician.ifb.com.ifptecnician.sohistory.Sohistory_model;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

public class OtherTicketDetails extends AppCompatActivity {


    private static final String TAG = CustomerDetailsActivity.class.getSimpleName();
    static final int PICK_CONTACT = 2;
    String serialnourl = "", oduurl = "", invioceurl = "";
    String FrCode, FGProducts, MachinStatus = "";
    String Component, ComponentDescription, FGDescription, FGProduct, FrCodes, MaterialCategory, good_stock, refurbished_stock;
    Dbhelper dbhelper;
    String proces_type;
    String mobile_details;
    SharedPreferences sharedPreferences;
    //csv file create
    private RequestQueue rQueue;
    Cursor ess_cursor;
    TextView tv_calltype, tv_ticketno, tv_address, tv_callbook, tv_status, tv_rcnno, tv_servicetype, tv_custname;
    String TicketNo, Franchise, CallType, Status,
            Product, SerialNo = "", DOP = "00000000", DOI = "00000000", CallBookDate, AssignDate,
            CustomerCode, CustomerName, PinCode, TelePhone, RCNNo, ProblemDescription, PendingReason,
            Street, City, State, Address, ServiceType, ChangeDate, flagstatus = "", PostingDate = "01/01/2021 1:06:31 AM";
    public ArrayList<Add_item_model> models = new ArrayList<>();

    TextView tv_modelname, tv_dealername, tv_bom_qty, tv_part_code, tv_quentity;
    SessionManager sessionManager;
    String PartnerId = "", eta = "00000000", rechaeck_date = "00000000", spare_not_found = "";

    int bom_qty = 0;

    public Add_item_model model;
    SharedPreferences prefdetails;
    //Part section

    List<String> itemcodelist;
    List<String> itemlist;

    //        Button btn_savepart;

    String quentity = "1";
    ListView lv_additem, lv_pending_item;
    String partname, partcode;
    Map<String, String> mappartcode;

    public PendingSpare pendingSpare;
    public ArrayList<PendingSpare> pendingSpares = new ArrayList<>();
    String CodeGroup, RegionCode = "";
    //customer details
    TextView tv_addresss, tv_TelePhone, tv_pending_status, tv_tel, tv_ageing, tv_escal, tv_problemdescription;
    ImageView iv_iv_priorit, iv_alt_call;
    Button btn_openmap;
    //Machine Details
    TextView tv_Product, tv_Model, tv_SerialNo, tv_MachinStatus, tv_DOP, tv_DOI, tv_problem_details;
    String datestatus, oduserno = "", dop, doi;

    LottieAnimationView scan_serialno;
    final Calendar myCalendar = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();

    TextView tv_serialno_capturetime, tv_oduserialno_capturetime;

    //Amc detaills
    LinearLayout ll_amc_view;
    TextView tv_amcno, tv_amcdate, tv_amcduration;

    TextView tv_fgproduct, ess_tv_shelflife;

    //TAb layout
    LinearLayout ll_customer, ll_spare, ll_essential, ll_sales, ll_machine, ll_status;
    ImageView iv_info, iv_part, iv_ess, iv_machine, iv_status;
    TextView t_cus, t_mach, t_spare, t_essen, t_status,  tv_amount;

    //View section
    LinearLayout ll_details, ll_part, ll_esential,  ll_machine_details, ll_status_details;

    //essential
    List<String> essspinerlist;
    Map<String, String> map_ess_code;

    double service_amount = 0.0, spare_amoont = 0.0, total_amount = 0.0;
    TextView tv_ess_sqty;
    int accstock = 0, addstock = 0, ess_stock = 0;

    // Button btn_scan_ecc,ess_btn_add;
    TextView tv_ess_component, tv_ess_quentity;
    public ArrayList<Essential_add_model> essential_add_models = new ArrayList<>();
    public Essential_add_model essential_add_model;
    Essential_add_adapter essential_add_adapter;
    String essComponentDescription, essComponent, ItemType;
    ListView lv_ess_add_item;
    LinearLayout ll_add_ess, ll_scan_ess;

    //status

    String rescheduledate;

    CardView cv_bill_section;

    Button btn_status;
    String str_spnr_status;
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";

    String remarkurl;
    SearchableSpinner spnr_remark;
    List<String> remarklist;
    List<String> waterlist;
    Map<String, String> remarkmap;
    String remark, remarkcode;

    TextView tv_sdate, tv_stime;
    String sdate = "00000000", stime = "000000";
    LinearLayout ll_select_date,  ll_add_details, ll_change;

    TextView tv_select_date, tv_select_time;

    //notes
    Spinner spnr_product, spnr_machine, spnr_member, spnr_residence;
    Switch aSwitch;

    String product_name, machine_name, years, switch_status;

    EditText et_contact;
    HashMap<String, String> map_residence = new HashMap<>();
    HashMap<String, String> map_machine = new HashMap<>();
    ListView lv_product;
    ArrayList<Add_Product_Model> add_product_models = new ArrayList<>();

    Add_Product_Model add_product_model;
    AddProductadapter addProductadapter;
    String  tss;
    List<String> All_click = new ArrayList<>();
    ErrorDetails errorDetails;
    String mobile_no;
    TextView tv_odu_ser_no;
    LottieAnimationView btn_scan_odu_serial_no;
    ImageView  view_invioce, iv_message, iv_call,
            view_serialno, view_oduserialno;

    byte[] seraialno_image_data = null, oduserialno_image_data = null, invoice_image_data = null;

    LinearLayout ll_serial_no_image, ll_odu_serial_no_image, ll_invioce_no_image;

    //image upload layout
    SharedPreferences ratingperf;
    Bitmap imagebitmap;
    Boolean imagestatus = false;
    Button btn_updateaddress;
    //essential value
    String name, shelflife = "", essuses = "1/day", ticketdetails, dbversion = "", Medium = "";
    Spinner spnr_ess_value;
    TextView tv_ess_calculation, tv_error_message, tv_sign, tv_sohistory, tv_rating,tv_current_status;
    LinearLayout  lv_custsign;

    ProgressBar progressBar;
    LinearLayout ll_medium;
    ImageView img_map, iv_customersign;
    RecyclerView so_recyclerView;
    Sohistory_model sohistory_model;
    List<Sohistory_model> sohistory_models;
    Sohistory_adapter sohistory_adapter;
    // tds details

    String icrstatus = "true";
    long milestone = System.currentTimeMillis();

    TextView  tv_spare_amount, tv_tot_amount;
    LinearLayout ll_service_charge_view;
    int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_ticket_details);
        prefdetails = getSharedPreferences("details", 0);
        ratingperf = getSharedPreferences("rating", 0);
        dbhelper = new Dbhelper(getApplicationContext());

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            dbversion = pInfo.versionName;
            System.out.println("versionname--->" + dbversion);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        errorDetails = new ErrorDetails();
        sharedPreferences = getSharedPreferences(AllUrl.MOBILE_DETAILS, 0);
        mobile_details = sharedPreferences.getString("Mobile_details", "");
        tss = Valuefilter.getdate();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // ------------------------- get user session value --------------------------
        sessionManager = new SessionManager(getApplicationContext());
        try {

            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId = user.get(SessionManager.KEY_PartnerId);
            FrCode = user.get(SessionManager.KEY_FrCode);
            mobile_no = user.get(SessionManager.KEY_mobileno);
            RegionCode = user.get(SessionManager.KEY_RegionCode);
            FirebaseCrashlytics.getInstance().setCustomKey("mobile", mobile_no);
            name = user.get(SessionManager.KEY_Name);

            getprofile();

        } catch (Exception e) {

            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);

        }

        progressBar = findViewById(R.id.taskProcessing);

        init();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();


    }

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

            FirebaseCrashlytics.getInstance().recordException(e);

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

        }

    };


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case (PICK_CONTACT):
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                    Cursor cursor = getContentResolver().query(uri, projection,
                            null, null, null);
                    cursor.moveToFirst();
                    int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String numbe = cursor.getString(numberColumnIndex);

                    int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String name = cursor.getString(nameColumnIndex);

                    String number = numbe.replace(" ", "");
                    Log.d(TAG, "ZZZ number : " + number + " , name : " + name);
                    String lastten;
                    if (number.length() > 10 || number.length() == 10) {
                        lastten = number.substring(number.length() - 10);
                        et_contact.setText(lastten);
                    } else {

                        showerror("Invaid Mobile No !");

                        //  Toast.makeText(this, "Invaid Mobile No", Toast.LENGTH_SHORT).show();
                    }

                }
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void getpendingdata() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            //  progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            //  String url= AllUrl.baseUrl+"spares/getspare?spareaccadd.Ticketno=2002374814&spareaccadd.Status=Pending&spareaccadd.ItemType=ZACC";
            String url = AllUrl.baseUrl + "spares/getspare?spareaccadd.Ticketno=" + TicketNo;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject object = new JSONObject(response);

                                String status = object.getString("Status");
                                if (status.equals("true")) {
                                    // JSONArray jsonArray=new JSONArray(response);
                                    JSONArray jsonArray = object.getJSONArray("Data");
                                    System.out.println(jsonArray.toString());

                                    dbhelper.delete_pending_spare(TicketNo);
                                    boolean pstatus= dbhelper.insert_pening_spare(TicketNo, jsonArray.toString());
                                    System.out.println("pstatus--> "+pstatus);
                                    if (pstatus){
                                        update_pending_spare();
                                    }

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                FirebaseCrashlytics.getInstance().recordException(e);


                            }

                            // progressBar.setVisibility(View.GONE);
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            queue.add(stringRequest);

        }
        else {
            // showerror("No internet Connection");
            update_pending_spare();
        }
    }

    public String Parsedate(String time) {
        String inputPattern = "dd-MMM-yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
            // System.out.println(str);
        } catch (ParseException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return str;
    }


    public void callwhatsapp() {

        String wno = et_contact.getText().toString();

        if (wno.length() == 10) {

            String contact = "+91 " + wno; // use country code with your phone number
            String url = "https://api.whatsapp.com/send?phone=" + contact;
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {


                showerror("Whatsapp app not installed in your phone");
                e.printStackTrace();

            }

        } else {

            showerror("Please Enter valid Mobile No");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.productdetailsmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_video_call) {
            openpopup();
            // startActivity(new Intent(CustomerDetailsActivity.this, ContactlistActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openpopup() {

        final Dialog dialog = new Dialog(OtherTicketDetails.this);

        dialog.setContentView(R.layout.videocalldialog);
        // Set dialog title
        dialog.setTitle("Whatsapp Call");

        et_contact = dialog.findViewById(R.id.vdialog_et_mobile);
        ImageView iv_no = dialog.findViewById(R.id.vdialog_iv_getcontact);
        Button btn_call = dialog.findViewById(R.id.vdialog_btn_call);

        iv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT);

            }
        });

        dialog.show();

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callwhatsapp();
            }
        });


    }

    public String processtype(String ptype) {

        String processtype = "";

        switch (ptype) {

            case "SERVICE CALL":
                processtype = "ZSER";
                break;
            case "INSTALLATION CALL":
                processtype = "ZINT";
                break;
            case "MANDATORY CALLS":
                processtype = "ZMAN";
                break;
            case "REINSTALLLATION ORDER":
                processtype = "ZRIN";
                break;
            case "COURTESY VISIT":
                processtype = "ZFOC";
                break;

            case "DEALER DEFECTIVE REQUEST":
                processtype = "ZDD";
                break;
            case "REWORK ORDER":
                processtype = "ZREW";
                break;
            default:
                break;
        }
        return processtype;
    }

    public boolean check_part_In_List(String partcode) {

        long status = 0;

        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).getDescription().equals(partcode)) {
                status = 1;

                showerror("Spare Already Added");

                //   alert.showDialog(CustomerDetailsActivity.this, "Alert", "Spare Already Added Please Change Quantity");

                break;
            }
        }

        for (int j = 0; j < pendingSpares.size(); j++) {

            if (pendingSpares.get(j).getPartCode().equals(partcode)) {

                if (pendingSpares.get(j).getFlags().equals("D")) {

                } else {

                    status = 1;

                    showerror("Please Click on Delete Icon Then Add Same Spare");
                    //  alert.showDialog(CustomerDetailsActivity.this,"Alert ","Please Click on Delete Icon Then Add Same Spare");

                }

                break;
            }
        }
        if (status > 0) {

            return true;
        }

        return false;
    }

    public boolean checkesscode(String esscode) {

        long status = 0;

        for (int i = 0; i < essential_add_models.size(); i++) {
            if (essential_add_models.get(i).getEcode().equals(esscode)) {

                status = 1;
                break;
            }
        }
        if (status > 0) {

            return true;
        }
        return false;
    }

    public int string_to_int(String Numbers) {


        if (Numbers.equals("")) {

            return 0;
        } else {

            int no = 0;
            try {


                no = Integer.parseInt(Numbers);
                return no;

            } catch (Exception e) {

                e.printStackTrace();

                return 0;
            }
        }

    }

    // ---------------------------  declear all layout --------------------------------------
    public void init() {


        tv_error_message = findViewById(R.id.tv_error_message);
        tv_ess_calculation = findViewById(R.id.tv_ess_calculation);

        lv_additem = findViewById(R.id.lv_add_item);
        spnr_remark = findViewById(R.id.spnr_remark);
        ll_details = findViewById(R.id.ll_details);
        ll_part = findViewById(R.id.ll_part);
        ll_esential = findViewById(R.id.ll_esential);

        ll_machine_details = findViewById(R.id.ll_machine_details);
        ll_status_details = findViewById(R.id.ll_status_details);
        tv_bom_qty = findViewById(R.id.tv_bom_qty);
        ess_tv_shelflife = findViewById(R.id.ess_tv_shelflife);
        tv_addresss = findViewById(R.id.tv_addresss);
        iv_iv_priorit = findViewById(R.id.iv_priorit);
        iv_alt_call = findViewById(R.id.iv_alt_call);
        iv_call = findViewById(R.id.iv_call);
        tv_escal = findViewById(R.id.tv_escal);
        tv_problemdescription = findViewById(R.id.tv_problemdescription);
        tv_calltype = findViewById(R.id.tv_calltype);
        tv_ticketno = findViewById(R.id.tv_ticketno);
        tv_address = findViewById(R.id.tv_address);
        tv_callbook = findViewById(R.id.tv_callbookdate);
        tv_status = findViewById(R.id.tv_status);
        tv_rcnno = findViewById(R.id.tv_rcnno);
        tv_servicetype = findViewById(R.id.tv_servicetype);
        tv_custname = findViewById(R.id.tv_custname);
        tv_TelePhone = findViewById(R.id.tv_telePhone);
        tv_current_status=findViewById(R.id.tv_current_status);
        tv_pending_status = findViewById(R.id.tv_pending_status);

        tv_sdate = findViewById(R.id.tv_sdate);
        tv_stime = findViewById(R.id.tv_stime);
        tv_ageing = findViewById(R.id.tv_ageing);
        tv_tel = findViewById(R.id.tv_tel);

        btn_openmap = findViewById(R.id.btn_openmap);

        tv_part_code = findViewById(R.id.tv_part_code);
        tv_quentity = findViewById(R.id.tv_quentity);

        //Machine details
        tv_Product = findViewById(R.id.tv_product);
        tv_Model = findViewById(R.id.tv_model);
        tv_SerialNo = findViewById(R.id.tv_serial_no);
        tv_MachinStatus = findViewById(R.id.tv_machine_status);
        tv_DOP = findViewById(R.id.tv_dop);
        tv_DOI = findViewById(R.id.tv_doi);
        ll_amc_view = findViewById(R.id.ll_amc_view);
        tv_amcno = findViewById(R.id.tv_amcno);
        tv_amcdate = findViewById(R.id.tv_amcdate);
        tv_amcduration = findViewById(R.id.tv_amcduration);
        tv_problem_details = findViewById(R.id.tv_problem_details);

        btn_scan_odu_serial_no = findViewById(R.id.btn_scan_odu_serial_no);
        scan_serialno = findViewById(R.id.btn_scan_serial_no);
        tv_fgproduct = findViewById(R.id.tv_fgproduct);
        cv_bill_section = findViewById(R.id.cv_bill_section);

        // et_status = findViewById(R.id.et_status);
        btn_status = findViewById(R.id.btn_status);

        tv_select_date = findViewById(R.id.tv_select_date);
        tv_select_time = findViewById(R.id.tv_select_time);

        ll_select_date = findViewById(R.id.ll_select_date);

        ll_customer = findViewById(R.id.ll_customer);

        ll_spare = findViewById(R.id.ll_spare);


        ll_essential = findViewById(R.id.ll_essential);


        ll_sales = findViewById(R.id.ll_sales);

        ll_machine = findViewById(R.id.ll_machine);

        ll_status = findViewById(R.id.ll_status);


        iv_info = findViewById(R.id.iv_info);
        iv_part = findViewById(R.id.iv_part);
        iv_ess = findViewById(R.id.iv_ess);

        iv_machine = findViewById(R.id.iv_machine);
        iv_status = findViewById(R.id.iv_status);

        t_cus = findViewById(R.id.t_customer);
        t_mach = findViewById(R.id.t_machine);
        t_spare = findViewById(R.id.t_spare);
        t_essen = findViewById(R.id.t_essential);
        t_status = findViewById(R.id.t_status);

        tv_amount = findViewById(R.id.tv_amount);
        tv_spare_amount = findViewById(R.id.tv_spare_amount);
        tv_tot_amount = findViewById(R.id.tv_tot_amount);

        // textView.setTextColor(getResources().getColor(R.color.errorColor));
        t_cus.setTextColor(getResources().getColor(R.color.apptextcolor));
        t_mach.setTextColor(getResources().getColor(R.color.black));
        t_spare.setTextColor(getResources().getColor(R.color.black));
        t_essen.setTextColor(getResources().getColor(R.color.black));
        t_status.setTextColor(getResources().getColor(R.color.black));

        iv_info.setBackgroundResource(R.drawable.account_blue);
        iv_part.setBackgroundResource(R.drawable.ic_download_black);
        iv_ess.setBackgroundResource(R.drawable.ic_essential_black);

        iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
        iv_status.setBackgroundResource(R.drawable.ic_status_black);

        // tab  view section

        ll_details.setVisibility(View.VISIBLE);
        ll_part.setVisibility(View.GONE);
        ll_esential.setVisibility(View.GONE);

        ll_machine_details.setVisibility(View.GONE);
        ll_status_details.setVisibility(View.GONE);
        ll_change = findViewById(R.id.ll_change);

        lv_ess_add_item = findViewById(R.id.lv_ess_add_item);
        tv_ess_quentity = findViewById(R.id.tv_ess_quentity);

        ll_add_ess = findViewById(R.id.ll_add_ess);

        ll_add_details = findViewById(R.id.ll_add_details);

        spnr_product = findViewById(R.id.spnr_product);
        spnr_machine = findViewById(R.id.spnr_machine);
        spnr_member = findViewById(R.id.spnr_member);
        spnr_residence = findViewById(R.id.spnr_residence);
        aSwitch = findViewById(R.id.simpleswitch);
        lv_product = findViewById(R.id.lv_product_list);

        tv_ess_component = findViewById(R.id.tv_ess_component);
        tv_ess_sqty = findViewById(R.id.tv_ess_sqty);

        lv_pending_item = findViewById(R.id.lv_pending_item);

        tv_odu_ser_no = findViewById(R.id.tv_odu_ser_no);
        view_invioce = findViewById(R.id.view_invioce);
        view_serialno = findViewById(R.id.view_serialno);
        view_oduserialno = findViewById(R.id.view_oduserialno);
        iv_message = findViewById(R.id.iv_message);
        ll_serial_no_image = findViewById(R.id.ll_serial_no_image);
        ll_odu_serial_no_image = findViewById(R.id.ll_odu_serial_no_image);
        ll_invioce_no_image = findViewById(R.id.ll_invioce_no_image);
        tv_rating = findViewById(R.id.tv_rating);
        btn_updateaddress = findViewById(R.id.btn_updateaddress);

        ll_medium = findViewById(R.id.ll_medium);
        spnr_ess_value = findViewById(R.id.spnr_ess_value);

        img_map = findViewById(R.id.img_map);
        tv_serialno_capturetime = findViewById(R.id.tv_serialno_capturetime);

        tv_serialno_capturetime.setText("hh:mm (00:00)");
        tv_sign = findViewById(R.id.tv_custsign);

        lv_custsign = findViewById(R.id.lv_custsign);

        iv_customersign = findViewById(R.id.imgv_custsign);
        tv_modelname = findViewById(R.id.tv_modelname);
        tv_dealername = findViewById(R.id.tv_dealername);


        sohistory_models = new ArrayList<>();
        so_recyclerView = findViewById(R.id.lv_so_history);
        sohistory_adapter = new Sohistory_adapter(this, sohistory_models);
        so_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        so_recyclerView.setLayoutManager(mLayoutManager);
        so_recyclerView.setItemAnimator(new DefaultItemAnimator());
        so_recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        so_recyclerView.setAdapter(sohistory_adapter);
        tv_sohistory = findViewById(R.id.tv_sohistory);


        gethistory();


        //  set all value
        update_All_TextBox_Value();

//       compress no imege value



    }

    public void gethistory() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            RequestQueue queue = Volley.newRequestQueue(this);
            progressBar.setVisibility(View.VISIBLE);
            //
            String sourl = AllUrl.baseUrl+"ServiceOrder/History?model.custcode=" + CustomerCode + "&model.productCode=" + Product;

            // System.out.println("sourl"+sourl);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, sourl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //   System.out.println("remark-->"+response);
                            progressBar.setVisibility(View.GONE);

                            try {

                                // System.out.println("soresponse-->"+response);
                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    sohistory_model = new Sohistory_model();
                                    sohistory_model.setCallBookDate(obj.getString("CallBookDate"));
                                    sohistory_model.setProblemDescription(obj.getString("ProblemDescription"));
                                    sohistory_model.setTicketNo(obj.getString("TicketNo"));
                                    sohistory_models.add(sohistory_model);

                                }

                                so_recyclerView.setVisibility(View.VISIBLE);

                                sohistory_adapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                                // FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    tv_sohistory.setVisibility(View.VISIBLE);


                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }



    // -----------------   change layout as per button  click

    public void ActiveCustomerTab() {

        ll_details.setVisibility(View.VISIBLE);
        ll_part.setVisibility(View.GONE);
        ll_esential.setVisibility(View.GONE);

        ll_machine_details.setVisibility(View.GONE);
        ll_status_details.setVisibility(View.GONE);

        iv_info.setBackgroundResource(R.drawable.account_blue);
        t_cus.setTextColor(getResources().getColor(R.color.apptextcolor));
        iv_part.setBackgroundResource(R.drawable.ic_download_black);
        iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
        iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
        iv_status.setBackgroundResource(R.drawable.ic_status_black);

        t_mach.setTextColor(getResources().getColor(R.color.black));
        t_spare.setTextColor(getResources().getColor(R.color.black));
        t_essen.setTextColor(getResources().getColor(R.color.black));
        t_status.setTextColor(getResources().getColor(R.color.black));

        hideSoftKeyboard(OtherTicketDetails.this);

    }

    public void ActiveProductTab() {
        iv_machine.setBackgroundResource(R.drawable.ic_machine_blue);
        t_mach.setTextColor(getResources().getColor(R.color.apptextcolor));

        ll_details.setVisibility(View.GONE);
        ll_part.setVisibility(View.GONE);
        ll_esential.setVisibility(View.GONE);

        ll_machine_details.setVisibility(View.VISIBLE);
        ll_status_details.setVisibility(View.GONE);

        iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);
        iv_part.setBackgroundResource(R.drawable.ic_download_black);
        iv_ess.setBackgroundResource(R.drawable.ic_essential_black);

        iv_status.setBackgroundResource(R.drawable.ic_status_black);
        t_cus.setTextColor(getResources().getColor(R.color.black));

        t_spare.setTextColor(getResources().getColor(R.color.black));
        t_essen.setTextColor(getResources().getColor(R.color.black));
        t_status.setTextColor(getResources().getColor(R.color.black));

        hideSoftKeyboard(OtherTicketDetails.this);
    }

    public void ActiveSpareTab() {

        iv_part.setBackgroundResource(R.drawable.ic_download);
        t_spare.setTextColor(getResources().getColor(R.color.apptextcolor));

        ll_details.setVisibility(View.GONE);
        ll_part.setVisibility(View.VISIBLE);
        ll_esential.setVisibility(View.GONE);
        ll_machine_details.setVisibility(View.GONE);
        ll_status_details.setVisibility(View.GONE);

        iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);

        iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
        iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
        iv_status.setBackgroundResource(R.drawable.ic_status_black);

        t_cus.setTextColor(getResources().getColor(R.color.black));
        t_mach.setTextColor(getResources().getColor(R.color.black));

        t_essen.setTextColor(getResources().getColor(R.color.black));
        t_status.setTextColor(getResources().getColor(R.color.black));

        hideSoftKeyboard(OtherTicketDetails.this);


        if (FGProducts.equals("")) {

            showerror("Spare List Not Found");
        }
    }

    public void ActiveEssTab() {


        ll_details.setVisibility(View.GONE);
        ll_part.setVisibility(View.GONE);
        ll_esential.setVisibility(View.VISIBLE);

        ll_machine_details.setVisibility(View.GONE);
        ll_status_details.setVisibility(View.GONE);

        iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);
        iv_part.setBackgroundResource(R.drawable.ic_download_black);
        iv_ess.setBackgroundResource(R.drawable.ic_essential_blue);
        iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
        iv_status.setBackgroundResource(R.drawable.ic_status_black);

        t_cus.setTextColor(getResources().getColor(R.color.black));
        t_mach.setTextColor(getResources().getColor(R.color.black));
        t_spare.setTextColor(getResources().getColor(R.color.black));
        t_essen.setTextColor(getResources().getColor(R.color.apptextcolor));
        t_status.setTextColor(getResources().getColor(R.color.black));

        hideSoftKeyboard(OtherTicketDetails.this);
    }

    public void ActiveNotesPage() {


        ll_details.setVisibility(View.GONE);
        ll_part.setVisibility(View.GONE);
        ll_esential.setVisibility(View.GONE);

        ll_machine_details.setVisibility(View.GONE);
        ll_status_details.setVisibility(View.GONE);

        iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);
        iv_part.setBackgroundResource(R.drawable.ic_download_black);
        iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
        iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
        iv_status.setBackgroundResource(R.drawable.ic_status_black);

        t_cus.setTextColor(getResources().getColor(R.color.black));
        t_mach.setTextColor(getResources().getColor(R.color.black));
        t_spare.setTextColor(getResources().getColor(R.color.black));
        t_essen.setTextColor(getResources().getColor(R.color.black));
        t_status.setTextColor(getResources().getColor(R.color.black));

        hideSoftKeyboard(OtherTicketDetails.this);

    }

    public void ActiveStatusPage() {

        ll_details.setVisibility(View.GONE);
        ll_machine_details.setVisibility(View.GONE);
        ll_status_details.setVisibility(View.VISIBLE);
        ll_part.setVisibility(View.GONE);
        ll_esential.setVisibility(View.GONE);

        iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);
        iv_part.setBackgroundResource(R.drawable.ic_download_black);
        iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
        iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
        iv_status.setBackgroundResource(R.drawable.ic_status_blue);

        t_cus.setTextColor(getResources().getColor(R.color.black));
        t_mach.setTextColor(getResources().getColor(R.color.black));
        t_spare.setTextColor(getResources().getColor(R.color.black));
        t_essen.setTextColor(getResources().getColor(R.color.black));
        t_status.setTextColor(getResources().getColor(R.color.apptextcolor));

    }

    // update all value in to  layout

    public void update_All_TextBox_Value() {

        try {


            // getting value from share pref
            prefdetails = getSharedPreferences("details", 0);
            ticketdetails = prefdetails.getString("details", "");

              System.out.println("details"+ticketdetails);

            JSONObject object1 = new JSONObject(ticketdetails);

            TicketNo = object1.getString("TicketNo");

            tv_modelname.setText(object1.getString("Model"));
            tv_Model.setText(object1.getString("Model"));
            tv_dealername.setText(object1.getString("DealerName"));
            String exchange = object1.getString("potential_exchange_flag");
            tv_current_status.setText(object1.getString("Status"));

            All_click.add("TicketNo%20" + TicketNo);
            CallType = object1.getString("CallType");
            Street = object1.getString("Street");
            City = object1.getString("City");
            State = object1.getString("State");
            DOP = Parsedate(object1.getString("DOP"));
            Product = object1.getString("Product");
            SerialNo = object1.getString("serial_no");
            Medium = object1.getString("Medium");

            PostingDate = object1.getString("PostingDate");

            if (Medium.equals("Phone") || Medium.equals("CSR Portal")) {

                ll_medium.setVisibility(View.VISIBLE);
            }

            if (DOP.equals("19000101")) {

                DOP = "00000000";
            }

            DOI = Parsedate(object1.getString("DOI"));

            if (DOI.equals("19000101")) {

                DOI = "00000000";
            }
            CustomerCode = object1.getString("CustomerCode");

            FGProducts = object1.getString("FGCode");

            tv_fgproduct.setText(FGProducts);

            ChangeDate = object1.getString("ChangeDate");
            Address = object1.getString("Address");
            PinCode = object1.getString("PinCode");
            CallBookDate = object1.getString("CallBookDate");


//       update calender for dop and doi
            // updatecalender();


            Status = object1.getString("Status");
            Franchise = object1.getString("Franchise");
            AssignDate = object1.getString("AssignDate");

            SimpleDateFormat formats = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
            Date dts = formats.parse(AssignDate);
            DateFormat dayfrmat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
            DateFormat maothfor = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
            DateFormat timef = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String finalDay = dayfrmat.format(dts);
            String mon = maothfor.format(dts);
            String times = timef.format(dts);
            tv_sdate.setText(finalDay + "  " + mon);
            tv_stime.setText(times);

            if (object1.getString("TelePhone").equals("")) {
                iv_alt_call.setVisibility(View.GONE);

            } else {
                tv_tel.setText(object1.getString("TelePhone"));
                TelePhone = object1.getString("TelePhone");
            }

            // if user click on alt. mobile no
            iv_alt_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     // open calling apps
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + TelePhone));// Initiates the Intent
                    startActivity(intent);

                }
            });

            tv_ageing.setText("/" + object1.getString("Ageing") + "  Days");

            String serial_no = object1.getString("serial_no");

            if (serial_no.length() == 0) {

                tv_SerialNo.setText("000000000000000000");
            } else {

                tv_SerialNo.setText(object1.getString("serial_no"));
            }



            getpendingdata();


            RCNNo = object1.getString("RCNNo");
            ServiceType = object1.getString("ServiceType");
            CustomerName = object1.getString("CustomerName");

            String priorit = object1.getString("Priority");

            if (priorit.equals("")) {
                tv_escal.setText("");
            } else {
                tv_escal.setText("Escalate ");
            }


            ProblemDescription = object1.getString("ProblemDescription");
            if (!ProblemDescription.equals("")) {

                tv_problemdescription.setText(object1.getString("ProblemDescription"));
            } else {


            }

            tv_calltype.setText(CallType.substring(0, 1));

            proces_type = processtype(CallType);
            tv_ticketno.setText(TicketNo);
            PinCode = object1.getString("PinCode");

            String fulladress = Address.replace("\\s+", "");
            tv_addresss.setText(fulladress + " , " + PinCode);

            tv_callbook.setText(CallBookDate);


            tv_status.setText(Status);
            tv_rcnno.setText(RCNNo);
            tv_servicetype.setText(ServiceType);
            tv_custname.setText(CustomerName);

            // if customer mobile no is null hode call button

            if (RCNNo.equals("")) {

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


            PendingReason = object1.getString("PendingReason");

            if (!PendingReason.equals("")) {

                tv_pending_status.setText(object1.getString("PendingReason") + " " + object1.getString("scheduledate"));
            } else {
                tv_pending_status.setVisibility(View.GONE);

            }

            Product = object1.getString("Product");
            String product = object1.getString("Product");

            product_name = object1.getString("Product");


            if (product.equals("AC")) {

                tv_modelname.setText("Air Conditioner");
                updateproduct("Air Conditioner", product_name);
            } else if (product.equals("CD")) {
                tv_modelname.setText("Clothes Dryer");
                updateproduct("Clothes Dryer", product_name);

            } else if (product.equals("DW")) {
                tv_modelname.setText("Dish Washer");
                updateproduct("Dish Washer", product_name);


            } else if (product.equals("IND")) {
                tv_modelname.setText("Industrial Dish washer");
                updateproduct("Industrial Dish washer", product_name);

            } else if (product.equals("KA")) {
                tv_modelname.setText("Kitchen Appliances");
                updateproduct("Kitchen Appliances", product_name);

            } else if (product.equals("RF")) {
                tv_modelname.setText("Refrigerator");
                updateproduct("Refrigerator", product_name);

            } else if (product.equals("ST")) {
                tv_modelname.setText("Stabilizer");
                updateproduct("Stabilizer", product_name);

            } else if (product.equals("WD")) {
                tv_modelname.setText("Washer Dryer");
                updateproduct("Washer Dryer", product_name);


            } else if (product.equals("WM")) {
                tv_modelname.setText("Washing Machine");

                updateproduct("Washing Machine", product_name);



            } else if (product.equals("WP")) {
                tv_modelname.setText("Water Purifier");
                updateproduct("Water Purifier", product_name);
            } else if (product.equals("MW")) {
                tv_modelname.setText("Microwave Oven");
                updateproduct("Microwave Oven", product_name);

            } else {

                tv_modelname.setText(product);
                tv_Product.setText(object1.getString("Product"));

            }

            //  tv_Model.setText(object1.getString("Model"));

            MachinStatus = object1.getString("MachinStatus");

            // update machine status

            if (MachinStatus.equals("SW")) {
                tv_MachinStatus.setText("Warranty");
                ll_amc_view.setVisibility(View.GONE);
            } else if (MachinStatus.equals("EW")) {
                tv_MachinStatus.setText("Ex-Warranty");
                ll_amc_view.setVisibility(View.GONE);
            } else if (MachinStatus.equals("OG")) {
                tv_MachinStatus.setText("Out of Warranty");
                ll_amc_view.setVisibility(View.GONE);



            }
            // if machine is in Amc then get amc details

            else if (MachinStatus.equals("AMC")) {

                tv_MachinStatus.setText("AMC");
                getamcdetails(SerialNo);
                ll_amc_view.setVisibility(View.VISIBLE);

            } else {

                tv_MachinStatus.setText(MachinStatus);
                ll_amc_view.setVisibility(View.GONE);
            }

            tv_DOP.setText(object1.getString("DOP"));
            tv_DOI.setText(object1.getString("DOI"));
            oduserno = object1.getString("odu_ser_no");
            tv_odu_ser_no.setText(oduserno);
            tv_problem_details.setText("");

        } catch (Exception e) {

            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);


        }

        // getting all pevois image

        if (Status.equals("Closed") || Status.equals("Cancelled")) {

            tv_rcnno.setVisibility(View.GONE);
            tv_addresss.setVisibility(View.GONE);
            btn_openmap.setVisibility(View.GONE);
            iv_alt_call.setVisibility(View.GONE);
            iv_call.setVisibility(View.GONE);
            tv_tel.setVisibility(View.GONE);

        }




        getuploaddoc();
        updatevalue();


    }


    public void updatevalue() {


        if (Status.equals("Resolved (soft closure)")) {

            if (SerialNo.length() == 0) {

                try {

                    String serial = dbhelper.getserialno(TicketNo, "serial");
                    tv_SerialNo.setText(serial);


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            if (Product.equals("AC")) {

                if (oduserno.length() == 0) {

                    try {

                        String oduno = dbhelper.getserialno(TicketNo, "odu");
                        tv_odu_ser_no.setText(oduno);
                        //SerialNo=oduno;

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }
            }
        }

    }


    public void openGoolgemap() {

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Street + "," + City + "," + State + "," + PinCode + "");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }


    // check any prevois spare is deleted or not
    public void check_delete_spare_list() {

        for (int pres = 0; pres < pendingSpares.size(); pres++) {

            try {

                if (pendingSpares.get(pres).getFlags().equals("D")) {
                    String Spare = pendingSpares.get(pres).getPartCode();
                    String Quentity = pendingSpares.get(pres).getQty();
                    String itemno = (pendingSpares.get(pres).getItemno());
                    String Itemtype = (pendingSpares.get(pres).getItemType());
                    String Status = (pendingSpares.get(pres).getStatus());
                    upload_deleted_spare(Spare, Quentity, itemno, Itemtype, Status, "", "");
                }

            } catch (Exception e) {

                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }

    }

    // upload deleted spare in to server
    public void upload_deleted_spare(String Spare, String Quentity, String itemno, String Itemtype, String Status, String Accessssories, String Additive) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            String url = AllUrl.baseUrl + "spareEssetial/update?model.sapreeEssential.OrderId=" + TicketNo
                    + "&model.sapreeEssential.Spare=" + Spare
                    + "&model.sapreeEssential.Accessssories=" + Accessssories
                    + "&model.sapreeEssential.Additive=" + Additive
                    + "&model.sapreeEssential.Status=" + Status
                    + "&model.sapreeEssential.Itemtype=" + Itemtype
                    + "&model.sapreeEssential.itemno=" + itemno
                    + "&model.sapreeEssential.Quentity=" + Quentity;


            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest postRequest = new StringRequest(POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

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

            String url = AllUrl.baseUrl + "spareEssetial/update?model.sapreeEssential.OrderId=" + TicketNo
                    + "&model.sapreeEssential.Spare=" + Spare
                    + "&model.sapreeEssential.Accessssories=" + Accessssories
                    + "&model.sapreeEssential.Additive=" + Additive
                    + "&model.sapreeEssential.Status=" + Status
                    + "&model.sapreeEssential.Itemtype=" + Itemtype
                    + "&model.sapreeEssential.itemno=" + itemno
                    + "&model.sapreeEssential.Quentity=" + Quentity;

            Boolean sta = dbhelper.insert_offline_deletespare_data(TicketNo, url, tss);

        }
    }

    // view iamge in popup with zoom
    public void view_image(String type) {

        // android.R.style.Theme_Black_NoTitleBar_Fullscreen
        // android.R.style.Theme_Black_NoTitleBar_Fullscreen
        final Dialog image_dialog = new Dialog(OtherTicketDetails.this);
        image_dialog.setContentView(R.layout.item_view_fillsizeimage);
        ImageView image_cancel;
        TextView textView;
        textView = image_dialog.findViewById(R.id.item_text_image);
        PhotoView item_image_view;
        image_cancel = image_dialog.findViewById(R.id.image_cancel);
        item_image_view = image_dialog.findViewById(R.id.item_image_view);

        image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_dialog.dismiss();
            }
        });

        try {

            if ( type.equals("serialno")) {

                textView.setText("Serial No Photo");

                if (!serialnourl.equals("")) {


                    Picasso.get()
                            .load(serialnourl)

                            .into(item_image_view);


                }
//                else {
//
//                    Bitmap bmp = BitmapFactory.decodeByteArray(seraialno_image_data, 0, seraialno_image_data.length);
//                    item_image_view.setImageBitmap(bmp);
//
//                }


            }

          //  else if (oduserialno_image_data != null && type.equals("oduserialno")) {

            else if ( type.equals("oduserialno")) {

                textView.setText("ODU Serial No Photo");

                if (!oduurl.equals("")) {

                    Picasso.get()
                            .load(oduurl)

                            .into(item_image_view);


                }
//                else {
//                    Bitmap bmp = BitmapFactory.decodeByteArray(oduserialno_image_data, 0, oduserialno_image_data.length);
//                    item_image_view.setImageBitmap(bmp);
//                }
            }
            // else if (invoice_image_data != null && type.equals("invioce")) {

            else if ( type.equals("invioce")) {
                textView.setText("Invoice Photo");

                if (!invioceurl.equals("")) {

                    Picasso.get()
                            .load(invioceurl)

                            .into(item_image_view);
                } else {

                    Bitmap bmp = BitmapFactory.decodeByteArray(invoice_image_data, 0, invoice_image_data.length);
                    item_image_view.setImageBitmap(bmp);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        image_dialog.show();

    }


    //  ----------------------------- get pending reason from dbhelper --------------------------------
    public void getdatabygroupcode(String code) {
        //  System.out.println(code);

        try {
            Cursor c_pendingReasons;

            c_pendingReasons = dbhelper.fetch_pending_reasone(code);

            if (c_pendingReasons != null) {

                if (c_pendingReasons.moveToFirst()) {

                    do {

                        final int recordid = c_pendingReasons.getInt(c_pendingReasons.getColumnIndex("recordid"));
                        String Codegroup = c_pendingReasons.getString(c_pendingReasons.getColumnIndex("Codegroup"));
                        String Data = c_pendingReasons.getString(c_pendingReasons.getColumnIndex("Data"));

                        try {
                            JSONArray jsonArray = new JSONArray(Data);
                            remarklist = new ArrayList<>();
                            remarkmap = new HashMap<>();

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject rmobj = jsonArray.getJSONObject(i);
                                CodeGroup = rmobj.getString("CodeGroup");
                                String Code = rmobj.getString("Code");
                                String Reason = rmobj.getString("Reason");
                                remarklist.add(Reason);
                                remarkmap.put(Reason, Code);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(OtherTicketDetails.this, android.R.layout.simple_spinner_dropdown_item, remarklist);
                            spnr_remark.setAdapter(arrayAdapter);
                            // spnr_remark.setSelection(0,false);

                        } catch (Exception e) {
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);

                        }

                    } while (c_pendingReasons.moveToNext());

                    c_pendingReasons.close();

                }
            } else {


                showerror("Recheck Reason Not Fount");

            }
        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }


    //  --------------------- get prevois image from server  --------------------------------------
    public void getuploaddoc() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = AllUrl.baseUrl + "uploadedDocByApp/getdoc?model.AppDocUpload.TicketNo=" + TicketNo;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {

                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    serialnourl = obj.getString("SerialPhoto");
                                    oduurl = obj.getString("ODUSerialPhoto");
                                    invioceurl = obj.getString("InvoicePhto");

                                    Picasso.get()
                                            .load(serialnourl)
                                            .into(view_serialno);


                                    Picasso.get()
                                            .load(oduurl)
                                            .into(view_oduserialno);


                                    Picasso.get()
                                            .load(invioceurl)

                                            .into(view_invioce);

                                    getimagedata(TicketNo);

                                }


                            } catch (Exception e) {


                                e.printStackTrace();
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            getimagedata(TicketNo);
                        }
                    });


                }
            });


            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        } else {

            getimagedata(TicketNo);

        }
    }

    // -------------------- check essential added or not (Submit time check essential add or not) --------------
    public boolean check_essential_empty() {

        boolean success;
        if (essential_add_models.size() == 0) {

            success = true;
        } else {
            success = false;
        }
        return success;
    }

    // ------------------------- open dialog if user not added any essentrial  ------------------------

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }



//  ------------- calculation shelf life date as per essential usage -------------------------


    public void getcal() {

        if (!shelflife.equals("") && !shelflife.equals("0") && !shelflife.equals("As per usage/Standard")) {

            String enddate = Valuefilter.Enddate(shelflife, essuses, quentity);
            tv_ess_calculation.setText(enddate);
        } else {

        }

    }


    //  --------------------------  for show any type of message ----------------------------
    public void showerror(String message) {
        tv_error_message.setVisibility(View.VISIBLE);

        tv_error_message.setText(message);

        Animation animation;
        animation = AnimationUtils.loadAnimation(OtherTicketDetails.this, R.anim.slide_down);
        tv_error_message.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                tv_error_message.setAnimation(null);
                tv_error_message.setVisibility(View.GONE);

            }
        }, 5000);   //5 seconds
    }


    // ------------------------ geting amc details if machine status in amc ---------------------------
    public void getamcdetails(String srno) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = AllUrl.baseUrl + "AMC/getAmcDetailsBySerialNo?model.amc.Srno=" + srno;

            // System.out.println("get all sales-->  " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressBar.setVisibility(View.GONE);

                            // System.out.println("ess details-->" + response);
                            if (response == null) {

                            } else {

                                try {

                                    JSONObject object = new JSONObject(response);

                                    String status = object.getString("Status");

                                    if (status.equals("true")) {

                                        String data = object.getString("Data");
                                        JSONArray array = new JSONArray(data);

                                        for (int i = 0; i < 1; i++) {

                                            JSONObject jsonObject = array.getJSONObject(i);

                                            String AMCType = jsonObject.getString("AMCType");
                                            String AMC_end_dat = jsonObject.getString("AMC_end_dat");
                                            String AMCNo = jsonObject.getString("AMCNo");

                                            tv_amcno.setText(AMCNo);
                                            tv_amcdate.setText(AMC_end_dat);
                                            tv_amcduration.setText(AMCType);

                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    FirebaseCrashlytics.getInstance().recordException(e);
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {

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

    public void updateproduct(String name, String product) {

        tv_Product.setText(name + " (" + product + ")");

    }



    public void updatecalender() {

        try {
            SimpleDateFormat spf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
            Date newDate = null;
            try {
                newDate = spf.parse(PostingDate);
            } catch (ParseException e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }

            spf = new SimpleDateFormat("dd", Locale.ENGLISH);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM", Locale.ENGLISH);
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            String dates = spf.format(newDate);
            String months = dateFormat.format(newDate);
            String years = yearFormat.format(newDate);
            int year = Integer.parseInt(years);
            int month = Integer.parseInt(months);
            int day = Integer.parseInt(dates);

            calendar2.set(year, month - 1, day);

            milestone = calendar2.getTimeInMillis();


        } catch (Exception e) {

            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbhelper.close();
    }



    public void clodeupdateicrlayout() {

        ll_service_charge_view.setVisibility(View.GONE);

    }


    public void getprofile() {

        if (CheckConnectivity.getInstance(this).isOnline()) {


            RequestQueue queue = Volley.newRequestQueue(this);
            String url = AllUrl.baseUrl+"sa-profile/getProfile/?user.Mobileno=" + mobile_no;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                System.out.println("Response-->" + response);


                                String status = jsonObject.getString("Status");

                                if (status.equals("true")) {

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    if (jsonArray.length() == 1) {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject obj = jsonArray.getJSONObject(i);

                                            PartnerId = obj.getString("PartnerId");
                                            if (PartnerId.equals("")) {


                                                showProfileerror("PartnerId Not Found. Please Login Again");

                                            } else {


                                            }

                                        }
                                    } else {
                                        showProfileerror("Multiple Account Linked. Please Login Again");

                                        // startActivity(new Intent(CustomerDetailsActivity.this,LoginActivity.class));
                                    }

                                } else {

                                    showProfileerror("No Valid Account Found. Please Login Again");

                                    //  startActivity(new Intent(CustomerDetailsActivity.this,LoginActivity.class));
                                }


                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {


        }

    }

    //  --------------------------  for show any type of message ----------------------------

    public void showProfileerror(String message) {
        tv_error_message.setVisibility(View.VISIBLE);

        tv_error_message.setText(message);

        Animation animation;
        animation = AnimationUtils.loadAnimation(OtherTicketDetails.this, R.anim.slide_down);
        tv_error_message.startAnimation(animation);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                tv_error_message.setAnimation(null);
                tv_error_message.setVisibility(View.GONE);

                dbhelper.deletebom();
                dbhelper.deleteess();
                dbhelper.deletetask();
                // dbhelper.delete_readtask();
                dbhelper.delete_added_spare();
                dbhelper.delete_added_ess();
                dbhelper.delete_reqcancel_data();
                dbhelper.delete_softclose_data();
                dbhelper.delete_nr_data();
                dbhelper.delete_pending_data();
                dbhelper.delete_assign_data();
                sessionManager.logoutUser();
                finishAffinity();

            }
        }, 5000);   //5 seconds
    }



    private void dispatchTakePictureIntent(int resuest) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, resuest);
        } catch (ActivityNotFoundException e) {
            // display error state to the user

            e.printStackTrace();
        }
    }

    public void getimagedata(String Ticketno) {


        try {

            Cursor image_cursor;
            image_cursor = dbhelper.get_offline_image_data(Ticketno);

            if (image_cursor != null) {

                if (image_cursor.moveToFirst()) {

                    //    Ticketno TEXT, Status TEXT, Serialno TEXT, DOP TEXT , DOI TEXT , Icr_date TEXT, Icr_no TEXT, Feedback TEXT, Member TEXT, House TEXT, Re_date TEXT, Re_time TEXT, Chnge_status TEXT, Pend_res TEXT,Pend_causes TEXT );";

                    do {

                        try {

                            imagestatus = true;
                            seraialno_image_data = image_cursor.getBlob(image_cursor.getColumnIndex("Serialnoimage"));
                            oduserialno_image_data = image_cursor.getBlob(image_cursor.getColumnIndex("Oduserialnoimage"));
                            invoice_image_data = image_cursor.getBlob(image_cursor.getColumnIndex("Invioceimage"));
                            Bitmap bmp = BitmapFactory.decodeByteArray(seraialno_image_data, 0, seraialno_image_data.length);
                            view_serialno.setImageBitmap(bmp);
                            Bitmap odu_bmp = BitmapFactory.decodeByteArray(oduserialno_image_data, 0, oduserialno_image_data.length);
                            view_oduserialno.setImageBitmap(odu_bmp);
                            Bitmap invce_bmp = BitmapFactory.decodeByteArray(invoice_image_data, 0, invoice_image_data.length);
                            view_invioce.setImageBitmap(invce_bmp);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } while (image_cursor.moveToNext());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void icrtypeclick(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_physical:
                if (checked)

                  icrstatus = "false";

                break;

            case R.id.radio_digital:
                if (checked)

                icrstatus = "true";

                GenerateDigitalICR digitalICR = new GenerateDigitalICR();

                showProgressDialog();

                digitalICR.getstring(new GenerateDigitalICR.VolleyCallback() {
                    @Override
                    public void onSuccess(String stas, String result) {

                        try {

                            System.out.println(result);

                            if (stas.equals("true")) {
                                hideProgressDialog();
                                JSONObject jsonObject = new JSONObject(result);


                                String status = jsonObject.getString("Status");
                                if (status.equals("true")) {

                                    String str = jsonObject.getString("Message");

                                    System.out.println("str-->" + str);
                                    str = str.replaceAll("\\D+", "");

                                    icrstatus = "true";

                                } else {

                                }
                            } else {

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                }, OtherTicketDetails.this, RegionCode, PartnerId);

                break;
        }
    }

    public void getspareprice() {

        spare_amoont = 0;
        double price = 0;
        for (int i = 0; i < models.size(); i++) {

            System.out.println("spare status  -- > " + models.get(i).getCheck());

            if (models.get(i).getCheck().equals("No")) {

                String totl = models.get(i).getTotal_price();
                price = price + Double.parseDouble(totl);
            }
            spare_amoont = price;
            tv_spare_amount.setText("" + price);
        }

        double pendingprice = 0;

        for (int j = 0; j < pendingSpares.size(); j++) {

            if (pendingSpares.get(j).getPending_fla().equals("No")) {

                pendingprice = pendingprice + Double.parseDouble(pendingSpare.getTotalprice());

            }
        }

        spare_amoont = pendingprice + spare_amoont;
        total_amount = service_amount + spare_amoont;
        tv_tot_amount.setText("" + total_amount);
        tv_spare_amount.setText("" + spare_amoont);

    }

    public void update_pending_spare() {

        try {

            Cursor pcursor;

            pcursor = dbhelper.get_pending_spare(TicketNo);

            if (pcursor != null && pcursor.getCount() >0) {

                if (pcursor.moveToFirst()) {

                    //    Ticketno TEXT, Status TEXT, Serialno TEXT, DOP TEXT , DOI TEXT , Icr_date TEXT, Icr_no TEXT, Feedback TEXT, Member TEXT, House TEXT, Re_date TEXT, Re_time TEXT, Chnge_status TEXT, Pend_res TEXT,Pend_causes TEXT );";

                    do {

                        try {

                            String json = pcursor.getString(pcursor.getColumnIndex("sparedetails"));


                            JSONArray jsonArray = new JSONArray(json);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject essobj = jsonArray.getJSONObject(i);
                                pendingSpare = new PendingSpare();

                                if (essobj.getString("ItemType").equals("ZSPP")) {

                                    pendingSpare.setItemType(essobj.getString("ItemType"));
                                    pendingSpare.setStatus(essobj.getString("Status"));
                                    pendingSpare.setItemno(essobj.getString("itemno"));
                                    pendingSpare.setETA(essobj.getString("ETA"));
                                    pendingSpare.setPartCode(essobj.getString("PartCode"));
                                    pendingSpare.setPartName(essobj.getString("PartName"));
                                    pendingSpare.setQty(essobj.getString("Qty"));
                                    pendingSpare.setFlags("U");
                                    pendingSpare.setChanges("");
                                    String penflag = essobj.getString("pending_fla");

                                    if (penflag.equals("N")) {
                                        pendingSpare.setPending_fla("No");

                                        GetSparePrice getSparePrice = new GetSparePrice();

                                        getSparePrice.SpareAmount(new GetSparePrice.Spareprice() {
                                            @Override
                                            public void onsuccess(String status, String result) {


                                                if (status.equals("true")) {
                                                    pendingSpare.setPrice(result);
                                                    try {
                                                        pendingSpare.setTotalprice("" + Double.parseDouble(result) * Double.parseDouble(essobj.getString("Qty")));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {

                                                    pendingSpare.setPrice("0");
                                                    pendingSpare.setTotalprice("0");

                                                }


                                            }
                                        }, OtherTicketDetails.this, Product, essobj.getString("PartCode"));
                                    } else if (penflag.equals("X")) {

                                        pendingSpare.setPending_fla("Yes");
                                        pendingSpare.setPrice("0");
                                        pendingSpare.setTotalprice("0");
                                    } else if (penflag.equals("")) {

                                        pendingSpare.setPending_fla("No");

                                        GetSparePrice getSparePrice = new GetSparePrice();

                                        getSparePrice.SpareAmount(new GetSparePrice.Spareprice() {
                                            @Override
                                            public void onsuccess(String status, String result) {


                                                if (status.equals("true")) {
                                                    pendingSpare.setPrice(result);
                                                    try {
                                                        pendingSpare.setTotalprice("" + Double.parseDouble(result) * Double.parseDouble(essobj.getString("Qty")));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {

                                                    pendingSpare.setPrice("0");
                                                    pendingSpare.setTotalprice("0");

                                                }
                                            }
                                        }, OtherTicketDetails.this, Product, essobj.getString("PartCode"));
                                    }
                                    pendingSpares.add(pendingSpare);

                                } else {

                                            essential_add_model=new Essential_add_model();
                                            essential_add_model.setEname(essobj.getString("EssName")+""+essobj.getString("AccName"));
                                            essential_add_model.setEcode(essobj.getString("EssCode")+""+essobj.getString("AccCode"));
                                            essential_add_model.setEquentity(essobj.getString("Qty"));
                                            essential_add_model.setItemtype(essobj.getString("ItemType"));
                                            essential_add_model.setFlag("U");
                                            essential_add_models.add(essential_add_model);

                                }
                            }

                                    essential_add_adapter = new Essential_add_adapter(essential_add_models, OtherTicketDetails.this);
                                    essential_add_adapter.notifyDataSetChanged();
                                    lv_ess_add_item.setAdapter(essential_add_adapter);

                            Parcelable state = lv_pending_item.onSaveInstanceState();
                            Pending_item_adapter pending_item_adapter = new Pending_item_adapter(pendingSpares, OtherTicketDetails.this);
                            pending_item_adapter.notifyDataSetChanged();
                            lv_pending_item.setAdapter(pending_item_adapter);
                            lv_pending_item.onRestoreInstanceState(state);

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    } while (pcursor.moveToNext());


                }
            }
        }catch(Exception e){

            e.printStackTrace();
        }

    }


    public void opencustomer(View view){

        ActiveCustomerTab();
    }

    public void openproduct(View view){

        ActiveProductTab();
    }

    public void openspare(View view){

        ActiveSpareTab();
    }

    public void openessential(View view){

        ActiveEssTab();
    }


    public void  opensoftclose(View view){

        ActiveStatusPage();
    }


    public void viewinviocephoto(View view){

        view_image("invioce");
    }
    public void viewserialphoto(View view){

        view_image("serialno");
    }

    public void viewoduserialphoto(View view){

        view_image("oduserialno");
    }


}