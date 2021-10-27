package technician.ifb.com.ifptecnician.exchange;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import technician.ifb.com.ifptecnician.TodaySale;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.exchange.adapter.ExchangeListAdapter;
import technician.ifb.com.ifptecnician.exchange.model.Exchange_list_model;
import technician.ifb.com.ifptecnician.fragment.adapter.TodaysSalesAdapter;
import technician.ifb.com.ifptecnician.fragment.dummy.TodaysalesModel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class ExchangeList extends AppCompatActivity implements ExchangeListAdapter.JobsAdapterListener, SwipeRefreshLayout.OnRefreshListener{

private static final String TAG = "Exchange List";

        SessionManager sessionManager;
        String PartnerId;
        RecyclerView rv_exchangelist;
        private List<Exchange_list_model> tasklist;
        public Exchange_list_model model;
        ExchangeListAdapter tasklist_adapter;
        String current_date;
        SwipeRefreshLayout mSwipeRefreshLayout;
        String currentdate,beforedate;
        ProgressBar progressBar;
        LinearLayout ll_nointernet;
        LinearLayout ll_no_data;
        private SearchView searchView;
        String [] todaylist={"Assigned","Pending"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rv_exchangelist=findViewById(R.id.rv_exchangelist);
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
        tasklist_adapter=new ExchangeListAdapter(ExchangeList.this, tasklist,this);
        rv_exchangelist.setLayoutManager(new LinearLayoutManager(ExchangeList.this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ExchangeList.this);
        rv_exchangelist.setLayoutManager(mLayoutManager);
        rv_exchangelist.setItemAnimator(new DefaultItemAnimator());
        rv_exchangelist.addItemDecoration(new MyDividerItemDecoration(ExchangeList.this, DividerItemDecoration.VERTICAL, 36));
        rv_exchangelist.setAdapter(tasklist_adapter);

        sessionManager=new SessionManager(ExchangeList.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);

        gettodaylistdata(PartnerId);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.swipe_exchange);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

    }


    public void gettodaylistdata(String PartnerId){
        tasklist.clear();

        for(int i=0;i<todaylist.length;i++){

            gettodaydata(PartnerId,todaylist[i]);
        }
        //  getassigndata(PartnerId,"Assigned");
    }


    private void gettodaydata(String PartnerId,String status) {


        if (CheckConnectivity.getInstance(ExchangeList.this).isOnline()) {

            ll_nointernet.setVisibility(View.GONE);


            String url=  AllUrl.baseUrl + "ServiceOrder/getServiceOrder?TechCode=" + PartnerId + "&Status="+status;

            //String url= "https://sapsecurity.ifbhub.com/api/ServiceOrder/getServiceOrder?TechCode=18450716&Status=Closed";

         //   String url="https://sapsecurity.ifbhub.com/api/ServiceOrder/getServiceOrder?TechCode=11955130&Status=Pending";

            System.out.println(url);
            progressBar.setVisibility(View.VISIBLE);
            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {


                            System.out.println("exchange"+response);

                            progressBar.setVisibility(View.GONE);
                            if (response == null) {
                                rv_exchangelist.setVisibility(View.GONE);
                                ll_no_data.setVisibility(View.VISIBLE);
                            }
                            else{
                                try{

                                    rv_exchangelist.setVisibility(View.VISIBLE);
                                    ll_no_data.setVisibility(View.GONE);


                                    List<Exchange_list_model> items = new Gson().fromJson(response.toString(), new TypeToken<List<Exchange_list_model>>() {
                                    }.getType());

                                    // adding contacts to contacts list
                                    tasklist.addAll(items);

                                    // refreshing recycler view
                                    tasklist_adapter.notifyDataSetChanged();
                                    filterdata();
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
                    rv_exchangelist.setVisibility(View.GONE);
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
        //gettodaydata(PartnerId);
        gettodaylistdata(PartnerId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onTaskSelected(TasklistModel tasklistModel) {
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exchange_search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.app_bar_exchangelist_search).getActionView();
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


    public void filterdata(){


        try {
            int size = tasklist.size() - 1;

            for (int i = size; i >= 0; i--) {

                String data = tasklist.get(i).getPotential_exchange_flag().replace(" ", "");

                if (data.equals("X"))

                {
                    tasklist_adapter.notifyDataSetChanged();

                }
                else {
                    tasklist.remove(i);
                    tasklist_adapter.notifyDataSetChanged();

                }
            }

            if (tasklist.size() == 0) {

                rv_exchangelist.setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);

            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }
}
