package technician.ifb.com.ifptecnician.amc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import technician.ifb.com.ifptecnician.R;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.formatter.IFillFormatter;

import org.json.JSONObject;

public class Amcdetails extends AppCompatActivity implements View.OnClickListener {

   // customer details
    TextView tv_name,tv_mobile,tv_alt_mobile,tv_email;
    String amcdata="";

    //Machine Details
    TextView tv_product,tv_productcode,tv_serialno,tv_machine_status;

    // payment section
    TextView tv_base,tv_dis,tv_tax,tv_gross,tv_type,tv_icr_date,tv_icrno;

    ImageView iv_cust;
    Toolbar toolbar;
    int rotationAngle = 0;
    LinearLayout ll_custview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amcdetails);


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Punched AMC Details");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent in = getIntent();
        amcdata=in.getStringExtra("data");
        System.out.println("Amc data--> "+amcdata);

        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void init(){


        tv_name=findViewById(R.id.tv_name);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_alt_mobile=findViewById(R.id.tv_alt_mobile);
        tv_email=findViewById(R.id.tv_email);

        tv_product=findViewById(R.id.tv_product);
        tv_productcode=findViewById(R.id.tv_productcode);
        tv_serialno=findViewById(R.id.tv_serialno);
        tv_machine_status=findViewById(R.id.tv_machine_status);

        tv_base=findViewById(R.id.tv_baseamount);
        tv_dis=findViewById(R.id.tv_discountamount);
        tv_tax =findViewById(R.id.tv_taxamount);
        tv_gross=findViewById(R.id.tv_grossamount);
        tv_type=findViewById(R.id.tv_paymentmode);

        tv_icr_date=findViewById(R.id.tv_icr_date);
        tv_icrno=findViewById(R.id.tv_icrno);

        iv_cust=findViewById(R.id.iv_cust);
        iv_cust.setOnClickListener(this);
        ll_custview=findViewById(R.id.ll_custview);

        updatetextvalue();

    }


    public void updatetextvalue(){

        try{

            if (amcdata.length()==0){

            }
            else{

                JSONObject jsonObject=new JSONObject(amcdata);

                tv_name.setText(jsonObject.getString("customer_name"));
               // tv_mobile.setText(jsonObject.getString("ren"));
                tv_alt_mobile.setText(jsonObject.getString("altno"));
                tv_email.setText(jsonObject.getString("email"));


              //  tv_product.setText(jsonObject.getString(""));
                tv_productcode.setText(jsonObject.getString("fg_product"));
                tv_serialno.setText(jsonObject.getString("serial_no"));
               // tv_machine_status.setText(jsonObject.getString(""));

                tv_base.setText(jsonObject.getString("sale_price"));
                tv_dis.setText(jsonObject.getString("discount"));
                tv_tax.setText(jsonObject.getString("total_tax"));
                tv_gross.setText(jsonObject.getString("gross_value"));
                tv_type.setText(jsonObject.getString("PayMentMode"));

               tv_icr_date.setText(jsonObject.getString("amc_purchased_date"));
               tv_icrno.setText(jsonObject.getString("ICRNO"));


              //  {"customer_code":"18038813","customer_name":"Jyoti Kumar",
                //  "ren":"9007141607","altno":"9875301186","email":"",
                //  "serial_no":"012945180712208764","fg_product":"8903287012945",
                //  "amc_purchased_date":"23-Aug-2021","dso_code":"3001661",
                //  "amc_extended_wty":"AMC 1 Year","description":"AMC 1 Year",
                //  "discount":"0","sale_price":"3700.00","total_tax":"666.00",
                //  "gross_value":"4366.00","cess":"0","ICRNO":"2521000052",
                //  "TechCode":"14361848","PayMentMode":"cash","Status":"",
                //  "CreatedBy":"14361848","chequeno":"","chequedate":"",
                //  "bankname":"","upirefno":"","newaddressflag":"","house_no":"",
                //  "pincode":"","state":"","area_hou_flat_no":"",
                //  "buld_street_name":"","moreaddressdtls":"",
                //  "landmark":"","city":"","address":"",
                //  "Frcode":"3001661"}


            }

        }catch (Exception E){

            E.printStackTrace();
        }
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.iv_cust:

                startanimation();
                break;

            default:
                break;

        }
    }

    public void startanimation(){

        Toast.makeText(this, "toast", Toast.LENGTH_SHORT).show();

//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate);
//        iv_cust.startAnimation(animation);
//        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_cust, "rotation",rotationAngle, rotationAngle + 180);
//        anim.setDuration(500);
//        anim.start();
//        rotationAngle += 180;
//        rotationAngle = rotationAngle%360;
//        iv_cust.setRotation(90);
//        openanimation();


    }


    public void closeanimations(){


        ScaleAnimation anim=new ScaleAnimation(1,1,1,0);
        anim.setDuration(1000);
        anim.setFillAfter(true);

        ll_custview.startAnimation(anim);

//        TranslateAnimation animation=new TranslateAnimation(500f, 0f,0f, 0f);
//        animation.setDuration(1000);
//        animation.setFillAfter(true);
//        applogo.startAnimation(animation);
//        applogo.setVisibility(View.VISIBLE);
//
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // rotateImageView();
//
//                checkapk();
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });


    }


    public void openanimation(){

        ScaleAnimation anim=new ScaleAnimation(1,1,0,1);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        ll_custview.startAnimation(anim);

    }


}