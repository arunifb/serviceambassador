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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import technician.ifb.com.ifptecnician.allurl.AllUrl;

public class DashbordApi {


   // AllUrl.baseUrl+DashBoard/getDashBoardCount?model.dashboard.PartnerId=17113512
    String result;


    public String  Dashborddata(Context context, String PartnerId){


        String url= AllUrl.baseUrl+"DashBoard/getDashBoardCount?model.dashboard.PartnerId=17113512";

        RequestQueue queue=Volley.newRequestQueue(context);


        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                result=response;

                System.out.println("dashboard data"+response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                System.out.println(error.toString());
            }
        });


        int socketTimeout = 10000; //10 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);



       return  result;
    }



    public interface VolleyCallback{
        void onSuccess(String result);
    }

}
