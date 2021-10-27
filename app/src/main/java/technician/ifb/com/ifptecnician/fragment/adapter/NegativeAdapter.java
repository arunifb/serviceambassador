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
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.fragment.dummy.AppormentModel;
import technician.ifb.com.ifptecnician.fragment.dummy.NegativeModel;
import technician.ifb.com.ifptecnician.negative_response.NegativeResponseDetails;


public class NegativeAdapter extends RecyclerView.Adapter<NegativeAdapter.MyViewHolder> {

    private View.OnClickListener mOnItemClickListener;
    SharedPreferences prefdetails;


    private List<NegativeModel> dataSet;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;


    public NegativeAdapter(Context context,List<NegativeModel> dataSet){
        this.context=context;
        this.dataSet=dataSet;
        inflater=LayoutInflater.from(context);
        prefdetails=context.getSharedPreferences("details",0);
    }


    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view=inflater.inflate(R.layout.item_task, viewGroup, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  final MyViewHolder myViewHolder, int i) {
        final NegativeModel dataSets=dataSet.get(i);
        myViewHolder.task_cv.setTag(i);
        holder.tv_address.setText(dataSets.getAddress().replace(","," "));
        holder.tv_callbook.setText(dataSets.getCallBookDate());
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
        holder.tv_rcnno.setText(dataSets.getRCNNo());
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());
        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());
//        if(dataSets.getPriority().equals("")){
//
//            holder.iv_priority.setImageResource(R.drawable.star_unfill);
//        }
//        else{
//
//            holder.iv_priority.setImageResource(R.drawable.ic_star_black_24dp);
//        }
        String Mediumm=dataSets.getMedium();

        if(Mediumm.equals("Phone") || Mediumm.equals("CSR Portal")  ){

            holder.item_task_ll_medium.setVisibility(View.VISIBLE);
        }
        else {
            holder.item_task_ll_medium.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_rcnno,tv_servicetype,tv_custname;
        ImageView iv_priority,iv_call;
        CardView task_cv;
        LinearLayout item_task_ll_medium;

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
            iv_call.setOnClickListener(this);
            item_task_ll_medium=view.findViewById(R.id.item_task_ll_medium);

//            itemView.setTag(this);
//            itemView.setOnClickListener(mOnItemClickListener);

        }

        @Override
        public void onClick(View v) {

            if (v==task_cv){

                System.out.println(getAdapterPosition());
                final NegativeModel dataSets=dataSet.get(getAdapterPosition());
                System.out.println(dataSets);
                Gson gson = new Gson();
                String json = gson.toJson(dataSets);
                System.out.println(json);
                SharedPreferences.Editor editor=prefdetails.edit();
                editor.putString("details",json);
                editor.apply();
              //  context.startActivity(new Intent(context,CustomerDetailsActivity.class));
                context.startActivity(new Intent(context,NegativeResponseDetails.class));
            }

            if (v==iv_call){

                //Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataSets.getRCNNo()));// Initiates the Intent
                // context.startActivity(intent);
            }
        }
    }

    //Setting the arraylist
    public void setListContent(ArrayList<NegativeModel> list_task){
        this.dataSet=list_task;
        notifyItemRangeChanged(0,list_task.size());

    }
}
