package technician.ifb.com.ifptecnician.ebill;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.essentiallead.EssentialPayment;
import technician.ifb.com.ifptecnician.model.Add_item_model;
import technician.ifb.com.ifptecnician.model.Essential_add_model;
import technician.ifb.com.ifptecnician.stock.StockAdapter;

public class CreateBill extends AppCompatActivity implements EbillAdapter.TotalAdapterListener{
    
    String sparedata,essentialdata,calltype;
    EbillModel ebillModel;
    ArrayList<EbillModel> ebillModels=new ArrayList<>();
    EbillAdapter ebillAdapter;
    RecyclerView rv_bill;
    TextView tv_totalbill;
    double sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bill);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Bill");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_totalbill=findViewById(R.id.tv_totalbill);

        rv_bill=findViewById(R.id.rv_bill);
        Intent in = getIntent();

        sparedata=in.getStringExtra("spare");
        essentialdata=in.getStringExtra("essential");
        calltype=in.getStringExtra("calltype");
        System.out.println("spare data -->"+sparedata);
        System.out.println("Essntial data -->"+essentialdata);

        try{


            if (calltype.equals("SERVICE CALL")){

                    ebillModel=new EbillModel();
                    ebillModel.setHeader("Service Charge");
                    ebillModel.setCode("");
                    ebillModel.setName("Service charge");
                    ebillModel.setQty("");

                    ebillModel.setBase_amount("500");

                    double nets=500;
                    ebillModel.setNet_amount(""+nets);

                    double taxs=nets*.18;
                    ebillModel.setTax_amount(""+taxs);

                    double grosss=nets+taxs;
                    ebillModel.setGross_amount(""+grosss);

                    ebillModels.add(ebillModel);


            }


            if (sparedata.length()!=0){

                JSONArray jsonArray=new JSONArray(sparedata);

                for (int i=0;i<jsonArray.length();i++){

                    ebillModel=new EbillModel();
                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                    if (i==0){
                        ebillModel.setHeader("Spare Details");

                    }
                    else{
                        ebillModel.setHeader("");

                    }

                    ebillModel.setName(jsonObject.getString("itemname"));
                    ebillModel.setCode(jsonObject.getString("description"));
                    ebillModel.setQty(jsonObject.getString("count"));


                    ebillModel.setBase_amount("100");


                    double qty=Double.parseDouble(jsonObject.getString("count"));

                    double net=100*qty;
                    ebillModel.setNet_amount(""+net);


                    double tax=net*.18;
                    ebillModel.setTax_amount(""+tax);

                    double gross=net+tax;
                    ebillModel.setGross_amount(""+gross);

                    ebillModels.add(ebillModel);
                }

            }


            if (essentialdata.length()!=0){

                JSONArray jsonArrays=new JSONArray(essentialdata);


                for (int j=0;j<jsonArrays.length();j++){

                    ebillModel=new EbillModel();

                    if (j==0){
                        ebillModel.setHeader("Essential Details");

                    }
                    else{
                        ebillModel.setHeader("");

                    }
                    JSONObject jsonObject=jsonArrays.getJSONObject(j);

                    ebillModel.setName(jsonObject.getString("ename"));
                    ebillModel.setCode(jsonObject.getString("ecode"));
                    ebillModel.setQty(jsonObject.getString("equentity"));
                    ebillModel.setBase_amount("100");


                    double qty=Double.parseDouble(jsonObject.getString("equentity"));

                    double net=100*qty;
                    ebillModel.setNet_amount(""+net);


                    double tax=net*0.18;
                    ebillModel.setTax_amount(""+tax);

                    double gross=net+tax;
                    ebillModel.setGross_amount(""+gross);



                    ebillModels.add(ebillModel);
                }



                ebillAdapter=new EbillAdapter(this, ebillModels,this);
                rv_bill.setLayoutManager(new LinearLayoutManager(this));

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rv_bill.setLayoutManager(mLayoutManager);
                rv_bill.setItemAnimator(new DefaultItemAnimator());
               // rv_bill.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
                rv_bill.setAdapter(ebillAdapter);

                gettotal();
            }




        }catch (Exception e){

            e.printStackTrace();
        }

    }


    public void gettotal(){

        sum = 0;
        for(int i = 0; i <ebillModels.size(); i++) {
            String price = ebillModels.get(i).getGross_amount();

            System.out.println("price--> "+price);

           try{

               sum += Double.parseDouble(price);
               System.out.println("Sum-->"+sum);
               tv_totalbill.setText("\u20B9 "+sum);
           }
           catch (Exception e){

               e.printStackTrace();
           }

        }

    }

    @Override
    public void onContactSelected(EbillModel ebillModel) {

        sum = 0;
        for(int i = 0; i <ebillModels.size(); i++) {
            String price = ebillModels.get(i).getGross_amount();

            System.out.println("price--> "+price);

            sum += Double.parseDouble(price);
            System.out.println("Sum-->"+sum);
            tv_totalbill.setText("\u20B9 "+sum);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void BillSubmit(View v){

        startActivity(new Intent(CreateBill.this, EssentialPayment.class)
                .putExtra("tot_amount",sum));

    }


}