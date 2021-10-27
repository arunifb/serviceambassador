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

public class Getcitycode {


    public void getCitykey(final Getcode getcode, Context context,String city){


        final String URL = AllUrl.baseUrl+"sa/Municipalitykey/City?MunicipalitykeyCity.CityName="+city;

        System.out.println("get city code-->"+URL);

        try {

            RequestQueue queue= Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try{

                        System.out.println("City key --> "+response);

                        JSONObject object =new JSONObject(response);
                        String Status=object.getString("Status");
                        if (Status.equals("true")){

                            JSONObject jsonObject=new JSONObject(object.getString("Data"));
                            String key=jsonObject.getString("Municipalitykey");
                            getcode.onsuccess("true",key);

                        }
                        else {

                            getcode.onsuccess("false","");
                        }
                    }catch (Exception e){

                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    getcode.onsuccess("false","");

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

    public interface Getcode{

       void onsuccess(String status,String result);
    }
}
