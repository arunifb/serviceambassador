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

import org.json.JSONArray;
import org.json.JSONObject;

import technician.ifb.com.ifptecnician.allurl.AllUrl;

public class CheckSerialIsActive {

    public void Checkserial(final Serialresult serialresult, Context context,String serialno){


        try {

            final String URL = AllUrl.baseUrl+"sa/customers/search?contact=&zzfranch=&zzsoldto=&serialNumber="+serialno;
            System.out.println("URL-->"+URL);

            RequestQueue queue= Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                 try{

                     JSONObject jsonObject=new JSONObject(response);

                     String status=jsonObject.getString("Status");

                     if (status.equals("true")){

                         JSONArray jsonArray=jsonObject.getJSONArray("Data");

                         for(int i=0;i<1;i++){

                             JSONObject object=jsonArray.getJSONObject(i);
                             String zzsoldto=object.getString("zzsoldto");
                             serialresult.onsuccess("true",zzsoldto);

                         }
                     }
                     else {

                         serialresult.onsuccess("false","");
                     }

                    }catch (Exception e){

                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    serialresult.onsuccess("false","");
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

    public interface Serialresult{

        void onsuccess(String status,String data);
    }


}
