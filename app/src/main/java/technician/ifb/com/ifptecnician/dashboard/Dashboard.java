package technician.ifb.com.ifptecnician.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class Dashboard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Toolbar toolbar;
    Spinner spinner;
   SessionManager sessionManager;
   String name,profileurl="";
   CircularImageView circularImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.toolbar);


       sessionManager=new SessionManager(getApplicationContext());
       circularImageView=findViewById(R.id.civ_image);


       try{

           HashMap<String,String> map=sessionManager.getUserDetails();
           name=map.get(SessionManager.KEY_Name);
           profileurl=map.get(SessionManager.KEY_PROFILE_URL);
           toolbar.setTitle(name);
           setSupportActionBar(toolbar);

               if (profileurl.equals("")){

               }
               else{

                   Picasso.get()
                           .load(profileurl)
                           .into(circularImageView);
               }





       }catch (Exception e){

           e.printStackTrace();
       }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashbord_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) item.getActionView();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_language, R.layout.menu_spinner_item);
        adapter.setDropDownViewResource(R.layout.menu_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        String itemText = (String)  spinner.getSelectedItem();

        System.out.println("itemText--> "+itemText);
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if (view.getId() == R.id.spinner){

            String itemText = (String)  spinner.getSelectedItem();

            System.out.println("itemText--> "+itemText);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}