package technician.ifb.com.ifptecnician.amc;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.ImagePickerActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.VolleyMultipartRequest;
import technician.ifb.com.ifptecnician.alert.Alertwithicon;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.barcode.SerialNoScan;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.service.CheckCustomerSerialNo;
import technician.ifb.com.ifptecnician.service.CheckSerialIsActive;
import technician.ifb.com.ifptecnician.service.GetBomStock;
import technician.ifb.com.ifptecnician.utility.AsyncFindmodel;
import technician.ifb.com.ifptecnician.utility.Valuefilter;

public class CreateUnpunchedAmc extends AppCompatActivity implements View.OnClickListener {

    LottieAnimationView btn_scan_serial_no,btn_scan_odu_no;
    ImageView iv_take_odu_photo,iv_take_photo,iv_sr_photo;

    public static final int SERIALNO_IMAGE_CAPTURE =90 ;
    public static final int ODU_SERIALNO_IMAGE_CAPTURE = 91;
    static final int SCANSERIALNO = 92;
    static final int ODU_NO = 93;
    Bitmap imagebitmap;
    TextView tv_serial_no,tv_odu_serial_no;
    byte[] seraialno_image_data = null, oduserialno_image_data = null, invoice_image_data = null, customersign_data = null;
    String ac_photo_status="false",serial_photo_status="false",custcode="",tss;
    boolean imagestatus=false;
    private RequestQueue rQueue;
    Toolbar toolbar;

