package technician.ifb.com.ifptecnician.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.MyApplication;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.fragment.adapter.AppormentAdapter;
import technician.ifb.com.ifptecnician.fragment.dummy.AppormentModel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class AppormentFragment extends Fragment implements AppormentAdapter.JobsAdapterListener, SwipeRefreshLayout.OnRefreshListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private static final String TAG = MainActivity.class.getSimpleName();

    SessionManager sessionManager;
    String PartnerId;
    RecyclerView listView;
   // public ArrayList<AppormentModel> models=new ArrayList<>();

    private List<AppormentModel> tasklist;
    private List<AppormentModel> templist;
    public AppormentModel model;
    AppormentAdapter tasklist_adapter;
    String current_date;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar taskProcessing;
    LinearLayout ll_nodata;


    public AppormentFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AppormentFragment newInstance(int columnCount) {
        AppormentFragment fragment = new AppormentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apporment_list, container, false);

        listView=view.findViewById(R.id.lv_appo);
        taskProcessing=view.findViewById(R.id.taskProcessing);
        tasklist=new ArrayList<>();
        templist=new ArrayList<>();
        ll_nodata=view.findViewById(R.id.ll_nodata);
        tasklist_adapter=new AppormentAdapter(getContext(), tasklist, this::onTaskSelected);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.addItemDecoration(new MyDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, 36));
        listView.setAdapter(tasklist_adapter);

        sessionManager=new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);

        Date c = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        current_date = df.format(c);

        gettodaydata(PartnerId);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }




    private void gettodaydata(String PartnerId) {


        if (CheckConnectivity.getInstance(getActivity()).isOnline()) {

            String url= AllUrl.baseUrl+"ServiceOrder/getServiceOrder?TechCode="+PartnerId+"&scheduledate="+current_date;

            System.out.println( "today"+url);
            taskProcessing.setVisibility(View.VISIBLE);
            JsonArrayRequest request = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {



                            taskProcessing.setVisibility(View.GONE);
                            if (response == null) {
                                listView.setVisibility(View.GONE);
                                ll_nodata.setVisibility(View.VISIBLE);

                            }
                            else {

                                try{

                                    listView.setVisibility(View.VISIBLE);
                                    ll_nodata.setVisibility(View.GONE);
                                    List<AppormentModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<AppormentModel>>() {
                                    }.getType());

                                    // adding contacts to contacts list
                                    tasklist.clear();
                                    templist.clear();
                                    tasklist.addAll(items);
                                    templist.addAll(items);
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
                    taskProcessing.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    // error in getting json
                    listView.setVisibility(View.GONE);
                    ll_nodata.setVisibility(View.VISIBLE);
                }
            });

         //
            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            MyApplication.getInstance().addToRequestQueue(request);

        }
        else{

            Toast.makeText(getContext(), "Data Not found ", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onTaskSelected(AppormentModel AppormentModel) {

    }


    public void filterdata(){


        try {
            int size = tasklist.size() - 1;

            for (int i = size; i >= 0; i--) {

                String data = tasklist.get(i).getStatus().replace(" ", "");

                if (data.equals("Assigned") || data.equals("Pending"))

                {
                    tasklist_adapter.notifyDataSetChanged();
                } else {
                    tasklist.remove(i);
                    tasklist_adapter.notifyDataSetChanged();

                }
            }

            if (tasklist.size() == 0) {

                listView.setVisibility(View.GONE);
                ll_nodata.setVisibility(View.VISIBLE);


            }

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        gettodaydata(PartnerId);
    }
}
