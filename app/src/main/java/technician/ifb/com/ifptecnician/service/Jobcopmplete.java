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
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;

public class Jobcopmplete {


    Dbhelper dbhelper;
    public void getstring(final Getresponse getresponse, Context context, String TicketNo, String ServiceAmt,
                          String PartsTotal,String AccTotal,String NetTotal,String ICRNo,String ICRDate,String PartnerId,String EnggCommnets){

        String url= AllUrl.baseUrl+"sa/jobcomlete/";

        System.out.println("jobcomplete --> "+url);

        dbhelper=new Dbhelper(context.getApplicationContext());

        try{

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("TicketNo",TicketNo);
            jsonObject.put("ServiceAmt",ServiceAmt);
            jsonObject.put("PartsTotal",PartsTotal);
            jsonObject.put("AccTotal",AccTotal);
            jsonObject.put("NetTotal",NetTotal);
            jsonObject.put("ICRNo",ICRNo);
            jsonObject.put("ICRDate",ICRDate);
            jsonObject.put("PartnerId",PartnerId);
            jsonObject.put("EnggCommnets",EnggCommnets);

            final String requestbody=jsonObject.toString();

            System.out.println("requestbody-->"+requestbody);

            if (CheckConnectivity.getInstance(context).isOnline()) {


                RequestQueue queue = Volley.newRequestQueue(context);
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {

                            getresponse.onSuccess("false", "");


                        } catch (Exception e) {
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

                                getresponse.onSuccess("true", new String(response.data));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                queue.add(request);

            }

            else {

                getresponse.onSuccess("false", "");

                dbhelper.insert_offline_data(TicketNo,requestbody,"POST",url);

            }
        }catch (Exception e){

            e.printStackTrace();

        }
    }

    public interface Getresponse{
        void onSuccess(String status,String result);

    }

}
