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

public class GetvaccinationType {

    public void getvactype(final Onresult result, Context context){

        final String URL = AllUrl.baseUrl+"sa/vaccine/vaccinationType";
        System.out.println("URL-->"+URL);

        try {

            RequestQueue queue= Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try{

                        JSONObject object =new JSONObject(response);
                        String Status=object.getString("Status");
                        if (Status.equals("true")){

                            String VaccineCategory=object.getString("VaccinationType");
                            result.onsuccess("true",VaccineCategory);

                        }
                        else {

                            result.onsuccess("false","");
                        }
                    }catch (Exception e){

                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    result.onsuccess("false","");

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


    public interface Onresult{

        void onsuccess(String status,String result);

    }
}
