package technician.ifb.com.ifptecnician;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import com.airbnb.lottie.LottieAnimationView;
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
import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
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
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import technician.ifb.com.ifptecnician.adapter.AddProductadapter;
import technician.ifb.com.ifptecnician.adapter.Add_item_adapter;
import technician.ifb.com.ifptecnician.adapter.Essential_add_adapter;
import technician.ifb.com.ifptecnician.adapter.Pending_item_adapter;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.alert.Alertwithicon;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.barcode.BarcodeCamera;
import technician.ifb.com.ifptecnician.barcode.SerialNoScan;
import technician.ifb.com.ifptecnician.customersign.CustomerSign;
import technician.ifb.com.ifptecnician.ebill.CreateBill;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.Add_Product_Model;
import technician.ifb.com.ifptecnician.model.Add_item_model;
import technician.ifb.com.ifptecnician.model.Essential_add_model;
import technician.ifb.com.ifptecnician.model.PendingSpare;
import technician.ifb.com.ifptecnician.model.Spare_Pending_model;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.offlinedata.OfflineService;
import technician.ifb.com.ifptecnician.service.CheckCustomerSerialNo;
import technician.ifb.com.ifptecnician.service.CheckSerialIsActive;
import technician.ifb.com.ifptecnician.service.CtcPendingReasons;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.service.GenerateDigitalICR;
import technician.ifb.com.ifptecnician.service.GetBomStock;
import technician.ifb.com.ifptecnician.service.GetJobstarttime;
import technician.ifb.com.ifptecnician.service.GetSparePrice;
import technician.ifb.com.ifptecnician.service.Getcitycode;
import technician.ifb.com.ifptecnician.service.Getprice;
import technician.ifb.com.ifptecnician.service.Jobcopmplete;
import technician.ifb.com.ifptecnician.service.VerifyIcr;
import technician.ifb.com.ifptecnician.serviceorder.SpareCodeList;
import technician.ifb.com.ifptecnician.serviceorder.SpareList;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.sohistory.Sohistory_adapter;
import technician.ifb.com.ifptecnician.sohistory.Sohistory_model;
import technician.ifb.com.ifptecnician.utility.AsyncFindmodel;
import technician.ifb.com.ifptecnician.utility.GetOfflineJobstarttime;
import technician.ifb.com.ifptecnician.utility.NumberPickerDialog;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

import static com.android.volley.Request.Method.POST;

