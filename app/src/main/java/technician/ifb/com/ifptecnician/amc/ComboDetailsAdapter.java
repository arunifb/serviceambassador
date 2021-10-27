package technician.ifb.com.ifptecnician.amc;

import android.content.Context;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;

import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;

public class ComboDetailsAdapter extends RecyclerView.Adapter<ComboDetailsAdapter.MyViewHolder>

       {


        SharedPreferences prefdetails;
        private List<ComboDetailsModel> tasklist;
        private List<ComboDetailsModel> taskListFiltered;
        private final LayoutInflater inflater;
        View view;
        MyViewHolder holder;
        private Context context;




    public ComboDetailsAdapter(Context context, List<ComboDetailsModel> tasklist) {
            this.context = context;
            this.tasklist = tasklist;
            taskListFiltered=tasklist;
            inflater=LayoutInflater.from(context);

            prefdetails=context.getSharedPreferences("amcdetails",0);

        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_combo, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final ComboDetailsModel dataSets=taskListFiltered.get(position);

            holder.tv_name.setText(dataSets.getPrdDes());
            holder.tv_code.setText(dataSets.getProductId());
            holder.tv_qty.setText(dataSets.getQuantity());

        }

        @Override
        public int getItemCount() {
            return taskListFiltered.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            private final ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);

            public ForegroundColorSpan getForegroundColorSpan(){
                return foregroundColorSpan;
            }

            TextView tv_name,tv_code,tv_qty;


            public MyViewHolder(View view) {
                super(view);

                tv_name=view.findViewById(R.id.tv_name);
                tv_code=view.findViewById(R.id.tv_code);
                tv_qty=view.findViewById(R.id.tv_qty);

            }
        }


        public interface JobsAdapterListener {
            void onTaskSelected(AmcleadModel tasklistModel);

        }

        public interface Altclick{

            void onAltclick(String refno,String smstype,String altno,String ticketno);
        }

        public interface Rcnclick{

            void onRcnclick(String refno,String smstype,String altno,String ticketno);
        }


    }


