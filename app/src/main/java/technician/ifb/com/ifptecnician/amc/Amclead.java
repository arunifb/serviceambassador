package technician.ifb.com.ifptecnician.amc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.MyApplication;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.TaskListActivity;

import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.stock.MystockModel;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Amclead extends AppCompatActivity implements AmcleadAdapter.JobsAdapterListener,
        AmcleadAdapter.Rcnclick,AmcleadAdapter.Altclick,
        SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = Amclead.class.getSimpleName();

    private List<AmcleadModel> models;
    public AmcleadModel model;
    RecyclerView lv_allamclist;
    SharedPreferences prefdetails;
    RecyclerView listView;
    String response,status;
    SessionManager sessionManager;
    String PartnerId;
    AmcleadAdapter tasklist_adapter;
    Toolbar toolbar;
    String currentdate,beforedate;
    String url;
    private SearchView searchView;
    Dbhelper dbhelper;
    ImageView iv_date,iv_ageing,iv_calltype;
    TextView tv_date,tv_ageing,tv_calltype;
    String tss,tsss ,mobile_no,custcode;

    private BroadcastReceiver mNetworkReceiver;
    static LinearLayout ll_connection;
    int dtsort_clk_cunt=0,ageing_sort_clk_count=0;
    String mobile_details;
    SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeView;
    boolean refreshToggle = true;
    ProgressBar progressBar;
    TextView tv_smssend;
    LinearLayout ll_sms_true,ll_sms_false;
    String Ticketno;

    LinearLayout ll_filter,ll_data,ll_search;

    //search section
    EditText et_cust_mobile,et_cust_code;
    String cust_mobile,FrCode;

    LinearLayout ll_nodata,ll_search_customer,ll_amclead;
    TextView tv_error;
    String amcleaddata="",searchdata="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amclead);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Lead AMC");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //  lv_allamclist=findViewById(R.id.lv_allamclist);

        ll_connection= findViewById(R.id.ll_nointernet);
        ll_nodata= findViewById(R.id.ll_nodata);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        ll_sms_true=findViewById(R.id.layout_sms_true);
        ll_sms_false=findViewById(R.id.layout_sms_false);
        tv_error=findViewById(R.id.tv_error_message);
        progressBar=findViewById(R.id.taskProcessing);
        iv_date=findViewById(R.id.tsklist_iv_date);
        iv_ageing=findViewById(R.id.tsklist_iv_ageing);
        iv_calltype=findViewById(R.id.tsklist_iv_calltype);

        tv_date=findViewById(R.id.tv_sort_date);
        tv_ageing=findViewById(R.id.tv_sort_ageing);
        tv_calltype=findViewById(R.id.tv_sort_status);
        tv_smssend=findViewById(R.id.tv_error_message);
        ll_filter=findViewById(R.id.ll_filter);
        ll_data=findViewById(R.id.ll_data);
        ll_search=findViewById(R.id.ll_search);

        ll_search_customer=findViewById(R.id.ll_search_customer);
        ll_amclead=findViewById(R.id.ll_amclead);
        ll_search_customer.setVisibility(View.VISIBLE);
        ll_amclead.setVisibility(View.GONE);
        dbhelper=new Dbhelper(getApplicationContext());

