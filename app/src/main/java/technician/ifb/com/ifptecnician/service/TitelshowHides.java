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
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;

import technician.ifb.com.ifptecnician.allurl.AllUrl;

public class TitelshowHides {


    public void getStatus(final Showhide showhide, Context context,String frcode){

        final String URL = AllUrl.baseUrl+"sa/titels/howHide/";


        try {



            JSONObject jsonBody = new JSONObject();
            jsonBody.put("frcode", frcode);
            JSONObject object=new JSONObject();
            object.put("tilesShowHide",jsonBody);
            final String mRequestBody = object.toString();

            System.out.println("tile check--> "+mRequestBody);
            RequestQueue requestQueue = Volley.newRequestQueue(context);


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_RESPONSE", response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_RESPONSE", error.toString());

                    showhide.onSuccess("false","");

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

                            showhide.onSuccess("true",new String(response.data));

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

   public interface Showhide{

       void onSuccess(String status,String result);
   }
}
