package technician.ifb.com.ifptecnician.amc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;

public class Punch_amc_adapter extends RecyclerView.Adapter<Punch_amc_adapter.MyViewHolder >implements Filterable {

    String Ticktno="";

    SharedPreferences prefdetails;
    private List<Punched_amc_model> tasklist;
    private List<Punched_amc_model> taskListFiltered;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    private Altclick altclick;
    private Rcnclick rcnclick;
    String searchString = "";
    ErrorDetails errorDetails;
    String dbversion="";




    public Punch_amc_adapter(Context context, List<Punched_amc_model> tasklist, JobsAdapterListener listener, Altclick altclick, Rcnclick rcnclick) {
        this.context = context;
        this.listener = listener;
        this.altclick=altclick;
        this.rcnclick=rcnclick;
        this.tasklist = tasklist;
        taskListFiltered=tasklist;
        inflater=LayoutInflater.from(context);
        prefdetails=context.getSharedPreferences("amcdetails",0);
        errorDetails=new ErrorDetails();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_punched_amc, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Punched_amc_model dataSets=taskListFiltered.get(position);


        holder.tv_name.setText(dataSets.getCustomer_name());
        holder.tv_amc_extended_wty.setText(dataSets.getAmc_extended_wty());
        holder.tv_icrno.setText(dataSets.getICRNO());


        if (dataSets.getRcn().length()==0){
            holder.tv_phone.setText(dataSets.getRcn());

        }
        else{
            holder.tv_phone.setText(dataSets.getAltno());
        }

        holder.tv_serialno.setText(dataSets.getSerial_no());


            try{
                SimpleDateFormat spf=new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
                Date newDate= null;
                try {
                    newDate = spf.parse(dataSets.getAmc_purchased_date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);


                String dates = dateFormat.format(newDate);

                holder.tv_startdate.setText(dates);


            }catch(Exception e){

                e.printStackTrace();
            }








        holder.tv_enddate.setText(dataSets.getAmc_extended_wty());
        holder.tv_product.setText(dataSets.getFg_product());
//        holder.tv_machinestatus.setText(dataSets.getMachine_status());
//        holder.tv_amcstatus.setText(dataSets.getMachine_status());


//        String product=dataSets.getProduct_name();
//
//
//        if (product.equals("AC"))
//        {
//            holder.tv_modelname.setText("Air Conditioner");
//        }
//        else  if (product.equals("CD"))
//        {
//            holder.tv_modelname.setText("Clothes Dryer");
//        }
//        else  if (product.equals("DW"))
//        {
//            holder.tv_modelname.setText("Dish Washer");
//        }
//        else  if (product.equals("IND"))
//        {
//            holder.tv_modelname.setText("Industrial Dish washer");
//        }
//        else  if (product.equals("KA"))
//        {
//            holder.tv_modelname.setText("Kitchen Appliances");
//        }
//
//        else  if (product.equals("RF"))
//        {
//            holder.tv_modelname.setText("Refrigerator");
//        }
//        else  if (product.equals("ST"))
//        {
//            holder.tv_modelname.setText("Stabilizer");
//        }
//        else  if (product.equals("WD"))
//        {
//            holder.tv_modelname.setText("Washer Dryer");
//        }
//        else  if (product.equals("WM"))
//        {
//            holder.tv_modelname.setText("Washing Machine");
//        }
//        else  if (product.equals("WP"))
//        {
//            holder.tv_modelname.setText("Water Purifier");
//        }
//        else  if (product.equals("MW"))
//        {
//            holder.tv_modelname.setText("Microwave Oven");
//        }
//
//        else {
//
//            holder.tv_modelname.setText(product);
//        }
//
//
//        //  holder.tv_modelname.setText(dataSets.getProduct());
//
//        if (dataSets.getCustphone().equals("")){
//
//            holder.iv_call.setVisibility(View.GONE);
//        }
//        holder.tv_servicetype.setText("AMC");
//
//        holder.tv_ticketno.setText(dataSets.getSerialno());
//        holder.tv_custname.setText(dataSets.getCustname());
//
//        String name =dataSets.getCustname().toLowerCase(Locale.getDefault());
//        String ticketno=dataSets.getSerialno();//.toLowerCase(Locale.getDefault());
//        String phoneno=dataSets.getCustphone();//.toLowerCase(Locale.getDefault());
//
//        if(ticketno.contains(searchString)){
//            int startPos = ticketno.indexOf(searchString);
//            int endPos = startPos + searchString.length();
//            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_ticketno.getText());
//            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            holder.tv_ticketno.setText(spanString);
//
//            if (searchString.length() == 0) {
//
//                spanString.removeSpan(holder.getForegroundColorSpan());
//                holder.tv_ticketno.setText(ticketno);
//            }
//
//        }
//
//
//        if(phoneno.contains(searchString)){
//            int startPos = phoneno.indexOf(searchString);
//            int endPos = startPos + searchString.length();
//            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_rcnno.getText());
//            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            holder.tv_rcnno.setText(spanString);
//            if (searchString.length() == 0) {
//
//                spanString.removeSpan(holder.getForegroundColorSpan());
//                holder.tv_rcnno.setText(phoneno);
//            }
//        }
//
//
//
//        if (name.contains(searchString)) {
//            int startPos = name.indexOf(searchString);
//            int endPos = startPos + searchString.length();
//            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_custname.getText());
//            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            holder.tv_custname.setText(spanString);
//            if (searchString.length() == 0) {
//
//                spanString.removeSpan(holder.getForegroundColorSpan());
//                holder.tv_custname.setText(dataSets.getCustname());
//            }
//        }

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

        TextView tv_name,tv_phone,tv_serialno,tv_startdate,tv_enddate,tv_product,
                tv_machinestatus,tv_amcstatus,tv_amc_extended_wty,tv_icrno;
        ImageView iv_call;
        CardView task_cv;

        public MyViewHolder(View view) {
            super(view);


            tv_name=view.findViewById(R.id.tv_pun_customer);
            tv_phone=view.findViewById(R.id.tv_pun_phone);

            tv_serialno=view.findViewById(R.id.tv_pun_serial);

            tv_startdate=view.findViewById(R.id.tv_pun_starttime);
            tv_enddate=view.findViewById(R.id.tv_pun_endtime);
            tv_product=view.findViewById(R.id.tv_pun_product);
            tv_machinestatus=view.findViewById(R.id.tv_pun_machinestatus);
            tv_amcstatus=view.findViewById(R.id.tv_pun_amcstatus);
            tv_amc_extended_wty=view.findViewById(R.id.tv_amc_extended_wty);
            tv_icrno=view.findViewById(R.id.tv_icrno);
            task_cv=view.findViewById(R.id.task_cv);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final Punched_amc_model dataSets=taskListFiltered.get(getAdapterPosition());


                    //  Toast.makeText(context, "Already Visited", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    String json = gson.toJson(dataSets);

                    context.startActivity(new Intent(context,Amcdetails.class)
                            .putExtra("data",json));
                    //  listener.onTaskSelected(taskListFiltered.get(getAdapterPosition()));

                }
            });
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

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                searchString=charString;
                if (charString.isEmpty()) {
                    taskListFiltered = tasklist;

                } else {
                    //searchString=charString;
                    List<Punched_amc_model> filteredList = new ArrayList<>();
                    for (Punched_amc_model row : tasklist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getSerial_no().toLowerCase().contains(charString.toLowerCase()) || row.getRcn().contains(charSequence)
                                || row.getCustomer_name().toLowerCase().contains(charString.toLowerCase()))
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
                taskListFiltered = (ArrayList<Punched_amc_model>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}

