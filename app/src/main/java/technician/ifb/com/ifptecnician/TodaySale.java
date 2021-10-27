package technician.ifb.com.ifptecnician;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.fragment.adapter.TodaysSalesAdapter;
import technician.ifb.com.ifptecnician.fragment.dummy.TodaysalesModel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class TodaySale extends AppCompatActivity implements TodaysSalesAdapter.JobsAdapterListener, SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "Today Sales";

    SessionManager sessionManager;
    String PartnerId;
    RecyclerView rv_todayssales;
    private List<TodaysalesModel> tasklist;
    public TodaysalesModel model;
    TodaysSalesAdapter tasklist_adapter;
    String current_date;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String currentdate,beforedate;
    ProgressBar progressBar;
    LinearLayout ll_nointernet;
    LinearLayout ll_no_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_sale);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_todayssales=findViewById(R.id.rv_todayssales);
        progressBar=findViewById(R.id.taskProcessing);
        ll_nointernet=findViewById(R.id.ll_nointernet);
        ll_no_data=findViewById(R.id.ll_nodata);
        tasklist=new ArrayList<>();
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        currentdate = df.format(c);

        Calendar calendar = Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_YEAR, - (day-1));
        Date newDate = calendar.getTime();
        beforedate=df.format(newDate);
        tasklist_adapter=new TodaysSalesAdapter(TodaySale.this, tasklist,this);
        rv_todayssales.setLayoutManager(new LinearLayoutManager(TodaySale.this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(TodaySale.this);
        rv_todayssales.setLayoutManager(mLayoutManager);
        rv_todayssales.setItemAnimator(new DefaultItemAnimator());
        rv_todayssales.addItemDecoration(new MyDividerItemDecoration(TodaySale.this, DividerItemDecoration.VERTICAL, 36));
        rv_todayssales.setAdapter(tasklist_adapter);

        sessionManager=new SessionManager(TodaySale.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);

        gettodaydata(PartnerId);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.swipe_todayssales);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
    }

    private void gettodaydata(String PartnerId) {


        if (CheckConnectivity.getInstance(TodaySale.this).isOnline()) {

            ll_nointernet.setVisibility(View.GONE);

            String url= AllUrl.baseUrl+"Sales/GetTodaySale?model.TechCode="+PartnerId;
            System.out.println(url);
            progressBar.setVisibility(View.VISIBLE);
            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            progressBar.setVisibility(View.GONE);
                            if (response == null) {
                                rv_todayssales.setVisibility(View.GONE);
                                ll_no_data.setVisibility(View.VISIBLE);
                            }
                            else{
                                try{

                                    rv_todayssales.setVisibility(View.VISIBLE);
                                    ll_no_data.setVisibility(View.GONE);


                                    List<TodaysalesModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<TodaysalesModel>>() {
                                    }.getType());

                                    // adding contacts to contacts list
                                    tasklist.clear();

                                    tasklist.addAll(items);

                                    // refreshing recycler view
                                    tasklist_adapter.notifyDataSetChanged();
                                    mSwipeRefreshLayout.setRefreshing(false);

                                }catch (Exception e){

                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    // error in getting json
                    rv_todayssales.setVisibility(View.GONE);
                    ll_no_data.setVisibility(View.VISIBLE);
                }
            });

            //
            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(request);

        }
        else{

            ll_nointernet.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onRefresh() {
        gettodaydata(PartnerId);
    }

    @Override
    public void onTaskSelected(TodaysalesModel AppormentModel) {

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
