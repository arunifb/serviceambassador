package technician.ifb.com.ifptecnician.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.fragment.adapter.TodaylistAdapter;

import technician.ifb.com.ifptecnician.fragment.dummy.Taskmodel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;


public class TaskFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    String TicketNo,Branch,Franchise,CallType,Status,
            Product,Model,SerialNo,MachinStatus,DOP,DOI,PendingReason,CallBookDate,AssignDate,ClosedDate, CancelledDate ,
            TechnicianCode , TechName  , CustomerCode , CustomerName, PinCode, TelePhone, RCNNo, MobileNo,
            Street,City,State,Address ,NO126,ServiceType,Email,Priority,Ageing;
    Dbhelper dbhelper;
    RecyclerView lv_list;
    public ArrayList<Taskmodel> models=new ArrayList<>();
    public Taskmodel model;
    SharedPreferences prefdetails;
    String result;
    Cursor taskcurser;
    SessionManager sessionManager;
    String PartnerId;
    TodaylistAdapter taskadapter;


    public TaskFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TaskFragment newInstance(int columnCount) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//
//            Toast.makeText(getContext(), "click on search", Toast.LENGTH_SHORT).show();
//            return true;
//        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        dbhelper=new Dbhelper(getContext());
        lv_list=view.findViewById(R.id.lv_task);
        lv_list.setLayoutManager(new LinearLayoutManager(getContext()));

        taskadapter=new TodaylistAdapter(getContext());
        prefdetails=getActivity().getSharedPreferences("details",0);
        sessionManager=new SessionManager(getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);
        System.out.println(PartnerId);
        getdata(PartnerId);
        lv_list.setOnClickListener(onItemClickListener);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

  public void  getdata(String PartnerId){


      if ( CheckConnectivity.getInstance(getContext()).isOnline()) {

          showProgressDialog();
          RequestQueue queue = Volley.newRequestQueue(getContext());
//          String url = AllUrl.loginUrl+"?url=http://centurionbattery.in/development&username="+username+"&password="+password;
          String url=AllUrl.baseUrl+"ServiceOrder/getServiceOrder?TechCode="+PartnerId+"&Status=Assigned";


          System.out.println("get all task-->  "+ url);
          StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                  new Response.Listener<String>() {
                      @Override
                      public void onResponse(String response) {

                          System.out.println("task details-->"+response);

                          result=response;

                          try {

                              JSONArray jsonArray=new JSONArray(response);

                              for (int i = 0; i < jsonArray.length(); i++) {

                                  JSONObject object1 = jsonArray.getJSONObject(i);

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


                                models.add(new Taskmodel(TicketNo,Branch,Franchise,CallType,Status,
                                        Product,Model,SerialNo,MachinStatus,DOP,DOI,PendingReason,CallBookDate,AssignDate,ClosedDate, CancelledDate ,
                                        TechnicianCode , TechName  , CustomerCode , CustomerName, PinCode, TelePhone, RCNNo, MobileNo,
                                        Street,City,State,Address ,NO126,ServiceType,Email,Priority,Ageing));

                                  }


                              taskadapter.setListContent(models);
                              //We in turn set the adapter to the RecyclerView
                              lv_list.setAdapter(taskadapter);

//                              Taskadapter taskadapter=new Taskadapter(models,getContext());
//                              lv_list.setAdapter(taskadapter);

                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                          hideProgressDialog();

                      }
                  }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  hideProgressDialog();
                  Toast.makeText(getContext(), "No Record Found ", Toast.LENGTH_SHORT).show();

              }
          });
// Add the request to the RequestQueue.

          int socketTimeout = 10000; //10 seconds - change to what you want
          RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
          stringRequest.setRetryPolicy(policy);
          queue.add(stringRequest);

      } else {

          Toast.makeText(getContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

      }
  }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
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


    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO: Step 4 of 4: Finally call getTag() on the view.
            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();

            Taskmodel thisItem = models.get(position);
           // Toast.makeText(getContext(), "You Clicked: " + thisItem.getTicketNo(), Toast.LENGTH_SHORT).show();
        }
    };



}
