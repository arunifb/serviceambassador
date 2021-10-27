package technician.ifb.com.ifptecnician;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.utility.InputStreamVolleyRequest;

public class CheckApk extends AppCompatActivity {

    TextView tv_outofdate,tv_whatsnew;
    long fileSize;
    Button btn_download;
    public static final int REQUEST_INSTALL = 110;
    public static final int REQUEST_SORAGE = 1234;
    private ProgressDialog progressDialog;
    public static final int progressType = 0;
    String name,version,downloadurl;

  //  private static String url = "https://crmapi.ifbhub.com/AppVer/ifb.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_apk);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // ----------------- get all intent data ----------------------------

        name=getIntent().getStringExtra("name");
        version=getIntent().getStringExtra("version");
        downloadurl=getIntent().getStringExtra("downloadurl");

        // ----------------   bind all layout ---------------------------

        tv_outofdate=findViewById(R.id.tv_outofdate);
        tv_whatsnew=findViewById(R.id.tv_details);
        btn_download=findViewById(R.id.btn_download);

        // ----------------------- set intent data into view -------------------------------

        String whatsnew=name.replace(";"," \n ");
        tv_whatsnew.setText(whatsnew);
        tv_outofdate.setText(Html.fromHtml("This version of Service Ambassador App becomes out of date . </font>  Tap  \"Download\" below to get the latest V."+version+" version App"));

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DownloadFileFromURL().execute(downloadurl);

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!getPackageManager().canRequestPackageInstalls()) {
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getPackageName()))), REQUEST_SORAGE);
            } else {
            }
        }

    }


    // -------------------------- app is instgall or not ----------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_INSTALL) {

            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this,"Install succeeded!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"Install canceled!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Install Failed!", Toast.LENGTH_SHORT).show();
            }

        }

        if (requestCode == REQUEST_SORAGE){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }






    // -------------------------- for download new apk -------------------------------------
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String pathFolder = "";
        String pathFile = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CheckApk.this);
            pd.setTitle("File is Downloading. Please wait...");
            pd.setMessage("Please wait.");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(false);
            pd.show();
        }



        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {
                pathFolder = Environment.getExternalStorageDirectory() + "/ifbdevapk";
                pathFile = pathFolder + "/ifbdev.apk";
                File futureStudioIconFile = new File(pathFolder);
                if(!futureStudioIconFile.exists()){
                    futureStudioIconFile.mkdirs();
                }

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lengthOfFile = connection.getContentLength();

                System.out.println(lengthOfFile);

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());

                FileOutputStream output = new FileOutputStream(pathFile);

                byte data[] = new byte[lengthOfFile]; //anybody know what 1024 means ?
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());

            }

            return pathFile;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pd.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (pd!=null) {
                pd.dismiss();
            }

            String app_pkg_name = "technician.ifb.com.ifptecnician";
            int UNINSTALL_REQUEST_CODE = 1;

            String PATH = pathFile;
            File file = new File(PATH);
            if(file.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uriFromFile(getApplicationContext(), new File(PATH)), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    getApplicationContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Log.e("TAG", "Error in opening the file!");
                }
            }else{
                Toast.makeText(getApplicationContext(),"installing",Toast.LENGTH_LONG).show();
            }

        }

    }



    Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }


    // ------------------------- progressbar HORIZONTAL ------------------------------

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progressType: // we set this to 0
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("File is Downloading. Please wait...");
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(100);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(true);
                progressDialog.show();
                return progressDialog;
            default:
                return null;
        }
    }

}
