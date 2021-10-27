package technician.ifb.com.ifptecnician.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;


public class PieChartFragment extends Fragment {


    PieChart pieChart;
    ArrayList<PieEntry> entries ;
    View view;
    Cursor cursor;
    Dbhelper dbhelper;


    public PieChartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_pie_chart,container,false);
        pieChart=view.findViewById(R.id.piechart);
        entries=new ArrayList<PieEntry>();
        dbhelper=new Dbhelper(getContext());
        // Inflate the layout for this fragment

        Beforeload();
        return view;
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


//        for (Map.Entry<String,Integer> entry : citys.entrySet()) {
//            String key = entry.getKey();
//            int value = entry.getValue();
//            entries.add(new PieEntry(value,key));
//
//            // do stuff
//        }


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

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("All Task Data");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }


    public void Beforeload(){

        try{

            entries=new ArrayList<PieEntry>();
            entries.clear();
            cursor=dbhelper.getalldashboard();


            if (cursor != null) {

                if (cursor.moveToFirst()) {

                    do {

                        final int recordid = cursor.getInt(cursor.getColumnIndex("recordid"));
                        System.out.println("recordid-->" + recordid);
                        String Name = cursor.getString(cursor.getColumnIndex("Name"));
                        String Count = cursor.getString(cursor.getColumnIndex("Count"));

                        switch (Name) {




                            case "Assigned":

                              int ascount=Integer.parseInt(Count);
                                entries.add(new PieEntry(ascount,"Assigned"));
                                break;

                            case "Pending":
                               int pencount=Integer.parseInt(Count);

                                entries.add(new PieEntry(pencount,"Pending"));
                                break;

                            case "Closed":

                                int clocount=Integer.parseInt(Count);
                                entries.add(new PieEntry(clocount,"Closed"));
                                break;

                            case "Cancelled":

                              int cancount=Integer.parseInt(Count);
                                entries.add(new PieEntry(cancount,"Cancelled"));
                                break;

                            case "Negative Response":

                               int nrcount=Integer.parseInt(Count);
                                entries.add(new PieEntry(nrcount,"Negative Response"));
                                break;

                            case "Resolved(softclosure)":

                             int  rsccount=Integer.parseInt(Count);
                                entries.add(new PieEntry(rsccount,"Resolved (soft closure)"));
                                break;

                            case "RequestCancellation":

                               int rccount=Integer.parseInt(Count);
                                entries.add(new PieEntry( rccount,"RequestCancellation"));
                                break;

                            default:

                                break;

                        }
                    } while (cursor.moveToNext());

                    graphview();
                }
            } else {


                Toast.makeText(getContext(), "No Record Found", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
