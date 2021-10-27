package technician.ifb.com.ifptecnician;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import technician.ifb.com.ifptecnician.adapter.EssentialListAdapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.essentialdetails.EssentialItemClick;
import technician.ifb.com.ifptecnician.essentiallead.EssentialLeadList;
import technician.ifb.com.ifptecnician.essentiallead.essentialmodel.EssentialList;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class EssentialListActivity extends AppCompatActivity implements EssentialItemClick {

    private static final String TAG = EssentialLeadList.class.getSimpleName();
    ProgressBar progressBar;
    String essentialurl="",PartnerId,Frcode,lead_category;
    SessionManager sessionManager;
    List<EssentialList> essentialLists = new ArrayList<>();
    RecyclerView rv_essentials;
    EssentialListAdapter essentialListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essentiallist);

        progressBar=findViewById(R.id.taskProcessing);
        rv_essentials=findViewById(R.id.rv_essentials);

        if (CheckConnectivity.getInstance(this).isOnline()){

            sessionManager=new SessionManager(getApplicationContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            Frcode = user.get(SessionManager.KEY_FrCode);
            lead_category = "ESS";

           getEssentialLeads(PartnerId,lead_category,Frcode);
        }
        else {

            Toast.makeText(this,"Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }


    private void getEssentialLeads( String partnerId,String lead_category, String Frcode) {



        essentialurl = AllUrl.baseUrl + "sa/leads/search?lead_category=" + lead_category + "&Frcode=" + Frcode + "&Contactno";

        // showProgressDialog();
        System.out.println(essentialurl);

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,essentialurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);

                        progressBar.setVisibility(View.GONE);


                        try{

                            JSONObject object=new JSONObject(response);

                            String status=object.getString("Status");

                            if (status.equals("true")){


                                String data=object.getString("Data");
                                JSONArray array=new JSONArray(data);

                                for (int i=0;i<array.length();i++){

                                    JSONObject jsonObject=array.getJSONObject(i);

                                    EssentialList essential = new EssentialList();

                                    essential.setSoldToPartyList(jsonObject.optString("sold_to_party_list"));
                                    essential.setSoldToParty(jsonObject.optString("sold_to_party"));
                                    essential.setCallerNo(jsonObject.optString("caller_no"));
                                    essential.setProcessTypeTxt(jsonObject.optString("process_type_txt"));
                                    if (!jsonObject.optString("tel_no").equals("")){
                                        essential.setTelNo(jsonObject.optString("tel_no"));

                                    }
                                    essential.setConcatstatuser(jsonObject.optString("concatstatuser"));
                                    essential.seteMail(jsonObject.optString("e_mail"));
                                    essential.setProcessTypeTxt(jsonObject.optString("process_type_txt"));
                                    essential.setLeadCategory(jsonObject.optString("lead_category"));
                                    essential.setZzmatGrp(jsonObject.optString("zzmat_grp"));
                                    essential.setZzproductDesc(jsonObject.optString("zzproduct_desc"));
                                    essential.setAddress(jsonObject.optString("address"));
                                    essential.setFranchise(jsonObject.optString("franchise"));
                                    essential.setBranch(jsonObject.optString("branch"));

                                    essentialLists.add(essential);

                                }

                                Log.d("essential_list",essentialLists.toString());

                                essentialListAdapter=new EssentialListAdapter(EssentialListActivity.this, essentialLists, EssentialListActivity.this);
                                rv_essentials.setLayoutManager(new LinearLayoutManager(EssentialListActivity.this));

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                rv_essentials.setLayoutManager(mLayoutManager);
                                rv_essentials.setItemAnimator(new DefaultItemAnimator());
                                rv_essentials.addItemDecoration(new MyDividerItemDecoration(EssentialListActivity.this, DividerItemDecoration.VERTICAL, 36));
                                rv_essentials.setAdapter(essentialListAdapter);


                            }

                        }catch (Exception e){

                            e.printStackTrace();
                        }
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





    }


    @Override
    public void OnEssentialItemClick(View v, int position) {


        Intent intent = new Intent(EssentialListActivity.this, EssentialsDetailsActivity.class);

        intent.putExtra("essentials_details", (Serializable) essentialLists.get(position));


        startActivity(intent);



    }
}