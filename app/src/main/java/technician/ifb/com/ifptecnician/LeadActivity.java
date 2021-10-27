package technician.ifb.com.ifptecnician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.adapter.LeadAdapter;
import technician.ifb.com.ifptecnician.adapter.Lead_adapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.Lead_model;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class LeadActivity extends AppCompatActivity implements Lead_adapter.JobsAdapterListener{

    private static final String TAG = LeadActivity.class.getSimpleName();
    Lead_adapter lead_adapter;
    RecyclerView listView;
    private List<Lead_model> lead_models;
    SessionManager sessionManager;
    String PartnerId,Frcode;
    EditText et_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("All Lead");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listView=findViewById(R.id.lv_alltasklist);
        et_search=findViewById(R.id.et_search);
        et_search.addTextChangedListener(watch);

        lead_models = new ArrayList<>();
        lead_adapter=new Lead_adapter(this, lead_models, this);
        listView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
       // listView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        listView.setAdapter(lead_adapter);


        try{

            sessionManager=new SessionManager(getApplicationContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            Frcode=user.get(SessionManager.KEY_FrCode);
            getdatas(PartnerId,"Frcode");


        }catch(Exception e){

            e.printStackTrace();
        }

    }

    private void getdatas(String PartnerId, String sta) {

        if (CheckConnectivity.getInstance(LeadActivity.this).isOnline()) {

          String  URL = AllUrl.baseUrl + "leads/getLead?model.lead.TechCode="+PartnerId+"";

          System.out.println(URL);

            showProgressDialog();

// pass second argument as "null" for GET requests
            JsonObjectRequest req = new JsonObjectRequest(URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            hideProgressDialog();

                            try {

                               if (response == null) {
                                Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_LONG).show();
                                return;
                              }

                                    System.out.println(response);
                                    JSONArray jsonArray = response.getJSONArray("Data");
                                    System.out.println(jsonArray.toString());

                                   List<Lead_model> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<Lead_model>>() {
                                  }.getType());
//
//                                   // adding contacts to contacts list
                                   lead_models.clear();
                                   lead_models.addAll(items);
//                                   // refreshing recycler view
                                   lead_adapter.notifyDataSetChanged();
                                   VolleyLog.v("Response:%n %s", response.toString(4));


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.e("Error: ", error.getMessage());
                    hideProgressDialog();
                }
            });

// add the request object to the queue to be executed
            MyApplication.getInstance().addToRequestQueue(req);





        }
        else {
        }
    }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }




    @Override
    public void onTaskSelected(Lead_model Lead_model) {

        startActivity(new Intent(LeadActivity.this,CloseLeadActivity.class));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    TextWatcher watch=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            lead_adapter.getFilter().filter(et_search.getText().toString());
            lead_adapter.notifyDataSetChanged();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
