package technician.ifb.com.ifptecnician.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import technician.ifb.com.ifptecnician.LoginActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;

public class UpdateError {

    String err_response;
    Context context;

    public UpdateError(Context context) {
        this.context = context;
    }

    public String  UserLogin(String Eventname,String pageUrl,String Exception,String ExceptionDate ){

      //  https://crmapi.ifbhub.com/api/Exception/getlog?errorLog.eventName=arun&errorLog.pageUrl=oage&errorLog.Exception=page&errorLog.ExceptionDate=12-jun-2019
            RequestQueue queue = Volley.newRequestQueue(context);

            String url=AllUrl.baseUrl+"Exception/getlog?errorLog.eventName="+Eventname+"&errorLog.pageUrl="+pageUrl+"&errorLog.Exception="+Exception+"&errorLog.ExceptionDate="+ExceptionDate;

            System.out.println("get all sales-->  "+ url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("Login_data-->"+response);
                            err_response=response;

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    hideProgressDialog();

                }
            });

            queue.add(stringRequest);

       return err_response;
    }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(context.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
