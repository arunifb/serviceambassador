package technician.ifb.com.ifptecnician.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import technician.ifb.com.ifptecnician.allurl.AllUrl;

public class Calltocustomer {

    public void  Calltocust(Context context, String mobile, String ticketno,String PartnerId){

        try{

            JSONObject jsonObject=new JSONObject();
            JSONObject order=new JSONObject();
            order.put("OrderId",ticketno);
            order.put("TechCode",PartnerId);
            jsonObject.put("Order",order);
            jsonObject.put("btnActionType","btnCALL");
            System.out.println("jsonObject -->"+jsonObject);


            String url=AllUrl.baseUrl+"sa/customer/call/";

            System.out.println(url);

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            final String requestBody = jsonObject.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    //  System.out.println(response);zzzz

                    if (response.equals("200")){

                        // if data insert success then insert submit ticket detais

                        System.out.println("response---> "+response);

                    }
                    else {
                        // if data insert success then insert submit ticket detais

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            })
            {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);

                        System.out.println("responseString --->"+responseString);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);

        }catch (Exception e){

            e.printStackTrace();

        }

    }

}
