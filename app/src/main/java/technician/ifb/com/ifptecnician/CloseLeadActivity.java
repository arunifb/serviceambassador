package technician.ifb.com.ifptecnician;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.adapter.Essential_add_adapter;
import technician.ifb.com.ifptecnician.adapter.Lead_item_add_adapter;
import technician.ifb.com.ifptecnician.alert.Alert;
import technician.ifb.com.ifptecnician.allurl.AllUrl;

import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.Essential_add_model;
import technician.ifb.com.ifptecnician.model.Lead_item_add_model;
import technician.ifb.com.ifptecnician.model.Lead_model;
import technician.ifb.com.ifptecnician.model.PendingSpare;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.NumberPickerDialog;

public class CloseLeadActivity extends AppCompatActivity implements
        View.OnClickListener,
        NumberPicker.OnValueChangeListener,
        AdapterView.OnItemSelectedListener{


    private static final String TAG = CloseLeadActivity.class.getSimpleName();

    String lead_data;

    Dbhelper dbhelper;

    //view details
    TextView tv_calltype,tv_custname,tv_callbookdate,tv_servicetype,tv_ticketno,tv_rcnno,tv_pending_status,tv_address;
    ImageView iv_call;

    //inner layout
    LinearLayout ll_customer_details,ll_essential_details,ll_status_details;
    //ees add section
    List<String> essspinerlist;
    Map<String, String> map_ess_code;
    SearchableSpinner ess_spiner;
    String accessories_stock,additives_stock;
    TextView tv_ess_sqty;
    int accstock=0,addstock=0,ess_stock=0;
    LinearLayout ll_add_ess, ll_scan_ess;
    String essComponentDescription, essComponent,ItemType;
    Cursor ess_cursor;
    ListView lv_ess_add_item;
    TextView tv_ess_component;
    Lead_item_add_model lead_item_add_model;
    public ArrayList<Lead_item_add_model> lead_item_add_models = new ArrayList<>();
    Lead_item_add_adapter lead_item_add_adapter;
    String quentity = "1";
    ImageView lead_close_iv_call;
    TextView tv_ess_quentity;
    Alert alert=new Alert();
    //Tab name
    LinearLayout ll_customer,ll_essential,ll_status;

    TextView lead_close_ess_name;

    LinearLayout  ll_spare, ll_sales, ll_machine;
    ImageView iv_info, iv_part, iv_ess, iv_note, iv_machine, iv_status;
    TextView t_cus, t_mach, t_spare, t_essen, t_status,lead_close_tv_callbookdate;
    String leadno;
    SessionManager sessionManager;
    String PartnerId,Frcode;

    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    String tss = dfs.format(c);

    String RCNNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_lead);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("Lead Update");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        try{

            leadno=getIntent().getExtras().getString("leadno");
        }catch (Exception e){

            e.printStackTrace();
        }


        dbhelper=new Dbhelper(getApplicationContext());

        sessionManager=new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);
        Frcode=user.get(SessionManager.KEY_FrCode);