public class CustomerDetailsActivity extends AppCompatActivity implements
         AdapterView.OnItemSelectedListener,
         NumberPicker.OnValueChangeListener,
         View.OnClickListener, AsyncFindmodel.AsyncResponse,Add_item_adapter.Deleteclick,Add_item_adapter.QuentityClick {

    private static final String TAG = CustomerDetailsActivity.class.getSimpleName();
    static final int PICK_CONTACT = 2;
    static final int ODU_NO = 5;
    static final int SCAN_PART = 6;
    static final int SCAN_ESS_NO = 4;
    static final int SCANSERIALNO = 115;
    public static final int INVOICE_IMAGE_CAPTURE = 7;
    public static final int SERIALNO_IMAGE_CAPTURE = 9;
    public static final int ODU_SERIALNO_IMAGE_CAPTURE = 10;
    public static final int CUSTOMER_SIGNATURE = 11;
    String serialnourl = "", oduurl = "", invioceurl = "";
    static final int REQUEST_IMAGE_CAPTURE = 15;
    String FrCode, FGProducts, MachinStatus = "";
    Alert alert = new Alert();
    String Component, ComponentDescription, FGDescription, FGProduct, FrCodes, MaterialCategory, good_stock, refurbished_stock;
    Dbhelper dbhelper;
    String proces_type;
    String mobile_details="";
    SharedPreferences sharedPreferences;
    //csv file create
    private RequestQueue rQueue;
    Cursor ess_cursor;
    TextView tv_calltype, tv_ticketno, tv_address, tv_callbook, tv_status, tv_rcnno, tv_servicetype, tv_custname;
    String TicketNo, Franchise, CallType, Status,
            Product, SerialNo = "000000000000000000", DOP = "00000000", DOI = "00000000", CallBookDate, AssignDate,
            CustomerCode, CustomerName, PinCode, TelePhone, RCNNo, ProblemDescription, PendingReason,
            Street, City, State, Address, ServiceType, ChangeDate, flagstatus = "", PostingDate = "01/01/2021 1:06:31 AM";
    public ArrayList<Add_item_model> models = new ArrayList<>();

    TextView tv_modelname, tv_dealername, tv_bom_qty, tv_part_code, tv_quentity;

    public static String serialno_scan = "";
    Add_item_adapter add_item_adapter;
    SessionManager sessionManager;
    String PartnerId = "", work_status = "", eta = "00000000", rechaeck_date = "00000000", spare_not_found = "";

    int bom_qty = 0;

    CheckBox checkBox;

    public Add_item_model model;
    SharedPreferences prefdetails;
    //Part section
    SearchableSpinner spinner;
    SearchableSpinner spinner_part_code;

    LinearLayout ll_spare_main, ll_spare_add, ll_spare_header, ll_add_part, ll_scan_part;

    String quentity = "1";
    ListView lv_additem, lv_pending_item;
    String partname, partcode;


    public PendingSpare pendingSpare;
    public ArrayList<PendingSpare> pendingSpares = new ArrayList<>();
    String CodeGroup, RegionCode = "";
    //customer details
    TextView tv_addresss, tv_TelePhone, tv_pending_status, tv_tel, tv_ageing, tv_escal, tv_problemdescription;
    ImageView iv_iv_priorit, iv_alt_call, iv_doi, iv_dop, iv_icr_verify;
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
    ImageView iv_info, iv_part, iv_ess, iv_note, iv_machine, iv_status;
    TextView t_cus, t_mach, t_spare, t_essen, t_status, t_note, tv_amount;

    //View section
    LinearLayout ll_details, ll_part, ll_esential, ll_notes, ll_machine_details, ll_status_details;

    //essential
    List<String> essspinerlist;
    Map<String, String> map_ess_code;
    SearchableSpinner ess_spiner;
    String accessories_stock, additives_stock;
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
    LinearLayout ll_icr_date, ll_icr_view;
    CardView cv_bill_section;
    TextView tv_icr_date, tv_icr_error;
    String st_icr_no = "";
    String st_icr_date = "00000000", icr_newdate = "";
    Spinner snpr_status;

    Button btn_status;
    String str_spnr_status;
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";
    private Button btnSave;

    String remarkurl;
    SearchableSpinner spnr_remark;
    List<String> remarklist;
    List<String> waterlist;
    Map<String, String> remarkmap;
    String remark, remarkcode;

    TextView tv_sdate, tv_stime;
    String sdate = "00000000", stime = "000000";
    LinearLayout ll_select_date, ll_select_time, ll_add_details, ll_change;

    TextView tv_select_date, tv_select_time;

    //notes
    Spinner spnr_product, spnr_machine, spnr_member, spnr_residence;
    Switch aSwitch;

    String product_name, machine_name, years, switch_status;

    String[] product_list = {"FL", "TL", "MW", "DW", "CD", "AC", "BIH", "BIO"};
    String[] machine_list = {"SAMSUNG", "CARRIER", "DAIKIN", "HITACHI", "BLUESTAR", "PHILIPS", "IFB", "LG", "WHIRLPOOL", "GODREJ", "SONY", "PANASONIC", "HAIER", "BOSCH", "VOLTAS", "OTHERS"};
    String[] residence_list = {"Residence", "1 BHK", "2 BHK", "3 BHK", "4 BHK", "BUNGLOW"};

    EditText et_contact, et_feedback, et_icr_no, et_tds_level;
    HashMap<String, String> map_residence = new HashMap<>();
    HashMap<String, String> map_machine = new HashMap<>();
    ListView lv_product;
    ArrayList<Add_Product_Model> add_product_models = new ArrayList<>();

    Add_Product_Model add_product_model;
    AddProductadapter addProductadapter;
    String no_of_member, residence, nomen = "", residen = "", tss, st_feedback = "";
    String zz_fl = "", zz_flbrand = "", zz_mt5fl = "", zz_tl = "", zz_tlbrand = "", zz_mt5tl = "", zz_mw = "", zz_mwbrand = "", zz_mt5mw = "", zz_dw = "", zz_dwbrand = "", zz_mt5dw = "", zz_cd = "", zz_cdbrand = "", zz_mt5cd = "", zz_ac = "", zz_acbrand = "", zz_mt5ac = "", zz_bih = "", zz_bihbrand = "", zz_mt5bih = "", zz_bio = "", zz_biobrand = "", zz_mt5bio = "";
    List<String> All_click = new ArrayList<>();
    String validNumber = "[0-9]+";
    ErrorDetails errorDetails;
    String mobile_no;
    TextView tv_odu_ser_no;
    LottieAnimationView btn_scan_odu_serial_no;
    ImageView lv_camera_invioce, view_invioce, iv_message, iv_call,
            lv_camera_serialno, lv_camera_oduserialno, view_serialno, view_oduserialno;

    String serialno_image = "%20", oduserialno_image = "%20", invoice_image = "%20";
    byte[] seraialno_image_data = null, oduserialno_image_data = null, invoice_image_data = null, customersign_data = null, custsign = null;

    LinearLayout ll_serial_no_image, ll_odu_serial_no_image, ll_invioce_no_image;

    //image upload layout
    SharedPreferences ratingperf;
    Bitmap imagebitmap;
    Boolean imagestatus = false;
    Button btn_updateaddress;
    //essential value
    String name, shelflife = "", essuses = "1/day", ticketdetails, dbversion = "", Medium = "";
    Spinner spnr_ess_value;
    TextView tv_ess_calculation, tv_error_message, tv_sign, tv_sohistory, tv_rating;
    LinearLayout ll_ess_cal, lv_custsign;

    JSONArray finaljsonArray;
    int totallinecount = 0;
    ProgressBar progressBar;
    LinearLayout ll_medium, ll_essential_main, ll_essential_add;
    ImageView img_map, iv_customersign;
    RecyclerView so_recyclerView;
    Sohistory_model sohistory_model;
    List<Sohistory_model> sohistory_models;
    Sohistory_adapter sohistory_adapter;
    // tds details
    SearchableSpinner spnr_water_source;
    String water_source = "0", tds_level = "NA", water_code = "NA";
    Map<String, String> map_water_source;
    String ctcstatus = "false", serial_photo_status = "false", ac_photo_status = "false", timer_status = "false";

    boolean isRunning = false;
    String icrstatus = "true",firstrow="true",submitstatus="false";
    // mandratarycheckbox
    final List<KeyPairBoolData> list_mcheck = new ArrayList<>();
    MultiSpinnerSearch spnr_mcheck;
    String mcheck = "", icr_msg, jobstarttime = "", ctcmessage;
    long ts;
    long milestone = System.currentTimeMillis();
    private static final String UNIQUE_PULL_DATA_FROM_SERVER = "UNIQUE_PULL_DATA_FROM_SERVER";
    ArrayList<SpareList> spareLists = new ArrayList<>();
    ArrayList<SpareCodeList> spareCodeLists = new ArrayList<>();
    ArrayList<Spare_Pending_model> spare_pending_models = new ArrayList<>();
    List<String> codelist;
    MyCountDownTimer myCountDownTimer;
    TextView tv_timeer, tv_spare_amount, tv_tot_amount;
    LinearLayout ll_service_charge_view;
    int progress;
    String Model,FGCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        prefdetails = getSharedPreferences("details", 0);
        ratingperf = getSharedPreferences("rating", 0);
        dbhelper = new Dbhelper(getApplicationContext());

        dbversion=AllUrl.APP_Version;



        errorDetails = new ErrorDetails();
        sharedPreferences = getSharedPreferences(AllUrl.MOBILE_DETAILS, 0);
        //mobile_details = sharedPreferences.getString("Mobile_details", "");
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
            startActivity(new Intent(CustomerDetailsActivity.this, MainActivity.class));

        }

        progressBar = findViewById(R.id.taskProcessing);
        tv_timeer = findViewById(R.id.tv_timeer);
        init();

    }

    public void getess() {

        try {

            essspinerlist = new ArrayList<>();
            map_ess_code = new HashMap<>();
            if (dbhelper.isessdataEmpty()) {

                ess_cursor = dbhelper.getallessdata();

                if (ess_cursor != null) {

                    if (ess_cursor.moveToFirst()) {

                        do {

                            final int recordid = ess_cursor.getInt(ess_cursor.getColumnIndex("recordid"));
                            String ComponentDescription = ess_cursor.getString(ess_cursor.getColumnIndex("ComponentDescription"));
                            String Component = ess_cursor.getString(ess_cursor.getColumnIndex("Component"));
                            essspinerlist.add(ComponentDescription);
                            map_ess_code.put(ComponentDescription, Component);

                        } while (ess_cursor.moveToNext());

                        ess_cursor.close();

                        ArrayAdapter<String> essAdapter = new ArrayAdapter<>(CustomerDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, essspinerlist);
                        ess_spiner.setAdapter(essAdapter);
                        // ess_spiner.setSelection(0,false);
                        ess_spiner.setOnItemSelectedListener(CustomerDetailsActivity.this);
                    }
                } else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        hideSoftKeyboard(CustomerDetailsActivity.this);

        if (parent.getId() == R.id.spinner) {

            // choose_type="partname"
            SpareList spareList = (SpareList) parent.getSelectedItem();
            partcode = spareList.getId();
            partname = spareList.getName();
            spinner_part_code.setSelection(position);

            if (!partcode.equals("")) {

                tv_part_code.setText(partcode);


                GetBomStock getBomStock = new GetBomStock();

                showProgressDialog();

                getBomStock.getstock(new GetBomStock.VolleyCallback() {
                    @Override
                    public void onSuccess(String result, String status) {

                        System.out.println("result--> " + result);
                        hideProgressDialog();

                        if (status.equals("true")) {

                            try {

                                JSONArray jsonArray = new JSONArray(result);

                                System.out.println("spare position" + position);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String good_stock = jsonObject.getString("good_stock");
                                    String refurbished_stock = jsonObject.getString("refurbished_stock");

                                    int goodstock = string_to_int(good_stock);
                                    int refurbishedstock = string_to_int(refurbished_stock);


                                    if (goodstock > 0) {

                                        if (goodstock < refurbishedstock) {

                                            tv_bom_qty.setText(refurbished_stock);
                                            bom_qty = refurbishedstock;
                                        } else {
                                            tv_bom_qty.setText(good_stock);
                                            bom_qty = goodstock;
                                        }

                                    } else {

                                        if (goodstock < refurbishedstock) {

                                            tv_bom_qty.setText(refurbished_stock);
                                            bom_qty = refurbishedstock;
                                        } else {
                                            tv_bom_qty.setText(good_stock);
                                            bom_qty = goodstock;
                                        }
                                    }
                                }
                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                        } else {

                            // showerror("Spare stock  not found");
                            bom_qty = 0;
                            tv_bom_qty.setText("0");
                        }

                    }
                }, CustomerDetailsActivity.this, FrCode, partcode);

            }
        } else if (parent.getId() == R.id.spinner_part_code) {

            SpareCodeList spareCodeList = (SpareCodeList) parent.getSelectedItem();
            partcode = spareCodeList.getName();
            partname = spareCodeList.getId();
            spinner.setSelection(position);

            if (partcode.equals("-- select code --")) {

            } else {

                if (!partcode.equals("")) {

                    tv_part_code.setText(partcode);

                    // String icr=et_icr_no.getText().toString();
                    GetBomStock getBomStock = new GetBomStock();

                    showProgressDialog();

                    getBomStock.getstock(new GetBomStock.VolleyCallback() {
                        @Override
                        public void onSuccess(String result, String status) {


                            System.out.println("result--> " + result);


                            hideProgressDialog();

                            if (status.equals("true")) {

                                try {

                                    JSONArray jsonArray = new JSONArray(result);

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        String good_stock = jsonObject.getString("good_stock");
                                        String refurbished_stock = jsonObject.getString("refurbished_stock");

                                        int goodstock = string_to_int(good_stock);
                                        int refurbishedstock = string_to_int(refurbished_stock);


                                        if (goodstock > 0) {


                                            if (goodstock < refurbishedstock) {

                                                tv_bom_qty.setText(refurbished_stock);
                                                bom_qty = refurbishedstock;
                                            } else {

                                                tv_bom_qty.setText(good_stock);
                                                bom_qty = goodstock;

                                            }

                                            break;
                                        } else {
                                            if (goodstock < refurbishedstock) {

                                                tv_bom_qty.setText(refurbished_stock);
                                                bom_qty = refurbishedstock;
                                            } else {

                                                tv_bom_qty.setText(good_stock);
                                                bom_qty = goodstock;

                                            }

                                        }

                                        System.out.println("spare position" + position);

                                    }
                                } catch (Exception e) {

                                    e.printStackTrace();
                                }

                            } else {

                                // showerror("Spare stock  not found");
                                bom_qty = 0;
                                tv_bom_qty.setText("0");


                            }

                        }
                    }, CustomerDetailsActivity.this, FrCode, partcode);

                }
            }

        } else if (parent.getId() == R.id.ess_spinner) {

            essComponentDescription = parent.getItemAtPosition(position).toString();
            Cursor cursor;

            try {

                cursor = dbhelper.getesstype(essComponentDescription);

                if (cursor != null) {

                    if (cursor.moveToFirst()) {

                        do {


                            essComponent = cursor.getString(cursor.getColumnIndex("Component"));
                            ItemType = cursor.getString(cursor.getColumnIndex("ItemType"));
                            shelflife = cursor.getString(cursor.getColumnIndex("ShelfLife"));
                            ess_tv_shelflife.setText(shelflife);
                            tv_ess_component.setText(essComponent);
                            accessories_stock = cursor.getString(cursor.getColumnIndex("accessories_stock"));
                            additives_stock = cursor.getString(cursor.getColumnIndex("additives_stock"));
                            accstock = string_to_int(accessories_stock);
                            addstock = string_to_int(additives_stock);

                            if (accstock < addstock) {
                                tv_ess_sqty.setText(additives_stock);
                                ess_stock = addstock;
                            } else {
                                ess_stock = accstock;
                                tv_ess_sqty.setText(accessories_stock);
                            }

                            getcal();
                        } while (cursor.moveToNext());
                        cursor.close();

                    }
                } else {

                    // 8657454408

                }
            } catch (Exception e) {

                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        } else if (parent.getId() == R.id.spnr_status) {

            str_spnr_status = parent.getItemAtPosition(position).toString();

            if (str_spnr_status.equals("Pending")) {

                btn_status.setVisibility(View.VISIBLE);
                tv_timeer.setVisibility(View.GONE);
                //
                clodeupdateicrlayout();

                if (check_spare_pending().equals("true")) {
                    upadte_recheck_for_spare();
                } else {
                    getremarkdata(CallType);
                }


                ll_change.setVisibility(View.VISIBLE);
                spnr_remark.setVisibility(View.VISIBLE);
                spnr_water_source.setVisibility(View.GONE);
                et_tds_level.setVisibility(View.GONE);
                spnr_mcheck.setVisibility(View.GONE);
                // clodeupdateicrlayout();

                if (ctcstatus.equals("true")) {

                    ll_change.setVisibility(View.VISIBLE);

                }
            } else if (str_spnr_status.equals("Resolved (soft closure)")) {


                if (MachinStatus.equals("OG")) {

                    if (CallType.equals("COURTESY VISIT") || CallType.equals("INSTALLATION CALL")){

                    }
                    else {

                        updateicrlayout();
                    }
                }

                if (timer_status.equals("true")) {

                    btn_status.setVisibility(View.VISIBLE);
                    tv_timeer.setVisibility(View.GONE);
                } else {
                    btn_status.setVisibility(View.GONE);
                    tv_timeer.setVisibility(View.VISIBLE);
                }


                if (check_spare_pending().equals("true")) {

                    showerror("Added spare is pending");
                    upadte_recheck_for_spare();

                }



                spnr_remark.setVisibility(View.GONE);
                ll_change.setVisibility(View.GONE);


                if (Product.equals("WM") || Product.equals("WD") || Product.equals("DW")) {

                    spnr_water_source.setVisibility(View.VISIBLE);
                    et_tds_level.setVisibility(View.VISIBLE);
                } else {
                    et_tds_level.setText("NA");

                }


                if (CallType.equals("MANDATORY CALLS")) {
                    spnr_mcheck.setVisibility(View.VISIBLE);
                }

                errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Status%20List", TAG, "Status%20Tab", str_spnr_status.replace(" ", "%20"), mobile_no, TicketNo, "", mobile_details, dbversion, tss);
            } else {

                spnr_remark.setVisibility(View.GONE);
                ll_change.setVisibility(View.GONE);
            }


            if (str_spnr_status.equals("Select status")) {


                spnr_remark.setVisibility(View.GONE);
                ll_change.setVisibility(View.GONE);

            }

        } else if (parent.getId() == R.id.spnr_remark) {

            remark = parent.getItemAtPosition(position).toString();
            remarkcode = remarkmap.get(remark);

            if (str_spnr_status.equals("Resolved (soft closure)")) {

                if (ctcstatus.equals("true")) {

                    spnr_remark.setVisibility(View.GONE);
                    remarkcode = "Z023";
                }

            }

        } else if (parent.getId() == R.id.spnr_water_source) {

            water_source = parent.getItemAtPosition(position).toString();
            water_code = map_water_source.get(water_source);


        } else if (parent.getId() == R.id.spnr_machine) {

            machine_name = parent.getItemAtPosition(position).toString();

        } else if (parent.getId() == R.id.spnr_product) {
            product_name = parent.getItemAtPosition(position).toString();

        } else if (parent.getId() == R.id.spnr_member) {

            no_of_member = parent.getItemAtPosition(position).toString();

            if (no_of_member.equals("No. of Members")) {
                nomen = "";

            } else {

                nomen = parent.getItemAtPosition(position).toString();
                // All_click.add("Selected%20no_of_member%20"+   nomen);
            }

        } else if (parent.getId() == R.id.spnr_residence) {

            residence = parent.getItemAtPosition(position).toString();

            if (residence.equals("Residence")) {

                residen = "";
            } else {

                residen = getresi(parent.getItemAtPosition(position).toString());
                // All_click.add("Selected%20Residence%20"+   residen.replace(" ","%20"));
            }
        } else if (parent.getId() == R.id.spnr_ess_value) {
            essuses = parent.getItemAtPosition(position).toString();
            getcal();

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        quentity = String.valueOf(picker.getValue());
        tv_quentity.setText(quentity);
        tv_ess_quentity.setText(quentity);

        getcal();

    }

    public void showNumberPicker(View view) {
        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setValueChangeListener(this);
        newFragment.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        dbhelper.delete_work_data(TicketNo);

        Gson gson = new Gson();
        String sparedata = gson.toJson(models);
        String essentialdata = gson.toJson(essential_add_models);
        boolean status = dbhelper.insert_work_data(TicketNo, SerialNo, oduserno, DOP, DOI, jobstarttime, "" + ts, sparedata, essentialdata, et_feedback.getText().toString(), "", "", "", str_spnr_status, "", ctcstatus);

        if (imagestatus) {
            dbhelper.delete_image_data(TicketNo);
            dbhelper.insert_offline_image_data(TicketNo, seraialno_image_data, oduserialno_image_data, invoice_image_data, customersign_data, tss, "worktime");
        }

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
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (datestatus.equals("doi")) {

            tv_DOI.setText(sdf.format(myCalendar.getTime()));
            errorDetails.Errorlog(CustomerDetailsActivity.this, "Change%20DOI%20Data", TAG, "Product%20Tab", tv_DOI.getText().toString(), mobile_no, TicketNo, "U", mobile_details, dbversion, tss);

            String dataformet = "yyyyMMdd";
            SimpleDateFormat dateFormats = new SimpleDateFormat(dataformet, Locale.ENGLISH);
            DOI = dateFormats.format(myCalendar.getTime());

        } else if (datestatus.equals("redate")) {

            tv_select_date.setText(sdf.format(myCalendar.getTime()));

            errorDetails.Errorlog(CustomerDetailsActivity.this, "Change%20Re_scheduled%20Date", TAG, "Status%20Tab", tv_select_date.getText().toString(), mobile_no, TicketNo, "U", mobile_details, dbversion, tss);

            String dataformet = "yyyyMMdd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(dataformet, Locale.ENGLISH);

            sdate = dateFormat.format(myCalendar.getTime());

            ll_select_time.setClickable(true);

        } else if (datestatus.equals("icrdate")) {

            tv_icr_date.setText(sdf.format(myCalendar.getTime()));

            errorDetails.Errorlog(CustomerDetailsActivity.this, "Change%20ICR%20Date", TAG, "Status%20Tab", tv_icr_date.getText().toString(), mobile_no, TicketNo, "U", mobile_details, dbversion, tss);
            String dataformet = "yyyyMMdd";
            SimpleDateFormat dateFormat = new SimpleDateFormat(dataformet, Locale.ENGLISH);
            st_icr_date = dateFormat.format(myCalendar.getTime());


            String dataformets = "yyyy-MM-dd";
            SimpleDateFormat dataFormets = new SimpleDateFormat(dataformets, Locale.ENGLISH);
            icr_newdate = dataFormets.format(myCalendar.getTime());

        } else if (datestatus.equals("dop")) {

            tv_DOP.setText(sdf.format(myCalendar.getTime()));

            errorDetails.Errorlog(CustomerDetailsActivity.this, "Change%20DOP%20Date", TAG, "Product%20Tab", tv_DOP.getText().toString(), mobile_no, TicketNo, "U", mobile_details, dbversion, tss);
            String dataformet = "yyyyMMdd";
            SimpleDateFormat dateFormats = new SimpleDateFormat(dataformet, Locale.ENGLISH);
            DOP = dateFormats.format(myCalendar.getTime());

        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case (SCAN_ESS_NO):

                if (resultCode == RESULT_OK) {

                    String datas = data.getStringExtra("barcode");

                    if (!datas.equals("")) {

                        try {

                            boolean status = dbhelper.IS_ess_Component_exits(datas);

                            if (status) {

                                get_ess_name(datas);
                                tv_ess_component.setText(datas);
                               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Essential%20Scan%20Result", TAG, "Essential%20Found%20In%20List", data.getStringExtra("serialno"), mobile_no, TicketNo, "U", mobile_details, dbversion, tss);

                            } else {
                                showerror("Essential Not Found");
                               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Essential%20Scan%20Result", TAG, "Essential%20Not%20Found%20In%20List", data.getStringExtra("serialno"), mobile_no, TicketNo, "E", mobile_details, dbversion, tss);

                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    }
                }
                break;

            case (SCANSERIALNO):

                if (resultCode == RESULT_OK) {

                    String datas = data.getStringExtra("barcode");

                    if (!datas.equals("")) {

                        String srno = datas;
                        errorDetails.Errorlog(CustomerDetailsActivity.this, "Serial%20No%20Scan%20Result", TAG, "Serial%20No", srno, mobile_no, TicketNo, "S", mobile_details, dbversion, tss);
                        if (checkSerialNo(srno)) {

                            getservertime();
                            serialno_scan = srno;
                            if (CallType.equals("INSTALLATION CALL")) {

                                try {

                                    tv_SerialNo.setText(srno);
                                    SerialNo = srno;

                                    dbhelper.delete_serial_no_data(TicketNo, "serial");
                                    dbhelper.insert_serial_no_data(SerialNo, TicketNo, "serial");

                                   if (CheckConnectivity.getInstance(this).isOnline()) {

                                        CheckSerialIsActive checkSerialIsActive = new CheckSerialIsActive();

                                        checkSerialIsActive.Checkserial(new CheckSerialIsActive.Serialresult() {
                                            @Override
                                            public void onsuccess(String status, String data) {

                                                if (status.equals("true")) {

                                                    ctcstatus = "true";
                                                    ctcmessage = "SERVICE ORDER COULD NOT BE SAVED. CUSTOMER IN IBASE " + data + " DIFFERS FROM SERVICE ORDER.";

                                                    Alertwithicon alertwithicon = new Alertwithicon();
                                                    alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "SERVICE ORDER COULD NOT BE SAVED. CUSTOMER IN IBASE (" + data + ") DIFFERS FROM SERVICE ORDER.\nPlease Capture Serial No photo ", false);

                                                } else {

                                                    ctcstatus = "false";
                                                    ctcmessage = "";


                                                    new AsyncFindmodel(CustomerDetailsActivity.this, CustomerDetailsActivity.this).execute("");

                                                }

                                            }
                                        }, CustomerDetailsActivity.this, srno);

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    FirebaseCrashlytics.getInstance().recordException(e);
                                }

                            }
                            else if (SerialNo.length() == 0) {


                                if (CheckConnectivity.getInstance(this).isOnline()) {

                                    CheckSerialIsActive checkSerialIsActive = new CheckSerialIsActive();

                                    checkSerialIsActive.Checkserial(new CheckSerialIsActive.Serialresult() {
                                        @Override
                                        public void onsuccess(String status, String data) {

                                            if (status.equals("true")) {

                                                ctcstatus = "true";
                                                ctcmessage = "SERVICE ORDER COULD NOT BE SAVED. CUSTOMER IN IBASE " + data + " DIFFERS FROM SERVICE ORDER.";
                                                Alertwithicon alertwithicon = new Alertwithicon();
                                                alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "CUSTOMER IN IBASE (" + data + ") DIFFERS FROM SERVICE ORDER. \nPlease Capture Serial No photo ", false);
                                                tv_SerialNo.setText(srno);
                                                SerialNo = srno;

                                            } else {

                                                ctcstatus = "false";
                                                ctcmessage = "";
                                                new AsyncFindmodel(CustomerDetailsActivity.this, CustomerDetailsActivity.this).execute("");

                                            }

                                        }
                                    }, CustomerDetailsActivity.this, srno);

                                } else {

                                    SerialNo = srno;
                                }
                            }
                            else if (MachinStatus.equals("AMC")) {

                            }
                            else {

                                if (SerialNo.equals(srno)) {

                                    ctcstatus = "false";
                                    ctcmessage = "";
                                } else if (CheckConnectivity.getInstance(this).isOnline()) {

                                    CheckCustomerSerialNo customerSerialNo = new CheckCustomerSerialNo();
                                    showProgressDialog();
                                    customerSerialNo.getstatus(new CheckCustomerSerialNo.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String status, String result) {

                                            if (status.equals("true")) {

                                                ctcstatus = "false";
                                                ctcmessage = "";
                                            } else if (status.equals("false")) {


                                                checkctcerror(srno);

//                                                    ctcstatus = "true";
//                                                    ctcmessage="SERVICE ORDER COULD NOT BE SAVED. CUSTOMER IN IBASE ("+data+") DIFFERS FROM SERVICE ORDER.";
//
//                                                    Alertwithicon alertwithicon=new Alertwithicon();
//                                                    alertwithicon.showDialog(CustomerDetailsActivity.this,"I-BASE Error","SERVICE ORDER COULD NOT BE SAVED. CUSTOMER IN IBASE ("+data+") DIFFERS FROM SERVICE ORDER.\nPlease Capture Serial No photo ",false);
//
                                            } else if (status.equals("error")) {

                                                ctcstatus = "false";
                                                ctcmessage = "";

                                            }
                                            hideProgressDialog();

                                        }
                                    }, CustomerDetailsActivity.this, CustomerCode, srno);
                                } else {


                                }

                            }

                        } else {
                            errorDetails.Errorlog(CustomerDetailsActivity.this, "Serial%20No%20Scan%20Result", TAG, "Serial%20No%20Error", srno, mobile_no, TicketNo, "E", mobile_details, dbversion, tss);
                        }
                    } else {

                        showerror("Please scan another time");
                    }
                }

                break;

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

            case ODU_NO:

                if (resultCode == RESULT_OK) {

                    String datas = data.getStringExtra("barcode");

                    if (!datas.equals("")) {

                        String odu_no = datas;

                        if (checkSerialNo(odu_no)) {

                            tv_odu_ser_no.setText(odu_no);
                            dbhelper.delete_serial_no_data(TicketNo, "odu");
                            dbhelper.insert_serial_no_data(odu_no, TicketNo, "odu");
                            oduserno = odu_no;
                            serialno_scan = odu_no;
                            errorDetails.Errorlog(CustomerDetailsActivity.this, "Odu%20Serial%20No%20Change", TAG, "Product%20TAB", "Scan%20Odu%20No%20" + odu_no, mobile_no, TicketNo, "U", mobile_details, dbversion, tss);

                            if (CheckConnectivity.getInstance(this).isOnline()) {


                                if (CheckConnectivity.getInstance(this).isOnline()) {

                                    CheckSerialIsActive checkSerialIsActive = new CheckSerialIsActive();

                                    checkSerialIsActive.Checkserial(new CheckSerialIsActive.Serialresult() {
                                        @Override
                                        public void onsuccess(String status, String data) {

                                            if (status.equals("true")) {

                                                ctcstatus = "true";
                                                ctcmessage = "SERVICE ORDER COULD NOT BE SAVED. CUSTOMER IN IBASE " + data + " DIFFERS FROM SERVICE ORDER.";

                                            } else {

                                                ctcstatus = "false";
                                                ctcmessage = "";

                                                new AsyncFindmodel(CustomerDetailsActivity.this, CustomerDetailsActivity.this).execute("");

                                            }

                                        }
                                    }, CustomerDetailsActivity.this, odu_no);

                                }

                                //  new AsyncFindmodel(CustomerDetailsActivity.this, this).execute("");

                            }

                        } else {

                            errorDetails.Errorlog(CustomerDetailsActivity.this, "Odu%20Serial%20No%20Scan%20Result", TAG, "Serail%20No%20Error", "Scan%20Odu%20No%20" + odu_no, mobile_no, TicketNo, "E", mobile_details, dbversion, tss);

                            showerror("Serial No Always 18 digit !");
                            //  Toast.makeText(this, "Seral no Always 18 digit !", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                break;

            case SCAN_PART:

                if (resultCode == RESULT_OK) {

                    String datas = data.getStringExtra("barcode");

                    if (!datas.equals("")) {

                        String no = datas;

                        System.out.println("no--> " + no);

                        boolean status = codelist.contains(no);

                        if (status) {

                            int pos = codelist.indexOf(no);

                            spinner_part_code.setSelection(pos);
                            spinner.setSelection(pos);

                            partcode = no;
                            if (!partcode.equals("")) {

                                tv_part_code.setText(partcode);

                                // String icr=et_icr_no.getText().toString();
                                GetBomStock getBomStock = new GetBomStock();

                                showProgressDialog();

                                getBomStock.getstock(new GetBomStock.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String result, String status) {


                                        System.out.println("result--> " + result);


                                        hideProgressDialog();

                                        if (status.equals("true")) {

                                            try {

                                                JSONArray jsonArray = new JSONArray(result);

                                                for (int i = 0; i < jsonArray.length(); i++) {

                                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                                    String good_stock = jsonObject.getString("good_stock");
                                                    String refurbished_stock = jsonObject.getString("refurbished_stock");

                                                    int goodstock = string_to_int(good_stock);
                                                    int refurbishedstock = string_to_int(refurbished_stock);

                                                    if (goodstock > 0) {

                                                        if (goodstock < refurbishedstock) {

                                                            tv_bom_qty.setText(refurbished_stock);
                                                            bom_qty = refurbishedstock;
                                                        } else {

                                                            tv_bom_qty.setText(good_stock);
                                                            bom_qty = goodstock;
                                                        }
                                                        break;

                                                    } else {

                                                        if (goodstock < refurbishedstock) {

                                                            tv_bom_qty.setText(refurbished_stock);
                                                            bom_qty = refurbishedstock;
                                                        } else {

                                                            tv_bom_qty.setText(good_stock);
                                                            bom_qty = goodstock;
                                                        }
                                                    }

                                                }
                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }

                                        } else {

                                            showerror("Spare stock  not found");
                                            bom_qty = 0;
                                            tv_bom_qty.setText("0");
                                        }

                                    }
                                }, CustomerDetailsActivity.this, FrCode, partcode);
                            }
                        } else {

                            showerror("Spare Not Found In List");
                        }
                    }
                }
                break;

            case INVOICE_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");

                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        // Bitmap converetdImage = getResizedBitmap(bitmap, 80);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                        invoice_image_data = byteArrayOutputStream.toByteArray();
                        errorDetails.Errorlog(CustomerDetailsActivity.this, "INVOICE_IMAGE_CAPTURE", TAG, "INVOICE_IMAGE_CAPTURE", "INVOICE_IMAGE_CAPTURE", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                        view_invioce.setImageBitmap(bitmap);
                        invioceurl = "";
                        imagestatus = true;

                    } catch (IOException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
                break;

            case SERIALNO_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");

                    //  System.out.println("uri-->"+uri);
                    try {
                        // You can update this bitmap to your server
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        // Bitmap converetdImage = getResizedBitmap(bitmap, 80);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                        seraialno_image_data = byteArrayOutputStream.toByteArray();
                        // System.out.println("seraialno_image_data-->"+seraialno_image_data);
                        imagestatus = true;
                        serial_photo_status = "true";
                        errorDetails.Errorlog(CustomerDetailsActivity.this, "SERIALNO_IMAGE_CAPTURE", TAG, "SERIALNO_IMAGE_CAPTURE", "SERIALNO_IMAGE_CAPTURE", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                        serialnourl = "";
                        view_serialno.setImageBitmap(bitmap);
                        ImagePickerActivity.clearCache(CustomerDetailsActivity.this);
                        getservertime();

                    } catch (IOException e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
                break;

            case ODU_SERIALNO_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");
                    try {
                        // You can update this bitmap to your server
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        // Bitmap converetdImage = getResizedBitmap(bitmap, 80);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                        oduserialno_image_data = byteArrayOutputStream.toByteArray();
                        view_oduserialno.setImageBitmap(bitmap);
                        imagestatus = true;
                        ac_photo_status = "true";
                        errorDetails.Errorlog(CustomerDetailsActivity.this, "ODU_SERIALNO_IMAGE_CAPTURE", TAG, "ODU_SERIALNO_IMAGE_CAPTURE", "ODU_SERIALNO_IMAGE", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                        oduurl = "";


                    } catch (Exception e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {

                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                    seraialno_image_data = byteArrayOutputStream.toByteArray();
                    // System.out.println("seraialno_image_data-->"+seraialno_image_data);
                    imagestatus = true;
                    serial_photo_status = "true";
                    errorDetails.Errorlog(CustomerDetailsActivity.this, "SERIALNO_IMAGE_CAPTURE", TAG, "SERIALNO_IMAGE_CAPTURE", "SERIALNO_IMAGE_CAPTURE", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                    serialnourl = "";
                    view_serialno.setImageBitmap(bitmap);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    tv_serialno_capturetime.setText("hh:mm (" + currentDateandTime + ")");
                    jobstarttime = currentDateandTime.replace(" ", "%20");

                }

                break;

            case CUSTOMER_SIGNATURE:

                if (resultCode == Activity.RESULT_OK) {
                    String signpath = data.getStringExtra("signpath");

                    try {


                        Bitmap bitmap = BitmapFactory.decodeFile(signpath);
                        //  customer_sign_bitmap= BitmapFactory.decodeFile(signpath);
                        iv_customersign.setImageBitmap(bitmap);
                        iv_customersign.setVisibility(View.VISIBLE);
                        imagestatus = true;
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                        customersign_data = byteArrayOutputStream.toByteArray();
                        custsign = byteArrayOutputStream.toByteArray();

                    } catch (Exception e) {
                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                }

                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void getremarkdata(String CallType) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            if (CallType.equals("SERVICE CALL")) {
                remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZSER07";
            } else if (CallType.equals("MANDATORY CALLS")) {
                remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZMAN07";
            } else if (CallType.equals("COURTESY VISIT")) {
                remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZFOC07";
            } else if (CallType.equals("INSTALLATION CALL")) {
                remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZINT07";
            } else if (CallType.equals("DEALER DEFECTIVE REQUEST")) {
                remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZDD07";
            } else if (CallType.equals("REINSTALLLATION ORDER")) {
                remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZRIN07";
            } else if (CallType.equals("REWORK ORDER")) {
                remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZREW07";
            }

            progressBar.setVisibility(View.VISIBLE);

            System.out.println("remarkurl-->" + remarkurl);
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, remarkurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            progressBar.setVisibility(View.GONE);

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                remarklist = new ArrayList<>();

//                            remarklist.add("select pending cause");
                                remarkmap = new HashMap<>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject rmobj = jsonArray.getJSONObject(i);
                                    CodeGroup = rmobj.getString("CodeGroup");
                                    String Code = rmobj.getString("Code");
                                    String Reason = rmobj.getString("Reason");

                                    remarklist.add(Reason);
                                    remarkmap.put(Reason, Code);
                                }
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CustomerDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, remarklist);
                                spnr_remark.setAdapter(arrayAdapter);
                                // spnr_remark.setSelection(0,false);
                                spnr_remark.setOnItemSelectedListener(CustomerDetailsActivity.this);

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
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {

            get_offline_pendingReasons(CallType);
            // System.out.println("Offline pending reason");

        }
    }

    public void checkproduct() {

        try {
            if (machine_name.equals("Select Machine")) {

                showerror("Select Machine");

            } else if (product_name.equals("Select product")) {

                showerror("Select Product");
            } else {

                if (aSwitch.isChecked()) {
                    years = "> 5";
                } else {

                    years = "< 5";
                }

                add_product_model = new Add_Product_Model();
                add_product_model.setMachine(machine_name);
                add_product_model.setProduct(product_name);
                add_product_model.setYear(years);
                add_product_models.add(add_product_model);
                addProductadapter = new AddProductadapter(add_product_models, CustomerDetailsActivity.this);
                addProductadapter.notifyDataSetChanged();
                lv_product.setAdapter(addProductadapter);
                lv_product.setSelection(addProductadapter.getCount() - 1);

            }

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);

        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_select_date:
                choose_re_date();
              //  errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Resedule%20Date%20Icon", TAG, "Status%20Tab", "Status%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);

                break;
            case R.id.ll_select_time:
                choose_time();
               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Resedule%20Time%20Icon", TAG, "Status%20Tab", "Status%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);

                break;
            case R.id.tv_quentity:
                showNumberPicker(v);
               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Spare%20Quantity", TAG, "Spare%20Tab", "Spare%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);

                break;

            case R.id.ll_scan_part:

                ScanPart();
               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Scan%20SpareItem%20Icon", TAG, "Spare%20Tab", "For%20Scan%20Spare%Item", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);

                break;

            case R.id.ll_icr_date:
                choose_icr_date();
                //  All_click.add("Click%20On%20ICRDate");
              //  errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20ICR%20Date%20Icon", TAG, "Status%20Tab", "Status%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);

                break;

//            case R.id.iv_whatsapp:
//                callwhatsapp();
//                break;
            case R.id.ll_customer:

                ActiveCustomerTab();
                //    All_click.add("Click%20On%20CustomerTab");
               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20CustomerTab", TAG, "Customer%20Tab", "Customer%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);


                break;
            case R.id.ll_spare:
                ActiveSpareTab();
              //  errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20SpareTab", TAG, "Spare%20Tab", "Spare%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);

                break;
            case R.id.ll_sales:

                ActiveNotesPage();

              //  errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Notes%20Tab", TAG, "Notes%20Tab", "Notes%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);


                break;
            case R.id.ll_essential:
                ActiveEssTab();

               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Essential%20Tab", TAG, "Essential%20Tab", "Essential%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);

                break;

            case R.id.ll_status:

               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20StatusTab", TAG, "Click%20On%20StatusTab", "Click%20On%20StatusTab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                ActiveStatusPage();
                if (check_spare_pending().equals("true")) {

                    upadte_recheck_for_spare();
                }
                break;

            case R.id.ll_machine:

                ActiveProductTab();
              //  errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20MachineTab", TAG, "Machine%20Tab", "Machine%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                break;

//            case R.id.btnclear:
            //               clear_Signature();
//                errorDetails.Errorlog(CustomerDetailsActivity.this,"Click%20On%20Customer%20Signature%20Clear%20Button", TAG,"Status%20Tab","Status%20Tab",mobile_no,TicketNo,"C",mobile_details,dbversion,tss);
//
//                break;
            case R.id.ll_scan_ess:

              //  errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Scan%20Essential%20Icon", TAG, "Essential%20Tab", "Essential%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                scan_essential();

                break;
            case R.id.btn_status:

                if (str_spnr_status.equals("Select status")) {

                    showerror("Please Select Status");

                } else if (PartnerId.length() == 0) {

                    showerror("PartnerId Not Found , Please login again");

                } else if (str_spnr_status.equals("Pending")) {

                    if (remarkcode.equals("Z011") || remarkcode.equals("Z012") || remarkcode.equals("Z015")) {

                        if (check_spare_pending().equals("true")) {

                            submit_Value();
                        } else {

                            showerror("Please select pending spare ");
                        }
                    } else {

                        submit_Value();

                    }

                } else if (str_spnr_status.equals("Resolved (soft closure)")) {

                    tds_level = et_tds_level.getText().toString();

                    if (check_spare_pending().equals("true")) {

                        showerror("Added Spare is Pending");

                        upadte_recheck_for_spare();

                        errorDetails.Errorlog(CustomerDetailsActivity.this, "Spare%20Pending", TAG, "Spare%20Item%20Pending", "Spare%20Pending", mobile_no, TicketNo, "E", mobile_details, dbversion, tss);

                    } else if (jobstarttime.length() == 0) {

                        showerror("Please Scan Serial no for getting job start Time");
                    } else if (custsign == null) {

                        showerror("Please Collect Customer Signatutre");
                    } else if (ctcstatus.equals("true") && Product.equals("AC") && ac_photo_status.equals("false") && serial_photo_status.equals("false")) {

                        showerror("Please Capture Serial No And Odu Serial No Photo");

                    } else if (ctcstatus.equals("true") && Product.equals("AC") && ac_photo_status.equals("false") && serial_photo_status.equals("true")) {

                        showerror("Please Capture Serial No And Odu Serial No Photo");

                    } else if (ctcstatus.equals("true") && Product.equals("AC") && ac_photo_status.equals("true") && serial_photo_status.equals("false")) {

                        showerror("Please Capture Serial No And Odu Serial No Photo");

                    } else if (ctcstatus.equals("true") && serial_photo_status.equals("false")) {

                        showerror("Please Capture Serial No Photo");

                    } else if ( MachinStatus.equals("OG") && !ogtstatus()) {

                        //  showerror("");

                    } else if (CallType.equals("MANDATORY CALLS")) {

                        if (mcheck.length() == 0) {

                            showerror("Please Select Mandratary Check List");
                        }
                        if (water_code.length() == 0) {

                            showerror("Please Select Water Source");

                        } else if (tds_level.length() == 0) {
                            showerror("Please Enter TDS Level");

                        } else {


                            submit_Value();
                        }
                    } else if (CallType.equals("INSTALLATION CALL")) {

                        if (SerialNo.length() == 0) {

                            showerror("Please scan serial no");
                        } else if (DOP.length() == 0) {

                            showerror("Please Select DOP");
                        } else if (DOI.length() == 0) {

                            showerror("Please Select DOI");
                        } else if (Product.equals("AC") && oduserno.length() == 0) {

                            showerror("Please Scan ODU Serisal No And Capture ODU Serial No Photo");
                        } else if (water_code.length() == 0) {

                            showerror("Please Select Water Source");

                        } else if (tds_level.length() == 0) {

                            showerror("Please Enter TDS Level");

                        } else {

                            submit_Value();
                        }
                    } else {

                        if (water_code.length() == 0) {

                            showerror("Please Select Water Source");

                        } else if (tds_level.length() == 0) {

                            showerror("Please Enter TDS Level");

                        } else {

                            submit_Value();

                        }
                    }
                }

                break;
            case R.id.ll_add_part:
                add_part();
               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Add%20Spare%20Icon", TAG, "Spare%20Tab", "Spare%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                break;
            case R.id.iv_doi:

               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Doi%20Date%20Icon", TAG, "Product%20Tab", "Product%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);

                if (DOP.equals("01-Jan-1900")) {

                    showerror("Please select DOP");
                } else {

                    update_doi();
                }
                break;

            case R.id.iv_dop:
               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Dop%20Date%20Icon", TAG, "Product%20Tab", "Product%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                update_dop();

                break;
            case R.id.tv_ess_quentity:

              //  errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Essential%20Quantity", TAG, "Essential%20Tab", "Essential%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                update_ess_quantity(v);
                break;

            case R.id.ll_add_ess:

             //   errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Add%20Essential%20Icon", TAG, "Essential%20Tab", "Essential%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                add_ess();
                break;
            case R.id.btn_scan_serial_no:

               // errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Product%20SerialNo%20Scan%20Icon", TAG, "Product%20Tab", "Product%20Tab", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                scan_serialNo();
                break;

            case R.id.btn_openmap:

                openGoolgemap();
              //  errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Get%20Direction%20Button", TAG, "Customer%20Tab", "For%20Open%20Google%20Map", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                break;

            case R.id.ll_add_details:
                  checkproduct();

                break;
            case R.id.btn_scan_odu_serial_no:
                 scanoduno();
                break;
            case R.id.lv_camera_invioce:
                launchCameraIntent("invoice");

                break;
            case R.id.lv_camera_serialno:
                launchCameraIntent("serialno");

                break;
            case R.id.lv_camera_oduserialno:
                launchCameraIntent("oduserialno");
                  break;

            case R.id.iv_message:
                send_sms_to_customer();
                errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Message%20Icon", TAG, "Header_Section", "Click%20On%20Message", mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
                break;

            case R.id.view_invioce:
                view_image("invioce");

                break;

            case R.id.view_oduserialno:
                view_image("oduserialno");

                break;

            case R.id.view_serialno:
                view_image("serialno");

                break;

            case R.id.btn_updateaddress:
                startActivity(new Intent(CustomerDetailsActivity.this, UpdateAddress.class));
                break;


            case R.id.tv_custsign:
                lunchSignIntent();
                break;

            case R.id.lv_custsign:
                lunchSignIntent();
                break;


        }
    }

    public void send_sms_to_customer() {

    }

    public void choose_re_date() {

        datestatus = "redate";
        DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDetailsActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.getDatePicker().setMaxDate((System.currentTimeMillis() - 1000) + (1000 * 60 * 60 * 24 * 15));

        datePickerDialog.show();

    }

    public void choose_icr_date() {
        datestatus = "icrdate";

        DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDetailsActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar calendar2 = Calendar.getInstance();
        int year = calendar2.get(Calendar.YEAR);
        int month = calendar2.get(Calendar.MONTH);

        int today = calendar2.get(Calendar.DAY_OF_MONTH);

        if(today == 1){

            calendar2.set(year, month -1, 26);

        }

       else if (today < 26) {

            calendar2.set(year, month, 2);


        } else {

            calendar2.set(year, month, 26);

        }

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        System.out.println("  " + year + "   " + month);
        datePickerDialog.getDatePicker().setMinDate(calendar2.getTimeInMillis());

        datePickerDialog.show();


    }

    public void choose_time() {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CustomerDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                try {

                    String minut, hours;

                    if (selectedHour < 10) {

                        hours = "0" + selectedHour;
                    } else {
                        hours = "" + selectedHour;
                    }
                    if (selectedMinute < 10) {

                        minut = "0" + selectedMinute;
                    } else {
                        minut = "" + selectedMinute;
                    }

                    stime = hours + "" + minut + "00";

                    errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Re_scheduled%20Time", TAG, "Status%20Tab", stime, mobile_no, TicketNo, "U", mobile_details, dbversion, tss);


                    tv_select_time.setText(hours + ":" + minut);


                    //    All_click.add("Select%20Re_scheduled%20Time"+hours + ":" + minut);
                } catch (Exception e) {

                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(e);
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void getpendingdata() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            //  progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            //  String url= AllUrl.baseUrl+"spares/getspare?spareaccadd.Ticketno=2002374814&spareaccadd.Status=Pending&spareaccadd.ItemType=ZACC";
            String url = AllUrl.baseUrl + "spares/getspare?spareaccadd.Ticketno=" + TicketNo;

           // String url = AllUrl.baseUrl + "spares/getspare?spareaccadd.Ticketno=1007499006";
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

    public String Parsedates(String time) {

        String inputPattern = "dd/MM/yyyy hh:mm:ss a";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.ENGLISH);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
            //  System.out.println(str);
        } catch (ParseException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return str;
    }

    public void ScanPart() {

        Intent i = new Intent(CustomerDetailsActivity.this, BarcodeCamera.class);
        startActivityForResult(i, SCAN_PART);

    }

    public String getbrand(String brand) {

        String brandcode;
        brandcode = map_machine.get(brand);
        // System.out.println(brandcode);
        return brandcode;
    }

    public String getyear(String gyear) {

        String yearcode;

        if (gyear.equals("> 5")) {
            yearcode = "X";
        } else {
            yearcode = "";
        }
        return yearcode;
    }

    public String getresi(String types) {

        String resicode;

        resicode = map_residence.get(types);

        return resicode;
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

        final Dialog dialog = new Dialog(CustomerDetailsActivity.this);

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

    public void getBomdata(String FrCode, String Product, String FGProductg) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);

            //  String urls= AllUrl.baseUrl+"bom?bom.FrCode="+FrCode+"&bom.MaterialCategory="+Product+"&bom.FGProduct="+FGProductg;

            String urls = AllUrl.baseUrl+"bom/getBomMaster?bom.MaterialCategory=" + Product + "&bom.FGProduct=" + FGProductg;

            System.out.println("bom urls-->" + urls);

            //  System.out.println("Customer page--> get all Spare"+TicketNo+" "+ urls);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urls,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                progressBar.setVisibility(View.GONE);


                                JSONArray jsonArray = new JSONArray(response);

                                spareLists.clear();
                                spareLists.add(new SpareList("-- select spare --", ""));

                                spareCodeLists.clear();
                                spareCodeLists.add(new SpareCodeList("-- select code --", ""));

                                codelist = new ArrayList<>();
                                codelist.add("");


                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    Component = obj.getString("Component");
                                    ComponentDescription = obj.getString("ComponentDescription");
                                    FGDescription = obj.getString("FGDescription");
                                    FGProduct = obj.getString("FGProduct");
                                    FrCodes = obj.getString("FrCode");
                                    MaterialCategory = obj.getString("MaterialCategory");
                                    good_stock = obj.getString("good_stock");
                                    refurbished_stock = obj.getString("refurbished_stock");

                                    codelist.add(obj.getString("Component"));
                                    spareLists.add(new SpareList(obj.getString("ComponentDescription"), obj.getString("Component")));

                                    spareCodeLists.add(new SpareCodeList(obj.getString("Component"), obj.getString("ComponentDescription")));
//                                    boolean status= dbhelper.insert_bom_data(ComponentDescription,Component,FGDescription,FGProduct,FrCodes,MaterialCategory,good_stock,refurbished_stock,tss);
//                                   // System.out.println("status"+status);


                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        ArrayAdapter spare_adapter = new ArrayAdapter(CustomerDetailsActivity.this, android.R.layout.simple_spinner_item, spareLists);
                                        spare_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner.setAdapter(spare_adapter);
                                        spinner.setOnItemSelectedListener(CustomerDetailsActivity.this);


                                        ArrayAdapter<String> arrayAdapters = new ArrayAdapter(CustomerDetailsActivity.this, R.layout.spinner_item, spareCodeLists);
                                        arrayAdapters.setDropDownViewResource(R.layout.spinner_dropdown_item);
                                        spinner_part_code.setAdapter(arrayAdapters);
                                        spinner_part_code.setOnItemSelectedListener(CustomerDetailsActivity.this);

                                    }
                                });

                                //  new Loadallbom().execute();

                            } catch (Exception e) {
                                e.printStackTrace();

                                FirebaseCrashlytics.getInstance().recordException(e);

                                //  Errorlog("get_spare",urls,e.getMessage().replace(" ","%20"),tss);
                            }

                            progressBar.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);

                    //  updateofflinespare(FrCode,Product,FGProductg);


                }
            });
