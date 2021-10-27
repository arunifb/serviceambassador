package technician.ifb.com.ifptecnician.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import technician.ifb.com.ifptecnician.allurl.AllUrl;

public class CheckCustomerSerialNo {


    public void getstatus(final VolleyCallback callback, Context context, String CustomerCode, String serialno) {

        final String URL = AllUrl.baseUrl+"sa/customers/search?contact=&zzfranch=&zzsoldto="+CustomerCode+"&serialNumber="+serialno;
        System.out.println("URL--> "+URL);

        try {

            RequestQueue queue=Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try{

                        JSONObject jsonObject=new JSONObject(response);

                        String status=jsonObject.getString("Status");

                        if (status.equals("true")){

                            callback.onSuccess("true",response);
                        }
                        else {

                            callback.onSuccess("false","");
                        }

                    }catch (Exception e){

                       e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    callback.onSuccess("error","");

                }
            });


            int socketTimeout = 5000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            queue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface VolleyCallback{
        void onSuccess(String status,String result);
    }
}
