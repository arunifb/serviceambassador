package technician.ifb.com.ifptecnician.language;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import technician.ifb.com.ifptecnician.MainActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;

public class SelectLanguage extends AppCompatActivity {

    Context context;
    Resources resources;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Language");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void hin(View v){

        SharedPreferences sharedPref = getSharedPreferences(AllUrl.LENGUAGE,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lenguage", "hi");
        editor.apply();


        startActivity(new Intent(SelectLanguage.this, MainActivity.class));
    }


    public void eng(View v){

        SharedPreferences sharedPref = getSharedPreferences(AllUrl.LENGUAGE,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lenguage", "");
        editor.apply();

        startActivity(new Intent(SelectLanguage.this, MainActivity.class));

    }

    public void tel(View v){

        SharedPreferences sharedPref = getSharedPreferences(AllUrl.LENGUAGE,0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lenguage", "te");
        editor.apply();

        startActivity(new Intent(SelectLanguage.this, MainActivity.class));
    }
}