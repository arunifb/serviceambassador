package technician.ifb.com.ifptecnician.otherticket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import technician.ifb.com.ifptecnician.DetailsActivity;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.notes.NotesActivity;
import technician.ifb.com.ifptecnician.offlinedata.Dbhelper;
import technician.ifb.com.ifptecnician.service.ErrorDetails;

public class OtherTicketAdapter extends RecyclerView.Adapter<OtherTicketAdapter.MyViewHolder>
        implements Filterable {

    String Ticktno="";
    Dbhelper dbhelper;
    Cursor cursor,del_cursor;
    SharedPreferences prefdetails;
    private List<OtherticketModel> tasklist;
    private List<OtherticketModel> taskListFiltered;
    private final LayoutInflater inflater;
    View view;
    MyViewHolder holder;
    private Context context;
    private JobsAdapterListener listener;
    private Altclick altclick;
    private Rcnclick rcnclick;
    String searchString = "";
    ErrorDetails errorDetails;

    // call to customer
    private Calltocustomer calltocustomer;


    public OtherTicketAdapter(Context context, List<OtherticketModel> tasklist, JobsAdapterListener listener, Altclick altclick, Rcnclick rcnclick, Calltocustomer calltocustomer) {
        this.context = context;
        this.listener = listener;
        this.altclick=altclick;
        this.rcnclick=rcnclick;
        this.tasklist = tasklist;
        taskListFiltered=tasklist;
        this.calltocustomer=calltocustomer;
        inflater=LayoutInflater.from(context);
        dbhelper=new Dbhelper(context);
        prefdetails=context.getSharedPreferences("details",0);
        errorDetails=new ErrorDetails();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final OtherticketModel dataSets=taskListFiltered.get(position);

        holder.tv_address.setText(dataSets.getAddress().replace(","," "));

        cursor=dbhelper.fetch_read_data(dataSets.getTicketNo());
        holder.task_cv.setCardBackgroundColor(Color.WHITE);
        holder.tv_address.setTextColor(Color.parseColor("#99000000"));
        Ticktno=dataSets.getTicketNo();

        holder.tv_delercode.setText(dataSets.getDealerName());
        String PendingReason=dataSets.getPendingReason();
        if (!PendingReason.equals("")) {
            holder.tv_pending_status.setText(dataSets.getPendingReason() + " " + dataSets.getScheduledate());
        }
        else{

            holder.tv_pending_status.setVisibility(View.GONE);
        }
        //tv_pending_status.setText(object1.getString("PendingReason") + " "+object1.getString("scheduledate"));

        System.out.println(dataSets.getStatus());

        if (cursor!=null)
        {
            if (cursor.moveToFirst()) {
                do {

                    String Status = cursor.getString(cursor.getColumnIndex("Status"));
                    String dates=cursor.getString(cursor.getColumnIndex("Date"));
                    String ChangeTime=cursor.getString(cursor.getColumnIndex("ChangeTime"));
                    String C_status=cursor.getString(cursor.getColumnIndex("C_Status"));

                    if (ChangeTime.equals(dataSets.getChangeDate())){
                        if ( C_status.equals("Resolved (soft closure)") ){

                            if (dataSets.getCallType().equals("MANDATORY CALLS") || dataSets.getCallType().equals("COURTESY VISIT"))
                            {

                            }

                            else {

                                if (!dataSets.getTelePhone().equals("")) {
                                    holder.ll_alt.setVisibility(View.VISIBLE);
                                    holder.tv_alt.setText(dataSets.getTelePhone());
                                }

                                if (!dataSets.getRCNNo().equals("")) {
                                    holder.iv_rcn.setVisibility(View.VISIBLE);
                                    holder.iv_calender.setVisibility(View.GONE);
                                }
                            }
                        }
                        holder.task_cv.setCardBackgroundColor(Color.LTGRAY);
                        holder.tv_address.setText( C_status+" | "+dates);
                        holder.tv_address.setTextColor(Color.parseColor("#b71c1c"));
                        holder.tv_address.setElegantTextHeight(true);
                        holder.iv_calender.setVisibility(View.GONE);
                    }

                    else {

                        del_cursor=dbhelper.delete_read_data(dataSets.getTicketNo());
                        dbhelper.delete_others_details(dataSets.getTicketNo());
                        holder.task_cv.setCardBackgroundColor(Color.WHITE);

                    }
                }
                while (cursor.moveToNext());
            }

        }

        holder.tv_callbook.setText(dataSets.getCallBookDate());
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));
        holder.tv_rcnno.setText(dataSets.getRCNNo());


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


        //  holder.tv_modelname.setText(dataSets.getProduct());

        if (dataSets.getRCNNo().equals("")){

            holder.iv_call.setVisibility(View.GONE);
        }
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());