//        sessionManager=new SessionManager(getApplicationContext());
//        HashMap<String, String> user = sessionManager.getUserDetails();
//        PartnerId=user.get(SessionManager.KEY_PartnerId);
//        mobile_no=user.get(SessionManager.KEY_mobileno);


        sessionManager=new SessionManager(getApplicationContext());

        try{

            HashMap<String, String> user = sessionManager.getUserDetails();

            mobile_no=user.get(SessionManager.KEY_mobileno);
            FrCode=user.get(SessionManager.KEY_FrCode);
            PartnerId=user.get(SessionManager.KEY_PartnerId);

        }catch (Exception e){

            e.printStackTrace();
        }

        listView=findViewById(R.id.lv_allamclist);
        models= new ArrayList<>();
        sharedPreferences=getSharedPreferences(AllUrl.MOBILE_DETAILS,0);
        mobile_details=sharedPreferences.getString("Mobile_details","");

        tasklist_adapter=new AmcleadAdapter(this, models, this,this,this);
        listView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
       // listView.addItemDecoration(new MyDividerItemDecoration(this,DividerItemDecoration.VERTICAL, 36));
        listView.setAdapter(tasklist_adapter);

    Date c = Calendar.getInstance().getTime();

    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    currentdate = df.format(c);

    SimpleDateFormat dfs = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
    tss = dfs.format(c).replace(" ","%20");

    SimpleDateFormat dfss = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    tsss = dfss.format(c);

    Calendar calendar = Calendar.getInstance();
    int day=calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_YEAR, -30);
    Date newDate = calendar.getTime();
    beforedate=df.format(newDate);
        System.out.println("before Code"+beforedate);


    // swipe to refrss section
    swipeView=findViewById(R.id.swipe_view);

        swipeView.setOnRefreshListener(this);
    // set the swipe time color
        swipeView.setColorSchemeResources(R.color.colorPrimary,
    android.R.color.holo_green_dark,
    android.R.color.holo_orange_dark,
    android.R.color.holo_blue_dark);


    et_cust_mobile=findViewById(R.id.et_cust_mobile);
    et_cust_code=findViewById(R.id.et_cust_code);

    getamc();

}

    // ----------------   on back press section  -----------------------
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // -----------------------    Menu search section ---------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                tasklist_adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                tasklist_adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ----------------------  user click on  back press ----------------------------------
    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    // ------------------ list data sort  by date -------------------------------

    public void sortdate(View v){

        tv_date.setBackgroundColor(Color.LTGRAY);
//        tv_ageing.setBackgroundColor(Color.WHITE);
//        tv_calltype.setBackgroundColor(Color.WHITE);

        iv_date.setVisibility(View.VISIBLE);
        iv_date.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        iv_ageing.setVisibility(View.GONE);
//        iv_calltype.setVisibility(View.GONE);

        if (dtsort_clk_cunt==0){
            iv_date.setImageResource(R.drawable.ic_arrow_downward_black_24dp);

            dtsort_clk_cunt = 1;
            tasklist_adapter.getFilter().filter("");
            tasklist_adapter.notifyDataSetChanged();


//            try{
//
//                Collections.sort(tasklist, new Comparator<AmcleadModel>() {
//                    DateFormat f = new SimpleDateFormat("dd-MMM");
//                    public int compare(AmcleadModel p1, AmcleadModel p2) {
//                        try {
//                            return f.parse(p1.getCallBookDate()).compareTo(f.parse(p2.getCallBookDate()));
//                        } catch (ParseException e) {
//                            throw new IllegalArgumentException(e);
//                        }
//                        // return String.valueOf(f).compareTo(s);
//                    }
//                });
//
//                tasklist_adapter.notifyDataSetChanged();
//
//            }catch (Exception e){
//
//                e.printStackTrace();
//            }
        }

        else if (dtsort_clk_cunt==1){

            dtsort_clk_cunt = 0;
            iv_date.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
            tasklist_adapter.getFilter().filter("");
            tasklist_adapter.notifyDataSetChanged();

        }
    }

    public String Dateformat(String time){
        String inputPattern = "dd-MMM";
        String outputPattern = "ddMM";
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbhelper.close();
    }

    @Override
    public void onRefresh() {

        if (CheckConnectivity.getInstance(Amclead.this).isOnline()) {

            swipeView.setRefreshing(false);
        }
        else {

            swipeView.setRefreshing(false);
        }
    }

    public void refreshdata(){


    }

    @Override
    public void onAltclick(String ref,String smstype,String altno,String ticketno) {

        String smsstaus=dbhelper.getnpsstatus( altno);
        Ticketno=ticketno;

        System.out.println(smsstaus +" smsstaus");
        if (smsstaus.equals("true")){

//            showsmspopup("Sms already sent !!");
         }
        else {

        }
    }

    @Override
    public void onRcnclick(String ref,String smstype,String rcn,String ticketno) {

        String smsstaus=dbhelper.getnpsstatus(rcn);
        Ticketno=ticketno;
        System.out.println("smsstaus"+smsstaus);

        if (smsstaus.equals("true")){
         //   showsmspopup("SMS already sent !!");
          }
        else {

        }
    }

    public void showsmsalert(String status){

        if (status.equals("true")){

            ll_sms_true.setVisibility(View.VISIBLE);
            ll_sms_false.setVisibility(View.GONE);

            Animation animation;
            animation = AnimationUtils.loadAnimation(Amclead.this, R.anim.slide_down);
            ll_sms_true.startAnimation(animation);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    ll_sms_true.setAnimation(null);
                    ll_sms_true.setVisibility(View.GONE);
                }
            }, 5000);

        }
        else {

            ll_sms_true.setVisibility(View.GONE);
            ll_sms_false.setVisibility(View.VISIBLE);
            Animation animation;
            animation = AnimationUtils.loadAnimation(Amclead.this, R.anim.slide_down);
            ll_sms_false.startAnimation(animation);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    ll_sms_false.setAnimation(null);
                    ll_sms_false.setVisibility(View.GONE);
                }
            }, 5000);
        }
    }

    public void sorttype(View view){


    }

    public void getamc(){


        if ( CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            ll_connection.setVisibility(View.GONE);
            RequestQueue queue = Volley.newRequestQueue(this);

            String urls=AllUrl.baseUrl+"sa/leads/search?lead_category=AMC&Frcode="+ FrCode;

            System.out.println("urls-->"+urls);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urls,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);


                            viewamclead(response);
                            amcleaddata=response;


                            System.out.println("response-->"+response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);

                }
            });
