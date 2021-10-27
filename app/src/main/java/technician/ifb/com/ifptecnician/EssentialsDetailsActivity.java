package technician.ifb.com.ifptecnician;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

import technician.ifb.com.ifptecnician.adapter.AddMoreEssentialAdapter;
import technician.ifb.com.ifptecnician.adapter.EssentialListAdapter;
import technician.ifb.com.ifptecnician.essentiallead.EssentialModel;
import technician.ifb.com.ifptecnician.essentiallead.essentialmodel.EssentialList;

public class EssentialsDetailsActivity extends AppCompatActivity {

    TextView tv_custname, tv_callbookdate,tv_ticketno,tv_address,tv_status,txt_franchise,txt_branch,tv_calltype,tv_customer_no,txt_zzproduct;
    EssentialList essentialModel;
    SearchableSpinner searchablespinner;
    List<String> searchablelist = new ArrayList<String>();
    RecyclerView rc_essentiallist;
    String searchable_spinner_item;
    ImageView add_essentials;
    List<String> addedlist = new ArrayList<>();
    private AddMoreEssentialAdapter addMoreEssentialAdapter;
    Button add_more;
    RelativeLayout add_text_layout,serachable_spinner_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essentialsdetails);
        tv_custname = findViewById(R.id.tv_custname);
        tv_callbookdate = findViewById(R.id.tv_callbookdate);
        tv_ticketno = findViewById(R.id.tv_ticketno);
        tv_address = findViewById(R.id.tv_address);
        tv_status = findViewById(R.id.tv_status);
        txt_franchise = findViewById(R.id.txt_franchise);
        txt_branch = findViewById(R.id.txt_branch);
        tv_calltype = findViewById(R.id.tv_calltype);
        tv_customer_no = findViewById(R.id.tv_customer_no);
        txt_zzproduct = findViewById(R.id.txt_zzproduct);
        add_more = findViewById(R.id.add_more);
        add_text_layout = findViewById(R.id.add_text_layout);
        serachable_spinner_layout = findViewById(R.id.serachable_spinner_layout);


        add_essentials = findViewById(R.id.add_essentials);

        searchablespinner = (SearchableSpinner) findViewById(R.id.search_essential);

        rc_essentiallist = (RecyclerView)findViewById(R.id.rc_essentiallist);

        essentialModel= (EssentialList) getIntent().getSerializableExtra("essentials_details");

        tv_custname.setText(essentialModel.getSoldToPartyList());
        tv_ticketno.setText(essentialModel.getSoldToParty());
        tv_address.setText(essentialModel.getAddress());
        tv_status.setText(essentialModel.getConcatstatuser());
        txt_franchise.setText("Franchise: "+essentialModel.getFranchise());
        txt_branch.setText("Branch :"+essentialModel.getBranch());
        tv_calltype.setText(essentialModel.getProcessTypeTxt());
        tv_customer_no.setText(essentialModel.getCallerNo());
        txt_zzproduct.setText(essentialModel.getZzproductDesc());

        searchablelist.add(essentialModel.getZzproductDesc());

        searchablespinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, searchablelist));


        if (addedlist.size()>0) {

            addMoreEssentialAdapter = new AddMoreEssentialAdapter(EssentialsDetailsActivity.this, addedlist);
            rc_essentiallist.setLayoutManager(new LinearLayoutManager(EssentialsDetailsActivity.this));

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            rc_essentiallist.setLayoutManager(mLayoutManager);
            rc_essentiallist.setItemAnimator(new DefaultItemAnimator());
            rc_essentiallist.addItemDecoration(new MyDividerItemDecoration(EssentialsDetailsActivity.this, DividerItemDecoration.VERTICAL, 36));
            rc_essentiallist.setAdapter(addMoreEssentialAdapter);

        }


        searchablespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {

                searchable_spinner_item = parent.getItemAtPosition(pos).toString();

                Log.d("search_item",searchable_spinner_item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {




            }
        });


        add_essentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addedlist.add(searchable_spinner_item);

                addMoreEssentialAdapter = new AddMoreEssentialAdapter(EssentialsDetailsActivity.this, addedlist);
                rc_essentiallist.setLayoutManager(new LinearLayoutManager(EssentialsDetailsActivity.this));

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                rc_essentiallist.setLayoutManager(mLayoutManager);
                rc_essentiallist.setItemAnimator(new DefaultItemAnimator());
                rc_essentiallist.setAdapter(addMoreEssentialAdapter);


            }
        });

       /* add_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (add_more.getText().equals("Add More")) {

                    add_text_layout.setVisibility(View.VISIBLE);
                    serachable_spinner_layout.setVisibility(View.VISIBLE);
                    add_more.setText("Close");

                }else if(add_more.getText().equals("Close")){

                    add_text_layout.setVisibility(View.GONE);
                    serachable_spinner_layout.setVisibility(View.GONE);
                    add_more.setText("Add More");

                }

            }
        });*/




    }
}