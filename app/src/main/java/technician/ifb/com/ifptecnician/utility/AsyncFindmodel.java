package technician.ifb.com.ifptecnician.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import technician.ifb.com.ifptecnician.CustomerDetailsActivity;

public class AsyncFindmodel extends AsyncTask<String, String, String> {

   Context context;
    private static final String SOAP_ACTION = "http://tempuri.org/IModelFinder/FindModel";
    private static final String METHOD_NAME = "FindModel";
    private static final String NAMESPACE = "http://tempuri.org/";

    public interface AsyncResponse {
        void modelresult(String output);
    }
    public AsyncResponse modelresult = null;

    @SuppressLint("NewApi")
    public AsyncFindmodel(Context context,AsyncResponse modelresult){
        this.modelresult = modelresult;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {


        String URL = "https://sales.ifbhub.com/ModelFinder.svc?wsdl";

        //for linear parameter
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("username", "vasant"); // adding method property here serially
        request.addProperty("password", "Ifb@1234"); // adding method property here serially
        request.addProperty("machineSerial", CustomerDetailsActivity.serialno_scan); // adding method property here serially

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.setOutputSoapObject(request);
        envelope.dotNet = true;

        HttpTransportSE httpTransport = new HttpTransportSE(URL);
        httpTransport.debug = true;

        try {
            httpTransport.call(SOAP_ACTION, envelope);
        } catch (HttpResponseException e) {
            // TODO Auto-generated catch block
            Log.e("HTTPLOG", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("IOLOG", e.getMessage());
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            Log.e("XMLLOG", e.getMessage());
            e.printStackTrace();
        } //send request

        Object  result = null;
        try {
            result = (Object )envelope.getResponse();
            Log.i("RESPONSE",String.valueOf(result)); // see output in the console
            System.out.println(result);
        } catch (SoapFault e) {
            // TODO Auto-generated catch block
            Log.e("SOAPLOG", e.getMessage());
            e.printStackTrace();
        }
        return String.valueOf(result);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        modelresult.modelresult(s);

        System.out.println("model result--> "+s);

    }
}
