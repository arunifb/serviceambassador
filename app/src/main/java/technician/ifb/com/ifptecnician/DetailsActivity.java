package technician.ifb.com.ifptecnician;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;

import android.telephony.SmsManager;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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

import com.google.android.material.tabs.TabLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import technician.ifb.com.ifptecnician.adapter.AddProductadapter;
import technician.ifb.com.ifptecnician.adapter.Add_item_adapter;
import technician.ifb.com.ifptecnician.adapter.DetailsCustomerHomeadapter;
import technician.ifb.com.ifptecnician.adapter.DetailsEssentialAdapter;
import technician.ifb.com.ifptecnician.adapter.DetailsSpareAdapter;
import technician.ifb.com.ifptecnician.adapter.Essential_add_adapter;
import technician.ifb.com.ifptecnician.adapter.Pending_item_adapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;

import technician.ifb.com.ifptecnician.fragment.adapter.DetailsAdapter;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.Add_Product_Model;
import technician.ifb.com.ifptecnician.model.Add_item_model;
import technician.ifb.com.ifptecnician.model.Details_Home_model;
import technician.ifb.com.ifptecnician.model.Details_ess_model;
import technician.ifb.com.ifptecnician.model.Details_spare_model;
import technician.ifb.com.ifptecnician.model.Essential_add_model;
import technician.ifb.com.ifptecnician.model.PendingSpare;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.NumberPickerDialog;
import technician.ifb.com.ifptecnician.utility.Valuefilter;
import technician.ifb.com.ifptecnician.utility.image.ZoomableImageView;