    TextView tv_custname,tv_rcnno,tv_address,tv_modelname;
    String custname,rcnno,address,modelname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_unpunched_amc);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Unpunched Amc");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_scan_serial_no=findViewById(R.id.btn_scan_serial_no);
        iv_take_odu_photo=findViewById(R.id.iv_take_odu_photo);
        btn_scan_odu_no=findViewById(R.id.btn_scan_odu_no);
        iv_take_photo=findViewById(R.id.iv_take_photo);

        btn_scan_serial_no.setOnClickListener(this);
        btn_scan_odu_no.setOnClickListener(this);
        iv_take_photo.setOnClickListener(this);
        iv_take_odu_photo.setOnClickListener(this);

        iv_sr_photo=findViewById(R.id.iv_sr_photo);
        tv_serial_no=findViewById(R.id.tv_serial_no);
        tv_odu_serial_no=findViewById(R.id.tv_odu_serial_no);
        tss = Valuefilter.getdate();

        tv_custname=findViewById(R.id.tv_custname);
        tv_rcnno=findViewById(R.id.tv_rcnno);
        tv_address=findViewById(R.id.tv_address);
        tv_modelname=findViewById(R.id. tv_modelname);

        Intent intent=getIntent();
        String details=intent.getStringExtra("details");

        System.out.println("details -- "+details);

        if (details != null && details.length() != 0) {
            try {

                JSONObject object=new JSONObject(details);

              //  {"altphone":"","custaddress":".,Ramchandra Pur Biswas Para Narendrapur N,,Narendrapur,Baruipur","custemail":"","custid":"12843189","custname":"Anjali Das","custphone":"9830367308","frcode":"3001661","product":"WM","product_name":"WM (ELENA VX)","productid":"8903287012037","serialno":""}
                //custname=, rcnno=, address=, modelname=

              tv_address.setText(object.getString("custaddress"));
              tv_custname.setText(object.getString("custname"));
              tv_rcnno.setText(object.getString("custphone"));
              tv_modelname.setText(object.getString("product_name"));
              custcode=object.getString("custid");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        new Compressimage().execute();

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_scan_serial_no:

                Intent i = new Intent(CreateUnpunchedAmc.this, SerialNoScan.class);
                startActivityForResult(i, SCANSERIALNO);
                break;

            case R.id. iv_take_photo:

                launchCameraIntent("serialno");
                break;

            case R.id.btn_scan_odu_no:
                Intent j = new Intent(CreateUnpunchedAmc.this, SerialNoScan.class);
                startActivityForResult(j, ODU_NO);

                break;

            case R.id.iv_take_odu_photo:
                launchCameraIntent("oduserialno");
                break;


        }

    }


    private void launchCameraIntent(String type) {
        Intent intent = new Intent(CreateUnpunchedAmc.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 16); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 9);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 500);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 500);

        if (type.equals("serialno")) {
            startActivityForResult(intent, SERIALNO_IMAGE_CAPTURE);
        } else if (type.equals("oduserialno")) {
            startActivityForResult(intent, ODU_SERIALNO_IMAGE_CAPTURE);
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {


            case (SCANSERIALNO):

                if (resultCode == RESULT_OK) {

                    String datas = data.getStringExtra("barcode");

                    if (!datas.equals("")) {

                        String odu_no = datas;

                        tv_serial_no.setText(datas);
                    }
                }

                break;


            case ODU_NO:

                if (resultCode == RESULT_OK) {

                    String datas = data.getStringExtra("barcode");

                    if (!datas.equals("")) {

                        String odu_no = datas;

                        tv_odu_serial_no.setText(datas);
                    }
                }
                    break;


                    case SERIALNO_IMAGE_CAPTURE:

                        if (resultCode == Activity.RESULT_OK) {
                            Uri uri = data.getParcelableExtra("path");

                            //  System.out.println("uri-->"+uri);
                            try {
                                // You can update this bitmap to your server
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                // Bitmap converetdImage = getResizedBitmap(bitmap, 80);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                                seraialno_image_data = byteArrayOutputStream.toByteArray();
                                // System.out.println("seraialno_image_data-->"+seraialno_image_data);
                                imagestatus = true;
                                serial_photo_status = "true";
                                iv_sr_photo.setImageBitmap(bitmap);
                                ImagePickerActivity.clearCache(CreateUnpunchedAmc.this);


                            } catch (IOException e) {
                                e.printStackTrace();
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        }
                        break;

                    case ODU_SERIALNO_IMAGE_CAPTURE:
                        if (resultCode == Activity.RESULT_OK) {
                            Uri uri = data.getParcelableExtra("path");
                            try {
                                // You can update this bitmap to your server
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                                // Bitmap converetdImage = getResizedBitmap(bitmap, 80);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                                oduserialno_image_data = byteArrayOutputStream.toByteArray();
                               // view_oduserialno.setImageBitmap(bitmap);
                                imagestatus = true;
                                ac_photo_status = "true";
                               // oduurl = "";


                            } catch (Exception e) {
                                e.printStackTrace();
                                FirebaseCrashlytics.getInstance().recordException(e);
                            }
                        }
                        break;

                }
        }

    public class Compressimage extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try {

                Thread.sleep(3000);
                imagebitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                ByteArrayOutputStream byteArrayOutputStreams = new ByteArrayOutputStream();
                imagebitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStreams);

                invoice_image_data = byteArrayOutputStreams.toByteArray();
                customersign_data=byteArrayOutputStreams.toByteArray();
                seraialno_image_data = byteArrayOutputStreams.toByteArray();
                oduserialno_image_data = byteArrayOutputStreams.toByteArray();

            } catch (Exception e) {

            }
            return null;
        }

        protected void onPostExecute(Void v) {


        }
    }


    private void uploadImage() {

        if (CheckConnectivity.getInstance(this).isOnline()) {

            String upload_URL = AllUrl.baseUrl + "doc/uploaddoc?";

            // System.out.println("upload_URL-->"+upload_URL);

           // progressBar.setVisibility(View.VISIBLE);
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(POST, upload_URL,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {

                            rQueue.getCache().clear();



                            if (response.statusCode == 200) {


                            } else {


                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            //   System.out.println("Image upload error");


                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    //model.AppDocUpload.
                    params.put("TicketNo", custcode);
                    params.put("UploadDate", tss);
                    params.put("ClientDocs", "AMC");
                     System.out.println(params);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();

                    // SerialPhoto
                    params.put("SerialPhoto", new DataPart("serialno_" + custcode + ".jpeg", seraialno_image_data));
                    params.put("ODUSerialPhoto", new DataPart("oduserialno_" + custcode + ".jpeg", oduserialno_image_data));
                    params.put("InvoicePhto", new DataPart("invoicephoto_" + custcode + ".jpeg", invoice_image_data));
                    params.put("SignaturePhoto", new DataPart("signature_" + custcode + ".jpeg", customersign_data));
                    System.out.println(params);
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(CreateUnpunchedAmc.this);
            rQueue.add(volleyMultipartRequest);

        } else {


        }
    }


    public void submitdata(View v){


        uploadImage();
    }

    }





