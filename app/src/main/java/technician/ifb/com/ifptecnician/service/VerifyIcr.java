package technician.ifb.com.ifptecnician.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;

public class VerifyIcr {


    public void getString(final VolleyCallback callback, Context context, String icrno, String FrCode) {


            final String URL = AllUrl.baseUrl+"service-contract/icr/validate";

                try {


                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("ICRNo", icrno);
                    jsonBody.put("Franchise_id", FrCode);


                    final String mRequestBody = jsonBody.toString();

                    System.out.println("icr check"+mRequestBody);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("LOG_RESPONSE", response);


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_RESPONSE", error.toString());


                            try{
                                JSONObject jsonObject=new JSONObject();
                               jsonObject.put("Message", "Please try after some time");
                                callback.onSuccess(jsonObject.toString());

                            }catch (Exception e){

                              e.printStackTrace();
                            }

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


                                    callback.onSuccess(new String(response.data));
//                                    JSONObject object = new JSONObject(new String(response.data));
//                                    //  {"Status":"FAILED","Data":"Mobile no already exist"}
//
//                                    System.out.println("object-->"+object);
//
//                                    String message=object.getString("Message");
//                                    if (message.equals("Success")){
//
//
//                                    }
//                                    else {
//
//                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };

                    requestQueue.add(stringRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
    }

    public interface VolleyCallback{
        void onSuccess(String result);
    }
}
