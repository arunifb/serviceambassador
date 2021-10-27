package technician.ifb.com.ifptecnician.negative_response.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Essential_add_adapter;
import technician.ifb.com.ifptecnician.adapter.Pending_item_adapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.Essential_add_model;
import technician.ifb.com.ifptecnician.model.PendingSpare;
import technician.ifb.com.ifptecnician.negative_response.fragment.adapter.NrSapreAdapter;
import technician.ifb.com.ifptecnician.negative_response.fragment.model.NrSpareModel;


public class NrSpareFragment extends Fragment {


    SharedPreferences sharedPreferences;
    ListView nr_spe_lv_pending_item;

    String TicketNo;

    public NrSpareModel pendingSpare;
    public ArrayList<NrSpareModel> pendingSpares=new ArrayList<NrSpareModel>();


    public NrSpareFragment() {
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
        View view= inflater.inflate(R.layout.fragment_nr_spare, container, false);
        nr_spe_lv_pending_item=view.findViewById(R.id.nr_spe_lv_pending_item);


        updatevalue();

        return view;
    }

    public void updatevalue(){

        try{
            sharedPreferences = getActivity().getSharedPreferences("details", 0);
            String details = sharedPreferences.getString("details", "");

            System.out.println("details"+details);

            JSONObject object = new JSONObject(details);

             TicketNo = object.getString("TicketNo");
            getpendingdata(TicketNo);

        }

        catch (Exception e){

            e.printStackTrace();
        }
    }

    public void getpendingdata(String TicketNo){

        if ( CheckConnectivity.getInstance(getActivity()).isOnline()) {

//            showProgressDialog();
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            //  String url= AllUrl.baseUrl+"spares/getspare?spareaccadd.Ticketno=2002374814&spareaccadd.Status=Pending&spareaccadd.ItemType=ZACC";
            String url= AllUrl.baseUrl+"spares/getspare?spareaccadd.Ticketno="+TicketNo;
              System.out.println("get pending spare -->  "+ url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                               System.out.println("pendimg spare details-->"+response);

                            try {

                                JSONObject object=new JSONObject(response);

                                String status=object.getString("Status");
                                if (status.equals("true")){
                                    // JSONArray jsonArray=new JSONArray(response);
                                    JSONArray jsonArray = object.getJSONArray("Data");
                                    //  System.out.println(jsonArray.toString());

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject essobj = jsonArray.getJSONObject(i);
                                        pendingSpare=new NrSpareModel();

                                        if (essobj.getString("ItemType").equals("ZSPP"))

                                        {

                                            pendingSpare.setItemType(essobj.getString("ItemType"));
                                            pendingSpare.setStatus(essobj.getString("Status"));
                                            pendingSpare.setItemno(essobj.getString("itemno"));
                                            pendingSpare.setETA(essobj.getString("ETA"));
                                            pendingSpare.setPartCode(essobj.getString("PartCode"));
                                            pendingSpare.setPartName(essobj.getString("PartName"));
                                            pendingSpare.setQty(essobj.getString("Qty"));
                                            pendingSpare.setFlags("U");
                                            String penflag=essobj.getString("pending_fla");
                                            if (penflag.equals("N") ){
                                                pendingSpare.setPending_fla("No");
                                            }
                                            else if(penflag.equals("X")){

                                                pendingSpare.setPending_fla("Yes");
                                            }
                                            else if (penflag.equals("")){

                                                pendingSpare.setPending_fla("No");
                                            }

                                            pendingSpares.add(pendingSpare);

                                        }

                                        else {

                                        }
                                    }



                                    NrSapreAdapter pending_item_adapter=new NrSapreAdapter(pendingSpares,getActivity());
                                    pending_item_adapter.notifyDataSetChanged();
                                    nr_spe_lv_pending_item.setAdapter(pending_item_adapter);


//                                    add_item_adapter = new Add_item_adapter(models, CustomerDetailsActivity.this);
//                                    add_item_adapter.notifyDataSetChanged();
//                                    lv_additem.setAdapter(add_item_adapter);
                                }

                                else{

                                }
                            } catch (Exception e) {
                                e.printStackTrace();


                            }

//                            hideProgressDialog();
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        } else {

            Toast.makeText(getActivity(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
        }

    }



    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
