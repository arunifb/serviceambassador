package technician.ifb.com.ifptecnician.exchange.Webview;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.R;

public class Viewifbpoint extends AppCompatActivity {


     RelativeLayout ll_main;

     WebView wv;
     ProgressBar progressBar;
     static String url = "",name="";

    PDFView pdfView;
    ProgressBar ecatalog_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewifbpoint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        url=getIntent().getExtras().getString("url");
        name=getIntent().getExtras().getString("name");
        toolbar.setTitle(name+" Pdf");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pdfView=findViewById(R.id.pdfView);
        ecatalog_progress=findViewById(R.id.ecatalog_progress);
        System.out.println(url);
        new RetrivePDFfromUrl().execute(url);

//        init();
//        Uri uri =  Uri.parse(  url );
//
//        System.out.println(uri);
//
//        pdfView.fromUri(uri)
//                .defaultPage(0)
//                .load();

      //  init();

    }



    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ecatalog_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream).load();

            ecatalog_progress.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void init(){

        wv = (WebView) findViewById(R.id.webView);

        // wv.setWebViewClient(new MyBrowser());
        wv.setWebViewClient(new myWebClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);
        wv.loadUrl("https://docs.google.com/gview?embedded=true&url=$urlurl");

       // https://drive.google.com/viewerng/viewer?embedded=true&url=
    }

    public  class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }

    }

}
