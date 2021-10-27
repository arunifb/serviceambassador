package technician.ifb.com.ifptecnician.ecatalog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.exchange.ExchangeMain;
import technician.ifb.com.ifptecnician.fragment.adapter.NegativeAdapter;
import technician.ifb.com.ifptecnician.fragment.dummy.NegativeModel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.troublesehoot.model.ProductModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EcatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EcatalogFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressBar progressBar;
    LinearLayout ll_nointernet;
    LinearLayout ll_nodata;
    RecyclerView lv_list;
    public ArrayList<Ecatalogmodel> models;
    public Ecatalogmodel model;
    Ecatalogadapter ecatalogadapter;


    public EcatalogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EcatalogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EcatalogFragment newInstance(String param1, String param2) {
        EcatalogFragment fragment = new EcatalogFragment();
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
        View view= inflater.inflate(R.layout.fragment_ecatalog, container, false);

      //  lv_list=view.findViewById(R.id.lv_nega);
        progressBar=view.findViewById(R.id.taskProcessing);
        ll_nointernet=view.findViewById(R.id.ll_nointernet);
        lv_list=view.findViewById(R.id.lv_catalog);
        ll_nodata=view.findViewById(R.id.ll_nodata);


        models = new ArrayList<>();

        ecatalogadapter=new Ecatalogadapter(getContext(),models);
        lv_list.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        lv_list.setLayoutManager(mLayoutManager);
        lv_list.setItemAnimator(new DefaultItemAnimator());
        //  lv_list.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        lv_list.setAdapter(ecatalogadapter);
        getcatalog();
        return view;
    }


    public void getcatalog(){


        if ( CheckConnectivity.getInstance(getContext()).isOnline()) {

            ll_nointernet.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url= AllUrl.baseUrl+"Catalogue/eCatalogue/";
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

                                    List<Ecatalogmodel> items = new Gson().fromJson(response.toString(), new TypeToken<List<Ecatalogmodel>>() {
                                    }.getType());

                                    // adding contacts to contacts list
                                    models.clear();
                                    models.addAll(items);
                                    // refreshing recycler view
                                    ecatalogadapter.notifyDataSetChanged();

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

}