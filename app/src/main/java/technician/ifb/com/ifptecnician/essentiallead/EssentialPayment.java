package technician.ifb.com.ifptecnician.essentiallead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class EssentialPayment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LinearLayout ll_checque;
    String  paymenttype="Cash";
    Spinner spnr_icr_type,spnr_payment_type;
    Toolbar toolbar;
    String [] icr_type_array={"-- select ICR type --","Franchises ICR","DSO ICR"};
    String [] payment_type_array={"-- select payment type --","Essential Payment","Service Payment"};
    double tot_amount=0;
    TextView tv_tot_amount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essential_payment);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Payment");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tv_tot_amount=findViewById(R.id.tv_tot_amount);



        Intent in = getIntent();

        tot_amount=in.getDoubleExtra("tot_amount",0);

        tv_tot_amount.setText("\u20B9 "+tot_amount);

        ll_checque=findViewById(R.id.ll_checque);

        spnr_icr_type=findViewById(R.id.spnr_icr_type);
        spnr_payment_type=findViewById(R.id.spnr_payment_type);


        ArrayAdapter product_adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, icr_type_array);
        product_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_icr_type.setAdapter(product_adapter);
        spnr_icr_type.setOnItemSelectedListener(this);



        ArrayAdapter payment_type_adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, payment_type_array);
        product_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnr_payment_type.setAdapter(payment_type_adapter);
        spnr_payment_type.setOnItemSelectedListener(this);

    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_cash:
                if (checked)

                ll_checque.setVisibility(View.GONE);
                paymenttype="cash";

                break;
            case R.id.radio_cheque:
                if (checked)
                paymenttype="cheque";
                ll_checque.setVisibility(View.VISIBLE);
                break;

            case R.id.radio_online:
                if (checked)
                paymenttype="online";
                ll_checque.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void submitdata(View v){

    }

    public void VerifyIcr(View v){


    }


    public  void openicrcamera(){


    }
}