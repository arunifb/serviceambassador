package technician.ifb.com.ifptecnician;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanQRCodeForEssentialActivity extends AppCompatActivity {

    TextView btn_submit;
    String from;
    ImageView paytm_accepted;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcodefor_essential);

        from = getIntent().getStringExtra("from");

        paytm_accepted = findViewById(R.id.paytm_accepted);


        if("paytm".equals(from)){

            paytm_accepted.setImageResource(R.drawable.paytm);

        }else if("googlepay".equals(from)){

            paytm_accepted.setImageResource(R.drawable.ic_gpay);

        }



        btn_submit = findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ScanQRCodeForEssentialActivity.this,EnterReferenceCodeActivity.class ));

            }
        });

    }
}