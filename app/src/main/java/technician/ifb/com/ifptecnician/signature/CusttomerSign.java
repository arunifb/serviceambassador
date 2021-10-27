package technician.ifb.com.ifptecnician.signature;

import androidx.appcompat.app.AppCompatActivity;
import technician.ifb.com.ifptecnician.R;

import android.os.Bundle;

import com.williamww.silkysignature.views.SignaturePad;

public class CusttomerSign extends AppCompatActivity {


    SignaturePad mSignaturePad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custtomer_sign);

        mSignaturePad = (SignaturePad) findViewById(R.id.signature_pad);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

            }

            @Override
            public void onSigned() {
//Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
//Event triggered when the pad is cleared
            }



        });

    }
}