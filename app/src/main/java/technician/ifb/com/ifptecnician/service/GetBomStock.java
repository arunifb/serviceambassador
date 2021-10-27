package technician.ifb.com.ifptecnician.service;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import technician.ifb.com.ifptecnician.allurl.AllUrl;

public class GetBomStock {

    public void getstock(final VolleyCallback callback, Context context, String FrCode, String Component) {

       // AllUrl.baseUrl+bom/franchiseinventory?bom.FrCode=3002480&bom.Component=FL221PLDHP010
        final String URL = AllUrl.baseUrl+"bom/franchiseinventory?bom.FrCode="+FrCode+"&bom.Component="+Component;


        System.out.println("URL-->"+URL);
       // final String URL = "AllUrl.baseUrl+bom/franchiseinventory?bom.FrCode=3002480&bom.Component=FL221PLDHP010";

        try {

            RequestQueue queue= Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    System.out.println("Get Bom Stock "+response);

                    try{

                        callback.onSuccess(response.toString(),"true");


                    }catch (Exception e){

                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    callback.onSuccess("","false");

                }
            });

            int socketTimeout = 5000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            queue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface VolleyCallback{
        void onSuccess(String result,String status);
    }
}
