package technician.ifb.com.ifptecnician.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.DetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.fragment.adapter.OfflineAddapter;
import technician.ifb.com.ifptecnician.fragment.dummy.OfflineModel;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;


public class Offlinework extends Fragment implements OfflineAddapter.ViewClick{

    Cursor offlinedataCursor;
    Dbhelper dbhelper;
    SessionManager sessionManager;
    RecyclerView lv_offlinework;
    SwipeRefreshLayout swipeRefreshLayout;
    OfflineModel offlineModel;
    ArrayList<OfflineModel> offlinemodels=new ArrayList<OfflineModel>();
    OfflineAddapter offlineAddapter;

  String  RefId,TicketNo,Branch,Franchise,CallType,Status,Product,Model,SerialNo,MachinStatus,DOP,DOI,
    PendingReason,CallBookDate,AssignDate,ClosedDate,CancelledDate,ChangeDate,TechnicianCode,
    TechName,CustomerCode,CustomerName,PinCode,TelePhone,RCNNo,MobileNo,Street,City,State,
    Address,ServiceType,Email,Priority,Quantity,scheduledate,FromDate,Todate,Ageing,odu_ser_no,
    FGCode,ProblemDescription,Additive,AdditiveDesc;

  SharedPreferences  prefdetails;

    public Offlinework() {
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
        View view= inflater.inflate(R.layout.fragment_offlinework, container, false);

        dbhelper=new Dbhelper(getContext());
        prefdetails=getActivity().getSharedPreferences("details",0);
        swipeRefreshLayout=view.findViewById(R.id.swipe_offlinework);

        lv_offlinework=view.findViewById(R.id.lv_offlinework);
        lv_offlinework.setLayoutManager(new LinearLayoutManager(getContext()));

        getofflineSubmitValue();
        return view;
    }


    public void getofflineSubmitValue(){

        offlinedataCursor=dbhelper.get_offlineurl_data();
         System.out.println();

        if (offlinedataCursor!=null && offlinedataCursor.moveToFirst()){

            System.out.println("data found");
            do{

                try{

                    String type=offlinedataCursor.getString(offlinedataCursor.getColumnIndex("Type"));

                    System.out.println("types-->"+type);
                    JSONObject object1=new JSONObject(type);

                    Status= object1.getString("Status");
                    TicketNo= object1.getString("TicketNo");
                    Branch= object1.getString("Branch");
                    Franchise= object1.getString("Franchise");
                    CallType= object1.getString("CallType");
                    Product= object1.getString("Product");
                    Model= object1.getString("Model");
                    SerialNo= object1.getString("SerialNo");
                    MachinStatus= object1.getString("MachinStatus");
                    DOP= object1.getString("DOP");
                    DOI= object1.getString("DOI");
                    PendingReason= object1.getString("PendingReason");
                    CallBookDate= object1.getString("CallBookDate");
                    AssignDate= object1.getString("AssignDate");
                    ClosedDate= object1.getString("ClosedDate");
                    CancelledDate = object1.getString("CancelledDate");
                    TechnicianCode= object1.getString("TechnicianCode");
                    TechName= object1.getString("TechName");
                    CustomerCode= object1.getString("CustomerCode");
                    CustomerName= object1.getString("CustomerName");
                    PinCode= object1.getString("PinCode");
                    TelePhone= object1.getString("TelePhone");
                    RCNNo= object1.getString("RCNNo");
                    MobileNo= object1.getString("MobileNo");
                    Street= object1.getString("Street");
                    City= object1.getString("City");
                    State= object1.getString("State");
                    Address = object1.getString("Address");
                    // NO126= object1.getString("NO126");
                    ServiceType= object1.getString("ServiceType");
                    Email= object1.getString("Email");
                    Priority= object1.getString("Priority");
                    Ageing=object1.getString("Ageing");
                    FGCode=object1.getString("FGCode");
                    ChangeDate=object1.getString("ChangeDate");
                    ProblemDescription=object1.getString("ProblemDescription");
                    odu_ser_no=object1.getString("odu_ser_no");

                    offlineModel=new OfflineModel();

                    offlineModel.setTicketNo(TicketNo);
                    offlineModel.setCustomerName(CustomerName);
                    offlineModel.setRCNNo(RCNNo);
                    offlineModel.setAddress(Address);
                    offlineModel.setAgeing(Ageing);
                    offlineModel.setStatus(Status);
                    offlineModel.setProblemDescription(ProblemDescription);
                    offlineModel.setCallBookDate(CallBookDate);
                    offlineModel.setCallType(CallType);

                    offlinemodels.add(offlineModel);

                }catch (Exception e){


                }


            }while(offlinedataCursor.moveToNext());


            offlineAddapter=new OfflineAddapter(getContext(),offlinemodels,this);
            lv_offlinework.setAdapter(offlineAddapter);
        }

        else {

            System.out.println("no data found offline ");
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onViewClick() {

//        SharedPreferences.Editor editor=prefdetails.edit();
//        editor.putString("details",json);
//
//        editor.apply();
//        startActivity(new Intent(getContext(), DetailsActivity.class));
    }
}
