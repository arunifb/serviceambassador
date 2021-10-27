package technician.ifb.com.ifptecnician;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.otp.EnterMobile;
import technician.ifb.com.ifptecnician.profile.AddVaccineDetails;
import technician.ifb.com.ifptecnician.profile.UpdateProfile;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class Profile extends AppCompatActivity implements View.OnClickListener{


    ImageView ll_update,ll_changepassword;
    SessionManager sessionManager;
    String name,email,mobile,FrCode;
    TextView tv_mobileno,tv_name,tv_email,tv_blood,tv_aadhar,tv_pan,tv_partnerid;
    CircularImageView civ_pimage;
    private BottomSheetBehavior sheetBehavior;
    LinearLayout bottom_sheet;
    public static final int PROFILE_IMAGE_CAPTURE = 21;
    public static final int PROFILE_IMAGE_GALLERY= 22;
    LinearLayout ll_open_camera,ll_open_gallery,ll_main_photo;
    TextView tv_photo_cancel;
    BottomSheetDialog dialoggg;
    ProgressBar progressBar;
    Dbhelper dbhelper;

    TextView tv_vac_name,tv_vac_type,tv_vac_ist_date,tv_snd_date,tv_location;

    int serverResponseCode = 0;

    /**********  File Path *************/
    final String uploadFilePath="" ;
    final String uploadFileName ="";

    private String upload_URL=AllUrl.baseUrl+"ProfileImageUpload/ImageUpload";
    String PartnerId, EmailId, Name, passsword, mobileno,subscriptionIds,imageurl="";
    //"https://crmapi.ifbhub.com/api/doc/uploaddoc?model.AppDocUpload.TicketNo="+TicketNo+"&model.AppDocUpload.SerialPhoto="+ seraialno_image_data+"&model.AppDocUpload.ODUSerialPhoto="+oduserialno_image_data+"&model.AppDocUpload.InvoicePhto="+invoice_image_data+"&model.AppDocUpload.UploadDate="+tss+"";
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;
    ProgressBar progressbar;
    TextView  tv_error;
    String alldata,blood,pan,aadhar;
    SharedPreferences preferences;
    LinearLayout ll_nointernet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        preferences=getSharedPreferences("profile",0);

        tv_error=findViewById(R.id.tv_error_message);
        ll_nointernet=findViewById(R.id.ll_nointernet);
        ll_changepassword=findViewById(R.id.ll_changepassword);
        progressBar=findViewById(R.id.taskProcessing);
        tv_partnerid=findViewById(R.id.tv_partnerid);
        ll_changepassword.setOnClickListener(this);

        dbhelper=new Dbhelper(getApplicationContext());

        ll_update=findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);

        progressbar=findViewById(R.id.taskProcessing);

        sessionManager=new SessionManager(getApplicationContext());

        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            name=user.get(SessionManager.KEY_Name);
            email=user.get(SessionManager.KEY_EmailId);
            mobile=user.get(SessionManager.KEY_mobileno);
            FrCode=user.get(SessionManager.KEY_FrCode);
            passsword=user.get(SessionManager.KEY_passsword);
            imageurl=user.get(SessionManager.KEY_PROFILE_URL);
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            alldata=user.get(SessionManager.KEY_ALL_DATA);
            tv_partnerid.setText(user.get(SessionManager.KEY_PartnerId));

        }catch (Exception e){

            e.printStackTrace();
        }

        try{

            JSONObject jsonObject=new JSONObject(alldata);
            blood=jsonObject.getString("BloodGroup");
            aadhar=jsonObject.getString("AadharNo");
            pan=jsonObject.getString("PANNo");

        }catch (Exception e){

            e.printStackTrace();

        }


        System.out.println(name +"  "+email);

        civ_pimage=findViewById(R.id.civ_pimage);
        civ_pimage.setOnClickListener(this);

        try {

            if (imageurl.equals("")){

            }
            else{

                       Picasso.get()
                        .load(imageurl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                        .into(civ_pimage);
            }

        }
        catch (Exception e){


            e.printStackTrace();
        }

        tv_mobileno=findViewById(R.id.tv_mobileno);
        tv_name=findViewById(R.id.tv_name);
        tv_email=findViewById(R.id.tv_email);
        tv_blood=findViewById(R.id.tv_blood);
        tv_aadhar=findViewById(R.id.tv_aadhar);
        tv_pan=findViewById(R.id.tv_panno);

        tv_vac_name=findViewById(R.id.tv_vaccine_name);
        tv_vac_type=findViewById(R.id.tv_vac_type);
        tv_vac_ist_date=findViewById(R.id.tv_ist_date);
        tv_snd_date=findViewById(R.id.tv_snd_date);
        tv_location=findViewById(R.id.tv_location);

        tv_mobileno.setText(mobile);
        tv_name.setText(name);
        tv_email.setText(email);

        tv_aadhar.setText(aadhar);
        tv_blood.setText(blood);
        tv_pan.setText(pan);


        dialoggg = new BottomSheetDialog(this);



        UserLogin();

        getvaccinedetails();

    }

