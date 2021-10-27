package technician.ifb.com.ifptecnician.essentiallead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.MyApplication;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.internet.NetworkChangeReceiver;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.offlinedata.OfflinedataService;
import technician.ifb.com.ifptecnician.service.Calltocustomer;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.session.SessionManager;
import android.Manifest;
import android.app.ActivityManager;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class EssentialLeadList extends AppCompatActivity
        implements EssentialAdapter.JobsAdapterListener,
        EssentialAdapter.Rcnclick,EssentialAdapter.Altclick,EssentialAdapter.Calltocustomer,
        SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = EssentialLeadList.class.getSimpleName();
    SharedPreferences prefdetails;
    RecyclerView listView;
    String response,status;
    public EssentialModel model;
    SessionManager sessionManager;
    String PartnerId;
    EssentialAdapter tasklist_adapter;
    Toolbar toolbar;
    String currentdate,beforedate;
    String url;
    private SearchView searchView;
    private List<EssentialModel> tasklist;
    Dbhelper dbhelper;
    ImageView iv_date,iv_ageing,iv_calltype;
    TextView tv_date,tv_ageing,tv_calltype;
    String [] todaylist={"Assigned","Pending"};
    String tss,tsss ,mobile_no;
    Cursor as_Cursor,pendingcursor,softclosecursor,nrcursor,reqcancursor;
    private BroadcastReceiver mNetworkReceiver;
    static LinearLayout ll_connection;
    ErrorDetails errorDetails;
    int dtsort_clk_cunt=0,ageing_sort_clk_count=0;
    String mobile_details="";
    SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeView;
    boolean refreshToggle = true;
    ProgressBar progressBar;
    TextView tv_smssend;
    LinearLayout ll_sms_true,ll_sms_false;
    String Ticketno;
    String refno="1402EC721EA01EEA94F93CD4CA0A6B7A";
    String dbversion="";
    Calltocustomer calltocustomer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essential_lead_list);
        errorDetails=new ErrorDetails();
        calltocustomer=new Calltocustomer();
        mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();
        ll_connection= findViewById(R.id.ll_nointernet);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        ll_sms_true=findViewById(R.id.layout_sms_true);
        ll_sms_false=findViewById(R.id.layout_sms_false);

        progressBar=findViewById(R.id.taskProcessing);
        iv_date=findViewById(R.id.tsklist_iv_date);
        iv_ageing=findViewById(R.id.tsklist_iv_ageing);
        iv_calltype=findViewById(R.id.tsklist_iv_calltype);

        tv_date=findViewById(R.id.tv_sort_date);
        tv_ageing=findViewById(R.id.tv_sort_ageing);
        tv_calltype=findViewById(R.id.tv_sort_status);
        tv_smssend=findViewById(R.id.tv_error_message);
        dbhelper=new Dbhelper(getApplicationContext());

         dbversion=AllUrl.APP_Version;

        toolbar.setTitle("Essential Lead");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sessionManager=new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);
        mobile_no=user.get(SessionManager.KEY_mobileno);
        listView=findViewById(R.id.lv_alltasklist);


        try{

            if (!isMyServiceRunning(OfflinedataService.class)){

                Intent intent = new Intent(EssentialLeadList.this, OfflinedataService.class);
                startService(intent);
            }
        }catch (Exception e){

            e.printStackTrace();
        }


        tasklist = new ArrayList<>();

       // sharedPreferences=getSharedPreferences(AllUrl.MOBILE_DETAILS,0);
       // mobile_details=sharedPreferences.getString("Mobile_details","");

        tasklist_adapter=new EssentialAdapter(this, tasklist, this,this,this,this);
        listView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
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

        gettodaylistdata();




        // swipe to refrss section
        swipeView=findViewById(R.id.swipe_view);

        swipeView.setOnRefreshListener(this);
        // set the swipe time color
        swipeView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

    }

    // ----------------   on back press section  -----------------------
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    //--------------------------- get all ticket data ----------------------------

    public void gettodaylistdata(){
        tasklist.clear();

        for(int i=0;i<todaylist.length;i++){

            gettodaydatas(PartnerId,todaylist[i]);
        }

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


    // ---------------------  get the list data any type----------------------------

    private void getdatas(String PartnerId, String sta) {


        if (CheckConnectivity.getInstance(EssentialLeadList.this).isOnline()) {


            progressBar.setVisibility(View.VISIBLE);

            if (sta.equals("today")) {

                url = AllUrl.baseUrl + "ServiceOrder/getServiceOrder?TechCode=" + PartnerId + "&Status=Assigned";

            }else if (sta.equals("Todaysales")){

                url=  AllUrl.baseUrl+"Sales/GetTodaySale?model.TechCode="+PartnerId;
            }


            else {

                url = AllUrl.baseUrl + "ServiceOrder/getServiceOrder?TechCode=" + PartnerId + "&Status=" + sta + "";
            }
            // showProgressDialog();
            System.out.println(url);
            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            if (response == null) {
                                // Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {

                                try {
                                    if (sta.equals("Assigned")) {

                                        dbhelper.delete_assign_data();
                                        dbhelper.insert_assign_data(response.toString(),tss);
                                    } else if (sta.equals("Pending")) {

                                        dbhelper.delete_pending_data();
                                        dbhelper.insert_pending_data(response.toString(),tss);
                                    } else if (sta.equals("Negative%20Response%20from%20Custome")) {

                                        dbhelper.delete_nr_data();
                                        dbhelper.insert_nr_data(response.toString(),tss);
                                    } else if (sta.equals("Resolved%20(soft%20closure)")) {

                                        dbhelper.delete_softclose_data();
                                        dbhelper.insert_softclose_data(response.toString(),tss);
                                    } else if (sta.equals("Request%20Cancellation")) {

                                        dbhelper.delete_reqcancel_data();
                                        dbhelper.insert_reqcancel_data(response.toString(),tss);
                                    }

                                    else if (sta.equals("Cancelled")) {

                                        dbhelper.delete_cancelled_data();
                                        dbhelper.insert_cancelled_data(response.toString(),tss);
                                    }
                                    else if (sta.equals("Closed")) {

                                        dbhelper.delete_closed_data();
                                        dbhelper.insert_closed_data(response.toString(),tss);
                                    }

                                }catch (Exception e){

                                    e.printStackTrace();
                                }

                                try {

                                    List<EssentialModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<EssentialModel>>() {
                                    }.getType());

                                    // adding contacts to contacts list

                                    tv_smssend.setVisibility(View.GONE);
                                    tasklist.clear();
                                    tasklist.addAll(items);
                                    // refreshing recycler view
                                    tasklist_adapter.notifyDataSetChanged();


                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            swipeView.setRefreshing(false);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    swipeView.setRefreshing(false);
                    Log.e(TAG, "Error: " + error.getMessage());
                    showsmspopup("Please try after some times");
                }
            });

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(request);

        }
        else{

            String data="";
            if (sta.equals("Assigned")){
                try{
                    as_Cursor=dbhelper.get_assign_data();

                    if (as_Cursor != null) {

                        if (as_Cursor.moveToFirst()) {
                            do {
                                final int recordid = as_Cursor.getInt(as_Cursor.getColumnIndex("recordid"));
                                data = as_Cursor.getString(as_Cursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (as_Cursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            else if (sta.equals("Pending")) {
                try{
                    pendingcursor=dbhelper.get_pending_data();

                    if (pendingcursor != null) {

                        if (pendingcursor.moveToFirst()) {
                            do {
                                final int recordid = pendingcursor.getInt(pendingcursor.getColumnIndex("recordid"));
                                data = pendingcursor.getString(pendingcursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (pendingcursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (sta.equals("Negative%20Response%20from%20Custome")) {

                try{
                    nrcursor=dbhelper.get_nr_data();

                    if (nrcursor != null) {

                        if (nrcursor.moveToFirst()) {
                            do {
                                final int recordid = nrcursor.getInt(nrcursor.getColumnIndex("recordid"));
                                data = nrcursor.getString(nrcursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (nrcursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (sta.equals("Resolved%20(soft%20closure)")) {
                try{
                    softclosecursor=dbhelper.get_softclose_data();

                    if (softclosecursor != null) {

                        if (softclosecursor.moveToFirst()) {
                            do {
                                final int recordid = softclosecursor.getInt(softclosecursor.getColumnIndex("recordid"));
                                data = softclosecursor.getString(softclosecursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (softclosecursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (sta.equals("Request%20Cancellation")) {

                try{
                    reqcancursor=dbhelper.get_reqcancel_data();

                    if (reqcancursor != null) {

                        if (reqcancursor.moveToFirst()) {
                            do {
                                final int recordid = reqcancursor.getInt(reqcancursor.getColumnIndex("recordid"));
                                data = reqcancursor.getString(reqcancursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (reqcancursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (sta.equals("Cancelled")) {

                try{
                    reqcancursor=dbhelper.get_cancelled_data();

                    if (reqcancursor != null) {

                        if (reqcancursor.moveToFirst()) {
                            do {
                                final int recordid = reqcancursor.getInt(reqcancursor.getColumnIndex("recordid"));
                                data = reqcancursor.getString(reqcancursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (reqcancursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (sta.equals("Closed")) {

                try{
                    reqcancursor=dbhelper.get_closed_data();

                    if (reqcancursor != null) {

                        if (reqcancursor.moveToFirst()) {
                            do {
                                final int recordid = reqcancursor.getInt(reqcancursor.getColumnIndex("recordid"));
                                data = reqcancursor.getString(reqcancursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (reqcancursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }
    }


    // ------------------- get ctc data --------------------------------------

    private void getctcdatas(String PartnerId, String sta) {

        if (CheckConnectivity.getInstance(EssentialLeadList.this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);

            url = AllUrl.baseUrl + "ServiceOrder/getServiceOrder?TechCode=" + PartnerId + "&Status=" + sta + "";

            // showProgressDialog();
            System.out.println(url);
            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            if (response == null) {
                                // Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {

//                                try {
//                                    if (sta.equals("Pending")) {
//
//                                        dbhelper.delete_pending_data();
//                                        dbhelper.insert_pending_data(response.toString(),tss);
//                                    }
//
//
//
//                                }catch (Exception e){
//
//                                    e.printStackTrace();
//                                }

                                try {


                                    JSONArray jsonArray=new JSONArray();


                                    for (int i=0;i<response.length();i++){

                                        JSONObject obj = response.getJSONObject(i);


                                        String PendingReason= obj.getString("PendingReason");

                                        if (PendingReason.equals("Req. for Call Type Change")){

                                            jsonArray.put(obj);
                                        }
                                    }


                                    List<EssentialModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<EssentialModel>>() {
                                    }.getType());

                                    // adding contacts to contacts list
                                    tv_smssend.setVisibility(View.GONE);
                                    tasklist.clear();
                                    tasklist.addAll(items);
                                    // refreshing recycler view
                                    tasklist_adapter.notifyDataSetChanged();

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            swipeView.setRefreshing(false);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    swipeView.setRefreshing(false);
                    Log.e(TAG, "Error: " + error.getMessage());
                    showsmspopup("Please try after some times");
                }
            });

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(request);

        }
        else{

            String data="";
            if (sta.equals("Assigned")){
                try{
                    as_Cursor=dbhelper.get_assign_data();

                    if (as_Cursor != null) {

                        if (as_Cursor.moveToFirst()) {
                            do {
                                final int recordid = as_Cursor.getInt(as_Cursor.getColumnIndex("recordid"));
                                data = as_Cursor.getString(as_Cursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (as_Cursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            else if (sta.equals("Pending")) {
                try{
                    pendingcursor=dbhelper.get_pending_data();

                    if (pendingcursor != null) {

                        if (pendingcursor.moveToFirst()) {
                            do {
                                final int recordid = pendingcursor.getInt(pendingcursor.getColumnIndex("recordid"));
                                data = pendingcursor.getString(pendingcursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (pendingcursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (sta.equals("Negative%20Response%20from%20Custome")) {

                try{
                    nrcursor=dbhelper.get_nr_data();

                    if (nrcursor != null) {

                        if (nrcursor.moveToFirst()) {
                            do {
                                final int recordid = nrcursor.getInt(nrcursor.getColumnIndex("recordid"));
                                data = nrcursor.getString(nrcursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (nrcursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (sta.equals("Resolved%20(soft%20closure)")) {
                try{
                    softclosecursor=dbhelper.get_softclose_data();

                    if (softclosecursor != null) {

                        if (softclosecursor.moveToFirst()) {
                            do {
                                final int recordid = softclosecursor.getInt(softclosecursor.getColumnIndex("recordid"));
                                data = softclosecursor.getString(softclosecursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (softclosecursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (sta.equals("Request%20Cancellation")) {

                try{
                    reqcancursor=dbhelper.get_reqcancel_data();

                    if (reqcancursor != null) {

                        if (reqcancursor.moveToFirst()) {
                            do {
                                final int recordid = reqcancursor.getInt(reqcancursor.getColumnIndex("recordid"));
                                data = reqcancursor.getString(reqcancursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (reqcancursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (sta.equals("Cancelled")) {

                try{
                    reqcancursor=dbhelper.get_cancelled_data();

                    if (reqcancursor != null) {

                        if (reqcancursor.moveToFirst()) {
                            do {
                                final int recordid = reqcancursor.getInt(reqcancursor.getColumnIndex("recordid"));
                                data = reqcancursor.getString(reqcancursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (reqcancursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (sta.equals("Closed")) {

                try{
                    reqcancursor=dbhelper.get_closed_data();

                    if (reqcancursor != null) {

                        if (reqcancursor.moveToFirst()) {
                            do {
                                final int recordid = reqcancursor.getInt(reqcancursor.getColumnIndex("recordid"));
                                data = reqcancursor.getString(reqcancursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (reqcancursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }
    }

    // --------------------------- get all ticket details ------------------------

    private void gettodaydatas(String PartnerId,String sta) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            url = AllUrl.baseUrl + "ServiceOrder/getServiceOrder?TechCode=" + PartnerId + "&Status=" + sta + "";
            progressBar.setVisibility(View.VISIBLE);
            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response == null) {
                                //  Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_LONG).show();
                                return;
                            }



                            if (sta.equals("Assigned")) {

                                dbhelper.delete_assign_data();
                                dbhelper.insert_assign_data(response.toString(),tss);
                            }
                            else if (sta.equals("Pending")) {

                                dbhelper.delete_pending_data();
                                dbhelper.insert_pending_data(response.toString(),tss);
                            }
                            List<EssentialModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<EssentialModel>>() {
                            }.getType());

                            // adding contacts to contacts list

                            tv_smssend.setVisibility(View.GONE);
                            tasklist.addAll(items);
                            // refreshing recycler view
                            tasklist_adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            swipeView.setRefreshing(false);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    swipeView.setRefreshing(false);

                    //  showsmspopup("Please try after some time");
                    //  Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_SHORT).show();
                }
            });
            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(request);

        }
        else{

            String data="";

            System.out.println("stattus-->"+ sta);

            if (sta.equals("Assigned")){
                try{
                    as_Cursor=dbhelper.get_assign_data();

                    if (as_Cursor != null) {

                        if (as_Cursor.moveToFirst()) {
                            do {
                                final int recordid = as_Cursor.getInt(as_Cursor.getColumnIndex("recordid"));
                                data = as_Cursor.getString(as_Cursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (as_Cursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            else if (sta.equals("Pending")) {
                try{
                    pendingcursor=dbhelper.get_pending_data();

                    if (pendingcursor != null) {

                        if (pendingcursor.moveToFirst()) {
                            do {
                                final int recordid = pendingcursor.getInt(pendingcursor.getColumnIndex("recordid"));
                                data = pendingcursor.getString(pendingcursor.getColumnIndex("Data"));
                                updateoffline(data);

                            } while (pendingcursor.moveToNext());
                        }
                    } else {
                        Toast.makeText(EssentialLeadList.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }



        }


    }

    @Override
    public void onTaskSelected(EssentialModel EssentialModel) {

    }


    // ------------------ list data sort  by date -------------------------------

    public void sortdate(View v){

        tv_date.setBackgroundColor(Color.LTGRAY);
        tv_ageing.setBackgroundColor(Color.WHITE);
        tv_calltype.setBackgroundColor(Color.WHITE);

        iv_date.setVisibility(View.VISIBLE);
        iv_date.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        iv_ageing.setVisibility(View.GONE);
        iv_calltype.setVisibility(View.GONE);

        if (dtsort_clk_cunt==0){
            iv_date.setImageResource(R.drawable.ic_arrow_downward_black_24dp);

            dtsort_clk_cunt = 1;
            tasklist_adapter.getFilter().filter("");
            tasklist_adapter.notifyDataSetChanged();


            try{

                Collections.sort(tasklist, new Comparator<EssentialModel>() {
                    DateFormat f = new SimpleDateFormat("dd-MMM");
                    public int compare(EssentialModel p1, EssentialModel p2) {
                        try {
                            return f.parse(p1.getCallBookDate()).compareTo(f.parse(p2.getCallBookDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                        // return String.valueOf(f).compareTo(s);
                    }
                });

                tasklist_adapter.notifyDataSetChanged();

            }catch (Exception e){

                e.printStackTrace();
            }
        }

        else if (dtsort_clk_cunt==1){

            dtsort_clk_cunt = 0;
            iv_date.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
            tasklist_adapter.getFilter().filter("");
            tasklist_adapter.notifyDataSetChanged();


            try{

                Collections.sort(tasklist, new Comparator<EssentialModel>() {
                    DateFormat f = new SimpleDateFormat("dd-MMM");
                    public int compare(EssentialModel p1, EssentialModel p2) {
                        try {
                            return f.parse(p2.getCallBookDate()).compareTo(f.parse(p1.getCallBookDate()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                        // return String.valueOf(f).compareTo(s);
                    }
                });

                tasklist_adapter.notifyDataSetChanged();

            }catch (Exception e){

                e.printStackTrace();
            }

        }
    }

    // ------------------  list data sort  by aging  -------------------------------

    public void sortageing(View v){

        tv_date.setBackgroundColor(Color.WHITE);
        tv_ageing.setBackgroundColor(Color.LTGRAY);
        tv_calltype.setBackgroundColor(Color.WHITE);
        iv_ageing.setVisibility(View.VISIBLE);
        iv_ageing.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        iv_date.setVisibility(View.GONE);
        iv_calltype.setVisibility(View.GONE);


        if (ageing_sort_clk_count==0) {

            iv_ageing.setImageResource(R.drawable.ic_arrow_downward_black_24dp);

            ageing_sort_clk_count = 1;

            try {

                tasklist_adapter.getFilter().filter("");
                tasklist_adapter.notifyDataSetChanged();

                Collections.sort(tasklist, new Comparator<EssentialModel>() {
                    public int compare(EssentialModel p1, EssentialModel p2) {

                        return Integer.compare(Integer.parseInt(p1.getAgeing()), Integer.parseInt(p2.getAgeing()));

                    }
                });

                tasklist_adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        else if(ageing_sort_clk_count==1){

            iv_ageing.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
            ageing_sort_clk_count = 0;
            try {

                tasklist_adapter.getFilter().filter("");
                tasklist_adapter.notifyDataSetChanged();

                Collections.sort(tasklist, new Comparator<EssentialModel>() {
                    public int compare(EssentialModel p1, EssentialModel p2) {

                        return Integer.compare(Integer.parseInt(p2.getAgeing()), Integer.parseInt(p1.getAgeing()));

                    }
                });

                tasklist_adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();

            }

        }



    }

    // ------------------  list data sort  by status  -------------------------------

    public void sortstatus(View v){

        tv_date.setBackgroundColor(Color.WHITE);
        tv_ageing.setBackgroundColor(Color.WHITE);
        tv_calltype.setBackgroundColor(Color.LTGRAY);
        iv_ageing.setVisibility(View.GONE);
        iv_date.setVisibility(View.GONE);
        iv_calltype.setVisibility(View.VISIBLE);
        iv_calltype.setImageResource(R.drawable.ic_arrow_downward_black_24dp);

        openpopup(v);
    }

    // --------------------  open menu popup ---------------------------------

    public void openpopup(View v){

        try
        {

            PopupMenu popup = new PopupMenu(EssentialLeadList.this,v);
            popup.getMenuInflater()
                    .inflate(R.menu.status_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    // Toast.makeText(EssentialLeadList.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();


                    // filter by Mandatory call
                    if(item.getTitle().equals("Mandatory")){

                        tasklist_adapter.getFilter().filter("MANDATORY CALLS");
                        listView.getRecycledViewPool().clear();
                        tasklist_adapter.notifyDataSetChanged();
                        errorDetails.Errorlog(EssentialLeadList.this,"Click%20On%20MANDATORY%20CALLS",TAG,"MANDATORY%20Filter","MANDATORY%20Filter",mobile_no,"0000000000","C",mobile_details,dbversion,tsss);

                    }
                    // filter by Mandatory service call
                    else if(item.getTitle().equals("Service")){

                        tasklist_adapter.getFilter().filter("SERVICE CALL");
                        listView.getRecycledViewPool().clear();
                        tasklist_adapter.notifyDataSetChanged();
                        errorDetails.Errorlog(EssentialLeadList.this,"Click%20On%20Service%20Call",TAG,"Click%20On%20Service%20Call","Service%20Filter",mobile_no,"0000000000","C",mobile_details,dbversion,tsss);
                    }
                    // filter by Mandatory service call
                    else if(item.getTitle().equals("Installation")){

                        tasklist_adapter.getFilter().filter("INSTALLATION CALL");
                        listView.getRecycledViewPool().clear();
                        tasklist_adapter.notifyDataSetChanged();
                        errorDetails.Errorlog(EssentialLeadList.this,"Click%20On%20INSTALLATION%20CALL",TAG,"Click%20On%20INSTALLATION%20CALL","INSTALLATION%20Filter",mobile_no,"0000000000","C",mobile_details,dbversion,tsss);

                    }
                    else if(item.getTitle().equals("All")){
                        errorDetails.Errorlog(EssentialLeadList.this,"Click%20On%20ALL",TAG,"Click%20On%20ALL","All%20Filter",mobile_no,"0000000000","C",mobile_details,dbversion,tsss);

                        // refreshing recycler view
                        tasklist_adapter.getFilter().filter("");
                        tasklist_adapter.notifyDataSetChanged();

                    }
                    //
                    else if(item.getTitle().equals("Courtesy")){
                        // refreshing recycler view
                        tasklist_adapter.getFilter().filter("COURTESY VISIT");
                        tasklist_adapter.notifyDataSetChanged();
                        errorDetails.Errorlog(EssentialLeadList.this,"Click%20On%20COURTESY%20VISIT",TAG,"Click%20On%20COURTESY%20VISIT","COURTESY%20VISIT",mobile_no,"0000000000","C",mobile_details,dbversion,tsss);

                    }
                    return true;
                }
            });

            popup.show();


        }catch(Exception e){
            e.printStackTrace();

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

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }


    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
        dbhelper.close();
    }

    @Override
    public void onRefresh() {

        if (CheckConnectivity.getInstance(EssentialLeadList.this).isOnline()) {

            refreshdata();
        }
        else {

            swipeView.setRefreshing(false);
        }
    }

    public void refreshdata(){

        try{

            status=getIntent().getExtras().getString("status");


            switch (status){

                case "todaytask":

                    gettodaylistdata();

                    break;
                case "assign":
                    getdatas(PartnerId,"Assigned");

                    break;
                case "pending":
                    getdatas(PartnerId,"Pending");

                    break;
                case "cancel":
                    getdatas(PartnerId,"Cancelled");

                    break;
                case "close":
                    getdatas(PartnerId,"Closed");

                    break;
                case "nr":
                    getdatas(PartnerId,"Negative%20Response%20from%20Custome");

                    break;
                case "resolved":
                    getdatas(PartnerId,"Resolved%20(soft%20closure)");

                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onAltclick(String ref,String smstype,String altno,String ticketno) {

        String smsstaus=dbhelper.getnpsstatus( altno);
        Ticketno=ticketno;

        System.out.println(smsstaus +" smsstaus");
        if (smsstaus.equals("true")){

            showsmspopup("Sms already sent !!");
            errorDetails.Errorlog(EssentialLeadList.this,"ALT%20SMS",TAG,"SMS%20already%20sent",altno,mobile_no,Ticketno,"C",mobile_details,dbversion,tsss);
        }
        else {

            AppMenuIcon("altnosms",altno,ref);


        }
    }

    @Override
    public void onRcnclick(String ref,String smstype,String rcn,String ticketno) {

        String smsstaus=dbhelper.getnpsstatus(rcn);
        Ticketno=ticketno;
        System.out.println("smsstaus"+smsstaus);

        if (smsstaus.equals("true")){
            showsmspopup("SMS already sent !!");
            errorDetails.Errorlog(EssentialLeadList.this,"RCN%20SMS",TAG,"SMS%20already%20sent",rcn,mobile_no,Ticketno,"C",mobile_details,dbversion,tsss);
        }
        else {
            AppMenuIcon("rcnnosms", rcn, ref);
        }
    }

    public void updateoffline(String data){

        try {
            List<EssentialModel> items = new Gson().fromJson(data, new TypeToken<List<EssentialModel>>() {
            }.getType());

            // adding contacts to contacts list

            tasklist.addAll(items);
            // refreshing recycler view
            tasklist_adapter.notifyDataSetChanged();

        }catch (Exception e){

            e.printStackTrace();
        }

    }

    public void showsmspopup(String message){

        tv_smssend.setText(message);

        tv_smssend.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(EssentialLeadList.this, R.anim.slide_down);
        tv_smssend.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_smssend.setAnimation(null);
                tv_smssend.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void callsms(String refno,String smstype,String Destination){

        smstype="SOFCLS";

//        refno="1402EC721EA01EEA94F93CD4CA0A6B7A";

        if (CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url =AllUrl.baseUrl+"SMS/RepeatFeedbackSMS?model.feddbacksms.RefId="+refno+"&model.feddbacksms.SMSType="+smstype+"&model.feddbacksms.Destination="+Destination;

            System.out.println(url);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            if (response.equals("")){


                            }
                            try{

                                JSONObject object=new JSONObject(response);
                                String status=object.getString("Status");
                                if (status.equals("true")){

                                    dbhelper.insert_nps_status("",Destination,"true");
                                    showsmsalert("true");
                                    errorDetails.Errorlog(EssentialLeadList.this,"SEND%20SMS",TAG,"Successfully",Destination,mobile_no,Ticketno,"S",mobile_details,dbversion,tsss);
                                }
                                else {
                                    showsmsalert("false");
                                    errorDetails.Errorlog(EssentialLeadList.this,"SEND%20SMS",TAG,"Server%20response","false%20"+Destination,mobile_no,Ticketno,"E",mobile_details,dbversion,tsss);

                                }

                            }catch (Exception e){

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    showsmsalert("false");

                }
            });

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }

    }

    public void AppMenuIcon(String type,String no,String ref){

        if (CheckConnectivity.getInstance(this).isOnline()) {

            String getMenuIconurl="";

            if (type.equals("altnosms")){

                getMenuIconurl =AllUrl.baseUrl+"AppMenuIcon/getMenuIcon?model.appNenuIcon.MenuIconID=ALT_MOB_FB_SMS";


            }else if (type.equals("rcnnosms")){

                getMenuIconurl =AllUrl.baseUrl+"AppMenuIcon/getMenuIcon?model.appNenuIcon.MenuIconID=RCN_MOB_FB_SMS";

            }
            System.out.println(getMenuIconurl);

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,getMenuIconurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println(response);

                            progressBar.setVisibility(View.GONE);

                            if (response.equals("")){

                                showsmspopup("Please try after some times");

                            }


                            try{

                                JSONObject object=new JSONObject(response);

                                String status=object.getString("Status");

                                if (status.equals("true")){


                                    String data=object.getString("Data");
                                    JSONArray array=new JSONArray(data);

                                    for (int i=0;i<1;i++){

                                        JSONObject jsonObject=array.getJSONObject(i);

                                        String IsActive=jsonObject.getString("IsActive");
                                        String MenuIconID=jsonObject.getString("MenuIconID");

                                        if (IsActive.equals("True") &&  MenuIconID.equals("RCN_MOB_FB_SMS")){

                                            callsms(ref,"",no);
                                            // errorDetails.Errorlog(EssentialLeadList.this,"SMS%20Button%20Active",TAG,IsActive,MenuIconID,mobile_no,Ticketno,"C",mobile_details,dbversion,tsss);

                                        }
                                        else if(IsActive.equals("True") &&  MenuIconID.equals("ALT_MOB_FB_SMS")){

                                            callsms(ref,"",no);
                                            //  errorDetails.Errorlog(EssentialLeadList.this,"SMS%20Button%20Active",TAG,IsActive,MenuIconID,mobile_no,Ticketno,"C",mobile_details,dbversion,tsss);

                                        }

                                        else {

                                            showsmspopup("Sms button not active");
                                            //    errorDetails.Errorlog(EssentialLeadList.this,"SMS%20Button%20Active",TAG,IsActive,MenuIconID,mobile_no,Ticketno,"C",mobile_details,dbversion,tsss);

                                        }
                                    }

                                }

                            }catch (Exception e){

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    showsmspopup("Please try after some times");


                }
            });

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);
        }

        //https://sapsecurity.ifbhub.com/api/AppMenuIcon/getMenuIcon?model.appNenuIcon.MenuIconID=RCN_MOB_FB_SMS
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (serviceClass.getName().equals(service.service.getClassName())) {

                System.out.println("Service status Running");
                return true;
            }
        }

        System.out.println("Service not Running");
        return false;

    }

    public void showsmsalert(String status){

        if (status.equals("true")){

            ll_sms_true.setVisibility(View.VISIBLE);
            ll_sms_false.setVisibility(View.GONE);

            Animation animation;
            animation = AnimationUtils.loadAnimation(EssentialLeadList.this, R.anim.slide_down);
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
            animation = AnimationUtils.loadAnimation(EssentialLeadList.this, R.anim.slide_down);
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


    @Override
    public void Onclickcall(String mobile, String ticketno) {


        calltocustomer.Calltocust(EssentialLeadList.this,mobile,ticketno,PartnerId);

//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));// Initiates the Intent
//        startActivity(intent);

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+mobile));

        if (ActivityCompat.checkSelfPermission(EssentialLeadList.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }


}
