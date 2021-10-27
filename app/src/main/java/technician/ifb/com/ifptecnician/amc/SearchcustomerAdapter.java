package technician.ifb.com.ifptecnician.amc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.service.ErrorDetails;

public class SearchcustomerAdapter extends RecyclerView.Adapter<SearchcustomerAdapter.MyViewHolder>

        implements Filterable {

    String Ticktno="";
    SharedPreferences prefdetails;
    private List<SearchCustomerModel> tasklist;
    private List<SearchCustomerModel> taskListFiltered;
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

    public SearchcustomerAdapter(Context context, List<SearchCustomerModel> tasklist,JobsAdapterListener listener, Altclick altclick, Rcnclick rcnclick) {
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
                .inflate(R.layout.item_amc, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final SearchCustomerModel dataSets=taskListFiltered.get(position);

        holder.tv_address.setText(dataSets.getZzstr_suppl1().replace(","," "));
        holder.task_cv.setCardBackgroundColor(Color.WHITE);
        holder.tv_address.setTextColor(Color.parseColor("#99000000"));
        holder.tv_calltype.setText("A");
        String product=dataSets.getZz0012();

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


        //  holder.tv_modelname.setText(dataSets.getProduct());

        if (dataSets.getZztel_number().equals("")){

            holder.iv_call.setVisibility(View.GONE);
        }
        holder.tv_servicetype.setText("AMC");

        holder.tv_ticketno.setText(dataSets.getZzr3ser_no());
        holder.tv_custname.setText(dataSets.getZzname_org1()+" "+dataSets.getZzname_org2());

        String name =dataSets.getZzname_org1().toLowerCase(Locale.getDefault());
        String ticketno=dataSets.getZzr3ser_no();//.toLowerCase(Locale.getDefault());
        String phoneno=dataSets.getZztel_number();//.toLowerCase(Locale.getDefault());

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



        if (name.contains(searchString)) {
            int startPos = name.indexOf(searchString);
            int endPos = startPos + searchString.length();
            Spannable spanString = Spannable.Factory.getInstance().newSpannable(holder.tv_custname.getText());
            spanString.setSpan(new ForegroundColorSpan(Color.RED), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_custname.setText(spanString);
            if (searchString.length() == 0) {

                spanString.removeSpan(holder.getForegroundColorSpan());
                holder.tv_custname.setText(dataSets.getZzname_org1());
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

        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_scfeedbackcatvalue,
                tv_rcnno,tv_servicetype,tv_custname,tv_ageing,tv_escaltion,tv_problemdescription,tv_alt;
        ImageView iv_call,iv_rcn,iv_alt,iv_thum;
        LinearLayout ll_alt,ll_nps,item_task_ll_medium,ll_problem,ll_exchange;
        CardView task_cv;
        TextView tv_modelname;

        public MyViewHolder(View view) {
            super(view);
            tv_calltype=view.findViewById(R.id.tv_calltype);
            tv_ticketno=view.findViewById(R.id.tv_ticketno);
            tv_address=view.findViewById(R.id.tv_address);
            tv_callbook=view.findViewById(R.id.tv_callbookdate);
            tv_status=view.findViewById(R.id.tv_status);
            tv_rcnno=view.findViewById(R.id.tv_rcnno);
            tv_servicetype=view.findViewById(R.id.tv_servicetype);
            tv_custname=view.findViewById(R.id.tv_custname);
            iv_call=view.findViewById(R.id.iv_call);
            task_cv=view.findViewById(R.id.task_cv);
            tv_ageing=view.findViewById(R.id.tv_ageing);
            tv_escaltion=view.findViewById(R.id.tv_escaltion);
            tv_problemdescription=view.findViewById(R.id.tv_problemdescription);
            iv_alt=view.findViewById(R.id.alt_iv_message);
            tv_alt=view.findViewById(R.id.tv_altno);
            iv_rcn=view.findViewById(R.id.rcn_iv_message);
            ll_alt=view.findViewById(R.id.ll_altview);
            iv_thum=view.findViewById(R.id.iv_thum);
            tv_scfeedbackcatvalue=view.findViewById(R.id.tv_scfeedbackcatvalue);
            ll_nps=view.findViewById(R.id.ll_nps);
            item_task_ll_medium=view.findViewById(R.id.item_task_ll_medium);
            tv_modelname=view.findViewById(R.id.tv_modelname);
            ll_problem=view.findViewById(R.id.ll_problem);
            ll_exchange=view.findViewById(R.id.ll_exchange);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final SearchCustomerModel dataSets=taskListFiltered.get(getAdapterPosition());


                    //  Toast.makeText(context, "Already Visited", Toast.LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    String json = gson.toJson(dataSets);
                    System.out.println("details-->"+json);
                    SharedPreferences.Editor editor=prefdetails.edit();
                    editor.putString("amcdetails",json);
                    editor.commit();
                    context.startActivity(new Intent(context,Create_customer.class));
                    //  listener.onTaskSelected(taskListFiltered.get(getAdapterPosition()));

                }
            });

            iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SearchCustomerModel tasklistModel=taskListFiltered.get(getAdapterPosition());

                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tasklistModel.getZztel_number()));// Initiates the Intent
                    context.startActivity(intent);
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
                    List<SearchCustomerModel> filteredList = new ArrayList<>();
                    for (SearchCustomerModel row : tasklist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getZzr3ser_no().toLowerCase().contains(charString.toLowerCase()) || row.getZztel_number().contains(charSequence)
                                || row.getZzname_org1().toLowerCase().contains(charString.toLowerCase()))
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
                taskListFiltered = (ArrayList<SearchCustomerModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
