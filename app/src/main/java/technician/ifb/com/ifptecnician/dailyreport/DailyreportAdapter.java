package technician.ifb.com.ifptecnician.dailyreport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.model.TasklistModel;

public class DailyreportAdapter extends RecyclerView.Adapter<DailyreportAdapter.MyViewHolder>
        implements Filterable {

    SharedPreferences prefdetails;
    private List<DailyreportModel> tasklist;
    private List<DailyreportModel> taskListFiltered;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;

    public DailyreportAdapter(Context context, List<DailyreportModel> tasklist, JobsAdapterListener listener) {
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

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dayreport, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final DailyreportModel dataSets=taskListFiltered.get(position);


        String status=dataSets.getStatus();
        System.out.println(status);

        if (status.equals("Closed") || status.equals("Pending")) {

            try {
                String str = dataSets.getChangeDate();
                //  String[] splited = str.split("\\s+");

                SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                Date dt1 = format1.parse(str);
                DateFormat timef = new SimpleDateFormat("hh:mm a");

                String times = timef.format(dt1);

                holder.tv_time.setText(times);
                holder.tv_calltype.setText(dataSets.getCallType());
                holder.tv_custaddress.setText(dataSets.getAddress());
                holder.tv_custname.setText(dataSets.getCustomerName());
                holder.tv_custphone.setText(dataSets.getRCNNo());
            } catch (Exception e) {

            }

        }


//        holder.tv_address.setText(dataSets.getAddress());
//        holder.tv_callbook.setText(dataSets.getCallBookDate());
//        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
//        holder.tv_rcnno.setText(dataSets.getRCNNo());
//        holder.tv_servicetype.setText(dataSets.getServiceType());
//        holder.tv_status.setText(dataSets.getStatus());
//        holder.tv_ticketno.setText(dataSets.getTicketNo());
//        holder.tv_custname.setText(dataSets.getCustomerName());
//        if(dataSets.getPriority().equals("")){
//
//            holder.iv_priority.setImageResource(R.drawable.star_unfill);
//        }
//        else{
//
//            holder.iv_priority.setImageResource(R.drawable.ic_star_black_24dp);
//        }


    }

    @Override
    public int getItemCount() {
        return taskListFiltered.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextView name, phone;
//        public ImageView thumbnail;

//        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_rcnno,tv_servicetype,tv_custname;
//        ImageView iv_priority,iv_call;
//        CardView task_cv;
         // TextView tv_id,tv_name,tv_assigndate,tv_closedate,tv_custname,tv_custphone;
           TextView tv_time,tv_calltype,tv_custname,tv_custphone,tv_custaddress;

        public MyViewHolder(View view) {
            super(view);
//            tv_calltype=(TextView)view.findViewById(R.id.tv_calltype);
//            tv_ticketno=(TextView)view.findViewById(R.id.tv_ticketno);
//            tv_address=(TextView)view.findViewById(R.id.tv_address);
//            tv_callbook=(TextView)view.findViewById(R.id.tv_callbookdate);
//            tv_status=(TextView)view.findViewById(R.id.tv_status);
//            tv_rcnno=(TextView)view.findViewById(R.id.tv_rcnno);
//            tv_servicetype=(TextView)view.findViewById(R.id.tv_servicetype);
//            tv_custname=(TextView)view.findViewById(R.id.tv_custname);
//            iv_priority=(ImageView)view.findViewById(R.id.iv_priority);
//            iv_call=(ImageView)view.findViewById(R.id.iv_call);
//            task_cv=(CardView)view.findViewById(R.id.task_cv);

//            tv_id=view.findViewById(R.id.itemdaily_tv_id);
//            tv_name=view.findViewById(R.id.itemdaily_tv_techname);
//            tv_assigndate=view.findViewById(R.id.itemdaily_tv_assigndate);
//            tv_closedate=view.findViewById(R.id.itemdaily_tv_closedate);
//            tv_custname=view.findViewById(R.id.itemdaily_tv_custname);
//            tv_custphone=view.findViewById(R.id.itemdaily_tv_custphone);

            tv_time=view.findViewById(R.id.itemday_time);
            tv_calltype=view.findViewById(R.id.itemday_calltype);
            tv_custname=view.findViewById(R.id.itemday_custname);
            tv_custphone=view.findViewById(R.id.itemday_custphone);
            tv_custaddress=view.findViewById(R.id.itemday_custaddress);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected contact in callback
//                    // listener.onTaskSelected(taskListFiltered.get(getAdapterPosition()));
//
//                    final DailyreportModel dataSets=taskListFiltered.get(getAdapterPosition());
//                    System.out.println(dataSets);
//                    Gson gson = new Gson();
//                    String json = gson.toJson(dataSets);
//                    System.out.println(json);
//                    SharedPreferences.Editor editor=prefdetails.edit();
//                    editor.putString("details",json);
//                    editor.commit();
//                    context.startActivity(new Intent(context,CustomerDetailsActivity.class));
//
//                }
//            });
//
//            iv_call.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    DailyreportModel tasklistModel=taskListFiltered.get(getAdapterPosition());
//
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tasklistModel.getRCNNo()));// Initiates the Intent
//                    context.startActivity(intent);
//                }
//            });
        }
    }

    //Setting the arraylist
//    public void setListContent(ArrayList<TasklistModel> list_task){
//        this.dataSet=list_task;
//        notifyItemRangeChanged(0,list_task.size());
//
//    }

    public interface JobsAdapterListener {
        void onTaskSelected(DailyreportModel tasklistModel);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    taskListFiltered = tasklist;
                } else {
                    List<DailyreportModel> filteredList = new ArrayList<>();
                    for (DailyreportModel row : tasklist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTicketNo().toLowerCase().contains(charString.toLowerCase()) || row.getRCNNo().contains(charSequence)
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
                taskListFiltered = (ArrayList<DailyreportModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

