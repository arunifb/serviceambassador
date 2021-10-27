package technician.ifb.com.ifptecnician.negative_response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.negative_response.fragment.CustomerFragment;
import technician.ifb.com.ifptecnician.negative_response.fragment.MachinesFragment;
import technician.ifb.com.ifptecnician.negative_response.fragment.NrEssentialFragment;
import technician.ifb.com.ifptecnician.negative_response.fragment.NrSpareFragment;
import technician.ifb.com.ifptecnician.negative_response.fragment.NrStatusFragment;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.session.SessionManager;
import technician.ifb.com.ifptecnician.utility.Valuefilter;


public class NegativeResponseDetails extends AppCompatActivity implements View.OnClickListener{

    BottomNavigationView bottomNavigationView;
    Fragment active;
    SharedPreferences sharedPreferences;

    private static final String TAG = NegativeResponseDetails.class.getSimpleName();
    TextView nr_tv_calltype,nr_tv_custname,nr_tv_callbookdate,nr_tv_servicetype,
             nr_tv_ticketno,nr_tv_rcnno, nr_tv_pending_status,nr_tv_address,nr_tv_status,
             nr_tv_escal,nr_tv_mocelname;

    String TicketNo,Branch,Franchise,CallType,Status,
            Product,Model,SerialNo="000000000000000000",MachinStatus,DOP="00000000",DOI="00000000",CallBookDate,AssignDate,ClosedDate,CancelledDate,
            TechnicianCode,TechName,CustomerCode,CustomerName,PinCode,TelePhone,RCNNo,MobileNo,ProblemDescription,PendingReason,
            Street, City, State, Address, NO126, ServiceType, Email,ChangeDate;
    ImageView nr_cus_iv_call;
    ErrorDetails errorDetails;
    String tss;
    String mobile_details="",mobile_no;
    SharedPreferences sharedPreferencesmobile;
    SessionManager sessionManager;
    Dbhelper dbhelper;
    String dbversion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negative_response_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        errorDetails=new ErrorDetails();
        tss= Valuefilter.getdate();
        dbhelper=new Dbhelper(getApplicationContext());

        dbversion=AllUrl.APP_Version;

//        sharedPreferencesmobile=getSharedPreferences(AllUrl.MOBILE_DETAILS,0);
//        mobile_details=sharedPreferencesmobile.getString("Mobile_details","");
        init();


