package technician.ifb.com.ifptecnician;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import technician.ifb.com.ifptecnician.fragment.adapter.Taskadapter;
import technician.ifb.com.ifptecnician.fragment.dummy.Taskmodel;
import technician.ifb.com.ifptecnician.internet.CheckConnectivity;


public class GraphView extends AppCompatActivity implements OnChartValueSelectedListener {

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pieChart=(PieChart)findViewById(R.id.chart1);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.0f);

        // pieChart.setCenterTextTypeface(tfLight);
        //  pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(false);

        pieChart.setRotationAngle(0);
        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // pieChart.setUnit(" €");
        // pieChart.setDrawUnitsInChart(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);


        //  pieChart.animateY(1400, Easing.EaseInOutQuad);
        // pieChart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();


//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
        l.setWordWrapEnabled(true);
//        l.setMaxSizePercent(0.4f);
        l.setDrawInside(false);

//        l.setXEntrySpace(5f);
//        l.setYEntrySpace(5f);
//        l.setYOffset(0f);


//        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);
//        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//        l.setWordWrapEnabled(true);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        //    pieChart.setEntryLabelTypeface(tfRegular);
        pieChart.setEntryLabelTextSize(0f);

        ArrayList<PieEntry> entries = new ArrayList<>();

        Map<String, Integer> citys = new HashMap<>();

        citys.put("abc",1);
        citys.put("abcde",2);
        citys.put("abcb",3);
        citys.put("abcbz",4);
        citys.put("abcdv",5);
        citys.put("abcdm",6);

        for (Map.Entry<String,Integer> entry : citys.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();

            entries.add(new PieEntry(value,key));



            // do stuff
        }
        PieDataSet dataSet = new PieDataSet(entries, "");

//                dataSet.setDrawIcons(false);
//                dataSet.setSliceSpace(3f);
//                dataSet.setIconsOffset(new MPPointF(0, 40));
//                dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);


        //   dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        //   data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        // data.setValueTypeface(tfLight);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();

    }



//    public void  getdata(String PartnerId){
////        System.out.println("i am in task page ");
////      RequestQueue queue = Volley.newRequestQueue(getContext());
////      String url=AllUrl.baseUrl+"User/ServiceOrder/17226089";
////
//        if ( CheckConnectivity.getInstance(getContext()).isOnline()) {
//
//            showProgressDialog();
//            RequestQueue queue = Volley.newRequestQueue(getContext());
////          String url = AllUrl.loginUrl+"?url=http://centurionbattery.in/development&username="+username+"&password="+password;
//            String url=AllUrl.baseUrl+"User/ServiceOrder/"+PartnerId;
//
//            System.out.println("get all task-->  "+ url);
//            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//
//                            System.out.println("task details-->"+response);
//
////                          if (dbhelper.istaskdataEmpty()){
////
////                              if(dbhelper.deletetask()){
////
////                                  if(Boolean.valueOf(dbhelper.insert_task_data(response,"")));
////
////                                  System.out.println("task data insert successfully");
////                              }
////                          }
////                          else{
////
////                              if(Boolean.valueOf(dbhelper.insert_task_data(response,"")));
////                              System.out.println("task data insert successfully");
////                          }
//
//                            result=response;
//
//                            try {
//
//                                JSONArray jsonArray=new JSONArray(response);
//
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    JSONObject object1 = jsonArray.getJSONObject(i);
//                                    CallType= object1.getString("CallType");;
//                                    TicketNo= object1.getString("TicketNo");;
//                                    Address= object1.getString("Address");;
//                                    CallBookDate= object1.getString("CallBookDate");;
//                                    Status= object1.getString("Status");;
//                                    RCNNo= object1.getString("RCNNo");;
//                                    ServiceType= object1.getString("ServiceType");
//                                    CustomerName=object1.getString("CustomerName");
//                                    Priority=object1.getString("Priority");
//
//                                    if(Status.equals("Assigned") || Status.equals("Pending")){
//
//                                        models.add(new Taskmodel(CallType,TicketNo,Address,CallBookDate,Status,RCNNo,ServiceType,CustomerName,Priority));
//                                    }
//                                }
//
//                                Taskadapter taskadapter=new Taskadapter(models,getContext());
//                                lv_list.setAdapter(taskadapter);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            hideProgressDialog();
//
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                }
//            });
//// Add the request to the RequestQueue.
//            queue.add(stringRequest);
//
//        } else {
//
//            Toast.makeText(getContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();
//
//            if (dbhelper.istaskdataEmpty()){
//
//                taskcurser=dbhelper.getalltaskdata();
//                if (taskcurser != null)
//                {
//
//                    if (taskcurser.moveToFirst()) {
//
//                        do {
//
//                            final int recordid = taskcurser.getInt(taskcurser.getColumnIndex("recordid"));
//                            System.out.println("recordid-->" + recordid);
//                            String data   = taskcurser.getString(taskcurser.getColumnIndex("data"));
//                            result=data;
//
//                            try {
//
//                                JSONArray jsonArray=new JSONArray(data);
//                                for (int i = 0; i < jsonArray.length(); i++) {
//
//                                    JSONObject object1 = jsonArray.getJSONObject(i);
//                                    CallType= object1.getString("CallType");;
//                                    TicketNo= object1.getString("TicketNo");;
//                                    Address= object1.getString("Address");;
//                                    CallBookDate= object1.getString("CallBookDate");;
//                                    Status= object1.getString("Status");;
//                                    RCNNo= object1.getString("RCNNo");;
//                                    ServiceType= object1.getString("ServiceType");
//                                    CustomerName=object1.getString("CustomerName");
//                                    Priority=object1.getString("Priority");
//
//                                    if(Status.equals("Assigned") || Status.equals("Pending")){
//                                        models.add(new Taskmodel(CallType,TicketNo,Address,CallBookDate,Status,RCNNo,ServiceType,CustomerName,Priority));
//                                    }
//                                }
//
//                                Taskadapter taskadapter=new Taskadapter(models,getContext());
//                                lv_list.setAdapter(taskadapter);
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        } while (taskcurser.moveToNext());
//                    }
//                }
//            }
//        }
//    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