//        dbhelper=new Dbhelper(getApplicationContext());
//        try{
//
//            lead_data=getIntent().getExtras().getString("leaddata");
//
//            System.out.println(TAG+"lead_single_data-->"+lead_data);
//
//        }
//        catch (Exception ex){
//
//            ex.printStackTrace();
//        }
//

        init();

    }

    public void init(){
        tv_calltype=findViewById(R.id.lead_close_tv_calltype);
        tv_custname=findViewById(R.id.lead_close_tv_custname);
        tv_callbookdate=findViewById(R.id.lead_close_tv_callbookdate);
        tv_ticketno=findViewById(R.id.lead_close_tv_ticketno);
        tv_rcnno=findViewById(R.id.lead_close_tv_rcnno);
        ll_add_ess = findViewById(R.id.ll_add_ess);
        ll_add_ess.setOnClickListener(this);
        lv_ess_add_item=findViewById(R.id.lv_ess_add_item);
        ess_spiner = (SearchableSpinner) findViewById(R.id.ess_spinner);
        lead_close_iv_call=findViewById(R.id.lead_close_iv_call);
        lead_close_iv_call.setOnClickListener(this);
        ll_scan_ess = findViewById(R.id.ll_scan_ess);
        ll_scan_ess.setOnClickListener(this);
        tv_ess_component = findViewById(R.id.tv_ess_component);
        tv_ess_sqty=findViewById(R.id.tv_ess_sqty);
        tv_ess_quentity=findViewById(R.id.tv_ess_quentity);
        tv_ess_quentity.setOnClickListener(this);
        lead_close_ess_name=findViewById(R.id.lead_close_ess_name);


        getess();

        getleaddetails(PartnerId,leadno,Frcode);

    }

    public void getleaddetails(String PartnerId,String leadno,String Frcode){


            if (CheckConnectivity.getInstance(CloseLeadActivity.this).isOnline()) {

                String  URL = AllUrl.baseUrl + "leads/getLead?model.lead.TechCode=" + PartnerId + "&model.lead.LeadNo="+leadno+"&model.lead.Frcode="+Frcode;

                System.out.println(URL);

                showProgressDialog();
                JsonObjectRequest req = new JsonObjectRequest(URL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                hideProgressDialog();

                                try {

                                    if (response == null) {
                                        Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    else {

                                        try{
                                            JSONArray jsonArray = response.getJSONArray("Data");

                                            for (int i = 0; i < 1; i++) {

                                            JSONObject leadobj = jsonArray.getJSONObject(i);

                                             tv_ticketno.setText(leadobj.getString("LeadNo"));
                                             tv_calltype.setText("L");
                                             tv_custname.setText(leadobj.getString("Customer"));
                                             tv_rcnno.setText(leadobj.getString("Phone"));
                                             RCNNo=leadobj.getString("Phone");
                                             lead_close_ess_name.setText(leadobj.getString("EssName"));
                                             tv_callbookdate.setText(Parsedate(leadobj.getString("StartDate")));

                                            }

                                        }catch (Exception e){

                                        }

                                    }

                                    System.out.println(response);


                                   String status=response.getString("Status");

                                   if (status.equals(false)){

                                       alert.showDialog(CloseLeadActivity.this,"Alert !! ","No Data found");
                                   }

                                   else{

                                   }

                                    VolleyLog.v("Response:%n %s", response.toString(4));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        hideProgressDialog();
                    }
                });

// add the request object to the queue to be executed
                MyApplication.getInstance().addToRequestQueue(req);
            }
            else {
            }
    }

