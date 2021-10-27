package technician.ifb.com.ifptecnician.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;


import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.CloseLeadActivity;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.DetailsActivity;
import technician.ifb.com.ifptecnician.LeadActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.model.Lead_model;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;

public class Lead_adapter extends RecyclerView.Adapter<Lead_adapter.MyViewHolder> implements Filterable {


    private List<Lead_model> leadlist;

    private List<Lead_model> leadListFiltered;
    private final LayoutInflater inflater;
    View view;
    Tasklist_Adapter.MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    String searchString = "";

    public Lead_adapter(Context context, List<Lead_model> leadlist, JobsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.leadlist = leadlist;
        leadListFiltered=leadlist;
        inflater=LayoutInflater.from(context);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_lead, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Lead_model dataSets=leadListFiltered.get(position);


        try {

            if (!dataSets.getLeadType().equals("FREE OF CHARGE")){

            holder.tv_leadno.setText(dataSets.getLeadNo());
            holder.tv_leadtype.setText(dataSets.getLeadType());
            holder.tv_custname.setText(dataSets.getCustomer());
            holder.tv_custcode.setText(dataSets.getCustomer());
            holder.tv_address.setText(dataSets.getAddress().replace("/[^\\w\\s]/gi"," "));
            holder.tv_phone.setText(dataSets.getPhone());
            holder.tv_status.setText(dataSets.getStatus());
            holder.tv_product.setText(dataSets.getEssName());
            holder.tv_start_date.setText(Parsedate(dataSets.getStartDate()));
            }
            else {

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        String name =dataSets.getCustomer().toLowerCase(Locale.getDefault());
        String ticketno=dataSets.getLeadNo();//.toLowerCase(Locale.getDefault());
        String phoneno=dataSets.getPhone();//.toLowerCase(Locale.getDefault());
        String status=dataSets.getStatus().toLowerCase(Locale.getDefault());
//
        if(ticketno.contains(searchString)){
            int startPos = ticketno.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_leadno.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_leadno.setText(spanString);

            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_leadno.setText(ticketno);
            }

        }
//
//
        if(phoneno.contains(searchString)){
            int startPos = phoneno.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_phone.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_phone.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_phone.setText(phoneno);
            }
        }

        if(status.contains(searchString)){
            int startPos = status.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_status.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_status.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_status.setText(dataSets.getStatus());
            }
        }

        if (name.contains(searchString)) {
            int startPos = name.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_custname.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_custname.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_custname.setText(dataSets.getCustomer());
            }
        }

    }

    @Override
    public int getItemCount() {
        return leadListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView name, phone;
//        public ImageView thumbnail;

        private ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

        public ForegroundColorSpan getForegroundColorSpan(){
            return foregroundColorSpan;
        }
        //tv_servicetype,
       TextView tv_leadno,tv_leadtype,tv_custname,tv_custcode,tv_address,tv_phone,tv_status,tv_product,tv_start_date,
                tv_end_date,tv_technician;


        ImageView iv_priority,iv_call;
        CardView lead_cv;

        public MyViewHolder(View view) {
            super(view);
            tv_leadno=view.findViewById(R.id.lead_item_tv_leadno);
            tv_leadtype=view.findViewById(R.id.lead_item_tv_leadtype);
            tv_custname=view.findViewById(R.id.lead_item_tv_custname);
            tv_custcode=view.findViewById(R.id.lead_item_tv_custcode);
            tv_address=view.findViewById(R.id.lead_item_tv_address);
            tv_phone=view.findViewById(R.id.lead_item_tv_phone);
            tv_status=view.findViewById(R.id.lead_item_tv_status);
            tv_product=view.findViewById(R.id.lead_item_tv_product);
            tv_start_date=view.findViewById(R.id.lead_item_tv_start_date);

            lead_cv=view.findViewById(R.id.lead_imem_lead_cv);

            lead_cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Lead_model dataSets=leadListFiltered.get(getAdapterPosition());


                    context.startActivity(new Intent(context,CloseLeadActivity.class)
                            .putExtra("leadno",dataSets.getLeadNo()));

                }
            });




//            tv_start_date=view.findViewById(R.id.lead_item_tv);
//                    tv_end_date,tv_technician


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "Click on ", Toast.LENGTH_SHORT).show();


                }
            });
            }

    }

    //Setting the arraylist
//    public void setListContent(ArrayList<Lead_model> list_task){
//        this.dataSet=list_task;
//        notifyItemRangeChanged(0,list_task.size());
//
//    }

    public interface JobsAdapterListener {
        void onTaskSelected(Lead_model Lead_model);

    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                searchString=charString;
                if (charString.isEmpty()) {
                    leadListFiltered = leadlist;

                } else {
                    //searchString=charString;
                    List<Lead_model> filteredList = new ArrayList<>();
                    for (Lead_model row : leadlist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLeadNo().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence) ||
                                 row.getStatus().toLowerCase().contains(charString.toLowerCase()) || row.getCustomer().toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(row);
                        }
                    }

                    leadListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = leadListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                leadListFiltered = (ArrayList<Lead_model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public String Parsedate(String time){

      //  "9\/2\/2019 7:03:05 PM"
        String inputPattern = "dd/MM/yyyy hh:mm:ss a";
        String outputPattern = "dd-MMM-yyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern,Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
            // System.out.println(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  str;
    }
}
