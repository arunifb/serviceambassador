package technician.ifb.com.ifptecnician.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.ChangePassword;
import technician.ifb.com.ifptecnician.EssentialListActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.TaskListActivity;
import technician.ifb.com.ifptecnician.TodaySale;
import technician.ifb.com.ifptecnician.VolleyRespondsListener;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.amc.Amclead;
import technician.ifb.com.ifptecnician.amc.PunchedAmc;

import technician.ifb.com.ifptecnician.essentiallead.EssentialLeadList;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.language.LocaleHelper;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.otherticket.OtherticketList;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.service.TitelshowHides;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.stock.Mystock;
import technician.ifb.com.ifptecnician.stock.StockActivity;

public class HomeFragment extends Fragment implements
        OnChartValueSelectedListener ,View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG="Home%20Fragment";
    VolleyRespondsListener listener;
    private SwipeRefreshLayout swipeView;
    Dbhelper dbhelper;
    Context context;
    Resources resources;
    Cursor cursor;
    SessionManager sessionManager;
    String PartnerId,Frcode;
    String responsedata;
    Map<String, Integer> citys = new HashMap<>();
    RelativeLayout ll_todaytask,ll_assign,ll_pending,ll_closed,ll_cancel,ll_nr,
            rl_resolved,rl_lead,rl_requestcancellation,rl_amc,
            rl_punched_amc,rl_trouble,rl_stock,rl_ctc,rl_mystock,rl_essential;
    TextView tv_todaytask,tv_assign,tv_pending,tv_closed,tv_cancel,tv_nr,tv_resolved,tv_lead,tv_requestcancellation_count;
    boolean refreshToggle = true;
    String currentdate,beforedate;
    int total_todaytask=0,ascount=0,pencount=0,cancount=0,clocount=0,nrcount=0,rsccount=0,rccount=0;
    String mobile_no="";
    ErrorDetails errorDetails;
    int no=1;
    String [] CallType={"ZSER07","ZMAN07","ZFOC07","ZINT07","ZDD07","ZRIN07","ZREW07"};
    String mobile_details;
    SharedPreferences sharedPreferences;
    String remarkurl;
    LinearLayout ll_nointernet;
    TextView tv_error,tv_lastupdate,tv_open,tv_ass,tv_pen,tv_clo,tv_can,tv_nrs,tv_soft,tv_rc;
    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    String tss = dfs.format(c);
    View view;
    ProgressBar progressBar;
    Alert alert=new Alert();




    Cursor pcursor,check_date_cursor;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_home,container,false);
        view.setClickable(false);

        context=container.getContext();

        sessionManager=new SessionManager(getContext());
        errorDetails=new ErrorDetails();

        dbhelper=new Dbhelper(getContext());


        sharedPreferences= getActivity().getSharedPreferences(AllUrl.MOBILE_DETAILS,0);
        mobile_details=sharedPreferences.getString("Mobile_details","");

        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            mobile_no=user.get(SessionManager.KEY_mobileno);
            Frcode=user.get(SessionManager.KEY_FrCode);

        }catch (Exception e){

            e.printStackTrace();
        }


        tv_error=view.findViewById(R.id.tv_error_message);
        tv_open=view.findViewById(R.id.tv_open);

        tv_ass=view.findViewById(R.id.tv_ass);
        tv_pen=view.findViewById(R.id.tv_pen);
        tv_clo=view.findViewById(R.id.tv_clo);
        tv_can=view.findViewById(R.id.tv_can);
        tv_nrs=view.findViewById(R.id.tv_nrs);
        tv_soft=view.findViewById(R.id.tv_soft);
        tv_rc=view.findViewById(R.id.tv_rc);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(AllUrl.LENGUAGE,0);
        String lengu = sharedPref.getString("lenguage","");


        System.out.println("leng"+lengu);

        context = LocaleHelper.setLocale(getContext(), lengu);
        resources = context.getResources();
        tv_open.setText(resources.getString(R.string.open));
        tv_ass.setText(resources.getString(R.string.assigned));
        tv_pen.setText(resources.getString(R.string.pending));
        tv_clo.setText(resources.getString(R.string.closed));
        tv_can.setText(resources.getString(R.string.cancelled));
        tv_nrs.setText(resources.getString(R.string.negative_response));
        tv_soft.setText(resources.getString(R.string.softclose));
        tv_rc.setText(resources.getString(R.string.requestcancellation));


        progressBar=view.findViewById(R.id.pbProcessing);

        ll_todaytask=view.findViewById(R.id.rl_todaytask);
        tv_lastupdate=view.findViewById(R.id.tv_lastupdate);
        ll_assign=view.findViewById(R.id.rl_assigned);
        ll_pending=view.findViewById(R.id.rl_pending);
        ll_closed=view.findViewById(R.id.rl_closed);
        ll_cancel=view.findViewById(R.id.rl_cancelled);
        ll_nr=view.findViewById(R.id.rl_nr);
        rl_resolved=view.findViewById(R.id.rl_resolved);
        rl_lead=view.findViewById(R.id.rl_lead);
        rl_punched_amc=view.findViewById(R.id.rl_punched_amc);
       // rl_trouble=view.findViewById(R.id.rl_trouble);
        rl_requestcancellation=view.findViewById(R.id.rl_requestcancellation);
        rl_requestcancellation.setOnClickListener(this);
        rl_amc=view.findViewById(R.id.rl_amc);
        rl_amc.setOnClickListener(this);
        rl_stock=view.findViewById(R.id.rl_stock);
        rl_stock.setOnClickListener(this);
        rl_ctc=view.findViewById(R.id.rl_ctc);
        rl_ctc.setOnClickListener(this);
        swipeView=view.findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        swipeView.setDistanceToTriggerSync(20);// in dips
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);// LARGE also can be used
        ll_nointernet=view.findViewById(R.id.ll_nointernet);
        tv_todaytask=view.findViewById(R.id.tv_today_count);
        tv_assign=view.findViewById(R.id.tv_assign_count);
        tv_pending=view.findViewById(R.id.tv_pending_count);
        tv_closed=view.findViewById(R.id.tv_closed_count);
        tv_cancel=view.findViewById(R.id.tv_cancel_count);
        tv_nr=view.findViewById(R.id.tv_nr_count);
        tv_resolved=view.findViewById(R.id.tv_resolved_count);
        tv_lead=view.findViewById(R.id.tv_lead_count);

        tv_requestcancellation_count=view.findViewById(R.id.tv_requestcancellation_count);

        ll_todaytask.setOnClickListener(this);
        ll_assign.setOnClickListener(this);
        ll_pending.setOnClickListener(this);
        ll_closed.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        ll_nr.setOnClickListener(this);
        rl_resolved.setOnClickListener(this);
        rl_lead.setOnClickListener(this);
        rl_punched_amc.setOnClickListener(this);
       // rl_trouble.setOnClickListener(this);

        rl_mystock=view.findViewById(R.id.rl_mystock);
        rl_mystock.setOnClickListener(this);

        rl_essential=view.findViewById(R.id.rl_essential);
        rl_essential.setOnClickListener(this);


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 20);

        if (CheckConnectivity.getInstance(getActivity()).isOnline()){


           ll_nointernet.setVisibility(View.GONE);
            getAllData();
            get_pending_reason();
            getlastupdate();

            getshowhide();

            }
        else{

            ll_nointernet.setVisibility(View.VISIBLE);

            Beforeload();
        }
        return  view;
    }

    public void get_pending_reason(){

      try{

        if (!Boolean.valueOf(dbhelper.ISDateExsits(tss)).booleanValue()) {

            dbhelper.delete_pending_reasone();

            for(int p=0 ; p < CallType.length ; p++){

                getremarkdata(CallType[p]);
            }

        }
        else {

        }

      }catch (Exception e){
          e.printStackTrace();
      }

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){



           // total_todaytask=0,ascount=0,pencount=0,cancount=0,clocount=0,nrcount=0;
            case R.id.rl_todaytask:


                if(total_todaytask== 0){

                    showpop("No records found");
                }
                else{

                    try{

                        startActivity(new Intent(getActivity(), TaskListActivity.class)
                                .putExtra("status","todaytask"));
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new RuntimeException("Test Crash"); // Force a crash


                    }

                }


                break;
            case R.id.rl_assigned:
                if (ascount==0){


                    showpop("No records found");
                }
                else{


                        startActivity(new Intent(getContext(), TaskListActivity.class)
                                .putExtra("status","assign"));
                }

                break;
            case R.id.rl_pending:

                if (pencount==0){

                    showpop("No records found");
                }
                else{
                      startActivity(new Intent(getContext(), TaskListActivity.class)
                            .putExtra("status","pending"));
                }

                break;
            case R.id.rl_cancelled:

                if (cancount == 0){
                    showpop("No records found");
                }
                else{
                    startActivity(new Intent(getContext(), OtherticketList.class)
                            .putExtra("status","cancel"));
                }

                break;
            case R.id.rl_closed:
                if (clocount==0){
                    showpop("No records found");
                }
                else {

                    startActivity(new Intent(getContext(), OtherticketList.class)
                            .putExtra("status","close"));
                }

                break;

            case R.id.rl_nr:
                if (nrcount==0){
                    showpop("No records found");
                }
               else {

                    startActivity(new Intent(getContext(), TaskListActivity.class)
                            .putExtra("status", "nr"));

                }
                break;

            case R.id.rl_resolved:
                if (rsccount==0){
                    showpop("No records found");
                }
                else {
                    startActivity(new Intent(getContext(), OtherticketList.class)
                            .putExtra("status", "resolved"));
                }
                break;

            case R.id.rl_requestcancellation:
                if (rccount==0){
                    showpop("No records found");
                }
                else {

                    startActivity(new Intent(getContext(), OtherticketList.class)
                            .putExtra("status", "Request%20Cancellation"));
                }
                break;


            case R.id.rl_lead:

               // startActivity(new Intent(getContext(), LeadActivity.class));
                startActivity(new Intent(getContext(), TodaySale.class));

                break;
            case R.id.rl_punched_amc:
                startActivity(new Intent(getContext(), PunchedAmc.class));
                break;

            case R.id.rl_essential:
                startActivity(new Intent(getContext(), EssentialListActivity.class));
               // startActivity(new Intent(getContext(), CreateBill.class));
                break;

          /*  case R.id.rl_trouble:
                //startActivity(new Intent(getContext(), Troublesehooting.class));
                break;*/

            case R.id.rl_stock:
                startActivity(new Intent(getContext(), StockActivity.class));
                break;
            case R.id.rl_ctc:
                    startActivity(new Intent(getContext(), TaskListActivity.class)
                            .putExtra("status", "ctc"));

                break;

            case R.id.rl_mystock:

                startActivity(new Intent(getContext(), Mystock.class));

                break;

            case R.id.rl_amc:

                startActivity(new Intent(getContext(), Amclead.class));
                break;
                default:

                    break;

        }
    }


    @Override
    public void onRefresh() {

        if (CheckConnectivity.getInstance(getActivity()).isOnline()) {

            ll_nointernet.setVisibility(View.GONE);

            getAllData();
        }

        else {

            ll_nointernet.setVisibility(View.VISIBLE);

            swipeView.setRefreshing(false);

        }
    }

    public void  getremarkdata(String Callcode){

        remarkurl=AllUrl.baseUrl+"Reasons/pendingReasons?pendingReasone.CodeGroup="+Callcode;
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequests = new StringRequest(Request.Method.GET, remarkurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                               try{

                                    dbhelper.insert_pending_reasone_data(Callcode,response,tss);

                               }catch (Exception e){
                                   e.printStackTrace();
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
        queue.add(stringRequests);

    }

    public void Beforeload(){

        try{

            cursor=dbhelper.getalldashboard();

            if (cursor != null) {

                if (cursor.moveToFirst()) {

                    do {

                        final int recordid = cursor.getInt(cursor.getColumnIndex("recordid"));
                        String Name = cursor.getString(cursor.getColumnIndex("Name"));
                        String Count = cursor.getString(cursor.getColumnIndex("Count"));

                        switch (Name) {

                            case "Total":
                                tv_todaytask.setText(Count);
                                total_todaytask=Integer.parseInt(Count);
                                break;

                            case "Assigned":
                                tv_assign.setText(Count);
                                ascount=Integer.parseInt(Count);
                                break;

                            case "Pending":
                                pencount=Integer.parseInt(Count);
                                tv_pending.setText(Count);

                                break;

                            case "Closed":
                                tv_closed.setText(Count);
                                clocount=Integer.parseInt(Count);
                                break;

                            case "Cancelled":
                                tv_cancel.setText(Count);
                                cancount=Integer.parseInt(Count);
                                break;

                            case "Negative Response":
                                tv_nr.setText(Count);
                                nrcount=Integer.parseInt(Count);
                                break;

                            case "Resolved(softclosure)":
                                tv_resolved.setText(Count);
                                rsccount=Integer.parseInt(Count);
                                break;

                            case "RequestCancellation":
                                tv_requestcancellation_count.setText(Count);
                                rccount=Integer.parseInt(Count);
                                break;

                            default:

                                break;

                        }
                    } while (cursor.moveToNext());

                    cursor.close();


                }
            } else {


                Toast.makeText(getContext(), "No Record Found", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getAllData(){

        String url= AllUrl.baseUrl+"DashBoard/getDashBoardCount?model.dashboard.PartnerId="+PartnerId+"";

        System.out.println("url"+url);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        hideProgressDialog();

                        progressBar.setVisibility(View.GONE);
                        swipeView.setRefreshing(false);

                        getprofile();


                        System.out.println("dash board response-->"+response);

                        try {

                            JSONObject object=new JSONObject(response);

                            dbhelper.deletedashboard();

                            String status=object.getString("Status");
                            String Message=object.getString("Message");
                            if (status.equals("true")){

                                JSONArray jsonArray = new JSONArray(object.getString("Data"));

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject objs = jsonArray.getJSONObject(i);

                                    tv_todaytask.setText(objs.getString("Total"));
                                    tv_assign.setText(objs.getString("Assigned"));
                                    tv_pending.setText( objs.getString("Pending"));
                                    tv_requestcancellation_count.setText(objs.getString("ReqCan"));
                                    //objs.getString("ReqForReAllocation");
                                    tv_resolved.setText(objs.getString("SoftClosed"));
                                    tv_closed.setText(objs.getString("Closed"));
                                    tv_cancel.setText(objs.getString("Cancelled"));
                                    tv_nr.setText(objs.getString("NegResFrCut"));

                                    total_todaytask=Integer.parseInt(objs.getString("Total"));
                                    ascount=Integer.parseInt(objs.getString("Assigned"));
                                    pencount=Integer.parseInt(objs.getString("Pending"));
                                    rsccount=Integer.parseInt(objs.getString("SoftClosed"));
                                    rccount=Integer.parseInt(objs.getString("ReqCan"));
                                    cancount=Integer.parseInt(objs.getString("Cancelled"));
                                    clocount = Integer.parseInt(objs.getString("Closed"));
                                    nrcount=Integer.parseInt(objs.getString("NegResFrCut"));

                                    dbhelper.insert_dashboarddata("Total",objs.getString("Total"));
                                    dbhelper.insert_dashboarddata("Assigned",objs.getString("Assigned"));
                                    dbhelper.insert_dashboarddata("Pending",objs.getString("Pending"));
                                    dbhelper.insert_dashboarddata("Resolved(softclosure)",objs.getString("SoftClosed"));
                                    dbhelper.insert_dashboarddata("Closed",objs.getString("Closed"));
                                    dbhelper.insert_dashboarddata("RequestCancellation",objs.getString("ReqCan"));
                                    dbhelper.insert_dashboarddata("Cancelled",objs.getString("Cancelled"));
                                    dbhelper.insert_dashboarddata("Negative Response",objs.getString("NegResFrCut"));

                                }



                            }
                            else{

                                showpop("Please Try Again Later");
                               // Toast.makeText(getContext(), "Please Try Again Later", Toast.LENGTH_SHORT).show();


                            }


                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        progressBar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                 progressBar.setVisibility(View.GONE);
                 swipeView.setRefreshing(false);
               // Beforeload();


//                if (error instanceof NetworkError) {
//
//                    alert.showDialog(getActivity(),"Network Error","slow internet connection");
//
//                    ll_nointernet.setVisibility(View.GONE);
//                    Beforeload();
//
//
//                } else if (error instanceof ServerError) {
//
//
//                    alert.showDialog(getActivity(),"Server Error","Please Try Again Later");
//
//                } else if (error instanceof AuthFailureError) {
//
//
//                    alert.showDialog(getActivity(),"AuthFailure Error","Please Try Again Later");
//
//                } else if (error instanceof ParseError) {
//
//
//                    alert.showDialog(getActivity(),"Parse Error","Please Try Again Later");
//
//                } else if (error instanceof NoConnectionError) {
//                    ll_nointernet.setVisibility(View.GONE);
//                    Beforeload();
//
//                    alert.showDialog(getContext(),"NoConnection Error","Please Try Again Later");
//
//                } else if (error instanceof TimeoutError) {
//
//                    alert.showDialog(getActivity(),"Timeout Error","Please Try Again Later");
//                    //ll_nointernet.setVisibility(View.GONE);
//                    Beforeload();
//
//                    }
            }
        });
// Add the request to the RequestQueue.
        int socketTimeout = 5000; //10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);


    }

    public void showpop(String text){

        tv_error.setText(text);
        tv_error.setVisibility(View.VISIBLE);
        Animation animation;
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        tv_error.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error.setAnimation(null);
                tv_error.setVisibility(View.GONE);
            }
        }, 5000);
    }


    public void getlastupdate(){


        String url= AllUrl.baseUrl+"sa/last/crm-connectivity/staus";

        System.out.println("url"+url);
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jsonObject=new JSONObject(response);

                            String lastupdate=jsonObject.getString("LastUpdate");
                            tv_lastupdate.setText(lastupdate);

                        }catch (Exception e){

                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
// Add the request to the RequestQueue.
        int socketTimeout = 10000; //10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);


    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(activity);
//        mContext = context;
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mContext = null;
//    }



    public void getprofile(){

        if (CheckConnectivity.getInstance(getActivity()).isOnline()) {

            RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext());
            String url = AllUrl.baseUrl+"sa-profile/getProfile/?user.Mobileno="+mobile_no;


            System.out.println("profileurl-->"+url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            try {

                                JSONObject jsonObject=new JSONObject(response);
                                System.out.println("profile Response-->"+response);
                                String status=jsonObject.getString("Status");

                                if (status.equals("true")){

                                    JSONArray jsonArray=jsonObject.getJSONArray("Data");

                                    if (jsonArray.length() == 1) {
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject obj = jsonArray.getJSONObject(i);

                                            PartnerId = obj.getString("PartnerId");
                                            if (PartnerId.equals("")) {

                                                showerrorpop("PartnerId Not Found. Please Login Again");

                                            }
                                            else {


                                            }

                                        }
                                    }else{

                                        showerrorpop("Multiple Account Linked. Please Login Again");

                                    }

                                }
                                else {

                                    showerrorpop("No Valid Account Found. Please Login Again");
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

    public void getshowhide(){

        if (CheckConnectivity.getInstance(getActivity()).isOnline()){

            TitelshowHides titelshowHides=new TitelshowHides();

            titelshowHides.getStatus(new TitelshowHides.Showhide() {
                @Override
                public void onSuccess(String status, String result) {

                    if (status.equals("true")){

                        try{

                            JSONObject jsonObject=new JSONObject(result);
                            String stas=jsonObject.getString("Status");

                            if (stas.equals("true")){

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        rl_amc.setVisibility(View.VISIBLE);
                                        rl_punched_amc.setVisibility(View.VISIBLE);
                                    }
                                });


                            }
                            else {


                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        rl_amc.setVisibility(View.GONE);
                                        rl_punched_amc.setVisibility(View.GONE);
                                    }
                                });



                            }

                        }catch (Exception e){

                            e.printStackTrace();
                        }
                    }
                    else{

                        rl_amc.setVisibility(View.GONE);
                        rl_punched_amc.setVisibility(View.GONE);
                    }
                }
            }, getContext(), Frcode);


        }
    }

    public void showerrorpop(String text){

        tv_error.setText(text);
        tv_error.setVisibility(View.VISIBLE);
        Animation animation;
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
        tv_error.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error.setAnimation(null);
                tv_error.setVisibility(View.GONE);

                sessionManager.logoutUser();
            }
        }, 7000);
    }
}
