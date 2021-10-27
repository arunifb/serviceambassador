package technician.ifb.com.ifptecnician;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import technician.ifb.com.ifptecnician.adapter.NotificationAdapter;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.model.NoticationModel;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class NotificationView extends AppCompatActivity implements NotificationAdapter.JobsAdapterListener{

    String notificationvalue;

    private static final String TAG = NotificationView.class.getSimpleName();

    RecyclerView listView;
    public NoticationModel model;
    SessionManager sessionManager;
    String PartnerId;
    NotificationAdapter tasklist_adapter;
    Toolbar toolbar;
    String url;
    private SearchView searchView;
    private List<NoticationModel> tasklist;
    Dbhelper dbhelper;
    TextView tv_date,tv_ageing,tv_calltype;

    private BroadcastReceiver mNetworkReceiver;
    static TextView tv_check_connection;
    ErrorDetails errorDetails=new ErrorDetails();
    int dtsort_clk_cunt=0,ageing_sort_clk_count=0;
    String mobile_details;
    SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeView;
    //SwipeRefreshLayout swipeRefreshLayout;
    boolean refreshToggle = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        notificationvalue=getIntent().getExtras().getString("model");
        System.out.println(notificationvalue);
        listView=findViewById(R.id.rv_notifiaction);

        tasklist = new ArrayList<>();
        tasklist_adapter=new NotificationAdapter(this, tasklist, this);
        listView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        listView.setAdapter(tasklist_adapter);
        updatelist();

    }


    public void updatelist(){

        List<NoticationModel> items = new Gson().fromJson(notificationvalue.toString(), new TypeToken<List<NoticationModel>>() {
        }.getType());

        // adding contacts to contacts list
        tasklist.clear();
        tasklist.addAll(items);
        // refreshing recycler view
        tasklist_adapter.notifyDataSetChanged();

    }

    @Override
    public void onTaskSelected(NoticationModel NoticationModel) {

    }
}
