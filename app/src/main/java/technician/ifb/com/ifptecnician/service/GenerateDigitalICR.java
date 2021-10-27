package technician.ifb.com.ifptecnician.service;

import android.content.Context;

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

public class GenerateDigitalICR {

    public void getstring(final VolleyCallback volleyCallback,Context context,String RegionCode,String CreatedBy ){

        String url= AllUrl.baseUrl+"sa/digitalicr/generate";


        try{

            RequestQueue queue= Volley.newRequestQueue(context);
            JSONObject jsonObject=new JSONObject();

            jsonObject.put("RegionCode",RegionCode);
            jsonObject.put("CreatedBy",CreatedBy);
            final String requestbody=jsonObject.toString();




            StringRequest request= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    try{


                        volleyCallback.onSuccess("false","");



                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }
            }){
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestbody == null ? null : requestbody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestbody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";

                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);

                        try {


                            volleyCallback.onSuccess("true",new String(response.data));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            queue.add(request);

        }catch (Exception e){

            e.printStackTrace();

        }
    }

        public interface VolleyCallback{
            void onSuccess(String status,String result);

        }
}
