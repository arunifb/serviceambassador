package technician.ifb.com.ifptecnician.negative_response.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.utility.Getlatlongfromaddress;


public class CustomerFragment extends Fragment implements OnMapReadyCallback,View.OnClickListener {


    SharedPreferences sharedPreferences;
    Button nr_cus_btn_openmap;
    TextView nr_cus_tv_tel,nr_cus_tv_sdate,nr_cus_tv_stime,nr_cus_tv_ageing,nr_cus_tv_addresss;
    ImageView nr_cus_iv_alt_call;

    String address,telephone="",Street,State,City,PinCode;

    public CustomerFragment() {
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

        View view=inflater.inflate(R.layout.fragment_customer2,container,false);
        nr_cus_iv_alt_call=view.findViewById(R.id.nr_cus_iv_alt_call);
        nr_cus_iv_alt_call.setOnClickListener(this);
        nr_cus_tv_sdate=view.findViewById(R.id.nr_cus_tv_sdate);
        nr_cus_tv_stime=view.findViewById(R.id.nr_cus_tv_stime);
        nr_cus_tv_ageing=view.findViewById(R.id.nr_cus_tv_ageing);
        nr_cus_tv_addresss=view.findViewById(R.id.nr_cus_tv_addresss);
        nr_cus_tv_tel=view.findViewById(R.id.nr_cus_tv_tel);
        nr_cus_btn_openmap=view.findViewById(R.id.nr_cus_btn_openmap);
        nr_cus_btn_openmap.setOnClickListener(this);

        updatevalue();
        return view;
    }


    public void updatevalue(){

        try{
            sharedPreferences = getActivity().getSharedPreferences("details", 0);
            String details = sharedPreferences.getString("details", "");

            System.out.println("details"+details);

            JSONObject object = new JSONObject(details);

            String TicketNo = object.getString("TicketNo");

           String AssignDate=object.getString("AssignDate");

            SimpleDateFormat formats=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
            Date dts=formats.parse(AssignDate);
            DateFormat dayfrmat=new SimpleDateFormat("EEEE",Locale.ENGLISH);
            DateFormat maothfor=new SimpleDateFormat("MMM dd yyyy",Locale.ENGLISH);
            DateFormat timef=new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
            String finalDay= dayfrmat.format(dts);
            String mon=maothfor.format(dts);
            String times=timef.format(dts);

            nr_cus_tv_tel.setText(object.getString("TelePhone"));
            nr_cus_tv_sdate.setText(finalDay + "  "+ mon);
            nr_cus_tv_stime.setText(object.getString("TicketNo"));
            nr_cus_tv_ageing.setText("/"+object.getString("Ageing") + "  Days");
            nr_cus_tv_addresss.setText(object.getString("Address"));

            telephone=object.getString("TelePhone");
            if (telephone.length()!=10){

                nr_cus_iv_alt_call.setVisibility(View.GONE);
            }

            Street=object.getString("Street");
            State=object.getString("State");
            City=object.getString("City");
            PinCode=object.getString("PinCode");
        }

        catch (Exception e){

            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.nr_cus_iv_alt_call:
                callalt();
                break;
            case R.id.nr_cus_btn_openmap:
                openGoolgemap();
                break;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {



        try{

            Getlatlongfromaddress getlatlongfromaddress=new Getlatlongfromaddress();
            LatLng customerlatlong = getlatlongfromaddress.getLocationFromAddress(getActivity(),"721423");

            if(!customerlatlong.equals(null)){
                googleMap.addMarker(new MarkerOptions().position(customerlatlong)
                        .title("Customer Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(customerlatlong));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15.0f);
                CameraUpdate center = CameraUpdateFactory.newLatLng(customerlatlong);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
            }
            else {

                LatLng customerpinnolatlong = getlatlongfromaddress.getLocationFromAddress(getActivity(),"721423");
                googleMap.addMarker(new MarkerOptions().position(customerpinnolatlong)
                        .title("Customer Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(customerpinnolatlong));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15.0f);
                CameraUpdate center = CameraUpdateFactory.newLatLng(customerpinnolatlong);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void callalt(){

        if (telephone.length() >9 ){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +telephone));// Initiates the Intent
            startActivity(intent);
        }
        else {

            Toast.makeText(getActivity(), "Phone No not valid", Toast.LENGTH_SHORT).show();
        }


    }

    public void openGoolgemap(){

        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+Street+","+City+","+State+","+PinCode+"");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}
