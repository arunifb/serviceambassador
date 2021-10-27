package technician.ifb.com.ifptecnician;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.WorkManager;

import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.amc.Amclead;
import technician.ifb.com.ifptecnician.ecatalog.EcatalogFragment;
import technician.ifb.com.ifptecnician.essentiallead.EssentialLeadList;
import technician.ifb.com.ifptecnician.feedback.Feedback;
import technician.ifb.com.ifptecnician.fragment.AllLeadFragment;
import technician.ifb.com.ifptecnician.fragment.AppormentFragment;
import technician.ifb.com.ifptecnician.fragment.HomeFragment;
import technician.ifb.com.ifptecnician.fragment.NegativeResponseFragment;
import technician.ifb.com.ifptecnician.fragment.Offlinework;
import technician.ifb.com.ifptecnician.fragment.PieChartFragment;
import technician.ifb.com.ifptecnician.fragment.SearchFragment;
import technician.ifb.com.ifptecnician.fragment.TodayssalesFragment;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.language.SelectLanguage;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.offlinedata.OfflinedataService;
import technician.ifb.com.ifptecnician.session.SessionManager;


public class MainActivity extends AppCompatActivity
                implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    Dbhelper dbhelper;
    SessionManager sessionManager;
    TextView tv_username,tv_email,tv_mobileno,tv_rating,textCartItemCount;
    String name="",email="",imageurl="",mobile="",title="",FrCode="";
    NavigationView navigationView;
    DrawerLayout drawer;
    private int hot_number = 0;
    private TextView ui_hot = null;
    static TextView notifCount;
    static int mNotifCount = 0;
    String Component,ComponentDescription,FGDescription,FGProduct,FrCodes,MaterialCategory,good_stock,refurbished_stock;
    ImageView imageView,iv_star;
    LinearLayout ll_edit_details;
    SharedPreferences prefdetails;
    int mCartItemCount = 0;
    String TAG="main activity";
    FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbhelper=new Dbhelper(getApplicationContext());
        sessionManager=new SessionManager(getApplicationContext());
        prefdetails=getSharedPreferences("rating",0);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

      //  gettoken();

    // ---------------- open background service for submit all offline saved data -------------
