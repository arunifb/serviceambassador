package technician.ifb.com.ifptecnician.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.customspinner.SearchableSpinner;

public class TextActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SearchableSpinner spinner;
    ArrayList<String> strings=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        spinner=findViewById(R.id.test_spnr);

        strings=new ArrayList<>();

        strings.add("pincode 100100");
        strings.add("essential list 123456");

        strings.add("pincode 100100");
        strings.add("essential list 123456");

        strings.add("pincode 100100");
        strings.add("essential list 123456");

        strings.add("pincode 100100");
        strings.add("essential list 123456");
        strings.add("pincode 100100");
        strings.add("essential list 123456");
        strings.add("pincode 100100");
        strings.add("essential list 123456");
        strings.add("pincode 100100");
        strings.add("essential list 123456");
        strings.add("pincode 100100");
        strings.add("essential list 123456");
        strings.add("pincode 100100");
        strings.add("essential list 123456");
        strings.add("pincode 100100");
        strings.add("essential list 123456");
        strings.add("pincode 100100");
        strings.add("essential list 123456");


        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item,strings);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);;
        spinner.setAdapter(arrayAdapter);

       spinner.setOnItemSelectedListener(this);



    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}