// Add the request to the RequestQueue.

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        } else {

            showerror("No internet connection , Spare List Not Found");

        }

    }

    public void add_part_item(String partname, String partcode, String quentity, String flag, String spare_not_found, int stockqty, String date, String price, String totalprice) {

        model = new Add_item_model();
        model.setItemname(partname);
        model.setDescription(partcode);
        model.setCount(quentity);
        model.setFlag(flag);
        model.setCheck(spare_not_found);
        model.setStockqty(stockqty);
        model.setDate(date);
        model.setPrice(price);
        model.setTotal_price(totalprice);
        models.add(model);
        add_item_adapter = new Add_item_adapter(models, CustomerDetailsActivity.this, CustomerDetailsActivity.this, CustomerDetailsActivity.this);
        add_item_adapter.notifyDataSetChanged();
        lv_additem.setAdapter(add_item_adapter);

    }

    public String check_spare_pending() {

        String status = "";

        for (int p = 0; p < pendingSpares.size(); p++) {
            try {

                if (pendingSpares.get(p).getPending_fla().equals("Yes")) {
                    status = "true";
                    break;
                }

            } catch (Exception e) {

                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }

        }

        for (int m = 0; m < models.size(); m++) {

            try {
                if (models.get(m).getCheck().equals("Yes")) {
                    status = "true";
                    break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }

        }

        return status;
    }

    public void get_ess_name(String component) {

        try {

            Cursor ess_name_cursor;

            ess_name_cursor = dbhelper.get_ess_Name(component);

            if (ess_name_cursor != null) {

                if (ess_name_cursor.moveToFirst()) {

                    do {

                        final int recordid = ess_name_cursor.getInt(ess_name_cursor.getColumnIndex("recordid"));
                        // System.out.println("recordid-->" + recordid);
                        String ComponentDescription = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("ComponentDescription"));

                        //  All_click.add("Scan%20Essential%20Name%20"+ComponentDescription);

                        essComponent = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("Component"));
                        ItemType = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("ItemType"));
                        //   System.out.println(ComponentDescription + "    " +essComponent + "  "+ItemType);
                        tv_ess_component.setText(essComponent);
                        essComponentDescription = ComponentDescription;
                        accessories_stock = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("accessories_stock"));
                        additives_stock = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("additives_stock"));

                        accstock = string_to_int(accessories_stock);
                        addstock = string_to_int(additives_stock);

                        if (accstock < addstock) {
                            tv_ess_sqty.setText(additives_stock);
                            ess_stock = addstock;
                        } else {
                            ess_stock = accstock;
                            tv_ess_sqty.setText(accessories_stock);
                        }

                        int position = -1;
                        position = essspinerlist.indexOf(ComponentDescription);

                        ess_spiner.setSelection(position);

                    } while (ess_name_cursor.moveToNext());

                    ess_name_cursor.close();

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);

            // Errorlog("get%20offline%20essential","Essential%20Tab",e.getMessage().replace(" ","%20"),tss);
        }

    }

    public void showConfirmDialog(Context context, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        String positiveText = context.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic

                        tds_level = et_tds_level.getText().toString();
                        if (str_spnr_status.equals("Resolved (soft closure)")) {

                            if (check_spare_pending().equals("true")) {

                                showerror("Added Spare is Pending");
                                errorDetails.Errorlog(CustomerDetailsActivity.this, "Spare%20Pending", TAG, "Spare%20Item%20Pending", "Spare%20Pending", mobile_no, TicketNo, "E", mobile_details, dbversion, tss);

                            } else {


                                try {


                                    work_status = "E0008";
                                    createcsv("success", 0);
                                    check_delete_spare_list();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    FirebaseCrashlytics.getInstance().recordException(e);


                                }

                                try {

                                    if (imagestatus) {

                                        uploadImage();
                                    }

                                } catch (Exception e) {

                                    e.printStackTrace();
                                    FirebaseCrashlytics.getInstance().recordException(e);
                                }
                            }
                        } else {

                            work_status = "E0007";

                            check_delete_spare_list();


                            createcsv("success", 0);
                            flagstatus = "";
                            //sendsoftclosedata(spnr_status);

                            try {

                                if (imagestatus) {

                                    uploadImage();
                                }

                            } catch (Exception e) {

                                e.printStackTrace();
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        }
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
                        errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Alert%20Cancel%20Button", TAG, "Popup%20Close", "Popup%20Close", mobile_no, TicketNo, "U", mobile_details, dbversion, tss);

                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    // ---------------------------  declear all layout --------------------------------------
    public void init() {
        tv_error_message = findViewById(R.id.tv_error_message);
        tv_ess_calculation = findViewById(R.id.tv_ess_calculation);
        ll_ess_cal = findViewById(R.id.ll_ess_cal);
        ll_essential_main = findViewById(R.id.ll_essential_main);
        ll_essential_add = findViewById(R.id.ll_essential_add);

        ll_spare_main = findViewById(R.id.ll_spare_main);
        ll_spare_add = findViewById(R.id.ll_spare_add);
        ll_spare_header = findViewById(R.id.ll_spare_header);


        ll_ess_cal.setVisibility(View.GONE);
        spinner = findViewById(R.id.spinner);
        spinner_part_code = findViewById(R.id.spinner_part_code);
        lv_additem = findViewById(R.id.lv_add_item);
        spnr_remark = findViewById(R.id.spnr_remark);
        ll_details = findViewById(R.id.ll_details);
        ll_part = findViewById(R.id.ll_part);
        ll_esential = findViewById(R.id.ll_esential);
        ll_notes = findViewById(R.id.ll_note);
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

        tv_pending_status = findViewById(R.id.tv_pending_status);

        tv_sdate = findViewById(R.id.tv_sdate);
        tv_stime = findViewById(R.id.tv_stime);
        tv_ageing = findViewById(R.id.tv_ageing);
        tv_tel = findViewById(R.id.tv_tel);

        btn_openmap = findViewById(R.id.btn_openmap);
        btn_openmap.setOnClickListener(this);

        tv_part_code = findViewById(R.id.tv_part_code);
        tv_quentity = findViewById(R.id.tv_quentity);
        tv_quentity.setOnClickListener(this);

        ll_scan_part = findViewById(R.id.ll_scan_part);
        ll_scan_part.setOnClickListener(this);
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

        iv_doi = findViewById(R.id.iv_doi);
        iv_doi.setOnClickListener(this);
        iv_dop = findViewById(R.id.iv_dop);
        iv_dop.setOnClickListener(this);
        btn_scan_odu_serial_no = findViewById(R.id.btn_scan_odu_serial_no);
        btn_scan_odu_serial_no.setOnClickListener(this);

        scan_serialno = findViewById(R.id.btn_scan_serial_no);
        scan_serialno.setOnClickListener(this);
        tv_fgproduct = findViewById(R.id.tv_fgproduct);

        // status sction
        tv_icr_date = findViewById(R.id.tv_icr_date);
        tv_icr_error = findViewById(R.id.tv_icr_error);
        ll_icr_view = findViewById(R.id.ll_icr_view);


        et_icr_no = findViewById(R.id.et_icr_no);
        ll_icr_date = findViewById(R.id.ll_icr_date);
        ll_icr_date.setOnClickListener(this);
        cv_bill_section = findViewById(R.id.cv_bill_section);
        snpr_status = findViewById(R.id.spnr_status);
        snpr_status.setOnItemSelectedListener(this);
        // et_status = findViewById(R.id.et_status);
        btn_status = findViewById(R.id.btn_status);
        btn_status.setOnClickListener(this);
        tv_select_date = findViewById(R.id.tv_select_date);
        tv_select_time = findViewById(R.id.tv_select_time);

        ll_select_date = findViewById(R.id.ll_select_date);
        ll_select_date.setOnClickListener(this);
        ll_select_time = findViewById(R.id.ll_select_time);
        ll_select_time.setOnClickListener(this);
        ll_select_time.setClickable(false);
        ll_customer = findViewById(R.id.ll_customer);
        ll_customer.setOnClickListener(this);

        ll_spare = findViewById(R.id.ll_spare);
        ll_spare.setOnClickListener(this);

        ll_essential = findViewById(R.id.ll_essential);
        ll_essential.setOnClickListener(this);

        ll_sales = findViewById(R.id.ll_sales);
        ll_sales.setOnClickListener(this);
        ll_machine = findViewById(R.id.ll_machine);
        ll_machine.setOnClickListener(this);
        ll_status = findViewById(R.id.ll_status);
        ll_status.setOnClickListener(this);

        iv_info = findViewById(R.id.iv_info);
        iv_part = findViewById(R.id.iv_part);
        iv_ess = findViewById(R.id.iv_ess);
        iv_note = findViewById(R.id.iv_note);
        iv_machine = findViewById(R.id.iv_machine);
        iv_status = findViewById(R.id.iv_status);

        t_cus = findViewById(R.id.t_customer);
        t_mach = findViewById(R.id.t_machine);
        t_spare = findViewById(R.id.t_spare);
        t_essen = findViewById(R.id.t_essential);
        t_status = findViewById(R.id.t_status);
        t_note = findViewById(R.id.t_notes);
        tv_amount = findViewById(R.id.tv_amount);
        tv_spare_amount = findViewById(R.id.tv_spare_amount);
        tv_tot_amount = findViewById(R.id.tv_tot_amount);

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
        ll_change = findViewById(R.id.ll_change);
        ll_add_part = findViewById(R.id.ll_add_part);
        ll_add_part.setOnClickListener(this);

        lv_ess_add_item = findViewById(R.id.lv_ess_add_item);
        tv_ess_quentity = findViewById(R.id.tv_ess_quentity);
        tv_ess_quentity.setOnClickListener(this);
        ll_add_ess = findViewById(R.id.ll_add_ess);
        ll_add_ess.setOnClickListener(this);
        ll_add_details = findViewById(R.id.ll_add_details);
        ll_add_details.setOnClickListener(this);
        spnr_product = findViewById(R.id.spnr_product);
        spnr_machine = findViewById(R.id.spnr_machine);
        spnr_member = findViewById(R.id.spnr_member);
        spnr_residence = findViewById(R.id.spnr_residence);
        aSwitch = findViewById(R.id.simpleswitch);
        lv_product = findViewById(R.id.lv_product_list);

        ll_scan_ess = findViewById(R.id.ll_scan_ess);
        ll_scan_ess.setOnClickListener(this);
        ess_spiner = findViewById(R.id.ess_spinner);
        tv_ess_component = findViewById(R.id.tv_ess_component);
        tv_ess_sqty = findViewById(R.id.tv_ess_sqty);

        lv_pending_item = findViewById(R.id.lv_pending_item);
        checkBox = findViewById(R.id.sp_not_found);
        lv_camera_invioce = findViewById(R.id.lv_camera_invioce);
        lv_camera_invioce.setOnClickListener(this);
        tv_odu_ser_no = findViewById(R.id.tv_odu_ser_no);
        view_invioce = findViewById(R.id.view_invioce);
        view_invioce.setOnClickListener(this);

        lv_camera_serialno = findViewById(R.id.lv_camera_serialno);
        lv_camera_serialno.setOnClickListener(this);
        lv_camera_oduserialno = findViewById(R.id.lv_camera_oduserialno);
        lv_camera_oduserialno.setOnClickListener(this);
        view_serialno = findViewById(R.id.view_serialno);
        view_serialno.setOnClickListener(this);
        view_oduserialno = findViewById(R.id.view_oduserialno);
        view_oduserialno.setOnClickListener(this);
        iv_message = findViewById(R.id.iv_message);
        iv_message.setOnClickListener(this);

        ll_serial_no_image = findViewById(R.id.ll_serial_no_image);
        ll_odu_serial_no_image = findViewById(R.id.ll_odu_serial_no_image);
        ll_invioce_no_image = findViewById(R.id.ll_invioce_no_image);
        tv_rating = findViewById(R.id.tv_rating);
        btn_updateaddress = findViewById(R.id.btn_updateaddress);
        btn_updateaddress.setOnClickListener(this);

        ll_medium = findViewById(R.id.ll_medium);
        spnr_ess_value = findViewById(R.id.spnr_ess_value);
        spnr_ess_value.setOnItemSelectedListener(this);

        et_tds_level = findViewById(R.id.et_tds_level);


        spnr_water_source = findViewById(R.id.spnr_water_source);
        spnr_water_source.setOnItemSelectedListener(this);

        img_map = findViewById(R.id.img_map);
        tv_serialno_capturetime = findViewById(R.id.tv_serialno_capturetime);

        tv_serialno_capturetime.setText("hh:mm (00:00)");

        //image upload layout

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    spare_not_found = "X";
                }
            }
        });


        tv_sign = findViewById(R.id.tv_custsign);
        tv_sign.setOnClickListener(this);
        lv_custsign = findViewById(R.id.lv_custsign);
        lv_custsign.setOnClickListener(this);
        iv_customersign = findViewById(R.id.imgv_custsign);
        tv_modelname = findViewById(R.id.tv_modelname);
        spnr_mcheck = findViewById(R.id.multipleItemSelectionSpinner);
        tv_dealername = findViewById(R.id.tv_dealername);
        et_feedback = findViewById(R.id.et_feedback);
        ll_service_charge_view = findViewById(R.id.ll_service_charge_view);
        //  set all value
        update_All_TextBox_Value();
        // add notes section spinner value
        update_notes_Spinner();
        // add value into residence array list
        update_map_residence();
        // add value into machine array list
        update_map_machine();
        // create  member list and add into spinner
        update_Members();
        // get all esstional from localdb or sqlite
        getess();

