package technician.ifb.com.ifptecnician.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.fragment.adapter.NegativeAdapter;

import technician.ifb.com.ifptecnician.fragment.dummy.NegativeModel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NegativeResponseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{


    RecyclerView lv_list;
    public ArrayList<NegativeModel> models;
    public NegativeModel model;
    String result;
    SessionManager sessionManager;
    String PartnerId;
    NegativeAdapter negativeAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar progressBar;
    LinearLayout ll_nointernet;
    LinearLayout ll_nodata;


    public NegativeResponseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_negativeresponse_list, container, false);

        lv_list=view.findViewById(R.id.lv_nega);
        progressBar=view.findViewById(R.id.taskProcessing);
        ll_nointernet=view.findViewById(R.id.ll_nointernet);

        ll_nodata=view.findViewById(R.id.ll_nodata);
        sessionManager=new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);
        System.out.println(PartnerId);
        getdata(PartnerId);


        models = new ArrayList<>();

        negativeAdapter=new NegativeAdapter(getContext(),models);
        lv_list.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        lv_list.setLayoutManager(mLayoutManager);
        lv_list.setItemAnimator(new DefaultItemAnimator());
      //  lv_list.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        lv_list.setAdapter(negativeAdapter);

        // SwipeRefreshLayout
        mSwipeRefreshLayout =  view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        return view;
    }

    public void  getdata(String PartnerId){

//
        if ( CheckConnectivity.getInstance(getContext()).isOnline()) {

            ll_nointernet.setVisibility(View.GONE);
           progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url= AllUrl.baseUrl+"ServiceOrder/getServiceOrder?TechCode="+PartnerId+"&Status=Negative%20Response%20from%20Custome";
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
                                result = response;

                                lv_list.setVisibility(View.VISIBLE);
                                ll_nodata.setVisibility(View.GONE);

                                try {

                                    List<NegativeModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<NegativeModel>>() {
                                    }.getType());

                                    // adding contacts to contacts list
                                    models.clear();
                                    models.addAll(items);
                                    // refreshing recycler view
                                    negativeAdapter.notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            mSwipeRefreshLayout.setRefreshing(false);
                            progressBar.setVisibility(View.GONE);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
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



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }



    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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
    public void onRefresh() {
        getdata(PartnerId);
    }
}
