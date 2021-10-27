package technician.ifb.com.ifptecnician.fragment.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.fragment.dummy.Taskmodel;

public class Taskadapter extends BaseAdapter implements Filterable {

    private ArrayList<Taskmodel> dataSet;
    private ArrayList<Taskmodel> contactsSetgets;
    Context mContext;
    LayoutInflater layoutInflater;

    private ContactsFilter contactsFilter;

    public Taskadapter(ArrayList<Taskmodel> dataSet, Context context) {
        // super(context, R.layout.item_leads_list, data);
        this.dataSet = dataSet;
        this.contactsSetgets=dataSet;
        this.mContext=context;
        layoutInflater=LayoutInflater.from(this.mContext);

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null)
        {
            convertView=layoutInflater.inflate(R.layout.item_task,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        final Taskmodel dataSets=dataSet.get(position);


       // Toast.makeText(mContext, dataSets.getCallType().charAt(0), Toast.LENGTH_SHORT).show();



        holder.tv_address.setText(dataSets.getAddress().replace(","," "));
        holder.tv_callbook.setText(dataSets.getCallBookDate());
        holder.tv_calltype.setText(dataSets.getCallType().substring(0,1));

//        GradientDrawable bgShape = (GradientDrawable)holder.tv_calltype.getBackground();
//       if(dataSets.getCallType().substring(0,1).equals("M"))
//       {
//
//           bgShape.setColor(Color.RED);
//       }
//
//       else {
//
//           bgShape.setColor(Color.GREEN);
//       }

        holder.tv_rcnno.setText(dataSets.getRCNNo());
        holder.tv_servicetype.setText(dataSets.getServiceType());
        holder.tv_status.setText(dataSets.getStatus());
        holder.tv_ticketno.setText(dataSets.getTicketNo());
        holder.tv_custname.setText(dataSets.getCustomerName());

        if(dataSets.getPriority().equals("")){

            holder.iv_priority.setImageResource(R.drawable.star_unfill);
        }
        else{

            holder.iv_priority.setImageResource(R.drawable.ic_star_black_24dp);
        }

        holder.iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
//                }
//                else
//                {
//                    Intent callIntent = new Intent(Intent.ACTION_CALL);
//                    callIntent.setData(Uri.parse("tel:"+dataSets.getRCNNo()));
//                    mContext.startActivity(callIntent);
//
//                }

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + dataSets.getRCNNo()));// Initiates the Intent
                mContext.startActivity(intent);
            }
        });



         return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tv_calltype,tv_ticketno,tv_address,tv_callbook,tv_status,tv_rcnno,tv_servicetype,tv_custname;
        ImageView iv_priority,iv_call;
        public ViewHolder(View view){
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
        }
    }

    @Override
    public Filter getFilter() {

        if ( contactsFilter == null)
        {
            contactsFilter=new ContactsFilter();
        }
        return contactsFilter;
    }

    public class ContactsFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults=new FilterResults();

            if(constraint!=null && constraint.length()>0)
            {
                ArrayList<Taskmodel> templist=new ArrayList<>();

                for(Taskmodel contactsSetget: dataSet){

                    if(contactsSetget.getAddress().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        templist.add(contactsSetget);
                    }
                }

                filterResults.count=templist.size();
                filterResults.values=templist;
            }
            else {

                filterResults.count=dataSet.size();
                filterResults.values=dataSet;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            contactsSetgets = (ArrayList<Taskmodel>) results.values;
            notifyDataSetChanged();
        }
    }
}


