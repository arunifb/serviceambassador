package technician.ifb.com.ifptecnician.fragment.adapter;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.fragment.dummy.AppormentModel;
import technician.ifb.com.ifptecnician.fragment.dummy.Taskmodel;

import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;

public class AppormentAdapter  extends RecyclerView.Adapter<AppormentAdapter.MyViewHolder>
{


    Dbhelper dbhelper;
    Cursor cursor,del_cursor;
    SharedPreferences prefdetails;
//    private ArrayList<AppormentModel> dataSet=new ArrayList<>();

    private List<AppormentModel> tasklist;

    private List<AppormentModel> taskListFiltered;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    String searchString = "";

    public AppormentAdapter(Context context, List<AppormentModel> tasklist, JobsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.tasklist = tasklist;
        taskListFiltered=tasklist;
        inflater=LayoutInflater.from(context);
        dbhelper=new Dbhelper(context);
        prefdetails=context.getSharedPreferences("details",0);
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

        final AppormentModel dataSets=taskListFiltered.get(position);

        holder.tv_address.setText(dataSets.getAddress().replace(","," "));

        cursor=dbhelper.fetch_read_data(dataSets.getTicketNo());
        holder.task_cv.setCardBackgroundColor(Color.WHITE);
        holder.tv_address.setTextColor(Color.parseColor("#99000000"));

        if (cursor!=null)
        {
            if (cursor.moveToFirst()) {
                do {

                    String Status = cursor.getString(cursor.getColumnIndex("Status"));
                    String dates=cursor.getString(cursor.getColumnIndex("Date"));
                    String C_status=cursor.getString(cursor.getColumnIndex("C_Status"));

                    if (Status.equals(dataSets.getStatus())){
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
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());
        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());
        holder.tv_problemdescription.setText(dataSets.getProblemDescription());
        holder.tv_ageing.setText(dataSets.getAgeing()+" Days");


        System.out.println(dataSets.getPriority());

        if (dataSets.getPriority().equals("01")){
            holder.tv_escaltion.setText("Level 1 Escaltion");
        }
        else if(dataSets.getPriority().equals("02")){
            holder.tv_escaltion.setText("Level 2 Escaltion");
        }
        else if(dataSets.getPriority().equals("98")){
            holder.tv_escaltion.setText("Management Escaltion");
        }
        else if(dataSets.getPriority().equals("99")){
            holder.tv_escaltion.setText("Social Media Escaktion");
        }
        else{
            holder.tv_escaltion.setText("");
        }


        if(dataSets.getPriority().equals("")){

            holder.iv_priority.setImageResource(R.drawable.star_unfill);
        }
        else{

            holder.iv_priority.setImageResource(R.drawable.ic_star_black_24dp);
        }

        holder.tv_delercode.setText(dataSets.getDealerName());

        String PendingReason=dataSets.getPendingReason();
        if (!PendingReason.equals("")) {
            holder.tv_pending_status.setText(dataSets.getPendingReason() + " " + dataSets.getScheduledate());
        }
        else{

            holder.tv_pending_status.setVisibility(View.GONE);
        }

        String product=dataSets.getProduct();

        if (product.equals("AC"))
        {
            holder.tv_modelname.setText("Air Conditioner");
        }
        else  if (product.equals("CD"))
        {
            holder.tv_modelname.setText("Clothes Dryer");
        }
        else  if (product.equals("DW"))
        {
            holder.tv_modelname.setText("Dish Washer");
        }
        else  if (product.equals("IND"))
        {
            holder.tv_modelname.setText("Industrial Dish washer");
        }
        else  if (product.equals("KA"))
        {
            holder.tv_modelname.setText("Kitchen Appliances");
        }

        else  if (product.equals("RF"))
        {
            holder.tv_modelname.setText("Refrigerator");
        }
        else  if (product.equals("ST"))
        {
            holder.tv_modelname.setText("Stabilizer");
        }
        else  if (product.equals("WD"))
        {
            holder.tv_modelname.setText("Washer Dryer");
        }
        else  if (product.equals("WM"))
        {
            holder.tv_modelname.setText("Washing Machine");
        }
        else  if (product.equals("WP"))
        {
            holder.tv_modelname.setText("Water Purifier");
        }
        else  if (product.equals("MW"))
        {
            holder.tv_modelname.setText("Microwave Oven");
        }

        else {

            holder.tv_modelname.setText(product);
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

        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_rcnno,tv_servicetype,
                tv_custname,tv_ageing,tv_escaltion,tv_problemdescription,tv_delercode,tv_pending_status,tv_modelname;
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

            tv_delercode=view.findViewById(R.id.tv_delercode);
            tv_pending_status=view.findViewById(R.id.tv_pending_status);
            tv_modelname=view.findViewById(R.id.tv_modelname);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onTaskSelected(taskListFiltered.get(getAdapterPosition()));
                    final AppormentModel dataSets=taskListFiltered.get(getAdapterPosition());

                    Boolean check=dbhelper.ISreadtaskexits(dataSets.getTicketNo());

                    if (check.equals(true)) {

                        Toast.makeText(context, "Already Visited", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        System.out.println(dataSets);
                        Gson gson = new Gson();
                        String json = gson.toJson(dataSets);
                        System.out.println(json);
                        SharedPreferences.Editor editor=prefdetails.edit();
                        editor.putString("details",json);
                        editor.commit();
                        context.startActivity(new Intent(context,CustomerDetailsActivity.class));
                    }


                }
            });

            iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AppormentModel AppormentModel=taskListFiltered.get(getAdapterPosition());

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppormentModel.getRCNNo()));// Initiates the Intent
                    context.startActivity(intent);
                }
            });
        }
    }

    //Setting the arraylist
//    public void setListContent(ArrayList<AppormentModel> list_task){
//        this.dataSet=list_task;
//        notifyItemRangeChanged(0,list_task.size());
//
//    }

    public interface JobsAdapterListener {
        void onTaskSelected(AppormentModel AppormentModel);
    }


}
