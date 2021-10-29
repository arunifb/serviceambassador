package technician.ifb.com.ifptecnician;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScanQRCodeForEssentialActivity extends AppCompatActivity {

    TextView btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcodefor_essential);

        btn_submit = findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ScanQRCodeForEssentialActivity.this,EnterReferenceCodeActivity.class ));

            }
        });

    }
}