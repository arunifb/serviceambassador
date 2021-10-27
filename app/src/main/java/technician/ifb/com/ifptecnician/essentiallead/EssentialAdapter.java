package technician.ifb.com.ifptecnician.essentiallead;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.DetailsActivity;
import technician.ifb.com.ifptecnician.R;

import technician.ifb.com.ifptecnician.appointment.Create_Appointment;

import technician.ifb.com.ifptecnician.negative_response.NegativeResponseDetails;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;

public class EssentialAdapter  extends RecyclerView.Adapter<EssentialAdapter.MyViewHolder>

        implements Filterable {

    String Ticktno="";
    Dbhelper dbhelper;
    Cursor cursor,del_cursor;
    SharedPreferences prefdetails;
    private List<EssentialModel> tasklist;
    private List<EssentialModel> taskListFiltered;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    private Altclick altclick;
    private Rcnclick rcnclick;
    String searchString = "";
    ErrorDetails errorDetails;

    // call to customer
    private Calltocustomer calltocustomer;


    public EssentialAdapter(Context context, List<EssentialModel> tasklist, JobsAdapterListener listener, Altclick altclick, Rcnclick rcnclick, Calltocustomer calltocustomer) {
        this.context = context;
        this.listener = listener;
        this.altclick=altclick;
        this.rcnclick=rcnclick;
        this.tasklist = tasklist;
        taskListFiltered=tasklist;
        this.calltocustomer=calltocustomer;
        inflater=LayoutInflater.from(context);
        dbhelper=new Dbhelper(context);
        prefdetails=context.getSharedPreferences("essential_details",0);
        errorDetails=new ErrorDetails();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_essential_lead, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final EssentialModel dataSets=taskListFiltered.get(position);

        holder.tv_address.setText(dataSets.getAddress().replace(","," "));


        cursor=dbhelper.fetch_read_data(dataSets.getTicketNo());
        holder.task_cv.setCardBackgroundColor(Color.WHITE);
        holder.tv_address.setTextColor(Color.parseColor("#99000000"));
        Ticktno=dataSets.getTicketNo();

        //  8240295518

        System.out.println(dataSets.getStatus());

        if (cursor!=null)
        {
            if (cursor.moveToFirst()) {
                do {

                    String Status = cursor.getString(cursor.getColumnIndex("Status"));
                    String dates=cursor.getString(cursor.getColumnIndex("Date"));
                    String ChangeTime=cursor.getString(cursor.getColumnIndex("ChangeTime"));
                    String C_status=cursor.getString(cursor.getColumnIndex("C_Status"));

                    if (ChangeTime.equals(dataSets.getChangeDate())){
                        if ( C_status.equals("Resolved (soft closure)") ){

                            if (dataSets.getCallType().equals("MANDATORY CALLS") || dataSets.getCallType().equals("COURTESY VISIT"))
                            {

                            }

                            else {

                                if (!dataSets.getTelePhone().equals("")) {
                                    holder.ll_alt.setVisibility(View.VISIBLE);
                                    holder.tv_alt.setText(dataSets.getTelePhone());
                                }

                                if (!dataSets.getRCNNo().equals("")) {
                                    holder.iv_rcn.setVisibility(View.VISIBLE);
                                    holder.iv_calender.setVisibility(View.GONE);
                                }
                            }
                        }
                        holder.task_cv.setCardBackgroundColor(Color.LTGRAY);
                        holder.tv_address.setText( C_status+" | "+dates);
                        holder.tv_address.setTextColor(Color.parseColor("#b71c1c"));
                        holder.tv_address.setElegantTextHeight(true);
                        holder.iv_calender.setVisibility(View.GONE);
                    }

                    else {

                        del_cursor=dbhelper.delete_read_data(dataSets.getTicketNo());
                        holder.task_cv.setCardBackgroundColor(Color.WHITE);

                    }
                }
                while (cursor.moveToNext());
            }

        }

        holder.tv_callbook.setText(dataSets.getCallBookDate());
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
        holder.tv_rcnno.setText(dataSets.getRCNNo());


        //  holder.tv_modelname.setText(dataSets.getProduct());

        if (dataSets.getRCNNo().equals("")){

            holder.iv_call.setVisibility(View.GONE);
        }
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());

        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());



        holder.tv_ageing.setText(dataSets.getAgeing()+" Days");


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

        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_scfeedbackcatvalue,
                tv_rcnno,tv_servicetype,tv_custname,tv_ageing,tv_escaltion,tv_problemdescription,tv_alt;
        ImageView iv_call,iv_rcn,iv_alt,iv_thum,iv_calender;
        LinearLayout ll_alt,item_task_ll_medium;
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

            item_task_ll_medium=view.findViewById(R.id.item_task_ll_medium);

            iv_calender=view.findViewById(R.id.iv_calender);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final EssentialModel dataSets=taskListFiltered.get(getAdapterPosition());
                    String status=dataSets.getStatus();
                    System.out.println("Status"+status);

                        Gson gson = new Gson();
                        String json = gson.toJson(dataSets);
                        System.out.println(json);
                        SharedPreferences.Editor editor=prefdetails.edit();
                        editor.putString("essential_details",json);
                        editor.commit();
                        context.startActivity(new Intent(context,Entryessential.class));


                    }



            });

            iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    EssentialModel EssentialModel=taskListFiltered.get(getAdapterPosition());

                    String rcn=EssentialModel.getRCNNo();
                    String ref=EssentialModel.getRefId();
                    String ticketno=EssentialModel.getTicketNo();
                    calltocustomer.Onclickcall(rcn,ticketno);

                }
            });

            iv_rcn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EssentialModel EssentialModel=taskListFiltered.get(getAdapterPosition());
                    String rcn=EssentialModel.getRCNNo();
                    String ref=EssentialModel.getRefId();
                    String ticketno=EssentialModel.getTicketNo();

                    rcnclick.onRcnclick(ref,"",rcn,ticketno);
                }
            });




        }
    }


    public interface Calltocustomer{

        void Onclickcall(String mobile,String ticketno);

    }


    public interface JobsAdapterListener {
        void onTaskSelected(EssentialModel EssentialModel);

    }

    public interface Altclick{

        void onAltclick(String refno,String smstype,String altno,String ticketno);
    }

    public interface Call{

        void onAltclick(String refno,String smstype,String altno,String ticketno);
    }


    public interface Rcnclick{

        void onRcnclick(String refno,String smstype,String altno,String ticketno);
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
                    List<EssentialModel> filteredList = new ArrayList<>();
                    for (EssentialModel row : tasklist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTicketNo().toLowerCase().contains(charString.toLowerCase()) || row.getRCNNo().contains(charSequence) || row.getCallType().contains(charSequence)
                                || row.getStatus().toLowerCase().contains(charString.toLowerCase()) || row.getCustomerName().toLowerCase().contains(charString.toLowerCase()))
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
                taskListFiltered = (ArrayList<EssentialModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
