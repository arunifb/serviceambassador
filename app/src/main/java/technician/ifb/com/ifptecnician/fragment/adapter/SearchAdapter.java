package technician.ifb.com.ifptecnician.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.gcm.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.fragment.dummy.SearchModel;
import technician.ifb.com.ifptecnician.fragment.dummy.SearchModel;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    SharedPreferences prefdetails;
    private List<SearchModel> dataSet;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;

    public SearchAdapter(Context context,List<SearchModel> dataSet){
        this.context=context;
        this.dataSet=dataSet;
        inflater=LayoutInflater.from(context);
        prefdetails=context.getSharedPreferences("details",0);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view=inflater.inflate(R.layout.item_task, viewGroup, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  final MyViewHolder myViewHolder, final int i) {

        final SearchModel dataSets=dataSet.get(i);
        myViewHolder.task_cv.setTag(i);

        holder.tv_address.setText(dataSets.getAddress().replace(","," "));
        holder.tv_callbook.setText(dataSets.getCallBookDate());
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
        holder.tv_rcnno.setText(dataSets.getRCNNo());
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());
        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());
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


    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_rcnno,tv_servicetype,
                tv_custname,tv_delercode,tv_pending_status,tv_modelname;
        ImageView iv_priority,iv_call;
        LinearLayout item_task_ll_medium;
        CardView task_cv;

        public MyViewHolder(View itemView) {
            super(itemView);
            //   itemView.setOnClickListener(this);
//            user_name=(TextView)itemView.findViewById(R.id.user_name);
//            content=(TextView)itemView.findViewById(R.id.content);
//            time=(TextView)itemView.findViewById(R.id.time);
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
            task_cv.setOnClickListener(this);

            tv_delercode=view.findViewById(R.id.tv_delercode);
            tv_pending_status=view.findViewById(R.id.tv_pending_status);
            tv_modelname=view.findViewById(R.id.tv_modelname);

            iv_call.setOnClickListener(this);

//            itemView.setTag(this);
//            itemView.setOnClickListener(mOnItemClickListener);

        }

        @Override
        public void onClick(View v) {

            if (v==task_cv){

                System.out.println(getAdapterPosition());
                final SearchModel dataSets=dataSet.get(getAdapterPosition());
                System.out.println(dataSets);

                Gson gson = new Gson();
                String json = gson.toJson(dataSets);

                SharedPreferences.Editor editor=prefdetails.edit();
                editor.putString("details",json);
                editor.commit();
                context.startActivity(new Intent(context,CustomerDetailsActivity.class));

            }

            if (v==iv_call){

                //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataSets.getRCNNo()));// Initiates the Intent
                // context.startActivity(intent);
            }

        }
    }

//    //Setting the arraylist
//    public void setListContent(ArrayList<SearchModel> list_task){
//        this.dataSet=list_task;
//        notifyItemRangeChanged(0,list_task.size());
//
//    }

}
