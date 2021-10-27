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
import technician.ifb.com.ifptecnician.allurl.AllUrl;
import technician.ifb.com.ifptecnician.model.NoticationModel;
import technician.ifb.com.ifptecnician.model.NoticationModel;
import technician.ifb.com.ifptecnician.negative_response.NegativeResponseDetails;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>

        implements Filterable {

    String Ticktno="";
    Dbhelper dbhelper;
    Cursor cursor,del_cursor;
    SharedPreferences prefdetails;
//    private ArrayList<NoticationModel> dataSet=new ArrayList<>();

    private List<NoticationModel> tasklist;
    private List<NoticationModel> taskListFiltered;
    private final LayoutInflater inflater;
    View view;
    Tasklist_Adapter.MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    String searchString = "";
    ErrorDetails errorDetails;


    public NotificationAdapter(Context context, List<NoticationModel> tasklist, JobsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.tasklist = tasklist;
        taskListFiltered=tasklist;
        inflater=LayoutInflater.from(context);
        dbhelper=new Dbhelper(context);
        prefdetails=context.getSharedPreferences("details",0);
        errorDetails=new ErrorDetails();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final NoticationModel dataSets=taskListFiltered.get(position);

        holder.tv_address.setText(dataSets.getAddress());

        cursor=dbhelper.fetch_read_data(dataSets.getTicketNo());
        holder.task_cv.setCardBackgroundColor(Color.WHITE);
        holder.tv_address.setTextColor(Color.parseColor("#99000000"));
        Ticktno=dataSets.getTicketNo();

        //  8240295518

        if (cursor!=null)
        {
            if (cursor.moveToFirst()) {
                do {

                    String Status = cursor.getString(cursor.getColumnIndex("Status"));
                    String dates=cursor.getString(cursor.getColumnIndex("Date"));
                    String ChangeTime=cursor.getString(cursor.getColumnIndex("ChangeTime"));
                    String C_status=cursor.getString(cursor.getColumnIndex("C_Status"));

                    if (ChangeTime.equals(dataSets.getChangeDate())){

                        holder.task_cv.setCardBackgroundColor(Color.LTGRAY);
                        holder.tv_address.setText( C_status+" | "+dates);
                        holder.tv_address.setTextColor(Color.parseColor("#b71c1c"));
                        holder.tv_address.setElegantTextHeight(true);
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

        if (dataSets.getRCNNo().equals("")){

            holder.iv_call.setVisibility(View.GONE);
        }
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());
        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());

        if (!dataSets.getProblemDescription().equals("")){

            holder.tv_problemdescription.setText(dataSets.getProblemDescription());
        }
        else {

            holder.tv_problemdescription.setVisibility(View.GONE);
        }

        holder.tv_ageing.setText(dataSets.getAgeing()+" Days");


        System.out.println(dataSets.getPriority());

        if (dataSets.getPriority().equals("")){
            holder.tv_escaltion.setText("");
        }
        else {
            holder.tv_escaltion.setText("Escalate ");
        }

//        if (dataSets.getPriority().equals("01")){
//            holder.tv_escaltion.setText("Level 1 Escalation");
//        }
//        else if(dataSets.getPriority().equals("02")){
//            holder.tv_escaltion.setText("Level 2 Escalation");
//        }
//        else if(dataSets.getPriority().equals("98")){
//            holder.tv_escaltion.setText("Management Escalation");
//        }
//        else if(dataSets.getPriority().equals("99")){
//            holder.tv_escaltion.setText("Social Media Escalation");
//        }
//        else{
//            holder.tv_escaltion.setText("");
//        }


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
//        public TextView name, phone;
//        public ImageView thumbnail;

        private ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

        public ForegroundColorSpan getForegroundColorSpan(){
            return foregroundColorSpan;
        }

        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_rcnno,tv_servicetype,tv_custname,tv_ageing,tv_escaltion,tv_problemdescription;
        ImageView iv_priority,iv_call;
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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final NoticationModel dataSets=taskListFiltered.get(getAdapterPosition());
                    String status=dataSets.getStatus();
                    System.out.println("Status"+status);

                    Boolean check=dbhelper.ISreadtaskexits(dataSets.getTicketNo());

                    if (check.equals(true)) {
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        String tss = dfs.format(c);
                        //   Errorlog("Click%20On%20Item",Ticktno,"",tss);
                        //  Toast.makeText(context, "Already Visited", Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();
                        String json = gson.toJson(dataSets);
                        System.out.println(json);
                        SharedPreferences.Editor editor=prefdetails.edit();
                        editor.putString("details",json);

                        editor.commit();
                        context.startActivity(new Intent(context,DetailsActivity.class));
                        //  listener.onTaskSelected(taskListFiltered.get(getAdapterPosition()));
                    }

                    else{

                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        String tss = dfs.format(c);


                        Gson gson = new Gson();
                        String json = gson.toJson(dataSets);
                        System.out.println(json);
                        SharedPreferences.Editor editor=prefdetails.edit();
                        editor.putString("details",json);
                        editor.commit();
                        if (status.equals("Negative Response from Custome")){
                            context.startActivity(new Intent(context,NegativeResponseDetails.class));
                        }
                        else{
                            context.startActivity(new Intent(context,CustomerDetailsActivity.class));
                        }

                        //   context.startActivity(new Intent(context,CustomerDetailsActivity.class));
                        //  listener.onTaskSelected(taskListFiltered.get(getAdapterPosition()));
                    }


                }
            });

            iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    NoticationModel NoticationModel=taskListFiltered.get(getAdapterPosition());

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + NoticationModel.getRCNNo()));// Initiates the Intent
                    context.startActivity(intent);
                }
            });
        }
    }



    public interface JobsAdapterListener {
        void onTaskSelected(NoticationModel NoticationModel);

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
                    List<NoticationModel> filteredList = new ArrayList<>();
                    for (NoticationModel row : tasklist) {

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
                taskListFiltered = (ArrayList<NoticationModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}