//        if (!isMyServiceRunning()){
//
//            try{
//
//
//
//
//
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
////                    startForegroundService(new Intent(MainActivity.this, OfflinedataService.class));
////                } else {
////                    startService(new Intent(MainActivity.this, OfflinedataService.class));
////                }
//
//                Intent intent = new Intent(MainActivity.this, OfflinedataService.class);
//                startService(intent);
//
//            }catch (Exception e){
//
//             e.printStackTrace();
//            }
//
//
//        }

         try{


     // ----------------------  get all session value  from sessionManager -------------------------

             HashMap<String, String> user = sessionManager.getUserDetails();
             name=user.get(SessionManager.KEY_Name);
             email=user.get(SessionManager.KEY_EmailId);
             FrCode=user.get(SessionManager.KEY_FrCode);
             mobile=user.get(SessionManager.KEY_mobileno);
             imageurl=user.get(SessionManager.KEY_PROFILE_URL);

             mFirebaseAnalytics.setUserId(mobile);


             String PartnerId=user.get(SessionManager.KEY_PartnerId);
             System.out.println(name +"  "+email);
             getrating(PartnerId);

         }catch(Exception e){

             e.printStackTrace();
         }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        tv_username=hView.findViewById(R.id.tv_username);


        try{

        tv_username.setText(name);
        tv_email=hView.findViewById(R.id.tv_email);
        tv_email.setText(email);
        tv_mobileno=hView.findViewById(R.id.tv_mobileno);
        tv_mobileno.setText(mobile);
        tv_rating=hView.findViewById(R.id.tv_rating);
        iv_star=hView.findViewById(R.id.iv_star);

        }catch (Exception e){
            e.printStackTrace();
        }

        ll_edit_details=hView.findViewById(R.id.ll_edit_details);

        ll_edit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,Profile.class));
            }
        });


        // ---------------------  update profile picture image in slide menu -------------------------
        try{

            imageView=hView.findViewById(R.id.header_imageView);
            if (imageurl.equals("")){

            }
            else{



                Picasso.get()
                        .load(imageurl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                        .into(imageView);
            }
        }catch (Exception e){

         e.printStackTrace();

        }

//       -----------------------  go to Profile page for edit profile  --------------------------------

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,Profile.class));
            }
        });

        if (savedInstanceState == null) {
            // on first time to display view for first navigation item based on the number
            ShowFragment(R.id.nav_home);// 2 is your fragment's number for "CollectionFragment"
            navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        }


       Cursor offcursor;

        offcursor =dbhelper.get_offlineurl_data();

        if (offcursor.getCount() == 0){

            WorkManager workManager = WorkManager.getInstance(getApplicationContext());
            workManager.cancelAllWork();
        }

        else {


        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_notification);

        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.notification_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }


    private void setupBadge() {

        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        int id = item.getItemId();

        //  --------------------------- logout from app --------------------------------
        if (id == R.id.action_settings) {

            dbhelper.deletebom();
            dbhelper.deleteess();
            dbhelper.deletetask();
           // dbhelper.delete_readtask();
            dbhelper.delete_added_spare();
            dbhelper.delete_added_ess();
            dbhelper.delete_reqcancel_data();
            dbhelper.delete_softclose_data();
            dbhelper.delete_nr_data();
            dbhelper.delete_pending_data();
            dbhelper.delete_assign_data();
            sessionManager.logoutUser();

            finish();
            return true;
        }

        if (id == R.id.action_notification)
        {

           // startActivity(new Intent(MainActivity.this,NotificationView.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        ShowFragment(item.getItemId());

        return true;
    }


    // ---------------------------  calling all slide menu fragment -----------------------
    private void ShowFragment(int itemId) {

        Fragment fragment = null;

        switch (itemId) {
            // open dashboard fragment
            case R.id.nav_home:
                title="Dashboard";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                fragment = new HomeFragment();
                break;
            // open all ticket (assign and panding )
            case R.id.nav_task:
                title="Dashboard";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                startActivity(new Intent(MainActivity.this, TaskListActivity.class)
                        .putExtra("status","todaytask"));
                break;
            // open graph  fragment
            case R.id.nav_piechart:
                title="Graph View";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                fragment = new PieChartFragment();
                break;

                // open today appoitment
            case R.id.nav_apporment:
                title="Today&apos;s Appointment ";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                fragment = new AppormentFragment();
                break;

            // open negative response


            case R.id.nav_catalog:
                title="e-Catalogue";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                fragment = new EcatalogFragment();
                break;

            case R.id.nav_nresponse:
                title="Negative Response";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                fragment = new NegativeResponseFragment();
                break;

            case R.id.nav_lead:
                title="All Lead";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                fragment = new AllLeadFragment();
                break;

                // open search fragment

            case R.id.nav_search:
                title="Search";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                fragment = new SearchFragment();
                break;

            // open todaysales fragment
            case R.id. nav_todaysales:
                title="Today&apos;s Sale";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                fragment = new TodayssalesFragment();
                break;

            case R.id. nav_offlinedata:
                title="Offline Works";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                fragment = new Offlinework();
                break;

            case R.id.nav_amc:

                startActivity(new Intent(MainActivity.this, Amclead.class));

                break;
//
//            case R.id.nav_esseneial:
//
//                startActivity(new Intent(MainActivity.this, EssentialLeadList.class));
//
//                break;



            case R.id.nav_sales:

                startActivity(new Intent(MainActivity.this,SalesDetailsActivity.class));

                break;

            case R.id.nav_feedback:

                startActivity(new Intent(MainActivity.this, Feedback.class));
                break;

            case R.id.nav_share:

                title="Update Spare Value";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
               // getbom();
                break;

            case R.id.nav_send:
                title="Update Essential value";
                getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + title + "</font>"));
                getess();
                break;


//            case R.id.nav_settings:
//
//                startActivity(new Intent(MainActivity.this, SelectLanguage.class));
//                break;
        }


        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        setMenuCounter(R.id.nav_legal,0);
    }


    // ----------------------  update all essential

    public void  getess(){

        if ( CheckConnectivity.getInstance(this).isOnline()) {

            showProgressDialog();
            RequestQueue queue = Volley.newRequestQueue(this);

            String url= AllUrl.baseUrl+"addacc?addacc.FrCode="+FrCode;

            System.out.println("get all essential-->  "+ url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        System.out.println("ess details-->"+response);

                            try {
                                JSONArray jsonArray=new JSONArray(response);
                                System.out.println(jsonArray.toString());

                                if (dbhelper.isessdataEmpty()) {

                                    System.out.println("ess data not empty");

                                    if (dbhelper.deleteess()) {

                                        System.out.println("delete ess data");


                                        dbhelper.insert_ess_data("-- select essential --", "", "" , "0", "0", "", "");

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject essobj = jsonArray.getJSONObject(i);

//                                                    "ItemType": "ADT",
//                                                    "Component": "SR921ADFAB020",
//                                                    "ComponentDescription": "FABO STAIN REMOVER 180 ML SPRAY.",
//                                                    "accessories_stock": "0 ",
//                                                    "additives_stock": "0",
//                                                    "FrCode": "3002474"
                                            if(essobj.getString("accessories_stock").equals("0") && essobj.getString("additives_stock").equals("0")){

                                            }

                                            else {

                                                if (Boolean.valueOf(dbhelper.insert_ess_data(essobj.getString("ComponentDescription"), essobj.getString("Component"), "Z" + essobj.getString("ItemType"), essobj.getString("accessories_stock"), essobj.getString("additives_stock"), essobj.getString("ShelfLife"), "")).booleanValue()) {

                                                }
                                            }
                                        }
                                    }
                                       // startActivity(new Intent(MainActivity.this,CustomerDetailsActivity.class));
                                    }
                                else{

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject essobj = jsonArray.getJSONObject(i);

                                        if(essobj.getString("accessories_stock").equals("0") && essobj.getString("additives_stock").equals("0")){

                                        }
                                        else {

                                            if (Boolean.valueOf(dbhelper.insert_ess_data(essobj.getString("ComponentDescription"),essobj.getString("Component"),"Z"+essobj.getString("ItemType"),essobj.getString("accessories_stock"),essobj.getString("additives_stock"),essobj.getString("ShelfLife"),"")).booleanValue())
                                            {
                                                System.out.println("offline data save"+ i);
                                            }
                                        }

                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            hideProgressDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
// Add the request to the RequestQueue.


            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        }

        else {

          // if no internet

        }
    }

    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    static abstract class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
        private String hint;
        private View view;

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        abstract public void onClick(View v);

        @Override
        public boolean onLongClick(View v) {
            final int[] screenPos = new int[2];
            final Rect displayFrame = new Rect();
            view.getLocationOnScreen(screenPos);
            view.getWindowVisibleDisplayFrame(displayFrame);
            final Context context = view.getContext();
            final int width = view.getWidth();
            final int height = view.getHeight();
            final int midy = screenPos[1] + height / 2;
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
                        screenWidth - screenPos[0] - width / 2, height);
            } else {
                cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
            }
            cheatSheet.show();
            return true;
        }
    }

    private void setNotifCount(int count){
        mNotifCount = count;
        invalidateOptionsMenu();
    }


    // -----------------------  set app version in left slider menu -----------------------------------

    private void setMenuCounter(@IdRes int itemId, int count) {
        TextView view = (TextView) navigationView.getMenu().findItem(itemId).getActionView();
       // view.setText(count > 0 ? String.valueOf(count) : null);
        String version=AllUrl.APP_Version;

    }



    // ---------------------------  get technician rating ------------------------------------------

    public void getrating(String Partnerid) {

        if (CheckConnectivity.getInstance(this).isOnline()) {


            RequestQueue queue = Volley.newRequestQueue(this);

            String url = AllUrl.baseUrl +"ratings/getRating?model.TechnicianRating.Partnerid="+Partnerid;

            System.out.println("get all sales-->  " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("ess details-->" + response);

                            try {

                                // after geting response
                               JSONObject object=new JSONObject(response);

                               String status=object.getString("Status");
                               String Message=object.getString("Message");
                               if (status.equals("true")){


                                   JSONArray jsonArray = new JSONArray(object.getString("Data"));
                                   System.out.println(jsonArray.toString());
                                   for (int i = 0; i < jsonArray.length(); i++) {

                                       JSONObject essobj = jsonArray.getJSONObject(i);
                                       String rating=essobj.getString("Rating");
                                       // update rating value in left mennu  header section

                                       iv_star.setVisibility(View.VISIBLE);
                                       tv_rating.setText(rating);
                                       // save data into shared preferences if need other place
                                       SharedPreferences.Editor editor=prefdetails.edit();
                                       editor.putString("rating",rating);
                                       editor.commit();

                                   }
                               }
                               else{

                                   // no api response false hide ratting section
                                  iv_star.setVisibility(View.GONE);
                               }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // no api response false hide ratting section

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            iv_star.setVisibility(View.GONE);

                        }
                    });

                   }
            });

            // for slow network retry after 10 sec
            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        } else {

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        }


    // ---------------------------  check the service is running or not -------------------------

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (OfflinedataService.class.getName().equals(service.service.getClassName())) {

                return true;
            }
        }

        return false;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbhelper.close();
    }


    public void gettoken(){

//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//
//                        System.out.println("Token--->"+token);
//
//                        // Log and toast
////                        String msg = getString(R.string.msg_token_fmt, token);
////                        Log.d(TAG, msg);
//                       // Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
}
