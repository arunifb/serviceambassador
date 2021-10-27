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
import technician.ifb.com.ifptecnician.fragment.dummy.AppormentModel;
import technician.ifb.com.ifptecnician.fragment.dummy.SpareModel;

public class SpareAdapter extends RecyclerView.Adapter<SpareAdapter.MyViewHolders> {

    private View.OnClickListener mOnItemClickListener;
    private ArrayList<SpareModel> spareSet=new ArrayList<>();

    private final LayoutInflater inflater;
    View view;
    MyViewHolders holder;
    private Context context;


    public SpareAdapter(Context context){
        this.context=context;
        inflater=LayoutInflater.from(context);
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
    public MyViewHolders onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view=inflater.inflate(R.layout.item_sales, viewGroup, false);
        holder=new MyViewHolders(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull  final MyViewHolders myViewHolder, int i) {

        final SpareModel dataSets=spareSet.get(i);
        myViewHolder.task_cv.setTag(i);
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
        holder.tv_ticketno.setText(dataSets.getTicketno());

        holder.tv_name.setText(dataSets.getSpare());
        holder.tv_product.setText(dataSets.getProduct());
        holder.tv_productname.setText(dataSets.getProductName());
        holder.tv_status.setText(dataSets.getMachineStaus());
        holder.tv_bookdate.setText(dataSets.getCallBookDate());
        holder.tv_closedate.setText((dataSets.getClosedDate()));
    }

    @Override
    public int getItemCount() {
        System.out.println("Arraysize-->"+spareSet.size());
        return spareSet.size();


    }

    class MyViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView tv_calltype,tv_ticketno,tv_name,tv_product,tv_productname,tv_status,tv_bookdate,tv_closedate;

        CardView task_cv;


        public MyViewHolders(View itemView) {
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

        }

        @Override
        public void onClick(View v) {

            if (v==task_cv){


            }

        }
    }

    //Setting the arraylist
    public void setListContent(ArrayList<SpareModel> list_task){
        this.spareSet=list_task;
        notifyItemRangeChanged(0,list_task.size());

    }

}
