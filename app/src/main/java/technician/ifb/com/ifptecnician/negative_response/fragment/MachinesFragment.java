package technician.ifb.com.ifptecnician.negative_response.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;


public class MachinesFragment extends Fragment implements View.OnClickListener{


    SharedPreferences sharedPreferences;

    TextView nr_mac_tv_product,nr_mac_tv_model,nr_mac_tv_serial_no,nr_mac_tv_odu_ser_no,nr_mac_tv_machine_status,
             nr_mac_tv_dop,nr_mac_tv_doi;
    ImageView nr_mac_view_serialno,nr_mac_view_oduserialno,nr_mac_view_invioce;

    String TicketNo, serialnourl,oduurl,invioceurl;

    public MachinesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_machines,container,false);
        nr_mac_tv_product=view.findViewById(R.id.nr_mac_tv_product);
        nr_mac_tv_model=view.findViewById(R.id.nr_mac_tv_model);
        nr_mac_tv_serial_no=view.findViewById(R.id.nr_mac_tv_serial_no);
        nr_mac_tv_odu_ser_no=view.findViewById(R.id. nr_mac_tv_odu_ser_no);
        nr_mac_tv_machine_status=view.findViewById(R.id.nr_mac_tv_machine_status);
        nr_mac_tv_dop=view.findViewById(R.id.nr_mac_tv_dop);
        nr_mac_tv_doi=view.findViewById(R.id.nr_mac_tv_doi);
        nr_mac_view_serialno=view.findViewById(R.id. nr_mac_view_serialno);
        nr_mac_view_serialno.setOnClickListener(this);
        nr_mac_view_oduserialno=view.findViewById(R.id.nr_mac_view_oduserialno);
        nr_mac_view_oduserialno.setOnClickListener(this);
        nr_mac_view_invioce=view.findViewById(R.id.nr_mac_view_invioce);
        nr_mac_view_invioce.setOnClickListener(this);

        updatevalue();

        return view;
    }


    public void updatevalue(){

        try{
            sharedPreferences = getActivity().getSharedPreferences("details", 0);
            String details = sharedPreferences.getString("details", "");

            System.out.println("details"+details);

            JSONObject object1 = new JSONObject(details);
            TicketNo=object1.getString("TicketNo");

            nr_mac_tv_product.setText(object1.getString("Product"));
            nr_mac_tv_model.setText(object1.getString("Model"));
            nr_mac_tv_serial_no.setText(object1.getString("serial_no"));
            nr_mac_tv_odu_ser_no.setText(object1.getString("odu_ser_no"));
            nr_mac_tv_machine_status.setText(object1.getString("MachinStatus"));
            nr_mac_tv_dop.setText(object1.getString("DOP"));
            nr_mac_tv_doi.setText(object1.getString("DOI"));

            getuploaddoc();

        }

        catch (Exception e){

            e.printStackTrace();
        }
    }


    public void getuploaddoc(){

        if (CheckConnectivity.getInstance(getActivity()).isOnline()) {

            RequestQueue queue = Volley.newRequestQueue(getActivity());

          //  String url = AllUrl.baseUrl+"uploadedDocByApp/getdoc?model.AppDocUpload.TicketNo="+"2002778771";
            String url = AllUrl.baseUrl+"uploadedDocByApp/getdoc?model.AppDocUpload.TicketNo="+TicketNo;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            hideProgressDialog();

                            //  [{"TicketNo":"3001999293","SerialPhoto":"iVBORw0KGgoAAAANSUhEUgAAAAIAAAABCAIAAAB7QOjdAAAAA3NCSVQICAjb4U/gAAAAD0lEQVQImWNk4xWxc4kHAAJkAQo8nxUQAAAAAElFTkSuQmCC","ODUSerialPhoto":"null","InvoicePhto":"null","UploadDate":null}]

                            try{

                                JSONArray jsonArray = new JSONArray(response);

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    serialnourl = obj.getString("SerialPhoto").replace("https://crmapi.ifbhub.com","https://sapsecurity.ifbhub.com");
                                    oduurl = obj.getString("ODUSerialPhoto").replace("https://crmapi.ifbhub.com","https://sapsecurity.ifbhub.com");
                                    invioceurl = obj.getString("InvoicePhto").replace("https://crmapi.ifbhub.com","https://sapsecurity.ifbhub.com");


//                                    Glide.with(getActivity())
//                                            .load(serialnourl)
//                                            .into(nr_mac_view_serialno);
//
//
//                                    Glide.with(getActivity())
//                                            .load(oduurl)
//                                            .into(nr_mac_view_oduserialno);
//
//                                    Glide.with(getActivity())
//                                            .load(invioceurl)
//                                            .into(nr_mac_view_invioce);


                                    Picasso.get()
                                            .load(serialnourl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                                            .into(nr_mac_view_serialno);

                                    Picasso.get()
                                            .load(oduurl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                                            .into(nr_mac_view_oduserialno);
                                    Picasso.get()
                                            .load(invioceurl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                                            .into(nr_mac_view_invioce);



//                                      "SerialPhoto": "https://crmapi.ifbhub.com/ClientDocument/2002778771invoice.png",
//                                              "ODUSerialPhoto": "https://crmapi.ifbhub.com/ClientDocument/2002778771oduserial.png",
//                                              "InvoicePhto": "https://crmapi.ifbhub.com/ClientDocument/2002778771serial.png",

                                }


                            }catch(Exception e){


                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    hideProgressDialog();
                }
            });


            int socketTimeout = 10000; //10 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            queue.add(stringRequest);

        } else {

            Toast.makeText(getActivity(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
        }
    }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public void  view_image(String type){

        // android.R.style.Theme_Black_NoTitleBar_Fullscreen
        // android.R.style.Theme_Black_NoTitleBar_Fullscreen
        final Dialog image_dialog = new Dialog(getActivity());


        image_dialog.setContentView(R.layout.item_view_fillsizeimage);
        ImageView image_cancel;
        TextView textView;
        textView =image_dialog.findViewById(R.id.item_text_image);
        PhotoView item_image_view;
        image_cancel=image_dialog.findViewById(R.id.image_cancel);
        item_image_view=image_dialog.findViewById(R.id.item_image_view);

        image_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_dialog.dismiss();
            }
        });

        try{

            if(serialnourl != null && type.equals("serialno")){

                textView.setText("Serial No Photo");

                if(!serialnourl.equals("")){
//                    Glide.with(getActivity())
//                            .load(serialnourl)
//                            .into(item_image_view);

                    Picasso.get()
                            .load(serialnourl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                            .into(item_image_view);
                }

            }
            else if (oduurl!=null && type.equals("oduserialno")){

                textView.setText("ODU Serial No Photo");

                if (!oduurl.equals("")){
//                    Glide.with(getActivity())
//                            .load(oduurl)
//                            .into(item_image_view);


                    Picasso.get()
                            .load(oduurl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                            .into(item_image_view);
                }

            }
            else if(invioceurl!=null && type.equals("invioce")){
                textView.setText("Invoice Photo");

                if (!invioceurl.equals("")){
//                    Glide.with(getActivity())
//                            .load(invioceurl)
//                            .into(item_image_view);

                    Picasso.get()
                            .load(invioceurl)
//                            .placeholder(R.drawable.user_placeholder)
//                            .error(R.drawable.user_placeholder_error)
                            .into(item_image_view);
                }



            }


        }catch (Exception e){
            e.printStackTrace();
        }


        image_dialog.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.nr_mac_view_serialno:

                view_image("serialno");
                break;

            case R.id.nr_mac_view_oduserialno:
                view_image("oduserialno");
                break;

            case R.id.nr_mac_view_invioce:
                view_image("invioce");
                break;
        }
    }
}