//        if (dataSets.getStatus().equals("Negative Response from Custome"));{
//            holder.tv_status.setText("Negative Response");
//        }
        if (dataSets.getStatus().equals("Resolved (soft closure)")){

            holder.ll_nps.setVisibility(View.VISIBLE);
            String nps=dataSets.getScFeedbackCatValue();

            if (nps.equals("")){
                holder.iv_thum.setImageResource(R.drawable.ic_thumb_up_waiting);
                holder.tv_scfeedbackcatvalue.setText("NPS awaiting");
            }
            else {

                holder.iv_thum.setImageResource(R.drawable.ic_thumb_up_received);
                holder.tv_scfeedbackcatvalue.setText("NPS Received");
            }

        }

        if (dataSets.getStatus().equals("Closed")  )
        {

            holder.tv_address.setVisibility(View.GONE);
            holder.tv_alt.setVisibility(View.GONE);
            holder.tv_rcnno.setVisibility(View.GONE);
            holder.iv_call.setVisibility(View.GONE);


            holder.ll_nps.setVisibility(View.VISIBLE);
            String nps=dataSets.getScFeedbackCatValue();

            if (nps.equals("")){
                holder.iv_thum.setImageResource(R.drawable.ic_thumb_up_waiting);
                holder.tv_scfeedbackcatvalue.setText("NPS awaiting");
            }
            else {

                holder.iv_thum.setImageResource(R.drawable.ic_thumb_up_received);
                holder.tv_scfeedbackcatvalue.setText("NPS Received");
            }

        }



        if (dataSets.getStatus().equals("Cancelled"))
        {

            holder.tv_address.setVisibility(View.GONE);
            holder.tv_alt.setVisibility(View.GONE);
            holder.tv_rcnno.setVisibility(View.GONE);
            holder.iv_call.setVisibility(View.GONE);


            holder.ll_nps.setVisibility(View.GONE);
            String nps=dataSets.getScFeedbackCatValue();

            if (nps.equals("")){
                holder.iv_thum.setImageResource(R.drawable.ic_thumb_up_waiting);
                holder.tv_scfeedbackcatvalue.setText("NPS awaiting");
            }
            else {

                holder.iv_thum.setImageResource(R.drawable.ic_thumb_up_received);
                holder.tv_scfeedbackcatvalue.setText("NPS Received");
            }

        }


        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());

        if (!dataSets.getProblemDescription().equals("")){

            holder.ll_problem.setVisibility(View.VISIBLE);
            holder.tv_problemdescription.setText(dataSets.getProblemDescription());
        }
        else {

            holder.ll_problem.setVisibility(View.GONE);
            holder.tv_problemdescription.setVisibility(View.GONE);
        }

        holder.tv_ageing.setText(dataSets.getAgeing()+" Days");


        System.out.println(dataSets.getPriority());

        if (dataSets.getPriority().equals("")){
            holder.tv_escaltion.setText("");
        }
        else {
            holder.tv_escaltion.setText("Escalate ");
        }

        String Mediumm=dataSets.getMedium();

        if(Mediumm.equals("Phone") || Mediumm.equals("CSR Portal")  ){

            holder.item_task_ll_medium.setVisibility(View.VISIBLE);
        }

