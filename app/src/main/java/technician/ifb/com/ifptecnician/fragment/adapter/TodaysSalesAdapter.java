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
import technician.ifb.com.ifptecnician.TodaysalesDetailsActivity;
import technician.ifb.com.ifptecnician.fragment.dummy.AppormentModel;
import technician.ifb.com.ifptecnician.fragment.dummy.NegativeModel;
import technician.ifb.com.ifptecnician.fragment.dummy.TodaysalesModel;
import technician.ifb.com.ifptecnician.negative_response.NegativeResponseDetails;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;

public class TodaysSalesAdapter extends RecyclerView.Adapter<TodaysSalesAdapter.MyViewHolder> {

SharedPreferences prefdetails;

private List<TodaysalesModel> tasklist;

private List<TodaysalesModel> taskListFiltered;
private final LayoutInflater inflater;
        View view;
        MyViewHolder holder;
private Context context;
private JobsAdapterListener listener;
        String searchString = "";

public TodaysSalesAdapter(Context context, List<TodaysalesModel> tasklist, JobsAdapterListener listener) {
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
        .inflate(R.layout.item_today_sales, parent, false);

        return new MyViewHolder(itemView);
        }

@Override
public void onBindViewHolder(final MyViewHolder holder, final int position) {

final TodaysalesModel dataSets=taskListFiltered.get(position);

        holder.tv_address.setText(dataSets.getAddress().replace(","," "));
        holder.task_cv.setCardBackgroundColor(Color.WHITE);
        holder.tv_callbook.setText(dataSets.getCallBookDate());
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
        holder.tv_rcnno.setText(dataSets.getRCNNo());
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());
        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());
        holder.tv_closedate.setText(dataSets.getClosedDate());
        holder.item_tstv_model.setText(dataSets.getModel());
        holder.item_tstv_product.setText(dataSets.getProduct());
        String name =dataSets.getCustomerName().toLowerCase(Locale.getDefault());
        String ticketno=dataSets.getTicketNo();
        String phoneno=dataSets.getRCNNo();
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

    TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,
             tv_rcnno,tv_servicetype,tv_custname,tv_closedate,item_tstv_model,item_tstv_product;
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
        tv_closedate=view.findViewById(R.id.item_tstv_close);
        item_tstv_model=view.findViewById(R.id.item_tstv_model);
        item_tstv_product=view.findViewById(R.id.item_tstv_product);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onTaskSelected(taskListFiltered.get(getAdapterPosition()));
                final TodaysalesModel dataSets=taskListFiltered.get(getAdapterPosition());

                    System.out.println(dataSets);
                    Gson gson = new Gson();
                    String json = gson.toJson(dataSets);
                    System.out.println(json);
                    SharedPreferences.Editor editor=prefdetails.edit();
                    editor.putString("details",json);
                    editor.commit();
                    context.startActivity(new Intent(context, TodaysalesDetailsActivity.class));

            }
        });

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TodaysalesModel AppormentModel=taskListFiltered.get(getAdapterPosition());

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + AppormentModel.getRCNNo()));// Initiates the Intent
                context.startActivity(intent);
            }
        });
    }
}


public interface JobsAdapterListener {
    void onTaskSelected(TodaysalesModel AppormentModel);
}


}