// Add the request to the RequestQueue.

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        }

        else {

            ll_connection.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onTaskSelected(AmcleadModel tasklistModel) {

    }

    public void createamc(View v){

        startActivity(new Intent(Amclead.this,Create_customer.class));
    }

    public void searchcustomer(View v){


        hideSoftKeyboard(Amclead.this);
        cust_mobile=et_cust_mobile.getText().toString();

        custcode=et_cust_code.getText().toString();


        if (cust_mobile.length()==0 && custcode.length() < 7)
        {

             showerrorpop("Please enter Cutomer Mobile or minimum 7 didit customer code ");

        }
        else{

            getcustomer();

        }


    }

    public void amclead(View view){

        toolbar.setTitle("Lead AMC");
        ll_search_customer.setVisibility(View.VISIBLE);
        ll_amclead.setVisibility(View.GONE);

        ll_search.setVisibility(View.GONE);
        ll_filter.setVisibility(View.VISIBLE);
        ll_data.setVisibility(View.VISIBLE);
        viewamclead(amcleaddata);


    }

    public void openSearch(View v){

        toolbar.setTitle("Customer Search");
        ll_search.setVisibility(View.VISIBLE);
        ll_filter.setVisibility(View.GONE);
        ll_data.setVisibility(View.GONE);

        ll_search_customer.setVisibility(View.GONE);
        ll_amclead.setVisibility(View.VISIBLE);


        if (searchdata.equals("")){}
        else{
            viewsearchdata(searchdata);
        }


    }

    public void viewamclead(String response){

        try{
            JSONObject jsonObject=new JSONObject(response);
            String status=jsonObject.getString("Status");

            if (status.equals("true")){


                ll_nodata.setVisibility(View.GONE);

                JSONArray jsonArray=jsonObject.getJSONArray("Data");

                models=new ArrayList<>();


                for (int i=0;i<jsonArray.length();i++){

                    JSONObject object=jsonArray.getJSONObject(i);

                    model=new AmcleadModel();
                    model.setCustid(object.getString( "sold_to_party"));
                    model.setCustname(object.getString("sold_to_party_list"));
                    model.setCustaddress(object.getString("address"));
                    model.setCustemail(object.getString("e_mail"));
                    model.setCustphone(object.getString("caller_no"));
                    model.setProduct_name(object.getString( "zzmat_grp")+" ("+object.getString( "zzproduct_desc")+")");
                    model.setProductid(object.getString("productid"));
                    model.setSerialno(object.getString("zzserial_numb"));
                    model.setAltphone(object.getString("tel_no"));
                    model.setFrcode(object.getString("frcode"));
                    model.setProduct(object.getString("zzmat_grp"));
                    models.add(model);

                }

                tasklist_adapter=new AmcleadAdapter(Amclead.this, models, Amclead.this,Amclead.this,Amclead.this);
                listView.setLayoutManager(new LinearLayoutManager(Amclead.this));

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                listView.setLayoutManager(mLayoutManager);
                listView.setItemAnimator(new DefaultItemAnimator());
               // listView.addItemDecoration(new MyDividerItemDecoration(Amclead.this,DividerItemDecoration.VERTICAL, 36));
                listView.setAdapter(tasklist_adapter);

                tasklist_adapter.notifyDataSetChanged();



            }
            else {

                ll_nodata.setVisibility(View.VISIBLE);

                //  Toast.makeText(Amclead.this,"No data found", Toast.LENGTH_SHORT).show();
            }


        }catch (Exception  e){

            e.printStackTrace();
        }
    }

    public void getcustomer(){

        String url = AllUrl.baseUrl+"sa/customers/search?contact="+cust_mobile+"&zzfranch="+FrCode+"&zzsoldto="+custcode;

        System.out.println("url-->"+url);

        if (CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            viewsearchdata(response);

                            searchdata=response;


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);

                }
            });
