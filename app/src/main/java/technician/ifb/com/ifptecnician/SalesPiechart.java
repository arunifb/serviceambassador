package technician.ifb.com.ifptecnician;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;
import technician.ifb.com.ifptecnician.session.SessionManager;

public class SalesPiechart extends AppCompatActivity implements View.OnClickListener{


    RelativeLayout rl_spare_sale,rl_accessories,rl_additive;
    TextView tv_spare_count,tv_accessories_count,tv_additive_count;
    SessionManager sessionManager;
    String PartnerId,FrCode,mobile_no;
    String [] salesarray={"ZADD","ZACC","ZSPP"};
    ArrayList<PieEntry> entries ;
    PieChart pieChart;

    String adddata,accdata,sparedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_piechart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager=new SessionManager(getApplicationContext());
        try{

            HashMap<String, String> user = sessionManager.getUserDetails();
            PartnerId=user.get(SessionManager.KEY_PartnerId);
            FrCode=user.get(SessionManager.KEY_FrCode);
            mobile_no=user.get(SessionManager.KEY_mobileno);
        }
        catch (Exception e){

            e.printStackTrace();

            // errorDetails.Errorlog(CustomerDetailsActivity.this,"Click%20On%20Serial%20No%20Scan", TAG,"Product%20TAb",no,mobile_no,TicketNo,"",mobile_details,AllUrl.APP_VERSION,tss);

            //  Errorlog("sessionManager","Customer_Tab",e.getMessage().replace(" ","%20"),tss);
        }

//        String Addurl= AllUrl.baseUrl+"sales/getsale?TechCode="+PartnerId+"&itemtype=ZADD";
//        String ACCurl= AllUrl.baseUrl+"sales/getsale?TechCode="+PartnerId+"&itemtype=ZACC";
//        String SPPurl= AllUrl.baseUrl+"sales/getsale?TechCode="+PartnerId+"&itemtype=ZSPP";
        init();

    }

    public void init(){

        pieChart=findViewById(R.id.saleschart);
        entries=new ArrayList<PieEntry>();
        rl_spare_sale=findViewById(R.id.rl_spare_sale);
        rl_accessories=findViewById(R.id.rl_accessories);
        rl_additive=findViewById(R.id.rl_additive);

        rl_spare_sale.setOnClickListener(this);
        rl_accessories.setOnClickListener(this);
        rl_additive.setOnClickListener(this);

        tv_spare_count=findViewById(R.id. tv_spare_count);
        tv_accessories_count=findViewById(R.id.tv_accessories_count);
        tv_additive_count=findViewById(R.id.tv_additive_count);


        for (int i=0;i<salesarray.length;i++){
            getdata(salesarray[i]);
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rl_spare_sale:

                break;
            case R.id.rl_accessories:
                break;
            case R.id.rl_additive:
                break;

        }

    }

    public void getdata(String itemtype){

            if ( CheckConnectivity.getInstance(SalesPiechart.this).isOnline()) {


                entries=new ArrayList<>();
                //url= AllUrl.baseUrl+"ServiceOrder/getTicketStatus?TechCode="+PartnerId;
              String  url= AllUrl.baseUrl+"sales/getsale?TechCode="+PartnerId+"&itemtype="+itemtype;
                System.out.println(url);

                showProgressDialog();

                JsonArrayRequest request = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {


                                if (response == null) {
                                    // Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                try {



                                        switch (itemtype) {


                                            case "ZADD":

                                                try{

                                                        adddata=response.toString();
                                                        int c=response.length();
                                                        tv_additive_count.setText(""+c);
                                                        entries.add(new PieEntry(response.length(),"Additive"));

                                                }catch (Exception e){

                                                    e.printStackTrace();
                                                }
                                                break;

                                            case "ZACC":
                                                try{

                                                        accdata=response.toString();
                                                        int c=response.length();
                                                        tv_accessories_count.setText(""+c);
                                                        entries.add(new PieEntry(response.length(),"Accessories"));

                                                        }catch (Exception e){

                                                    e.printStackTrace();
                                                }
                                                break;

                                            case "ZSPP":

                                                try{
                                                   sparedata=response.toString();
                                                   int c=response.length();
                                                   tv_spare_count.setText(""+c);
                                                   entries.add(new PieEntry(response.length(),"Spare"));

                                                }catch (Exception e){

                                                    e.printStackTrace();
                                                }

                                                break;


                                            default:
                                                break;

                                        }





                                    graphview();

                                } catch (Exception e) {



                                    e.printStackTrace();

                                    hideProgressDialog();

                                }

                                hideProgressDialog();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();

                        // error in getting json
                        //  errorDetails.Errorlog(getActivity(),"" ,TAG,url,error.getMessage().replace(" ","%20"),mobile_no,"0000000000","F",mobile_details,AllUrl.APP_VERSION,tss);

//                    Log.e(TAG, "Error: " + error.getMessage());
//                    Toast.makeText(getApplicationContext(), "Couldn't fetch the data Pleas try again.", Toast.LENGTH_SHORT).show();
//
                    }
                });

                MyApplication.getInstance().addToRequestQueue(request);




            }
            else {

                Toast.makeText(SalesPiechart.this, "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();



            }


        }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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


    public void graphview(){

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        // pieChart.setCenterTextTypeface(tfLight);
        //  pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

       // pieChart.setOnChartValueSelectedListener(this);


        Legend l = pieChart.getLegend();
        l.setWordWrapEnabled(true);
//        l.setMaxSizePercent(0.4f);
        l.setDrawInside(false);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        //    pieChart.setEntryLabelTypeface(tfRegular);
        pieChart.setEntryLabelTextSize(0f);
        PieDataSet dataSet = new PieDataSet(entries, "");

        PieData data = new PieData();
        data.addDataSet(dataSet);
        // data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
//      dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(2f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);



        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        //   dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

//        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        // data.setValueTypeface(tfLight);
        //data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(data);
        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.invalidate();


    }

}
