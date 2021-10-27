package technician.ifb.com.ifptecnician.exchange;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.LoginActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.AddProductadapter;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.exchange.Webview.Viewifbpoint;
import technician.ifb.com.ifptecnician.exchange.model.Ifbpointmodel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.Add_Product_Model;
import technician.ifb.com.ifptecnician.troublesehoot.Troublesehooting;
import technician.ifb.com.ifptecnician.troublesehoot.model.ModelsModel;
import technician.ifb.com.ifptecnician.troublesehoot.model.ProblemModel;
import technician.ifb.com.ifptecnician.troublesehoot.model.ProductModel;

public class ExchangeMain extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    Spinner spnr_product,spnr_machine,spnr_ifbpoint;
    ListView lv_product;
    ExchangeProductAdapter exchangeProductAdapter;
    public ExchangeProductModel exchangeProductModel;
    ArrayList<ExchangeProductModel> exchangeProductModels =new ArrayList<ExchangeProductModel>();
    Button exchange_btn_openwebsite,exchange_btn_submit,exchange_btn_ifbpont,exchange_btn_openpdf,ex_btn_add;
    SharedPreferences prefdetails;
    String TicketNo,CallType,Street,City,State,Product, CustomerCode,
           FGProducts,Address,PinCode,TelePhone,Dop,
           SerialNo,RCNNo, CustomerName,MachinStatus;
    ImageView exchange_iv_call;
    TextView exchange_tv_custname,exchange_tv_callbookdate,exchange_tv_ticketno,exchange_tv_rcnno,
             exchange_tv_pending_status,exchange_tv_problemdescription;
    ArrayList<String> ProductName;
    ArrayList<String> ProductDesc;
    HashMap<String,String>  hasProductdes=new HashMap<>();
    String productsname="",productsdesc="",product_id="";
    String productcode="",modelcode="";
    EditText et_price;
    String OrderId,Products,Model,ExchangeExplained,EstimatedCost;
    ArrayList<ProductModel> ProductModel = new ArrayList<>();
    ArrayList<ModelsModel> modelsModels = new ArrayList<>();
    ArrayList<Ifbpointmodel> ifbpointmodels=new ArrayList<>();

    ProgressBar progressBar;
    TextView tv_smssend,tv_pending_status,tv_problemdescription,tv_status,tv_escal,tv_modelname,tv_calltype;
    String productnames,SAPID="";
    LinearLayout  ll_medium;
    RadioButton radio1,radio2;
    RadioGroup radio_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefdetails = getSharedPreferences("details", 0);

        tv_smssend=findViewById(R.id.tv_error_message);

        radio_group=findViewById(R.id.radio_group);
        progressBar=findViewById(R.id.taskProcessing);
        init();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void init(){

        spnr_product=findViewById(R.id.exchange_spnr_product);
        spnr_machine=findViewById(R.id.exchange_spnr_machine);
        spnr_ifbpoint=findViewById(R.id.exchange_spnr_ifbpoint);
        lv_product=findViewById(R.id.exchange_lv_product_list);
        exchange_btn_openwebsite=findViewById(R.id.exchange_btn_openwebsite);
        exchange_btn_openwebsite.setOnClickListener(this);
        exchange_btn_submit=findViewById(R.id.exchange_btn_submit);
        exchange_btn_submit.setOnClickListener(this);
        exchange_btn_ifbpont=findViewById(R.id.exchange_btn_ifbpont);
        exchange_btn_ifbpont.setOnClickListener(this);
        exchange_btn_openpdf=findViewById(R.id.exchange_btn_openpdf);
        exchange_btn_openpdf.setOnClickListener(this);
        exchange_tv_custname=findViewById(R.id.exchange_tv_custname);
        exchange_tv_callbookdate=findViewById(R.id. exchange_tv_callbookdate);
        exchange_tv_ticketno=findViewById(R.id.exchange_tv_ticketno);
        exchange_tv_rcnno=findViewById(R.id.exchange_tv_rcnno);
        exchange_tv_pending_status=findViewById(R.id.exchange_tv_pending_status);
        exchange_tv_problemdescription=findViewById(R.id.exchange_tv_problemdescription);
        et_price=findViewById(R.id.exchange_et_price);
        tv_pending_status=findViewById(R.id.tv_pending_status);
        ex_btn_add=findViewById(R.id.ex_btn_add);
        ex_btn_add.setOnClickListener(this);
        tv_problemdescription=findViewById(R.id.tv_problemdescription);
        ll_medium=findViewById(R.id.ll_medium);
        tv_status=findViewById(R.id.tv_status);
        tv_escal=findViewById(R.id.tv_escal);
        tv_modelname=findViewById(R.id.tv_modelname);
        tv_calltype=findViewById(R.id.exchange_tv_calltype);
        get_tickrt_details();

    }

    public void getproducts() {

        if (CheckConnectivity.getInstance(this).isOnline()){

            // showProgressDialog();
            final String URL = "https://sapsecurity.ifbhub.com/api/Product/getProduct";
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            if (response == null){

                                showsmspopup("No data found");
                                return;
                            }

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getString("Status").equals("true")) {

                                    JSONArray array = new JSONArray(jsonObject.getString("Data"));

                                    ProductModel.clear();
                                  //  ProductModel.add(new ProductModel(" -- select product -- ", ""));


                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject obj = array.getJSONObject(i);

                                        String pronamre=obj.getString("Code");
                                        if (pronamre.equals(productnames)){
                                            ProductModel.add(new ProductModel(obj.getString("Name"), obj.getString("Code")));
                                        }


                                    }

                                    if (ProductModel.size()==0){

                                        ProductModel.add(new ProductModel(" -- select product -- ", ""));
                                    }

                                    ArrayAdapter recheck_adapter = new ArrayAdapter(ExchangeMain.this, android.R.layout.simple_spinner_item, ProductModel);
                                    recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    spnr_product.setAdapter(recheck_adapter);
                                    spnr_product.setOnItemSelectedListener(ExchangeMain.this);
//
                                } else {

                                    showsmspopup("No data found");
                                    ProductModel.clear();
                                    ProductModel.add(new ProductModel(" -- select product -- ", ""));

                                    ArrayAdapter recheck_adapter = new ArrayAdapter(ExchangeMain.this, android.R.layout.simple_spinner_item, ProductModel);
                                    recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    spnr_product.setAdapter(recheck_adapter);
                                    spnr_product.setOnItemSelectedListener(ExchangeMain.this);

                                    // Toast.makeText(Troublesehooting.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    showsmspopup("No Data Found");

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
        else {

            showsmspopup("No internet connection");

        }

    }

    public void getmodel() {

        if (CheckConnectivity.getInstance(this).isOnline()){

            // showProgressDialog();
            final String URL = "https://sapsecurity.ifbhub.com/api/Product/getProductModel?model.troubleshoot.Code="+productcode;
            System.out.println(URL);
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            System.out.println("product details-->" + response);

                            if (response == null){

                                showsmspopup("No data found");
                                return;
                            }

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                if (jsonObject.getString("Status").equals("true")) {

                                    JSONArray array = new JSONArray(jsonObject.getString("Data"));

                                    modelsModels.clear();
                                    modelsModels.add(new ModelsModel(" -- select model -- ", ""));

                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject obj = array.getJSONObject(i);
                                        String ProductModels=obj.getString("ProductModel");

                                        JSONObject json=new JSONObject(ProductModels);

                                        modelsModels.add(new ModelsModel(json.getString("MName"), json.getString("MId")));
                                    }

                                    ArrayAdapter recheck_adapter = new ArrayAdapter(ExchangeMain.this, android.R.layout.simple_spinner_item, modelsModels);
                                    recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    spnr_machine.setAdapter(recheck_adapter);
                                    spnr_machine.setOnItemSelectedListener(ExchangeMain.this);


//
                                } else {

                                    showsmspopup("No data found");

                                    modelsModels.clear();
                                    modelsModels.add(new ModelsModel(" -- select model -- ", ""));

                                    ArrayAdapter recheck_adapter = new ArrayAdapter(ExchangeMain.this, android.R.layout.simple_spinner_item, modelsModels);
                                    recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    spnr_machine.setAdapter(recheck_adapter);
                                    spnr_machine.setOnItemSelectedListener(ExchangeMain.this);

                                    // Toast.makeText(Troublesehooting.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    showsmspopup("No data found ");

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
        else {

            showsmspopup("No internet connection");

        }

    }

    public void get_tickrt_details(){

        try {

            prefdetails = getSharedPreferences("details", 0);
            String details = prefdetails.getString("details", "");

            JSONObject object1 = new JSONObject(details);
            TicketNo = object1.getString("TicketNo");
            CallType = object1.getString("CallType");
            Street=object1.getString("Street");
            City=object1.getString("City");
            State=object1.getString("State");
            Dop=(object1.getString("DOP"));
            Product=object1.getString("Product");
            CustomerCode=object1.getString("CustomerCode");
            FGProducts=object1.getString("FGCode");
            Address = object1.getString("Address");
            PinCode=object1.getString("PinCode");

            if (!PinCode.equals(null)) {

                getifbpoint(PinCode);
            }
            SerialNo=object1.getString("serial_no");
            RCNNo = object1.getString("RCNNo");
            CustomerName = object1.getString("CustomerName");
            //  tv_address.setText(Address.replace("\\s+", ""));
            PinCode=object1.getString("PinCode");
            MachinStatus = object1.getString("MachinStatus");
            productnames=object1.getString("Product");
            exchange_tv_callbookdate.setText(object1.getString("CallBookDate"));
            String Status = object1.getString("Status");
            tv_status.setText(Status);
            CallType = object1.getString("CallType");

            tv_calltype.setText(CallType.substring(0, 1));
            String  Medium=object1.getString("Medium");

            String product=object1.getString("Product");

            if (product.equals("AC"))
            {
               tv_modelname.setText("Air Conditioner");
            }
            else  if (product.equals("CD"))
            {
               tv_modelname.setText("Clothes Dryer");
            }
            else  if (product.equals("DW"))
            {
               tv_modelname.setText("Dish Washer");
            }
            else  if (product.equals("IND"))
            {
               tv_modelname.setText("Industrial Dish washer");
            }
            else  if (product.equals("KA"))
            {
               tv_modelname.setText("Kitchen Appliances");
            }

            else  if (product.equals("RF"))
            {
               tv_modelname.setText("Refrigerator");
            }
            else  if (product.equals("ST"))
            {
               tv_modelname.setText("Stabilizer");
            }
            else  if (product.equals("WD"))
            {
               tv_modelname.setText("Washer Dryer");
            }
            else  if (product.equals("WM"))
            {
               tv_modelname.setText("Washing Machine");
            }
            else  if (product.equals("WP"))
            {
               tv_modelname.setText("Water Purifier");
            }
            else  if (product.equals("MW"))
            {
                tv_modelname.setText("Microwave Oven");
            }

            else {

                tv_modelname.setText(product);
            }

          //  tv_modelname.setText(object1.getString("Model"));

            if(Medium.equals("Phone") || Medium.equals("CSR Portal")  ){

                ll_medium.setVisibility(View.VISIBLE);
            }

            String priorit = object1.getString("Priority");

            if (priorit.equals("")){
                tv_escal.setText("");
            }
            else {
                tv_escal.setText("Escalate ");
            }

            String PendingReason=object1.getString("PendingReason");

            if (!PendingReason.equals("")){

                tv_pending_status.setText(object1.getString("PendingReason"));
            }

            else{
                tv_pending_status.setVisibility(View.GONE);

            }

            String ProblemDescription=object1.getString("ProblemDescription");
            if (!ProblemDescription.equals("")){

                tv_problemdescription.setText(object1.getString("ProblemDescription"));
            }
            else
            {

                tv_problemdescription.setVisibility(View.GONE);
            }

            updatevalue();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void updatevalue(){

        exchange_tv_custname.setText(CustomerName);

        exchange_tv_ticketno.setText(TicketNo);
        exchange_tv_rcnno.setText(RCNNo);
        exchange_tv_pending_status.setText(Address);

        getproducts();
    }

    public void getproductdetails(){

        try{

            JSONArray jsonArray=new JSONArray(loadJSONFromAsset(ExchangeMain.this, "productdata.json"));


            ProductName = new ArrayList<>();
            for(int i=0;i<jsonArray.length();i++){

                JSONObject object=jsonArray.getJSONObject(i);
                ProductName.add(object.getString("product"));

            }
            ArrayAdapter product_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ProductName);
            product_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spnr_product.setAdapter(product_adapter);
            spnr_product.setOnItemSelectedListener(ExchangeMain.this);

        }catch (Exception e){

        }

    }

    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream is=null;
        try {
            AssetManager manager = context.getAssets();
            //Log.d(TAG,"path "+jsonFileName);
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.exchange_btn_openwebsite:

                openwebsite("https://www.ifbappliances.com/storelocator/");
                break;

            case R.id.exchange_iv_call:
                callcustomer();
                break;

            case R.id.exchange_btn_submit:

                 getalllistitem();

                break;

            case R.id.exchange_btn_openpdf:

                openwebsite("https://drive.google.com/file/d/1vQ2o_4rc7eDqXRFCKtBJP9X0xVrf4UTA/view");

            break;
            case R.id.exchange_btn_ifbpont:

                openwebsite("https://www.ifbappliances.com/storelocator/");
             break;

            case R.id.ex_btn_add:
                addproduct();
                break;

        }

    }

    public void addproduct(){


        EstimatedCost = et_price.getText().toString();

        if(productcode.equals(""))
        {
            showsmspopup("Please select Product");
        }
        else if(modelcode.equals("")){

            showsmspopup("Please select Model");
        }
        else if (EstimatedCost.length()==0){

            showsmspopup("Please enter Amount");
        }
        else if(SAPID.length()==0){

            showsmspopup("Please Select IFB POINT");
        }

        else {

            int selectedId = radio_group.getCheckedRadioButtonId();
            radio1 = (RadioButton) findViewById(selectedId);
            ExchangeExplained=radio1.getText().toString();

            try {

                exchangeProductModel = new ExchangeProductModel();
                exchangeProductModel.setId(modelcode);
                exchangeProductModel.setProduct(productsname);
                exchangeProductModel.setModel(productsdesc);
                exchangeProductModels.add(exchangeProductModel);
                exchangeProductAdapter = new ExchangeProductAdapter(exchangeProductModels, ExchangeMain.this);
                exchangeProductAdapter.notifyDataSetChanged();
                lv_product.setAdapter(exchangeProductAdapter);
              //  showsmspopup("Product added in List");

            } catch (Exception e) {


                e.printStackTrace();
            }
        }
    }

    public void openwebsite(String url){


        startActivity(new Intent(ExchangeMain.this, Viewifbpoint.class).putExtra("url",url));
    }

    public void callcustomer(){

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +TelePhone));// Initiates the Intent
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.exchange_spnr_product) {

            ProductModel selectedItem = (ProductModel) parent.getSelectedItem();
            productsname = selectedItem.getName();
            productcode = selectedItem.getCode();

            if (!productcode.equals("")){

                getmodel();
            }

            else {

                modelsModels.clear();
                modelsModels.add(new ModelsModel(" -- select model -- ", ""));
                ArrayAdapter recheck_adapter = new ArrayAdapter(ExchangeMain.this, android.R.layout.simple_spinner_item, modelsModels);
                recheck_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                spnr_machine.setAdapter(recheck_adapter);
                spnr_machine.setOnItemSelectedListener(ExchangeMain.this);

            }

        }
       else if(parent.getId() == R.id.exchange_spnr_machine){

            ModelsModel modelsModel=(ModelsModel) parent.getSelectedItem();

            modelcode=modelsModel.getCode();
            productsdesc=modelsModel.getName();
        }

        else if(parent.getId() == R.id.exchange_spnr_ifbpoint){

            Ifbpointmodel ifbpointmodel=(Ifbpointmodel) parent.getSelectedItem();
            SAPID=ifbpointmodel.getCode();
            if (SAPID.length()==0){

                SAPID="NA";
            }

            System.out.println(SAPID);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void Submitdata(String productsname,String modelcode) {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            long imagename = System.currentTimeMillis();


            String url = AllUrl.baseUrl+"Exchange/AddMachineExchangeRequest?" +
                    "model.potentialexchange.OrderId="+TicketNo+"" +
                    "&model.potentialexchange.EstimatedCost="+EstimatedCost+
                    "&model.potentialexchange.Product="+productsname.replace(" ","%20")+"" +
                    "&model.potentialexchange.Model="+modelcode+
                    "&model.potentialexchange.IFBPointSAPID="+SAPID+
                    "&model.potentialexchange.Source=SAAPP"+
                    "&model.potentialexchange.ExchangeExplained="+ExchangeExplained
                    ;


            System.out.println(url);


            // calling api via volley string request

            RequestQueue queue = Volley.newRequestQueue(this);
          //  progressBar.setVisibility(View.VISIBLE);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            System.out.println("Login--->response"+response);

                          //  progressBar.setVisibility(View.GONE);

                            try{

                                // after getting josn response

                                JSONObject object=new JSONObject(response);

                                String status=object.getString("Status");

                                if (status.equals("true")){

                                    showsmspopup(object.getString("Message"));
                                  //  showpop("Please Enter Valid Mobileno And Password ");

                                }
                                else {

                                    showsmspopup(object.getString("Message"));
                                    // if user enter wrong user id and password
                                    // Toast.makeText(LoginActivity.this, "Please Enter Valid Mobileno And Password or Technician Inactive", Toast.LENGTH_SHORT).show();
                                 //   showpop("Please Enter Valid Mobileno And Password ");

                                }

                            }catch (Exception e){

                                e.printStackTrace();

                            }


                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            progressBar.setVisibility(View.GONE);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();

                    return params;
                }
            };
            queue.add(postRequest);

        } else {

            // if no internet found open no internet layout


            showsmspopup("No Internet connection");

        }
    }





    //  https://sapsecurity.ifbhub.com/api/Exchange/AddMachineExchangeRequest?model.potentialexchange.OrderId=121222299&model.potentialexchange.Product=WM&model.potentialexchange.Model=898989898998&model.potentialexchange.ExchangeExplained=wwwwwwwwwwww&model.potentialexchange.EstimatedCost=1122333.232

    public void showsmspopup(String message){

        tv_smssend.setText(message);

        tv_smssend.setVisibility(View.VISIBLE);

        Animation animation;
        animation = AnimationUtils.loadAnimation(ExchangeMain.this, R.anim.slide_down);
        tv_smssend.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_smssend.setAnimation(null);
                tv_smssend.setVisibility(View.GONE);
            }
        }, 3000);
    }


    public void getalllistitem(){

        if (exchangeProductModels.size()==0){

            showsmspopup("Please Add Proposed Product");
        }
        else {

            for (int i=0;i<exchangeProductModels.size();i++){

                Submitdata(exchangeProductModels.get(i).getProduct(),exchangeProductModels.get(i).getId());
            }

        }

    }


    public void getifbpoint(String pincode){
        if (CheckConnectivity.getInstance(this).isOnline()){

            // showProgressDialog();
           // final String URL = "https://sapsecurity.ifbhub.com/api/ifbpoint/getNearestIfbPoint?model.ifbpoint.ZipCode="+pincode;
             final String URL="https://sapsecurity.ifbhub.com/api/ifbpoint/getNearestIfbPoint?model.ifbpoint.ZipCode="+pincode;

            System.out.println(URL);
            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            System.out.println("product details-->" + response);

                            if (response == null) {

                                showsmspopup("No data found");


                                ifbpointmodels.clear();
                                ifbpointmodels.add(new Ifbpointmodel(" -- No IFB POINT found -- ", ""));

                                    ArrayAdapter ifbpoint_adapter = new ArrayAdapter(ExchangeMain.this, android.R.layout.simple_spinner_item, modelsModels);
                                    ifbpoint_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    spnr_ifbpoint.setAdapter(ifbpoint_adapter);
                                    spnr_ifbpoint.setOnItemSelectedListener(ExchangeMain.this);

                                    // Toast.makeText(Troublesehooting.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            } else {

                                try {


                                    JSONArray array = new JSONArray(response);

                                    ifbpointmodels.clear();
                                    ifbpointmodels.add(new Ifbpointmodel(" -- select IFB POINT -- ", ""));

                                    for (int i = 0; i < array.length(); i++) {

                                        JSONObject obj = array.getJSONObject(i);
                                        String SAPCode = obj.getString("SAPCode");
                                        String Name= obj.getString("Name");

                                        ifbpointmodels.add(new Ifbpointmodel(Name,SAPCode));
                                    }

                                    ArrayAdapter ifbpoint_adapter = new ArrayAdapter(ExchangeMain.this, android.R.layout.simple_spinner_item, ifbpointmodels);
                                    ifbpoint_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Setting the ArrayAdapter data on the Spinner
                                    spnr_ifbpoint.setAdapter(ifbpoint_adapter);
                                    spnr_ifbpoint.setOnItemSelectedListener(ExchangeMain.this);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.setVisibility(View.GONE);
                    ifbpointmodels.clear();
                    ifbpointmodels.add(new Ifbpointmodel(" -- No IFB POINT found -- ", ""));
                    ArrayAdapter ifbpoint_adapter = new ArrayAdapter(ExchangeMain.this, android.R.layout.simple_spinner_item, ifbpointmodels);
                    ifbpoint_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //Setting the ArrayAdapter data on the Spinner
                    spnr_ifbpoint.setAdapter(ifbpoint_adapter);
                    spnr_ifbpoint.setOnItemSelectedListener(ExchangeMain.this);

                }
            });
// Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
        else {

            showsmspopup("No internet connection");

        }
    }
}


