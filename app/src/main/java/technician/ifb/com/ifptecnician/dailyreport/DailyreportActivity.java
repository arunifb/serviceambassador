package technician.ifb.com.ifptecnician.dailyreport;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.MyApplication;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class DailyreportActivity extends AppCompatActivity implements DailyreportAdapter.JobsAdapterListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    SharedPreferences prefdetails;
    RecyclerView listView;
    String response,status;
    public DailyreportModel model;
    SessionManager sessionManager;
    String PartnerId;
    DailyreportAdapter tasklist_adapter;
    Toolbar toolbar;
    String currentdate,beforedate;
    String url;
    private SearchView searchView;
    private List<DailyreportModel> tasklist;
    TextView tv_task_count,tv_date;
    String days,dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailyreport);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Daily Report");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tv_task_count=findViewById(R.id.tv_closecount);
        sessionManager=new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        PartnerId=user.get(SessionManager.KEY_PartnerId);
        listView=findViewById(R.id.rv_dailyreport);
        tv_date=findViewById(R.id.tv_currentdate);
        tasklist = new ArrayList<>();
        // mAdapter = new ContactsAdapter(this, contactList, this);
        // white background notification bar
        //whiteNotificationBar(listView);
        tasklist_adapter=new DailyreportAdapter(this, tasklist, this);
        listView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
      //  listView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        listView.setAdapter(tasklist_adapter);

        try{

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            currentdate = df.format(c);

            Calendar calendar = Calendar.getInstance();
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DAY_OF_YEAR, - (day-1));
            Date newDate = calendar.getTime();
            beforedate=df.format(newDate);
            System.out.println("before-->"+beforedate);


            getdatas(PartnerId,"Closed");


            SimpleDateFormat format1=new SimpleDateFormat("dd-MMM-yyyy");
            Date dt1=Calendar.getInstance().getTime();
            DateFormat dayfrmat=new SimpleDateFormat("EEEE");
            DateFormat maothfor=new SimpleDateFormat("MMM yyyy");

            days= dayfrmat.format(dt1);
            dates=maothfor.format(dt1);


            tv_date.setText("Today "+days+" , " +dates);
        }catch (Exception e){


           e.printStackTrace();
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView();
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        // listening to search query text change
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // filter recycler view when query submitted
//                tasklist_adapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                // filter recycler view when text is changed
//                tasklist_adapter.getFilter().filter(query);
//                return false;
//            }
//        });
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        // close search view on back button pressed
//        if (!searchView.isIconified()) {
//            searchView.setIconified(true);
//            return;
//        }
//        super.onBackPressed();
//    }

    private void getdatas(String PartnerId,String sta) {

     //   url= AllUrl.baseUrl+"ServiceOrder/getServiceOrder?TechCode="+PartnerId+"&Status=Closed&FromDate="+currentdate+"&ToDate="+currentdate+"";
        url="https://sapsecurity.ifbhub.com/api/ServiceOrder/getServiceOrder?TechCode=11125074&ChangeDate=11-jul-2019";

    //    url= "https://sapsecurity.ifbhub.com/api/ServiceOrder/getServiceOrder?TechCode=11125074&Status=Closed&FromDate=08-Jul-2019&ToDate=08-Jul-2019";
        System.out.println(url);


        showProgressDialog();
        JsonArrayRequest request = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                       // System.out.println(response);

                        List<DailyreportModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<DailyreportModel>>() {
                        }.getType());

                        // adding contacts to contacts list
                        tasklist.clear();



                        tasklist.addAll(items);

                        tv_task_count.setText("You have closed "+items.size()+" task");

                        Collections.sort(tasklist, new Comparator<DailyreportModel>() {
                            public int compare(DailyreportModel p1, DailyreportModel p2) {

                                return String.valueOf(p1.getChangeDate()).compareTo(p2.getChangeDate());
                            }
                        });


                        // refreshing recycler view
                        tasklist_adapter.notifyDataSetChanged();
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "No data fount" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

    @Override
    public void onTaskSelected(DailyreportModel tasklistModel) {

    }
}