//    public void updatevalue(){
//
//        try{
//
//
//            tv_ticketno.setText(object1.getString("TicketNo"));
//            tv_calltype.setText("L");
//            tv_custname.setText(object1.getString("CustomerName"));
//            tv_rcnno.setText(object1.getString("RCNNo"));
////            CallType = object1.getString("CallType");
////            Street=object1.getString("Street");
////            City=object1.getString("City");
////            State=object1.getString("State");
////            DOP=Parsedate(object1.getString("DOP"));
////            DOI=Parsedate(object1.getString("DOI"));
////            CustomerCode=object1.getString("CustomerCode");
////            FGProducts=object1.getString("FGCode");
////            Product=object1.getString("Product");
////            ChangeDate=object1.getString("ChangeDate");
//
//            getess();
//
//        }catch(Exception e){
//
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ll_add_ess:
                add_ess();
                break;
            case R.id.tv_ess_quentity:
                showNumberPicker(v);
                break;
            case R.id.ll_scan_ess:
                scan_bar_code();
                break;
            case R.id.lead_close_iv_call:
                calltocustomer();
                break;
        }
    }

    public void calltocustomer(){

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + RCNNo));// Initiates the Intent
        startActivity(intent);
    }

    public void add_ess(){

        int eqty=string_to_int(quentity);

        if (!quentity.equals("0")  ){

            lead_item_add_model = new Lead_item_add_model();

            Boolean status=checkesscode(essComponentDescription);

            if (status){
                Toast.makeText(CloseLeadActivity.this, "Already Exists", Toast.LENGTH_SHORT).show();
            }
            else{

                if (eqty <= ess_stock){
                    lead_item_add_model.setEname(essComponentDescription);
                    lead_item_add_model.setEcode(essComponent);
                    lead_item_add_model.setEquentity(quentity);
                    lead_item_add_model.setItemtype(ItemType);
                    lead_item_add_model.setFlag("I");
                    lead_item_add_model.setSqty(ess_stock);
                    lead_item_add_models.add(lead_item_add_model);

                    lead_item_add_adapter = new Lead_item_add_adapter(lead_item_add_models, CloseLeadActivity.this);
                    lead_item_add_adapter.notifyDataSetChanged();
                    lv_ess_add_item.setAdapter(lead_item_add_adapter);
                }

                else {

                    Toast.makeText(CloseLeadActivity.this, "Stock Mismatch", Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            Toast.makeText(CloseLeadActivity.this, "Please select quantity", Toast.LENGTH_SHORT).show();
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
                            //  System.out.println("recordid-->" + recordid);
                            String ComponentDescription = ess_cursor.getString(ess_cursor.getColumnIndex("ComponentDescription"));
                            String Component = ess_cursor.getString(ess_cursor.getColumnIndex("Component"));
                            essspinerlist.add(ComponentDescription);
                            map_ess_code.put(ComponentDescription, Component);
                            //   System.out.println(ComponentDescription + "    " + Component);

                        } while (ess_cursor.moveToNext());

                        ArrayAdapter<String> essAdapter = new ArrayAdapter<>(CloseLeadActivity.this, android.R.layout.simple_spinner_dropdown_item, essspinerlist);
                        ess_spiner.setAdapter(essAdapter);
                        ess_spiner.setSelection(0,false);
                        ess_spiner.setOnItemSelectedListener(CloseLeadActivity.this);
                    }
                } else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();

             }
    }

    public void  get_ess_name(String component){

        try {

            Cursor ess_name_cursor;

            ess_name_cursor = dbhelper.get_ess_Name(component);

            if (ess_cursor != null) {

                if (ess_cursor.moveToFirst()) {

                    for(int i=0;i<1;i++) {
                        final int recordid = ess_name_cursor.getInt(ess_name_cursor.getColumnIndex("recordid"));
                        // System.out.println("recordid-->" + recordid);
                        String ComponentDescription = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("ComponentDescription"));
                        essComponent = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("Component"));
                        ItemType = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("ItemType"));
                        //   System.out.println(ComponentDescription + "    " +essComponent + "  "+ItemType);
                        tv_ess_component.setText(essComponent);
                        essComponentDescription = ComponentDescription;
                        accessories_stock = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("accessories_stock"));
                        additives_stock = ess_name_cursor.getString(ess_name_cursor.getColumnIndex("additives_stock"));

                        accstock = string_to_int(accessories_stock);
                        addstock = string_to_int(additives_stock);

                        if (accstock < addstock) {
                            tv_ess_sqty.setText(additives_stock);
                            ess_stock = addstock;
                        } else {
                            ess_stock = accstock;
                            tv_ess_sqty.setText(accessories_stock);
                        }

//                        int position = -1;
//                        position =essspinerlist.indexOf(ComponentDescription);
//
//                        ess_spiner.setSelection(position);


                        if (ess_stock != 0) {

                            lead_item_add_model=new Lead_item_add_model();
                            lead_item_add_model.setEname(essComponentDescription);
                            lead_item_add_model.setEcode(essComponent);
                            lead_item_add_model.setEquentity("1");
                            lead_item_add_model.setItemtype(ItemType);
                            lead_item_add_model.setFlag("I");
                            lead_item_add_model.setSqty(ess_stock);
                            lead_item_add_models.add(lead_item_add_model);

                            lead_item_add_adapter = new Lead_item_add_adapter(lead_item_add_models, CloseLeadActivity.this);
                            lead_item_add_adapter.notifyDataSetChanged();
                            lv_ess_add_item.setAdapter(lead_item_add_adapter);

                        } else {
                            alert.showDialog(CloseLeadActivity.this, "Alert !!!", "No Stock Found");
                        }

                    }


                }
            } else {

            }


        } catch (Exception e) {
            e.printStackTrace();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            String tss = dfs.format(c);
             }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

         if (parent.getId() == R.id.ess_spinner) {

            essComponentDescription = parent.getItemAtPosition(position).toString();

                 getdetails_by_name(essComponentDescription);

         }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int string_to_int(String Numbers){
        int no=0;
        no= Integer.parseInt(Numbers);

        return no;
    }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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

    public boolean checkesscode(String essname){

        long status=0;


        for(int i=0;i< lead_item_add_models.size();i++){
            if ( lead_item_add_models.get(i).getEname().equals(essname)){

                status = 1;
                break;
            }
        }
        if (status > 0){

            return true;
        }
        return false;
    }

    public void showNumberPicker(View view) {
        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setValueChangeListener(CloseLeadActivity.this);
        newFragment.show(getSupportFragmentManager(), "time picker");

    }


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        quentity = String.valueOf(picker.getValue());
        tv_ess_quentity.setText(quentity);

    }

  public void scan_bar_code(){

//      Intent i = new Intent(CloseLeadActivity.this, FullScannerActivity.class);
//      startActivityForResult(i, 100);
  }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {

            if (resultCode == RESULT_OK) {

                    if(dbhelper.IS_ess_Component_exits(data.getStringExtra("serialno"))){

                        get_ess_name(data.getStringExtra("serialno"));
                        tv_ess_component.setText(data.getStringExtra("serialno"));

                    }

                    else{

                        Alert alert=new Alert();
                        alert.showDialog(CloseLeadActivity.this,"Alert","Essential Not Found");

                }
            }

        }


    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getdetails_by_name(String name){

        Cursor cursor;

        try {

            cursor = dbhelper.getesstype(name);

            if (cursor != null) {

                if (cursor.moveToFirst()) {
                    {
                          do {
                              final int recordid = cursor.getInt(cursor.getColumnIndex("recordid"));
                              //   System.out.println("recordid-->" + recordid);
                              String ComponentDescription = cursor.getString(cursor.getColumnIndex("ComponentDescription"));
                              essComponent = cursor.getString(cursor.getColumnIndex("Component"));
                              ItemType = cursor.getString(cursor.getColumnIndex("ItemType"));
                              //   System.out.println(ComponentDescription + "    " +essComponent + "  "+ItemType);
                              tv_ess_component.setText(essComponent);

                              accessories_stock = cursor.getString(cursor.getColumnIndex("accessories_stock"));
                              additives_stock = cursor.getString(cursor.getColumnIndex("additives_stock"));

                              accstock = string_to_int(accessories_stock);
                              addstock = string_to_int(additives_stock);

                              if (accstock < addstock) {
                                  tv_ess_sqty.setText(additives_stock);
                                  ess_stock = addstock;
                              } else {
                                  ess_stock = accstock;
                                  tv_ess_sqty.setText(accessories_stock);
                              }

                          }while (cursor.moveToNext());
                    }

                }

            }
        } catch (Exception e){

            e.printStackTrace();
        }


    }


    public String Parsedate(String time){
        //  "9\/2\/2019 7:03:05 PM"
        String inputPattern = "dd/MM/yyyy hh:mm:ss a";
        String outputPattern = "dd-MMM";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
            // System.out.println(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }

}
