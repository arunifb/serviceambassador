package technician.ifb.com.ifptecnician.exchange.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.exchange.ExchangeMain;
import technician.ifb.com.ifptecnician.exchange.model.Exchange_list_model;
import technician.ifb.com.ifptecnician.model.TasklistModel;

public class ExchangeListAdapter extends RecyclerView.Adapter<ExchangeListAdapter.MyViewHolder>

        implements Filterable {

    String Ticktno="";

    private List<Exchange_list_model> tasklist;
    private List<Exchange_list_model> taskListFiltered;
    private final LayoutInflater inflater;
    View view;
    private Context context;
    private JobsAdapterListener listener;

    String searchString = "";
    SharedPreferences prefdetails;

    public ExchangeListAdapter(Context context, List<Exchange_list_model> tasklist, JobsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.tasklist = tasklist;
        taskListFiltered=tasklist;
        inflater=LayoutInflater.from(context);
        prefdetails=context.getSharedPreferences("details",0);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exchange_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Exchange_list_model dataSets=taskListFiltered.get(position);

        holder.tv_address.setText(dataSets.getAddress().replace(","," "));


        holder.task_cv.setCardBackgroundColor(Color.WHITE);
        holder.tv_address.setTextColor(Color.parseColor("#99000000"));
        Ticktno=dataSets.getTicketNo();

        holder.tv_callbook.setText(dataSets.getCallBookDate());
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
        holder.tv_rcnno.setText(dataSets.getRCNNo());

        if (dataSets.getRCNNo().equals("")){

            holder.iv_call.setVisibility(View.GONE);
        }
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());

        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());
        holder.tv_problemdescription.setText(dataSets.getProblemDescription());

        holder.tv_ageing.setText(dataSets.getAgeing()+" Days");


        System.out.println(dataSets.getPriority());

        if (dataSets.getPriority().equals("")){
            holder.tv_escaltion.setText("");
        }
        else {
            holder.tv_escaltion.setText("Escalate ");
        }


        if(dataSets.getPriority().equals("")){

            holder.iv_priority.setImageResource(R.drawable.star_unfill);
        }
        else{

            holder.iv_priority.setImageResource(R.drawable.ic_star_black_24dp);
        }

        String name =dataSets.getCustomerName().toLowerCase(Locale.getDefault());
        String ticketno=dataSets.getTicketNo();//.toLowerCase(Locale.getDefault());
        String phoneno=dataSets.getRCNNo();//.toLowerCase(Locale.getDefault());
        String status=dataSets.getStatus().toLowerCase(Locale.getDefault());

        if(ticketno.contains(searchString)){
            int startPos = ticketno.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_ticketno.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_ticketno.setText(spanString);

            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_ticketno.setText(ticketno);
            }

        }


        if(phoneno.contains(searchString)){
            int startPos = phoneno.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_rcnno.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_rcnno.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_rcnno.setText(phoneno);
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
                holder.tv_custname.setText(dataSets.getCustomerName());
            }
        }

    }

    @Override
    public int getItemCount() {
        return taskListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

        public ForegroundColorSpan getForegroundColorSpan(){
            return foregroundColorSpan;
        }

        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_scfeedbackcatvalue,tv_rcnno,tv_servicetype,tv_custname,tv_ageing,tv_escaltion,tv_problemdescription,tv_alt;
        ImageView iv_priority,iv_call,iv_rcn,iv_alt,iv_thum;
        LinearLayout ll_alt,ll_nps;
        CardView task_cv;

        public MyViewHolder(View view) {
            super(view);
            tv_calltype=(TextView)view.findViewById(R.id.tv_calltype);
            tv_ticketno=(TextView)view.findViewById(R.id.tv_ticketno);
            tv_address=(TextView)view.findViewById(R.id.tv_address);
            tv_callbook=(TextView)view.findViewById(R.id.tv_callbookdate);
            tv_status=(TextView)view.findViewById(R.id.tv_status);
            tv_rcnno=(TextView)view.findViewById(R.id.tv_rcnno);
            tv_servicetype=(TextView)view.findViewById(R.id.tv_servicetype);
            tv_custname=(TextView)view.findViewById(R.id.tv_custname);
            iv_priority=(ImageView)view.findViewById(R.id.iv_priority);
            iv_call=(ImageView)view.findViewById(R.id.iv_call);
            task_cv=(CardView)view.findViewById(R.id.task_cv);
            tv_ageing=view.findViewById(R.id.tv_ageing);
            tv_escaltion=view.findViewById(R.id.tv_escaltion);
            tv_problemdescription=view.findViewById(R.id.tv_problemdescription);
            iv_alt=view.findViewById(R.id.alt_iv_message);
            tv_alt=view.findViewById(R.id.tv_altno);
            iv_rcn=view.findViewById(R.id.rcn_iv_message);
            ll_alt=view.findViewById(R.id.ll_altview);
            iv_thum=view.findViewById(R.id.iv_thum);
            tv_scfeedbackcatvalue=view.findViewById(R.id.tv_scfeedbackcatvalue);
            ll_nps=view.findViewById(R.id.ll_nps);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                        final Exchange_list_model dataSets=taskListFiltered.get(getAdapterPosition());
                        String status=dataSets.getStatus();
                        System.out.println("Status"+status);

                        //  Toast.makeText(context, "Already Visited", Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();
                        String json = gson.toJson(dataSets);
                        System.out.println(json);
                        SharedPreferences.Editor editor=prefdetails.edit();
                        editor.putString("details",json);

                        editor.apply();
                        context.startActivity(new Intent(context, ExchangeMain.class));
                }
            });

            iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Exchange_list_model tasklistModel=taskListFiltered.get(getAdapterPosition());

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tasklistModel.getRCNNo()));// Initiates the Intent
                    context.startActivity(intent);
                }
            });



        }
    }


    public interface JobsAdapterListener {
        void onTaskSelected(TasklistModel tasklistModel);

    }

    public interface Altclick{

        void onAltclick(String refno,String smstype,String altno);
    }

    public interface Rcnclick{

        void onRcnclick(String refno,String smstype,String altno);
    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                searchString=charString;
                if (charString.isEmpty()) {
                    taskListFiltered = tasklist;

                } else {
                    //searchString=charString;
                    List<Exchange_list_model> filteredList = new ArrayList<>();
                    for (Exchange_list_model row : tasklist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTicketNo().toLowerCase().contains(charString.toLowerCase()) || row.getRCNNo().contains(charSequence) || row.getCallType().contains(charSequence)
                                || row.getStatus().toLowerCase().contains(charString.toLowerCase()) || row.getCustomerName().toLowerCase().contains(charString.toLowerCase())
                                || row.getPotential_exchange_flag().toLowerCase().contains(charString.toLowerCase()))

                        {
                            filteredList.add(row);
                        }
                    }

                    taskListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = taskListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                taskListFiltered = (ArrayList<Exchange_list_model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
