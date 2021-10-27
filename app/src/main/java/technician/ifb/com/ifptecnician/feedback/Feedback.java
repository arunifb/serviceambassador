package technician.ifb.com.ifptecnician.feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.ecatalog.Ecatalogadapter;
import technician.ifb.com.ifptecnician.ecatalog.Ecatalogmodel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

public class Feedback extends AppCompatActivity {


    ProgressBar progressBar;
    LinearLayout ll_nointernet;
    LinearLayout ll_nodata;
    RecyclerView lv_list;
    public ArrayList<Feedbackmodel> models;
    public Feedbackmodel model;
    FeedbackAdapter adapter;
    Button btn_submit;
    SessionManager sessionManager;
    String PartnerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Feedback");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar=findViewById(R.id.taskProcessing);
        ll_nointernet=findViewById(R.id.ll_nointernet);
        lv_list=findViewById(R.id.lv_feedback);
        ll_nodata=findViewById(R.id.ll_nodata);
        models = new ArrayList<>();
        adapter=new FeedbackAdapter(Feedback.this,models);
        lv_list.setLayoutManager(new LinearLayoutManager(Feedback.this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Feedback.this);
        lv_list.setLayoutManager(mLayoutManager);
        lv_list.setItemAnimator(new DefaultItemAnimator());
        //  lv_list.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        lv_list.setAdapter(adapter);


        try{

            sessionManager=new SessionManager(getApplicationContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);



        }catch(Exception e){

            e.printStackTrace();
        }
        getfeedback();

        btn_submit=findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatevalue();
            }
        });
    }

    public void getfeedback(){

        if ( CheckConnectivity.getInstance(Feedback.this).isOnline()) {

            ll_nointernet.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(Feedback.this);
            String url= AllUrl.baseUrl+"surveys/question";
            System.out.println("get all task-->  "+ url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println(response);

                            if (response.equals("")){

                                lv_list.setVisibility(View.GONE);
                                ll_nodata.setVisibility(View.VISIBLE);
                            }

                            else {
                                models.clear();
                                ///  result = response;

                                lv_list.setVisibility(View.VISIBLE);
                                ll_nodata.setVisibility(View.GONE);

                                try {

                                    List<Feedbackmodel> items = new Gson().fromJson(response.toString(), new TypeToken<List<Feedbackmodel>>() {
                                    }.getType());

                                    // adding contacts to contacts list
                                    models.clear();
                                    models.addAll(items);
                                    // refreshing recycler view
                                    adapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            // mSwipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    // mSwipeRefreshLayout.setRefreshing(false);
                    lv_list.setVisibility(View.GONE);
                    ll_nodata.setVisibility(View.VISIBLE);

                }
            });
// Add the request to the RequestQueue.

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);


        } else {

            ll_nointernet.setVisibility(View.VISIBLE);

        }
    }

    public void updatevalue(){

        JSONArray jsonArray=new JSONArray();

        for (int i=0;i<models.size();i++){

            try{

                JSONObject jsonObject=new JSONObject();
                jsonObject.put("SurveyId",models.get(i).SurveyId);
                jsonObject.put("QuestionId",models.get(i).QuestionId);
                jsonObject.put("PartnerId",PartnerId);
                jsonArray.put(jsonObject);
            }catch (Exception e){

                e.printStackTrace();
            }


         }

        submitfinaldata(jsonArray);

        System.out.println("Final Json Array --->"+jsonArray.toString());



    }


    public void submitfinaldata(JSONArray jsonArray){

        String url= AllUrl.baseUrl+"surveys/question/ans";

        //    System.out.println(url);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final String requestBody = jsonArray.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response);
                //  System.out.println(response);

                if (response.equals("200")){


                }
                else {

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
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        requestQueue.add(stringRequest);

    }
}