package technician.ifb.com.ifptecnician.essentiallead;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import technician.ifb.com.ifptecnician.R;
import technician.ifb.com.ifptecnician.adapter.Essential_add_adapter;
import technician.ifb.com.ifptecnician.model.Essential_add_model;

public class Item_add_adapter extends BaseAdapter implements Filterable {

    private ArrayList<Entry_ess_model> dataSet;
    private ArrayList<Entry_ess_model> contactsSetgets;
    Context mContext;
    LayoutInflater layoutInflater;

    private ContactsFilter contactsFilter;

    public Item_add_adapter(ArrayList<Entry_ess_model> dataSet, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null)
        {
            convertView=layoutInflater.inflate(R.layout.item_essential_lead_add,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        final Entry_ess_model dataSets=dataSet.get(position);

        holder.tv_ename.setText(dataSets.getEname());
        holder.tv_ecode.setText(dataSets.getEcode());
        holder.tv_equentity.setText(dataSets.getEquentity());
        holder.tv_itemtype.setText(dataSets.getItemtype());
        holder.tv_ess_date.setText(dataSets.getDate());


        holder.tv_ess_price.setText(dataSets.getPrice());
        holder.tv_ess_disper.setText(dataSets.getDis_per());
        holder.tv_ess_discount.setText(dataSets.getDis_amount());
        holder.tv_ess_gross.setText(dataSets.getGross());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSet.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tv_ename,tv_ecode,tv_equentity,tv_itemtype,tv_ess_date,tv_ess_price,tv_ess_disper,tv_ess_discount,tv_ess_gross;
        Button btn_delete;
        public ViewHolder(View view){

            tv_ename=(TextView)view.findViewById(R.id.tv_ess_comdes);
            tv_ecode=(TextView)view.findViewById(R.id.tv_ess_com);
            tv_equentity=(TextView)view.findViewById(R.id.tv_ess_quentity);
            btn_delete=(Button) view.findViewById(R.id.btn_ess_delete);
            tv_itemtype=view.findViewById(R.id.tv_itemtype);
            tv_ess_date=view.findViewById(R.id.tv_ess_date);

            tv_ess_price=view.findViewById(R.id.tv_ess_price);
            tv_ess_disper=view.findViewById(R.id.tv_ess_disper);
            tv_ess_discount=view.findViewById(R.id.tv_ess_discount);
            tv_ess_gross=view.findViewById(R.id.tv_ess_gross);

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
                ArrayList<Entry_ess_model> templist=new ArrayList<>();

                for(Entry_ess_model contactsSetget: dataSet){

                    if(contactsSetget.getEname().toLowerCase().contains(constraint.toString().toLowerCase()))
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

            contactsSetgets = (ArrayList<Entry_ess_model>) results.values;
            notifyDataSetChanged();
        }
    }
}


