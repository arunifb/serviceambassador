package technician.ifb.com.ifptecnician.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import technician.ifb.com.ifptecnician.MyApplication;
import technician.ifb.com.ifptecnician.MyDividerItemDecoration;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.TaskListActivity;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.alert.Alertwithicon;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.model.TasklistModel;

public class NotesActivity extends AppCompatActivity {


    private static final String TAG = TaskListActivity.class.getSimpleName();

    RecyclerView rv_notes;
    private List<NotesModel> notesModels;
    ProgressBar progressBar;
    NotesAdapter notesAdapter;
    String ticketno;
    LinearLayout ll_nointernet;

    TextView tv_ticketno;
    Alertwithicon alertwithicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Notes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ll_nointernet=findViewById(R.id.ll_nointernet);

        alertwithicon=new Alertwithicon();
        Intent intent=getIntent();

        ticketno=intent.getStringExtra("ticketno");

        tv_ticketno=findViewById(R.id.tv_ticketno);

        tv_ticketno.setText(ticketno);

        rv_notes=findViewById(R.id.rv_notes);
        progressBar=findViewById(R.id.taskProcessing);

        notesModels = new ArrayList<>();

        notesAdapter=new NotesAdapter(this, notesModels);
        rv_notes.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_notes.setLayoutManager(mLayoutManager);
        rv_notes.setItemAnimator(new DefaultItemAnimator());
        rv_notes.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        rv_notes.setAdapter(notesAdapter);

        getnotes(ticketno);
    }


    // ----------------   on back press section  -----------------------
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void getnotes( String ticketno) {


        if (CheckConnectivity.getInstance(NotesActivity.this).isOnline()) {


            progressBar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);

            String url=AllUrl.baseUrl+"notes/NoteByTicketId?model.TicktNo="+ticketno;
            // showProgressDialog();

            System.out.println(url);

            ll_nointernet.setVisibility(View.GONE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            try {



                                JSONObject jsonObject=new JSONObject(response);

                                System.out.println("Response-->"+response);


                                String status=jsonObject.getString("Status");

                                if (status.equals("true")){

                                    JSONArray jsonArray=jsonObject.getJSONArray("Data");

                                    List<NotesModel> items = new Gson().fromJson(jsonArray.toString(), new TypeToken<List<NotesModel>>() {
                                    }.getType());

                                    // adding contacts to contacts list


                                    notesModels.clear();
                                    notesModels.addAll(items);
                                    // refreshing recycler view
                                    notesAdapter.notifyDataSetChanged();


                                }

                                else {

                                    alertwithicon.showDialog(NotesActivity.this,"No Data","No Notes Data Found For Ticket No"+ticketno,false);

                                    //Toast.makeText(NotesActivity.this, "No data Found", Toast.LENGTH_SHORT).show();
                                }



                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                }
            });

            queue.add(stringRequest);

        }
        else{



            ll_nointernet.setVisibility(View.VISIBLE);


        }
    }








}