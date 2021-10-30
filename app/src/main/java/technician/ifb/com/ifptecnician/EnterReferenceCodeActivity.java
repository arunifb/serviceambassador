package technician.ifb.com.ifptecnician;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EnterReferenceCodeActivity extends AppCompatActivity {

    TextView btn_submit_reference_code;
    EditText et_referencecode;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_reference_code);

        btn_submit_reference_code = findViewById(R.id.btn_submit_reference_code);

        et_referencecode = findViewById(R.id.et_referencecode);
        img_back = findViewById(R.id.img_back);


        btn_submit_reference_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isValidatingTrue()){

                    AlertDialog alertDialog = new AlertDialog.Builder(EnterReferenceCodeActivity.this).create();
                    alertDialog.setTitle("Message");
                    alertDialog.setMessage("you have submitted reference code successfully");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    //Toast.makeText(EnterReferenceCodeActivity.this,"",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(EnterReferenceCodeActivity.this, EssentialListActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            });
                    alertDialog.show();





                }

            }
        });



        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });




    }

    private boolean isValidatingTrue() {

        if (et_referencecode.getText().equals("")){

            et_referencecode.setError("Enter request Code");

            return  false;

        }

        return  true;

    }
}