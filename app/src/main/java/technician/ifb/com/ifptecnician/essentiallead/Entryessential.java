package technician.ifb.com.ifptecnician.essentiallead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Essential_add_adapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.Essential_add_model;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Entryessential extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    SharedPreferences prefdetails;
    String ticketdetails;


    TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_scfeedbackcatvalue,
            tv_rcnno,tv_servicetype,tv_custname,tv_ageing,tv_escaltion,tv_problemdescription,tv_alt;
    ImageView iv_call,iv_rcn,iv_alt,iv_thum,iv_calender;
    LinearLayout ll_alt,item_task_ll_medium;
    CardView task_cv;

    List<String> essspinerlist;
    Map<String, String> map_ess_code;
    SearchableSpinner ess_spiner;
    String accessories_stock,additives_stock;
    TextView tv_ess_sqty;
    int accstock=0,addstock=0,ess_stock=0;
    String FrCode,FGProducts,PartnerId,mobile_no,name;
    Cursor bom_cursor, ess_cursor;
    SessionManager sessionManager;
    Dbhelper dbhelper;
    String essComponentDescription, essComponent,ItemType;
    String shelflife="",essuses="1/day";

    TextView tv_fgproduct,ess_tv_shelflife;
    String quentity = "1";
    TextView tv_ess_component, tv_ess_quentity;
    public ArrayList<Entry_ess_model> essential_add_models = new ArrayList<>();
    public Entry_ess_model essential_add_model;
    Item_add_adapter essential_add_adapter;
    ListView lv_ess_add_item;
    LinearLayout ll_add_ess, ll_scan_ess;
    LinearLayout ll_ess_cal;
    TextView tv_ess_calculation;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entryessential);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Essential Entry");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       dbhelper=new Dbhelper(getApplicationContext());

       sessionManager=new SessionManager(getApplicationContext());
        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            FrCode=user.get(SessionManager.KEY_FrCode);

            System.out.println("FrCode-->>>> "+FrCode);
            mobile_no=user.get(SessionManager.KEY_mobileno);
            name=user.get(SessionManager.KEY_Name);
        }
        catch (Exception e){

            e.printStackTrace();
        }
        init();




    }


    public void init(){

        tv_calltype=findViewById(R.id.tv_calltype);
        tv_ticketno=findViewById(R.id.tv_ticketno);
        tv_address=findViewById(R.id.tv_address);
        tv_callbook=findViewById(R.id.tv_callbookdate);
        tv_status=findViewById(R.id.tv_status);
        tv_rcnno=findViewById(R.id.tv_rcnno);
        tv_servicetype=findViewById(R.id.tv_servicetype);
        tv_custname=findViewById(R.id.tv_custname);
        iv_call=findViewById(R.id.iv_call);
        task_cv=findViewById(R.id.task_cv);
        tv_ageing=findViewById(R.id.tv_ageing);
        tv_escaltion=findViewById(R.id.tv_escaltion);
        tv_problemdescription=findViewById(R.id.tv_problemdescription);
        iv_alt=findViewById(R.id.alt_iv_message);
        tv_alt=findViewById(R.id.tv_altno);
        iv_rcn=findViewById(R.id.rcn_iv_message);
        ll_alt=findViewById(R.id.ll_altview);
        iv_thum=findViewById(R.id.iv_thum);
        tv_scfeedbackcatvalue=findViewById(R.id.tv_scfeedbackcatvalue);
        item_task_ll_medium=findViewById(R.id.item_task_ll_medium);
        iv_calender=findViewById(R.id.iv_calender);

        ess_spiner = findViewById(R.id.ess_spinner);
        tv_fgproduct=findViewById(R.id.tv_fgproduct);


        tv_ess_calculation=findViewById(R.id.tv_ess_calculation);
        ll_ess_cal=findViewById(R.id.ll_ess_cal);
        ll_ess_cal.setVisibility(View.GONE);
        tv_ess_component = findViewById(R.id.tv_ess_component);
        tv_ess_sqty=findViewById(R.id.tv_ess_sqty);
        ess_tv_shelflife=findViewById(R.id.ess_tv_shelflife);
        lv_ess_add_item = findViewById(R.id.lv_ess_add_item);
        ll_add_ess = findViewById(R.id.ll_add_ess);
        ll_add_ess.setOnClickListener(this);
        updatetextvalue();
    }


    public void  updatetextvalue(){


        try{
        prefdetails = getSharedPreferences("essential_details", 0);
        ticketdetails = prefdetails.getString("essential_details", "");

            JSONObject object1=new JSONObject(ticketdetails);

            tv_ticketno.setText(object1.getString("TicketNo"));

            tv_address.setText(object1.getString("Address"));

            tv_calltype.setText(object1.getString("CallType").substring(0,1));
            tv_rcnno.setText(object1.getString("RCNNo"));
            tv_custname.setText(object1.getString("CustomerName"));


            getess();

        }catch (Exception e){
            e.printStackTrace();
        }



    }


    public void getess() {

        try {

            essspinerlist = new ArrayList<>();
            map_ess_code = new HashMap<>();
            if (dbhelper.isessdataEmpty()) {

                ess_cursor = dbhelper.getallessdata();

                if (ess_cursor != null) {

                    if (ess_cursor.moveToFirst()) {

                        do {

                            final int recordid = ess_cursor.getInt(ess_cursor.getColumnIndex("recordid"));

                            String ComponentDescription = ess_cursor.getString(ess_cursor.getColumnIndex("ComponentDescription"));
                            String Component = ess_cursor.getString(ess_cursor.getColumnIndex("Component"));
                            essspinerlist.add(ComponentDescription);
                            map_ess_code.put(ComponentDescription, Component);
                            //   System.out.println(ComponentDescription + "    " + Component);

                        } while (ess_cursor.moveToNext());

                        ess_cursor.close();

                        ArrayAdapter<String> essAdapter = new ArrayAdapter<>(Entryessential.this, android.R.layout.simple_spinner_dropdown_item, essspinerlist);
                        ess_spiner.setAdapter(essAdapter);
                        // ess_spiner.setSelection(0,false);
                        ess_spiner.setOnItemSelectedListener(Entryessential.this);
                    }
                } else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            if (parent.getId() == R.id.ess_spinner) {

            essComponentDescription = parent.getItemAtPosition(position).toString();
            Cursor cursor;

            try{

                cursor=dbhelper.getesstype(essComponentDescription);

                if (cursor != null) {

                    if (cursor.moveToFirst()) {

                        do {

                            final int recordid = cursor.getInt(cursor.getColumnIndex("recordid"));
                            //   System.out.println("recordid-->" + recordid);
                            String ComponentDescription = cursor.getString(cursor.getColumnIndex("ComponentDescription"));
                            essComponent  =cursor.getString(cursor.getColumnIndex("Component"));
                            ItemType=cursor.getString(cursor.getColumnIndex("ItemType"));
                            //   System.out.println(ComponentDescription + "    " +essComponent + "  "+ItemType);

                            shelflife=cursor.getString(cursor.getColumnIndex("ShelfLife"));
                            ess_tv_shelflife.setText(shelflife);


                            System.out.println("shelflife-->"+shelflife);
                            tv_ess_component.setText(essComponent);

                            accessories_stock = cursor.getString(cursor.getColumnIndex("accessories_stock"));
                            additives_stock =   cursor.getString(cursor.getColumnIndex("additives_stock"));

                            accstock=string_to_int(accessories_stock);
                            addstock=string_to_int(additives_stock);

                            if (accstock < addstock){
                                tv_ess_sqty.setText(additives_stock);
                                ess_stock=addstock;
                            }
                            else {
                                ess_stock=accstock;
                                tv_ess_sqty.setText(accessories_stock);
                            }

                            getcal();
                        } while (cursor.moveToNext());
                        cursor.close();

                    }
                } else {

                }
            }catch (Exception e){

                e.printStackTrace();
            }


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void updateessential(View view){


        if (CheckConnectivity.getInstance(this).isOnline()) {

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = AllUrl.baseUrl + "addacc?addacc.FrCode=" + FrCode;

            System.out.println("get all sales-->  " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                          //  progressBar.setVisibility(View.GONE);

                            System.out.println("ess details-->" + response);
                            if (response == null){

                            }
                            else {

                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    System.out.println(jsonArray.toString());

                                    dbhelper.deleteess();

                                    dbhelper.insert_ess_data("-- select essential --", "", "" , "0", "0", "", "");

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject essobj = jsonArray.getJSONObject(i);

                                        if (Boolean.valueOf(dbhelper.insert_ess_data(essobj.getString("ComponentDescription"), essobj.getString("Component"), "Z" + essobj.getString("ItemType"), essobj.getString("accessories_stock"), essobj.getString("additives_stock"), essobj.getString("ShelfLife"), "")).booleanValue()) {

                                        }
                                    }
                                    getess();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        } else {

            //  ll_nointernet.setVisibility(View.VISIBLE);
           // showerror("Please check internet connection");
        }
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.ll_add_ess:

                add_ess();
                break;
        }
    }


    public void add_ess(){


        int eqty=string_to_int(quentity);

        if (!quentity.equals("0")  ){

            essential_add_model = new Entry_ess_model();
            Boolean status=checkesscode(essComponentDescription);

            if (status){
               // showerror("Essential Already Exists");
            }
            else{

                if (eqty <= ess_stock){
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    essential_add_model.setEname(essComponentDescription);
                    essential_add_model.setEcode(essComponent);
                    essential_add_model.setEquentity(quentity);
                    essential_add_model.setItemtype(ItemType);
                    essential_add_model.setFlag("I");
                    essential_add_model.setDate(currentDateandTime);
                    essential_add_model.setPrice("\u20B9 "+Integer.parseInt(quentity)*100);
                    essential_add_model.setDis_amount("- \u20B9 "+5*Integer.parseInt(quentity));
                    essential_add_model.setDis_per("5%");
                    essential_add_model.setGross("\u20B9 "+95*Integer.parseInt(quentity));
                    essential_add_models.add(essential_add_model);

                }

                else {

                    if (essComponentDescription.equals("-- select essential --")){
                      //  showerror("Please select essential");

                    }
                    else {
                      //  showerror("Stock Mismatch");
                    }


                }
            }
//                    }


            for (int e=0;e < essential_add_models.size();e++){

                String value=essential_add_models.get(e).getFlag();
                //    System.out.println(value);
            }


            essential_add_adapter = new Item_add_adapter(essential_add_models, Entryessential.this);
            essential_add_adapter.notifyDataSetChanged();
            lv_ess_add_item.setAdapter(essential_add_adapter);

        } else {

          //  showerror("Please Select quantity");
        }

    }


    public int string_to_int(String Numbers){
        int no=0;
        no= Integer.parseInt(Numbers);

        return no;
    }

    public void getcal(){

        if (!shelflife.equals("") && !shelflife.equals("0") && !shelflife.equals("As per usage/Standard")){
            ll_ess_cal.setVisibility(View.VISIBLE);
            String enddate= Valuefilter.Enddate(shelflife,essuses,quentity);
            tv_ess_calculation.setText(enddate);
        }
        else {
            ll_ess_cal.setVisibility(View.GONE);
        }

    }


    public boolean checkesscode(String essname){

        long status=0;

        for(int i=0;i< essential_add_models.size();i++){
            if ( essential_add_models.get(i).getEname().equals(essname)){

                status = 1;
                break;
            }
        }
        if (status > 0){

            return true;
        }
        return false;
    }


    public void payment(View view){

        startActivity(new Intent(Entryessential.this,EssentialPayment.class));
    }

}