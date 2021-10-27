package technician.ifb.com.ifptecnician.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.gson.Gson;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.CustomerDetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.fragment.dummy.AdditiveModel;
import technician.ifb.com.ifptecnician.fragment.dummy.SpareModel;

public class AdditiveAdapter extends RecyclerView.Adapter<AdditiveAdapter.MyViewHolder> {

    private View.OnClickListener mOnItemClickListener;
    SharedPreferences prefdetails;
    private ArrayList<AdditiveModel> dataSet=new ArrayList<>();
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;


    public AdditiveAdapter(Context context){
        this.context=context;
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
        view=inflater.inflate(R.layout.item_sales, viewGroup, false);
        holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  final MyViewHolder myViewHolder, int i) {
        final AdditiveModel dataSets=dataSet.get(i);

        myViewHolder.task_cv.setTag(i);
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
        holder.tv_ticketno.setText(dataSets.getTicketno());

        holder.tv_name.setText(dataSets.getAdd());
        holder.tv_product.setText(dataSets.getProduct());
        holder.tv_productname.setText(dataSets.getProductName());
        holder.tv_status.setText(dataSets.getMachineStaus());
        holder.tv_bookdate.setText(dataSets.getCallBookDate());
        holder.tv_closedate.setText((dataSets.getClosedDate()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_calltype,tv_ticketno,tv_name,tv_product,tv_productname,tv_status,tv_bookdate,tv_closedate;

        CardView task_cv;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_calltype=(TextView)view.findViewById(R.id.tv_calltype);
            tv_ticketno=(TextView)view.findViewById(R.id.tv_ticketno);
            tv_name=(TextView)view.findViewById(R.id.tv_name);
            tv_product=(TextView)view.findViewById(R.id.tv_product);
            tv_productname=(TextView)view.findViewById(R.id.tv_productname);
            tv_status=(TextView)view.findViewById(R.id.tv_machine_status);
            tv_bookdate=(TextView)view.findViewById(R.id.tv_bookdate);
            tv_closedate=(TextView)view.findViewById(R.id.tv_closedate);
            task_cv=(CardView)view.findViewById(R.id.task_cv);
            task_cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v==task_cv){

                System.out.println(getAdapterPosition());
                final AdditiveModel dataSets=dataSet.get(getAdapterPosition());
                System.out.println(dataSets);

                Gson gson = new Gson();
                String json = gson.toJson(dataSets);


                System.out.println(json);

//                SharedPreferences.Editor editor=prefdetails.edit();
//                editor.putString("details",json);
//                editor.commit();
//                context.startActivity(new Intent(context,CustomerDetailsActivity.class));

            }



        }
    }

    //Setting the arraylist
    public void setListContent(ArrayList<AdditiveModel> list_task){
        this.dataSet=list_task;
        notifyItemRangeChanged(0,list_task.size());

    }

}
