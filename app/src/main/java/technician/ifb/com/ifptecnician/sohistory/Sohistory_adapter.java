package technician.ifb.com.ifptecnician.sohistory;

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
import technician.ifb.com.ifptecnician.adapter.Tasklist_Adapter;
import technician.ifb.com.ifptecnician.model.TasklistModel;
import technician.ifb.com.ifptecnician.negative_response.NegativeResponseDetails;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;

public class Sohistory_adapter extends RecyclerView.Adapter<Sohistory_adapter.MyViewHolder> {

    String Ticktno="";
    Dbhelper dbhelper;
    Cursor cursor,del_cursor;
    SharedPreferences prefdetails;
    private List<Sohistory_model> tasklist;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    String searchString = "";
    ErrorDetails errorDetails;
    String dbversion="";

    public Sohistory_adapter(Context context, List<Sohistory_model> tasklist) {
        this.context = context;

        this.tasklist = tasklist;

        inflater=LayoutInflater.from(context);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_so_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Sohistory_model dataSets=tasklist.get(position);

        holder.tv_date.setText(dataSets.getCallBookDate());
        holder.tv_problem.setText(dataSets.getProblemDescription());
        holder.tv_ticketno.setText(dataSets.getTicketNo());
    }

    @Override
    public int getItemCount() {
        return tasklist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date,tv_ticketno,tv_problem;


        public MyViewHolder(View view) {
            super(view);
            tv_date=(TextView)view.findViewById(R.id.item_so_date);
            tv_ticketno=(TextView)view.findViewById(R.id.item_so_ticketno);
            tv_problem=view.findViewById(R.id.item_so_problem);


        }
    }




}