public class DetailsActivity extends AppCompatActivity implements
        View.OnClickListener {


    static final int PICK_CONTACT=2;
    String FrCode,FGProducts;
    String Component,ComponentDescription,FGDescription,FGProduct,FrCodes,MaterialCategory,good_stock,refurbished_stock;
    Dbhelper dbhelper;
    String proces_type;
    private static final String TAG = CustomerDetailsActivity.class.getSimpleName();

    //csv file create
    String csv ;//= Environment.getExternalStorageDirectory().getAbsolutePath() + "/techapp.csv";
    Cursor bom_cursor, ess_cursor,home_cursor,other_cursor,image_cursor;
    TextView tv_calltype, tv_ticketno, tv_address, tv_callbook, tv_status, tv_rcnno, tv_servicetype, tv_custname;
    String TicketNo,Branch,Franchise,CallType,Status,
            Product,Model,SerialNo,MachinStatus,DOP="00000000",DOI="00000000",CallBookDate,AssignDate,ClosedDate,CancelledDate,
            TechnicianCode,TechName,CustomerCode,CustomerName,PinCode,TelePhone,RCNNo,MobileNo,ProblemDescription,PendingReason,
            Street, City, State, Address, NO126, ServiceType, Email;
    public ArrayList<Details_spare_model> models = new ArrayList<Details_spare_model>();
    DetailsSpareAdapter add_item_adapter;
    SessionManager sessionManager;
    String PartnerId;
    TextView tv_bom_qty;
    CheckBox checkBox;
    ImageView view_serialno,view_odu_serialno_photo,view_invioce;
    public Details_spare_model model;

    SharedPreferences prefdetails;
    //Part section
    TextView tv_part_code;
    TextView tv_quentity;
    String quentity = "1";
    ListView lv_additem;
    LinearLayout ll_add_part, ll_scan_part;
    ListView lv_pending_item;
    public PendingSpare pendingSpare;
    String CodeGroup;
    //customer details
    TextView tv_mobile, tv_fax, tv_email, tv_website, tv_addresss,
            tv_Brachname, tv_Franchise, tv_PinCode, tv_TelePhone, tv_Street, tv_City, tv_State, tv_pending_status;
    TextView tv_tel,tv_ageing;
    ImageView iv_iv_priorit,iv_alt_call;
    TextView tv_escal,tv_problemdescription;
    Button btn_openmap;
    //Machine Details
    TextView tv_Product, tv_Model, tv_SerialNo,tv_odu_ser_no, tv_MachinStatus, tv_DOP, tv_DOI, tv_problem_details, tv_CallBookDate, tv_AssignDate;
    String datestatus,oduserno;
    ImageView iv_doi, iv_dop;
    LottieAnimationView scan_serialno;
    final Calendar myCalendar = Calendar.getInstance();
    //Amc detaills
    LinearLayout ll_amc_view;
    EditText et_whatsappno;
    ImageView iv_whatsapp;
    //TAb layout
    LinearLayout ll_customer, ll_spare, ll_essential, ll_sales, ll_machine, ll_status;
    ImageView iv_info, iv_part, iv_ess, iv_note, iv_machine, iv_status;
    TextView t_cus, t_mach, t_spare, t_essen, t_status, t_note;
    //View section
    LinearLayout ll_details, ll_part, ll_esential, ll_notes, ll_machine_details, ll_status_details;
    //essential
    TextView tv_ess_sqty;
    // Button btn_scan_ecc,ess_btn_add;
    TextView tv_ess_component, tv_ess_quentity;
    public ArrayList<Details_ess_model> essential_add_models = new ArrayList<>();
    public Details_ess_model essential_add_model;
    DetailsEssentialAdapter essential_add_adapter;
    String essComponentDescription, essComponent,ItemType;
    ListView lv_ess_add_item;
    LinearLayout ll_add_ess, ll_scan_ess;

    //status

    TextView et_icr_no;
    LinearLayout ll_icr_date;
    TextView tv_icr_date;
    String st_icr_no="";
    String st_icr_date="00000000";
    Spinner snpr_status;
    EditText et_status;
    Button btn_status;
    String spnr_status;
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";
    private Button btnClear, btnSave;
    private File file;
    private LinearLayout canvasLL;
    private View view;
    private Bitmap bitmap;
    ImageView iv_call;
    String remarkurl;
    SearchableSpinner spnr_remark;
    List<String> remarklist;
    Map<String,String> remarkmap;
    String remark,remarkcode;
    String barcodestatus = "";
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView tv_sdate,tv_stime;
    String sdate="",stime="";
    LinearLayout ll_select_date,ll_select_time;
    LinearLayout ll_change;
    TextView tv_select_date,tv_select_time;
    TextView tv_feedback,tv_member,tv_residence;
    TextView tv_pre_status,tv_remarks;
    Switch aSwitch;
    LinearLayout ll_add_details;
    EditText et_contact;
    ListView lv_product;
    ArrayList<Details_Home_model> add_product_models=new ArrayList<>();
    Details_Home_model add_product_model;
    DetailsCustomerHomeadapter addProductadapter;
    Button btn_offline_sync;
    Cursor offcursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefdetails = getSharedPreferences("details", 0);
        dbhelper = new Dbhelper(getApplicationContext());
        lv_ess_add_item = findViewById(R.id.lv_ess_add_item);
        lv_product=findViewById(R.id.lv_product_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sessionManager=new SessionManager(getApplicationContext());
        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            FrCode=user.get(SessionManager.KEY_FrCode);
        }
        catch (Exception e){
        }

        tv_feedback=findViewById(R.id.tv_feedback);
        tv_member=findViewById(R.id.tv_member);
        tv_residence=findViewById(R.id.tv_residence);

        tv_pre_status=findViewById(R.id.tv_pre_status);
        tv_remarks=findViewById(R.id.tv_remark);

        et_whatsappno=findViewById(R.id.et_whatsapp_no);
        iv_whatsapp=findViewById(R.id.iv_whatsapp);
        iv_whatsapp.setOnClickListener(this);


        lv_additem = (ListView) findViewById(R.id.lv_add_item);

        spnr_remark=findViewById(R.id.spnr_remark);
        ll_details = (LinearLayout) findViewById(R.id.ll_details);
        ll_part = (LinearLayout) findViewById(R.id.ll_part);
        ll_esential = (LinearLayout) findViewById(R.id.ll_esential);
        ll_notes = (LinearLayout) findViewById(R.id.ll_note);
        ll_machine_details = (LinearLayout) findViewById(R.id.ll_machine_details);
        ll_status_details = (LinearLayout) findViewById(R.id.ll_status_details);
        tv_bom_qty=findViewById(R.id.tv_bom_qty);

        //customer details
        tv_mobile = (TextView) findViewById(R.id.tv_mobile);
        tv_fax = (TextView) findViewById(R.id.tv_fax);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_website = (TextView) findViewById(R.id.tv_website);
        tv_addresss = (TextView) findViewById(R.id.tv_addresss);
        iv_iv_priorit = (ImageView) findViewById(R.id.iv_priorit);
        iv_alt_call=findViewById(R.id.iv_alt_call);
        iv_call = findViewById(R.id.iv_call);
        tv_escal=findViewById(R.id.tv_escal);
        tv_problemdescription=findViewById(R.id.tv_problemdescription);
        tv_calltype = (TextView) findViewById(R.id.tv_calltype);
        tv_ticketno = (TextView) findViewById(R.id.tv_ticketno);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_callbook = (TextView) findViewById(R.id.tv_callbookdate);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_rcnno = (TextView) findViewById(R.id.tv_rcnno);
        tv_servicetype = (TextView) findViewById(R.id.tv_servicetype);
        tv_custname = (TextView) findViewById(R.id.tv_custname);
        tv_Brachname = (TextView) findViewById(R.id.tv_brachname);
        tv_Franchise = (TextView) findViewById(R.id.tv_Franchise);

        tv_PinCode = (TextView) findViewById(R.id.tv_pincode);
        tv_TelePhone = (TextView) findViewById(R.id.tv_telePhone);
        tv_Street = (TextView) findViewById(R.id.tv_street);
        tv_City = (TextView) findViewById(R.id.tv_city);
        tv_State = (TextView) findViewById(R.id.tv_state);
        tv_pending_status = findViewById(R.id.tv_pending_status);

        tv_sdate=findViewById(R.id.tv_sdate);
        tv_stime=findViewById(R.id.tv_stime);
        tv_ageing=findViewById(R.id.tv_ageing);
        tv_tel=findViewById(R.id.tv_tel);
        btn_openmap=findViewById(R.id.btn_openmap);

        tv_part_code = (TextView) findViewById(R.id.tv_part_code);
        tv_quentity = (TextView) findViewById(R.id.tv_quentity);
        tv_quentity.setOnClickListener(this);

        ll_scan_part = findViewById(R.id.ll_scan_part);
        ll_scan_part.setOnClickListener(this);

        //Machine details
        tv_Product = findViewById(R.id.tv_product);
        tv_Model = findViewById(R.id.tv_model);
        tv_SerialNo = findViewById(R.id.tv_serial_no);
        tv_odu_ser_no=findViewById(R.id.tv_odu_ser_no);
        tv_MachinStatus = findViewById(R.id.tv_machine_status);
        tv_DOP = findViewById(R.id.tv_dop);
        tv_DOI = findViewById(R.id.tv_doi);
        ll_amc_view = findViewById(R.id.ll_amc_view);
        tv_problem_details = findViewById(R.id.tv_problem_details);

        iv_doi = findViewById(R.id.iv_doi);
        iv_dop = findViewById(R.id.iv_dop);
        scan_serialno = findViewById(R.id.btn_scan_serial_no);

        // status sction
        tv_icr_date=findViewById(R.id.tv_icr_date);
        et_icr_no=findViewById(R.id.et_icr_no);
        ll_icr_date=findViewById(R.id.ll_icr_date);
        ll_icr_date.setOnClickListener(this);
        snpr_status = findViewById(R.id.spnr_status);
        et_status = findViewById(R.id.et_status);
        btn_status = findViewById(R.id.btn_status);
        btn_status.setOnClickListener(this);
        tv_select_date=findViewById(R.id.tv_select_date);
        tv_select_time=findViewById(R.id.tv_select_time);
        String selectdate;
        String selecttime;

        ll_select_date=findViewById(R.id.ll_select_date);
        ll_select_date.setOnClickListener(this);
        ll_select_time=findViewById(R.id.ll_select_time);

        ll_change=findViewById(R.id.ll_change);

        btnClear = (Button) findViewById(R.id.btnclear);
//        btnSave = (Button) findViewById(R.id.btnsave);
        view = canvasLL;

        ll_customer = (LinearLayout) findViewById(R.id.ll_customer);
        ll_spare = (LinearLayout) findViewById(R.id.ll_spare);
        ll_essential = (LinearLayout) findViewById(R.id.ll_essential);
        ll_sales = (LinearLayout) findViewById(R.id.ll_sales);
        ll_machine = (LinearLayout) findViewById(R.id.ll_machine);
        ll_status = (LinearLayout) findViewById(R.id.ll_status);

        iv_info = (ImageView) findViewById(R.id.iv_info);
        iv_part = (ImageView) findViewById(R.id.iv_part);
        iv_ess = (ImageView) findViewById(R.id.iv_ess);
        iv_note = (ImageView) findViewById(R.id.iv_note);
        iv_machine = (ImageView) findViewById(R.id.iv_machine);
        iv_status = (ImageView) findViewById(R.id.iv_status);

        t_cus = (TextView) findViewById(R.id.t_customer);
        t_mach = (TextView) findViewById(R.id.t_machine);
        t_spare = (TextView) findViewById(R.id.t_spare);
        t_essen = (TextView) findViewById(R.id.t_essential);
        t_status = (TextView) findViewById(R.id.t_status);
        t_note = (TextView) findViewById(R.id.t_notes);

        // textView.setTextColor(getResources().getColor(R.color.errorColor));
        t_cus.setTextColor(getResources().getColor(R.color.apptextcolor));
        t_mach.setTextColor(getResources().getColor(R.color.black));
        t_spare.setTextColor(getResources().getColor(R.color.black));
        t_essen.setTextColor(getResources().getColor(R.color.black));
        t_status.setTextColor(getResources().getColor(R.color.black));
        t_note.setTextColor(getResources().getColor(R.color.black));

        iv_info.setBackgroundResource(R.drawable.account_blue);
        iv_part.setBackgroundResource(R.drawable.ic_download_black);
        iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
        iv_note.setBackgroundResource(R.drawable.ic_note_add_black_24dp);
        iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
        iv_status.setBackgroundResource(R.drawable.ic_status_black);

        // tab  view section

        ll_details.setVisibility(View.VISIBLE);
        ll_part.setVisibility(View.GONE);
        ll_esential.setVisibility(View.GONE);
        ll_notes.setVisibility(View.GONE);
        ll_machine_details.setVisibility(View.GONE);
        ll_status_details.setVisibility(View.GONE);

        // getbom();
        //essential


        tv_ess_component = findViewById(R.id.tv_ess_component);



        btn_offline_sync=findViewById(R.id.btn_offline_sync);

           offcursor =dbhelper.get_offlineurl_data();

        if (offcursor.getCount() == 0){
            btn_offline_sync.setVisibility(View.GONE);

        }

        else {
            btn_offline_sync.setVisibility(View.VISIBLE);
            btn_offline_sync.setOnClickListener(this);

        }

        tv_ess_sqty=findViewById(R.id.tv_ess_sqty);



        ll_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_details.setVisibility(View.VISIBLE);
                ll_part.setVisibility(View.GONE);
                ll_esential.setVisibility(View.GONE);
                ll_notes.setVisibility(View.GONE);
                ll_machine_details.setVisibility(View.GONE);
                ll_status_details.setVisibility(View.GONE);

                iv_info.setBackgroundResource(R.drawable.account_blue);
                t_cus.setTextColor(getResources().getColor(R.color.apptextcolor));
                iv_part.setBackgroundResource(R.drawable.ic_download_black);
                iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
                iv_note.setBackgroundResource(R.drawable.ic_note_add_black_24dp);
                iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
                iv_status.setBackgroundResource(R.drawable.ic_status_black);

                t_mach.setTextColor(getResources().getColor(R.color.black));
                t_spare.setTextColor(getResources().getColor(R.color.black));
                t_essen.setTextColor(getResources().getColor(R.color.black));
                t_status.setTextColor(getResources().getColor(R.color.black));
                t_note.setTextColor(getResources().getColor(R.color.black));

            }
        });

        ll_machine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iv_machine.setBackgroundResource(R.drawable.ic_machine_blue);
                t_mach.setTextColor(getResources().getColor(R.color.apptextcolor));

                ll_details.setVisibility(View.GONE);
                ll_part.setVisibility(View.GONE);
                ll_esential.setVisibility(View.GONE);
                ll_notes.setVisibility(View.GONE);
                ll_machine_details.setVisibility(View.VISIBLE);
                ll_status_details.setVisibility(View.GONE);

                iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);
                iv_part.setBackgroundResource(R.drawable.ic_download_black);
                iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
                iv_note.setBackgroundResource(R.drawable.ic_note_add_black_24dp);

                iv_status.setBackgroundResource(R.drawable.ic_status_black);
                t_cus.setTextColor(getResources().getColor(R.color.black));

                t_spare.setTextColor(getResources().getColor(R.color.black));
                t_essen.setTextColor(getResources().getColor(R.color.black));
                t_status.setTextColor(getResources().getColor(R.color.black));
                t_note.setTextColor(getResources().getColor(R.color.black));

            }
        });

        ll_spare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iv_part.setBackgroundResource(R.drawable.ic_download);
                t_spare.setTextColor(getResources().getColor(R.color.apptextcolor));

                ll_details.setVisibility(View.GONE);
                ll_part.setVisibility(View.VISIBLE);
                ll_esential.setVisibility(View.GONE);
                ll_notes.setVisibility(View.GONE);
                ll_machine_details.setVisibility(View.GONE);
                ll_status_details.setVisibility(View.GONE);

                iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);

                iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
                iv_note.setBackgroundResource(R.drawable.ic_note_add_black_24dp);
                iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
                iv_status.setBackgroundResource(R.drawable.ic_status_black);

                t_cus.setTextColor(getResources().getColor(R.color.black));
                t_mach.setTextColor(getResources().getColor(R.color.black));

                t_essen.setTextColor(getResources().getColor(R.color.black));
                t_status.setTextColor(getResources().getColor(R.color.black));
                t_note.setTextColor(getResources().getColor(R.color.black));



            }
        });

        ll_essential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ll_details.setVisibility(View.GONE);
                ll_part.setVisibility(View.GONE);
                ll_esential.setVisibility(View.VISIBLE);
                ll_notes.setVisibility(View.GONE);
                ll_machine_details.setVisibility(View.GONE);
                ll_status_details.setVisibility(View.GONE);

                iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);
                iv_part.setBackgroundResource(R.drawable.ic_download_black);
                iv_ess.setBackgroundResource(R.drawable.ic_essential_blue);
                iv_note.setBackgroundResource(R.drawable.ic_note_add_black_24dp);
                iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
                iv_status.setBackgroundResource(R.drawable.ic_status_black);

                t_cus.setTextColor(getResources().getColor(R.color.black));
                t_mach.setTextColor(getResources().getColor(R.color.black));
                t_spare.setTextColor(getResources().getColor(R.color.black));
                t_essen.setTextColor(getResources().getColor(R.color.apptextcolor));
                t_status.setTextColor(getResources().getColor(R.color.black));
                t_note.setTextColor(getResources().getColor(R.color.black));

            }
        });

        ll_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_details.setVisibility(View.GONE);
                ll_machine_details.setVisibility(View.GONE);
                ll_status_details.setVisibility(View.VISIBLE);
                ll_part.setVisibility(View.GONE);
                ll_esential.setVisibility(View.GONE);
                ll_notes.setVisibility(View.GONE);

                iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);
                iv_part.setBackgroundResource(R.drawable.ic_download_black);
                iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
                iv_note.setBackgroundResource(R.drawable.ic_note_add_black_24dp);
                iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
                iv_status.setBackgroundResource(R.drawable.ic_status_blue);

                t_cus.setTextColor(getResources().getColor(R.color.black));
                t_mach.setTextColor(getResources().getColor(R.color.black));
                t_spare.setTextColor(getResources().getColor(R.color.black));
                t_essen.setTextColor(getResources().getColor(R.color.black));
                t_status.setTextColor(getResources().getColor(R.color.apptextcolor));
                t_note.setTextColor(getResources().getColor(R.color.black));


            }
        });

        ll_sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_details.setVisibility(View.GONE);
                ll_part.setVisibility(View.GONE);
                ll_esential.setVisibility(View.GONE);
                ll_notes.setVisibility(View.VISIBLE);
                ll_machine_details.setVisibility(View.GONE);
                ll_status_details.setVisibility(View.GONE);

                iv_info.setBackgroundResource(R.drawable.account_circle_black_24dp);
                iv_part.setBackgroundResource(R.drawable.ic_download_black);
                iv_ess.setBackgroundResource(R.drawable.ic_essential_black);
                iv_note.setBackgroundResource(R.drawable.note_add_blue);
                iv_machine.setBackgroundResource(R.drawable.ic_machine_details);
                iv_status.setBackgroundResource(R.drawable.ic_status_black);

                t_cus.setTextColor(getResources().getColor(R.color.black));
                t_mach.setTextColor(getResources().getColor(R.color.black));
                t_spare.setTextColor(getResources().getColor(R.color.black));
                t_essen.setTextColor(getResources().getColor(R.color.black));
                t_status.setTextColor(getResources().getColor(R.color.black));
                t_note.setTextColor(getResources().getColor(R.color.apptextcolor));

            }
        });



        try {

            prefdetails = getSharedPreferences("details", 0);
            String details = prefdetails.getString("details", "");


            System.out.println(details);

            JSONObject object1 = new JSONObject(details);
            TicketNo = object1.getString("TicketNo");

            tv_SerialNo.setText(object1.getString("serial_no"));
            tv_MachinStatus.setText(object1.getString("MachinStatus"));
            DOP=Parsedate(object1.getString("DOP"));
            DOI=Parsedate(object1.getString("DOI"));
            tv_DOP.setText(object1.getString("DOP"));
            tv_DOI.setText(object1.getString("DOI"));
            getbom(TicketNo);

            getess(TicketNo);

            getimagedata(TicketNo);

            get_home_details(TicketNo);

            get_other_details(TicketNo);

            CallType = object1.getString("CallType");
            Street=object1.getString("Street");
            City=object1.getString("City");
            State=object1.getString("State");

            CustomerCode=object1.getString("CustomerCode");

            FGProducts=object1.getString("FGCode");
            Product=object1.getString("Product");

            if (FGProducts.equals("")){

            }
            else{
               // getBomdata(FrCode,Product,FGProducts);
            }


            if (CallType.equals("INSTALLATION CALL")){
                scan_serialno.setVisibility(View.VISIBLE);
                iv_dop.setVisibility(View.VISIBLE);
                iv_doi.setVisibility(View.VISIBLE);
            }
            else{
                scan_serialno.setVisibility(View.GONE);
                iv_dop.setVisibility(View.GONE);
                iv_doi.setVisibility(View.GONE);
            }


            Address = object1.getString("Address");
            PinCode=object1.getString("PinCode");
            CallBookDate = object1.getString("CallBookDate");
            Status = object1.getString("Status");
            Franchise=object1.getString("Franchise");
            AssignDate=object1.getString("AssignDate");

            SimpleDateFormat formats=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a",Locale.ENGLISH);
            Date dts=formats.parse(AssignDate);
            DateFormat dayfrmat=new SimpleDateFormat("EEEE",Locale.ENGLISH);
            DateFormat maothfor=new SimpleDateFormat("MMM dd yyyy",Locale.ENGLISH);
            DateFormat timef=new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
            String finalDay= dayfrmat.format(dts);
            String mon=maothfor.format(dts);
            String times=timef.format(dts);
            tv_sdate.setText(finalDay + "  "+ mon);
            tv_stime.setText(times);

            if (object1.getString("TelePhone").equals(""))
            {
                iv_alt_call.setVisibility(View.GONE);

            }
            else{
                tv_tel.setText(object1.getString("TelePhone"));
                TelePhone=object1.getString("TelePhone");
            }

            iv_alt_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +TelePhone));// Initiates the Intent
                    startActivity(intent);

                }
            });
            tv_ageing.setText("/"+object1.getString("Ageing") + "  Days");

            if(  Status.equals("Closed") || Status.equals("Cancelled") || Status.equals("Negative Response from Custome") ){

                ll_spare.setClickable(false);
                ll_essential.setClickable(false);
                ll_status.setClickable(false);
                ll_sales.setClickable(false);

            }
            SerialNo=object1.getString("serial_no");



            RCNNo = object1.getString("RCNNo");
            ServiceType = object1.getString("ServiceType");
            CustomerName = object1.getString("CustomerName");

            String priorit = object1.getString("Priority");

            if (priorit.equals("01")){
                tv_escal.setText("Level 1 Escaltion");
            }
            else if(priorit.equals("02")){
                tv_escal.setText("Level 2 Escaltion");
            }
            else if(priorit.equals("98")){
                tv_escal.setText("Management Escaltion");
            }
            else if(priorit.equals("99")){
                tv_escal.setText("Social Media Escaktion");
            }
            else{
                tv_escal.setText("");
            }

            ProblemDescription=object1.getString("ProblemDescription");
            if (!ProblemDescription.equals("")){

                tv_problemdescription.setText(object1.getString("ProblemDescription"));
            }
            else
            {

                tv_problemdescription.setVisibility(View.GONE);
            }

            tv_calltype.setText(CallType.substring(0, 1));

            proces_type=processtype(CallType);
            tv_ticketno.setText(TicketNo);
            //  tv_address.setText(Address.replace("\\s+", ""));
            PinCode=object1.getString("PinCode");

            String fulladress=Address.replace("\\s+", "");
            tv_addresss.setText(fulladress+" , "+PinCode);

            tv_callbook.setText(CallBookDate);
            tv_status.setText(Status);
            tv_rcnno.setText(RCNNo);
            tv_servicetype.setText(ServiceType);
            tv_custname.setText(CustomerName);

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
            // brachname,Franchise,PinCode, TelePhone,Street,City,State
            tv_Brachname.setText(object1.getString("Branch"));
            tv_Franchise.setText(object1.getString("Franchise"));
            tv_PinCode.setText(object1.getString("PinCode"));
