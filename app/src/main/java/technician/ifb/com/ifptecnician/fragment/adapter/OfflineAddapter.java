package technician.ifb.com.ifptecnician.fragment.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;

import android.telecom.Call;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.DetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.TodaysalesDetailsActivity;
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.fragment.dummy.OfflineModel;

public class OfflineAddapter extends RecyclerView.Adapter<OfflineAddapter.MyViewHolder> {

    SharedPreferences prefdetails;

    private List<OfflineModel> tasklist;

    private List<OfflineModel> taskListFiltered;
    private final LayoutInflater inflater;
    private ViewClick viewClick;
    View view;
    MyViewHolder holder;
    private Context context;

    String searchString = "";

    public OfflineAddapter(Context context, List<OfflineModel> tasklist,ViewClick viewClick) {
        this.context = context;
        this.tasklist = tasklist;
        taskListFiltered=tasklist;
        this.viewClick=viewClick;
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
                .inflate(R.layout.item_offlineworks, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final OfflineModel dataSets=taskListFiltered.get(position);

        holder.tv_address.setText(dataSets.getAddress().replace(","," "));
        holder.task_cv.setCardBackgroundColor(Color.WHITE);
        holder.tv_callbook.setText(dataSets.getCallBookDate());
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
        holder.tv_rcnno.setText(dataSets.getRCNNo());
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());
        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());
        holder.item_tstv_model.setText(dataSets.getModel());
        holder.item_tstv_ageing.setText(dataSets.getAgeing());
        String name =dataSets.getCustomerName().toLowerCase(Locale.getDefault());
        String ticketno=dataSets.getTicketNo();
        String phoneno=dataSets.getRCNNo();
        String status=dataSets.getStatus().toLowerCase(Locale.getDefault());

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

        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,
                tv_rcnno,tv_servicetype,tv_custname,item_tstv_model,item_tstv_product,item_tstv_ageing;
        ImageView iv_call;
        CardView task_cv;

        public MyViewHolder(View view) {
            super(view);
            tv_calltype=view.findViewById(R.id.item_tstv_calltype);
            tv_ticketno=view.findViewById(R.id.item_tstv_ticketno);
            tv_address=view.findViewById(R.id.item_tstv_address);
            tv_callbook=view.findViewById(R.id.item_tstv_callbookdate);
            tv_status=view.findViewById(R.id.item_tstv_status);
            tv_rcnno=view.findViewById(R.id.item_tstv_rcnno);
            tv_servicetype=view.findViewById(R.id.item_tstv_servicetype);
            tv_custname=view.findViewById(R.id.item_tstv_custname);
            iv_call=view.findViewById(R.id.item_tsiv_call);
            task_cv=view.findViewById(R.id.item_tstask_cv);
            item_tstv_model=view.findViewById(R.id.item_tstv_model);
            item_tstv_product=view.findViewById(R.id.item_tstv_product);
            item_tstv_ageing=view.findViewById(R.id.item_tstv_ageing);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    viewClick.onViewClick();
                }
            });

            iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OfflineModel AppormentModel=taskListFiltered.get(getAdapterPosition());

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppormentModel.getRCNNo()));// Initiates the Intent
                    context.startActivity(intent);
                }
            });
        }
    }

    public interface ViewClick{
        void onViewClick();


    }

}
