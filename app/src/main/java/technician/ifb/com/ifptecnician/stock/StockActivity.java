package technician.ifb.com.ifptecnician.stock;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.OkHttpClient;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class StockActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener,
        StockAdapter.Assignclick,SwipeRefreshLayout.OnRefreshListener {

    String PartnerId,FrCode;
    SessionManager sessionManager;
    ProgressBar progressBar;
    String Component="",ComponentDescription="",FGDescription="",FGProduct="",
            FrCodes="",MaterialCategory="",good_stock="",refurbished_stock="";
    StockAdapter stockAdapter;
    public StockModel model;
    private List<StockModel> stockModels;
    EditText et_search,et_product;
    RecyclerView listView;
    String searchitem,product="",Subcode="";
    private SearchView searchView;
    Spinner spnr_type,spnr_product;
    EditText et_spare_name;
    Button btn_search;
    String [] array_type={"Select Category","AC","CD","DW","IND","KA","RF","ST","WD","WM","WP","MW"};
    List<String> SubProductCode=new ArrayList<>();

    LinearLayout ll_connection,ll_nodata;
    private SwipeRefreshLayout swipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Inventory Management");
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
        progressBar=findViewById(R.id.taskProcessing);
        sessionManager=new SessionManager(getApplicationContext());

        listView=findViewById(R.id.rv_stock);
        et_search=findViewById(R.id.et_search);

        et_product=findViewById(R.id.et_product);

        spnr_product=findViewById(R.id.spnr_product);
        spnr_product.setOnItemSelectedListener(this);

        stockModels=new ArrayList<>();
        stockAdapter=new StockAdapter(this, stockModels,this);
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


        }catch (Exception e){

        e.printStackTrace();

        }

        btn_search=findViewById(R.id.btn_search);


        btn_search.setOnClickListener(this);
        et_spare_name=findViewById(R.id.et_spare_name);
        spnr_type=findViewById(R.id.spnr_type);
        spnr_type.setOnItemSelectedListener(this);


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item,array_type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_type.setAdapter(aa);
        spnr_type.setOnItemSelectedListener(this);



        firstgetstock();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_search){

            product=et_product.getText().toString();
            FGDescription=et_spare_name.getText().toString();

            getstock();
        }
    }

    public void getstock(){

        if ( CheckConnectivity.getInstance(this).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);

            String urls= AllUrl.baseUrl+"bom?model.bom.MaterialCategory="+MaterialCategory+"&model.bom.FrCode="+FrCode+"&model.bom.FGProduct="+product+"&model.bom.FGDescription="+FGDescription+"&model.bom.SubProductCode="+Subcode;

            System.out.println("urls-->"+urls);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urls,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ll_nodata.setVisibility(View.GONE);

                            System.out.println("response-->"+response);

                            List<StockModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<StockModel>>() {
                            }.getType());

                            stockModels.clear();
                            stockModels.addAll(items);
                            // refreshing recycler view
                            stockAdapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);

                    stockModels.clear();
                    ll_nodata.setVisibility(View.VISIBLE);



                   // Toast.makeText(StockActivity.this, "No records found", Toast.LENGTH_SHORT).show();

                }
            });
// Add the request to the RequestQueue.

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        }
    }


    public void firstgetstock(){

        if ( CheckConnectivity.getInstance(this).isOnline()) {
            ll_connection.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            ll_nodata.setVisibility(View.GONE);
            RequestQueue queue = Volley.newRequestQueue(this);

            String urls= "https://sapsecurity.ifbhub.com/api/bom?model.bom.MaterialCategory=WM&model.bom.FrCode="+FrCode+"&model.bom.FGProduct=&model.bom.FGDescription=pcb&model.bom.SubProductCode=FL";

            System.out.println("urls-->"+urls);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urls,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            swipeView.setEnabled(false);
                            ll_nodata.setVisibility(View.GONE);
                            System.out.println("response-->"+response);

                            List<StockModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<StockModel>>() {
                            }.getType());

                            stockModels.clear();
                            stockModels.addAll(items);
                            // refreshing recycler view
                            stockAdapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    ll_nodata.setVisibility(View.VISIBLE);

                    // Toast.makeText(StockActivity.this, "No records found", Toast.LENGTH_SHORT).show();

                }
            });
