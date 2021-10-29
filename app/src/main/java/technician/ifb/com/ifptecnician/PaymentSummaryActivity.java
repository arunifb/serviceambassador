package technician.ifb.com.ifptecnician;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentSummaryActivity extends AppCompatActivity {

   TextView txt_pay;
   RadioGroup payment_radiogroup;
   RadioButton radio_cash;
   RadioButton radio_paytm;
   RadioButton radio_gpay;
    boolean cash = false;
    boolean paytm = false;
    boolean gpay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_summary);
        txt_pay = findViewById(R.id.txt_pay);
        payment_radiogroup = (RadioGroup)findViewById(R.id.payment_radiogroup);
        radio_cash = (RadioButton)payment_radiogroup.findViewById(R.id.radio_cash);
        radio_paytm= (RadioButton)payment_radiogroup.findViewById(R.id.radio_paytm);
        radio_gpay = (RadioButton)payment_radiogroup.findViewById(R.id.radio_gpay);


        payment_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
                // This puts the value (true/false) into the variable
               //isChecked = checkedRadioButton.isChecked();

               // Log.d("checked_id",String.valueOf(isChecked));

                switch(checkedId){


                    case -1:


                        break;

                    case R.id.radio_cash:

                        cash = true;
                        paytm = false;
                        gpay = false;

                        Log.d("cash mode","cash");

                        break;

                    case R.id.radio_paytm:

                        cash = false;
                        paytm = true;
                        gpay = false;

                        Log.d("cash mode","paytm");

                        break;

                    case R.id.radio_gpay:

                        cash = false;
                        paytm = false;
                        gpay = true;

                        Log.d("cash mode","Google pay");

                        break;

                }


            }
        });





        txt_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = payment_radiogroup.getCheckedRadioButtonId();


                if(cash){
                    Toast.makeText(PaymentSummaryActivity.this,"Cash selected", Toast.LENGTH_SHORT).show();
                }
                else if(paytm){
                    //Toast.makeText(PaymentSummaryActivity.this, payment_radiogroup.getText(), Toast.LENGTH_SHORT).show();

                  startActivity(new Intent(PaymentSummaryActivity.this,ScanQRCodeForEssentialActivity.class));

                }else if(gpay){

                    startActivity(new Intent(PaymentSummaryActivity.this,ScanQRCodeForEssentialActivity.class));

                }

            }
        });
    }
}