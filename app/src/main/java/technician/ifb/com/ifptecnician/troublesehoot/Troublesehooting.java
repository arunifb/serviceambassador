package technician.ifb.com.ifptecnician.troublesehoot;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.LoginActivity;
import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.MyApplication;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.TaskListActivity;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.troublesehoot.model.ModelsModel;
import technician.ifb.com.ifptecnician.troublesehoot.model.ProblemModel;
import technician.ifb.com.ifptecnician.troublesehoot.model.ProductModel;
import technician.ifb.com.ifptecnician.troublesehoot.model.TroubleShootModel;

public class Troublesehooting extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        SwipeRefreshLayout.OnRefreshListener,TroubleShootAdapter.JobsAdapterListener,TroubleShootAdapter.Yesclick,
        TroubleShootAdapter.Noclick{


    ProgressBar progressBar;
    TextView tv_smssend;
    Spinner troublesehooting_spnr_product,troublesehooting_spnr_model,troublesehooting_spnr_problem;
    ArrayList<ModelsModel> modelsModels = new ArrayList<>();
    ArrayList<ProblemModel> problemModels = new ArrayList<>();
    ArrayList<ProductModel> ProductModel = new ArrayList<>();
    private SwipeRefreshLayout swipeView;
    String productcode="",modelcode="",problemcode="",problemstep="",problem_model_code="";
    public TroubleShootModel model;
    private List<TroubleShootModel> troubleShootModels;
    TroubleShootAdapter troubleShootAdapter;
    RecyclerView listView;
    String all_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_troublesehooting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tv_smssend=findViewById(R.id.tv_error_message);

        progressBar=findViewById(R.id.taskProcessing);

        troublesehooting_spnr_product=findViewById(R.id.troublesehooting_spnr_product);
        troublesehooting_spnr_model=findViewById(R.id.troublesehooting_spnr_model);
        troublesehooting_spnr_problem=findViewById(R.id.troublesehooting_spnr_problem);

        troublesehooting_spnr_product.setOnItemSelectedListener(this);
        troublesehooting_spnr_model.setOnItemSelectedListener(this);
        troublesehooting_spnr_problem.setOnItemSelectedListener(this);

        listView=findViewById(R.id.lv_problem_step);

        troubleShootModels=new ArrayList<>();
        troubleShootAdapter=new TroubleShootAdapter(this, troubleShootModels, this,this,this);
        listView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
      //  listView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        listView.setAdapter(troubleShootAdapter);

        getproduct();

        // swipe to refrss section
//        swipeView=findViewById(R.id.swipe_view);
//
//        swipeView.setOnRefreshListener(this);
//        // set the swipe time color
//        swipeView.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

    }

  //  https://sapsecurity.ifbhub.com/api/Product/getProduct


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.troublesehooting_spnr_product) {

            ProductModel selectedItem = (ProductModel) parent.getSelectedItem();

            productcode = selectedItem.getCode();

            if (!productcode.equals("")){

                getmocdel();
            }

            else {
                problemModels.clear();
                modelsModels.clear();

                modelsModels.add(new ModelsModel(" -- select model -- ", ""));

                ArrayAdapter recheck_adapter = new ArrayAdapter(Troublesehooting.this, android.R.layout.simple_spinner_item, modelsModels);
                recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                troublesehooting_spnr_model.setAdapter(recheck_adapter);
                troublesehooting_spnr_model.setOnItemSelectedListener(Troublesehooting.this);

                problemModels.add(new ProblemModel(" -- select problem -- ", ""));
                ArrayAdapter arrayAdapter = new ArrayAdapter(Troublesehooting.this, android.R.layout.simple_spinner_item, problemModels);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                troublesehooting_spnr_problem.setAdapter(arrayAdapter);
                troublesehooting_spnr_problem.setOnItemSelectedListener(Troublesehooting.this);

            }

        }
        else if(parent.getId() == R.id.troublesehooting_spnr_model){

            ModelsModel modelsModel=(ModelsModel) parent.getSelectedItem();

            modelcode=modelsModel.getCode();

            if (!modelcode.equals("")){

                getproblem();
            }

            else {
                problemModels.clear();
                problemModels.add(new ProblemModel(" -- select problem -- ", ""));
                ArrayAdapter arrayAdapter = new ArrayAdapter(Troublesehooting.this, android.R.layout.simple_spinner_item, problemModels);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                troublesehooting_spnr_problem.setAdapter(arrayAdapter);
                troublesehooting_spnr_problem.setOnItemSelectedListener(Troublesehooting.this);
            }
        }
        else if(parent.getId() == R.id.troublesehooting_spnr_problem){

           ProblemModel problemModel=(ProblemModel) parent.getSelectedItem();

            problem_model_code=problemModel.getCode();

            if (!problem_model_code.equals("")){

                getproblemstep(problem_model_code);
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void getproduct() {

        if (CheckConnectivity.getInstance(this).isOnline()){

           // showProgressDialog();
            final String URL = "https://sapsecurity.ifbhub.com/api/Product/getProduct";
            System.out.println("producturl  "+URL);
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            System.out.println("getProduct response--->"+ response);


                            if (response == null){

                                showsmspopup("No data found");
                                return;
                            }

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getString("Status").equals("true")) {

                                    JSONArray array = new JSONArray(jsonObject.getString("Data"));

                                    ProductModel.clear();
                                    ProductModel.add(new ProductModel(" -- select product -- ", ""));


                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject obj = array.getJSONObject(i);
                                        ProductModel.add(new ProductModel(obj.getString("Name"), obj.getString("Code")));
                                    }

                                    ArrayAdapter recheck_adapter = new ArrayAdapter(Troublesehooting.this, android.R.layout.simple_spinner_item, ProductModel);
                                    recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    troublesehooting_spnr_product.setAdapter(recheck_adapter);
                                    troublesehooting_spnr_product.setOnItemSelectedListener(Troublesehooting.this);
//
                                    } else {

                                     showsmspopup("No data found");

                                    problemModels.clear();
                                    problemModels.add(new ProblemModel(" -- select product -- ", ""));

                                    ArrayAdapter recheck_adapter = new ArrayAdapter(Troublesehooting.this, android.R.layout.simple_spinner_item, problemModels);
                                    recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    troublesehooting_spnr_product.setAdapter(recheck_adapter);
                                    troublesehooting_spnr_product.setOnItemSelectedListener(Troublesehooting.this);
//

                                    // Toast.makeText(Troublesehooting.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    showsmspopup("Please try after some time");
                    System.out.println("VolleyError errror ");

                     }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
        else {

            showsmspopup("No internet connection");

        }

    }

    public void getmocdel() {

        if (CheckConnectivity.getInstance(this).isOnline()){

            // showProgressDialog();
            final String URL = "https://sapsecurity.ifbhub.com/api/Product/getProductModel?model.troubleshoot.Code="+productcode;


            System.out.println("Model url-->"+URL);
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            System.out.println("product details-->" + response);

                            if (response == null){

                                showsmspopup("No data found");
                                return;
                            }

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getString("Status").equals("true")) {

                                    JSONArray array = new JSONArray(jsonObject.getString("Data"));

                                    modelsModels.clear();
                                    modelsModels.add(new ModelsModel(" -- select model -- ", ""));

                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject obj = array.getJSONObject(i);
                                        String ProductModels=obj.getString("ProductModel");

                                        JSONObject json=new JSONObject(ProductModels);

                                        modelsModels.add(new ModelsModel(json.getString("MName"), json.getString("MId")));
                                    }

                                    ArrayAdapter recheck_adapter = new ArrayAdapter(Troublesehooting.this, android.R.layout.simple_spinner_item, modelsModels);
                                    recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    troublesehooting_spnr_model.setAdapter(recheck_adapter);
                                    troublesehooting_spnr_model.setOnItemSelectedListener(Troublesehooting.this);


//
                                } else {

                                    showsmspopup("No data found");

                                    modelsModels.clear();
                                    modelsModels.add(new ModelsModel(" -- select model -- ", ""));

                                    ArrayAdapter recheck_adapter = new ArrayAdapter(Troublesehooting.this, android.R.layout.simple_spinner_item, modelsModels);
                                    recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    troublesehooting_spnr_model.setAdapter(recheck_adapter);
                                    troublesehooting_spnr_model.setOnItemSelectedListener(Troublesehooting.this);

                                    // Toast.makeText(Troublesehooting.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    showsmspopup("Please try after some time");

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
        else {

            showsmspopup("No internet connection");

        }

    }

    public void getproblem() {

        if (CheckConnectivity.getInstance(this).isOnline()){

            // showProgressDialog();
           // modelcode
            final String URL = "https://sapsecurity.ifbhub.com/api/Product/getProductModelProblem?model.troubleshoot.Refid=1";
            System.out.println("Problem  url-->"+URL);

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            System.out.println("product details-->" + response);

                            if (response == null){

                                showsmspopup("No data found");
                                return;
                            }

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getString("Status").equals("true")) {

                                    JSONArray array = new JSONArray(jsonObject.getString("Data"));

                                    problemModels.clear();
                                    problemModels.add(new ProblemModel(" -- select problem -- ", ""));

                                    for (int i = 0; i < array.length(); i++) {


                                        JSONObject obj = array.getJSONObject(i);
                                        String ModelProblems=obj.getString("ModelProblem");

                                        String productmodel =obj.getString("ProductModel");
                                        JSONObject jsonObj=new JSONObject(productmodel);
                                        JSONObject json=new JSONObject(ModelProblems);

                                        problemModels.add(new ProblemModel(json.getString("PName"), jsonObj.getString("MId")));

                                    }

                                    ArrayAdapter arrayAdapter = new ArrayAdapter(Troublesehooting.this, android.R.layout.simple_spinner_item, problemModels);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    troublesehooting_spnr_problem.setAdapter(arrayAdapter);
                                    troublesehooting_spnr_problem.setOnItemSelectedListener(Troublesehooting.this);
//
                                } else {

                                    showsmspopup("No data found");

                                    problemModels.clear();
                                    problemModels.add(new ProblemModel(" -- select problem -- ", ""));
                                    ArrayAdapter arrayAdapter = new ArrayAdapter(Troublesehooting.this, android.R.layout.simple_spinner_item, problemModels);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    troublesehooting_spnr_problem.setAdapter(arrayAdapter);
                                    troublesehooting_spnr_problem.setOnItemSelectedListener(Troublesehooting.this);

                                    // Toast.makeText(Troublesehooting.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    showsmspopup("Please try after some time");

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
        else {

            showsmspopup("No internet connection");

        }

    }

    public void getproblemstep(String problemcodes){

        if (CheckConnectivity.getInstance(this).isOnline()){

            // showProgressDialog();
            final String URL = "https://sapsecurity.ifbhub.com/api/Product/getModelProblemStep?model.troubleshoot.Refid="+problemcodes;
            System.out.println("getModelProblemStep-->"+ URL);

            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            System.out.println("product details-->" + response);

                            if (response == null){

                                showsmspopup("No data found");
                                return;
                            }

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getString("Status").equals("true")) {


                                    all_data=jsonObject.getString("Data");

                                    JSONArray array = new JSONArray(jsonObject.getString("Data"));

                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject obj = array.getJSONObject(i);
                                        String ModelProblems=obj.getString("ModelProblemStep");
                                        JSONObject json=new JSONObject(ModelProblems);
                                        model=new TroubleShootModel();
                                        model.setAction(json.getString("Action"));
                                        model.setCheck(json.getString("Check"));
                                        model.setStatus(json.getString("Status"));
                                        model.setStep(json.getString("Step"));
                                        if (i==0){

                                            model.setRunning("running");
                                        }
                                        else {

                                            model.setRunning("");
                                        }

                                        troubleShootModels.add(model);
                                    }

                                    troubleShootAdapter=new TroubleShootAdapter(Troublesehooting.this, troubleShootModels, Troublesehooting.this,Troublesehooting.this,Troublesehooting.this);

                                    troubleShootAdapter.notifyDataSetChanged();
//
                                } else {

                                    // Toast.makeText(Troublesehooting.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    showsmspopup("Please try after some time ");

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
        else {

            showsmspopup("No internet connection");

        }

    }

    public void showsmspopup(String message){

        tv_smssend.setText(message);

        tv_smssend.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(Troublesehooting.this, R.anim.slide_down);
        tv_smssend.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_smssend.setAnimation(null);
                tv_smssend.setVisibility(View.GONE);
            }
        }, 3000);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onTaskSelected(TasklistModel tasklistModel) {

    }

    @Override
    public void onYesclick(int pos) {

        System.out.println("pos-->"+pos);

        try{

        JSONArray array = new JSONArray(all_data);

        troubleShootModels.clear();

        for (int i = 0; i < array.length(); i++) {

            System.out.println("pos --> "+ i);
            JSONObject obj = array.getJSONObject(i);
            String ModelProblems=obj.getString("ModelProblemStep");
            JSONObject json=new JSONObject(ModelProblems);
            model=new TroubleShootModel();
            model.setAction(json.getString("Action"));
            model.setCheck(json.getString("Check"));
            model.setStatus(json.getString("Status"));
            model.setStep(json.getString("Step"));

            if (i < pos+2){

                if (i == pos+1){
                    model.setRunning("running");
                }
                else {
                    model.setRunning("complete");
                }
            }
            else {
                model.setRunning("");
            }

            troubleShootModels.add(model);
        }


        troubleShootAdapter=new TroubleShootAdapter(Troublesehooting.this, troubleShootModels, Troublesehooting.this,Troublesehooting.this,Troublesehooting.this);

            listView.setLayoutManager(new LinearLayoutManager(this));

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            listView.setLayoutManager(mLayoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
          //  listView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
            listView.setAdapter(troubleShootAdapter);

            troubleShootAdapter.notifyDataSetChanged();

        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    @Override
    public void onNoclick(int pos) {

        try{

            JSONArray array = new JSONArray(all_data);
            troubleShootModels.clear();

            for (int i = 0; i < array.length(); i++) {

                JSONObject obj = array.getJSONObject(i);
                String ModelProblems=obj.getString("ModelProblemStep");
                JSONObject json=new JSONObject(ModelProblems);
                model=new TroubleShootModel();
                model.setAction(json.getString("Action"));
                model.setCheck(json.getString("Check"));
                model.setStatus(json.getString("Status"));
                model.setStep(json.getString("Step"));

                if (i < pos+1){
                    if (i==pos){

                        model.setRunning("cancel");
                    }
                    else {

                        model.setRunning("complete");
                    }

                }
                else {

                    model.setRunning("");
                }



                troubleShootModels.add(model);
            }

            troubleShootAdapter=new TroubleShootAdapter(Troublesehooting.this, troubleShootModels, Troublesehooting.this,Troublesehooting.this,Troublesehooting.this);

            listView.setLayoutManager(new LinearLayoutManager(this));

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            listView.setLayoutManager(mLayoutManager);
            listView.setItemAnimator(new DefaultItemAnimator());
          //  listView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
            listView.setAdapter(troubleShootAdapter);

            troubleShootAdapter.notifyDataSetChanged();

        }
        catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