// Add the request to the RequestQueue.

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        }

        else {
            ll_connection.setVisibility(View.INVISIBLE);
        }
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        if (parent.getId() == R.id.spnr_type) {
            //tree_name=parent.getItemAtPosition(position).toString();
            MaterialCategory = parent.getItemAtPosition(position).toString();
            System.out.println("MaterialCategory-->"+MaterialCategory);

            if (!MaterialCategory.equals("Select Category")) {

                getSubproduct("MaterialCategory");

            }

        }


        if (parent.getId() == R.id.spnr_product) {
            //tree_name=parent.getItemAtPosition(position).toString();

            SubProductCode=new ArrayList<>();
             Subcode = parent.getItemAtPosition(position).toString();
            System.out.println(" product-->"+ product);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void show(int pos,int no,String name){
        final Dialog npDialog = new Dialog(StockActivity.this);
        npDialog.setTitle("NumberPicker Example");
        npDialog.setContentView(R.layout.item_assign);
        Button setBtn = (Button)npDialog.findViewById(R.id.setBtn);
        Button cnlBtn = (Button)npDialog.findViewById(R.id.CancelButton_NumberPicker);
        TextView tv=npDialog.findViewById(R.id.tv_spare_name);
        final NumberPicker numberPicker = (NumberPicker)npDialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(30);
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



                try{

                    JSONArray jsonArray=new JSONArray();
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("OrderId", "12345");
                    jsonObject.put("TechCode",PartnerId);
                    jsonObject.put("ItemId", stockModels.get(pos).FGProduct);
                    jsonObject.put("ItemName", stockModels.get(pos).ComponentDescription);
                    jsonObject.put("Quantity", numberPicker.getValue());
                    jsonObject.put("stock_type","REQUESTED");

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

    @Override
    public void onassignclick(int position,int no, String name) {
        show(position,no,name);



    }


    public void  getSubproduct( String productcode){


        if ( CheckConnectivity.getInstance(this).isOnline()) {
           ll_connection.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);

            String urls="https://sapsecurity.ifbhub.com/api/product/subProduct/?ProductCode="+MaterialCategory;

            System.out.println("urls-->"+urls);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urls,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            System.out.println("response-->"+response);
                         try{


                             JSONArray jsonArray=new JSONArray(response);

                             for (int i=0;i<jsonArray.length();i++){

                                 JSONObject jsonObject=jsonArray.getJSONObject(i);

                                 SubProductCode.add(jsonObject.getString("SubProductCode"));


                             }

                             ArrayAdapter aas = new ArrayAdapter(StockActivity.this, android.R.layout.simple_spinner_item,SubProductCode);
                             aas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                             //Setting the ArrayAdapter data on the Spinner
                             spnr_product.setAdapter(aas);

                         }catch (Exception e){

                             e.printStackTrace();
                         }





                            progressBar.setVisibility(View.GONE);
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

    public void submitfinaldata(JSONArray jsonArray){

        //  String url=AllUrl.baseUrl+"ZTECHSO/AddNewZTechSO";

        String url="https://sapsecurity.ifbhub.com/api/stock/add/return-stock/";

        System.out.println(url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String requestBody = jsonArray.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                //  System.out.println(response);zzzz



                startActivity(new Intent(StockActivity.this,RequestStock.class));

//                Alert alert=new Alert();
//                alert.showDialog(StockActivity.this,"Assign Spare ","Spare Assign Request Sent Successfully");
//

                if (response.equals("200")){

                    // if data insert success then insert submit ticket detais

//                    Toast.makeText(Mystock.this, "Submit data ", Toast.LENGTH_SHORT).show();


                   // getstock();

                }
                else {
                    // if data insert success then insert submit ticket detais

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
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

                    System.out.println("responseString --->"+responseString);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);

    }

    public void requestStock(View v){

        startActivity(new Intent(StockActivity.this,RequestStock.class));
    }


    @Override
    public void onRefresh() {


        if (CheckConnectivity.getInstance(StockActivity.this).isOnline()) {
            ll_connection.setVisibility(View.GONE);
            firstgetstock();
        }
        else {

            ll_connection.setVisibility(View.VISIBLE);
            swipeView.setRefreshing(false);
        }

    }
}