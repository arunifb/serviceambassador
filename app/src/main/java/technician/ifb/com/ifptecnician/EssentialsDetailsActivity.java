package technician.ifb.com.ifptecnician;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import technician.ifb.com.ifptecnician.adapter.AddMoreEssentialAdapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.essentialdetails.essentialsdetailsmodel.AddMoreItemDeleteCallback;
import technician.ifb.com.ifptecnician.essentialdetails.essentialsdetailsmodel.EssentialItemList;
import technician.ifb.com.ifptecnician.essentiallead.essentialmodel.EssentialList;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class EssentialsDetailsActivity extends AppCompatActivity implements AddMoreItemDeleteCallback {

    TextView tv_custname, tv_callbookdate,tv_ticketno,tv_address,tv_status,txt_franchise,txt_branch,tv_calltype,
             tv_customer_no,txt_zzproduct,tv_zzmat_grpdetails;
    EssentialList essentialModel;
    SearchableSpinner searchablespinner;
    List<String> searchablelist = new ArrayList<String>();
    RecyclerView rc_essentiallist;
    String searchable_spinner_item;
    ImageView add_essentials;
    List<EssentialItemList> addedlist = new ArrayList<>();

    List<EssentialItemList> selectionlist = new ArrayList<>();

    private AddMoreEssentialAdapter addMoreEssentialAdapter;
    Button add_more;
    RelativeLayout add_text_layout,serachable_spinner_layout;

    String url,Frcode;
    SessionManager sessionManager;
    ProgressBar progressBar;
    TextView essential_txt,essential_component_txt,add_essential_txt,essential_count_txt,decrease_essential_txt,txt_proceed;
    ImageView img_select_essential;

    boolean selected = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essentialsdetails);
        tv_custname = findViewById(R.id.tv_custname);
        tv_callbookdate = findViewById(R.id.tv_callbookdate);
        tv_ticketno = findViewById(R.id.tv_ticketno);
        tv_address = findViewById(R.id.tv_address);
        tv_status = findViewById(R.id.tv_status);
        txt_franchise = findViewById(R.id.txt_franchise);
        txt_branch = findViewById(R.id.txt_branch);
        tv_calltype = findViewById(R.id.tv_calltype);
        tv_customer_no = findViewById(R.id.tv_customer_no);
        txt_zzproduct = findViewById(R.id.txt_zzproduct);
        add_more = findViewById(R.id.add_more);
        add_text_layout = findViewById(R.id.add_text_layout);
        serachable_spinner_layout = findViewById(R.id.serachable_spinner_layout);
        tv_zzmat_grpdetails = findViewById(R.id.tv_zzmat_grpdetails);

        essential_txt = findViewById(R.id.essential_txt);
        essential_component_txt = findViewById(R.id.essential_component_txt);
        add_essential_txt = findViewById(R.id.add_essential_txt);
        essential_count_txt = findViewById(R.id.essential_count_txt);
        decrease_essential_txt = findViewById(R.id.decrease_essential_txt);
        img_select_essential = findViewById(R.id.img_select_essential);
        txt_proceed =findViewById(R.id.txt_proceed);



        add_essentials = findViewById(R.id.add_essentials);

        searchablespinner = (SearchableSpinner) findViewById(R.id.search_essential);

        rc_essentiallist = (RecyclerView)findViewById(R.id.rc_essentiallist);

        progressBar=findViewById(R.id.taskProcessing);

        essentialModel= (EssentialList) getIntent().getSerializableExtra("essentials_details");

        tv_custname.setText(essentialModel.getSoldToPartyList());
        tv_ticketno.setText(essentialModel.getSoldToParty());
        tv_address.setText(essentialModel.getAddress());
        tv_status.setText(essentialModel.getConcatstatuser());
        txt_franchise.setText("Franchise: "+essentialModel.getFranchise());
        txt_branch.setText("Branch :"+essentialModel.getBranch());
        tv_calltype.setText(essentialModel.getZzmatGrp());
        tv_customer_no.setText(essentialModel.getCallerNo());
        txt_zzproduct.setText(essentialModel.getZzproductDesc());
        tv_zzmat_grpdetails.setText(essentialModel.getProcessTypeTxt());
        searchablelist.add("---Select Essential---");
        searchablespinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, searchablelist));

        sessionManager=new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        Frcode = user.get(SessionManager.KEY_FrCode);

        getEssentialComponent(Frcode);

        addedlist.clear();

        selectionlist.clear();



       add_essential_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.parseInt(essential_count_txt.getText().toString());

                count++;

                essential_count_txt.setText(String.valueOf(count));



            }
        });


       decrease_essential_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.parseInt(essential_count_txt.getText().toString());

                if (count>1) {
                    count--;

                    essential_count_txt.setText(String.valueOf(count));

                }


            }
        });



     /*   EssentialItemList essentialItemList = null;

        essentialItemList = new EssentialItemList();

        essentialItemList.setComponent(essentialModel.getProductid());
        essentialItemList.setComponentDescription(essentialModel.getZzproductDesc());
        essentialItemList.setSelected(true);

        addedlist.add(essentialItemList);*/

        if (essentialModel.getProductid().length()>0){

            essential_txt.setText(essentialModel.getZzproductDesc());
            essential_component_txt.setText(essentialModel.getProductid());
            img_select_essential.setImageResource(R.drawable.ic_checkcircle);
        }



        img_select_essential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selected){

                    selected = false;

                    img_select_essential.setImageResource(R.drawable.ic_un_check);


                }else{

                    selected = true;
                    img_select_essential.setImageResource(R.drawable.ic_checkcircle);

                }


            }
        });




        if (addedlist.size()>0) {

            addMoreEssentialAdapter = new AddMoreEssentialAdapter(EssentialsDetailsActivity.this, addedlist, this);
            rc_essentiallist.setLayoutManager(new LinearLayoutManager(EssentialsDetailsActivity.this));
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rc_essentiallist.setLayoutManager(mLayoutManager);
            rc_essentiallist.setItemAnimator(new DefaultItemAnimator());
            rc_essentiallist.setAdapter(addMoreEssentialAdapter);

        }




        searchablespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {

                searchable_spinner_item = parent.getItemAtPosition(pos).toString();

                   if (pos>0){

                       addedlist.add(selectionlist.get(pos-1));
                   }



                Log.d("search_item",searchable_spinner_item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {




            }
        });



        add_essentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  addedlist.add(searchable_spinner_item);

                addMoreEssentialAdapter = new AddMoreEssentialAdapter(EssentialsDetailsActivity.this, addedlist, EssentialsDetailsActivity.this);
                rc_essentiallist.setLayoutManager(new LinearLayoutManager(EssentialsDetailsActivity.this));

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rc_essentiallist.setLayoutManager(mLayoutManager);
                rc_essentiallist.setItemAnimator(new DefaultItemAnimator());
                rc_essentiallist.setAdapter(addMoreEssentialAdapter);


            }
        });

        txt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(EssentialsDetailsActivity.this, PaymentSummaryActivity.class);

                startActivity(intent);

            }
        });



    }

    private void getEssentialComponent(String frcode) {

        url = AllUrl.baseUrl + "addacc?addacc.FrCode=" + frcode;



        // showProgressDialog();
        System.out.println(url);

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);

                        progressBar.setVisibility(View.GONE);


                        try{

                            if(response!=null){


                                JSONArray jsonArray=new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {


                                     JSONObject jsonObject = jsonArray.optJSONObject(i);
                                     EssentialItemList essentialItemList = new EssentialItemList();
                                     essentialItemList.setItemType(jsonObject.optString("ItemType"));
                                     essentialItemList.setComponent(jsonObject.optString("Component"));
                                     essentialItemList.setComponentDescription(jsonObject.optString("ComponentDescription"));
                                     essentialItemList.setAccessoriesStock(jsonObject.optString("accessories_stock"));
                                     essentialItemList.setAdditivesStock(jsonObject.optString("additives_stock"));
                                     essentialItemList.setFrCode(jsonObject.optString("FrCode"));
                                     essentialItemList.setShelfLife(jsonObject.optString("ShelfLife"));
                                     essentialItemList.setStockInTransit(jsonObject.optString("stock_in_transit"));

                                    selectionlist.add(essentialItemList);

                                    searchablelist.add(jsonObject.optString("ComponentDescription"));

                                }



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
    public void OnAddMoreItemDelete(View v, int position) {

        addedlist.remove(position);
        addMoreEssentialAdapter.notifyItemRemoved(position);
        addMoreEssentialAdapter.notifyDataSetChanged();



    }
}