// Add the request to the RequestQueue.

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);


        }
    }

    public void viewsearchdata(String response){



        try {
            progressBar.setVisibility(View.GONE);
            JSONObject jsonObject=new JSONObject(response);

            String status=jsonObject.getString("Status");

            if (status.equals("true")){

                ll_nodata.setVisibility(View.GONE);
                JSONArray jsonArray=jsonObject.getJSONArray("Data");

                models=new ArrayList<>();


                for (int i=0;i<jsonArray.length();i++){

                    JSONObject object=jsonArray.getJSONObject(i);

                    model=new AmcleadModel();
                    model.setCustid(object.getString("zzsoldto"));
                    model.setCustname(object.getString("zzname_org1") + " "+object.getString("zzname_org2"));
                    model.setCustaddress(object.getString("zzstr_suppl1"));
                    model.setCustemail(object.getString("zzemail"));
                    model.setCustphone(object.getString("zztel_number"));
                    model.setProduct_name(object.getString( "zz0012")+" ("+object.getString( "Product")+")");
                    model.setProductid(object.getString( "zzr3mat_id"));
                    model.setSerialno(object.getString("zzr3ser_no"));
                    model.setAltphone(object.getString("zzalt_number"));
                    model.setFrcode(object.getString("zzfranch"));
                    model.setProduct(object.getString("zz0012"));
                    models.add(model);

                }

                tasklist_adapter=new AmcleadAdapter(Amclead.this, models, Amclead.this,Amclead.this,Amclead.this);
                listView.setLayoutManager(new LinearLayoutManager(Amclead.this));

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                listView.setLayoutManager(mLayoutManager);
                listView.setItemAnimator(new DefaultItemAnimator());
               // listView.addItemDecoration(new MyDividerItemDecoration(Amclead.this,DividerItemDecoration.VERTICAL, 36));
                listView.setAdapter(tasklist_adapter);

                tasklist_adapter.notifyDataSetChanged();

                ll_filter.setVisibility(View.VISIBLE);
                ll_data.setVisibility(View.VISIBLE);

            }

            else {
                ll_data.setVisibility(View.VISIBLE);
                Toast.makeText(Amclead.this, "No Data Found", Toast.LENGTH_SHORT).show();
                ll_nodata.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){

            e.printStackTrace();
        }

    }

    public static void hideSoftKeyboard(Activity activity) {

        try {

            if (activity != null) {
                InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
                }
            }
        }
        catch (Exception e){

            e.printStackTrace();


        }

    }

    public void showerrorpop(String text){

        tv_error.setText(text);
        tv_error.setVisibility(View.VISIBLE);
        Animation animation;
        animation = AnimationUtils.loadAnimation(Amclead.this, R.anim.slide_down);
        tv_error.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error.setAnimation(null);
                tv_error.setVisibility(View.GONE);

                //  sessionManager.logoutUser();


            }
        }, 7000);
    }

}


