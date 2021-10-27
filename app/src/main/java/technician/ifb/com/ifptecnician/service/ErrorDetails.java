package technician.ifb.com.ifptecnician.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import technician.ifb.com.ifptecnician.SplashScreen;
import technician.ifb.com.ifptecnician.allurl.AllUrl;

public class ErrorDetails {


    public void  Errorlog(Context context, String Eventname, String pageUrl, String Exception, String Details, String Mobile, String Ticket_no, String Flag, String Model_no, String app_version, String ExceptionDate ){


        if (Flag.equals("")){
            Flag="I";
        }
        else if(Ticket_no.equals("")){

            Ticket_no="0000000000";
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        String url= AllUrl.baseUrl+
                "Exception/getlog?errorLog.eventName="+Eventname.replace(" ","%20")+
                "&errorLog.pageUrl="+pageUrl.replace(" ","%20")+
                "&errorLog.Exception="+Exception+
                "&errorLog.Details="+Details.replace(" ","%20")+
                "&errorLog.Mobile="+Mobile+
                "&errorLog.Ticket_no="+Ticket_no+
                "&errorLog.Flag="+Flag+
                "&errorLog.Model_no="+Model_no.replace(" ","%20")+
                "&errorLog.app_version="+app_version.replace(" ","%20")+
                "&errorLog.ExceptionDate="+ExceptionDate;

        System.out.println(url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }

}