        // ------------------------- get user session value --------------------------
        sessionManager=new SessionManager(getApplicationContext());
        try{

            HashMap<String, String> user = sessionManager.getUserDetails();

            mobile_no=user.get(SessionManager.KEY_mobileno);

        }
        catch (Exception e){

            e.printStackTrace();
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        removetextpadding();
        final Fragment fragment1 = new CustomerFragment();
        final Fragment fragment2 = new MachinesFragment();
        final Fragment fragment3 = new NrSpareFragment();
        final Fragment fragment4 = new NrEssentialFragment();
        final Fragment fragment5 = new NrStatusFragment();

        final FragmentManager fm = getSupportFragmentManager();
        active = fragment1;
        fm.beginTransaction().add(R.id.nr_fragment, fragment5, "5").hide(fragment5).commit();
        fm.beginTransaction().add(R.id.nr_fragment, fragment4, "4").hide(fragment4).commit();
        fm.beginTransaction().add(R.id.nr_fragment, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.nr_fragment, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.nr_fragment,fragment1, "1").commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_cutomer:

                                fm.beginTransaction().hide(active).show(fragment1).commit();
                                active = fragment1;
                                errorDetails.Errorlog(NegativeResponseDetails.this,"Click%20On%20CustomerTab", TAG,"Customer%20Tab","Customer%20Tab",mobile_no,TicketNo,"C",mobile_details,dbversion,tss);

                                return true;

                            case R.id.action_machine:

                                fm.beginTransaction().hide(active).show(fragment2).commit();
                                active = fragment2;
                                errorDetails.Errorlog(NegativeResponseDetails.this,"Click%20On%20MachineTab", TAG,"MachineTab","MachineTab",mobile_no,TicketNo,"C",mobile_details,dbversion,tss);

                                return true;

                            case R.id.action_spare:
                                errorDetails.Errorlog(NegativeResponseDetails.this,"Click%20On%20SpareTab", TAG,"Spare%20Tab","Spare%20Tab",mobile_no,TicketNo,"C",mobile_details,dbversion,tss);

                                fm.beginTransaction().hide(active).show(fragment3).commit();
                                active = fragment3;
                                return true;

                            case R.id.action_essential:
                                errorDetails.Errorlog(NegativeResponseDetails.this,"Click%20On%20EssentialTab", TAG,"Essential%20Tab","Essential%20Tab",mobile_no,TicketNo,"C",mobile_details,dbversion,tss);

                                fm.beginTransaction().hide(active).show(fragment4).commit();
                                active = fragment4;
                                return true;

                            case R.id.action_status:
                                errorDetails.Errorlog(NegativeResponseDetails.this,"Click%20On%20StausTab", TAG,"StausTab","StausTab",mobile_no,TicketNo,"C",mobile_details,dbversion,tss);

                                fm.beginTransaction().hide(active).show(fragment5).commit();
                                active = fragment5;
                                return true;
                        }
                        return true;
                    }
                });

    }

    public void removetextpadding(){

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
            View activeLabel = item.findViewById(R.id.largeLabel);
            if (activeLabel instanceof TextView) {
                activeLabel.setPadding(0, 0, 0, 0);
            }
        }
    }

    public void init(){

        nr_tv_calltype=findViewById(R.id.nr_tv_calltype);
        nr_tv_custname=findViewById(R.id.nr_tv_custname);
        nr_tv_callbookdate=findViewById(R.id.nr_tv_callbookdate);
        nr_tv_servicetype=findViewById(R.id.nr_tv_servicetype);
        nr_tv_ticketno=findViewById(R.id.nr_tv_ticketno);
        nr_tv_rcnno=findViewById(R.id.nr_tv_rcnno);
        nr_tv_pending_status=findViewById(R.id.nr_tv_pending_status);
        nr_tv_address=findViewById(R.id.nr_tv_address);
        nr_tv_status=findViewById(R.id.nr_tv_status);
        nr_tv_escal=findViewById(R.id.nr_tv_escal);
        nr_cus_iv_call=findViewById(R.id.nr_cus_iv_call);
        nr_cus_iv_call.setOnClickListener(this);
        nr_tv_mocelname=findViewById(R.id.nr_tv_mocelname);

        updatevalue();

    }

    public void updatevalue(){

        try {

            sharedPreferences = getSharedPreferences("details", 0);
            String details = sharedPreferences.getString("details", "");

            JSONObject object1 = new JSONObject(details);

            TicketNo = object1.getString("TicketNo");
            CallType = object1.getString("CallType");
            Street=object1.getString("Street");
            City=object1.getString("City");
            State=object1.getString("State");
            Product=object1.getString("Product");
            CustomerCode=object1.getString("CustomerCode");
            ChangeDate=object1.getString("ChangeDate");
            Address = object1.getString("Address");
            PinCode=object1.getString("PinCode");
            CallBookDate = object1.getString("CallBookDate");
            Status = object1.getString("Status");
            Franchise=object1.getString("Franchise");
            AssignDate=object1.getString("AssignDate");

           // nr_tv_mocelname.setText(object1.getString("Model"));


            String product=object1.getString("Product");

            if (product.equals("AC"))
            {
                nr_tv_mocelname.setText("Air Conditioner");
            }
            else  if (product.equals("CD"))
            {
                nr_tv_mocelname.setText("Clothes Dryer");
            }
            else  if (product.equals("DW"))
            {
                nr_tv_mocelname.setText("Dish Washer");
            }
            else  if (product.equals("IND"))
            {
                nr_tv_mocelname.setText("Industrial Dish washer");
            }
            else  if (product.equals("KA"))
            {
                nr_tv_mocelname.setText("Kitchen Appliances");
            }

            else  if (product.equals("RF"))
            {
                nr_tv_mocelname.setText("Refrigerator");
            }
            else  if (product.equals("ST"))
            {
                nr_tv_mocelname.setText("Stabilizer");
            }
            else  if (product.equals("WD"))
            {
                nr_tv_mocelname.setText("Washer Dryer");
            }
            else  if (product.equals("WM"))
            {
                nr_tv_mocelname.setText("Washing Machine");
            }
            else  if (product.equals("WP"))
            {
                nr_tv_mocelname.setText("Water Purifier");
            }

            else  if (product.equals("MW"))
            {
                nr_tv_mocelname.setText("Microwave Oven");
            }


            else {

                nr_tv_mocelname.setText(product);
            }

            SimpleDateFormat formats=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
            Date dts=formats.parse(AssignDate);
            DateFormat dayfrmat=new SimpleDateFormat("EEEE",Locale.ENGLISH);
            DateFormat maothfor=new SimpleDateFormat("MMM dd yyyy",Locale.ENGLISH);
            DateFormat timef=new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
            String finalDay= dayfrmat.format(dts);
            String mon=maothfor.format(dts);
            String times=timef.format(dts);

            SerialNo=object1.getString("serial_no");

            RCNNo = object1.getString("RCNNo");
            ServiceType = object1.getString("ServiceType");
            CustomerName = object1.getString("CustomerName");

            String priorit = object1.getString("Priority");

            ProblemDescription=object1.getString("ProblemDescription");

            nr_tv_calltype.setText(CallType.substring(0, 1));

            nr_tv_ticketno.setText(TicketNo);
            //  tv_address.setText(Address.replace("\\s+", ""));
            PinCode=object1.getString("PinCode");

            String fulladress=Address.replace("\\s+", "");
            nr_tv_status.setText(Status);
            nr_tv_rcnno.setText(RCNNo);
            nr_tv_servicetype.setText(ServiceType);
            nr_tv_custname.setText(CustomerName);

            PendingReason=object1.getString("PendingReason");

            if (!PendingReason.equals("")){

                nr_tv_pending_status.setText(object1.getString("PendingReason"));
            }

            else{

                nr_tv_pending_status.setVisibility(View.GONE);
            }
            // brachname,Franchise,PinCode, TelePhone,Street,City,State

            String mc=object1.getString("MachinStatus");

            RCNNo=object1.getString("RCNNo");

        }catch (Exception e){

            e.printStackTrace();

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.nr_cus_iv_call:
                callcustomer();
                break;
        }
    }

    public void callcustomer(){

        try{

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +RCNNo));// Initiates the Intent
            startActivity(intent);
        }catch (Exception e){
          e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
