package technician.ifb.com.ifptecnician.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.fragment.adapter.SpareAdapter;
import technician.ifb.com.ifptecnician.fragment.dummy.AccessoriesModel;
import technician.ifb.com.ifptecnician.fragment.dummy.SpareModel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;


public class SparesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SessionManager sessionManager;
    String PartnerId;

    String  TechCode,itemtype,Spare,Acc,Add,Ticketno,CallType,product,ProductName,MachineStaus,CallBookDate,ClosedDate;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList<SpareModel> models=new ArrayList<SpareModel>();
    public SpareModel model;
    RecyclerView lv_list;
    SpareAdapter taskadapter;

    public SparesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_spares, container, false);

        sessionManager=new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);
        lv_list=view.findViewById(R.id.lv_spare);
        lv_list.setLayoutManager(new LinearLayoutManager(getContext()));
        taskadapter=new SpareAdapter(getContext());

        getspare(PartnerId);
       // https://sapsecurity.ifbhub.com/api/sales/getsale?TechCode=17226089&itemtype=ZSPP

        return view;
    }


    public void  getspare(String pid){

        if ( CheckConnectivity.getInstance(getContext()).isOnline()) {

            showProgressDialog();
            RequestQueue queue = Volley.newRequestQueue(getContext());
//          String url = AllUrl.loginUrl+"?url=http://centurionbattery.in/development&username="+username+"&password="+password;
            String url= AllUrl.baseUrl+"sales/getsale?TechCode="+pid+"&itemtype=ZSPP";

            System.out.println("get all sales-->  "+ url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("sales details-->"+response);

                            try {

                                JSONArray jsonArray=new JSONArray(response);

                                System.out.println(jsonArray.toString());

                                models=new ArrayList<SpareModel>();

                                for (int i = 0; i < jsonArray.length(); i++) {


                                    System.out.println(jsonArray.length());

                                    JSONObject object1 = jsonArray.getJSONObject(i);

                                    TechCode= object1.getString("TechCode");
                                    itemtype= object1.getString("itemtype");
                                    Spare= object1.getString("Spare");
                                    Acc= object1.getString("Acc");
                                    Add= object1.getString("Add");
                                    Ticketno= object1.getString("Ticketno");
                                    CallType= object1.getString("CallType");
                                    product= object1.getString("product");
                                    ProductName= object1.getString("ProductName");
                                    MachineStaus= object1.getString("MachineStaus");
                                    CallBookDate= object1.getString("CallBookDate");
                                    ClosedDate= object1.getString("ClosedDate");

                                    models.add(new SpareModel(TechCode,itemtype,Spare,Acc,Add,Ticketno,CallType,product,ProductName,MachineStaus,CallBookDate,ClosedDate));
                                }

                                System.out.println(models.size());

                                taskadapter.setListContent(models);
                                //We in turn set the adapter to the RecyclerView
                                lv_list.setAdapter(taskadapter);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            hideProgressDialog();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    hideProgressDialog();
                  //  Toast.makeText(getContext(), "No Record Found ", Toast.LENGTH_SHORT).show();

                }
            });
// Add the request to the RequestQueue.
            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        } else {

            Toast.makeText(getContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
        }
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

}
