package technician.ifb.com.ifptecnician.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.MyApplication;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.fragment.adapter.SearchAdapter;
import technician.ifb.com.ifptecnician.fragment.dummy.SearchModel;
import technician.ifb.com.ifptecnician.fragment.dummy.Taskmodel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.session.SessionManager;


public class SearchFragment extends Fragment implements View.OnClickListener ,AdapterView.OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayout ll_start,ll_end;
    TextView tv_startdate,tv_enddate,tv_search;
    Spinner spnr_search,spnr_product,spnr_calltype;
    String selectesvalue,product,calltype,mobile;
    Button btn_search;
    RecyclerView lv_list;
    private List<SearchModel> models;
    SearchModel searchModel;
    long milestone=System.currentTimeMillis();
    SearchAdapter searchAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SessionManager sessionManager;
    private OnFragmentInteractionListener mListener;
    final Calendar myCalendar = Calendar.getInstance();
    String status;
    String PartnerId;
    String startdate="",enddate="";
    EditText et_ticketno,et_mobile;
    ProgressBar progressBar;
    TextView tv_error;
    LinearLayout ll_nodata,ll_search;

    String[] sta = {"Select status","Assign","pending","Cancelled","Closed"};
    String[] product_array= {"select product","AC","CD","DW","IND","KA","RF","ST","WD","WM","WP","MW"};
    String[] call_array={"select type","Mandatory","SERVICE CALL","INSTALLATION CALL","COURTESY VISIT"};


    public SearchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ll_start=view.findViewById(R.id.ll_startdate);
        ll_end=view.findViewById(R.id.ll_enddate);
        ll_start.setOnClickListener(this);
        et_mobile=view.findViewById(R.id.et_mobile);
        ll_end.setOnClickListener(this);
        lv_list=view.findViewById(R.id.lv_search);
        tv_error=view.findViewById(R.id.tv_error_message);
        progressBar=view.findViewById(R.id.taskProcessing);
        tv_startdate=view.findViewById(R.id.tv_startdate);
        tv_enddate=view.findViewById(R.id.tv_enddate);
        spnr_search=view.findViewById(R.id.spnr_search);
        spnr_search.setOnItemSelectedListener(this);
        btn_search=view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        ArrayAdapter aa = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,sta);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_search.setAdapter(aa);
        spnr_product=view.findViewById(R.id.spnr_product);

        ArrayAdapter aaproduct = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,product_array);
        aaproduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_product.setAdapter(aaproduct);
        spnr_product.setOnItemSelectedListener(this);
        spnr_calltype=view.findViewById(R.id.spnr_calltype);

        ArrayAdapter aacall = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,call_array);
        aacall.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_calltype.setAdapter(aacall);
        spnr_calltype.setOnItemSelectedListener(this);


        sessionManager=new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);
        System.out.println(PartnerId);

        et_ticketno=view.findViewById(R.id.et_ticketno);
        ll_nodata=view.findViewById(R.id.ll_nodata);


        tv_search=view.findViewById(R.id.tv_search);
        ll_search=view.findViewById(R.id.ll_search);

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_search.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.ll_startdate:

                status="start";

                DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;

            case R.id.ll_enddate:
                status="end";

                try{
                    DatePickerDialog datePickerDialogs=new DatePickerDialog(getActivity(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));
                    //following line to restrict future date selection
                    datePickerDialogs.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialogs.getDatePicker().setMinDate(milestone);
                    datePickerDialogs.show();
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;

            case R.id.btn_search:

                checkblank();

                break;
        }
    }


    public void checkblank(){

        startdate=tv_startdate.getText().toString();
        enddate=tv_enddate.getText().toString();
        mobile=et_mobile.getText().toString();


        if (selectesvalue.equals("Select status")){

            selectesvalue="";
        }
        if (product.equals("select product")){

            product="";
        }

        if (calltype.equals("select type")){

            calltype="";
        }


        if (et_ticketno.getText().toString().length() == 0)
        {
//          if(startdate.equals("From Date")){
//
//              showpop("Please select from date");
//
//        }
//        else if(enddate.equals("To Date")){
//
//              showpop("Please select to date");
//        }
//        else if (selectesvalue.equals("Select status")){
//
//              showpop("Please select status");
//
//        }
//        else{
//
//              getdata(PartnerId);
//
//        }

            getdata(PartnerId);
        }
        else {


            getresult(PartnerId);

        }


    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        if (status.equals("start")){
            tv_startdate.setText(sdf.format(myCalendar.getTime()));

            milestone=myCalendar.getTimeInMillis();

        }
        else{

           tv_enddate.setText(sdf.format(myCalendar.getTime()));
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.spnr_search){

         selectesvalue=parent.getItemAtPosition(position).toString();

        }

        if (parent.getId() == R.id.spnr_product){

            product=parent.getItemAtPosition(position).toString();
        }

        if (parent.getId() == R.id.spnr_calltype){

          calltype=parent.getItemAtPosition(position).toString();
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void  getdata(String PartnerId){


        if ( CheckConnectivity.getInstance(getContext()).isOnline()) {

           progressBar.setVisibility(View.VISIBLE);
          //  String url= AllUrl.baseUrl+"ServiceOrder/getServiceOrder?TechCode="+PartnerId+"&Status="+selectesvalue+"&FromDate="+startdate+"&ToDate="+enddate+"";
           String url="https://sapsecurity.ifbhub.com/api/ServiceOrder/getServiceOrder?TechCode="+PartnerId+"&Status="+selectesvalue+"&FromDate="+startdate+"&ToDate="+enddate+"&Mobile="+mobile+"&productCode="+product+"&CallType="+calltype;

           System.out.println("search url-->"+url);

            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            System.out.println(response);
                            if (response == null) {
                                showpop("Please try after some times");
                                nodatalayout();
                                // Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                opendatalayout();
                                try {

                                    List<SearchModel> item = new Gson().fromJson(response.toString(), new TypeToken<List<SearchModel>>() {
                                    }.getType());
                                    // adding contacts to contacts list
//                                    models.clear();
//
//                                    searchAdapter.notifyDataSetChanged();


                                    models = new ArrayList<>();
                                    models.addAll(item);
                                    searchAdapter=new SearchAdapter(getContext(), models);
                                    lv_list.setLayoutManager(new LinearLayoutManager(getContext()));

                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    lv_list.setLayoutManager(mLayoutManager);
                                    lv_list.setItemAnimator(new DefaultItemAnimator());
                                    lv_list.addItemDecoration(new MyDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, 36));
                                    lv_list.setAdapter(searchAdapter);

                                    ll_search.setVisibility(View.GONE);
                                    tv_search.setVisibility(View.VISIBLE);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            progressBar.setVisibility(View.GONE);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);

                    nodatalayout();
                }
            });

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(request);
        } else {


            showpop("No Internet Connection");

        }
    }


    public void  getresult(String PartnerId){


        if ( CheckConnectivity.getInstance(getContext()).isOnline()) {

            progressBar.setVisibility(View.VISIBLE);
            String objid=et_ticketno.getText().toString();
            String url= AllUrl.baseUrl+"/ServiceOrder/getServiceOrder?TechCode="+PartnerId+"&ObjectId="+objid+"";
            System.out.println("url-->  "+ url);
            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            System.out.println(response);
                            if (response == null) {
                                showpop("Please try after some times");
                                nodatalayout();
                                // Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {

                                opendatalayout();
                                try {

                                    List<SearchModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<SearchModel>>() {
                                    }.getType());
//
//                                    models.clear();
//                                    models.addAll(items);
//                                    // refreshing recycler view
//                                    searchAdapter.notifyDataSetChanged();

                                    models = new ArrayList<>();
                                    models.addAll(items);
                                    searchAdapter=new SearchAdapter(getContext(), models);
                                    lv_list.setLayoutManager(new LinearLayoutManager(getContext()));

                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                    lv_list.setLayoutManager(mLayoutManager);
                                    lv_list.setItemAnimator(new DefaultItemAnimator());
                                    lv_list.addItemDecoration(new MyDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, 36));
                                    lv_list.setAdapter(searchAdapter);

                                    ll_search.setVisibility(View.GONE);
                                    tv_search.setVisibility(View.VISIBLE);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            progressBar.setVisibility(View.GONE);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    nodatalayout();

                }
            });

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(request);


        } else {

          //  Toast.makeText(getContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

            showpop("No internet connection");

        }
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


    public void nodatalayout(){
        lv_list.setVisibility(View.GONE);
        ll_nodata.setVisibility(View.VISIBLE);
    }

    public void opendatalayout(){

        ll_nodata.setVisibility(View.GONE);
        lv_list.setVisibility(View.VISIBLE);

    }

}
