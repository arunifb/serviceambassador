package technician.ifb.com.ifptecnician;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.alert.Alert;

public class UpdateAddress extends AppCompatActivity implements View.OnClickListener{

    String Street,City,State,address,PinCode;
    EditText et_Street,et_City,et_State,et_address,et_PinCode;
    Button btn_update;
    SharedPreferences prefdetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        prefdetails = getSharedPreferences("details", 0);

        init();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void init(){

        et_Street=findViewById(R.id.update_et_street);
        et_City=findViewById(R.id.update_et_city);
        et_State=findViewById(R.id.update_et_state);
        et_address=findViewById(R.id.update_et_fulladdress);
        et_PinCode=findViewById(R.id.update_et_pincode);
        btn_update=findViewById(R.id.update_btn_address);

        btn_update.setOnClickListener(this);

        getaddress();
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.update_btn_address){

            Alert  alert=new Alert();
            alert.showDialog(UpdateAddress.this,"Update Status ","Update Will be done After Verification");
        }

    }

    public void getaddress(){


            try {

                prefdetails = getSharedPreferences("details", 0);
                String details = prefdetails.getString("details", "");


                JSONObject object1 = new JSONObject(details);

                Street= object1.getString("Street");
                City= object1.getString("City");
                State= object1.getString("State");
                address= object1.getString("Address");
                PinCode= object1.getString("PinCode");

                et_Street.setText(Street);
                et_City.setText(City);
                et_State.setText(State);
                et_address.setText(address);
                et_PinCode.setText(PinCode);

            }
            catch (Exception e){

                e.printStackTrace();
            }
        }


    public static void hideSoftKeyboard(Activity activity) {

        try {

            if (activity != null) {
                InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (manager != null) {
                    manager.hideSoftInputFromWindow(activity.findViewById(android.R.id.content).getWindowToken(), 0);
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
