package technician.ifb.com.ifptecnician.stock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RequestStock extends AppCompatActivity implements RequestAdapter.Assignclick, SwipeRefreshLayout.OnRefreshListener{

    String PartnerId,FrCode;
    SessionManager sessionManager;
    ProgressBar progressBar;
    String Component,ComponentDescription,FGDescription,FGProduct,FrCodes,MaterialCategory,good_stock,refurbished_stock;
    RequestAdapter stockAdapter;
    public RequestModel model;
    private List<RequestModel> stockModels;
    EditText et_search;
    RecyclerView listView;
    String searchitem;
    private SearchView searchView;
    Button my_stock_edit;
    TextView tv_returnall;
    String returnstus="select_all";
    String returnitem;
    LinearLayout ll_connection,ll_nodata;
    private SwipeRefreshLayout swipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_stock);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("My Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ll_connection= findViewById(R.id.ll_nointernet);
        ll_nodata= findViewById(R.id.ll_nodata);
        swipeView=findViewById(R.id.swipe_views);

        swipeView.setOnRefreshListener(this);
        // set the swipe time color
        swipeView.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        progressBar=findViewById(R.id.taskProcessing);
        sessionManager=new SessionManager(getApplicationContext());

        listView=findViewById(R.id.rv_stock);
        et_search=findViewById(R.id.et_search);
        // et_search.addTextChangedListener(watch);

        stockModels=new ArrayList<>();
        stockAdapter=new RequestAdapter(this, stockModels,this);
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
            getstock();

        }catch (Exception e){

            e.printStackTrace();
        }
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void getstock() {


        stockModels.clear();
       //  REQUESTED REQUEST
        String url = "https://sapsecurity.ifbhub.com/api/stock/return-request/?TechId="+PartnerId+"&StockType=REQUESTED";

        System.out.println("url-->"+url);

        if (CheckConnectivity.getInstance(this).isOnline()) {

            ll_nodata.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            ll_connection.setVisibility(View.GONE);
            RequestQueue queue = Volley.newRequestQueue(this);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                ll_nodata.setVisibility(View.GONE);
                                swipeView.setEnabled(false);
                                List<RequestModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<RequestModel>>() {
                                }.getType());

                                stockModels.clear();
                                stockModels.addAll(items);
                                // refreshing recycler view
                                stockAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);

                            }catch (Exception e){

                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                  //  Toast.makeText(RequestStock.this, "No item found", Toast.LENGTH_SHORT).show();
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
        else{

            ll_connection.setVisibility(View.VISIBLE);
            swipeView.setEnabled(false);
        }
    }


    public void createdata(int pos){

        try{

            JSONArray jsonArray=new JSONArray();
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("OrderId", "12345");
            jsonObject.put("TechCode",PartnerId);
            jsonObject.put("ItemId", stockModels.get(pos).ItemId);
            jsonObject.put("ItemName", stockModels.get(pos).ItemName);
            jsonObject.put("Quantity","0");
            jsonObject.put("IssuedQty",stockModels.get(pos).IssuedQty);
            jsonObject.put("Amount","0");
            jsonObject.put("stock_type","REQUESTED");
            jsonObject.put("Status","ACCEPTED");
            jsonArray.put(jsonObject);

            System.out.println("jsonArray-->  "+jsonArray);


            if (stockModels.get(pos).IssuedQty.equals("0")){

                Toast.makeText(this, "Issue QuAntity is 0   ", Toast.LENGTH_SHORT).show();
            }
            else {
                submitfinaldata(jsonArray);
            }



        }catch (Exception e){

            e.printStackTrace();
        }
    }


    public void submitfinaldata(JSONArray jsonArray){


        if (CheckConnectivity.getInstance(this).isOnline()) {

            ll_connection.setVisibility(View.GONE);

            String url = "https://sapsecurity.ifbhub.com/api/stock/add/return-stock/";

            System.out.println(url);

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            final String requestBody = jsonArray.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    //  System.out.println(response);zzzz

                    getstock();
//                Alert alert=new Alert();
//                alert.showDialog(RequestStock.this,"Requested Spare","Request Accepted");
//
//
                    startActivity(new Intent(RequestStock.this, Mystock.class));


                    // getstock();
                    if (response.equals("200")) {

                        // if data insert success then insert submit ticket detais

//                    Toast.makeText(Mystock.this, "Submit data ", Toast.LENGTH_SHORT).show();


                        // getstock();

                    } else {
                        // if data insert success then insert submit ticket detais

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

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

                        System.out.println("responseString --->" + responseString);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);

        }
        else {

            ll_connection.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onassignclick(int position) {
        createdata(position);
    }

    @Override
    public void onRefresh() {

//        if (CheckConnectivity.getInstance(RequestStock.this).isOnline()) {
//            ll_connection.setVisibility(View.GONE);
//            getstock();
//        }
//        else {
//
//            ll_connection.setVisibility(View.VISIBLE);
//            swipeView.setRefreshing(false);
//        }

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (CheckConnectivity.getInstance(RequestStock.this).isOnline()) {
            ll_connection.setVisibility(View.GONE);
            getstock();
        }
        else {

            ll_connection.setVisibility(View.VISIBLE);
            swipeView.setRefreshing(false);
        }
    }
}