//              tv_TelePhone.setText(object1.getString("TelePhone"));
            tv_Street.setText(object1.getString("Street"));
            tv_City.setText(object1.getString("City"));
            tv_State.setText(object1.getString("State"));
            tv_mobile.setText(object1.getString("MobileNo"));
            //  tv_fax.setText(object1.getString("MobileNo"));
            tv_email.setText(object1.getString("Email"));
            // tv_website.setText(object1.getString("MobileNo"));
            //tv_addresss.setText(object1.getString("Address"));
            tv_Product.setText(object1.getString("Product"));
            tv_Model.setText(object1.getString("Model"));

            oduserno=object1.getString("odu_ser_no");
            tv_odu_ser_no.setText("oduserno");


            tv_problem_details.setText("");

            String machinestatus = object1.getString("MachinStatus");

            if (!machinestatus.equals("AMC")) {

                ll_amc_view.setVisibility(View.GONE);

            }

            if(machinestatus.equals("OG")){

                updateicrlayout();
            }


        } catch (Exception e) {

            e.printStackTrace();
        }

        lv_pending_item=findViewById(R.id.lv_pending_item);
        checkBox=findViewById(R.id.sp_not_found);

//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((CheckBox) v).isChecked()) {
//                  spare_not_found="X";
//                }
//            }
//        });

        ll_add_part = findViewById(R.id.ll_add_part);
        //   btn_savepart=(Button)findViewById(R.id.btn_add);
        tv_ess_quentity = findViewById(R.id.tv_ess_quentity);
        ll_add_ess = findViewById(R.id.ll_add_ess);


        btn_openmap.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {

            Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Street+","+City+","+State+","+PinCode+"");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);

        }
    });

        //notes
    }

    public void getbom(String TicketNo) {

        try {

            if (dbhelper.is_save_spare_Empty()) {

                bom_cursor = dbhelper.get_spare_Data(TicketNo);

                if (bom_cursor != null) {

                    if (bom_cursor.moveToFirst()) {



                        do {
                            model=new Details_spare_model();
                            final int recordid = bom_cursor.getInt(bom_cursor.getColumnIndex("recordid"));
                            model.setItemname( bom_cursor.getString(bom_cursor.getColumnIndex("Sname")));
                            model.setDescription(bom_cursor.getString(bom_cursor.getColumnIndex("Scode")));
                            model.setCount(bom_cursor.getString(bom_cursor.getColumnIndex("Sqty")));
                            model.setFlag(bom_cursor.getString(bom_cursor.getColumnIndex("Sflag")));
                            System.out.println(bom_cursor.getString(bom_cursor.getColumnIndex("Sflag")));
                            models.add(model);


                        } while (bom_cursor.moveToNext());

                        add_item_adapter = new DetailsSpareAdapter(models, DetailsActivity.this);
                        add_item_adapter.notifyDataSetChanged();
                        lv_additem.setAdapter(add_item_adapter);
                        bom_cursor.close();

                    }
                } else {
                    bom_cursor.close();
                }
            }

            else
            {

               // Toast.makeText(this, "No perevious spare found", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getess(String TicketNo) {

        try {

            if (dbhelper.is_save_ess_Empty()) {

                ess_cursor = dbhelper.get_save_ess_Data(TicketNo);

                if (ess_cursor != null) {

                    if (ess_cursor.moveToFirst()) {

                        do {

                            essential_add_model=new Details_ess_model();
                            final int recordid = ess_cursor.getInt(ess_cursor.getColumnIndex("recordid"));
                           // String Ename,String Ecode,String Eqty,String Etype
                            essential_add_model.setEname(ess_cursor.getString(ess_cursor.getColumnIndex("Ename")));
                            essential_add_model.setEcode(ess_cursor.getString(ess_cursor.getColumnIndex("Ecode")));
                            essential_add_model.setEquentity(ess_cursor.getString(ess_cursor.getColumnIndex("Eqty")));
//                            essential_add_model.setFlag(ess_cursor.getString(ess_cursor.getColumnIndex("Component")));
                            essential_add_model.setItemtype(ess_cursor.getString(ess_cursor.getColumnIndex("Etype")));
                            essential_add_models.add(essential_add_model);
                            System.out.println("essential details-->"+ess_cursor.getString(ess_cursor.getColumnIndex("Ename")));

                        } while (ess_cursor.moveToNext());

                        essential_add_adapter = new DetailsEssentialAdapter(essential_add_models, DetailsActivity.this);
                        lv_ess_add_item.setAdapter(essential_add_adapter);

                    }
                } else {



                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("error data"+e.getMessage());
        }
    }

    public void get_home_details(String TicketNo){

        try {

            if (dbhelper.is_save_home_Empty()) {

                home_cursor = dbhelper.get_home_Data(TicketNo);

                if (home_cursor != null) {

                    if (home_cursor.moveToFirst()) {


                        do {

                            final int recordid = home_cursor.getInt(home_cursor.getColumnIndex("recordid"));

                            add_product_model=new Details_Home_model();
                            add_product_model.setProduct(home_cursor.getString(home_cursor.getColumnIndex("Product")));
                            add_product_model.setYear(home_cursor.getString(home_cursor.getColumnIndex("Year")));
                            add_product_model.setMachine(home_cursor.getString(home_cursor.getColumnIndex("Company")));
                            add_product_models.add(add_product_model);

                        } while ( home_cursor.moveToNext());

                        addProductadapter = new DetailsCustomerHomeadapter(add_product_models, DetailsActivity.this);
                        addProductadapter.notifyDataSetChanged();
                        lv_product.setAdapter(addProductadapter);

                    }
                } else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void get_other_details(String TicketNo){

        try {

            if (dbhelper.is_save_other_Empty()) {

                other_cursor = dbhelper.get_others_details(TicketNo);

                if (other_cursor != null) {

                    if (other_cursor.moveToFirst()) {

                 //    Ticketno TEXT, Status TEXT, Serialno TEXT, DOP TEXT , DOI TEXT , Icr_date TEXT, Icr_no TEXT, Feedback TEXT, Member TEXT, House TEXT, Re_date TEXT, Re_time TEXT, Chnge_status TEXT, Pend_res TEXT,Pend_causes TEXT );";

                        do {

                          final int recordid = other_cursor.getInt(other_cursor.getColumnIndex("recordid"));


                            tv_feedback.setText(other_cursor.getString(other_cursor.getColumnIndex("Feedback")));
                            tv_residence.setText(other_cursor.getString(other_cursor.getColumnIndex("House")));
                            tv_member.setText(other_cursor.getString(other_cursor.getColumnIndex("Member")));

                            tv_pre_status.setText(other_cursor.getString(other_cursor.getColumnIndex("Chnge_status")));
                            tv_remarks.setText(other_cursor.getString(other_cursor.getColumnIndex("Pend_res")));

                            et_icr_no.setText(other_cursor.getString(other_cursor.getColumnIndex("Icr_no")));
                            tv_icr_date.setText(other_cursor.getString(other_cursor.getColumnIndex("Icr_date")));

                            tv_select_date.setText(other_cursor.getString(other_cursor.getColumnIndex("Re_date")));
                            tv_select_time.setText(other_cursor.getString(other_cursor.getColumnIndex("Re_time")));

                            tv_SerialNo.setText(other_cursor.getString(other_cursor.getColumnIndex("Serialno")));
                            tv_DOI.setText(other_cursor.getString(other_cursor.getColumnIndex("DOI")));
                            tv_DOP.setText(other_cursor.getString(other_cursor.getColumnIndex("DOP")));

                            System.out.println(other_cursor.getString(other_cursor.getColumnIndex("Serialno")));
                            System.out.println(other_cursor.getString(other_cursor.getColumnIndex("DOI")));


                        } while ( other_cursor.moveToNext());

                    }
                }

                else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        if (datestatus.equals("doi")) {
            tv_DOI.setText(sdf.format(myCalendar.getTime()));
            String dataformet="yyyyMMdd";
            SimpleDateFormat dateFormats=new SimpleDateFormat(dataformet,Locale.ENGLISH);

            DOI= dateFormats.format(myCalendar.getTime());

        }
        else if(datestatus.equals("redate")){

            tv_select_date.setText(sdf.format(myCalendar.getTime()));

            String dataformet="yyyyMMdd";
            SimpleDateFormat dateFormat=new SimpleDateFormat(dataformet,Locale.ENGLISH);

            sdate= dateFormat.format(myCalendar.getTime());

        }

        else if(datestatus.equals("icrdate")){
            tv_icr_date.setText(sdf.format(myCalendar.getTime()));

            String dataformet="yyyyMMdd";
            SimpleDateFormat dateFormat=new SimpleDateFormat(dataformet,Locale.ENGLISH);

            st_icr_date= dateFormat.format(myCalendar.getTime());

        }
        else if(datestatus.equals("dop")){
            tv_DOP.setText(sdf.format(myCalendar.getTime()));
            String dataformet="yyyyMMdd";
            SimpleDateFormat dateFormats=new SimpleDateFormat(dataformet,Locale.ENGLISH);

            DOP= dateFormats.format(myCalendar.getTime());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_offline_sync:

                if (CheckConnectivity.getInstance(this).isOnline()){

                    checkofflinedata();
                }
                else {

                    Toast.makeText(this,"Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                }

           break;

        }
    }

    public void checkofflinedata(){

        try {

            offcursor=dbhelper.get_offlineurl_data();

            if (offcursor != null) {

                if (offcursor.moveToFirst()) {

                    do {

                        final int recordid = offcursor.getInt(offcursor.getColumnIndex("recordid"));

                        String datas=offcursor.getString(offcursor.getColumnIndex("Fileurl"));
                        String TicketNo=offcursor.getString(offcursor.getColumnIndex("Ticketno"));
                        getimagedata(TicketNo);
                        System.out.println("offline ticket no"+TicketNo);
                        String type=offcursor.getString(offcursor.getColumnIndex("Type"));
                        submitfinaldata(datas,TicketNo);


                    } while (offcursor.moveToNext());
                    offcursor.close();
                }
            }
            else {



            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void submitfinaldata(String data,String TicketNo){

        String url=AllUrl.baseUrl+"ZTECHSO/AddNewZTechSO";

        // String url=AllUrl.baseUrl+"so/softcloser-by-app";

        System.out.println(url);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        final String requestBody = data;

        System.out.println("requestBody"+requestBody);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("200")){

                    btn_offline_sync.setVisibility(View.GONE);
                    dbhelper.delete_offlineurl(TicketNo);
                   // errorDetails.Errorlog(getApplicationContext(),"File%20Upload", "Offline%20service","Data%20Upload%20done",response,"",TicketNo,"S","",dbversion, Valuefilter.getdate());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
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

    }

    public String Parsedate(String time){
        String inputPattern = "dd-MMM-yyyy";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }

    public String Parsedates(String time){

        //   1\/1\/1900 12:00:00 AM
        String inputPattern = "dd/MM/yyyy hh:mm:ss a";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }

    public void updateicrlayout(){
        et_icr_no.setVisibility(View.VISIBLE);
        ll_icr_date.setVisibility(View.VISIBLE);
    }

    public void  callwhatsapp(){


        String wno=et_contact.getText().toString();

        if(wno.length()==10){

            String contact = "+91 "+wno; // use country code with your phone number
            String url = "https://api.whatsapp.com/send?phone=" + contact;
            try {
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(DetailsActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
        else{

            Toast.makeText(this, "Please Enter valid Mobile No", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.productdetailsmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_video_call) {
            openpopup();
            // startActivity(new Intent(DetailsActivity.this, ContactlistActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void  openpopup(){

        final Dialog dialog = new Dialog(DetailsActivity.this);

        dialog.setContentView(R.layout.videocalldialog);
        // Set dialog title
        dialog.setTitle("Whatsapp Call");

        et_contact=dialog.findViewById(R.id.vdialog_et_mobile);
        ImageView iv_no=dialog.findViewById(R.id.vdialog_iv_getcontact);
        Button btn_call=dialog.findViewById(R.id.vdialog_btn_call);

        iv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                startActivityForResult(intent, PICK_CONTACT);

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
        }
        return processtype;
    }

    public void getimagedata(String Ticketno){


        try {

                image_cursor=dbhelper.get_offline_image_data(Ticketno);

                if (image_cursor != null) {

                    if (image_cursor.moveToFirst()) {

                        //    Ticketno TEXT, Status TEXT, Serialno TEXT, DOP TEXT , DOI TEXT , Icr_date TEXT, Icr_no TEXT, Feedback TEXT, Member TEXT, House TEXT, Re_date TEXT, Re_time TEXT, Chnge_status TEXT, Pend_res TEXT,Pend_causes TEXT );";

                        do {

                            byte [] Serialnodata=image_cursor.getBlob(image_cursor.getColumnIndex("Serialnoimage"));
                            byte [] Oduserialnodata=image_cursor.getBlob(image_cursor.getColumnIndex("Oduserialnoimage"));
                            byte [] Invioceimage=image_cursor.getBlob(image_cursor.getColumnIndex("Invioceimage"));

                            System.out.println(Serialnodata);
                            System.out.println(Oduserialnodata);
                            System.out.println(Invioceimage);

                            view_serialno=findViewById(R.id.view_serialno);
                            view_odu_serialno_photo=findViewById(R.id.view_odu_serialno_photo);
                            view_invioce=findViewById(R.id.view_invioce);


                            try{

                                if (Serialnodata!=null){
                                    Bitmap bmp = BitmapFactory.decodeByteArray(Serialnodata, 0, Serialnodata.length);
                                    view_serialno.setImageBitmap(bmp);
                                    view_serialno.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            view_image("serialno",Serialnodata);

                                        }
                                    });

                                }

                                if (Oduserialnodata!=null){


                                    Bitmap bmp = BitmapFactory.decodeByteArray(Oduserialnodata, 0, Oduserialnodata.length);
                                    view_odu_serialno_photo.setImageBitmap(bmp);

                                    view_odu_serialno_photo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            view_image("oduserialno",Oduserialnodata);
                                        }
                                    });

                                }

                                if (Invioceimage !=null){
                                    Bitmap bmp = BitmapFactory.decodeByteArray(Invioceimage, 0, Invioceimage.length);
                                    view_invioce.setImageBitmap(bmp);

                                    view_invioce.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            view_image("invioce",Invioceimage);
                                        }
                                    });

                                }


                            }catch (Exception e){

                                e.printStackTrace();
                            }



                        } while ( image_cursor.moveToNext());

                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void  view_image(String type,byte [] data){

        // android.R.style.Theme_Black_NoTitleBar_Fullscreen
        // android.R.style.Theme_Black_NoTitleBar_Fullscreen
        final Dialog image_dialog = new Dialog(DetailsActivity.this);

        image_dialog.setContentView(R.layout.item_view_fillsizeimage);
        ImageView image_cancel;
        TextView textView;
        textView =image_dialog.findViewById(R.id.item_text_image);
        ZoomableImageView item_image_view;
        image_cancel=image_dialog.findViewById(R.id.image_cancel);
       // item_image_view=image_dialog.findViewById(R.id.item_image_view);

        image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_dialog.dismiss();
            }
        });

        try{

            if(data != null && type.equals("serialno")){

                textView.setText("Serial No Photo");

                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                //   item_image_view.setImageBitmap(bmp);
            }
            else if (data!=null && type.equals("oduserialno")){

                textView.setText("ODU Serial No Photo");
                System.out.println("seraialno_image_data-->"+data);
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                //item_image_view.setImageBitmap(bmp);
            }
            else if(data!=null && type.equals("invioce")){
                textView.setText("Invoice Photo");
                System.out.println("seraialno_image_data-->"+data);
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
             //   item_image_view.setImageBitmap(bmp);
            }


        }catch (Exception e){
            e.printStackTrace();
        }


        image_dialog.show();

    }


}
