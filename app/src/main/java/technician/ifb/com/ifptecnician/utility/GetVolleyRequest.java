package technician.ifb.com.ifptecnician.utility;

import android.app.Activity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import technician.ifb.com.ifptecnician.VolleyRespondsListener;

public class GetVolleyRequest {

    private String  type;
    private Activity act;
    private VolleyRespondsListener volleyRespondsListener;
    private String networkurl;
    private String jsonObject = null;
    private String params;


    public GetVolleyRequest(Activity act,VolleyRespondsListener volleyRespondsListener,  String type, String netnetworkUrl,String params) {
        this.act = act;
        this.type = type;
        this.networkurl = netnetworkUrl;
        this.params = params;
        this.volleyRespondsListener=volleyRespondsListener;
        sendRequest();


    }

    private void sendRequest() {

        Log.d("url", "url" + networkurl);

        StringRequest request=new StringRequest(networkurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try
                {
                if (volleyRespondsListener != null) {
                    volleyRespondsListener.onSuccess(response, type);
                }
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                        try {
                            NetworkResponse response = error.networkResponse;
                            Log.e("response", "response " + response);
                            if (response != null) {
                                int code = response.statusCode;

                                String errorMsg = new String(response.data);
                                Log.e("response", "response" + errorMsg);
//                                try {
//                                    jsonObject = new JSONObject(errorMsg);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                                try
                                {
                                    if (volleyRespondsListener != null) {
                                      volleyRespondsListener.onFailureJson(code, errorMsg);
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            } else {
                                String errorMsg = error.getMessage();
                                try
                                {
                                    if (volleyRespondsListener != null) {
                                     volleyRespondsListener.onFailureJson(0, errorMsg);
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
            }
        });
                request.setRetryPolicy(new DefaultRetryPolicy(
                6000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueue requestqueue = Volley.newRequestQueue(act);
            requestqueue.add(request);

//        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,networkurl,params,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("response", "response " + response);
//                        volleyRespondsListener.onSuccessJson(response, type);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        try {
//                            NetworkResponse response = error.networkResponse;
//                            Log.e("response", "response " + response);
//                            if (response != null) {
//                                int code = response.statusCode;
//
//                                String errorMsg = new String(response.data);
//                                Log.e("response", "response" + errorMsg);
//                                try {
//                                    jsonObject = new JSONObject(errorMsg);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                                String msg = jsonObject.optString("message");
//                                volleyRespondsListener.onFailureJson(code, msg);
//                            } else {
//                                String errorMsg = error.getMessage();
//                                volleyRespondsListener.onFailureJson(0, errorMsg);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
//                600000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//


    }

}