//    public void getprofile(){
//
//        if (CheckConnectivity.getInstance(this).isOnline()) {
//            ll_nointernet.setVisibility(View.GONE);
//            progressBar.setVisibility(View.VISIBLE);
//            RequestQueue queue = Volley.newRequestQueue(this);
//            String url = AllUrl.baseUrl+"sa-profile/getProfile/?user.Mobileno=" + mobile;
//
//
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            progressBar.setVisibility(View.GONE);
//
//                            try {
//
//                                JSONObject jsonObject=new JSONObject(response);
//
//                                System.out.println("Response-->"+response);
//
//
//                                String status=jsonObject.getString("Status");
//
//                                if (status.equals("true")){
//
//                                    JSONArray jsonArray=jsonObject.getJSONArray("Data");
//
//
//                                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                                        JSONObject obj = jsonArray.getJSONObject(i);
//
//                                        PartnerId = obj.getString("PartnerId");
//                                        if (PartnerId.equals("")) {
//
//                                        } else {
//                                            FrCode = obj.getString("FrCode");
//                                            EmailId = obj.getString("EmailId");
//                                            Name = obj.getString("Name");
//                                            passsword = obj.getString("Password");
//                                            mobileno = obj.getString("Mobileno");
//                                            String url=obj.getString("URL");
//                                            pan=obj.getString("PANNo");
//                                            aadhar=obj.getString("AadharNo");
//                                            blood=obj.getString("BloodGroup") ;
//                                            sessionManager.createLoginSession(PartnerId, FrCode, EmailId, Name, passsword, mobileno,url,obj.toString(),pan,aadhar,blood);
//                                            tv_aadhar.setText(aadhar);
//                                            tv_blood.setText(blood);
//                                            tv_pan.setText(pan);
//
//                                        }
//
//                                    }
//
//                                }
//
//
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//
//                            }
//
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    progressbar.setVisibility(View.GONE);
//                 }
//            });
//// Add the request to the RequestQueue.
//            queue.add(stringRequest);
//
//        } else {
//
//            ll_nointernet.setVisibility(View.VISIBLE);
//        }
//
//    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ll_changepassword:
                startActivity(new Intent(Profile.this, EnterMobile.class)
                .putExtra("type","changepassword"));



                break;

            case R.id.ll_update:
                startActivity(new Intent(Profile.this, UpdateProfile.class));


                break;

            case R.id.civ_pimage:

                showBottomSheetDialog();
             //   pickFromGallery();
                break;

            case  R.id.ll_open_camera:
                launchCameraIntent();
                dialoggg.dismiss();

                break;
            case  R.id.ll_open_gallery:
                pickFromGallery();
                dialoggg.dismiss();

                break;
            case R.id.tv_photo_cancel:
               // ll_main_photo.setVisibility(View.GONE);
                dialoggg.dismiss();
                break;


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(Profile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);
        startActivityForResult(intent, PROFILE_IMAGE_CAPTURE);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PROFILE_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");
                    try {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        // Bitmap converetdImage = getResizedBitmap(bitmap, 80);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                        byte [] profile_image_data= byteArrayOutputStream.toByteArray();
                        civ_pimage.setImageBitmap(bitmap);


                        if (CheckConnectivity.getInstance(Profile.this).isOnline()) {

                            uploadImage(bitmap);
                        }
                        else {
                            showpop("No internet connection!!");

                        }



                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case PROFILE_IMAGE_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getParcelableExtra("path");
                    try {


                        System.out.println("uri-->"+uri);
                       // upload(uri);

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        // Bitmap converetdImage = getResizedBitmap(bitmap, 80);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                        byte [] profile_image_data= byteArrayOutputStream.toByteArray();
                        civ_pimage.setImageBitmap(bitmap);

                        uploadImage(bitmap);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    private void pickFromGallery() {

        Intent intent = new Intent(Profile.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,1);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);
        startActivityForResult(intent, PROFILE_IMAGE_GALLERY);
    }


    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.photo_popup, null);

        dialoggg.setContentView(view);
        ll_open_camera=dialoggg.findViewById(R.id.ll_open_camera);
        ll_open_camera.setOnClickListener(this);
        ll_open_gallery=dialoggg.findViewById(R.id.ll_open_gallery);
        ll_open_gallery.setOnClickListener(this);
        ll_main_photo=dialoggg.findViewById(R.id.ll_main_photo);

        tv_photo_cancel=dialoggg.findViewById(R.id.tv_photo_cancel);
        tv_photo_cancel.setOnClickListener(this);

        dialoggg.show();
    }


    private void uploadImage(final Bitmap bitmap){

        progressbar.setVisibility(View.VISIBLE);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        System.out.println(response);
                        Log.d("ressssssoo",new String(response.data));
                        rQueue.getCache().clear();

                        System.out.println(response.statusCode);
                        progressbar.setVisibility(View.GONE);

                        if (response.statusCode==200){

                            showpop("Profile picture upload successfully done ");
                            UserLogin();
                        }
                        else {

                            showpop("Please try again later");
                         //   Toast.makeText(Profile.this, "Please Try Again ...", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressbar.setVisibility(View.GONE);
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("PartnerId", PartnerId); // add string parameters
                params.put("ClientDocs", "ClientDocs");
                System.out.println(params);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("test", new DataPart(imagename + ".jpeg", getFileDataFromDrawable(bitmap)));
                System.out.println(params);

                return params;
            }
        };


        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(Profile.this);
        rQueue.add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void UserLogin() {


        if (CheckConnectivity.getInstance(this).isOnline()) {

            progressbar.setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(this);
            //String url = AllUrl.loginUrl+"?url=http://centurionbattery.in/development&username="+username+"&password="+password;
            String url = AllUrl.baseUrl+"user/validateuser?mobileno=" + mobile + "&password=" + passsword;

            System.out.println("get all sales-->  " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressbar.setVisibility(View.GONE);

                                try {

                                    JSONArray jsonArray = new JSONArray(response);


                                    System.out.println(response);

                                    //  if (object.get(get"Message"))
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject obj = jsonArray.getJSONObject(i);

                                        PartnerId = obj.getString("PartnerId");
                                        if (PartnerId.equals("")) {

                                            } else {
                                            FrCode = obj.getString("FrCode");
                                            EmailId = obj.getString("EmailId");
                                            Name = obj.getString("Name");
                                            passsword = obj.getString("Password");
                                            mobileno = obj.getString("Mobileno");
                                            String url=obj.getString("URL");
                                            String AadharNo=obj.getString("AadharNo");
                                            String blood =obj.getString("BloodGroup");
                                            String PANNo=obj.getString("PANNo");
                                            String RegionCode=obj.getString("RegionCode");
                                            sessionManager.createLoginSession(PartnerId, FrCode, EmailId, Name, passsword, mobileno,url,obj.toString(),RegionCode,PANNo,AadharNo,blood);
                                            tv_aadhar.setText(AadharNo);
                                            tv_blood.setText(blood);
                                            tv_pan.setText(PANNo);
                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressbar.setVisibility(View.GONE);

                }
            });

            queue.add(stringRequest);

        } else {


        }
    }



    public void showpop(String text){

        tv_error.setText(text);
        tv_error.setVisibility(View.VISIBLE);
        Animation animation;
        animation = AnimationUtils.loadAnimation(Profile.this, R.anim.slide_down);
        tv_error.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                tv_error.setAnimation(null);
                tv_error.setVisibility(View.GONE);
            }
        }, 5000);
    }


    public void logoutuser(View v){

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
        finishAffinity();

    }

    public void openphoto(View v){

        showBottomSheetDialog();
    }


    public void UpdateVaccine(View v){

        startActivity(new Intent(Profile.this, AddVaccineDetails.class));
    }


    public void getvaccinedetails(){


        if (CheckConnectivity.getInstance(this).isOnline()) {


            RequestQueue queue = Volley.newRequestQueue(this);
            //String url = AllUrl.loginUrl+"?url=http://centurionbattery.in/development&username="+username+"&password="+password;
            String url = AllUrl.baseUrl+"sa/vaccine/getVaccineDetailsByTechician?VaccineDetails.PartnerId="+PartnerId;

            System.out.println("get all sales-->  " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonObject=new JSONObject(response);

                                String status=jsonObject.getString("Status");
                                if(status.equals("Success")){

                                    JSONObject object=jsonObject.getJSONObject("VaccineDetails");

                                    tv_vac_name.setText(object.getString("VaccineId"));
                                    tv_vac_type.setText(object.getString("Type"));
                                    tv_vac_ist_date.setText(object.getString("DateOf1stDose"));

                                    if(object.getString("DateOf2stDose").equals("1/1/1900 12:00:00 AM")){
                                        tv_snd_date.setText("");

                                    }
                                    else {
                                        tv_snd_date.setText(object.getString("DateOf2stDose"));
                                    }





                                }

                                System.out.println(response);


                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });

            queue.add(stringRequest);

        } else {


        }

    }

}