//     String exchange=dataSets.getPotential_exchange_flag();
//        if (exchange.equals("X")){
//
//            holder.ll_exchange.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.ll_exchange.setVisibility(View.GONE);
//        }

        String name =dataSets.getCustomerName().toLowerCase(Locale.getDefault());
        String ticketno=dataSets.getTicketNo();//.toLowerCase(Locale.getDefault());
        String phoneno=dataSets.getRCNNo();//.toLowerCase(Locale.getDefault());
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

        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_scfeedbackcatvalue,tv_delercode,
                tv_rcnno,tv_servicetype,tv_custname,tv_ageing,tv_escaltion,tv_problemdescription,tv_alt;
        ImageView iv_call,iv_rcn,iv_alt,iv_thum,iv_calender;
        LinearLayout ll_alt,ll_nps,item_task_ll_medium,ll_problem,ll_exchange,ll_notes;
        CardView task_cv;
        TextView tv_modelname,tv_pending_status;

        public MyViewHolder(View view) {
            super(view);
            tv_calltype=(TextView)view.findViewById(R.id.tv_calltype);
            tv_ticketno=(TextView)view.findViewById(R.id.tv_ticketno);
            tv_address=(TextView)view.findViewById(R.id.tv_address);
            tv_callbook=(TextView)view.findViewById(R.id.tv_callbookdate);
            tv_status=(TextView)view.findViewById(R.id.tv_status);
            tv_rcnno=(TextView)view.findViewById(R.id.tv_rcnno);
            tv_servicetype=(TextView)view.findViewById(R.id.tv_servicetype);
            tv_custname=(TextView)view.findViewById(R.id.tv_custname);
            iv_call=(ImageView)view.findViewById(R.id.iv_call);
            task_cv=(CardView)view.findViewById(R.id.task_cv);
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
            iv_calender=view.findViewById(R.id.iv_calender);
            tv_delercode=view.findViewById(R.id.tv_delercode);
            ll_notes=view.findViewById(R.id.ll_notes);
            tv_pending_status=view.findViewById(R.id.tv_pending_status);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    final OtherticketModel dataSets=taskListFiltered.get(getAdapterPosition());
                    String status=dataSets.getStatus();
                    System.out.println("Status"+status);

                    Boolean check=dbhelper.ISreadtaskexits(dataSets.getTicketNo());

                    if (check.equals(true)) {
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        String tss = dfs.format(c);
                        //   Errorlog("Click%20On%20Item",Ticktno,"",tss);

                        //  Toast.makeText(context, "Already Visited", Toast.LENGTH_SHORT).show();
                        Gson gson = new Gson();
                        String json = gson.toJson(dataSets);
                        System.out.println("details-->"+json);
                        SharedPreferences.Editor editor=prefdetails.edit();
                        editor.putString("details",json);

                        editor.apply();
                        context.startActivity(new Intent(context, DetailsActivity.class));
                        //  listener.onTaskSelected(taskListFiltered.get(getAdapterPosition()));
                    }

                    else{

                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat dfs = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                        String tss = dfs.format(c);
                        Gson gson = new Gson();
                        String json = gson.toJson(dataSets);
                        System.out.println(json);
                        SharedPreferences.Editor editor=prefdetails.edit();
                        editor.putString("details",json);
                        editor.apply();
                        System.out.println("details-->"+json);
                        context.startActivity(new Intent(context,OtherTicketDetails.class));

                    }


                }
            });


            ll_notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    OtherticketModel tasklistModel=taskListFiltered.get(getAdapterPosition());
                    String ticketno=tasklistModel.getTicketNo();

                    context.startActivity(new Intent(context, NotesActivity.class).putExtra("ticketno",ticketno));



                }
            });

            iv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    OtherticketModel tasklistModel=taskListFiltered.get(getAdapterPosition());

                    String rcn=tasklistModel.getRCNNo();
                    String ref=tasklistModel.getRefId();
                    String ticketno=tasklistModel.getTicketNo();
                    calltocustomer.Onclickcall(rcn,ticketno);

                }
            });

            iv_rcn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OtherticketModel tasklistModel=taskListFiltered.get(getAdapterPosition());
                    String rcn=tasklistModel.getRCNNo();
                    String ref=tasklistModel.getRefId();
                    String ticketno=tasklistModel.getTicketNo();

                    rcnclick.onRcnclick(ref,"",rcn,ticketno);
                }
            });




        }
    }


    public interface Calltocustomer{

        void Onclickcall(String mobile,String ticketno);

    }


    public interface JobsAdapterListener {
        void onTaskSelected(OtherticketModel tasklistModel);

    }

    public interface Altclick{

        void onAltclick(String refno,String smstype,String altno,String ticketno);
    }

    public interface Call{

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
                    List<OtherticketModel> filteredList = new ArrayList<>();
                    for (OtherticketModel row : tasklist) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTicketNo().toLowerCase().contains(charString.toLowerCase()) || row.getRCNNo().contains(charSequence) || row.getCallType().contains(charSequence)
                                || row.getStatus().toLowerCase().contains(charString.toLowerCase()) || row.getCustomerName().toLowerCase().contains(charString.toLowerCase()))
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
                taskListFiltered = (ArrayList<OtherticketModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
