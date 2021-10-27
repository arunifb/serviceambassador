package technician.ifb.com.ifptecnician;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import technician.ifb.com.ifptecnician.essentiallead.EssentialModel;
import technician.ifb.com.ifptecnician.essentiallead.essentialmodel.EssentialList;

public class EssentialsDetailsActivity extends AppCompatActivity {

    TextView tv_custname, tv_callbookdate,tv_ticketno,tv_address,tv_status,txt_franchise,txt_branch,tv_calltype,tv_customer_no;
    EssentialList essentialModel;

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

        essentialModel= (EssentialList) getIntent().getSerializableExtra("essentials_details");

        tv_custname.setText(essentialModel.getSoldToPartyList());
        tv_ticketno.setText(essentialModel.getSoldToParty());
        tv_address.setText(essentialModel.getAddress());
        tv_status.setText(essentialModel.getConcatstatuser());
        txt_franchise.setText("Franchise: "+essentialModel.getFranchise());
        txt_branch.setText("Branch :"+essentialModel.getBranch());
        tv_calltype.setText(essentialModel.getProcessTypeTxt());
        tv_customer_no.setText(essentialModel.getCallerNo());




    }
}