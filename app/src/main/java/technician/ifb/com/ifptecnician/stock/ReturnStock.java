package technician.ifb.com.ifptecnician.stock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReturnStock extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    String PartnerId,FrCode;
    SessionManager sessionManager;
    ProgressBar progressBar;
    String Component,ComponentDescription,FGDescription,FGProduct,FrCodes,MaterialCategory,good_stock,refurbished_stock;
    ReturnAdapter stockAdapter;
    public ReturnModel model;
    private List<ReturnModel> stockModels;
    EditText et_search;
    RecyclerView listView;
    String searchitem;
    private SearchView searchView;
    Button my_stock_edit;
    TextView tv_returnall;
    String returnstus="select_all";
    String returnitem;

    Toolbar toolbar;
    LinearLayout ll_connection,ll_nodata;
    private SwipeRefreshLayout swipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_stock);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Request for Return");
        //getSupportActionBar().setBackgroundDrawable(R.drawable.summary_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        ll_connection= findViewById(R.id.ll_nointernet);
        ll_nodata= findViewById(R.id.ll_nodata);
        swipeView=findViewById(R.id.swipe_view);

        swipeView.setOnRefreshListener(this);
        // set the swipe time color
        swipeView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        ll_connection= findViewById(R.id.ll_nointernet);
        progressBar=findViewById(R.id.taskProcessing);
        sessionManager=new SessionManager(getApplicationContext());

        listView=findViewById(R.id.rv_stock);

        // et_search.addTextChangedListener(watch);

        stockModels=new ArrayList<>();
        stockAdapter=new ReturnAdapter(this, stockModels);
        listView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        // listView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        listView.setAdapter(stockAdapter);

        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            FrCode=user.get(SessionManager.KEY_FrCode);
            getreturn();

        }catch (Exception e){

            e.printStackTrace();

        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


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
                stockAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                stockAdapter.getFilter().filter(query);
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



    public void getreturn(){



            stockModels.clear();
       // 18883975
            String url = "https://sapsecurity.ifbhub.com/api/stock/return-request/?TechId=18883975&StockType=RETURNED";

            System.out.println("url-->"+url);

            if (CheckConnectivity.getInstance(this).isOnline()) {

                ll_connection.setVisibility(View.GONE);
                ll_nodata.setVisibility(View.GONE);

                progressBar.setVisibility(View.VISIBLE);
                RequestQueue queue = Volley.newRequestQueue(this);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                               swipeView.setEnabled(false);

                                try {

                                    List<ReturnModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<ReturnModel>>() {
                                    }.getType());

                                    stockModels.clear();
                                    stockModels.addAll(items);
                                    // refreshing recycler view
                                    stockAdapter.notifyDataSetChanged();

                                    progressBar.setVisibility(View.GONE);
                                    progressBar.setVisibility(View.GONE);

                                }catch (Exception e){

                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.GONE);


                        swipeView.setEnabled(false);

                        ll_nodata.setVisibility(View.VISIBLE);
                    }
                });
// Add the request to the RequestQueue.

                int socketTimeout = 10000; //10 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                queue.add(stringRequest);
            }

            else {

                swipeView.setEnabled(false);
                ll_connection.setVisibility(View.VISIBLE);
                ll_nodata.setVisibility(View.GONE);
            }
        }

    @Override
    public void onRefresh() {
        if (CheckConnectivity.getInstance(ReturnStock.this).isOnline()) {
            ll_connection.setVisibility(View.GONE);
            getreturn();
        }
        else {

            ll_connection.setVisibility(View.VISIBLE);
            swipeView.setRefreshing(false);
        }

    }
}