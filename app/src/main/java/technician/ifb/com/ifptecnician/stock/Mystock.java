package technician.ifb.com.ifptecnician.stock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.TaskListActivity;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mystock extends AppCompatActivity implements View.OnClickListener,MystockAdapter.Ruturnclick,SwipeRefreshLayout.OnRefreshListener {


    String PartnerId,FrCode;
    SessionManager sessionManager;
    ProgressBar progressBar;
    String Component,ComponentDescription,FGDescription,FGProduct,FrCodes,MaterialCategory,good_stock,refurbished_stock;
    MystockAdapter stockAdapter;
    public MystockModel model;
    private List<MystockModel> stockModels;
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
        setContentView(R.layout.activity_mystock);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("My Stock");
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
        tv_returnall=findViewById(R.id.tv_returnall);
        tv_returnall.setOnClickListener(this);

        progressBar=findViewById(R.id.taskProcessing);
        sessionManager=new SessionManager(getApplicationContext());

        listView=findViewById(R.id.rv_stock);
        et_search=findViewById(R.id.et_search);
       // et_search.addTextChangedListener(watch);
        my_stock_edit=findViewById(R.id.my_stock_edit);

        my_stock_edit.setOnClickListener(this);

        stockModels=new ArrayList<>();
        stockAdapter=new MystockAdapter(this, stockModels,stockModels,false,this);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getstock() {


        stockModels.clear();

        String url = "https://sapsecurity.ifbhub.com/api/stock/mystock/?model.TechPartnerId="+PartnerId;

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

                            try {

                                swipeView.setRefreshing(false);
                                JSONArray jsonArray=new JSONArray(response);

                                for (int i=0;i<jsonArray.length();i++){

                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    MystockModel mystockModel=new MystockModel();
                                    mystockModel.setIscheck(false);
                                    mystockModel.setQuantity(jsonObject.getString("Quantity"));
                                    mystockModel.setAmount(jsonObject.getString("Amount"));
                                    mystockModel.setTechPartnerId(jsonObject.getString("TechPartnerId"));
                                    mystockModel.setSpareCode(jsonObject.getString("SpareCode"));
                                    mystockModel.setSpareDes(jsonObject.getString("SpareDes"));
                                    mystockModel.setReturnqty(jsonObject.getString("Quantity"));
                                    stockModels.add(mystockModel);


                                }

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

                  //  Toast.makeText(Mystock.this, "No item Found", Toast.LENGTH_SHORT).show();
                    ll_nodata.setVisibility(View.VISIBLE);
                    swipeView.setRefreshing(false);
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

    public void submitfinaldata(JSONArray jsonArray){

        //  String url=AllUrl.baseUrl+"ZTECHSO/AddNewZTechSO";

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

                    if (response.equals("200")) {

                        // if data insert success then insert submit ticket detais

//                    Toast.makeText(Mystock.this, "Submit data ", Toast.LENGTH_SHORT).show();

//                        Alert alert = new Alert();
//                        alert.showDialog(Mystock.this, "Spare Return", returnitem + " Spare return successfully done");

                        getstock();

                        startActivity(new Intent(Mystock.this,ReturnStock.class));

                    } else {
                        // if data insert success then insert submit ticket detais
                        Alert alert = new Alert();
                        alert.showDialog(Mystock.this, "Spare Return", "Please try after some time");

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

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    TextWatcher watch=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            stockAdapter.getFilter().filter(et_search.getText().toString());
            stockAdapter.notifyDataSetChanged();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


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
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.my_stock_edit:


                JSONArray jsonArray=new JSONArray();


                for (int i=0;i<stockModels.size();i++){
                    try{


                        if (stockModels.get(i).isIscheck()) {

                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("OrderId", "12345");
                            jsonObject.put("TechCode",PartnerId);
                            jsonObject.put("ItemId", stockModels.get(i).SpareCode);
                            jsonObject.put("ItemName", stockModels.get(i).SpareDes);
                            jsonObject.put("Quantity", stockModels.get(i).returnqty);

                            jsonObject.put("IssuedQty",stockModels.get(i).Quantity);
                            jsonObject.put("stock_type","RETURNED");
                            jsonArray.put(jsonObject);

                        }

                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }

                System.out.println("jsonArray-->  "+jsonArray);




                submitfinaldata(jsonArray);

                break;

            case R.id.tv_returnall:

                 if (returnstus.equals("select_all")){

                     my_stock_edit.setVisibility(View.VISIBLE);
                     returnstus="unselect_all";
                     stockAdapter.selectAll();
                     tv_returnall.setText("Back");
                 }
                 else if (returnstus.equals("unselect_all"))
                 {

                     my_stock_edit.setVisibility(View.GONE);
                     returnstus="select_all";
                     stockAdapter.unselectall();

                     tv_returnall.setText("Return All");
                 }
                break;
        }
    }

    @Override
    public void onReturnclick(int position,int number,String name) {

        show(position,number,name);

    }

    public void show(int pos,int no,String name){
        final Dialog npDialog = new Dialog(Mystock.this);
        npDialog.setTitle("NumberPicker Example");
        npDialog.setContentView(R.layout.numberpicker_layout);
        Button setBtn = (Button)npDialog.findViewById(R.id.setBtn);
        Button cnlBtn = (Button)npDialog.findViewById(R.id.CancelButton_NumberPicker);
        TextView tv=npDialog.findViewById(R.id.tv_spare_name);
        final NumberPicker numberPicker = (NumberPicker)npDialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(no);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(false);
        tv.setText(name);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // TODO Auto-generated method stub
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

              //  Toast.makeText(Mystock.this, "Number selected: " + numberPicker.getValue() , Toast.LENGTH_SHORT).show();

                 try{

                     JSONArray jsonArray=new JSONArray();
                     JSONObject jsonObject = new JSONObject();

                     jsonObject.put("OrderId", "123457");
                     jsonObject.put("TechCode", PartnerId);
                     jsonObject.put("ItemId", stockModels.get(pos).SpareCode);
                     jsonObject.put("ItemName", stockModels.get(pos).SpareDes);
                     jsonObject.put("Quantity",stockModels.get(pos).Quantity);
                     jsonObject.put("IssuedQty",numberPicker.getValue());
                     jsonObject.put("stock_type","RETURNED");

                     returnitem= stockModels.get(pos).SpareDes+" "+numberPicker.getValue();
                     jsonArray.put(jsonObject);

                     System.out.println("jsonArray-->  "+jsonArray);

                     submitfinaldata(jsonArray);

                 }catch (Exception e){

                     e.printStackTrace();
                 }


                npDialog.dismiss();
            }
        });

        cnlBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                npDialog.dismiss();
            }
        });

        npDialog.show();
    }

    public void returnStock(View view) {

        startActivity(new Intent(Mystock.this,ReturnStock.class));
    }

    @Override
    public void onRefresh() {


        if (CheckConnectivity.getInstance(Mystock.this).isOnline()) {

            ll_connection.setVisibility(View.GONE);

          getstock();
        }
        else {
            ll_connection.setVisibility(View.VISIBLE);

            swipeView.setRefreshing(false);
        }
    }
}