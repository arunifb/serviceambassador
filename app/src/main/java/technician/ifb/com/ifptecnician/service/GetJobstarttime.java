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

public class GetJobstarttime {

    public void getjobstart(final VolleyCallback callback, Context context){

        final String Url= AllUrl.baseUrl+"sa/getserverDatetime";

        System.out.println("url-->"+Url);


        try{

            RequestQueue queue= Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONObject jsonObject=new JSONObject(response);

                        String status=jsonObject.getString("Status");

                        System.out.println("Status --> "+status);

                        if (status.equals("true")){

                            JSONObject object=jsonObject.getJSONObject("Data");

                            String jobstarttime=object.getString("StartDateTime");
                            String jobendtime=object.getString("SoftCloserTimeGap");

                            callback.onsuccess("true",jobstarttime,jobendtime);

                        }
                        else {

                            callback.onsuccess("false","","");

                        }


                    }catch (Exception e){

                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    callback.onsuccess("false","","");
                }
            });

            int socketTimeout = 5000;
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            queue.add(request);


        }catch (Exception e){

            e.printStackTrace();
        }
    }

    public interface VolleyCallback{

        void onsuccess(String status,String jobstarttime,String jobendtime);
    }
}
