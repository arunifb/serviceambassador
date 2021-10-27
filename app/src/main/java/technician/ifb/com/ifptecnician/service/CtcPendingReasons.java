package technician.ifb.com.ifptecnician.service;

import android.content.Context;
import android.graphics.Color;
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

import technician.ifb.com.ifptecnician.alert.Alertwithicon;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.amc.Create_customer;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;

public class CtcPendingReasons {


    public void Submitdata(Context context,String ticketno, String message){

        try{

            JSONObject jsonObject=new JSONObject();

            jsonObject.put("ObjectId",ticketno);
            jsonObject.put("CTCRemark",message);
            JSONObject object=new JSONObject();
            object.put("CTCReasone",jsonObject);


                String url= AllUrl.baseUrl+"sa/ctc/PendingReasons";

                if (CheckConnectivity.getInstance(context).isOnline()) {
                    try {


                        RequestQueue requestQueue = Volley.newRequestQueue(context);

                        final String mRequestBody = object.toString();

                        System.out.println("body-->"+mRequestBody);

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("LOG_RESPONSE", response);


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("LOG_RESPONSE", error.toString());

                            }
                        }) {
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8";
                            }

                            @Override
                            public byte[] getBody() throws AuthFailureError {
                                try {
                                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                                } catch (UnsupportedEncodingException uee) {
                                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                                    return null;
                                }
                            }

                            @Override
                            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                String responseString = "";

                                if (response != null) {
                                    responseString = String.valueOf(response.statusCode);

                                    try {

                                        String status=new String(response.data);

                                        System.out.println("status-->"+status);
                                        if (status.equals("true")){


                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                        };

                        requestQueue.add(stringRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {


                }





        }catch (Exception e){

            e.printStackTrace();
        }

    }
}
