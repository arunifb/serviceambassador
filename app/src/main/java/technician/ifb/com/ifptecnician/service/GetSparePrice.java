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

public class GetSparePrice {


    public void SpareAmount(final Spareprice spareprice, Context context,String PrdCode,String ProductID){

      //  String url="https://sapsecurity.ifbhub.com/api/sa/price/mat-prd-code?model.serviceCharge.PrdCode="+PrdCode+"&model.serviceCharge.ProductID="+ProductID;
        String url= AllUrl.baseUrl+"sa/price/mat-prd-code?model.Product.PrdCode="+PrdCode+"&model.Product.Material="+ProductID;
        System.out.println("url--> "+url);
        try {

            RequestQueue queue= Volley.newRequestQueue(context);

            StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try{

                        JSONObject object =new JSONObject(response);
                        String Status=object.getString("Status");
                        if (Status.equals("true")){

                            JSONObject jsonObject=new JSONObject(object.getString("ProductPrice"));
                            String Price=jsonObject.getString("Price");
                            spareprice.onsuccess("true",Price);

                        }
                        else {

                            spareprice.onsuccess("false","");
                        }
                    }catch (Exception e){

                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    spareprice.onsuccess("false","");

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

    public interface Spareprice{

      void onsuccess(String status,String result);


    }
}