//       compress no imege value
        new Compressimage().execute();

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
        iv_icr_verify = findViewById(R.id.iv_icr_verify);

        gethistory();
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

//                                    dbhelper.delete_sohistory("");
//                                    dbhelper.insert_sohistory_data(CustomerCode,obj.getString("TicketNo"),obj.getString("ProblemDescription"),"","");

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

    //------------------- add residanse value into hasmap residence ---------------------
    public void update_map_residence() {

        map_residence.put("1 BHK", "Z1");
        map_residence.put("2 BHK", "Z2");
        map_residence.put("3 BHK", "Z3");
        map_residence.put("4 BHK", "Z4");
        map_residence.put("BUNGLOW", "Z5");
    }

    //------------------- add machine value into hasmap machine ---------------------
    public void update_map_machine() {

        map_machine.put("SAMSUNG", "Z1");
        map_machine.put("CARRIER", "Z10");
        map_machine.put("DAIKIN", "Z11");
        map_machine.put("HITACHI", "Z12");
        map_machine.put("BLUESTAR", "Z13");
        map_machine.put("PHILIPS", "Z14");
        map_machine.put("IFB", "Z15");
        map_machine.put("OTHERS", "Z16");
        map_machine.put("LG", "Z2");
        map_machine.put("WHIRLPOOL", "Z3");
        map_machine.put("GODREJ", "Z4");
        map_machine.put("SONY", "Z5");
        map_machine.put("PANASONIC", "Z6");
        map_machine.put("HAIER", "Z7");
        map_machine.put("BOSCH", "Z8");
        map_machine.put("VOLTAS", "Z9");
    }

    // ----------------- add member value and member spinner dec ---------------------
    public void update_Members() {

        List memberlist = new ArrayList<Integer>();

        memberlist.add("No. of Members");

        for (int i = 0; i < 50; i++) {

            memberlist.add(Integer.toString(i));
        }

        // add  member list into spinner adapter

        ArrayAdapter member_adapter = new ArrayAdapter(this, R.layout.spinner_item, memberlist);
        member_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_member.setAdapter(member_adapter);
        spnr_member.setOnItemSelectedListener(this);

    }

    // -----------------   change layout as per button  click

    public void ActiveCustomerTab() {

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
        hideSoftKeyboard(CustomerDetailsActivity.this);

    }

    public void ActiveProductTab() {
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
        hideSoftKeyboard(CustomerDetailsActivity.this);
    }

    public void ActiveSpareTab() {

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

        hideSoftKeyboard(CustomerDetailsActivity.this);


        if (FGProducts.equals("")) {

            showerror("Spare List Not Found");
        }
    }

    public void ActiveEssTab() {


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
        hideSoftKeyboard(CustomerDetailsActivity.this);
    }

    public void ActiveNotesPage() {


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
        hideSoftKeyboard(CustomerDetailsActivity.this);

    }

    public void ActiveStatusPage() {

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

        getspareprice();
        if (check_spare_pending().equals("true")) {
            upadte_recheck_for_spare();
        } else {
            getremarkdata(CallType);
        }

    }

    // update all value in to  layout

    public void update_All_TextBox_Value() {

        try {


            // getting value from share pref
            prefdetails = getSharedPreferences("details", 0);
            ticketdetails = prefdetails.getString("details", "");

            //  System.out.println("details"+ticketdetails);

            JSONObject object1 = new JSONObject(ticketdetails);

            TicketNo = object1.getString("TicketNo");

            tv_modelname.setText(object1.getString("Model"));
            Model=object1.getString("Model");
            tv_Model.setText(object1.getString("Model"));
            tv_dealername.setText(object1.getString("DealerName"));
            String exchange = object1.getString("potential_exchange_flag");


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


            // getting spare data using fgproduct name

            if (FGProducts.equals("")) {


                // showerror("Spare List Not Found");

            } else {

                // getting spare value
                getBomdata(FrCode, Product, FGProducts);
            }
            // if installation call update layout


            if (CallType.equals("INSTALLATION CALL")) {

                iv_dop.setVisibility(View.VISIBLE);
                iv_doi.setVisibility(View.VISIBLE);

                updatecalender();

                // if installation call is ac update odu serialno scan active

                if (Product.equals("AC")) {

                    btn_scan_odu_serial_no.setVisibility(View.VISIBLE);

                } else {

                    btn_scan_odu_serial_no.setVisibility(View.GONE);
                }

            }

            // for otthers call update layout ( dop , doi, serial no , oduseral no update button)
            else {

                btn_scan_odu_serial_no.setVisibility(View.GONE);
                iv_dop.setVisibility(View.GONE);
                iv_doi.setVisibility(View.GONE);
            }

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

                    errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Call%20Icon", TAG, "Click%20On%20Call%20Icon", "AltNo__" + TelePhone, mobile_no, TicketNo, "C", mobile_details, dbversion, tss);
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

            // if  ticket status others like closed,cancel,negative response ,soft close hide notes and submit tab
            if (Status.equals("Closed") || Status.equals("Cancelled") || Status.equals("Negative Response from Custome") || Status.equals("Resolved (soft closure)") || Status.equals("Request Cancellation")) {

                ll_status.setClickable(false);
                ll_sales.setClickable(false);

                lv_camera_serialno.setVisibility(View.GONE);
                lv_camera_oduserialno.setVisibility(View.GONE);
                lv_camera_invioce.setVisibility(View.GONE);
                scan_serialno.setVisibility(View.GONE);
                iv_dop.setVisibility(View.GONE);
                iv_doi.setVisibility(View.GONE);
                btn_scan_odu_serial_no.setVisibility(View.GONE);

                ll_essential_main.setVisibility(View.GONE);
                ll_essential_add.setVisibility(View.GONE);

//              ll_spare_main.setVisibility(View.GONE);
//              ll_spare_header.setVisibility(View.GONE);
//              ll_spare_add.setVisibility(View.GONE);



            }

//          getting prevois added spare and sales essential

            getpendingdata();

            if (Status.equals("Pending")) {

                String[] sta = {"Select status", "Pending", "Resolved (soft closure)"};

                //  et_status.setHint("");

                ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sta);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                snpr_status.setAdapter(aa);
            } else if (Status.equals("Assigned")) {

                String[] sta = {"Select status", "Pending", "Resolved (soft closure)"};

                //  et_status.setHint("");

                ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sta);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                snpr_status.setAdapter(aa);
            }

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
                // tv_problemdescription.setVisibility(View.GONE);
                // tv_modelname.setText();

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

                    errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Call%20Icon", TAG, "Click%20On%20Call%20Icon", "callto__" + RCNNo, mobile_no, TicketNo, "C", mobile_details, dbversion, tss);

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

                getWaterQuality();
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

                getWaterQuality();
            } else if (product.equals("WM")) {
                tv_modelname.setText("Washing Machine");

                updateproduct("Washing Machine", product_name);

                getWaterQuality();

            } else if (product.equals("WP")) {
                tv_modelname.setText("Water Purifier");
                updateproduct("Water Purifier", product_name);
            } else if (product.equals("MW")) {
                tv_modelname.setText("Microwave Oven");
                updateproduct("Microwave Oven", product_name);

            } else {

                tv_modelname.setText(product);
                tv_Product.setText(object1.getString("Product"));
                et_tds_level.setVisibility(View.GONE);
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


               if (CallType.equals("COURTESY VISIT") || CallType.equals("INSTALLATION CALL")){

               }
               else {

                   tv_MachinStatus.setText("Out of Warranty");
                   ll_amc_view.setVisibility(View.GONE);
                   getservicePrice();
                   updateicrlayout();
               }


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


        if (CallType.equals("")) {


        }

        getuploaddoc();
        getMandatoryCheck();
        updatevalue();

    }

    // if user click in essentiall scan button

    public void scan_essential() {
        Intent i = new Intent(CustomerDetailsActivity.this, BarcodeCamera.class);
        startActivityForResult(i, SCAN_ESS_NO);

    }

    // for sumit all value
    public void submit_Value() {

        boolean essstatus = check_essential_empty();

        if (essstatus) {
            popupessential();
        } else {
            try {

                if (str_spnr_status.equals("Select status")) {
                    showerror("Please Select Status");
                } else {
                    showConfirmDialog(CustomerDetailsActivity.this, "Service Order Update", "Do you want to submit");
                }
            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }
    }

    // adding any spare into list
    public void add_part() {

        tv_error_message.setVisibility(View.GONE);

        if (checkBox.isChecked()) {

            spare_not_found = "Yes";
        } else {
            spare_not_found = "No";
        }

        if (!quentity.equals("0")) {

            checkBox.setChecked(false);

            model = new Add_item_model();
            Boolean check_part_In = check_part_In_List(partcode);


            if (check_part_In) {

                showerror("Already Exist");
            } else {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                try {

                    int bqty = string_to_int(quentity);

                    if (spare_not_found.equals("Yes")) {

                        if (bqty > bom_qty) {


                            //  add_part_item(partname,partcode,quentity,"I",spare_not_found);
                            model = new Add_item_model();
                            model.setItemname(partname);
                            model.setDescription(partcode);
                            model.setCount(quentity);
                            model.setFlag("I");
                            model.setCheck(spare_not_found);
                            model.setStockqty(bom_qty);
                            model.setDate(currentDateandTime);
                            model.setPrice("0");
                            model.setTotal_price("0");
                            models.add(model);
                            errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Add%20Spare%20Item", TAG, partname.replace(" ", "%20") + "%20" + partcode + "%20" + quentity + "%20" + spare_not_found, "Spare%20Item%20Add%20In%20List", mobile_no, TicketNo, "S", mobile_details, dbversion, tss);
                            add_item_adapter = new Add_item_adapter(models, CustomerDetailsActivity.this, CustomerDetailsActivity.this, CustomerDetailsActivity.this);
                            add_item_adapter.notifyDataSetChanged();
                            lv_additem.setAdapter(add_item_adapter);

                        } else {

                            if (partname.equals("-- select spare --")) {

                                showerror("Please select spare");
                            } else {
                                showerror("Stock Not Available");
                            }

                        }
                    } else if (bqty <= bom_qty) {

                        if (CheckConnectivity.getInstance(this).isOnline()) {

                            GetSparePrice getSparePrice = new GetSparePrice();

                            getSparePrice.SpareAmount(new GetSparePrice.Spareprice() {
                                @Override
                                public void onsuccess(String status, String result) {

                                    if (status.equals("true")) {


                                        model = new Add_item_model();
                                        model.setItemname(partname);
                                        model.setDescription(partcode);
                                        model.setCount(quentity);
                                        model.setFlag("I");
                                        model.setCheck(spare_not_found);
                                        model.setStockqty(bom_qty);
                                        model.setDate(currentDateandTime);
                                        model.setPrice(result);
                                        model.setTotal_price("" + Integer.parseInt(quentity) * Integer.parseInt(result));
                                        models.add(model);
                                        add_item_adapter = new Add_item_adapter(models, CustomerDetailsActivity.this, CustomerDetailsActivity.this, CustomerDetailsActivity.this);
                                        add_item_adapter.notifyDataSetChanged();
                                        lv_additem.setAdapter(add_item_adapter);

                                        errorDetails.Errorlog(CustomerDetailsActivity.this, "Spare_added", TAG, partname.replace(" ", "%20") + "%20" + partcode + "%20" + quentity + "%20" + spare_not_found, "Spare%20Add%20In%20List", mobile_no, TicketNo, "S", mobile_details, dbversion, tss);


                                    } else {

                                        model = new Add_item_model();
                                        model.setItemname(partname);
                                        model.setDescription(partcode);
                                        model.setCount(quentity);
                                        model.setFlag("I");
                                        model.setCheck(spare_not_found);
                                        model.setStockqty(bom_qty);
                                        model.setDate(currentDateandTime);
                                        model.setPrice("0");
                                        model.setTotal_price("0");
                                        models.add(model);
                                        errorDetails.Errorlog(CustomerDetailsActivity.this, "Spare_added", TAG, partname.replace(" ", "%20") + "%20" + partcode + "%20" + quentity + "%20" + spare_not_found, "Spare%20Add%20In%20List", mobile_no, TicketNo, "S", mobile_details, dbversion, tss);
                                        add_item_adapter = new Add_item_adapter(models, CustomerDetailsActivity.this, CustomerDetailsActivity.this, CustomerDetailsActivity.this);
                                        add_item_adapter.notifyDataSetChanged();
                                        lv_additem.setAdapter(add_item_adapter);

                                    }

                                }
                            }, CustomerDetailsActivity.this, Product, partcode);


                        } else {

                            model = new Add_item_model();
                            model.setItemname(partname);
                            model.setDescription(partcode);
                            model.setCount(quentity);
                            model.setFlag("I");
                            model.setCheck(spare_not_found);
                            model.setStockqty(bom_qty);
                            model.setDate(currentDateandTime);
                            model.setPrice("0");
                            model.setTotal_price("0");
                            models.add(model);
                            add_item_adapter = new Add_item_adapter(models, CustomerDetailsActivity.this, CustomerDetailsActivity.this, CustomerDetailsActivity.this);
                            add_item_adapter.notifyDataSetChanged();
                            lv_additem.setAdapter(add_item_adapter);
                            errorDetails.Errorlog(CustomerDetailsActivity.this, "Spare_added", TAG, partname.replace(" ", "%20") + "%20" + partcode + "%20" + quentity + "%20" + spare_not_found, "Spare%20Add%20In%20List", mobile_no, TicketNo, "S", mobile_details, dbversion, tss);


                        }


                    } else {


                        if (partname.equals("-- select spare --")) {
                            showerror("Please select spare");
                        } else {
                            showerror("Stock Not Available");
                            errorDetails.Errorlog(CustomerDetailsActivity.this, "Spare_quentity", TAG, partname.replace(" ", "%20") + "%20" + partcode + "%20" + quentity + "%20" + spare_not_found, "Spare%20Add%20In%20List", mobile_no, TicketNo, "S", mobile_details, dbversion, tss);

                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(e);
                }

            }

        } else {

            showerror("Please select quantity");

        }

    }

    // adding doi
    public void update_doi() {

        datestatus = "doi";
        DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDetailsActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        //following line to restrict future date selection
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.getDatePicker().setMinDate(milestone);
        datePickerDialog.show();


    }

    // adding dop
    public void update_dop() {

        datestatus = "dop";

        try {

            DatePickerDialog datePickerDialog = new DatePickerDialog(CustomerDetailsActivity.this, date, calendar2
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    calendar2.get(Calendar.DAY_OF_MONTH));

            //   System.out.println("calender-->"+calendar2.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(calendar2.getTimeInMillis());
            // datePickerDialog.getDatePicker().setMinDate();
            datePickerDialog.show();


        } catch (Exception e) {

            FirebaseCrashlytics.getInstance().recordException(e);

            e.printStackTrace();
        }
    }

    // open number picker
    public void update_ess_quantity(View v) {
        showNumberPicker(v);
    }

    // add essential into essential added list

    public void add_ess() {

        int eqty = string_to_int(quentity);

        if (!quentity.equals("0")) {

            essential_add_model = new Essential_add_model();

            Boolean status = checkesscode(essComponent);

            if (status) {
                showerror("Essential Already Exists");
            } else {

                if (eqty <= ess_stock) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    essential_add_model.setEname(essComponentDescription);
                    essential_add_model.setEcode(essComponent);
                    essential_add_model.setEquentity(quentity);
                    essential_add_model.setItemtype(ItemType);
                    essential_add_model.setFlag("I");
                    essential_add_model.setDate(currentDateandTime);
                    essential_add_models.add(essential_add_model);

                    errorDetails.Errorlog(CustomerDetailsActivity.this, "Click%20On%20Essential%20Add", TAG, essComponentDescription.replace(" ", "%20") + "%20" + essComponent + "%20" + quentity + "%20" + ItemType, "Essential%20Tab", mobile_no, TicketNo, "", mobile_details, dbversion, tss);


                } else {

                    if (essComponentDescription.equals("-- select essential --")) {
                        showerror("Please select essential");

                    } else {
                        showerror("Stock Mismatch");
                    }


                }
            }
//                    }


            for (int e = 0; e < essential_add_models.size(); e++) {

                String value = essential_add_models.get(e).getFlag();
                //    System.out.println(value);
            }


            essential_add_adapter = new Essential_add_adapter(essential_add_models, CustomerDetailsActivity.this);
            essential_add_adapter.notifyDataSetChanged();
            lv_ess_add_item.setAdapter(essential_add_adapter);

        } else {

            showerror("Please Select quantity");
        }

    }

    // open scanner layout for seral no scan
    public void scan_serialNo() {

        Intent i = new Intent(CustomerDetailsActivity.this, SerialNoScan.class);
        startActivityForResult(i, SCANSERIALNO);


    }

    // open google map for get direction
    public void openGoolgemap() {

        errorDetails.Errorlog(CustomerDetailsActivity.this, "Open%20Google%20Map", TAG, Street + "," + City + "," + State + "," + PinCode, "Map%20Direction%20Addresss", mobile_no, TicketNo, "", mobile_details, dbversion, tss);

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Street + "," + City + "," + State + "," + PinCode + "");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

    // add residence,  product and machine adapter value into spinner
    public void update_notes_Spinner() {

        ArrayAdapter residence_adapter = new ArrayAdapter(this, R.layout.spinner_item, residence_list);
        residence_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_residence.setAdapter(residence_adapter);
        spnr_residence.setOnItemSelectedListener(this);


        ArrayAdapter product_adapter = new ArrayAdapter(this, R.layout.spinner_item, product_list);
        product_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_product.setAdapter(product_adapter);
        spnr_product.setOnItemSelectedListener(this);


        ArrayAdapter machine_adapter = new ArrayAdapter(this, R.layout.spinner_item, machine_list);
        machine_adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_machine.setAdapter(machine_adapter);
        spnr_machine.setOnItemSelectedListener(this);


    }

    // after scan serial no is valid or not
    public boolean checkSerialNo(String scanres) {
        boolean suc;

       // if (scanres.matches(validNumber)) {
        if(isNumeric(scanres) ){

            if (scanres.length() == 18) {

                suc = true;
            } else {
                suc = false;
                alert.showDialog(CustomerDetailsActivity.this, "Serial no scan error !!!", "Serial No Always 18 Digit. \nScan result - " + scanres + " \nsomething went wrong please try again");

            }

        } else {

            suc = false;
            alert.showDialog(CustomerDetailsActivity.this, "Invalid Serial No !!!", "Allow only numeric value. \nScan result - " + scanres + " \nsomething went wrong please try again");
        }

        return suc;
    }

    // open scanner value for scan odu serial no
    public void scanoduno() {

        Intent i = new Intent(CustomerDetailsActivity.this, SerialNoScan.class);
        startActivityForResult(i, ODU_NO);


    }


    // for take any type of photo  open image picker
    private void launchCameraIntent(String type) {
        Intent intent = new Intent(CustomerDetailsActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 16); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 9);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);

        if (type.equals("invoice")) {
            startActivityForResult(intent, INVOICE_IMAGE_CAPTURE);
        } else if (type.equals("serialno")) {
            startActivityForResult(intent, SERIALNO_IMAGE_CAPTURE);
        } else if (type.equals("oduserialno")) {
            startActivityForResult(intent, ODU_SERIALNO_IMAGE_CAPTURE);
        }

    }

    // go to customer siignaturw page for customer signature
    public void lunchSignIntent() {
        Intent intent = new Intent(CustomerDetailsActivity.this, CustomerSign.class);
        startActivityForResult(intent, CUSTOMER_SIGNATURE);
    }

    // upload all image serialno,odu serialno invoice and cust sign image upload to server
    private void uploadImage() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            String upload_URL = AllUrl.baseUrl + "doc/uploaddoc?";

            // System.out.println("upload_URL-->"+upload_URL);

            progressBar.setVisibility(View.VISIBLE);
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            rQueue.getCache().clear();

                            //   System.out.println(response.statusCode);
                            progressBar.setVisibility(View.GONE);

                            if (response.statusCode == 200) {

                                errorDetails.Errorlog(CustomerDetailsActivity.this, "image%20upload", TAG, "Image%20upload", "" + response.statusCode, mobile_no, TicketNo, "U", mobile_details, dbversion, tss);
                                dbhelper.insert_offline_image_data(TicketNo, seraialno_image_data, oduserialno_image_data, invoice_image_data, customersign_data, tss, "upload");

                            } else {

                                dbhelper.insert_offline_image_data(TicketNo, seraialno_image_data, oduserialno_image_data, invoice_image_data, customersign_data, tss, "notupload");
                                errorDetails.Errorlog(CustomerDetailsActivity.this, "image%20upload", TAG, "Image%20upload", "" + response.statusCode, mobile_no, TicketNo, "E", mobile_details, dbversion, tss);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            //   System.out.println("Image upload error");
                            errorDetails.Errorlog(CustomerDetailsActivity.this, "image%20upload", TAG, "Image%20upload", "error", mobile_no, TicketNo, "E", mobile_details, dbversion, tss);

                            dbhelper.insert_offline_image_data(TicketNo, seraialno_image_data, oduserialno_image_data, invoice_image_data, customersign_data, tss, "notupload");

                            progressBar.setVisibility(View.GONE);


                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    //model.AppDocUpload.
                    params.put("TicketNo", TicketNo);
                    params.put("UploadDate", tss);
                    params.put("ClientDocs", "ClientDocs");
                    // System.out.println(params);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();

                    // SerialPhoto
                    params.put("SerialPhoto", new DataPart("serialno_" + TicketNo + ".jpeg", seraialno_image_data));
                    params.put("ODUSerialPhoto", new DataPart("oduserialno_" + TicketNo + ".jpeg", oduserialno_image_data));
                    params.put("InvoicePhto", new DataPart("invoicephoto_" + TicketNo + ".jpeg", invoice_image_data));
                    params.put("SignaturePhoto", new DataPart("signature_" + TicketNo + ".jpeg", customersign_data));

                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(CustomerDetailsActivity.this);
            rQueue.add(volleyMultipartRequest);

        } else {

            dbhelper.insert_offline_image_data(TicketNo, seraialno_image_data, oduserialno_image_data, invoice_image_data, customersign_data, tss, "notupload");

        }
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
        final Dialog image_dialog = new Dialog(CustomerDetailsActivity.this);
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

            if (seraialno_image_data != null && type.equals("serialno")) {

                textView.setText("Serial No Photo");

                if (!serialnourl.equals("")) {


                    Picasso.get()
                            .load(serialnourl)

                            .into(item_image_view);


                } else {

                    Bitmap bmp = BitmapFactory.decodeByteArray(seraialno_image_data, 0, seraialno_image_data.length);
                    item_image_view.setImageBitmap(bmp);

                }


            } else if (oduserialno_image_data != null && type.equals("oduserialno")) {

                textView.setText("ODU Serial No Photo");

                if (!oduurl.equals("")) {

                    Picasso.get()
                            .load(oduurl)

                            .into(item_image_view);


                } else {
                    Bitmap bmp = BitmapFactory.decodeByteArray(oduserialno_image_data, 0, oduserialno_image_data.length);
                    item_image_view.setImageBitmap(bmp);
                }
            } else if (invoice_image_data != null && type.equals("invioce")) {
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

    // if user is offline get pending reason from dbhelper
    public void get_offline_pendingReasons(String CallType) {

        if (CallType.equals("SERVICE CALL")) {

            getdatabygroupcode("ZSER07");

        } else if (CallType.equals("MANDATORY CALLS")) {
            getdatabygroupcode("ZMAN07");

        } else if (CallType.equals("COURTESY VISIT")) {

            getdatabygroupcode("ZFOC07");

        } else if (CallType.equals("INSTALLATION CALL")) {

            getdatabygroupcode("ZINT07");
//           remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZINT07";
        } else if (CallType.equals("DEALER DEFECTIVE REQUEST")) {
            getdatabygroupcode("ZDD07");
            // remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZDD07";
        } else if (CallType.equals("REINSTALLLATION ORDER")) {
            getdatabygroupcode("ZRIN07");
            // remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZRIN07";
        } else if (CallType.equals("REWORK ORDER")) {
            getdatabygroupcode("ZREW07");
            // remarkurl = AllUrl.baseUrl + "Reasons/pendingReasons?pendingReasone.CodeGroup=ZREW07";
        }

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
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CustomerDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, remarklist);
                            spnr_remark.setAdapter(arrayAdapter);
                            // spnr_remark.setSelection(0,false);
                            spnr_remark.setOnItemSelectedListener(CustomerDetailsActivity.this);

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
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                                            .into(view_serialno);


                                    Picasso.get()
                                            .load(oduurl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                                            .into(view_oduserialno);


                                    Picasso.get()
                                            .load(invioceurl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
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
    public void popupessential() {

        final Dialog dialog = new Dialog(CustomerDetailsActivity.this);

        dialog.setContentView(R.layout.popupessentialnotsale);
        Button skip = dialog.findViewById(R.id.popup_btn_skip);
        Button ok = dialog.findViewById(R.id.popup_btn_ok);

        // if user click on skip buttton
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                try {

                    //check user select any status (pending or soft close )

                    if (str_spnr_status.equals("Select status")) {

                        showerror("Please Select Status");
                    }

                    //open confirm dialog for send data
                    else {

                        showConfirmDialog(CustomerDetailsActivity.this, "Service Order Update", "Do you want to submit");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(e);
                }
            }

        });

        // if user click on ok button go to essential tab

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActiveEssTab();
                dialog.dismiss();

            }
        });

        dialog.show();
    }

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
            ll_ess_cal.setVisibility(View.VISIBLE);
            String enddate = Valuefilter.Enddate(shelflife, essuses, quentity);
            tv_ess_calculation.setText(enddate);
        } else {
            ll_ess_cal.setVisibility(View.GONE);
        }

    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    //csv file creation section ...

    // ---------------------- getting all data and change as per crm data ------------------------

    public void createcsv(String smsstatus, int subid) {

        finaljsonArray = new JSONArray();

        if (work_status.equals("E0008")) {

            if (ctcstatus.equals("true")) {
                remarkcode = "Z023";
                updatectcerror(ctcmessage);
            } else {

                remarkcode = "";
            }
        }

        if (ctcstatus.equals("true")) {
            st_feedback = ctcmessage;
        } else {
            st_feedback = et_feedback.getText().toString();
        }

        //st_icr_no=et_icr_no.getText().toString();
        String icrdates = tv_icr_date.getText().toString();
        String redate = tv_select_date.getText().toString();
        String retime = tv_select_time.getText().toString();
        String dops = tv_DOP.getText().toString();
        String dois = tv_DOI.getText().toString();

        // insert other details in to local db for show the data
        dbhelper.insert_others_details(TicketNo, Status, SerialNo, dops, dois, icrdates, st_icr_no, st_feedback, nomen, residence, redate, retime, str_spnr_status, remark, "");

        //csv= Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+TicketNo+".csv";

        String rescheduledates = sdate + "" + stime;

        if (rescheduledates.equals("00000000000000")) {

            rescheduledate = "";
        } else {

            rescheduledate = sdate + "" + stime;
        }

        //"FL","TL","MW","DW","CD","AC","BIH","BIO"

        // getting fcustomer other details data from list

        for (int pr = 0; pr < add_product_models.size(); pr++) {

            String product = add_product_models.get(pr).getProduct();
            String company = add_product_models.get(pr).getMachine();
            String year = add_product_models.get(pr).getYear();
            dbhelper.insert_save_home(TicketNo, Status, product, year, company);

            switch (add_product_models.get(pr).getProduct()) {

                case "FL":
                    // zz_fl=add_product_models.get(pr).getProduct();
                    zz_fl = "X";
                    zz_flbrand = getbrand(add_product_models.get(pr).getMachine());
                    zz_mt5fl = getyear(add_product_models.get(pr).getYear());

                    break;

                case "TL":
                    zz_tl = "X";
                    zz_tlbrand = getbrand(add_product_models.get(pr).getMachine());
                    zz_mt5tl = getyear(add_product_models.get(pr).getYear());
                    break;

                case "MW":

                    zz_mw = "X";
                    zz_mwbrand = getbrand(add_product_models.get(pr).getMachine());
                    zz_mt5mw = getyear(add_product_models.get(pr).getYear());
                    break;
                case "DW":
                    zz_dw = "X";
                    zz_dwbrand = getbrand(add_product_models.get(pr).getMachine());
                    zz_mt5dw = getyear(add_product_models.get(pr).getYear());
                    break;

                case "CD":
                    //zz_cd,zz_cdbrand,zz_mt5cd
                    zz_cd = "X";
                    zz_cdbrand = getbrand(add_product_models.get(pr).getMachine());
                    zz_mt5cd = getyear(add_product_models.get(pr).getYear());
                    break;

                case "AC":
                    //  zz_ac,zz_acbrand,zz_mt5ac
                    zz_ac = "X";
                    zz_acbrand = getbrand(add_product_models.get(pr).getMachine());
                    zz_mt5ac = getyear(add_product_models.get(pr).getYear());
                    break;

                case "BIH":
                    //  zz_bih,zz_bihbrand,zz_mt5bih
                    zz_bih = "X";
                    zz_bihbrand = getbrand(add_product_models.get(pr).getMachine());
                    zz_mt5bih = getyear(add_product_models.get(pr).getYear());
                    break;

                case "BIO":
                    //  zz_bio,zz_biobrand,zz_mt5bio
                    zz_bio = "X";
                    zz_biobrand = getbrand(add_product_models.get(pr).getMachine());
                    zz_mt5bio = getyear(add_product_models.get(pr).getYear());
                    break;

                default:
                    break;
            }

        }

        try {


            int penspare= getpeningsparesize();
            int partsize = models.size();
            int esssize = essential_add_models.size();


            totallinecount = partsize + esssize + penspare;

            if (partsize == 0 && esssize == 0 && penspare == 0) {
                // insert value into json array
                createjson(PartnerId, TicketNo, Franchise, proces_type, SerialNo, oduserno, DOP, DOI, work_status, remarkcode, st_feedback, "00000000", rechaeck_date, rescheduledate, st_icr_no, st_icr_date, "", "", "", "", "", "", "", CustomerCode, residen, nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio, flagstatus, st_feedback);

            } else {

//                if (penspare != 0) {
//
//                    for (int pre = 0; pre < pendingSpares.size(); pre++) {
//
//                        String psflag = pendingSpares.get(pre).getFlags();
//                        String pspartname = pendingSpares.get(pre).getPartName();
//                        String pspartcode = pendingSpares.get(pre).getPartCode();
//                        String psqty = pendingSpares.get(pre).getQty();
//                        String pending_fla = pendingSpares.get(pre).getPending_fla();
//                        eta = Parsedates(pendingSpares.get(pre).getETA());
//                        String itemno = (pendingSpares.get(pre).getItemno());
//                        // System.out.println("peta-->"+eta);
//
//                        if (eta.equals("19000101")) {
//
//                            eta = "00000000";
//                        }
//
//                        String pflag = "";
//
//                        if (pending_fla.equals("Yes")) {
//                            pflag = "X";
//                        } else if (pending_fla.equals("No")) {
//
//                            pflag = "";
//                        }
//                        //  data.add(new String[]{TicketNo,Franchise,proces_type,SerialNo,oduserno, DOP, DOI, work_status, remarkcode, st_feedback, eta, rechaeck_date, rescheduledate,st_icr_no, st_icr_date, psflag, pspartcode,"",pflag, psqty, "PCS", itemno, CustomerCode, residen,nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio});
//                        createjson(PartnerId, TicketNo, Franchise, proces_type, SerialNo, oduserno, DOP, DOI, work_status, remarkcode, st_feedback, eta, rechaeck_date, rescheduledate, st_icr_no, st_icr_date, psflag, pspartcode, "", pflag, psqty, "PCS", itemno, CustomerCode, residen, nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio, "", st_feedback);
//                        dbhelper.insert_spare(TicketNo, Status, pspartname, pspartcode, pending_fla, psqty);
//
//                        System.out.println("Pending Spare Data--> " + psflag + " " + pspartcode + " " + pflag + " " + psqty);
//
//                    }
//                }
                if (partsize != 0) {
                    for (int p = 0; p < partsize; p++) {

                        String sflag = models.get(p).getFlag();
                        String scode = models.get(p).getDescription();
                        String sqty = models.get(p).getCount();
                        String spartname = models.get(p).getItemname();
                        String sp_not_founds = models.get(p).getCheck();

                        dbhelper.insert_spare(TicketNo, Status, spartname, scode, sp_not_founds, sqty);


                        if (spartname.equals("NA")) {

                            createjson(PartnerId, TicketNo, Franchise, proces_type, SerialNo, oduserno, DOP, DOI, work_status, remarkcode, st_feedback, "00000000", rechaeck_date, rescheduledate, st_icr_no, st_icr_date, sflag, scode, "", "", sqty, "PCS", "", CustomerCode, residen, nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio, "", st_feedback);


                        } else if (sp_not_founds.equals("Yes")) {


                            createjson(PartnerId, TicketNo, Franchise, proces_type, SerialNo, oduserno, DOP, DOI, work_status, remarkcode, st_feedback, "00000000", rechaeck_date, rescheduledate, st_icr_no, st_icr_date, sflag, scode, "", "X", sqty, "PCS", "", CustomerCode, residen, nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio, "", st_feedback);

                        } else {


                            createjson(PartnerId, TicketNo, Franchise, proces_type, SerialNo, oduserno, DOP, DOI, work_status, remarkcode, st_feedback, "00000000", rechaeck_date, rescheduledate, st_icr_no, st_icr_date, sflag, scode, "", "", sqty, "PCS", "", CustomerCode, residen, nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio, "", st_feedback);

                        }
                    }
                }

                if (esssize != 0) {

                    for (int e = 0; e < essential_add_models.size(); e++) {

                        String eflag = essential_add_models.get(e).getFlag();
                        String ecode = essential_add_models.get(e).getEcode();
                        String eqty = essential_add_models.get(e).getEquentity();
                        String esname = essential_add_models.get(e).getEname();
                        String etype = essential_add_models.get(e).getItemtype();
                        dbhelper.insert_save_ess(TicketNo, Status, esname, ecode, eqty, etype);

                        if (esname.equals("NA")) {

                            //  data.add(new String[]{PartnerId,TicketNo,Franchise,proces_type,SerialNo,oduserno,DOP,DOI,work_status,remarkcode,st_feedback,eta,rechaeck_date,rescheduledate,st_icr_no,st_icr_date,eflag,"",ecode,"",eqty,"PCS","",CustomerCode,residen,nomen,zz_fl,zz_flbrand,zz_mt5fl,zz_tl,zz_tlbrand,zz_mt5tl,zz_mw,zz_mwbrand,zz_mt5mw,zz_dw,zz_dwbrand,zz_mt5dw,zz_cd,zz_cdbrand,zz_mt5cd,zz_ac,zz_acbrand,zz_mt5ac,zz_bih,zz_bihbrand,zz_mt5bih,zz_bio,zz_biobrand,zz_mt5bio,""});
                            createjson(PartnerId, TicketNo, Franchise, proces_type, SerialNo, oduserno, DOP, DOI, work_status, remarkcode, st_feedback, eta, rechaeck_date, rescheduledate, st_icr_no, st_icr_date, eflag, "", ecode, "", eqty, "PCS", "", CustomerCode, residen, nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio, "", st_feedback);

                        } else {

                            createjson(PartnerId, TicketNo, Franchise, proces_type, SerialNo, oduserno, DOP, DOI, work_status, remarkcode, st_feedback, eta, rechaeck_date, rescheduledate, st_icr_no, st_icr_date, eflag, ecode, "", "", eqty, "PCS", "", CustomerCode, residen, nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio, "", st_feedback);


                            //  data.add(new String[]{PartnerId,TicketNo,Franchise,proces_type,SerialNo,oduserno,DOP,DOI,work_status,remarkcode,st_feedback,eta,rechaeck_date,rescheduledate,st_icr_no,st_icr_date,eflag,ecode,"","",eqty,"PCS","",CustomerCode,residen,nomen,zz_fl,zz_flbrand,zz_mt5fl,zz_tl,zz_tlbrand,zz_mt5tl,zz_mw,zz_mwbrand,zz_mt5mw,zz_dw,zz_dwbrand,zz_mt5dw,zz_cd,zz_cdbrand,zz_mt5cd,zz_ac,zz_acbrand,zz_mt5ac,zz_bih,zz_bihbrand,zz_mt5bih,zz_bio,zz_biobrand,zz_mt5bio,""});
                        }

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);

        }
    }

    private int getpeningsparesize() {

        int size=0;

        try {

            Gson gson = new Gson();
            String sparedata = gson.toJson(pendingSpares);

            System.out.println("spare data --> "+sparedata);

            Cursor pcursor;

            pcursor = dbhelper.get_pending_spare(TicketNo);

            if (pcursor != null && pcursor.getCount() >0) {

                if (pcursor.moveToFirst()) {

                    //    Ticketno TEXT, Status TEXT, Serialno TEXT, DOP TEXT , DOI TEXT , Icr_date TEXT, Icr_no TEXT, Feedback TEXT, Member TEXT, House TEXT, Re_date TEXT, Re_time TEXT, Chnge_status TEXT, Pend_res TEXT,Pend_causes TEXT );";

                    do {

                        try {

                            String json = pcursor.getString(pcursor.getColumnIndex("sparedetails"));


                            JSONArray jsonArray = new JSONArray(json);
                            JSONArray chngarray = new JSONArray(sparedata);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject essobj = jsonArray.getJSONObject(i);
                                JSONObject chngobj=chngarray.getJSONObject(i);

                                if (essobj.getString("PartCode").equals(chngobj.getString("PartCode"))){

                                    int qty=Integer.parseInt(essobj.getString("Qty"));
                                    String pending_fla=essobj.getString("pending_fla");
                                    if (pending_fla.equals("")){
                                        pending_fla="No";
                                    }

                                    int cqty=Integer.parseInt(chngobj.getString("Qty"));
                                    String cpending_fla=chngobj.getString("pending_fla");

                                    if (qty == cqty){

                                        if (pending_fla.equals(cpending_fla)){

                                            size=size+0;
                                        }
                                        else {
                                            size=size+1;

                                          //  String psflag = pendingSpares.get(pre).getFlags();
                                            String psflag = chngobj.getString("flags");
                                          //  String pspartname = pendingSpares.get(pre).getPartName();
                                            String pspartname = chngobj.getString("PartName");
                                          //  String pspartcode = pendingSpares.get(pre).getPartCode();
                                            String pspartcode=chngobj.getString("PartCode");
                                           // String psqty = pendingSpares.get(pre).getQty();
                                            String psqty =chngobj.getString("Qty");

                                          //  String pending_fla = pendingSpares.get(pre).getPending_fla();

                                            String spending_fla =chngobj.getString("pending_fla");
                                          //  eta = Parsedates(pendingSpares.get(pre).getETA());

                                            eta=Parsedates(chngobj.getString("ETA"));
                                          //  String itemno = (pendingSpares.get(pre).getItemno());
                                            String itemno=chngobj.getString("itemno");
                                            // System.out.println("peta-->"+eta);

                                            if (eta.equals("19000101")) {

                                                eta = "00000000";
                                            }

                                            String pflag = "";

                                            if (spending_fla.equals("Yes")) {
                                                pflag = "X";
                                            } else if (spending_fla.equals("No")) {

                                                pflag = "";
                                            }
                                            //  data.add(new String[]{TicketNo,Franchise,proces_type,SerialNo,oduserno, DOP, DOI, work_status, remarkcode, st_feedback, eta, rechaeck_date, rescheduledate,st_icr_no, st_icr_date, psflag, pspartcode,"",pflag, psqty, "PCS", itemno, CustomerCode, residen,nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio});
                                            createjson(PartnerId, TicketNo, Franchise, proces_type, SerialNo, oduserno, DOP, DOI, work_status, remarkcode, st_feedback, eta, rechaeck_date, rescheduledate, st_icr_no, st_icr_date, psflag, pspartcode, "", pflag, psqty, "PCS", itemno, CustomerCode, residen, nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio, "", st_feedback);
                                            dbhelper.insert_spare(TicketNo, Status, pspartname, pspartcode, pending_fla, psqty);

                                        }

                                    }
                                    else {

                                        size=size+1;

                                        //  String psflag = pendingSpares.get(pre).getFlags();
                                        String psflag = chngobj.getString("flags");
                                        //  String pspartname = pendingSpares.get(pre).getPartName();
                                        String pspartname = chngobj.getString("PartName");
                                        //  String pspartcode = pendingSpares.get(pre).getPartCode();
                                        String pspartcode=chngobj.getString("PartCode");
                                        // String psqty = pendingSpares.get(pre).getQty();
                                        String psqty =chngobj.getString("Qty");

                                        //  String pending_fla = pendingSpares.get(pre).getPending_fla();

                                        String spending_fla =chngobj.getString("pending_fla");
                                        //  eta = Parsedates(pendingSpares.get(pre).getETA());

                                        eta=Parsedates(chngobj.getString("ETA"));
                                        //  String itemno = (pendingSpares.get(pre).getItemno());
                                        String itemno=chngobj.getString("itemno");
                                        // System.out.println("peta-->"+eta);

                                        if (eta.equals("19000101")) {

                                            eta = "00000000";
                                        }

                                        String pflag = "";

                                        if (spending_fla.equals("Yes")) {
                                            pflag = "X";
                                        } else if (spending_fla.equals("No")) {

                                            pflag = "";
                                        }
                                        //  data.add(new String[]{TicketNo,Franchise,proces_type,SerialNo,oduserno, DOP, DOI, work_status, remarkcode, st_feedback, eta, rechaeck_date, rescheduledate,st_icr_no, st_icr_date, psflag, pspartcode,"",pflag, psqty, "PCS", itemno, CustomerCode, residen,nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio});
                                        createjson(PartnerId, TicketNo, Franchise, proces_type, SerialNo, oduserno, DOP, DOI, work_status, remarkcode, st_feedback, eta, rechaeck_date, rescheduledate, st_icr_no, st_icr_date, psflag, pspartcode, "", pflag, psqty, "PCS", itemno, CustomerCode, residen, nomen, zz_fl, zz_flbrand, zz_mt5fl, zz_tl, zz_tlbrand, zz_mt5tl, zz_mw, zz_mwbrand, zz_mt5mw, zz_dw, zz_dwbrand, zz_mt5dw, zz_cd, zz_cdbrand, zz_mt5cd, zz_ac, zz_acbrand, zz_mt5ac, zz_bih, zz_bihbrand, zz_mt5bih, zz_bio, zz_biobrand, zz_mt5bio, "", st_feedback);
                                        dbhelper.insert_spare(TicketNo, Status, pspartname, pspartcode, pending_fla, psqty);


                                    }

                                }



                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    } while (pcursor.moveToNext());


                }
            }
        }catch(Exception e){

            e.printStackTrace();
        }

        return size;
    }

    // -------------------------- creating final json array one by one -----------------------------

    public void createjson(String TechnicianCode, String TicketNo, String franchise_bp, String CallType, String serial_no, String odu_serial_no, String DOP, String DOY, String Status, String status_reason, String notes, String eta, String rechecked_date, String rescheduled_date, String ICR_no, String ICR_Date, String spare_flag, String product_code, String spare_serno, String pending_flag, String quantity, String unit, String itemno, String zz_bp, String zz_residence, String zz_members, String zz_fl, String zz_flbrand, String zz_mt5fl, String zz_tl, String zz_tlbrand, String zz_mt5tl, String zz_mw, String zz_mwbrand, String zz_mt5mw, String zz_dw, String zz_dwbrand, String zz_mt5dw, String zz_cd, String zz_cdbrand, String zz_mt5cd, String zz_ac, String zz_acbrand, String zz_mt5ac, String zz_bih, String zz_bihbrand, String zz_mt5bih, String zz_bio, String zz_biobrand, String zz_mt5bio, String status_flag, String remarks) {

        try {

            JSONObject jsonObject = new JSONObject();

            if(Status.equals("E0008") && firstrow.equals("true")){
                status_flag = "H";
                firstrow="false";
            }
            else {

                status_flag = "";
                firstrow="false";
            }

            // TechnicianCode,orderid,franchise_bp,CallType,serial_no,
            jsonObject.put("TechnicianCode", TechnicianCode);
            jsonObject.put("TicketNo", TicketNo);
            jsonObject.put("franchise_bp", franchise_bp);
            jsonObject.put("CallType", CallType);
            jsonObject.put("serial_no", serial_no);

            // odu_serial_no,DOP,DOI,Status,status_reason,
            jsonObject.put("odu_serial_no", odu_serial_no);
            jsonObject.put("DOP", DOP);
            jsonObject.put("DOI", DOI);
            jsonObject.put("Status", Status);
            jsonObject.put("status_reason", status_reason);

            // notes,eta,rechecked_date,rescheduled_date,

            jsonObject.put("notes", notes);
            jsonObject.put("eta", eta);
            jsonObject.put("rechecked_date", rechecked_date);
            jsonObject.put("rescheduled_date", rescheduled_date);

            // ICR_no,ICR_Date,spare_flag,product_code,

            jsonObject.put("ICR_no", ICR_no);
            jsonObject.put("ICR_Date", ICR_Date);
            jsonObject.put("spare_flag", spare_flag);
            jsonObject.put("product_code", product_code);

            // spare_serno,pending_flag,quantity,unit,itemno,

            jsonObject.put("spare_serno", spare_serno);
            jsonObject.put("pending_flag", pending_flag);
            jsonObject.put("quantity", quantity);
            jsonObject.put("unit", unit);
            jsonObject.put("itemno", itemno);

            // zz_bp,zz_residence,zz_members,zz_fl,zz_flbrand,
            jsonObject.put("zz_bp", zz_bp);
            jsonObject.put("zz_residence", zz_residence);
            jsonObject.put("zz_members", zz_members);
            jsonObject.put("zz_fl", zz_fl);
            jsonObject.put("zz_flbrand", zz_flbrand);

            // zz_mt5fl,zz_tl,zz_tlbrand,
            jsonObject.put("zz_mt5fl", zz_mt5fl);
            jsonObject.put("zz_tl", zz_tl);
            jsonObject.put("zz_tlbrand", zz_tlbrand);

            // zz_mt5tl,zz_mw,zz_mwbrand,zz_mt5mw,zz_dw,

            jsonObject.put("zz_mt5tl", zz_mt5tl);
            jsonObject.put("zz_mw", zz_mw);
            jsonObject.put("zz_mwbrand", zz_mwbrand);
            jsonObject.put("zz_mt5mw", zz_mt5mw);
            jsonObject.put("zz_dw", zz_dw);

            // zz_dwbrand,zz_mt5dw,zz_cd,zz_cdbrand,zz_mt5cd,
            jsonObject.put("zz_dwbrand", zz_dwbrand);
            jsonObject.put("zz_mt5dw", zz_mt5dw);
            jsonObject.put("zz_cd", zz_cd);
            jsonObject.put("zz_cdbrand", zz_cdbrand);
            jsonObject.put("zz_mt5cd", zz_mt5cd);

            // zz_ac,zz_acbrand,zz_mt5ac,zz_bih,zz_bihbrand,

            jsonObject.put("zz_ac", zz_ac);
            jsonObject.put("zz_acbrand", zz_acbrand);
            jsonObject.put("zz_mt5ac", zz_mt5ac);
            jsonObject.put("zz_bih", zz_bih);
            jsonObject.put("zz_bihbrand", zz_bihbrand);

            // zz_mt5bih,zz_bio,zz_biobrand,zz_mt5bio,status_flag,remarks

            jsonObject.put("zz_mt5bih", zz_mt5bih);
            jsonObject.put("zz_bio", zz_bio);
            jsonObject.put("zz_biobrand", zz_biobrand);
            jsonObject.put("zz_mt5bio", zz_mt5bio);
            jsonObject.put("status_flag", status_flag);
            jsonObject.put("remarks", "");
            jsonObject.put("recheck_rsn", "");


            // new
//            jsonObject.put("lv_1stresp_app_slot","");///stop first
//            jsonObject.put("lv_1stresp_app_date",""); /// date
//            jsonObject.put("lv_1stresp_app_remark",""); //remarks
//            jsonObject.put("lv_job_start_time",jobstarttime); //start
//            jsonObject.put("lv_water_type",water_code);
//            jsonObject.put("lv_TDS_level",tds_level);
//
//            lv_1stresp_app_slot { get; set; }
//            public string lv_1stresp_app_date { get; set; }
//            public string lv_1stresp_app_remark { get; set; }
//            // JOB STRAT TIME AND END TIME
//
//            public string lv_job_start_time { get; set; }
//
//            public string lv_water_type { get; set; }
//            public string lv_TDS_level { get; set; }

            finaljsonArray.put(jsonObject);
            btn_status.setClickable(false);
            btn_status.setBackgroundColor(Color.GRAY);

            // submit data into server
            if (totallinecount == finaljsonArray.length()) {

                if (CheckConnectivity.getInstance(this).isOnline()) {

                   // System.out.println("finaljsonArray-->" + finaljsonArray);
                    submitfinaldata(finaljsonArray);
                } else {

                   // System.out.println("finaljsonArray-->" + finaljsonArray);
                     dataforoffline();

                }
            } else if (totallinecount == 0) {

                if (CheckConnectivity.getInstance(this).isOnline()) {

                 //   System.out.println("finaljsonArray-->" + finaljsonArray);
                    submitfinaldata(finaljsonArray);

                } else {

                  //  System.out.println("finaljsonArray-->" + finaljsonArray);
                      dataforoffline();

                }
            }
        } catch (Exception e) {

            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }



    }


    // ------------------ Insert no imagge value into all image data ---------------------------

    public class Compressimage extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try {

                Thread.sleep(3000);
                imagebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                ByteArrayOutputStream byteArrayOutputStreams = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStreams);

                invoice_image_data = byteArrayOutputStreams.toByteArray();
                seraialno_image_data = byteArrayOutputStreams.toByteArray();
                oduserialno_image_data = byteArrayOutputStreams.toByteArray();
                customersign_data = byteArrayOutputStreams.toByteArray();


            } catch (Exception e) {

            }
            return null;
        }

        protected void onPostExecute(Void v) {


        }
    }

    // ----------------------------- after scan serial no check serial no is valid or not ------------------

    @Override
    public void modelresult(String output) {


        //  anyType{DCDate=2021-09-03T00:00:00; ModelCode=8903287005367; ModelcripDestion=MICRO WAVE OVEN 20BC4; ProductCategory=MW; ProductSubCategory=CM; Result=0; SerialNo=005367210815040129; }
        System.out.println("output -- > " + output);

        boolean b = output.contains("Result=1");
        boolean c = output.contains("Result=0");
        boolean d = output.contains("ProductCategory=" + Product);
        try{

            String[] data_array=output.split(";");
            String data=data_array[2];
            String[] model= data.split("=");
            String ModelcripDestion=model[1];

            System.out.println("ModelcripDestion-->"+ModelcripDestion);

            if (Model.length()==0){

                tv_Model.setText(ModelcripDestion);
            }

        }catch (Exception e){

            e.printStackTrace();
        }



        if (CallType.equals("INSTALLATION CALL")) {

            if (d) {

                if (b) {

                    SerialNo = serialno_scan;
                    tv_SerialNo.setText(SerialNo);
                    ctcstatus = "false";
                    ctcmessage = "";

                } else if (c) {

                    SerialNo = serialno_scan;
                    tv_SerialNo.setText(SerialNo);
                    ctcstatus = "false";
                    ctcmessage = "";
                } else {

                    SerialNo = serialno_scan;
                    tv_SerialNo.setText(SerialNo);
                    boolean p = output.contains("ModelCode=null");
                    ctcstatus = "true";
                    ctcmessage = "Invalid Serial ID";
                    st_feedback = "";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Alertwithicon alertwithicon = new Alertwithicon();
                            alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "Invalid Serial ID\n Please Capture Serial No photo ", false);

                        }
                    });
                }
            }
            else {

                SerialNo = serialno_scan;
                tv_SerialNo.setText(SerialNo);
                boolean p = output.contains("ModelCode=null");


                ctcstatus = "true";
                ctcmessage = "Invalid Serial ID";
                st_feedback = "";


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Alertwithicon alertwithicon = new Alertwithicon();
                        alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "Invalid Serial ID\n Please Capture Serial No photo ", false);

                    }
                });

            }


        }
        else if (SerialNo.length() == 0) {


            if (d) {

                if (b) {

                    SerialNo = serialno_scan;
                    tv_SerialNo.setText(SerialNo);
                    ctcstatus = "false";
                    ctcmessage = "";

                } else if (c) {

                    SerialNo = serialno_scan;
                    tv_SerialNo.setText(SerialNo);
                    ctcstatus = "false";
                    ctcmessage = "";
                } else {

                    SerialNo = serialno_scan;
                    tv_SerialNo.setText(SerialNo);
                    boolean p = output.contains("ModelCode=null");

                    Alertwithicon alertwithicon = new Alertwithicon();
                    alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "Invalid Serial ID\n Please Capture Serial No photo ", false);
                    ctcstatus = "true";
                    ctcmessage = "Invalid Serial ID";
                    st_feedback = "";
                }
            } else {

                SerialNo = serialno_scan;
                tv_SerialNo.setText(SerialNo);
                boolean p = output.contains("ModelCode=null");

                Alertwithicon alertwithicon = new Alertwithicon();
                alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "Invalid Serial ID\n Please Capture Serial No photo ", false);

                ctcstatus = "true";
                ctcmessage = "Invalid Serial ID";
                st_feedback = "";
            }


        } else {

        if (d) {

                if (b) {

//                    SerialNo=serialno_scan;
//                    tv_SerialNo.setText(SerialNo);
                    ctcstatus = "false";
                    ctcmessage = "";

                } else if (c) {

//                    SerialNo=serialno_scan;
//                    tv_SerialNo.setText(SerialNo);
                    ctcstatus = "false";
                    ctcmessage = "";
                } else {

                    SerialNo = serialno_scan;
                    tv_SerialNo.setText(SerialNo);
                    boolean p = output.contains("ModelCode=null");

                    Alertwithicon alertwithicon = new Alertwithicon();
                    alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "Invalid Serial ID\n Please Capture Serial No photo ", false);
                    ctcstatus = "true";
                    ctcmessage = "Invalid Serial ID";
                    st_feedback = "";
                }
            }
        else {

                Alertwithicon alertwithicon = new Alertwithicon();
                alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "Invalid Serial ID\n Please Capture Serial No photo ", false);
                ctcstatus = "true";
                ctcmessage = "Invalid Serial ID";
                st_feedback = "";
            }
        }
    }

    //  --------------------- Submit final data into server  -----------------------

    public void submitfinaldata(JSONArray jsonArray) {

       String url = AllUrl.baseUrl + "ZTECHSO/AddNewZTechSO";

        // String url=AllUrl.baseUrl+"so/softcloser-by-app";

        System.out.println(url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String requestBody = jsonArray.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                uploadstarttime();
                updatesparedata();

                if (response.equals("200")) {
                    submitstatus="true";
                    // if data insert success then insert submit ticket detais
                    dbhelper.insert_read_data(TicketNo, tss, ChangeDate, Status, str_spnr_status);
                    errorDetails.Errorlog(CustomerDetailsActivity.this, "File%20Upload", TAG, "Data%20Upload%20done", response, mobile_no, TicketNo, "S", mobile_details, dbversion, tss);


                    closeticket("Data Saved SuccessFully");

                } else {

                    submitstatus="true";
                    errorDetails.Errorlog(CustomerDetailsActivity.this, "File%20Upload", TAG, "Data%20upload%20error", response, mobile_no, TicketNo, "S", mobile_details, dbversion, tss);
                    // if data submit time error find save data for offline
                    dataforoffline();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorDetails.Errorlog(CustomerDetailsActivity.this, "File%20Upload", TAG, "Data%20upload%20error", "other%20errror", mobile_no, TicketNo, "S", mobile_details, dbversion, tss);
                dataforoffline();


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

                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);

    }

    // ---------------------- for closing  animation --------------------------------------
    public void closeticket(String result) {

        tv_error_message.setVisibility(View.VISIBLE);
        tv_error_message.setText(result);

        Animation animation;
        animation = AnimationUtils.loadAnimation(CustomerDetailsActivity.this, R.anim.slide_down);
        tv_error_message.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // yourMethod();
                tv_error_message.setAnimation(null);
                tv_error_message.setVisibility(View.GONE);
                startActivity(new Intent(CustomerDetailsActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);   //5 seconds

    }

    // -------------------------- save oofline data into local db and submit the data ------------------
    public void dataforoffline() {

        boolean status = dbhelper.insert_offlineservice_data(TicketNo, Valuefilter.getdate(), finaljsonArray.toString(), ticketdetails);
        if (status) {

            submitstatus="true";
            dbhelper.insert_read_data(TicketNo, tss, ChangeDate, Status, str_spnr_status);
            errorDetails.Errorlog(CustomerDetailsActivity.this, "File%20save", TAG, "Data%20save%20in%20localdb", "" + status, mobile_no, TicketNo, "S", mobile_details, dbversion, tss);
            closeticket("Offline Data Saved Successfully");

            WorkManager workManager = WorkManager.getInstance(getApplicationContext());
            Constraints.Builder constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED);

            OneTimeWorkRequest downloadSyncRequest = new OneTimeWorkRequest.Builder(OfflineService.class)
                    .setConstraints(constraints.build())
                    .setBackoffCriteria(
                            BackoffPolicy.LINEAR,
                            OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                            TimeUnit.MICROSECONDS)
                    .build();

            workManager.enqueueUniqueWork(UNIQUE_PULL_DATA_FROM_SERVER, ExistingWorkPolicy.REPLACE, downloadSyncRequest);


        } else {
            errorDetails.Errorlog(CustomerDetailsActivity.this, "File%20save", TAG, "Data%20save%20in%20localdb", "false", mobile_no, TicketNo, "S", mobile_details, dbversion, tss);

            tv_error_message.setVisibility(View.VISIBLE);

            tv_error_message.setText("Offline Data Not saved. Please Reclick On Submit Button");
            btn_status.setEnabled(true);

            btn_status.setBackgroundColor(Color.RED);

            Animation animation;
            animation = AnimationUtils.loadAnimation(CustomerDetailsActivity.this, R.anim.slide_down);
            tv_error_message.startAnimation(animation);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    // yourMethod();
                    tv_error_message.setAnimation(null);
                    tv_error_message.setVisibility(View.GONE);

                }
            }, 2000);
        }

    }

    //  --------------------------  for show any type of message ----------------------------
    public void showerror(String message) {
        tv_error_message.setVisibility(View.VISIBLE);

        tv_error_message.setText(message);

        Animation animation;
        animation = AnimationUtils.loadAnimation(CustomerDetailsActivity.this, R.anim.slide_down);
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

    //   -------------------------------  updateessential and add into local db --------------------------------
    public void updateessential(View view) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = AllUrl.baseUrl + "addacc?addacc.FrCode=" + FrCode;

            System.out.println("get all sales-->  " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressBar.setVisibility(View.GONE);

                            //  System.out.println("ess details-->" + response);
                            if (response == null) {

                            } else {

                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    // System.out.println(jsonArray.toString());

                                    errorDetails.Errorlog(CustomerDetailsActivity.this, "essential%20update", TAG, "no", "essential%20Update%20Success", mobile_no, TicketNo, "u", mobile_details, dbversion, tss);

                                    boolean status = dbhelper.deleteess();

                                    dbhelper.insert_ess_data("-- select essential --", "", "", "0", "0", "", "");

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject essobj = jsonArray.getJSONObject(i);


                                        if (essobj.getString("accessories_stock").equals("0") && essobj.getString("additives_stock").equals("0")) {

                                        } else {

                                            if (Boolean.valueOf(dbhelper.insert_ess_data(essobj.getString("ComponentDescription"), essobj.getString("Component"), "Z" + essobj.getString("ItemType"), essobj.getString("accessories_stock"), essobj.getString("additives_stock"), essobj.getString("ShelfLife"), "")).booleanValue()) {

                                            }
                                        }
                                    }
                                    getess();
                                } catch (Exception e) {

                                    FirebaseCrashlytics.getInstance().recordException(e);
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    showerror("Please try again later ");
                    errorDetails.Errorlog(CustomerDetailsActivity.this, "essential%20update", TAG, "404error", "essential%20not%20found", mobile_no, TicketNo, "u", mobile_details, dbversion, tss);

                }
            });
            queue.add(stringRequest);

        } else {

            //  ll_nointernet.setVisibility(View.VISIBLE);
            showerror("Please check internet connection");
        }
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

    public void createbill(View view) {

        Gson gson = new Gson();
        String sparedata = gson.toJson(models);
        String essentialdata = gson.toJson(essential_add_models);
        startActivity(new Intent(CustomerDetailsActivity.this, CreateBill.class)
                .putExtra("calltype", CallType)
                .putExtra("spare", sparedata)
                .putExtra("essential", essentialdata));

    }

    public void getWaterQuality() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            map_water_source = new HashMap<>();
            waterlist = new ArrayList<>();

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);

            String url = AllUrl.WaterQuality;


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressBar.setVisibility(View.GONE);

                            //  System.out.println("WaterQuality" + response);
                            if (response == null) {

                            } else {

                                try {

                                    JSONArray jsonArray = new JSONArray(response);

                                    dbhelper.delete_waterquality();

                                    dbhelper.insert_waterquality(response);


                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                                        map_water_source.put(jsonObject.getString("WaterSource"), jsonObject.getString("Id"));
                                        String WaterSource = jsonObject.getString("WaterSource");
                                        waterlist.add(WaterSource);

                                    }


                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CustomerDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, waterlist);
                                    spnr_water_source.setAdapter(arrayAdapter);
                                    // spnr_remark.setSelection(0,false);
                                    spnr_water_source.setOnItemSelectedListener(CustomerDetailsActivity.this);

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

                    map_water_source = new HashMap<>();
                    waterlist = new ArrayList<>();

                    try {

                        Cursor wcursor;

                        wcursor = dbhelper.get_waterquality();

                        if (wcursor != null) {

                            if (wcursor.moveToFirst()) {

                                do {


                                    String response = wcursor.getString(wcursor.getColumnIndex("data"));

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);

                                        dbhelper.delete_waterquality();

                                        dbhelper.insert_waterquality(response);


                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject = jsonArray.getJSONObject(i);


                                            map_water_source.put(jsonObject.getString("WaterSource"), jsonObject.getString("Id"));
                                            String WaterSource = jsonObject.getString("WaterSource");
                                            waterlist.add(WaterSource);

                                        }


                                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CustomerDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, waterlist);
                                        spnr_water_source.setAdapter(arrayAdapter);
                                        // spnr_remark.setSelection(0,false);
                                        spnr_water_source.setOnItemSelectedListener(CustomerDetailsActivity.this);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        FirebaseCrashlytics.getInstance().recordException(e);
                                    }


                                } while (wcursor.moveToNext());

                                wcursor.close();
                            }
                        } else {

                        }


                    } catch (Exception e) {

                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {

            map_water_source = new HashMap<>();
            waterlist = new ArrayList<>();

            try {

                Cursor wcursor;

                wcursor = dbhelper.get_waterquality();

                if (wcursor != null) {

                    if (wcursor.moveToFirst()) {

                        do {


                            String response = wcursor.getString(wcursor.getColumnIndex("data"));

                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                dbhelper.delete_waterquality();

                                dbhelper.insert_waterquality(response);


                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                                    map_water_source.put(jsonObject.getString("WaterSource"), jsonObject.getString("Id"));
                                    String WaterSource = jsonObject.getString("WaterSource");
                                    waterlist.add(WaterSource);

                                }


                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CustomerDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, waterlist);
                                spnr_water_source.setAdapter(arrayAdapter);
                                // spnr_remark.setSelection(0,false);
                                spnr_water_source.setOnItemSelectedListener(CustomerDetailsActivity.this);

                            } catch (Exception e) {
                                e.printStackTrace();
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }


                        } while (wcursor.moveToNext());

                        wcursor.close();
                    }
                } else {

                }


            } catch (Exception e) {

                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }

        }
    }

    public void getMandatoryCheck() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            map_water_source = new HashMap<>();
            waterlist = new ArrayList<>();

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            // AllUrl.baseUrl+AMC/getAmcDetailsBySerialNo?model.amc.Srno=012228130220008870

            String url = AllUrl.baseUrl+"MandatoryCheck/product/?model.PrdCode=" + Product;

            // System.out.println("get all sales-->  " + url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            progressBar.setVisibility(View.GONE);

                            //  System.out.println("WaterQuality" + response);
                            if (response == null) {

                            } else {

                                try {

                                    JSONArray jsonArray = new JSONArray(response);

                                    dbhelper.delete_mandatorycheck(Product);
                                    dbhelper.insert_mandatorycheck(response, Product);

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject obj = jsonArray.getJSONObject(i);

                                        KeyPairBoolData h = new KeyPairBoolData();
                                        long ids = Long.parseLong(obj.getString("Id"));
                                        h.setId(ids);
                                        h.setName(obj.getString("Nanme"));
                                        h.setSelected(false);
                                        list_mcheck.add(h);
                                        updatemlist();

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

                    try {

                        Cursor mcursor;

                        mcursor = dbhelper.get_mandatorycheck(Product);

                        if (mcursor != null) {

                            if (mcursor.moveToFirst()) {

                                do {


                                    String response = mcursor.getString(mcursor.getColumnIndex("data"));
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        KeyPairBoolData h = new KeyPairBoolData();
                                        long ids = Long.parseLong(obj.getString("Id"));
                                        h.setId(ids);
                                        h.setName(obj.getString("Nanme"));
                                        h.setSelected(false);
                                        list_mcheck.add(h);

                                        updatemlist();

                                    }
                                } while (mcursor.moveToNext());

                                mcursor.close();
                            }
                        } else {

                        }


                    } catch (Exception e) {

                        e.printStackTrace();
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {

            try {

                Cursor mcursor;

                mcursor = dbhelper.get_mandatorycheck(Product);

                if (mcursor != null) {

                    if (mcursor.moveToFirst()) {

                        do {

                            String response = mcursor.getString(mcursor.getColumnIndex("data"));
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                KeyPairBoolData h = new KeyPairBoolData();
                                long ids = Long.parseLong(obj.getString("Id"));
                                h.setId(ids);
                                h.setName(obj.getString("Nanme"));
                                h.setSelected(false);
                                list_mcheck.add(h);

                                updatemlist();

                            }
                        } while (mcursor.moveToNext());

                        mcursor.close();
                    }
                } else {

                }

            } catch (Exception e) {

                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }

        }
    }

    public void updatemlist() {

        spnr_mcheck.setEmptyTitle("Not Data Found!");
        spnr_mcheck.setSearchHint("Find Data");

        spnr_mcheck.setHintText("Mandatory Check List for " + Product);

        spnr_mcheck.setItems(list_mcheck, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {


                StringBuilder sb = new StringBuilder();
                String prefix = "";

                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedItems.get(i).isSelected()) {
                        // Log.i(TAG, i + " : " + items.get(i).getName() + " : " + items.get(i).isSelected());

                        sb.append(prefix);
                        prefix = ",";
                        sb.append(selectedItems.get(i).getId());
                    }
                }

                mcheck = sb.toString();
            }
        });

    }

    public void uploadstarttime() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            if (mcheck.length() == 0) {
                mcheck = "NA";
            }

            if (tds_level.length() == 0) {
                tds_level = "NA";
            }

            if (jobstarttime.length() == 0) {

                jobstarttime = "00:00";
            }


            String url = AllUrl.baseUrl+"bulk-request/save/?model.OrderId=" + TicketNo + "&model.StartTime=" + jobstarttime + "&model.ManCheckId=" + mcheck + "&model.WaterSourceId=" + water_code + "&model.TDS=" + tds_level + "&model.CreatedBy=" + PartnerId;

            System.out.println("submit job start time-->  " + url);

            RequestQueue queue = Volley.newRequestQueue(this);
            //  showProgressDialog();
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            dbhelper.insert_starttime(TicketNo, jobstarttime, mcheck, water_code, tds_level, PartnerId);

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

            //  Toast.makeText(this, "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
            dbhelper.insert_starttime(TicketNo, jobstarttime, mcheck, water_code, tds_level, PartnerId);

        }
    }

    //  AllUrl.baseUrl+bulk-request/save/?model.OrderId=2004550190&model.StartTime=12:30&model.ManCheckId=1,2,3,3,4,4&model.WaterSourceId=1,2,2&model.TDS=test12333&model.CreatedBy=121212212

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

    @Override
    public void onDeleteclick(String code, String name, String qty, String status) {

        errorDetails.Errorlog(CustomerDetailsActivity.this, "Spare_Remove", TAG, name.replace(" ", "%20") + "%20" + code, "Remove from spare List ", mobile_no, TicketNo, "R", mobile_details, dbversion, tss);

    }

    @Override
    public void onQuentityClick(String refno, String smstype, String altno, String ticketno) {

        // errorDetails.Errorlog(CustomerDetailsActivity.this,"Spare_change_qty", TAG,name.replace(" ","%20")+"%20"+partcode,"Spare%20Tab",mobile_no,TicketNo,"U",mobile_details,dbversion,tss);

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

    @Override
    protected void onStop() {
        super.onStop();

        if (submitstatus.equals("false")){

            dbhelper.delete_work_data(TicketNo);

            Gson gson = new Gson();
            String sparedata = gson.toJson(models);
            String essentialdata = gson.toJson(essential_add_models);
             dbhelper.insert_work_data(TicketNo, SerialNo, oduserno, DOP, DOI, jobstarttime, "" + ts, sparedata, essentialdata, et_feedback.getText().toString(), "", "", "", str_spnr_status, "", ctcstatus);


            if (imagestatus) {
                dbhelper.delete_worktime_image("worktime");
                dbhelper.insert_offline_image_data(TicketNo, seraialno_image_data, oduserialno_image_data, invoice_image_data, customersign_data, tss, "worktime");
            }
        }
        else {

            dbhelper.delete_work_data(TicketNo);
            dbhelper.delete_worktime_image("worktime");
        }
    }

    public void verifyicr(View v) {

        String icr = et_icr_no.getText().toString();

        VerifyIcr verifyIcr = new VerifyIcr();

        showProgressDialog();
        verifyIcr.getString(new VerifyIcr.VolleyCallback() {
            @Override
            public void onSuccess(String result) {


                System.out.println("result" + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    icr_msg = jsonObject.getString("Message");

                    String message = jsonObject.getString("Message");
                    if (message.equals("Success")) {

                        icrstatus = "true";
                        st_icr_no = icr;
                    } else {
                        icrstatus = "false";
                    }

                    tv_icr_error.setText(icr_msg);

                } catch (Exception e) {


                    e.printStackTrace();
                    FirebaseCrashlytics.getInstance().recordException(e);
                }

                hideProgressDialog();

            }
        }, CustomerDetailsActivity.this, icr, FrCode);

    }

    public void updateicrlayout() {

        ll_service_charge_view.setVisibility(View.VISIBLE);
    }

    public void clodeupdateicrlayout() {

        ll_service_charge_view.setVisibility(View.GONE);

    }

    public void checkctcerror(String srno) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            CheckCustomerSerialNo customerSerialNo = new CheckCustomerSerialNo();
            showProgressDialog();
            customerSerialNo.getstatus(new CheckCustomerSerialNo.VolleyCallback() {
                @Override
                public void onSuccess(String status, String result) {

                    if (status.equals("true")) {

                        try {
                            // {"Data":[{"zztel_number":"9200211088","zzalt_number":"7974672006","zzsoldto":"0018566212","zzname_org1":"GURU   DEV","zzname_org2":"STATIONARY","zzemail":"","zz0012":"AC","zz0010":"","warranty_edate":"2/2/1900 12:00:00 AM","zzr3ser_no":"028694190115013719","zzstr_suppl1":"JANTAD PANCHAYATH BADE RAJPUT KESKAL","city1":"Kanker","zzpost_code1":"494226","zzpurchase_date":"4/25/2019 12:00:00 AM","zzstreet":"KONDAGAON NAKA","region":"Chhattisgarh","zzr3mat_id":"8903287028694","House_num1":"","zzstr_suppl2":"","zzstr_suppl3":"","zzinstall_date":"4/29/2019 12:00:00 AM","Product":"SPLIT AC 2.0 T IAFS24XA3T4C-IDU","warrantydesc":"","warranty_sdate":"4/29/2019 12:00:00 AM","zzfranch":"0003003668","zzdeact_ib":""}],"serialNumber":null,"contact":null,"zzsoldto":null,"Message":"Data Received successfully","Status":true,"zzfranch":null}

                            JSONObject jsonObject = new JSONObject(result);

                            JSONArray jsonArray = jsonObject.getJSONArray("Data");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String Srnos = object.getString("zzr3ser_no");
                                String custcode = object.getString("zzsoldto");
                                String  zzdeact_ib =object.getString("String zzdeact_ib");

                                if (zzdeact_ib.equals("X") || zzdeact_ib.equals("x")){

                                    Alertwithicon alertwithicon = new Alertwithicon();
                                    alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "This " + srno + " Ibase is deactivated. \nPlease Take Serial No Photo", false);
                                    ctcstatus = "true";
                                    ctcmessage = "Ibase is deactivated.";

                                }

                                if (Srnos != SerialNo) {

                                    Alertwithicon alertwithicon = new Alertwithicon();
                                    alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "This " + srno + " Serial No Already Tag With Different Customers \nPlease Take Serial No Photo", false);
                                    ctcstatus = "true";
                                    ctcmessage = "SERVICE ORDER COULD NOT BE SAVED. CUSTOMER IN IBASE " + custcode + " DIFFERS FROM SERVICE ORDER.";

                                } else if (Srnos.equals(SerialNo)) {

                                    ctcstatus = "false";
                                    ctcmessage = "";

                                }


                            }


                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    } else if (status.equals("false")) {

                        //        new AsyncFindmodel(CustomerDetailsActivity.this, CustomerDetailsActivity.this).execute("");

                        Alertwithicon alertwithicon = new Alertwithicon();
                        alertwithicon.showDialog(CustomerDetailsActivity.this, "I-BASE Error", "Invalid Serial ID \nPlease Take Serial No Photo", false);
                        ctcstatus = "true";
                        ctcmessage = "Invalid Serial ID";

                    } else if (status.equals("error")) {

                        ctcstatus = "false";
                        ctcmessage = "";

                    }
                    hideProgressDialog();

                }
            }, CustomerDetailsActivity.this, "", srno);
        }
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
        animation = AnimationUtils.loadAnimation(CustomerDetailsActivity.this, R.anim.slide_down);
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

    public void upadte_recheck_for_spare() {

        remarklist = new ArrayList<>();
        remarkmap = new HashMap<>();

        remarklist.add("Spares not available at Franchise");
        remarkmap.put("Spares not available at Franchise", "Z011");

        remarklist.add("Spares not taken");
        remarkmap.put("Spares not taken", "Z012");

        remarklist.add("Spares not available at Branch");
        remarkmap.put("Spares not available at Branch", "Z015");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CustomerDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, remarklist);
        spnr_remark.setAdapter(arrayAdapter);
        // spnr_remark.setSelection(0,false);
        spnr_remark.setOnItemSelectedListener(CustomerDetailsActivity.this);

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

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            progress = (int) (millisUntilFinished / 1000);
            int minutes = (progress % 3600) / 60;
            int seconds = progress % 60;
            tv_timeer.setText("Submit Data After : " + minutes + " Min " + seconds + " Sec");
            timer_status = "false";
            isRunning = true;

            if (progress == 0) {

                btn_status.setVisibility(View.VISIBLE);
                tv_timeer.setVisibility(View.GONE);
                myCountDownTimer.cancel();
                timer_status = "true";
                isRunning = false;
            }
        }

        @Override
        public void onFinish() {

            btn_status.setVisibility(View.VISIBLE);
            tv_timeer.setVisibility(View.GONE);
            isRunning = false;

        }

    }

    public void updatectcerror(String msg) {

        CtcPendingReasons ctcPendingReasons = new CtcPendingReasons();
        ctcPendingReasons.Submitdata(CustomerDetailsActivity.this, TicketNo, msg);

    }

    public void getservertime() {


        if (isRunning) {

        } else {

            myCountDownTimer = new MyCountDownTimer(300000, 1000);
            myCountDownTimer.start();
        }


        if (CheckConnectivity.getInstance(this).isOnline()) {

            GetJobstarttime getJobstarttime = new GetJobstarttime();

            getJobstarttime.getjobstart(new GetJobstarttime.VolleyCallback() {
                @Override
                public void onsuccess(String status, String jobstarttimes, String jobendtimes) {

                    System.out.println("status" + status);

                    if (status.equals("true")) {

                        upadte_jobstart_layout(jobstarttimes);

                        ts = new Date().getTime();


                    } else {

                        GetOfflineJobstarttime getOfflineJobstarttime = new GetOfflineJobstarttime();
                        String currentDateandTime = getOfflineJobstarttime.gettime(CustomerDetailsActivity.this);

                        upadte_jobstart_layout(currentDateandTime);

                        ts = new Date().getTime();
                    }

                }
            }, CustomerDetailsActivity.this);
        } else {

            GetOfflineJobstarttime getOfflineJobstarttime = new GetOfflineJobstarttime();
            String currentDateandTime = getOfflineJobstarttime.gettime(CustomerDetailsActivity.this);

            upadte_jobstart_layout(currentDateandTime);

            ts = new Date().getTime();
        }
    }

    public void upadte_jobstart_layout(String time) {

        System.out.println("time-->" + time);
        tv_serialno_capturetime.setText("HH:mm (" + time + ")");
        jobstarttime = time.replace(" ", "%20");

    }

    public void updatevalue() {

        try {

            Cursor smcursor;

            smcursor = dbhelper.get_work_data(TicketNo);

            if (smcursor != null) {

                if (smcursor.moveToFirst()) {


                    do {

                        SerialNo = smcursor.getString(smcursor.getColumnIndex("serialno"));
                        tv_SerialNo.setText(SerialNo);
                        String DOPs = smcursor.getString(smcursor.getColumnIndex("dop"));
                        String DOIs = smcursor.getString(smcursor.getColumnIndex("doi"));
                        DOP = smcursor.getString(smcursor.getColumnIndex("dop"));
                        DOI = smcursor.getString(smcursor.getColumnIndex("doi"));

                        tv_DOI.setText(DOI);
                        tv_DOP.setText(DOP);

                        oduserno = smcursor.getString(smcursor.getColumnIndex("oduserialno"));
                        tv_odu_ser_no.setText(oduserno);
                        st_feedback = smcursor.getString(smcursor.getColumnIndex("feedback"));
                        et_feedback.setText(st_feedback);
                        ctcstatus = smcursor.getString(smcursor.getColumnIndex("ctcstatus"));
                        String sparedata = smcursor.getString(smcursor.getColumnIndex("sparedata"));
                        updatespare(sparedata);
                        String remaintime = smcursor.getString(smcursor.getColumnIndex("remaintime"));
                        ts = Long.parseLong(remaintime);
                        jobstarttime = smcursor.getString(smcursor.getColumnIndex("jobstarttime"));
                        if (jobstarttime.length() == 0) {

                        } else {

                            tv_serialno_capturetime.setText("HH:mm (" + jobstarttime.replace("%20", " ") + ")");
                            long curtms = new Date().getTime();
                            long oldtms = Long.parseLong(remaintime);
                            long diff = curtms - oldtms;
                            System.out.println("seconds--> " + diff + " curtms-- >" + curtms);
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

                            System.out.println("seconds--> " + seconds + " curtms-- >" + curtms);

                            if (seconds < 300) {

                                int retime = 300 - (int) seconds;

                                if (!isRunning) {
                                    myCountDownTimer = new MyCountDownTimer(retime * 1000, 1000);
                                    myCountDownTimer.start();
                                }

                            } else {

                                btn_status.setVisibility(View.VISIBLE);
                                tv_timeer.setVisibility(View.GONE);
                                timer_status = "true";
                                isRunning = false;
                            }

                        }

                        String essentialdata = smcursor.getString(smcursor.getColumnIndex("essdata"));
                        upadetess(essentialdata);

                    } while (smcursor.moveToNext());
                    smcursor.close();
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }


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

    public void updatespare(String data) {

        try {

            JSONArray jsonArray = new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                // String spare_not_found,int stockqty,String date){

                Boolean result = check_part_In_List(object.getString("description"));

                if (result.equals(false)) {

                    add_part_item(object.getString("itemname"), object.getString("description"), object.getString("count"),
                            object.getString("flag"), object.getString("Check"), Integer.parseInt(object.getString("stockqty")), object.getString("date"), object.getString("price"), object.getString("total_price"));

                }
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void upadetess(String data) {

        try {
            //  [{"date":"2021-09-17_14-42-33","ecode":"8903287080159","ename":"Tap Adapter","equentity":"1","flag":"I","itemtype":"ZACC"}]

            JSONArray jsonArray = new JSONArray(data);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Boolean status = checkesscode(object.getString("ecode"));

                if (status) {
                } else {
                    essential_add_model = new Essential_add_model();
                    essential_add_model.setEname(object.getString("ename"));
                    essential_add_model.setEcode(object.getString("ecode"));
                    essential_add_model.setEquentity(object.getString("equentity"));
                    essential_add_model.setItemtype(object.getString("itemtype"));
                    essential_add_model.setFlag("I");
                    essential_add_model.setDate(object.getString("date"));
                    essential_add_models.add(essential_add_model);
                }
            }

            essential_add_adapter = new Essential_add_adapter(essential_add_models, CustomerDetailsActivity.this);
            essential_add_adapter.notifyDataSetChanged();
            lv_ess_add_item.setAdapter(essential_add_adapter);


        } catch (Exception e) {

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

    public void getservicePrice() {

        Getcitycode getcitycode = new Getcitycode();
        getcitycode.getCitykey(new Getcitycode.Getcode() {
            @Override
            public void onsuccess(String status, String result) {


                if (status.equals("true")) {

                    getprice(result);

                } else {

                }
            }
        }, CustomerDetailsActivity.this, City.replace(" ", "%20"));
    }

    public void getprice(String result) {

        Getprice getprice = new Getprice();

        getprice.getCityPrice(new Getprice.GetPrices() {
            @Override
            public void onsuccess(String result, String price) {

                if (result.equals("true")) {

                    service_amount = Double.parseDouble(price);

                    tv_amount.setText(price);
                }

                System.out.println("price-- > " + price);

            }
        }, CustomerDetailsActivity.this, result, Product, FGProducts,ServiceType);
    }

    public void icrtypeclick(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_physical:
                if (checked)

                    icrstatus = "false";
                tv_icr_error.setVisibility(View.VISIBLE);
                iv_icr_verify.setVisibility(View.VISIBLE);
                et_icr_no.setText("");

                break;

            case R.id.radio_digital:
                if (checked)

                    icrstatus = "true";
                tv_icr_error.setVisibility(View.VISIBLE);
                iv_icr_verify.setVisibility(View.GONE);
                et_icr_no.setText("");

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

                                tv_icr_error.setText(jsonObject.getString("Message"));

                                String status = jsonObject.getString("Status");
                                if (status.equals("true")) {

                                    String str = jsonObject.getString("Message");

                                    System.out.println("str-->" + str);
                                    str = str.replaceAll("\\D+", "");
                                    et_icr_no.setText(str);
                                    icrstatus = "true";
                                    st_icr_no = str;
                                } else {

                                    et_icr_no.setText("");
                                    st_icr_no = "";

                                }
                            } else {

                                et_icr_no.setText("");
                                st_icr_no = "";
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                }, CustomerDetailsActivity.this, RegionCode, PartnerId);

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

    public void updatesparedata() {

        if (MachinStatus.equals("OG")) {


            if (CallType.equals("COURTESY VISIT") || CallType.equals("INSTALLATION CALL")) {

            } else {
                Jobcopmplete jobcopmplete = new Jobcopmplete();
                jobcopmplete.getstring(new Jobcopmplete.Getresponse() {
                    @Override
                    public void onSuccess(String status, String result) {

                    }
                }, CustomerDetailsActivity.this, TicketNo, "" + service_amount, "" + spare_amoont, "0", "" + total_amount, st_icr_no, icr_newdate, PartnerId, st_feedback);

            }
        }
    }

    public boolean ogtstatus() {

        boolean status = false;
        st_icr_no=et_icr_no.getText().toString();


        if (CallType.equals("COURTESY VISIT") || CallType.equals("INSTALLATION CALL")){
            status = true;
        }
        else if (st_icr_no.length() == 0){

            status = true;
        }
        else {
            if (CheckConnectivity.getInstance(this).isOnline()) {
                if (st_icr_no.length() == 0) {
                    showerror("Please enter icr no");
                } else if (icrstatus.equals("false")) {

                    showerror("Please verify icr no");
                } else if (st_icr_date.equals("00000000")) {

                    showerror("Please select icr date");
                } else {

                    status = true;
                }

            } else {

                status = true;
            }

        }
        return status;
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
                                        }, CustomerDetailsActivity.this, Product, essobj.getString("PartCode"));
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
                                        }, CustomerDetailsActivity.this, Product, essobj.getString("PartCode"));
                                    }
                                    pendingSpares.add(pendingSpare);

                                } else {

//                                            essential_add_model=new Essential_add_model();
//                                            essential_add_model.setEname(essobj.getString("EssName")+""+essobj.getString("AccName"));
//                                            essential_add_model.setEcode(essobj.getString("EssCode")+""+essobj.getString("AccCode"));
//                                            essential_add_model.setEquentity(essobj.getString("Qty"));
//                                            essential_add_model.setItemtype(essobj.getString("ItemType"));
//                                            essential_add_model.setFlag("U");
//                                            essential_add_models.add(essential_add_model);

                                }
                            }

//                                    essential_add_adapter = new Essential_add_adapter(essential_add_models, CustomerDetailsActivity.this);
//                                    essential_add_adapter.notifyDataSetChanged();
//                                    lv_ess_add_item.setAdapter(essential_add_adapter);

                            Parcelable state = lv_pending_item.onSaveInstanceState();
                            Pending_item_adapter pending_item_adapter = new Pending_item_adapter(pendingSpares, CustomerDetailsActivity.this);
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

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(.\\d+)?");
    }
}